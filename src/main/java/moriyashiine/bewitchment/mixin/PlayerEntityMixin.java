package moriyashiine.bewitchment.mixin;

import io.github.ladysnake.impersonate.Impersonator;
import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.api.interfaces.entity.ContractAccessor;
import moriyashiine.bewitchment.api.interfaces.entity.FortuneAccessor;
import moriyashiine.bewitchment.api.interfaces.entity.MagicAccessor;
import moriyashiine.bewitchment.api.interfaces.entity.TransformationAccessor;
import moriyashiine.bewitchment.api.registry.Fortune;
import moriyashiine.bewitchment.api.registry.Transformation;
import moriyashiine.bewitchment.common.block.CoffinBlock;
import moriyashiine.bewitchment.common.block.entity.BrazierBlockEntity;
import moriyashiine.bewitchment.common.block.entity.GlyphBlockEntity;
import moriyashiine.bewitchment.common.entity.interfaces.*;
import moriyashiine.bewitchment.common.misc.BWUtil;
import moriyashiine.bewitchment.common.registry.*;
import moriyashiine.bewitchment.common.statuseffect.PolymorphStatusEffect;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.explosion.Explosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;
import java.util.UUID;

@SuppressWarnings("ConstantConditions")
@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements MagicAccessor, PolymorphAccessor, FortuneAccessor, ContractAccessor, TransformationAccessor, RespawnTimerAccessor, WerewolfAccessor, BroomUserAccessor, TrueInvisibleAccessor {
	private static final TrackedData<Integer> MAGIC = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.INTEGER);
	private static final TrackedData<Integer> MAGIC_TIMER = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.INTEGER);
	
	private static final TrackedData<String> TRANSFORMATION = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.STRING);
	private static final TrackedData<Boolean> ALTERNATE_FORM = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
	
	private static final TrackedData<Integer> WEREWOLF_VARIANT = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.INTEGER);
	
	private static final TrackedData<Boolean> PRESSING_FORWARD = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
	
	private static final TrackedData<Boolean> TRUE_INVISIBLE = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
	
	private Fortune.Instance fortune = null;
	
	private UUID polymorphUUID = null;
	private String polymorphName = null;
	
	private int respawnTimer = 400;
	
	private boolean forcedTransformation = false;
	
	@Shadow
	public abstract HungerManager getHungerManager();
	
	@Shadow
	public abstract SoundCategory getSoundCategory();
	
	@Shadow
	public abstract boolean damage(DamageSource source, float amount);
	
	protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}
	
	@Override
	public int getMagic() {
		return dataTracker.get(MAGIC);
	}
	
	@Override
	public void setMagic(int magic) {
		dataTracker.set(MAGIC, magic);
	}
	
	@Override
	public int getMagicTimer() {
		return dataTracker.get(MAGIC_TIMER);
	}
	
	@Override
	public void setMagicTimer(int magicTimer) {
		dataTracker.set(MAGIC_TIMER, magicTimer);
	}
	
	@Override
	public Fortune.Instance getFortune() {
		return fortune;
	}
	
	@Override
	public void setFortune(Fortune.Instance fortune) {
		this.fortune = fortune;
	}
	
	@Override
	public Transformation getTransformation() {
		return BWRegistries.TRANSFORMATIONS.get(new Identifier(dataTracker.get(TRANSFORMATION)));
	}
	
	@Override
	public void setTransformation(Transformation transformation) {
		dataTracker.set(TRANSFORMATION, BWRegistries.TRANSFORMATIONS.getId(transformation).toString());
	}
	
	@Override
	public boolean getAlternateForm() {
		return dataTracker.get(ALTERNATE_FORM);
	}
	
	@Override
	public void setAlternateForm(boolean alternateForm) {
		dataTracker.set(ALTERNATE_FORM, alternateForm);
	}
	
	@Override
	public UUID getPolymorphUUID() {
		return polymorphUUID;
	}
	
	@Override
	public void setPolymorphUUID(UUID uuid) {
		polymorphUUID = uuid;
	}
	
	@Override
	public String getPolymorphName() {
		return polymorphName;
	}
	
	@Override
	public void setPolymorphName(String name) {
		polymorphName = name;
	}
	
	@Override
	public int getRespawnTimer() {
		return respawnTimer;
	}
	
	@Override
	public void setRespawnTimer(int respawnTimer) {
		this.respawnTimer = respawnTimer;
	}
	
	@Override
	public boolean getForcedTransformation() {
		return forcedTransformation;
	}
	
	@Override
	public void setForcedTransformation(boolean forcedTransformation) {
		this.forcedTransformation = forcedTransformation;
	}
	
	@Override
	public int getWerewolfVariant() {
		return dataTracker.get(WEREWOLF_VARIANT);
	}
	
	@Override
	public void setWerewolfVariant(int variant) {
		dataTracker.set(WEREWOLF_VARIANT, variant);
	}
	
	@Override
	public boolean getPressingForward() {
		return dataTracker.get(PRESSING_FORWARD);
	}
	
	@Override
	public void setPressingForward(boolean pressingForward) {
		dataTracker.set(PRESSING_FORWARD, pressingForward);
	}
	
	@Override
	public boolean getTrueInvisible() {
		return dataTracker.get(TRUE_INVISIBLE);
	}
	
	@Override
	public void setTrueInvisible(boolean pressingForward) {
		dataTracker.set(TRUE_INVISIBLE, pressingForward);
	}
	
	@Inject(method = "tick", at = @At("HEAD"))
	private void tick(CallbackInfo callbackInfo) {
		if (!world.isClient) {
			if (getPolymorphUUID() != null && !hasStatusEffect(BWStatusEffects.POLYMORPH)) {
				setPolymorphUUID(null);
				setPolymorphName(null);
				Impersonator.get((PlayerEntity) (Object) this).stopImpersonation(PolymorphStatusEffect.IMPERSONATE_IDENTIFIER);
			}
			if (getMagicTimer() > 0) {
				setMagicTimer(getMagicTimer() - 1);
			}
			if (getFortune() != null) {
				if (getFortune().fortune.tick((ServerWorld) world, (PlayerEntity) (Object) this)) {
					getFortune().duration = 0;
				}
				else {
					getFortune().duration--;
				}
				if (getFortune().duration <= 0) {
					if (getFortune().fortune.finish((ServerWorld) world, (PlayerEntity) (Object) this)) {
						setFortune(null);
					}
					else {
						getFortune().duration = world.random.nextInt(120000);
					}
				}
			}
			if (getTrueInvisible()) {
				setTrueInvisible(false);
			}
			BWUtil.updateAttributeModifiers((PlayerEntity) (Object) this);
			if (getRespawnTimer() > 0) {
				setRespawnTimer(getRespawnTimer() - 1);
			}
			if (BewitchmentAPI.isVampire(this, true)) {
				BWUtil.doVampireLogic(((PlayerEntity) (Object) this), getAlternateForm());
			}
			if (BewitchmentAPI.isWerewolf(this, true)) {
				BWUtil.doWerewolfLogic(((PlayerEntity) (Object) this), getAlternateForm());
			}
		}
	}
	
	@ModifyVariable(method = "applyDamage", at = @At(value = "INVOKE", shift = At.Shift.BEFORE, ordinal = 0, target = "Lnet/minecraft/entity/player/PlayerEntity;getHealth()F"))
	private float modifyDamage(float amount, DamageSource source) {
		if (!world.isClient) {
			amount = BWDamageSources.handleDamage(this, source, amount);
		}
		return amount;
	}
	
	@ModifyVariable(method = "addExhaustion", at = @At("HEAD"))
	private float modifyExhaustion(float exhaustion) {
		if (!world.isClient) {
			if (hasNegativeEffects() && hasContract(BWContracts.GLUTTONY)) {
				exhaustion *= 1.5f;
			}
			if (BewitchmentAPI.isWerewolf(this, true)) {
				exhaustion *= 1.25f;
			}
			if (BewitchmentAPI.isWerewolf(this, false)) {
				exhaustion *= 2;
			}
		}
		return exhaustion;
	}
	
	@Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;isDay()Z"))
	private boolean coffinHack(World obj) {
		Optional<BlockPos> pos = getSleepingPosition();
		if (pos.isPresent() && world.getBlockState(pos.get()).getBlock() instanceof CoffinBlock) {
			return obj.isNight();
		}
		return obj.isDay();
	}
	
	@Inject(method = "getHurtSound", at = @At("HEAD"))
	private void getHurtSound(DamageSource source, CallbackInfoReturnable<SoundEvent> callbackInfo) {
		if (source == BWDamageSources.SUN) {
			world.playSound(null, getBlockPos(), SoundEvents.BLOCK_FIRE_EXTINGUISH, getSoundCategory(), getSoundVolume(), getSoundPitch());
		}
	}
	
	@Inject(method = "canFoodHeal", at = @At("HEAD"), cancellable = true)
	private void canFoodHeal(CallbackInfoReturnable<Boolean> callbackInfo) {
		if (BewitchmentAPI.isVampire(this, true)) {
			callbackInfo.setReturnValue(false);
		}
	}
	
	@Inject(method = "isInvulnerableTo", at = @At("RETURN"), cancellable = true)
	private void isInvulnerableTo(DamageSource source, CallbackInfoReturnable<Boolean> callbackInfo) {
		if (callbackInfo.getReturnValue() && source.isFire() && BewitchmentAPI.isVampire(this, true)) {
			callbackInfo.setReturnValue(false);
		}
	}
	
	@Inject(method = "eatFood", at = @At("HEAD"))
	private void eat(World world, ItemStack stack, CallbackInfoReturnable<ItemStack> callbackInfo) {
		if (!world.isClient) {
			if (hasStatusEffect(BWStatusEffects.NOURISHING)) {
				getHungerManager().add(getStatusEffect(BWStatusEffects.NOURISHING).getAmplifier() + 2, 0.5f);
			}
			FoodComponent foodComponent = stack.getItem().getFoodComponent();
			if (foodComponent != null) {
				if (BWTags.WITCHBERRY_FOODS.contains(stack.getItem())) {
					fillMagic(foodComponent.getHunger(), false);
				}
				if (hasContract(BWContracts.GLUTTONY)) {
					getHungerManager().add(foodComponent.getHunger(), foodComponent.getSaturationModifier());
				}
			}
			boolean vampire = BewitchmentAPI.isVampire(this, true);
			if (vampire || (BewitchmentAPI.isWerewolf(this, true) && !foodComponent.isMeat())) {
				addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, 100, 1));
				addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 100, 1));
				addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 100, 1));
				addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 100, 1));
			}
			if (vampire && (stack.getItem() == BWObjects.GARLIC || stack.getItem() == BWObjects.GRILLED_GARLIC || stack.getItem() == BWObjects.GARLIC_BREAD)) {
				damage(BWDamageSources.MAGIC_COPY, Float.MAX_VALUE);
			}
		}
	}
	
	@Inject(method = "dropItem(Lnet/minecraft/item/ItemStack;ZZ)Lnet/minecraft/entity/ItemEntity;", at = @At("HEAD"))
	private void dropItem(ItemStack stack, boolean throwRandomly, boolean retainOwnership, CallbackInfoReturnable<ItemEntity> callbackInfo) {
		if (!world.isClient && stack.getItem() == BWObjects.VOODOO_POPPET) {
			LivingEntity owner = BewitchmentAPI.getTaglockOwner(world, stack);
			if (owner != null) {
				if (stack.damage(BewitchmentAPI.getFamiliar((PlayerEntity) (Object) this) == EntityType.WOLF && random.nextBoolean() ? 0 : 1, random, null) && stack.getDamage() >= stack.getMaxDamage()) {
					stack.decrement(1);
				}
				ItemStack potentialPoppet = BewitchmentAPI.getPoppet(world, BWObjects.VOODOO_PROTECTION_POPPET, owner, null);
				if (!potentialPoppet.isEmpty()) {
					if (potentialPoppet.damage(owner instanceof PlayerEntity && BewitchmentAPI.getFamiliar((PlayerEntity) owner) == EntityType.WOLF && random.nextBoolean() ? 0 : 1, random, null) && potentialPoppet.getDamage() >= potentialPoppet.getMaxDamage()) {
						potentialPoppet.decrement(1);
					}
					return;
				}
				owner.addVelocity(getRotationVector().x, getRotationVector().y, getRotationVector().z);
				owner.velocityModified = true;
			}
		}
	}
	
	@Inject(method = "onKilledOther", at = @At("HEAD"))
	private void onKilledOther(ServerWorld serverWorld, LivingEntity livingEntity, CallbackInfo callbackInfo) {
		if (!world.isClient) {
			BlockPos pos = livingEntity.getBlockPos();
			BlockPos glyph = BWUtil.getClosestBlockPos(pos, 6, currentPos -> world.getBlockEntity(currentPos) instanceof GlyphBlockEntity);
			if (glyph != null) {
				((GlyphBlockEntity) world.getBlockEntity(glyph)).onUse(world, glyph, (PlayerEntity) (Object) this, Hand.MAIN_HAND, livingEntity);
			}
			if (world.isNight()) {
				boolean chicken = livingEntity instanceof ChickenEntity;
				if ((chicken && world.getBiome(pos).getCategory() == Biome.Category.EXTREME_HILLS) || (livingEntity instanceof WolfEntity && (world.getBiome(pos).getCategory() == Biome.Category.FOREST || world.getBiome(pos).getCategory() == Biome.Category.TAIGA))) {
					BlockPos brazierPos = BWUtil.getClosestBlockPos(pos, 8, currentPos -> {
						BlockEntity blockEntity = world.getBlockEntity(currentPos);
						return blockEntity instanceof BrazierBlockEntity && ((BrazierBlockEntity) blockEntity).incenseRecipe.effect == BWStatusEffects.MORTAL_COIL;
					});
					if (brazierPos != null) {
						world.breakBlock(brazierPos, false);
						world.createExplosion(livingEntity, brazierPos.getX() + 0.5, brazierPos.getY() + 0.5, brazierPos.getZ() + 0.5, 3, Explosion.DestructionType.BREAK);
						Entity entity = chicken ? BWEntityTypes.LILITH.create(world) : BWEntityTypes.HERNE.create(world);
						if (entity instanceof MobEntity) {
							((MobEntity) entity).initialize((ServerWorldAccess) world, world.getLocalDifficulty(brazierPos), SpawnReason.EVENT, null, null);
							entity.updatePositionAndAngles(brazierPos.getX() + 0.5, brazierPos.getY(), brazierPos.getZ() + 0.5, world.random.nextFloat() * 360, 0);
							world.spawnEntity(entity);
						}
					}
				}
			}
		}
	}
	
	@Inject(method = "readCustomDataFromTag", at = @At("TAIL"))
	private void readCustomDataFromTag(CompoundTag tag, CallbackInfo callbackInfo) {
		setMagic(tag.getInt("Magic"));
		if (tag.contains("Fortune")) {
			setFortune(new Fortune.Instance(BWRegistries.FORTUNES.get(new Identifier(tag.getString("Fortune"))), tag.getInt("FortuneDuration")));
		}
		if (tag.contains("Transformation")) {
			setTransformation(BWRegistries.TRANSFORMATIONS.get(new Identifier(tag.getString("Transformation"))));
		}
		setAlternateForm(tag.getBoolean("AlternateForm"));
		if (tag.contains("PolymorphUUID")) {
			setPolymorphUUID(tag.getUuid("PolymorphUUID"));
			setPolymorphName(tag.getString("PolymorphName"));
		}
		setRespawnTimer(tag.getInt("RespawnTimer"));
		setForcedTransformation(tag.getBoolean("ForcedTransformation"));
		setWerewolfVariant(tag.getInt("WerewolfVariant"));
		setPressingForward(tag.getBoolean("PressingForward"));
		setTrueInvisible(tag.getBoolean("TrueInvisible"));
	}
	
	@Inject(method = "writeCustomDataToTag", at = @At("TAIL"))
	private void writeCustomDataToTag(CompoundTag tag, CallbackInfo callbackInfo) {
		tag.putInt("Magic", getMagic());
		if (getFortune() != null) {
			tag.putString("Fortune", BWRegistries.FORTUNES.getId(getFortune().fortune).toString());
			tag.putInt("FortuneDuration", getFortune().duration);
		}
		tag.putString("Transformation", BWRegistries.TRANSFORMATIONS.getId(getTransformation()).toString());
		tag.putBoolean("AlternateForm", getAlternateForm());
		if (getPolymorphUUID() != null) {
			tag.putUuid("PolymorphUUID", getPolymorphUUID());
			tag.putString("PolymorphName", getPolymorphName());
		}
		tag.putInt("RespawnTimer", getRespawnTimer());
		tag.putBoolean("ForcedTransformation", getForcedTransformation());
		tag.putInt("WerewolfVariant", getWerewolfVariant());
		tag.putBoolean("PressingForward", getPressingForward());
		tag.putBoolean("TrueInvisible", getTrueInvisible());
	}
	
	@Inject(method = "initDataTracker", at = @At("TAIL"))
	private void initDataTracker(CallbackInfo callbackInfo) {
		dataTracker.startTracking(MAGIC, 0);
		dataTracker.startTracking(MAGIC_TIMER, 60);
		dataTracker.startTracking(TRANSFORMATION, BWRegistries.TRANSFORMATIONS.getId(BWTransformations.HUMAN).toString());
		dataTracker.startTracking(ALTERNATE_FORM, false);
		dataTracker.startTracking(WEREWOLF_VARIANT, 0);
		dataTracker.startTracking(PRESSING_FORWARD, false);
		dataTracker.startTracking(TRUE_INVISIBLE, false);
	}
}
