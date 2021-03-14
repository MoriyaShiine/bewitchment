package moriyashiine.bewitchment.mixin.contract;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.api.interfaces.entity.ContractAccessor;
import moriyashiine.bewitchment.api.registry.Contract;
import moriyashiine.bewitchment.common.registry.BWContracts;
import moriyashiine.bewitchment.common.registry.BWPledges;
import moriyashiine.bewitchment.common.registry.BWRegistries;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ConstantConditions")
@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements ContractAccessor {
	private final List<Contract.Instance> contracts = new ArrayList<>();
	
	@Shadow
	public abstract boolean addStatusEffect(StatusEffectInstance effect);
	
	@Shadow
	public abstract float getHealth();
	
	@Shadow
	public abstract float getMaxHealth();
	
	public LivingEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}
	
	@Override
	public List<Contract.Instance> getContracts() {
		return contracts;
	}
	
	@Override
	public boolean hasNegativeEffects() {
		return !world.isClient && !BewitchmentAPI.isPledged(world, BWPledges.BAPHOMET, getUuid());
	}
	
	@Inject(method = "tick", at = @At("TAIL"))
	private void tick(CallbackInfo callbackInfo) {
		if (!world.isClient) {
			for (int i = contracts.size() - 1; i >= 0; i--) {
				Contract.Instance instance = contracts.get(i);
				instance.contract.tick((LivingEntity) (Object) this, hasNegativeEffects());
				instance.duration--;
				if (instance.duration <= 0) {
					contracts.remove(i);
				}
			}
		}
	}
	
	@ModifyVariable(method = "applyArmorToDamage", at = @At("HEAD"))
	private float modifyDamage(float amount, DamageSource source) {
		if (!world.isClient) {
			Entity directSource = source.getSource();
			if (((Object) this instanceof PlayerEntity) && hasContract(BWContracts.FAMINE)) {
				amount *= (1 - (0.025f * (20 - ((PlayerEntity) (Object) this).getHungerManager().getFoodLevel())));
			}
			if (directSource instanceof PlayerEntity && ((ContractAccessor) directSource).hasNegativeEffects() && ((ContractAccessor) directSource).hasContract(BWContracts.FAMINE)) {
				amount *= (1 - (0.025f * (20 - ((PlayerEntity) directSource).getHungerManager().getFoodLevel())));
			}
			if (directSource instanceof LivingEntity && ((ContractAccessor) directSource).hasContract(BWContracts.WRATH)) {
				amount *= (1 + (0.025f * (((LivingEntity) directSource).getMaxHealth() - ((LivingEntity) directSource).getHealth())));
			}
			if (hasNegativeEffects() && hasContract(BWContracts.WRATH)) {
				amount *= (1 + (0.025f * (getMaxHealth() - getHealth())));
			}
			if (directSource instanceof ContractAccessor && ((ContractAccessor) directSource).hasContract(BWContracts.PESTILENCE)) {
				addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, 100));
				addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 100));
			}
		}
		return amount;
	}
	
	@Inject(method = "readCustomDataFromTag", at = @At("TAIL"))
	private void readCustomDataFromTag(CompoundTag tag, CallbackInfo callbackInfo) {
		ListTag contracts = tag.getList("Contracts", NbtType.COMPOUND);
		for (int i = 0; i < contracts.size(); i++) {
			CompoundTag contract = contracts.getCompound(i);
			addContract(new Contract.Instance(BWRegistries.CONTRACTS.get(new Identifier(contract.getString("Contract"))), contract.getInt("Duration")));
		}
	}
	
	@Inject(method = "writeCustomDataToTag", at = @At("TAIL"))
	private void writeCustomDataToTag(CompoundTag tag, CallbackInfo callbackInfo) {
		tag.put("Contracts", toTagContract());
	}
}
