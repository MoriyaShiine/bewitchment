package moriyashiine.bewitchment.mixin.contract;

import moriyashiine.bewitchment.api.interfaces.entity.ContractAccessor;
import moriyashiine.bewitchment.api.registry.Contract;
import moriyashiine.bewitchment.common.registry.BWContracts;
import moriyashiine.bewitchment.common.registry.BWRegistries;
import moriyashiine.bewitchment.common.registry.BWStatusEffects;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
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
	
	@Inject(method = "tick", at = @At("TAIL"))
	private void tick(CallbackInfo callbackInfo) {
		if (!world.isClient) {
			int level = 0;
			for (int i = contracts.size() - 1; i >= 0; i--) {
				Contract.Instance instance = contracts.get(i);
				level += instance.cost;
				instance.contract.tick((PlayerEntity) (Object) this);
				instance.duration--;
				if (instance.duration <= 0) {
					contracts.remove(i);
				}
			}
			if (level > 0) {
				addStatusEffect(new StatusEffectInstance(BWStatusEffects.PACT, 10, level - 1, true, false));
			}
		}
	}
	
	@ModifyVariable(method = "addExperience", at = @At("HEAD"))
	private int modifyAddExperience(int experience) {
		if (hasContract(BWContracts.PRIDE)) {
			experience *= 2;
		}
		return experience;
	}
	
	@Inject(method = "eatFood", at = @At("HEAD"))
	private void eatFood(World world, ItemStack stack, CallbackInfoReturnable<ItemStack> callbackInfo) {
		if (!world.isClient) {
			FoodComponent foodComponent = stack.getItem().getFoodComponent();
			if (foodComponent != null && hasContract(BWContracts.GLUTTONY)) {
				getHungerManager().add(foodComponent.getHunger(), foodComponent.getSaturationModifier());
			}
		}
	}
	
	@Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
	private void readCustomDataFromNbt(NbtCompound nbt, CallbackInfo callbackInfo) {
		NbtList contracts = nbt.getList("Contracts", NbtType.COMPOUND);
		for (int i = 0; i < contracts.size(); i++) {
			NbtCompound contract = contracts.getCompound(i);
			addContract(new Contract.Instance(BWRegistries.CONTRACTS.get(new Identifier(contract.getString("Contract"))), contract.getInt("Duration"), contract.getInt("Cost")));
		}
	}
	
	@Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
	private void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo callbackInfo) {
		nbt.put("Contracts", toNbtContract());
	}
}
