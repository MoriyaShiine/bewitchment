package moriyashiine.bewitchment.mixin;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.api.interfaces.entity.*;
import moriyashiine.bewitchment.api.registry.Fortune;
import moriyashiine.bewitchment.common.entity.interfaces.PolymorphAccessor;
import moriyashiine.bewitchment.common.entity.interfaces.RespawnTimerAccessor;
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
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;
import java.util.UUID;

@SuppressWarnings("ConstantConditions")
@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements MagicAccessor, PolymorphAccessor, FortuneAccessor, ContractAccessor, TransformationAccessor, RespawnTimerAccessor {
	private static final TrackedData<Integer> MAGIC = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.INTEGER);
	private static final TrackedData<Integer> MAGIC_TIMER = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.INTEGER);
	
	private static final TrackedData<String> TRANSFORMATION = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.STRING);
	private static final TrackedData<Boolean> ALTERNATE_FORM = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
	
	private static final TrackedData<Optional<UUID>> POLYMORPH_UUID = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.OPTIONAL_UUID);
	private static final TrackedData<String> POLYMORPH_NAME = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.STRING);
	
	private static final EntityAttributeModifier WOLF_FAMILIAR_ARMOR_BONUS = new EntityAttributeModifier(UUID.fromString("1b2866e6-ca04-43e4-b643-1142c0791e6d"), "Familiar bonus", 6, EntityAttributeModifier.Operation.ADDITION);
	private static final EntityAttributeModifier WOLF_FAMILIAR_ARMOR_TOUGHNESS_BONUS = new EntityAttributeModifier(UUID.fromString("ec7f7a2e-d5c5-40c4-9338-c2808946f7c4"), "Familiar bonus", 6, EntityAttributeModifier.Operation.ADDITION);
	
	private static final EntityAttributeModifier VAMPIRE_ATTACK_DAMAGE_BONUS_0 = new EntityAttributeModifier(UUID.fromString("066862f6-989c-4f35-ac6d-2696b91a1a5b"), "Transformation bonus", 2, EntityAttributeModifier.Operation.ADDITION);
	private static final EntityAttributeModifier VAMPIRE_MOVEMENT_SPEED_BONUS_0 = new EntityAttributeModifier(UUID.fromString("a782c03d-af7b-4eb7-b997-dd396bfdc7a0"), "Transformation bonus", 0.04, EntityAttributeModifier.Operation.ADDITION);
	
	private Fortune.Instance fortune = null;
	
	private int respawnTimer = 400;
	
	@Shadow
	public abstract HungerManager getHungerManager();
	
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
	public String getTransformation() {
		return dataTracker.get(TRANSFORMATION);
	}
	
	@Override
	public void setTransformation(String transformation) {
		dataTracker.set(TRANSFORMATION, transformation);
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
	public Optional<UUID> getPolymorphUUID() {
		return dataTracker.get(POLYMORPH_UUID);
	}
	
	@Override
	public void setPolymorphUUID(Optional<UUID> uuid) {
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
			if (age % 20 == 0) {
				boolean shouldHave = BewitchmentAPI.getFamiliar((PlayerEntity) (Object) this) == EntityType.WOLF;
				EntityAttributeInstance attackDamageAttribute = getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE);
				EntityAttributeInstance armorAttribute = getAttributeInstance(EntityAttributes.GENERIC_ARMOR);
				EntityAttributeInstance armorToughnessAttribute = getAttributeInstance(EntityAttributes.GENERIC_ARMOR_TOUGHNESS);
				EntityAttributeInstance movementSpeedAttribute = getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED);
				if (shouldHave && !armorAttribute.hasModifier(WOLF_FAMILIAR_ARMOR_BONUS)) {
					armorAttribute.addPersistentModifier(WOLF_FAMILIAR_ARMOR_BONUS);
					armorToughnessAttribute.addPersistentModifier(WOLF_FAMILIAR_ARMOR_TOUGHNESS_BONUS);
				}
				else if (!shouldHave && armorAttribute.hasModifier(WOLF_FAMILIAR_ARMOR_BONUS)) {
					armorAttribute.removeModifier(WOLF_FAMILIAR_ARMOR_BONUS);
					armorToughnessAttribute.removeModifier(WOLF_FAMILIAR_ARMOR_TOUGHNESS_BONUS);
				}
				shouldHave = vampire;
				if (shouldHave && !attackDamageAttribute.hasModifier(VAMPIRE_ATTACK_DAMAGE_BONUS_0)) {
					attackDamageAttribute.addPersistentModifier(VAMPIRE_ATTACK_DAMAGE_BONUS_0);
					movementSpeedAttribute.addPersistentModifier(VAMPIRE_MOVEMENT_SPEED_BONUS_0);
				}
				else if (!shouldHave && attackDamageAttribute.hasModifier(VAMPIRE_ATTACK_DAMAGE_BONUS_0)) {
					attackDamageAttribute.removeModifier(VAMPIRE_ATTACK_DAMAGE_BONUS_0);
					movementSpeedAttribute.removeModifier(VAMPIRE_MOVEMENT_SPEED_BONUS_0);
				}
			}
			if (getRespawnTimer() > 0) {
				setRespawnTimer(getRespawnTimer() - 1);
			}
			if (vampire) {
				if (world.isDay() && world.isSkyVisible(getBlockPos())) {
					setOnFireFor(8);
				}
				if (age % 40 == 0) {
					HungerManager hungerManager = getHungerManager();
					if (((BloodAccessor) this).getBlood() > 0) {
						if (getHealth() < getMaxHealth()) {
							heal(1);
							hungerManager.addExhaustion(3);
						}
						if ((hungerManager.isNotFull() || hungerManager.getSaturationLevel() < 10) && ((BloodAccessor) this).drainBlood(1, false)) {
							hungerManager.add(1, 20);
						}
					}
					else {
						hungerManager.addExhaustion(Float.MAX_VALUE);
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
		if (!world.isClient && hasNegativeEffects() && hasContract(BWContracts.GLUTTONY)) {
			return exhaustion * 1.5f;
		}
		return exhaustion;
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
			if (BewitchmentAPI.isVampire(this, true)) {
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
			setTransformation(tag.getString("Transformation"));
		}
		setAlternateForm(tag.getBoolean("AlternateForm"));
		setPolymorphUUID(tag.getString("PolymorphUUID").isEmpty() ? Optional.empty() : Optional.of(UUID.fromString(tag.getString("PolymorphUUID"))));
		setPolymorphName(tag.getString("PolymorphName"));
		setRespawnTimer(tag.getInt("RespawnTimer"));
	}
	
	@Inject(method = "writeCustomDataToTag", at = @At("TAIL"))
	private void writeCustomDataToTag(CompoundTag tag, CallbackInfo callbackInfo) {
		tag.putInt("Magic", getMagic());
		if (getFortune() != null) {
			tag.putString("Fortune", BWRegistries.FORTUNES.getId(getFortune().fortune).toString());
			tag.putInt("FortuneDuration", getFortune().duration);
		}
		tag.putString("Transformation", getTransformation());
		tag.putBoolean("AlternativeForm", getAlternateForm());
		tag.putString("PolymorphUUID", getPolymorphUUID().isPresent() ? getPolymorphUUID().get().toString() : "");
		tag.putString("PolymorphName", getPolymorphName());
		tag.putInt("RespawnTimer", getRespawnTimer());
	}
	
	@Inject(method = "initDataTracker", at = @At("TAIL"))
	private void initDataTracker(CallbackInfo callbackInfo) {
		dataTracker.startTracking(MAGIC, 0);
		dataTracker.startTracking(MAGIC_TIMER, 60);
		dataTracker.startTracking(TRANSFORMATION, BWTransformations.HUMAN);
		dataTracker.startTracking(ALTERNATE_FORM, false);
		dataTracker.startTracking(POLYMORPH_UUID, Optional.empty());
		dataTracker.startTracking(POLYMORPH_NAME, "");
	}
}
