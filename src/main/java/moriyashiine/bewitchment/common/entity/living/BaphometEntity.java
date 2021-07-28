package moriyashiine.bewitchment.common.entity.living;

import com.google.common.collect.Sets;
import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.api.interfaces.entity.Pledgeable;
import moriyashiine.bewitchment.api.registry.Contract;
import moriyashiine.bewitchment.client.network.packet.SyncContractsPacket;
import moriyashiine.bewitchment.client.network.packet.SyncDemonTradesPacket;
import moriyashiine.bewitchment.client.screen.BaphometScreenHandler;
import moriyashiine.bewitchment.common.entity.interfaces.DemonMerchant;
import moriyashiine.bewitchment.common.entity.living.util.BWHostileEntity;
import moriyashiine.bewitchment.common.misc.BWUtil;
import moriyashiine.bewitchment.common.registry.BWPledges;
import moriyashiine.bewitchment.common.registry.BWRegistries;
import moriyashiine.bewitchment.common.registry.BWSoundEvents;
import moriyashiine.bewitchment.common.registry.BWStatusEffects;
import net.fabricmc.fabric.api.util.NbtType;
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
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

public class BaphometEntity extends BWHostileEntity implements Pledgeable, DemonMerchant {
	private final ServerBossBar bossBar;
	
	private final Set<UUID> pledgedPlayerUUIDS = new HashSet<>();
	private int timeSinceLastAttack = 0;
	public int flameIndex = random.nextInt(8);
	
	private final List<DemonEntity.DemonTradeOffer> offers = new ArrayList<>();
	private PlayerEntity customer = null;
	private int tradeResetTimer = 0;
	
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
			tradeResetTimer++;
			if (tradeResetTimer >= 168000) {
				tradeResetTimer = 0;
				offers.clear();
			}
			LivingEntity target = getTarget();
			int timer = age + getId();
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
						FireballEntity fireball = new FireballEntity(world, this, target.getX() - getX() + (i * 2), target.getBodyY(0.5) - getBodyY(0.5), target.getZ() - getZ() + (i * 2), 1);
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
	public Collection<UUID> getPledgedPlayerUUIDs() {
		return pledgedPlayerUUIDS;
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
	public int getTimeSinceLastAttack() {
		return timeSinceLastAttack;
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
	protected ActionResult interactMob(PlayerEntity player, Hand hand) {
		if (!world.isClient && isAlive() && getTarget() == null && BewitchmentAPI.isPledged(player, getPledgeID())) {
			if (BWUtil.rejectTrades(this)) {
				return ActionResult.FAIL;
			}
			if (getCurrentCustomer() == null) {
				setCurrentCustomer(player);
			}
			if (!getOffers().isEmpty()) {
				SyncContractsPacket.send(player);
				player.openHandledScreen(new SimpleNamedScreenHandlerFactory((id, playerInventory, customer) -> new BaphometScreenHandler(id, this), getDisplayName())).ifPresent(syncId -> SyncDemonTradesPacket.send(player, this, syncId));
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
	public boolean canHaveStatusEffect(StatusEffectInstance effect) {
		return effect.getEffectType().getType() == StatusEffectType.BENEFICIAL;
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
	public boolean handleFallDamage(float fallDistance, float damageMultiplier, DamageSource damageSource) {
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
	public void onDeath(DamageSource source) {
		super.onDeath(source);
		setCurrentCustomer(null);
	}
	
	@Override
	public void setTarget(@Nullable LivingEntity target) {
		super.setTarget(target);
		if (target != null) {
			setCurrentCustomer(null);
		}
	}
	
	@Override
	public void onStartedTrackingBy(ServerPlayerEntity player) {
		super.onStartedTrackingBy(player);
		bossBar.addPlayer(player);
	}
	
	@Override
	public void onStoppedTrackingBy(ServerPlayerEntity player) {
		super.onStoppedTrackingBy(player);
		bossBar.removePlayer(player);
	}
	
	@Override
	public void readCustomDataFromNbt(NbtCompound nbt) {
		super.readCustomDataFromNbt(nbt);
		if (hasCustomName()) {
			bossBar.setName(getDisplayName());
		}
		fromNbtPledgeable(nbt);
		if (nbt.contains("Offers")) {
			offers.clear();
			NbtList offersTag = nbt.getList("Offers", NbtType.COMPOUND);
			for (NbtElement offerTag : offersTag) {
				offers.add(new DemonEntity.DemonTradeOffer((NbtCompound) offerTag));
			}
		}
		tradeResetTimer = nbt.getInt("TradeResetTimer");
	}
	
	@Override
	public void writeCustomDataToNbt(NbtCompound nbt) {
		super.writeCustomDataToNbt(nbt);
		toNbtPledgeable(nbt);
		if (!offers.isEmpty()) {
			NbtList offersTag = new NbtList();
			for (DemonEntity.DemonTradeOffer offer : offers) {
				offersTag.add(offer.toTag());
			}
			nbt.put("Offers", offersTag);
		}
		nbt.putInt("TradeResetTimer", tradeResetTimer);
	}
	
	@Override
	protected void initGoals() {
		goalSelector.add(0, new SwimGoal(this));
		goalSelector.add(1, new DemonEntity.LookAtCustomerGoal<>(this));
		goalSelector.add(2, new MeleeAttackGoal(this, 1, true));
		goalSelector.add(3, new WanderAroundFarGoal(this, 1));
		goalSelector.add(4, new LookAtEntityGoal(this, PlayerEntity.class, 8));
		goalSelector.add(5, new LookAroundGoal(this));
		targetSelector.add(0, new RevengeGoal(this));
		targetSelector.add(1, BWUtil.createGenericPledgeableTargetGoal(this));
	}
	
	@Override
	public List<DemonEntity.DemonTradeOffer> getOffers() {
		if (offers.isEmpty()) {
			List<Contract> availableContracts = BWRegistries.CONTRACTS.stream().collect(Collectors.toList());
			for (int i = 0; i < 5; i++) {
				Contract contract = availableContracts.get(random.nextInt(availableContracts.size()));
				offers.add(new DemonEntity.DemonTradeOffer(contract, 72000, 2 + random.nextInt(2) * 2));
				availableContracts.remove(contract);
			}
		}
		return offers;
	}
	
	@Override
	public LivingEntity getDemonTrader() {
		return this;
	}
	
	@Override
	public void onSell(DemonEntity.DemonTradeOffer offer) {
		if (!world.isClient) {
			world.playSound(null, getBlockPos(), BWSoundEvents.ITEM_CONTRACT_USE, getSoundCategory(), getSoundVolume(), getSoundPitch());
			world.playSound(null, getBlockPos(), getAmbientSound(), getSoundCategory(), getSoundVolume(), getSoundPitch());
		}
	}
	
	@Override
	public void setCurrentCustomer(PlayerEntity customer) {
		this.customer = customer;
	}
	
	@Override
	public @Nullable PlayerEntity getCurrentCustomer() {
		return customer;
	}
}
