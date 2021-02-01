package moriyashiine.bewitchment.mixin;

import com.jamieswhiteshirt.reachentityattributes.ReachEntityAttributes;
import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.api.interfaces.entity.*;
import moriyashiine.bewitchment.api.registry.Fortune;
import moriyashiine.bewitchment.api.registry.Transformation;
import moriyashiine.bewitchment.common.entity.interfaces.PolymorphAccessor;
import moriyashiine.bewitchment.common.entity.interfaces.RespawnTimerAccessor;
import moriyashiine.bewitchment.common.entity.interfaces.WerewolfAccessor;
import moriyashiine.bewitchment.common.item.CaduceusItem;
import moriyashiine.bewitchment.common.item.ScepterItem;
import moriyashiine.bewitchment.common.network.packet.TransformationAbilityPacket;
import moriyashiine.bewitchment.common.registry.*;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
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
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
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
public abstract class PlayerEntityMixin extends LivingEntity implements MagicAccessor, PolymorphAccessor, FortuneAccessor, ContractAccessor, TransformationAccessor, RespawnTimerAccessor, WerewolfAccessor {
	private static final TrackedData<Integer> MAGIC = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.INTEGER);
	private static final TrackedData<Integer> MAGIC_TIMER = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.INTEGER);
	
	private static final TrackedData<String> TRANSFORMATION = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.STRING);
	private static final TrackedData<Boolean> ALTERNATE_FORM = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
	
	private static final TrackedData<String> POLYMORPH_UUID = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.STRING);
	private static final TrackedData<String> POLYMORPH_NAME = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.STRING);
	
	private static final TrackedData<Integer> WEREWOLF_VARIANT = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.INTEGER);
	
	private static final EntityAttributeModifier WOLF_FAMILIAR_ARMOR_MODIFIER = new EntityAttributeModifier(UUID.fromString("1b2866e6-ca04-43e4-b643-1142c0791e6d"), "Familiar modifier", 6, EntityAttributeModifier.Operation.ADDITION);
	private static final EntityAttributeModifier WOLF_FAMILIAR_ARMOR_TOUGHNESS_MODIFIER = new EntityAttributeModifier(UUID.fromString("ec7f7a2e-d5c5-40c4-9338-c2808946f7c4"), "Familiar modifier", 6, EntityAttributeModifier.Operation.ADDITION);
	
	private static final EntityAttributeModifier VAMPIRE_ATTACK_DAMAGE_MODIFIER_0 = new EntityAttributeModifier(UUID.fromString("066862f6-989c-4f35-ac6d-2696b91a1a5b"), "Transformation modifier", 2, EntityAttributeModifier.Operation.ADDITION);
	private static final EntityAttributeModifier VAMPIRE_ATTACK_DAMAGE_MODIFIER_1 = new EntityAttributeModifier(UUID.fromString("d2be3564-97e7-42c9-88c5-6b753472e37f"), "Transformation modifier", 4, EntityAttributeModifier.Operation.ADDITION);
	private static final EntityAttributeModifier VAMPIRE_MOVEMENT_SPEED_MODIFIER_0 = new EntityAttributeModifier(UUID.fromString("a782c03d-af7b-4eb7-b997-dd396bfdc7a0"), "Transformation modifier", 0.04, EntityAttributeModifier.Operation.ADDITION);
	private static final EntityAttributeModifier VAMPIRE_MOVEMENT_SPEED_MODIFIER_1 = new EntityAttributeModifier(UUID.fromString("7c7a61eb-83e8-4e85-94d6-a410a4153d6d"), "Transformation modifier", 0.08, EntityAttributeModifier.Operation.ADDITION);
	
	private static final EntityAttributeModifier WEREWOLF_ARMOR_MODIFIER = new EntityAttributeModifier(UUID.fromString("f00b0b0f-8ad6-4a2f-bdf5-6c337ffee56c"), "Transformation modifier", 20, EntityAttributeModifier.Operation.ADDITION);
	private static final EntityAttributeModifier WEREWOLF_ATTACK_RANGE_MODIFIER = new EntityAttributeModifier(UUID.fromString("ae0e4c0a-971f-4629-99ad-60c115112c1d"), "Transformation modifier", 1, EntityAttributeModifier.Operation.ADDITION);
	private static final EntityAttributeModifier WEREWOLF_REACH_MODIFIER = new EntityAttributeModifier(UUID.fromString("4c6d90ab-41ad-4d8a-b77a-7329361d3a7b"), "Transformation modifier", 1, EntityAttributeModifier.Operation.ADDITION);
	private static final EntityAttributeModifier WEREWOLF_ARMOR_TOUGHNESS_MODIFIER_0 = new EntityAttributeModifier(UUID.fromString("44f17821-1e30-426f-81d7-cd1da88fa584"), "Transformation modifier", 10, EntityAttributeModifier.Operation.ADDITION);
	private static final EntityAttributeModifier WEREWOLF_ARMOR_TOUGHNESS_MODIFIER_1 = new EntityAttributeModifier(UUID.fromString("edfd078d-e25c-4e27-ad91-c2b32037c8be"), "Transformation modifier", 20, EntityAttributeModifier.Operation.ADDITION);
	
	private static final EntityAttributeModifier WEREWOLF_ATTACK_DAMAGE_MODIFIER_0 = new EntityAttributeModifier(UUID.fromString("06861902-ebc4-4e6e-956c-59c1ae3085c7"), "Transformation modifier", 6, EntityAttributeModifier.Operation.ADDITION);
	private static final EntityAttributeModifier WEREWOLF_ATTACK_DAMAGE_MODIFIER_1 = new EntityAttributeModifier(UUID.fromString("10c0bedf-bde5-4cae-8acc-90b1204731dd"), "Transformation modifier", 10, EntityAttributeModifier.Operation.ADDITION);
	private static final EntityAttributeModifier WEREWOLF_MOVEMENT_SPEED_MODIFIER_0 = new EntityAttributeModifier(UUID.fromString("e26a276a-86cd-44db-9091-acd42fc00d95"), "Transformation modifier", 0.08, EntityAttributeModifier.Operation.ADDITION);
	private static final EntityAttributeModifier WEREWOLF_MOVEMENT_SPEED_MODIFIER_1 = new EntityAttributeModifier(UUID.fromString("718104a6-aa19-4b53-bad9-1f9edd46d38a"), "Transformation modifier", 0.16, EntityAttributeModifier.Operation.ADDITION);
	
	
	private Fortune.Instance fortune = null;
	
	private int respawnTimer = 400;
	
	private boolean forcedTransformation = false;
	
	@Shadow
	public abstract HungerManager getHungerManager();
	
	@Shadow
	public abstract void sendAbilitiesUpdate();
	
	@Shadow
	@Final
	public PlayerAbilities abilities;
	
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
	public String getPolymorphUUID() {
		return dataTracker.get(POLYMORPH_UUID);
	}
	
	@Override
	public void setPolymorphUUID(String uuid) {
		dataTracker.set(POLYMORPH_UUID, uuid);
	}
	
	@Override
	public String getPolymorphName() {
		return dataTracker.get(POLYMORPH_NAME);
	}
	
	@Override
	public void setPolymorphName(String name) {
		dataTracker.set(POLYMORPH_NAME, name);
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
	
	@Inject(method = "tick", at = @At("TAIL"))
	private void tick(CallbackInfo callbackInfo) {
		if (!world.isClient) {
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
			boolean vampire = BewitchmentAPI.isVampire(this, true);
			boolean werewolfBoth = BewitchmentAPI.isWerewolf(this, true);
			boolean werewolfBeast = BewitchmentAPI.isWerewolf(this, false);
			if (age % 20 == 0) {
				if (vampire || werewolfBeast) {
					addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, Integer.MAX_VALUE, 0, true, false));
				}
				boolean shouldHave = BewitchmentAPI.getFamiliar((PlayerEntity) (Object) this) == EntityType.WOLF;
				EntityAttributeInstance attackDamageAttribute = getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE);
				EntityAttributeInstance armorAttribute = getAttributeInstance(EntityAttributes.GENERIC_ARMOR);
				EntityAttributeInstance armorToughnessAttribute = getAttributeInstance(EntityAttributes.GENERIC_ARMOR_TOUGHNESS);
				EntityAttributeInstance movementSpeedAttribute = getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED);
				EntityAttributeInstance attackRange = getAttributeInstance(ReachEntityAttributes.ATTACK_RANGE);
				EntityAttributeInstance reach = getAttributeInstance(ReachEntityAttributes.REACH);
				if (shouldHave && !armorAttribute.hasModifier(WOLF_FAMILIAR_ARMOR_MODIFIER)) {
					armorAttribute.addPersistentModifier(WOLF_FAMILIAR_ARMOR_MODIFIER);
					armorToughnessAttribute.addPersistentModifier(WOLF_FAMILIAR_ARMOR_TOUGHNESS_MODIFIER);
				}
				else if (!shouldHave && armorAttribute.hasModifier(WOLF_FAMILIAR_ARMOR_MODIFIER)) {
					armorAttribute.removeModifier(WOLF_FAMILIAR_ARMOR_MODIFIER);
					armorToughnessAttribute.removeModifier(WOLF_FAMILIAR_ARMOR_TOUGHNESS_MODIFIER);
				}
				shouldHave = vampire && !BewitchmentAPI.isPledged(world, BWPledges.LILITH, getUuid());
				if (shouldHave && !attackDamageAttribute.hasModifier(VAMPIRE_ATTACK_DAMAGE_MODIFIER_0)) {
					attackDamageAttribute.addPersistentModifier(VAMPIRE_ATTACK_DAMAGE_MODIFIER_0);
					movementSpeedAttribute.addPersistentModifier(VAMPIRE_MOVEMENT_SPEED_MODIFIER_0);
				}
				else if (!shouldHave && attackDamageAttribute.hasModifier(VAMPIRE_ATTACK_DAMAGE_MODIFIER_0)) {
					attackDamageAttribute.removeModifier(VAMPIRE_ATTACK_DAMAGE_MODIFIER_0);
					movementSpeedAttribute.removeModifier(VAMPIRE_MOVEMENT_SPEED_MODIFIER_0);
				}
				shouldHave = vampire && BewitchmentAPI.isPledged(world, BWPledges.LILITH, getUuid());
				if (shouldHave && !attackDamageAttribute.hasModifier(VAMPIRE_ATTACK_DAMAGE_MODIFIER_1)) {
					attackDamageAttribute.addPersistentModifier(VAMPIRE_ATTACK_DAMAGE_MODIFIER_1);
					movementSpeedAttribute.addPersistentModifier(VAMPIRE_MOVEMENT_SPEED_MODIFIER_1);
				}
				else if (!shouldHave && attackDamageAttribute.hasModifier(VAMPIRE_ATTACK_DAMAGE_MODIFIER_1)) {
					attackDamageAttribute.removeModifier(VAMPIRE_ATTACK_DAMAGE_MODIFIER_1);
					movementSpeedAttribute.removeModifier(VAMPIRE_MOVEMENT_SPEED_MODIFIER_1);
				}
				shouldHave = werewolfBeast;
				if (shouldHave && !armorAttribute.hasModifier(WEREWOLF_ARMOR_MODIFIER)) {
					armorAttribute.addPersistentModifier(WEREWOLF_ARMOR_MODIFIER);
					attackRange.addPersistentModifier(WEREWOLF_ATTACK_RANGE_MODIFIER);
					reach.addPersistentModifier(WEREWOLF_REACH_MODIFIER);
				}
				else if (!shouldHave && armorAttribute.hasModifier(WEREWOLF_ARMOR_MODIFIER)) {
					armorAttribute.removeModifier(WEREWOLF_ARMOR_MODIFIER);
					attackRange.removeModifier(WEREWOLF_ATTACK_RANGE_MODIFIER);
					reach.removeModifier(WEREWOLF_REACH_MODIFIER);
				}
				shouldHave = werewolfBeast && !BewitchmentAPI.isPledged(world, BWPledges.HERNE, getUuid());
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
				shouldHave = werewolfBeast && BewitchmentAPI.isPledged(world, BWPledges.HERNE, getUuid());
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
			}
			if (getRespawnTimer() > 0) {
				setRespawnTimer(getRespawnTimer() - 1);
			}
			if (vampire) {
				if (getRespawnTimer() <= 0 && world.isDay() && !world.isRaining() && world.isSkyVisible(getBlockPos())) {
					setOnFireFor(8);
				}
				HungerManager hungerManager = getHungerManager();
				if (((BloodAccessor) this).getBlood() > 0) {
					if (age % (BewitchmentAPI.isPledged(world, BWPledges.LILITH, getUuid()) ? 30 : 40) == 0) {
						if (getHealth() < getMaxHealth()) {
							heal(1);
							hungerManager.addExhaustion(3);
						}
						if ((hungerManager.isNotFull() || hungerManager.getSaturationLevel() < 10) && ((BloodAccessor) this).drainBlood(1, false)) {
							hungerManager.add(1, 20);
						}
					}
				}
				else {
					if (getAlternateForm()) {
						TransformationAbilityPacket.useAbility((PlayerEntity) (Object) this, true);
					}
					hungerManager.addExhaustion(Float.MAX_VALUE);
				}
				if (getAlternateForm()) {
					if (!abilities.flying) {
						abilities.flying = true;
						sendAbilitiesUpdate();
					}
					hungerManager.addExhaustion(0.5f);
				}
			}
			else if (werewolfBoth) {
				if (!werewolfBeast && BewitchmentAPI.getMoonPhase(world) == 0 && world.isNight() && world.isSkyVisible(getBlockPos())) {
					TransformationAbilityPacket.useAbility((PlayerEntity) (Object) this, true);
					setForcedTransformation(true);
				}
				else if (werewolfBeast && BewitchmentAPI.getMoonPhase(world) != 0 && getForcedTransformation()) {
					TransformationAbilityPacket.useAbility((PlayerEntity) (Object) this, true);
					setForcedTransformation(false);
				}
				if (werewolfBeast) {
					getArmorItems().forEach(stack -> dropStack(stack.split(1)));
					if (isTool(getMainHandStack())) {
						dropStack(getMainHandStack().split(1));
					}
					if (isTool(getOffHandStack())) {
						dropStack(getOffHandStack().split(1));
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
	
	@Inject(method = "getHurtSound", at = @At("HEAD"))
	private void getHurtSound(DamageSource source, CallbackInfoReturnable<SoundEvent> callbackInfo) {
		if (source == BWDamageSources.SUN) {
			world.playSound(null, getBlockPos(), SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.PLAYERS, 1, 1);
		}
	}
	
	@Inject(method = "canFoodHeal", at = @At("HEAD"), cancellable = true)
	private void canFoodHeal(CallbackInfoReturnable<Boolean> callbackInfo) {
		if (BewitchmentAPI.isVampire(this, true)) {
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
					fillMagic(foodComponent.getHunger() * 100, false);
				}
				if (hasContract(BWContracts.GLUTTONY)) {
					getHungerManager().add(foodComponent.getHunger(), foodComponent.getSaturationModifier());
				}
			}
			if (BewitchmentAPI.isVampire(this, true) || (BewitchmentAPI.isWerewolf(this, true) && !foodComponent.isMeat())) {
				addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, 100, 1));
				addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 100, 1));
				addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 100, 1));
				addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 100, 1));
			}
		}
	}
	
	@Inject(method = "dropItem(Lnet/minecraft/item/ItemStack;ZZ)Lnet/minecraft/entity/ItemEntity;", at = @At("HEAD"))
	private void dropItem(ItemStack stack, boolean throwRandomly, boolean retainOwnership, CallbackInfoReturnable<ItemEntity> callbackInfo) {
		if (!world.isClient && stack.getItem() == BWObjects.VOODOO_POPPET) {
			LivingEntity owner = BewitchmentAPI.getTaglockOwner(world, stack);
			if (owner != null && !owner.getUuid().equals(getUuid())) {
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
				owner.addVelocity(getRotationVector().x / 2, getRotationVector().y / 2, getRotationVector().z / 2);
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
		setPolymorphUUID(tag.getString("PolymorphUUID"));
		setPolymorphName(tag.getString("PolymorphName"));
		setRespawnTimer(tag.getInt("RespawnTimer"));
		setForcedTransformation(tag.getBoolean("ForcedTransformation"));
		setWerewolfVariant(tag.getInt("WerewolfVariant"));
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
		tag.putString("PolymorphUUID", getPolymorphUUID());
		tag.putString("PolymorphName", getPolymorphName());
		tag.putInt("RespawnTimer", getRespawnTimer());
		tag.putBoolean("ForcedTransformation", getForcedTransformation());
		tag.putInt("WerewolfVariant", getWerewolfVariant());
	}
	
	@Inject(method = "initDataTracker", at = @At("TAIL"))
	private void initDataTracker(CallbackInfo callbackInfo) {
		dataTracker.startTracking(MAGIC, 0);
		dataTracker.startTracking(MAGIC_TIMER, 60);
		dataTracker.startTracking(TRANSFORMATION, BWRegistries.TRANSFORMATIONS.getId(BWTransformations.HUMAN).toString());
		dataTracker.startTracking(ALTERNATE_FORM, false);
		dataTracker.startTracking(POLYMORPH_UUID, "");
		dataTracker.startTracking(POLYMORPH_NAME, "");
		dataTracker.startTracking(WEREWOLF_VARIANT, 0);
	}
	
	private static boolean isTool(ItemStack stack) {
		Item item = stack.getItem();
		return item instanceof ToolItem || item instanceof RangedWeaponItem || item instanceof ScepterItem || item instanceof CaduceusItem || item instanceof ShieldItem || item instanceof TridentItem;
	}
}
