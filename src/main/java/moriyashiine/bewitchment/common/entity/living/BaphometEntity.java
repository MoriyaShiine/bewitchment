package moriyashiine.bewitchment.common.entity.living;

import com.google.common.collect.Sets;
import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.api.interfaces.entity.Pledgeable;
import moriyashiine.bewitchment.api.registry.Contract;
import moriyashiine.bewitchment.common.entity.living.util.BWHostileEntity;
import moriyashiine.bewitchment.common.misc.BWUtil;
import moriyashiine.bewitchment.common.registry.*;
import moriyashiine.bewitchment.mixin.StatusEffectAccessor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.village.Merchant;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOfferList;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

@SuppressWarnings("ConstantConditions")
public class BaphometEntity extends BWHostileEntity implements Pledgeable, Merchant {
	private final ServerBossBar bossBar;
	private int timeSinceLastAttack = 0;
	
	public int flameIndex = random.nextInt(8);
	
	private TradeOfferList tradeOffers = null;
	private PlayerEntity customer = null;
	
	private int refreshTimer = 0;
	
	public BaphometEntity(EntityType<? extends HostileEntity> entityType, World world) {
		super(entityType, world);
		bossBar = new ServerBossBar(getDisplayName(), BossBar.Color.RED, BossBar.Style.PROGRESS);
		setPathfindingPenalty(PathNodeType.DANGER_FIRE, 0);
		setPathfindingPenalty(PathNodeType.DAMAGE_FIRE, 0);
		experiencePoints = 50;
	}
	
	public static DefaultAttributeContainer.Builder createAttributes() {
		return MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 375).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 12).add(EntityAttributes.GENERIC_ARMOR, 6).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25).add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0.75).add(EntityAttributes.GENERIC_FOLLOW_RANGE, 32);
	}
	
	@Override
	public void tick() {
		super.tick();
		flameIndex = ++flameIndex % 8;
		if (!world.isClient) {
			bossBar.setPercent(getHealth() / getMaxHealth());
			refreshTimer++;
			if (refreshTimer >= 24000) {
				resetTradeList();
				refreshTimer = 0;
			}
			if (customer != null) {
				navigation.stop();
			}
			LivingEntity target = getTarget();
			int timer = age + getEntityId();
			if (timer % 20 == 0) {
				heal(1);
			}
			if (target != null) {
				timeSinceLastAttack++;
				if (timeSinceLastAttack >= 600) {
					BWUtil.teleport(this, target.getX(), target.getY(), target.getZ(), true);
					timeSinceLastAttack = 0;
				}
				lookAtEntity(target, 360, 360);
				if (timer % 60 == 0) {
					for (int i = -1; i <= 1; i++) {
						FireballEntity fireball = new FireballEntity(world, this, target.getX() - getX() + (i * 2), target.getBodyY(0.5) - getBodyY(0.5), target.getZ() - getZ() + (i * 2));
						fireball.updatePosition(fireball.getX(), getBodyY(0.5), fireball.getZ());
						fireball.setOwner(this);
						world.playSound(null, getBlockPos(), BWSoundEvents.ENTITY_GENERIC_SHOOT, getSoundCategory(), getSoundVolume(), getSoundPitch());
						world.spawnEntity(fireball);
					}
					swingHand(Hand.MAIN_HAND);
				}
				if (timer % 600 == 0) {
					summonMinions(this);
				}
			}
			else {
				if (getY() > -64) {
					heal(8);
				}
				timeSinceLastAttack = 0;
			}
		}
	}
	
	@Override
	public String getPledgeID() {
		return BWPledges.BAPHOMET;
	}
	
	@Override
	public EntityType<?> getMinionType() {
		return EntityType.BLAZE;
	}
	
	@Override
	public Collection<StatusEffectInstance> getMinionBuffs() {
		return Sets.newHashSet(new StatusEffectInstance(StatusEffects.RESISTANCE, Integer.MAX_VALUE), new StatusEffectInstance(BWStatusEffects.HARDENING, Integer.MAX_VALUE, 1));
	}
	
	@Override
	public void setTimeSinceLastAttack(int timeSinceLastAttack) {
		this.timeSinceLastAttack = timeSinceLastAttack;
	}
	
	@Override
	protected boolean hasShiny() {
		return false;
	}
	
	@Override
	public int getVariants() {
		return 1;
	}
	
	@Override
	public EntityGroup getGroup() {
		return BewitchmentAPI.DEMON;
	}
	
	@Nullable
	@Override
	protected SoundEvent getAmbientSound() {
		return BWSoundEvents.ENTITY_BAPHOMET_AMBIENT;
	}
	
	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return BWSoundEvents.ENTITY_BAPHOMET_HURT;
	}
	
	@Override
	protected SoundEvent getDeathSound() {
		return BWSoundEvents.ENTITY_BAPHOMET_DEATH;
	}
	
	@Override
	public SoundEvent getYesSound() {
		return getAmbientSound();
	}
	
	@Override
	protected ActionResult interactMob(PlayerEntity player, Hand hand) {
		if (!world.isClient && isAlive()) {
			TradeOfferList offers = getOffers();
			if (DemonEntity.rejectTradesFromCurses(this) || DemonEntity.rejectTradesFromContracts(this)) {
				offers = DemonEntity.EMPTY;
			}
			if (!offers.isEmpty() && getCurrentCustomer() == null && getTarget() == null) {
				boolean client = world.isClient;
				if (!client && BewitchmentAPI.isPledged(world, getPledgeID(), player.getUuid())) {
					setCurrentCustomer(player);
					sendOffers(player, getDisplayName(), 0);
				}
				return ActionResult.success(client);
			}
		}
		return super.interactMob(player, hand);
	}
	
	@Override
	public boolean canBeLeashedBy(PlayerEntity player) {
		return false;
	}
	
	@Override
	public boolean canHaveStatusEffect(StatusEffectInstance effect) {
		return ((StatusEffectAccessor) effect.getEffectType()).bw_getType() == StatusEffectType.BENEFICIAL;
	}
	
	@Override
	public boolean isAffectedBySplashPotions() {
		return false;
	}
	
	@Override
	public boolean cannotDespawn() {
		return true;
	}
	
	@Override
	public boolean tryAttack(Entity target) {
		boolean flag = super.tryAttack(target);
		if (flag && target instanceof LivingEntity) {
			target.setOnFireFor(8);
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
	public boolean handleFallDamage(float fallDistance, float damageMultiplier) {
		return false;
	}
	
	@Override
	protected void fall(double heightDifference, boolean onGround, BlockState landedState, BlockPos landedPosition) {
	}
	
	@Override
	public void setCustomName(@Nullable Text name) {
		super.setCustomName(name);
		bossBar.setName(getDisplayName());
	}
	
	@Override
	public void onStartedTrackingBy(ServerPlayerEntity player) {
		super.onStartedTrackingBy(player);
		bossBar.addPlayer(player);
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
			resetTradeList();
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
	public void onStoppedTrackingBy(ServerPlayerEntity player) {
		super.onStoppedTrackingBy(player);
		bossBar.removePlayer(player);
	}
	
	@Override
	public void readCustomDataFromTag(CompoundTag tag) {
		super.readCustomDataFromTag(tag);
		if (hasCustomName()) {
			bossBar.setName(getDisplayName());
		}
		timeSinceLastAttack = tag.getInt("TimeSinceLastAttack");
		if (tag.contains("Offers")) {
			tradeOffers = new TradeOfferList(tag.getCompound("Offers"));
		}
		refreshTimer = tag.getInt("RefreshTimer");
	}
	
	@Override
	public void writeCustomDataToTag(CompoundTag tag) {
		super.writeCustomDataToTag(tag);
		tag.putInt("TimeSinceLastAttack", timeSinceLastAttack);
		if (!getOffers().isEmpty()) {
			tag.put("Offers", tradeOffers.toTag());
		}
		tag.putInt("RefreshTimer", refreshTimer);
	}
	
	@Override
	protected void initGoals() {
		goalSelector.add(0, new SwimGoal(this));
		goalSelector.add(1, new MeleeAttackGoal(this, 1, true));
		goalSelector.add(2, new WanderAroundFarGoal(this, 1));
		goalSelector.add(3, new LookAtEntityGoal(this, PlayerEntity.class, 8));
		goalSelector.add(3, new LookAroundGoal(this));
		targetSelector.add(0, new RevengeGoal(this));
		targetSelector.add(1, new FollowTargetGoal<>(this, LivingEntity.class, 10, true, false, entity -> entity.getGroup() != BewitchmentAPI.DEMON && !BewitchmentAPI.isVampire(entity, true) && BWUtil.getArmorPieces(entity, stack -> stack.getItem() instanceof ArmorItem && ((ArmorItem) stack.getItem()).getMaterial() == BWMaterials.BESMIRCHED_ARMOR) < 3 && !(entity instanceof PlayerEntity && BewitchmentAPI.isPledged(world, getPledgeID(), entity.getUuid()))));
	}
	
	private void resetTradeList() {
		tradeOffers = new TradeOfferList();
		while (tradeOffers.size() < 5) {
			Contract contract = null;
			while (contract == null || !contract.doesBaphometOffer) {
				contract = BWRegistries.CONTRACTS.get(random.nextInt(BWRegistries.CONTRACTS.getIds().size()));
			}
			ItemStack stack = new ItemStack(BWObjects.DEMONIC_CONTRACT);
			stack.getOrCreateTag().putString("Contract", BWRegistries.CONTRACTS.getId(contract).toString());
			stack.getOrCreateTag().putInt("Duration", 24000);
			tradeOffers.add(new TradeOffer(new ItemStack(Items.PAPER), new ItemStack(BWObjects.BOTTLE_OF_BLOOD), stack, 1, 0, 1));
		}
	}
}
