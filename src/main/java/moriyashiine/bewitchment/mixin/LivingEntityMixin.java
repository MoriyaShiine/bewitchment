package moriyashiine.bewitchment.mixin;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.api.interfaces.BloodAccessor;
import moriyashiine.bewitchment.api.interfaces.ContractAccessor;
import moriyashiine.bewitchment.api.registry.AthameDropRecipe;
import moriyashiine.bewitchment.api.registry.Contract;
import moriyashiine.bewitchment.client.network.packet.SpawnExplosionParticlesPacket;
import moriyashiine.bewitchment.common.item.tool.AthameItem;
import moriyashiine.bewitchment.common.registry.*;
import net.fabricmc.fabric.api.server.PlayerStream;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.EntityDamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.tag.EntityTypeTags;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ConstantConditions")
@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements BloodAccessor, ContractAccessor {
	private static final TrackedData<Integer> BLOOD = DataTracker.registerData(LivingEntity.class, TrackedDataHandlerRegistry.INTEGER);
	
	private final List<Contract.Instance> contracts = new ArrayList<>();
	
	@Shadow
	@Nullable
	public abstract StatusEffectInstance getStatusEffect(StatusEffect effect);
	
	@Shadow
	public abstract boolean hasStatusEffect(StatusEffect effect);
	
	@Shadow
	public abstract boolean addStatusEffect(StatusEffectInstance effect);
	
	@Shadow
	public abstract boolean removeStatusEffect(StatusEffect type);
	
	@Shadow
	public abstract float getMaxHealth();
	
	@Shadow
	public abstract float getHealth();
	
	@Shadow
	public abstract void heal(float amount);
	
	public LivingEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}
	
	@Override
	public int getBlood() {
		return BWTags.HAS_BLOOD.contains(getType()) ? dataTracker.get(BLOOD) : 0;
	}
	
	@Override
	public void setBlood(int blood) {
		if (BWTags.HAS_BLOOD.contains(getType())) {
			dataTracker.set(BLOOD, blood);
		}
	}
	
	@Override
	public List<Contract.Instance> getContracts() {
		return contracts;
	}
	
	@Override
	public boolean hasNegativeEffects() {
		return true;
	}
	
	@ModifyVariable(method = "applyDamage", at = @At(value = "INVOKE", shift = At.Shift.BEFORE, ordinal = 0, target = "Lnet/minecraft/entity/LivingEntity;getHealth()F"))
	private float applyDamage(float amount, DamageSource source) {
		if (BewitchmentAPI.isWeakToSilver((LivingEntity) (Object) this)) {
			if (BewitchmentAPI.isSourceFromSilver(source)) {
				return amount + 4;
			}
		}
		return amount;
	}
	
	@ModifyVariable(method = "damage", at = @At("HEAD"))
	private float damage(float amount, DamageSource source) {
		Entity attacker = source.getSource();
		if (attacker instanceof LivingEntity && ((LivingEntity) attacker).hasStatusEffect(BWStatusEffects.ENCHANTED)) {
			amount /= 4 + ((LivingEntity) attacker).getStatusEffect(BWStatusEffects.ENCHANTED).getAmplifier();
		}
		if (hasStatusEffect(BWStatusEffects.MAGIC_RESISTANCE) && source.getMagic()) {
			amount *= (1 - (0.2f * (getStatusEffect(BWStatusEffects.MAGIC_RESISTANCE).getAmplifier() + 1)));
		}
		if (!(source instanceof EntityDamageSource && ((EntityDamageSource) source).isThorns())) {
			if (attacker instanceof LivingEntity) {
				LivingEntity livingAttacker = (LivingEntity) attacker;
				if (BewitchmentAPI.isWeakToSilver(livingAttacker)) {
					int armorPieces = BewitchmentAPI.getArmorPieces(livingAttacker, stack -> BWTags.SILVER_ARMOR.contains(stack.getItem()));
					if (armorPieces > 0) {
						attacker.damage(DamageSource.thorns(this), armorPieces);
					}
					amount *= (1 - (0.125f * armorPieces));
				}
			}
		}
		if (((Object) this instanceof PlayerEntity) && hasContract(BWContracts.FAMINE)) {
			amount *= (1 - (0.025 * (20 - ((PlayerEntity) (Object) this).getHungerManager().getFoodLevel())));
		}
		if (attacker instanceof PlayerEntity && ((ContractAccessor) attacker).hasNegativeEffects() && ((ContractAccessor) attacker).hasContract(BWContracts.FAMINE)) {
			amount *= (1 - (0.025 * (20 - ((PlayerEntity) attacker).getHungerManager().getFoodLevel())));
		}
		if (attacker instanceof LivingEntity && ((ContractAccessor) attacker).hasContract(BWContracts.WRATH)) {
			amount *= (1 + (0.025 * (((LivingEntity) attacker).getMaxHealth() - ((LivingEntity) attacker).getHealth())));
		}
		if (hasNegativeEffects() && hasContract(BWContracts.WRATH)) {
			amount *= (1 + (0.025 * (getMaxHealth() - getHealth())));
		}
		return amount;
	}
	
	@ModifyVariable(method = "damage", at = @At("HEAD"))
	private DamageSource modifyDamageSource(DamageSource source) {
		Entity attacker = source.getSource();
		if (attacker instanceof LivingEntity && ((LivingEntity) attacker).hasStatusEffect(BWStatusEffects.ENCHANTED)) {
			return DamageSource.MAGIC;
		}
		return source;
	}
	
	@Inject(method = "tick", at = @At("HEAD"))
	private void tick(CallbackInfo callbackInfo) {
		if (!world.isClient && (Object) this instanceof LivingEntity) {
			LivingEntity livingEntity = (LivingEntity) (Object) this;
			if (BewitchmentAPI.isWeakToSilver(livingEntity)) {
				int damage = BewitchmentAPI.getArmorPieces(livingEntity, stack -> BWTags.SILVER_ARMOR.contains(stack.getItem()));
				if (BewitchmentAPI.isHoldingSilver(livingEntity, Hand.MAIN_HAND)) {
					damage++;
				}
				if (BewitchmentAPI.isHoldingSilver(livingEntity, Hand.OFF_HAND)) {
					damage++;
				}
				if (damage > 0) {
					damage(DamageSource.MAGIC, damage);
				}
			}
			for (int i = contracts.size() - 1; i >= 0; i--) {
				Contract.Instance instance = contracts.get(i);
				instance.contract.tick(livingEntity, hasNegativeEffects());
				instance.duration--;
				if (instance.duration <= 0) {
					contracts.remove(i);
				}
			}
		}
	}
	
	@Inject(method = "damage", at = @At("HEAD"), cancellable = true)
	private void damage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> callbackInfo) {
		Entity attacker = source.getSource();
		if (source != DamageSource.OUT_OF_WORLD && (hasStatusEffect(BWStatusEffects.ETHEREAL) || (attacker instanceof LivingEntity && ((LivingEntity) attacker).hasStatusEffect(BWStatusEffects.ETHEREAL)))) {
			callbackInfo.setReturnValue(false);
		}
		else if (hasStatusEffect(BWStatusEffects.DEFLECTION) && attacker != null && EntityTypeTags.ARROWS.contains(attacker.getType())) {
			int amplifier = getStatusEffect(BWStatusEffects.DEFLECTION).getAmplifier() + 1;
			Vec3d velocity = attacker.getVelocity();
			attacker.setVelocity(velocity.getX() * 2 * amplifier, velocity.getY() * 2 * amplifier, velocity.getZ() * 2 * amplifier);
			callbackInfo.setReturnValue(false);
		}
		else {
			if (amount > 0 && hasStatusEffect(BWStatusEffects.LEECHING)) {
				heal(amount * (getStatusEffect(BWStatusEffects.LEECHING).getAmplifier() + 1) / 4);
			}
			if (amount > 0 && hasStatusEffect(BWStatusEffects.THORNS) && !(source instanceof EntityDamageSource && ((EntityDamageSource) source).isThorns())) {
				attacker.damage(DamageSource.thorns(attacker), 2 * (getStatusEffect(BWStatusEffects.THORNS).getAmplifier() + 1));
			}
			if (amount > 0 && hasStatusEffect(BWStatusEffects.VOLATILITY) && !source.isExplosive()) {
				for (LivingEntity entity : world.getEntitiesByClass(LivingEntity.class, getBoundingBox().expand(3), livingEntity -> true)) {
					entity.damage(DamageSource.explosion(((LivingEntity) (Object) this)), 4 * (getStatusEffect(BWStatusEffects.VOLATILITY).getAmplifier() + 1));
				}
				world.playSound(null, getBlockPos(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.NEUTRAL, 1, 1);
				PlayerStream.watching(this).forEach(playerEntity -> SpawnExplosionParticlesPacket.send(playerEntity, this));
				if (((Object) this) instanceof PlayerEntity) {
					SpawnExplosionParticlesPacket.send((PlayerEntity) (Object) this, this);
				}
				removeStatusEffect(BWStatusEffects.VOLATILITY);
			}
		}
		if (amount > 0 && attacker instanceof ContractAccessor && ((ContractAccessor) attacker).hasContract(BWContracts.PESTILENCE)) {
			addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, 100));
			addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 100));
		}
	}
	
	@Inject(method = "canBreatheInWater", at = @At("HEAD"), cancellable = true)
	private void canBreatheInWater(CallbackInfoReturnable<Boolean> callbackInfo) {
		if (hasStatusEffect(BWStatusEffects.GILLS)) {
			callbackInfo.setReturnValue(true);
		}
	}
	
	@Inject(method = "drop", at = @At("HEAD"))
	private void drop(DamageSource source, CallbackInfo callbackInfo) {
		if (!world.isClient) {
			Entity attacker = source.getSource();
			if (attacker instanceof LivingEntity) {
				LivingEntity livingAttacker = (LivingEntity) attacker;
				ItemStack stack = livingAttacker.getMainHandStack();
				if (stack.getItem() instanceof AthameItem && livingAttacker.preferredHand == Hand.MAIN_HAND) {
					for (AthameDropRecipe recipe : world.getRecipeManager().listAllOfType(BWRecipeTypes.ATHAME_DROP_RECIPE_TYPE)) {
						if (recipe.entity_type.equals(getType()) && world.random.nextFloat() < recipe.chance * (EnchantmentHelper.getLevel(Enchantments.LOOTING, stack) + 1)) {
							ItemStack drop = recipe.getOutput().copy();
							if (recipe.entity_type == EntityType.PLAYER) {
								drop.getOrCreateTag().putString("SkullOwner", getName().getString());
							}
							world.spawnEntity(new ItemEntity(world, getX() + 0.5, getY() + 0.5, getZ() + 0.5, drop));
						}
					}
					if (livingAttacker.getOffHandStack().getItem() == Items.GLASS_BOTTLE && getBlood() > 30) {
						world.playSound(null, attacker.getBlockPos(), SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.PLAYERS, 1, 0.5f);
						ItemStack bloodBottle = new ItemStack(BWObjects.BOTTLE_OF_BLOOD);
						bloodBottle.getOrCreateTag().putUuid("OwnerUUID", getUuid());
						bloodBottle.getOrCreateTag().putString("OwnerName", getDisplayName().getString());
						BewitchmentAPI.addItemToInventoryAndConsume(livingAttacker, Hand.OFF_HAND, bloodBottle);
					}
				}
			}
		}
	}
	
	@Inject(method = "isClimbing", at = @At("HEAD"), cancellable = true)
	private void isClimbing(CallbackInfoReturnable<Boolean> callbackInfo) {
		if (hasStatusEffect(BWStatusEffects.CLIMBING) && horizontalCollision) {
			callbackInfo.setReturnValue(true);
		}
	}
	
	@Inject(method = "readCustomDataFromTag", at = @At("TAIL"))
	private void readCustomDataFromTag(CompoundTag tag, CallbackInfo callbackInfo) {
		if (BWTags.HAS_BLOOD.contains(getType())) {
			setBlood(tag.getInt("Blood"));
		}
		ListTag contracts = tag.getList("Contracts", NbtType.COMPOUND);
		for (int i = 0; i < contracts.size(); i++) {
			CompoundTag contract = contracts.getCompound(i);
			addContract(new Contract.Instance(BWRegistries.CONTRACTS.get(new Identifier(contract.getString("Contract"))), contract.getInt("Duration")));
		}
	}
	
	@Inject(method = "writeCustomDataToTag", at = @At("TAIL"))
	private void writeCustomDataToTag(CompoundTag tag, CallbackInfo callbackInfo) {
		if (BWTags.HAS_BLOOD.contains(getType())) {
			tag.putInt("Blood", getBlood());
		}
		ListTag contracts = new ListTag();
		for (Contract.Instance instance : getContracts()) {
			CompoundTag contractTag = new CompoundTag();
			contractTag.putString("Contract", BWRegistries.CONTRACTS.getId(instance.contract).toString());
			contractTag.putInt("Duration", instance.duration);
			contracts.add(contractTag);
		}
		tag.put("Contracts", contracts);
	}
	
	@Inject(method = "initDataTracker", at = @At("TAIL"))
	private void initDataTracker(CallbackInfo callbackInfo) {
		if (BWTags.HAS_BLOOD.contains(getType())) {
			dataTracker.startTracking(BLOOD, MAX_BLOOD);
		}
	}
}
