package moriyashiine.bewitchment.mixin.transformation;

import com.jamieswhiteshirt.reachentityattributes.ReachEntityAttributes;
import dev.emi.stepheightentityattribute.StepHeightEntityAttributeMain;
import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.api.event.AllowVampireBurn;
import moriyashiine.bewitchment.api.event.AllowVampireHeal;
import moriyashiine.bewitchment.api.event.OnTransformationSet;
import moriyashiine.bewitchment.api.interfaces.entity.BloodAccessor;
import moriyashiine.bewitchment.api.interfaces.entity.TransformationAccessor;
import moriyashiine.bewitchment.api.registry.Transformation;
import moriyashiine.bewitchment.common.entity.interfaces.RespawnTimerAccessor;
import moriyashiine.bewitchment.common.entity.interfaces.WerewolfAccessor;
import moriyashiine.bewitchment.common.misc.BWUtil;
import moriyashiine.bewitchment.common.network.packet.TransformationAbilityPacket;
import moriyashiine.bewitchment.common.registry.*;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;

@SuppressWarnings("ConstantConditions")
@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements TransformationAccessor, WerewolfAccessor {
	private static final EntityAttributeModifier VAMPIRE_ATTACK_DAMAGE_MODIFIER_0 = new EntityAttributeModifier(UUID.fromString("066862f6-989c-4f35-ac6d-2696b91a1a5b"), "Transformation modifier", 2, EntityAttributeModifier.Operation.ADDITION);
	private static final EntityAttributeModifier VAMPIRE_ATTACK_DAMAGE_MODIFIER_1 = new EntityAttributeModifier(UUID.fromString("d2be3564-97e7-42c9-88c5-6b753472e37f"), "Transformation modifier", 4, EntityAttributeModifier.Operation.ADDITION);
	private static final EntityAttributeModifier VAMPIRE_MOVEMENT_SPEED_MODIFIER_0 = new EntityAttributeModifier(UUID.fromString("a782c03d-af7b-4eb7-b997-dd396bfdc7a0"), "Transformation modifier", 0.04, EntityAttributeModifier.Operation.ADDITION);
	private static final EntityAttributeModifier VAMPIRE_MOVEMENT_SPEED_MODIFIER_1 = new EntityAttributeModifier(UUID.fromString("7c7a61eb-83e8-4e85-94d6-a410a4153d6d"), "Transformation modifier", 0.08, EntityAttributeModifier.Operation.ADDITION);
	
	private static final EntityAttributeModifier WEREWOLF_ATTACK_SPEED_MODIFIER = new EntityAttributeModifier(UUID.fromString("db2512a4-655d-4843-8b06-619748a33954"), "Transformation modifier", -2, EntityAttributeModifier.Operation.ADDITION);
	private static final EntityAttributeModifier WEREWOLF_ARMOR_MODIFIER = new EntityAttributeModifier(UUID.fromString("f00b0b0f-8ad6-4a2f-bdf5-6c337ffee56c"), "Transformation modifier", 20, EntityAttributeModifier.Operation.ADDITION);
	private static final EntityAttributeModifier WEREWOLF_ATTACK_RANGE_MODIFIER = new EntityAttributeModifier(UUID.fromString("ae0e4c0a-971f-4629-99ad-60c115112c1d"), "Transformation modifier", 1, EntityAttributeModifier.Operation.ADDITION);
	private static final EntityAttributeModifier WEREWOLF_REACH_MODIFIER = new EntityAttributeModifier(UUID.fromString("4c6d90ab-41ad-4d8a-b77a-7329361d3a7b"), "Transformation modifier", 1, EntityAttributeModifier.Operation.ADDITION);
	private static final EntityAttributeModifier WEREWOLF_STEP_HEIGHT_MODIFIER = new EntityAttributeModifier(UUID.fromString("af386c1c-b4fc-429d-97b6-b2559826fa9d"), "Transformation modifier", 0.4, EntityAttributeModifier.Operation.ADDITION);
	
	private static final EntityAttributeModifier WEREWOLF_ATTACK_DAMAGE_MODIFIER_0 = new EntityAttributeModifier(UUID.fromString("06861902-ebc4-4e6e-956c-59c1ae3085c7"), "Transformation modifier", 7, EntityAttributeModifier.Operation.ADDITION);
	private static final EntityAttributeModifier WEREWOLF_ATTACK_DAMAGE_MODIFIER_1 = new EntityAttributeModifier(UUID.fromString("10c0bedf-bde5-4cae-8acc-90b1204731dd"), "Transformation modifier", 14, EntityAttributeModifier.Operation.ADDITION);
	private static final EntityAttributeModifier WEREWOLF_ARMOR_TOUGHNESS_MODIFIER_0 = new EntityAttributeModifier(UUID.fromString("44f17821-1e30-426f-81d7-cd1da88fa584"), "Transformation modifier", 10, EntityAttributeModifier.Operation.ADDITION);
	private static final EntityAttributeModifier WEREWOLF_ARMOR_TOUGHNESS_MODIFIER_1 = new EntityAttributeModifier(UUID.fromString("edfd078d-e25c-4e27-ad91-c2b32037c8be"), "Transformation modifier", 20, EntityAttributeModifier.Operation.ADDITION);
	private static final EntityAttributeModifier WEREWOLF_MOVEMENT_SPEED_MODIFIER_0 = new EntityAttributeModifier(UUID.fromString("e26a276a-86cd-44db-9091-acd42fc00d95"), "Transformation modifier", 0.08, EntityAttributeModifier.Operation.ADDITION);
	private static final EntityAttributeModifier WEREWOLF_MOVEMENT_SPEED_MODIFIER_1 = new EntityAttributeModifier(UUID.fromString("718104a6-aa19-4b53-bad9-1f9edd46d38a"), "Transformation modifier", 0.16, EntityAttributeModifier.Operation.ADDITION);
	
	
	private static final TrackedData<String> TRANSFORMATION = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.STRING);
	private static final TrackedData<Boolean> ALTERNATE_FORM = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
	
	private static final TrackedData<Integer> WEREWOLF_VARIANT = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.INTEGER);
	
	private boolean forcedTransformation = false;
	
	@Shadow
	public abstract SoundCategory getSoundCategory();
	
	protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}
	
	@Override
	public Transformation getTransformation() {
		return BWRegistries.TRANSFORMATIONS.get(new Identifier(dataTracker.get(TRANSFORMATION)));
	}
	
	@Override
	public void setTransformation(Transformation transformation) {
		OnTransformationSet.EVENT.invoker().onTransformationSet((PlayerEntity) (Object) this, transformation);
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
	
	@Inject(method = "tick", at = @At("HEAD"))
	private void tickHead(CallbackInfo callbackInfo) {
		if (BewitchmentAPI.isWerewolf(this, false)) {
			flyingSpeed *= 1.5f;
		}
	}
	
	@Inject(method = "tick", at = @At("TAIL"))
	private void tickTail(CallbackInfo callbackInfo) {
		if (!world.isClient) {
			PlayerEntity player = (PlayerEntity) (Object) this;
			boolean vampire = BewitchmentAPI.isVampire(player, true);
			boolean werewolfBeast = BewitchmentAPI.isWerewolf(player, false);
			EntityAttributeInstance attackDamageAttribute = player.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE);
			EntityAttributeInstance attackSpeedAttribute = player.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_SPEED);
			EntityAttributeInstance armorAttribute = player.getAttributeInstance(EntityAttributes.GENERIC_ARMOR);
			EntityAttributeInstance armorToughnessAttribute = player.getAttributeInstance(EntityAttributes.GENERIC_ARMOR_TOUGHNESS);
			EntityAttributeInstance movementSpeedAttribute = player.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED);
			EntityAttributeInstance attackRange = player.getAttributeInstance(ReachEntityAttributes.ATTACK_RANGE);
			EntityAttributeInstance reach = player.getAttributeInstance(ReachEntityAttributes.REACH);
			EntityAttributeInstance stepHeight = player.getAttributeInstance(StepHeightEntityAttributeMain.STEP_HEIGHT);
			boolean shouldHave = vampire && !BewitchmentAPI.isPledged(player, BWPledges.LILITH);
			if (shouldHave && !attackDamageAttribute.hasModifier(VAMPIRE_ATTACK_DAMAGE_MODIFIER_0)) {
				attackDamageAttribute.addPersistentModifier(VAMPIRE_ATTACK_DAMAGE_MODIFIER_0);
				movementSpeedAttribute.addPersistentModifier(VAMPIRE_MOVEMENT_SPEED_MODIFIER_0);
			}
			else if (!shouldHave && attackDamageAttribute.hasModifier(VAMPIRE_ATTACK_DAMAGE_MODIFIER_0)) {
				attackDamageAttribute.removeModifier(VAMPIRE_ATTACK_DAMAGE_MODIFIER_0);
				movementSpeedAttribute.removeModifier(VAMPIRE_MOVEMENT_SPEED_MODIFIER_0);
			}
			shouldHave = vampire && BewitchmentAPI.isPledged(player, BWPledges.LILITH);
			if (shouldHave && !attackDamageAttribute.hasModifier(VAMPIRE_ATTACK_DAMAGE_MODIFIER_1)) {
				attackDamageAttribute.addPersistentModifier(VAMPIRE_ATTACK_DAMAGE_MODIFIER_1);
				movementSpeedAttribute.addPersistentModifier(VAMPIRE_MOVEMENT_SPEED_MODIFIER_1);
			}
			else if (!shouldHave && attackDamageAttribute.hasModifier(VAMPIRE_ATTACK_DAMAGE_MODIFIER_1)) {
				attackDamageAttribute.removeModifier(VAMPIRE_ATTACK_DAMAGE_MODIFIER_1);
				movementSpeedAttribute.removeModifier(VAMPIRE_MOVEMENT_SPEED_MODIFIER_1);
			}
			shouldHave = werewolfBeast;
			if (shouldHave && !attackSpeedAttribute.hasModifier(WEREWOLF_ATTACK_SPEED_MODIFIER)) {
				attackSpeedAttribute.addPersistentModifier(WEREWOLF_ATTACK_SPEED_MODIFIER);
				armorAttribute.addPersistentModifier(WEREWOLF_ARMOR_MODIFIER);
				attackRange.addPersistentModifier(WEREWOLF_ATTACK_RANGE_MODIFIER);
				reach.addPersistentModifier(WEREWOLF_REACH_MODIFIER);
				stepHeight.addPersistentModifier(WEREWOLF_STEP_HEIGHT_MODIFIER);
			}
			else if (!shouldHave && attackSpeedAttribute.hasModifier(WEREWOLF_ATTACK_SPEED_MODIFIER)) {
				attackSpeedAttribute.removeModifier(WEREWOLF_ATTACK_SPEED_MODIFIER);
				armorAttribute.removeModifier(WEREWOLF_ARMOR_MODIFIER);
				attackRange.removeModifier(WEREWOLF_ATTACK_RANGE_MODIFIER);
				reach.removeModifier(WEREWOLF_REACH_MODIFIER);
				stepHeight.removeModifier(WEREWOLF_STEP_HEIGHT_MODIFIER);
			}
			shouldHave = werewolfBeast && !BewitchmentAPI.isPledged(player, BWPledges.HERNE);
			if (shouldHave && !attackDamageAttribute.hasModifier(WEREWOLF_ATTACK_DAMAGE_MODIFIER_0)) {
				attackDamageAttribute.addPersistentModifier(WEREWOLF_ATTACK_DAMAGE_MODIFIER_0);
				armorToughnessAttribute.addPersistentModifier(WEREWOLF_ARMOR_TOUGHNESS_MODIFIER_0);
				movementSpeedAttribute.addPersistentModifier(WEREWOLF_MOVEMENT_SPEED_MODIFIER_0);
			}
			else if (!shouldHave && attackDamageAttribute.hasModifier(WEREWOLF_ATTACK_DAMAGE_MODIFIER_0)) {
				attackDamageAttribute.removeModifier(WEREWOLF_ATTACK_DAMAGE_MODIFIER_0);
				armorToughnessAttribute.removeModifier(WEREWOLF_ARMOR_TOUGHNESS_MODIFIER_0);
				movementSpeedAttribute.removeModifier(WEREWOLF_MOVEMENT_SPEED_MODIFIER_0);
			}
			shouldHave = werewolfBeast && BewitchmentAPI.isPledged(player, BWPledges.HERNE);
			if (shouldHave && !attackDamageAttribute.hasModifier(WEREWOLF_ATTACK_DAMAGE_MODIFIER_1)) {
				attackDamageAttribute.addPersistentModifier(WEREWOLF_ATTACK_DAMAGE_MODIFIER_1);
				armorToughnessAttribute.addPersistentModifier(WEREWOLF_ARMOR_TOUGHNESS_MODIFIER_1);
				movementSpeedAttribute.addPersistentModifier(WEREWOLF_MOVEMENT_SPEED_MODIFIER_1);
			}
			else if (!shouldHave && attackDamageAttribute.hasModifier(WEREWOLF_ATTACK_DAMAGE_MODIFIER_1)) {
				attackDamageAttribute.removeModifier(WEREWOLF_ATTACK_DAMAGE_MODIFIER_1);
				armorToughnessAttribute.removeModifier(WEREWOLF_ARMOR_TOUGHNESS_MODIFIER_1);
				movementSpeedAttribute.removeModifier(WEREWOLF_MOVEMENT_SPEED_MODIFIER_1);
			}
			if (vampire) {
				boolean pledgedToLilith = BewitchmentAPI.isPledged(player, BWPledges.LILITH);
				player.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, Integer.MAX_VALUE, 0, true, false));
				if (((RespawnTimerAccessor) player).getRespawnTimer() <= 0 && player.world.isDay() && !player.world.isRaining() && player.world.isSkyVisible(player.getBlockPos()) && AllowVampireBurn.EVENT.invoker().allowBurn(player)) {
					player.setOnFireFor(8);
				}
				HungerManager hungerManager = player.getHungerManager();
				if (((BloodAccessor) player).getBlood() > 0) {
					if (AllowVampireHeal.EVENT.invoker().allowHeal((PlayerEntity) (Object) this, pledgedToLilith)) {
						if (player.age % (pledgedToLilith ? 30 : 40) == 0) {
							if (player.getHealth() < player.getMaxHealth()) {
								player.heal(1);
								hungerManager.addExhaustion(3);
							}
							if ((hungerManager.isNotFull() || hungerManager.getSaturationLevel() < 10) && ((BloodAccessor) player).drainBlood(1, false)) {
								hungerManager.add(1, 20);
							}
						}
					}
				}
				else {
					if (getAlternateForm()) {
						TransformationAbilityPacket.useAbility(player, true);
					}
					hungerManager.addExhaustion(Float.MAX_VALUE);
				}
				if (getAlternateForm()) {
					hungerManager.addExhaustion(0.5f);
					if (!pledgedToLilith) {
						TransformationAbilityPacket.useAbility(player, true);
					}
				}
			}
			if (BewitchmentAPI.isWerewolf(player, true)) {
				boolean forced = ((WerewolfAccessor) player).getForcedTransformation();
				if (!getAlternateForm() && player.world.isNight() && BewitchmentAPI.getMoonPhase(player.world) == 0 && player.world.isSkyVisible(player.getBlockPos())) {
					TransformationAbilityPacket.useAbility(player, true);
					((WerewolfAccessor) player).setForcedTransformation(true);
				}
				else if (getAlternateForm() && forced && (player.world.isDay() || BewitchmentAPI.getMoonPhase(player.world) != 0)) {
					TransformationAbilityPacket.useAbility(player, true);
					((WerewolfAccessor) player).setForcedTransformation(false);
				}
				if (getAlternateForm()) {
					player.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, Integer.MAX_VALUE, 0, true, false));
					player.getArmorItems().forEach(stack -> player.dropStack(stack.split(1)));
					if (BWUtil.isTool(player.getMainHandStack())) {
						player.dropStack(player.getMainHandStack().split(1));
					}
					if (BWUtil.isTool(player.getOffHandStack())) {
						player.dropStack(player.getOffHandStack().split(1));
					}
					if (!forced && !BewitchmentAPI.isPledged(player, BWPledges.HERNE)) {
						TransformationAbilityPacket.useAbility(player, true);
					}
				}
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
			if (BewitchmentAPI.isWerewolf(this, true)) {
				exhaustion *= 1.25f;
			}
			if (BewitchmentAPI.isWerewolf(this, false)) {
				exhaustion *= 2;
			}
		}
		return exhaustion;
	}
	
	@Inject(method = "getHurtSound", at = @At("HEAD"))
	private void getHurtSound(DamageSource source, CallbackInfoReturnable<SoundEvent> callbackInfo) {
		if (source == BWDamageSources.SUN) {
			world.playSound(null, getBlockPos(), SoundEvents.BLOCK_FIRE_EXTINGUISH, getSoundCategory(), getSoundVolume(), getSoundPitch());
		}
	}
	
	@Inject(method = "canFoodHeal", at = @At("RETURN"), cancellable = true)
	private void canFoodHeal(CallbackInfoReturnable<Boolean> callbackInfo) {
		if (callbackInfo.getReturnValue() && BewitchmentAPI.isVampire(this, true)) {
			callbackInfo.setReturnValue(false);
		}
	}
	
	@Inject(method = "eatFood", at = @At("HEAD"))
	private void eat(World world, ItemStack stack, CallbackInfoReturnable<ItemStack> callbackInfo) {
		if (!world.isClient) {
			FoodComponent foodComponent = stack.getItem().getFoodComponent();
			if (foodComponent != null) {
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
	}
	
	@Inject(method = "readCustomDataFromTag", at = @At("TAIL"))
	private void readCustomDataFromTag(CompoundTag tag, CallbackInfo callbackInfo) {
		if (tag.contains("Transformation")) {
			setTransformation(BWRegistries.TRANSFORMATIONS.get(new Identifier(tag.getString("Transformation"))));
		}
		setAlternateForm(tag.getBoolean("AlternateForm"));
		setForcedTransformation(tag.getBoolean("ForcedTransformation"));
		setWerewolfVariant(tag.getInt("WerewolfVariant"));
	}
	
	@Inject(method = "writeCustomDataToTag", at = @At("TAIL"))
	private void writeCustomDataToTag(CompoundTag tag, CallbackInfo callbackInfo) {
		tag.putString("Transformation", BWRegistries.TRANSFORMATIONS.getId(getTransformation()).toString());
		tag.putBoolean("AlternateForm", getAlternateForm());
		tag.putBoolean("ForcedTransformation", getForcedTransformation());
		tag.putInt("WerewolfVariant", getWerewolfVariant());
	}
	
	@Inject(method = "initDataTracker", at = @At("TAIL"))
	private void initDataTracker(CallbackInfo callbackInfo) {
		dataTracker.startTracking(TRANSFORMATION, BWRegistries.TRANSFORMATIONS.getId(BWTransformations.HUMAN).toString());
		dataTracker.startTracking(ALTERNATE_FORM, false);
		dataTracker.startTracking(WEREWOLF_VARIANT, 0);
	}
}
