package moriyashiine.bewitchment.common.entity.living;

import moriyashiine.bewitchment.common.entity.living.util.BWHostileEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.village.Merchant;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOfferList;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class DemonEntity extends BWHostileEntity implements Merchant {
	public static TrackedData<Boolean> MALE = DataTracker.registerData(DemonEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
	
	private TradeOfferList tradeOffers = null;
	private PlayerEntity customer = null;
	
	private final SimpleInventory inventory = new SimpleInventory(6);
	
	public DemonEntity(EntityType<? extends HostileEntity> entityType, World world) {
		super(entityType, world);
		experiencePoints = 20;
	}
	
	public static DefaultAttributeContainer.Builder createAttributes() {
		return MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 200).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 6).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25).add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0.75);
	}
	
	@Override
	protected boolean hasShiny() {
		return true;
	}
	
	@Override
	public int getVariants() {
		return 5;
	}
	
	@Nullable
	@Override
	protected SoundEvent getAmbientSound() {
		return SoundEvents.ENTITY_BLAZE_AMBIENT;
	}
	
	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return SoundEvents.ENTITY_BLAZE_HURT;
	}
	
	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.ENTITY_BLAZE_DEATH;
	}
	
	@Override
	public SoundEvent getYesSound() {
		return SoundEvents.ENTITY_BLAZE_AMBIENT;
	}
	
	@Override
	protected float getSoundPitch() {
		return 0.5f;
	}
	
	@Override
	protected ActionResult interactMob(PlayerEntity player, Hand hand) {
		if (isAlive() && !getOffers().isEmpty() && getCurrentCustomer() == null) {
			boolean client = world.isClient;
			if (!client) {
				setCurrentCustomer(player);
				sendOffers(player, getDisplayName(), 0);
			}
			return ActionResult.success(client);
		}
		return super.interactMob(player, hand);
	}
	
	@Override
	public boolean canBeLeashedBy(PlayerEntity player) {
		return false;
	}
	
	@Override
	public void onDeath(DamageSource source) {
		super.onDeath(source);
		setCurrentCustomer(null);
	}
	
	@Override
	public World getMerchantWorld() {
		return world;
	}
	
	@Nullable
	@Override
	public PlayerEntity getCurrentCustomer() {
		return customer;
	}
	
	@Override
	public void setCurrentCustomer(@Nullable PlayerEntity customer) {
		this.customer = customer;
	}
	
	@Override
	public TradeOfferList getOffers() {
		if (tradeOffers == null) {
			tradeOffers = new TradeOfferList();
		}
		return tradeOffers;
	}
	
	@Override
	public void trade(TradeOffer offer) {
		offer.use();
	}
	
	@Override
	public void onSellingItem(ItemStack stack) {
		world.playSound(null, getBlockPos(), SoundEvents.ENTITY_VILLAGER_TRADE, SoundCategory.HOSTILE, 1, 0.5f);
	}
	
	@Override
	public boolean isLeveledMerchant() {
		return false;
	}
	
	@Override
	public int getExperience() {
		return 0;
	}
	
	@Environment(EnvType.CLIENT)
	@Override
	public void setOffersFromServer(@Nullable TradeOfferList offers) {
	}
	
	@Override
	public void setExperienceFromServer(int experience) {
	}
	
	@Override
	public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable CompoundTag entityTag) {
		dataTracker.set(MALE, random.nextBoolean());
		return super.initialize(world, difficulty, spawnReason, entityData, entityTag);
	}
	
	@Override
	public void readCustomDataFromTag(CompoundTag tag) {
		super.readCustomDataFromTag(tag);
		tag.putBoolean("Male", dataTracker.get(MALE));
		if (tag.contains("Offers")) {
			tradeOffers = new TradeOfferList(tag.getCompound("Offers"));
		}
		inventory.readTags(tag.getList("Inventory", NbtType.COMPOUND));
	}
	
	@Override
	public void writeCustomDataToTag(CompoundTag tag) {
		super.writeCustomDataToTag(tag);
		tag.putBoolean("Male", dataTracker.get(MALE));
		if (!getOffers().isEmpty()) {
			tag.put("Offers", tradeOffers.toTag());
		}
		tag.put("Inventory", inventory.getTags());
	}
	
	@Override
	protected void initDataTracker() {
		super.initDataTracker();
		dataTracker.startTracking(MALE, false);
	}
	
	@Override
	protected void initGoals() {
		goalSelector.add(0, new SwimGoal(this));
		goalSelector.add(1, new MeleeAttackGoal(this, 1, true));
		goalSelector.add(2, new WanderAroundFarGoal(this, 1));
		goalSelector.add(3, new LookAtEntityGoal(this, PlayerEntity.class, 8));
		goalSelector.add(3, new LookAroundGoal(this));
		targetSelector.add(0, new RevengeGoal(this));
		targetSelector.add(1, new FollowTargetGoal<>(this, PlayerEntity.class, true));
		targetSelector.add(2, new FollowTargetGoal<>(this, IronGolemEntity.class, true));
		targetSelector.add(3, new FollowTargetGoal<>(this, MerchantEntity.class, 10, true, false, entity -> entity instanceof MerchantEntity));
	}
}
