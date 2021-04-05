package moriyashiine.bewitchment.mixin.contract;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.api.interfaces.entity.ContractAccessor;
import moriyashiine.bewitchment.api.registry.Contract;
import moriyashiine.bewitchment.common.registry.*;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements ContractAccessor {
	private final List<Contract.Instance> contracts = new ArrayList<>();
	
	@Shadow
	public abstract HungerManager getHungerManager();
	
	@Shadow
	public abstract boolean damage(DamageSource source, float amount);
	
	protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}
	
	@Override
	public List<Contract.Instance> getContracts() {
		return contracts;
	}
	
	@Override
	public boolean hasNegativeEffects() {
		return !(BewitchmentAPI.isPledged((PlayerEntity) (Object) this, BWPledges.BAPHOMET));
	}
	
	@Inject(method = "tick", at = @At("TAIL"))
	private void tick(CallbackInfo callbackInfo) {
		if (!world.isClient) {
			int level = 0;
			for (int i = contracts.size() - 1; i >= 0; i--) {
				Contract.Instance instance = contracts.get(i);
				level += instance.cost;
				instance.contract.tick(this, hasNegativeEffects());
				instance.duration--;
				if (instance.duration <= 0) {
					contracts.remove(i);
				}
			}
			if (level > 0) {
				addStatusEffect(new StatusEffectInstance(BWStatusEffects.PACT, 10, level - 1, true, false));
				if (getHealth() > getMaxHealth()) {
					setHealth(getMaxHealth());
				}
				if (level >= defaultMaxHealth) {
					contracts.clear();
					damage(BWDamageSources.DEATH, Float.MAX_VALUE);
				}
			}
		}
	}
	
	@ModifyVariable(method = "addExhaustion", at = @At("HEAD"))
	private float modifyExhaustion(float exhaustion) {
		if (!world.isClient && hasNegativeEffects() && hasContract(BWContracts.GLUTTONY)) {
			exhaustion *= 1.5f;
		}
		return exhaustion;
	}
	
	@ModifyVariable(method = "addExperience", at = @At("HEAD"))
	private int modifyAddExperience(int experience) {
		if (hasContract(BWContracts.PRIDE)) {
			experience *= 2;
		}
		return experience;
	}
	
	@Inject(method = "addExperience", at = @At(value = "INVOKE", ordinal = 2, target = "Lnet/minecraft/entity/player/PlayerEntity;addExperienceLevels(I)V"))
	private void addExperience(int experience, CallbackInfo callbackInfo) {
		if (!world.isClient && hasNegativeEffects() && hasContract(BWContracts.PRIDE)) {
			damage(DamageSource.explosion(this), getMaxHealth() / 2);
			world.createExplosion(this, getX(), getY(), getZ(), 2, Explosion.DestructionType.NONE);
		}
	}
	
	@Inject(method = "eatFood", at = @At("HEAD"))
	private void eatFood(World world, ItemStack stack, CallbackInfoReturnable<ItemStack> callbackInfo) {
		if (!world.isClient) {
			FoodComponent foodComponent = stack.getItem().getFoodComponent();
			if (foodComponent != null) {
				if (hasContract(BWContracts.GLUTTONY)) {
					getHungerManager().add(foodComponent.getHunger(), foodComponent.getSaturationModifier());
				}
			}
		}
	}
	
	@Inject(method = "readCustomDataFromTag", at = @At("TAIL"))
	private void readCustomDataFromTag(CompoundTag tag, CallbackInfo callbackInfo) {
		ListTag contracts = tag.getList("Contracts", NbtType.COMPOUND);
		for (int i = 0; i < contracts.size(); i++) {
			CompoundTag contract = contracts.getCompound(i);
			addContract(new Contract.Instance(BWRegistries.CONTRACTS.get(new Identifier(contract.getString("Contract"))), contract.getInt("Duration"), contract.getInt("Cost")));
		}
	}
	
	@Inject(method = "writeCustomDataToTag", at = @At("TAIL"))
	private void writeCustomDataToTag(CompoundTag tag, CallbackInfo callbackInfo) {
		tag.put("Contracts", toTagContract());
	}
}
