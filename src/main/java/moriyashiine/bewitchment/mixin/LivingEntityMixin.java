package moriyashiine.bewitchment.mixin;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.api.interfaces.*;
import moriyashiine.bewitchment.api.registry.Contract;
import moriyashiine.bewitchment.api.registry.Curse;
import moriyashiine.bewitchment.client.network.packet.SpawnExplosionParticlesPacket;
import moriyashiine.bewitchment.common.block.entity.BrazierBlockEntity;
import moriyashiine.bewitchment.common.block.entity.GlyphBlockEntity;
import moriyashiine.bewitchment.common.block.entity.SigilBlockEntity;
import moriyashiine.bewitchment.common.item.AthameItem;
import moriyashiine.bewitchment.common.recipe.AthameDropRecipe;
import moriyashiine.bewitchment.common.recipe.IncenseRecipe;
import moriyashiine.bewitchment.common.registry.*;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.EntityDamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.tag.EntityTypeTags;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.BlockPos;
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
public abstract class LivingEntityMixin extends Entity implements BloodAccessor, CurseAccessor, ContractAccessor {
	private static final TrackedData<Integer> BLOOD = DataTracker.registerData(LivingEntity.class, TrackedDataHandlerRegistry.INTEGER);
	
	private final List<Curse.Instance> curses = new ArrayList<>();
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
	protected abstract boolean shouldDropLoot();
	
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
	public List<Curse.Instance> getCurses() {
		return curses;
	}
	
	@Override
	public List<Contract.Instance> getContracts() {
		return contracts;
	}
	
	@Override
	public boolean hasNegativeEffects() {
		return !world.isClient && !BewitchmentAPI.isPledged(world, BWPledges.BAPHOMET_UUID, getUuid());
	}
	
	@ModifyVariable(method = "addStatusEffect", at = @At("HEAD"))
	private StatusEffectInstance modifyStatusEffect(StatusEffectInstance effect) {
		if (hasCurse(BWCurses.COMPROMISED) && ((StatusEffectAccessor) effect.getEffectType()).bw_getType() == StatusEffectType.HARMFUL) {
			return new StatusEffectInstance(effect.getEffectType(), effect.getDuration(), effect.getAmplifier() + 1);
		}
		return effect;
	}
	
	@ModifyVariable(method = "applyDamage", at = @At(value = "INVOKE", shift = At.Shift.BEFORE, target = "Lnet/minecraft/entity/LivingEntity;getHealth()F"))
	private float modifyDamage0(float amount, DamageSource source) {
		if (BewitchmentAPI.isWeakToSilver((LivingEntity) (Object) this)) {
			if (BewitchmentAPI.isSourceFromSilver(source)) {
				return amount + 4;
			}
		}
		return amount;
	}
	
	@ModifyVariable(method = "applyArmorToDamage", at = @At("HEAD"))
	private float modifyDamage1(float amount, DamageSource source) {
		Entity attacker = source.getAttacker();
		if (amount > 0 && (Object) this instanceof PlayerEntity) {
			ItemStack poppet = BewitchmentAPI.getPoppet(world, BWObjects.VAMPIRIC_POPPET, null, (PlayerEntity) (Object) this);
			if (!poppet.isEmpty()) {
				if (BewitchmentAPI.getTaglockOwner(world, poppet).damage(BWDamageSources.VAMPIRE, amount) && poppet.damage((int) amount, random, null) && poppet.getDamage() >= poppet.getMaxDamage()) {
					poppet.decrement(1);
				}
				return 0;
			}
		}
		if (amount > 0 && attacker instanceof FortuneAccessor && ((FortuneAccessor) attacker).getFortune() != null && ((FortuneAccessor) attacker).getFortune().fortune == BWFortunes.HAWKEYE && source.isProjectile()) {
			((FortuneAccessor) attacker).getFortune().duration = 0;
			amount *= 3;
		}
		attacker = source.getSource();
		if (source.isFire() || source == DamageSource.DROWN || source == DamageSource.FALL || source == DamageSource.FLY_INTO_WALL) {
			ItemStack poppet = BewitchmentAPI.getPoppet(world, BWObjects.PROTECTION_POPPET, this, null);
			if (!poppet.isEmpty()) {
				if (poppet.damage((int) amount, random, null) && poppet.getDamage() >= poppet.getMaxDamage()) {
					poppet.decrement(1);
				}
				return 0;
			}
		}
		if (getHealth() - amount <= 0) {
			ItemStack poppet = BewitchmentAPI.getPoppet(world, BWObjects.DEATH_PROTECTION_POPPET, this, null);
			if (!poppet.isEmpty()) {
				if (poppet.damage(1, random, null) && poppet.getDamage() >= poppet.getMaxDamage()) {
					poppet.decrement(1);
				}
				heal(Float.MAX_VALUE);
				return 0;
			}
		}
		if (attacker instanceof PlayerEntity && BewitchmentAPI.isWeakToSilver((LivingEntity) attacker)) {
			ItemStack poppet = BewitchmentAPI.getPoppet(world, BWObjects.JUDGMENT_POPPET, this, null);
			if (!poppet.isEmpty()) {
				if (poppet.damage(1, random, null) && poppet.getDamage() >= poppet.getMaxDamage()) {
					poppet.decrement(1);
				}
				amount /= 4;
			}
		}
		if (attacker instanceof LivingEntity) {
			ItemStack poppet = BewitchmentAPI.getPoppet(world, BWObjects.FATIGUE_POPPET, this, null);
			if (!poppet.isEmpty() && ((LivingEntity) attacker).addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 60, 1)) && poppet.damage(1, random, null) && poppet.getDamage() >= poppet.getMaxDamage()) {
				poppet.decrement(1);
			}
		}
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
		if (source == DamageSource.FALL) {
			BlockPos sigilPos = BewitchmentAPI.getClosestBlockPos(getBlockPos(), 16, currentPos -> world.getBlockEntity(currentPos) instanceof HasSigil && ((HasSigil) world.getBlockEntity(currentPos)).getSigil() == BWSigils.HEAVY);
			if (sigilPos != null) {
				BlockEntity blockEntity = world.getBlockEntity(sigilPos);
				HasSigil sigil = (HasSigil) blockEntity;
				if (sigil.test(this)) {
					sigil.setUses(sigil.getUses() - 1);
					blockEntity.markDirty();
					amount *= 3;
				}
			}
		}
		if (hasCurse(BWCurses.FORESTS_WRATH) && (((attacker instanceof LivingEntity && ((LivingEntity) attacker).getMainHandStack().getItem() instanceof AxeItem)) || source.isFire())) {
			amount *= 2;
		}
		if (hasCurse(BWCurses.SUSCEPTIBILITY) && (source.isFire() || (attacker instanceof LivingEntity && (((LivingEntity) attacker).getGroup() == EntityGroup.AQUATIC || ((LivingEntity) attacker).getGroup() == BewitchmentAPI.DEMON)))) {
			amount *= 2;
		}
		if (((Object) this instanceof PlayerEntity) && hasContract(BWContracts.FAMINE)) {
			amount *= (1 - (0.025f * (20 - ((PlayerEntity) (Object) this).getHungerManager().getFoodLevel())));
		}
		if (attacker instanceof PlayerEntity && ((ContractAccessor) attacker).hasNegativeEffects() && ((ContractAccessor) attacker).hasContract(BWContracts.FAMINE)) {
			amount *= (1 - (0.025f * (20 - ((PlayerEntity) attacker).getHungerManager().getFoodLevel())));
		}
		if (attacker instanceof LivingEntity && ((ContractAccessor) attacker).hasContract(BWContracts.WRATH)) {
			amount *= (1 + (0.025f * (((LivingEntity) attacker).getMaxHealth() - ((LivingEntity) attacker).getHealth())));
		}
		if (hasNegativeEffects() && hasContract(BWContracts.WRATH)) {
			amount *= (1 + (0.025f * (getMaxHealth() - getHealth())));
		}
		if (source.getMagic() && (Object) this instanceof LivingEntity) {
			int armorPieces = BewitchmentAPI.getArmorPieces((LivingEntity) (Object) this, stack -> {
				if (stack.getItem() instanceof ArmorItem) {
					ArmorMaterial material = ((ArmorItem) stack.getItem()).getMaterial();
					return material == BWMaterials.HEDGEWITCH_ARMOR || material == BWMaterials.ALCHEMIST_ARMOR || material == BWMaterials.BESMIRCHED_ARMOR;
				}
				return false;
			});
			if (armorPieces > 0) {
				amount *= (1 - (0.2f * armorPieces));
			}
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
			for (int i = curses.size() - 1; i >= 0; i--) {
				Curse.Instance instance = curses.get(i);
				instance.curse.tick(livingEntity);
				instance.duration--;
				if (instance.duration <= 0) {
					curses.remove(i);
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
		InsanityTargetAccessor insanityTargetAccessor = InsanityTargetAccessor.of(this).orElse(null);
		if (insanityTargetAccessor != null && insanityTargetAccessor.getInsanityTargetUUID().isPresent()) {
			callbackInfo.setReturnValue(false);
			return;
		}
		if (!source.isOutOfWorld()) {
			if (!BewitchmentAPI.getBlockPoses(getBlockPos(), 16, currentPos -> world.getBlockEntity(currentPos) instanceof GlyphBlockEntity && ((GlyphBlockEntity) world.getBlockEntity(currentPos)).ritualFunction == BWRitualFunctions.PREVENT_DAMAGE).isEmpty()) {
				callbackInfo.setReturnValue(false);
				return;
			}
		}
		Entity attacker = source.getSource();
		if (!source.isOutOfWorld() && (hasStatusEffect(BWStatusEffects.ETHEREAL) || (attacker instanceof LivingEntity && ((LivingEntity) attacker).hasStatusEffect(BWStatusEffects.ETHEREAL)))) {
			callbackInfo.setReturnValue(false);
		}
		else if (hasStatusEffect(BWStatusEffects.DEFLECTION) && attacker != null && EntityTypeTags.ARROWS.contains(attacker.getType())) {
			int amplifier = getStatusEffect(BWStatusEffects.DEFLECTION).getAmplifier() + 1;
			Vec3d velocity = attacker.getVelocity();
			attacker.setVelocity(velocity.getX() * 2 * amplifier, velocity.getY() * 2 * amplifier, velocity.getZ() * 2 * amplifier);
			callbackInfo.setReturnValue(false);
		}
		else if (amount > 0) {
			if (hasStatusEffect(BWStatusEffects.LEECHING)) {
				heal(amount * (getStatusEffect(BWStatusEffects.LEECHING).getAmplifier() + 1) / 4);
			}
			if (hasStatusEffect(BWStatusEffects.THORNS) && !(source instanceof EntityDamageSource && ((EntityDamageSource) source).isThorns())) {
				attacker.damage(DamageSource.thorns(attacker), 2 * (getStatusEffect(BWStatusEffects.THORNS).getAmplifier() + 1));
			}
			if (hasStatusEffect(BWStatusEffects.VOLATILITY) && !source.isExplosive()) {
				for (LivingEntity entity : world.getEntitiesByClass(LivingEntity.class, getBoundingBox().expand(3), LivingEntity::isAlive)) {
					entity.damage(DamageSource.explosion(((LivingEntity) (Object) this)), 4 * (getStatusEffect(BWStatusEffects.VOLATILITY).getAmplifier() + 1));
				}
				world.playSound(null, getBlockPos(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.NEUTRAL, 1, 1);
				PlayerLookup.tracking(this).forEach(playerEntity -> SpawnExplosionParticlesPacket.send(playerEntity, this));
				if (((Object) this) instanceof PlayerEntity) {
					SpawnExplosionParticlesPacket.send((PlayerEntity) (Object) this, this);
				}
				removeStatusEffect(BWStatusEffects.VOLATILITY);
			}
			if (attacker instanceof ContractAccessor && ((ContractAccessor) attacker).hasContract(BWContracts.PESTILENCE)) {
				addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, 100));
				addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 100));
			}
			if (source == DamageSource.DROWN && (Object) this instanceof PlayerEntity) {
				ItemStack poppet = BewitchmentAPI.getPoppet(world, BWObjects.VOODOO_POPPET, null, (PlayerEntity) (Object) this);
				if (!poppet.isEmpty()) {
					LivingEntity owner = BewitchmentAPI.getTaglockOwner(world, poppet);
					if (!owner.getUuid().equals(getUuid())) {
						if (poppet.damage(1, random, null) && poppet.getDamage() >= poppet.getMaxDamage()) {
							poppet.decrement(1);
						}
						ItemStack potentialPoppet = BewitchmentAPI.getPoppet(world, BWObjects.VOODOO_PROTECTION_POPPET, owner, null);
						if (!potentialPoppet.isEmpty()) {
							if (potentialPoppet.damage(1, random, null) && potentialPoppet.getDamage() >= potentialPoppet.getMaxDamage()) {
								potentialPoppet.decrement(1);
							}
							return;
						}
						owner.damage(source, amount);
					}
				}
			}
		}
	}
	
	@Inject(method = "isAffectedBySplashPotions", at = @At("HEAD"), cancellable = true)
	private void isAffectedBySplashPotions(CallbackInfoReturnable<Boolean> callbackInfo) {
		MasterAccessor.of(this).ifPresent(masterAccessor -> {
			if (masterAccessor.getMasterUUID() != null) {
				callbackInfo.setReturnValue(false);
			}
		});
	}
	
	@Inject(method = "canHaveStatusEffect", at = @At("HEAD"), cancellable = true)
	private void canHaveStatusEffect(StatusEffectInstance effect, CallbackInfoReturnable<Boolean> callbackInfo) {
		StatusEffectType type = ((StatusEffectAccessor) effect.getEffectType()).bw_getType();
		if (type == StatusEffectType.BENEFICIAL) {
			if (hasCurse(BWCurses.UNLUCKY) && random.nextBoolean()) {
				callbackInfo.setReturnValue(false);
			}
		}
		if (type != StatusEffectType.HARMFUL) {
			BlockPos sigilPos = BewitchmentAPI.getClosestBlockPos(getBlockPos(), 16, currentPos -> world.getBlockEntity(currentPos) instanceof HasSigil && ((HasSigil) world.getBlockEntity(currentPos)).getSigil() == BWSigils.RUIN);
			if (sigilPos != null) {
				BlockEntity blockEntity = world.getBlockEntity(sigilPos);
				HasSigil sigil = (HasSigil) blockEntity;
				if (sigil.test(this)) {
					sigil.setUses(sigil.getUses() - 1);
					blockEntity.markDirty();
					callbackInfo.setReturnValue(false);
				}
			}
		}
	}
	
	@Inject(method = "canBreatheInWater", at = @At("HEAD"), cancellable = true)
	private void canBreatheInWater(CallbackInfoReturnable<Boolean> callbackInfo) {
		if (hasStatusEffect(BWStatusEffects.GILLS)) {
			callbackInfo.setReturnValue(true);
		}
	}
	
	@Inject(method = "isClimbing", at = @At("HEAD"), cancellable = true)
	private void isClimbing(CallbackInfoReturnable<Boolean> callbackInfo) {
		if (hasStatusEffect(BWStatusEffects.CLIMBING) && horizontalCollision) {
			callbackInfo.setReturnValue(true);
		}
	}
	
	@Inject(method = "drop", at = @At("HEAD"))
	private void drop(DamageSource source, CallbackInfo callbackInfo) {
		if (!world.isClient && shouldDropLoot()) {
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
							ItemScatterer.spawn(world, getX() + 0.5, getY() + 0.5, getZ() + 0.5, drop);
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
	
	@Inject(method = "onDeath", at = @At("HEAD"))
	private void onDeath(DamageSource source, CallbackInfo callbackInfo) {
		if (!world.isClient) {
			Entity attacker = source.getSource();
			if (attacker instanceof PlayerEntity) {
				PlayerEntity player = (PlayerEntity) attacker;
				ItemStack stack = player.getMainHandStack();
				if (stack.getItem() instanceof AthameItem && player.preferredHand == Hand.MAIN_HAND) {
					BlockPos glyph = BewitchmentAPI.getClosestBlockPos(getBlockPos(), 6, currentPos -> world.getBlockEntity(currentPos) instanceof GlyphBlockEntity);
					if (glyph != null) {
						((GlyphBlockEntity) world.getBlockEntity(glyph)).onUse(world, glyph, player, Hand.MAIN_HAND, (LivingEntity) (Object) this);
					}
				}
			}
		}
	}
	
	@Inject(method = "wakeUp", at = @At("TAIL"))
	private void wakeUp(CallbackInfo callbackInfo) {
		if (!world.isClient) {
			BewitchmentAPI.getBlockPoses(getBlockPos(), 12, foundPos -> world.getBlockEntity(foundPos) instanceof BrazierBlockEntity && ((BrazierBlockEntity) world.getBlockEntity(foundPos)).incenseRecipe != null).forEach(foundPos -> {
				IncenseRecipe recipe = ((BrazierBlockEntity) world.getBlockEntity(foundPos)).incenseRecipe;
				int durationMultiplier = 1;
				BlockPos nearestSigil = BewitchmentAPI.getClosestBlockPos(getBlockPos(), 16, foundSigil -> world.getBlockEntity(foundSigil) instanceof SigilBlockEntity && ((SigilBlockEntity) world.getBlockEntity(foundSigil)).getSigil() == BWSigils.EXTENDING);
				if (nearestSigil != null) {
					BlockEntity blockEntity = world.getBlockEntity(nearestSigil);
					HasSigil sigil = ((HasSigil) blockEntity);
					if (sigil.test(this)) {
						sigil.setUses(sigil.getUses() - 1);
						blockEntity.markDirty();
						durationMultiplier = 2;
					}
				}
				addStatusEffect(new StatusEffectInstance(recipe.effect, 24000 * durationMultiplier, recipe.amplifier, true, false));
			});
		}
	}
	
	@Inject(method = "addStatusEffect", at = @At("HEAD"))
	private void addStatusEffect(StatusEffectInstance effect, CallbackInfoReturnable<Boolean> callbackInfo) {
		if ((Object) this instanceof PlayerEntity) {
			ItemStack poppet = BewitchmentAPI.getPoppet(world, BWObjects.VOODOO_POPPET, null, (PlayerEntity) (Object) this);
			if (!poppet.isEmpty()) {
				LivingEntity owner = BewitchmentAPI.getTaglockOwner(world, poppet);
				if (!owner.getUuid().equals(getUuid())) {
					if (poppet.damage(1, random, null) && poppet.getDamage() >= poppet.getMaxDamage()) {
						poppet.decrement(1);
					}
					ItemStack potentialPoppet = BewitchmentAPI.getPoppet(world, BWObjects.VOODOO_PROTECTION_POPPET, owner, null);
					if (!potentialPoppet.isEmpty()) {
						if (potentialPoppet.damage(1, random, null) && potentialPoppet.getDamage() >= potentialPoppet.getMaxDamage()) {
							potentialPoppet.decrement(1);
						}
						return;
					}
					owner.addStatusEffect(effect);
				}
			}
		}
	}
	
	@Inject(method = "readCustomDataFromTag", at = @At("TAIL"))
	private void readCustomDataFromTag(CompoundTag tag, CallbackInfo callbackInfo) {
		if (BWTags.HAS_BLOOD.contains(getType())) {
			setBlood(tag.getInt("Blood"));
		}
		ListTag curses = tag.getList("Curses", NbtType.COMPOUND);
		for (int i = 0; i < curses.size(); i++) {
			CompoundTag curse = curses.getCompound(i);
			addCurse(new Curse.Instance(BWRegistries.CURSES.get(new Identifier(curse.getString("Curse"))), curse.getInt("Duration")));
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
		ListTag curses = new ListTag();
		for (Curse.Instance instance : getCurses()) {
			CompoundTag curseTag = new CompoundTag();
			curseTag.putString("Curse", BWRegistries.CURSES.getId(instance.curse).toString());
			curseTag.putInt("Duration", instance.duration);
			curses.add(curseTag);
		}
		tag.put("Curses", curses);
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
