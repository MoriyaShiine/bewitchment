package moriyashiine.bewitchment.common.entity.living;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.common.entity.living.util.BWHostileEntity;
import moriyashiine.bewitchment.common.registry.BWMaterials;
import moriyashiine.bewitchment.common.registry.BWObjects;
import moriyashiine.bewitchment.common.registry.BWRegistries;
import moriyashiine.bewitchment.common.registry.BWSoundEvents;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.SmallFireballEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.village.Merchant;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOfferList;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DemonEntity extends BWHostileEntity implements Merchant {
	public static TrackedData<Boolean> MALE = DataTracker.registerData(DemonEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
	
	private TradeOfferList tradeOffers = null;
	private PlayerEntity customer = null;
	
	private final SimpleInventory inventory = new SimpleInventory(6);
	
	public DemonEntity(EntityType<? extends HostileEntity> entityType, World world) {
		super(entityType, world);
		setPathfindingPenalty(PathNodeType.DANGER_FIRE, 0);
		setPathfindingPenalty(PathNodeType.DAMAGE_FIRE, 0);
		experiencePoints = 20;
	}
	
	public static DefaultAttributeContainer.Builder createAttributes() {
		return MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 200).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 6).add(EntityAttributes.GENERIC_ARMOR, 4).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25).add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0.75);
	}
	
	@Override
	protected boolean hasShiny() {
		return true;
	}
	
	@Override
	public int getVariants() {
		return 5;
	}
	
	@Override
	public EntityGroup getGroup() {
		return BewitchmentAPI.DEMON;
	}
	
	@Nullable
	@Override
	protected SoundEvent getAmbientSound() {
		return BWSoundEvents.ENTITY_DEMON_AMBIENT;
	}
	
	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return BWSoundEvents.ENTITY_DEMON_HURT;
	}
	
	@Override
	protected SoundEvent getDeathSound() {
		return BWSoundEvents.ENTITY_DEMON_DEATH;
	}
	
	@Override
	public SoundEvent getYesSound() {
		return getAmbientSound();
	}
	
	@Override
	protected ActionResult interactMob(PlayerEntity player, Hand hand) {
		if (isAlive() && !getOffers().isEmpty() && getCurrentCustomer() == null && getTarget() == null) {
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
	public boolean cannotDespawn() {
		return true;
	}
	
	@Override
	public void tick() {
		super.tick();
		if (!world.isClient) {
			if (customer != null) {
				navigation.stop();
			}
			LivingEntity target = getTarget();
			if (target != null) {
				lookAtEntity(target, 360, 360);
				if ((age + getEntityId()) % 40 == 0) {
					SmallFireballEntity fireball = new SmallFireballEntity(world, this, target.getX() - getX(), target.getBodyY(0.5) - getBodyY(0.5), target.getZ() - getZ());
					fireball.updatePosition(fireball.getX(), getBodyY(0.5), fireball.getZ());
					world.playSound(null, getBlockPos(), BWSoundEvents.ENTITY_GENERIC_SHOOT, SoundCategory.HOSTILE, 1, 1);
					world.spawnEntity(fireball);
					swingHand(Hand.MAIN_HAND);
				}
			}
		}
	}
	
	@Override
	public void onDeath(DamageSource source) {
		super.onDeath(source);
		setCurrentCustomer(null);
	}
	
	@Override
	public boolean tryAttack(Entity target) {
		boolean flag = super.tryAttack(target);
		if (flag && target instanceof LivingEntity) {
			target.setOnFireFor(6);
			target.addVelocity(0, 0.2, 0);
			swingHand(Hand.MAIN_HAND);
		}
		return flag;
	}
	
	@Override
	public void setTarget(@Nullable LivingEntity target) {
		super.setTarget(target);
		if (target != null) {
			setCurrentCustomer(null);
		}
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
			tradeOffers = TradeGenerator.build(random);
		}
		return tradeOffers;
	}
	
	@Override
	public void trade(TradeOffer offer) {
		offer.use();
	}
	
	@Override
	public void onSellingItem(ItemStack stack) {
		world.playSound(null, getBlockPos(), SoundEvents.ENTITY_ZOMBIE_AMBIENT, SoundCategory.HOSTILE, 1, getSoundPitch());
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
		dataTracker.set(MALE, tag.getBoolean("Male"));
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
		targetSelector.add(1, new FollowTargetGoal<>(this, LivingEntity.class, 10, true, false, entity -> entity.getGroup() != BewitchmentAPI.DEMON && BewitchmentAPI.getArmorPieces(entity, stack -> stack.getItem() instanceof ArmorItem && ((ArmorItem) stack.getItem()).getMaterial() == BWMaterials.BESMIRCHED_ARMOR) < 3));
	}
	
	@SuppressWarnings("ConstantConditions")
	private static class TradeGenerator {
		public static TradeOfferList build(Random random) {
			TradeOfferList offers = new TradeOfferList();
			for (int i = 0; i < 5; i++) {
				List<ItemStack> cost = generateCost(random, false);
				offers.add(new TradeOffer(cost.get(0), cost.size() > 1 ? cost.get(1) : ItemStack.EMPTY, generateRewardStack(random), Integer.MAX_VALUE, 0, 1));
			}
			List<ItemStack> cost = generateCost(random, true);
			ItemStack stack = new ItemStack(BWObjects.DEMONIC_CONTRACT);
			stack.getOrCreateTag().putString("Contract", BWRegistries.CONTRACTS.getId(BWRegistries.CONTRACTS.get(random.nextInt(BWRegistries.CONTRACTS.getEntries().size()))).toString());
			offers.add(new TradeOffer(cost.get(0), cost.get(1), stack, Integer.MAX_VALUE, 0, 1));
			return offers;
		}
		
		private static List<ItemStack> generateCost(Random random, boolean contract) {
			List<ItemStack> cost = new ArrayList<>();
			ItemStack stack = generateCostStack(random, contract ? 3 : 1);
			cost.add(stack);
			if (contract || random.nextBoolean()) {
				ItemStack stack2 = null;
				while (stack2 == null || stack.getItem() == stack2.getItem()) {
					stack2 = generateCostStack(random, contract ? 3 : 1);
				}
				cost.add(stack2);
			}
			return cost;
		}
		
		private static ItemStack generateCostStack(Random random, int costMultiplier) {
			switch (random.nextInt(9)) {
				case 0:
					return new ItemStack(Items.GOLD_INGOT, MathHelper.nextInt(random, 12, 20) * costMultiplier);
				case 1:
					return new ItemStack(Items.DIAMOND, MathHelper.nextInt(random, 2, 8) * costMultiplier);
				case 2:
					return new ItemStack(Items.BLAZE_ROD, MathHelper.nextInt(random, 5, 18) * costMultiplier);
				case 3:
					return new ItemStack(Items.FERMENTED_SPIDER_EYE, MathHelper.nextInt(random, 6, 15) * costMultiplier);
				case 4:
					switch (random.nextInt(4)) {
						case 0:
							return new ItemStack(Items.ZOMBIE_HEAD, MathHelper.nextInt(random, 1, 2) * costMultiplier);
						case 1:
							return new ItemStack(Items.CREEPER_HEAD, MathHelper.nextInt(random, 1, 2) * costMultiplier);
						case 2:
							return new ItemStack(Items.SKELETON_SKULL, MathHelper.nextInt(random, 1, 2) * costMultiplier);
						case 3:
							return new ItemStack(Items.WITHER_SKELETON_SKULL, costMultiplier);
					}
				case 5:
					return new ItemStack(BWObjects.BESMIRCHED_WOOL, MathHelper.nextInt(random, 4, 14) * costMultiplier);
				case 6:
					return new ItemStack(BWObjects.SNAKE_TONGUE, MathHelper.nextInt(random, 6, 13) * costMultiplier);
				case 7:
					return new ItemStack(BWObjects.DEMON_HORN, MathHelper.nextInt(random, 3, 14) * costMultiplier);
				case 8:
					return new ItemStack(BWObjects.BOTTLE_OF_BLOOD);
				default:
					return ItemStack.EMPTY;
			}
		}
		
		private static ItemStack generateRewardStack(Random random) {
			switch (random.nextInt(7)) {
				case 0:
					return new ItemStack(Items.GHAST_TEAR, MathHelper.nextInt(random, 2, 5));
				case 1:
					return new ItemStack(Items.WARPED_FUNGUS, MathHelper.nextInt(random, 2, 5));
				case 2:
					return new ItemStack(Items.NETHERITE_SCRAP, MathHelper.nextInt(random, 1, 3));
				case 3:
					return new ItemStack(Items.SKELETON_SKULL, MathHelper.nextInt(random, 1, 2));
				case 4:
					return new ItemStack(Items.WITHER_SKELETON_SKULL);
				case 5:
					return new ItemStack(Items.GILDED_BLACKSTONE, MathHelper.nextInt(random, 24, 48));
				case 6:
					return new ItemStack(BWObjects.DEMON_HEART);
				default:
					return new ItemStack(Items.BEDROCK);
			}
		}
	}
}
