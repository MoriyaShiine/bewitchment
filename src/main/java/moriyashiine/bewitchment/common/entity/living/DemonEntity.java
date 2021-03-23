package moriyashiine.bewitchment.common.entity.living;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.api.interfaces.entity.ContractAccessor;
import moriyashiine.bewitchment.api.interfaces.entity.CurseAccessor;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.entity.living.util.BWHostileEntity;
import moriyashiine.bewitchment.common.misc.BWUtil;
import moriyashiine.bewitchment.common.recipe.DemonTrade;
import moriyashiine.bewitchment.common.registry.*;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
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
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Box;
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

@SuppressWarnings("ConstantConditions")
public class DemonEntity extends BWHostileEntity implements Merchant {
	public static final TradeOfferList EMPTY = new TradeOfferList();
	
	public static final TrackedData<Boolean> MALE = DataTracker.registerData(DemonEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
	
	private TradeOfferList tradeOffers = null;
	private PlayerEntity customer = null;
	
	private int refreshTimer = 0;
	
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
	public void tick() {
		super.tick();
		if (!world.isClient) {
			refreshTimer++;
			if (refreshTimer >= 168000) {
				if (Bewitchment.config.doDemonTradesRefresh) {
					for (TradeOffer offer : getOffers()) {
						offer.resetUses();
					}
				}
				refreshTimer = 0;
			}
			if (customer != null) {
				navigation.stop();
			}
			LivingEntity target = getTarget();
			if (target != null) {
				lookAtEntity(target, 360, 360);
				if ((age + getEntityId()) % 40 == 0) {
					SmallFireballEntity fireball = new SmallFireballEntity(world, this, target.getX() - getX(), target.getBodyY(0.5) - getBodyY(0.5), target.getZ() - getZ());
					fireball.updatePosition(fireball.getX(), getBodyY(0.5), fireball.getZ());
					world.playSound(null, getBlockPos(), BWSoundEvents.ENTITY_GENERIC_SHOOT, getSoundCategory(), getSoundVolume(), getSoundPitch());
					world.spawnEntity(fireball);
					swingHand(Hand.MAIN_HAND);
				}
			}
		}
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
		if (!world.isClient && isAlive() && getCurrentCustomer() == null && getTarget() == null) {
			setCurrentCustomer(player);
			TradeOfferList offers = getOffers();
			if (rejectTradesFromCurses(this) || rejectTradesFromContracts(this)) {
				offers = EMPTY;
			}
			if (!offers.isEmpty()) {
				sendOffers(player, getDisplayName(), 0);
			}
			else {
				setCurrentCustomer(null);
			}
		}
		return ActionResult.success(world.isClient);
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
			tradeOffers = TradeGenerator.build((ServerWorld) world, getCurrentCustomer() != null ? getCurrentCustomer().getLuck() : 0F);
		}
		return tradeOffers;
	}
	
	@Override
	public void trade(TradeOffer offer) {
		offer.use();
	}
	
	@Override
	public void onSellingItem(ItemStack stack) {
		world.playSound(null, getBlockPos(), getAmbientSound(), getSoundCategory(), getSoundVolume(), getSoundPitch());
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
		refreshTimer = tag.getInt("RefreshTimer");
	}
	
	@Override
	public void writeCustomDataToTag(CompoundTag tag) {
		super.writeCustomDataToTag(tag);
		tag.putBoolean("Male", dataTracker.get(MALE));
		if (!getOffers().isEmpty()) {
			tag.put("Offers", tradeOffers.toTag());
		}
		tag.putInt("RefreshTimer", refreshTimer);
	}
	
	@Override
	protected void initDataTracker() {
		super.initDataTracker();
		dataTracker.startTracking(MALE, true);
	}
	
	@Override
	protected void initGoals() {
		goalSelector.add(0, new SwimGoal(this));
		goalSelector.add(1, new MeleeAttackGoal(this, 1, true));
		goalSelector.add(2, new WanderAroundFarGoal(this, 1));
		goalSelector.add(3, new LookAtEntityGoal(this, PlayerEntity.class, 8));
		goalSelector.add(3, new LookAroundGoal(this));
		targetSelector.add(0, new RevengeGoal(this));
		targetSelector.add(1, new FollowTargetGoal<>(this, LivingEntity.class, 10, true, false, entity -> BWUtil.getArmorPieces(entity, stack -> stack.getItem() instanceof ArmorItem && ((ArmorItem) stack.getItem()).getMaterial() == BWMaterials.BESMIRCHED_ARMOR) < 3 && (entity.getGroup() != BewitchmentAPI.DEMON || entity instanceof PlayerEntity)));
	}
	
	public static boolean rejectTradesFromCurses(LivingEntity merchant) {
		return !merchant.world.getEntitiesByClass(LivingEntity.class, new Box(merchant.getBlockPos()).expand(8), entity -> merchant.canSee(entity) && entity.isAlive() && ((CurseAccessor) entity).hasCurse(BWCurses.APATHY)).isEmpty();
	}
	
	public static boolean rejectTradesFromContracts(LivingEntity merchant) {
		return !merchant.world.getEntitiesByClass(LivingEntity.class, new Box(merchant.getBlockPos()).expand(8), entity -> merchant.canSee(entity) && entity.isAlive() && (((ContractAccessor) entity).hasContract(BWContracts.FRAUD) && ((ContractAccessor) entity).hasNegativeEffects())).isEmpty();
	}
	
	@SuppressWarnings("ConstantConditions")
	private static class TradeGenerator {
		public static TradeOfferList build(ServerWorld world, float luck) {
			TradeOfferList offers = new TradeOfferList();
			LootContext context = new LootContext.Builder(world).luck(luck).build(LootContextTypes.EMPTY);
			List<DemonTrade> trades = new ArrayList<>(DemonTrade.TRADES.values());
			int amount = (int) (4 + world.random.nextInt(4) + 2 * luck);
			for (int i = 0; i < amount; i++) {
				if (trades.isEmpty()) {
					break;
				}
				DemonTrade trade = trades.get(world.random.nextInt(trades.size()));
				if (world.random.nextFloat() < trade.getChance(context)) {
					offers.add(trade.generate(context));
					trades.remove(trade);
				}
			}
			offers.add(generateContractOffer(world.random));
			return offers;
		}
		
		private static TradeOffer generateContractOffer(Random random) {
			ItemStack stack = new ItemStack(BWObjects.DEMONIC_CONTRACT);
			stack.getOrCreateTag().putString("Contract", BWRegistries.CONTRACTS.getId(BWRegistries.CONTRACTS.get(random.nextInt(BWRegistries.CONTRACTS.getEntries().size()))).toString());
			stack.getOrCreateTag().putInt("Duration", 168000);
			switch (random.nextInt(7)) {
				case 0:
					return new TradeOffer(new ItemStack(Items.GOLD_INGOT, MathHelper.nextInt(random, 10, 20)), new ItemStack(BWObjects.BOTTLE_OF_BLOOD, MathHelper.nextInt(random, 2, 4)), stack, 1, 0, 1);
				case 1:
					return new TradeOffer(new ItemStack(Items.DIAMOND, MathHelper.nextInt(random, 3, 4)), new ItemStack(BWObjects.BOTTLE_OF_BLOOD, MathHelper.nextInt(random, 2, 4)), stack, 1, 0, 1);
				case 2:
					return new TradeOffer(new ItemStack(Items.BLAZE_ROD, MathHelper.nextInt(random, 6, 13)), new ItemStack(BWObjects.BOTTLE_OF_BLOOD, MathHelper.nextInt(random, 2, 3)), stack, 1, 0, 1);
				case 3:
					return new TradeOffer(new ItemStack(getRandomHead(random), MathHelper.nextInt(random, 1, 3)), new ItemStack(BWObjects.BOTTLE_OF_BLOOD, MathHelper.nextInt(random, 2, 3)), stack, 1, 0, 1);
				case 4:
					return new TradeOffer(new ItemStack(Items.WITHER_SKELETON_SKULL, 1), new ItemStack(BWObjects.BOTTLE_OF_BLOOD, MathHelper.nextInt(random, 2, 5)), stack, 1, 0, 1);
				case 5:
					return new TradeOffer(new ItemStack(BWObjects.SNAKE_TONGUE, MathHelper.nextInt(random, 5, 8)), new ItemStack(BWObjects.BOTTLE_OF_BLOOD, MathHelper.nextInt(random, 1, 3)), stack, 1, 0, 1);
				case 6:
					return new TradeOffer(new ItemStack(BWObjects.DEMON_HORN, MathHelper.nextInt(random, 3, 8)), new ItemStack(BWObjects.BOTTLE_OF_BLOOD, MathHelper.nextInt(random, 2, 3)), stack, 1, 0, 1);
			}
			return null;
		}
		
		private static Item getRandomHead(Random random) {
			int value = random.nextInt(3);
			return value == 0 ? Items.ZOMBIE_HEAD : value == 1 ? Items.CREEPER_HEAD : Items.SKELETON_SKULL;
		}
	}
}
