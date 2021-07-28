package moriyashiine.bewitchment.common.entity.living;

import com.google.common.collect.Sets;
import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.api.interfaces.entity.Pledgeable;
import moriyashiine.bewitchment.api.interfaces.entity.TransformationAccessor;
import moriyashiine.bewitchment.client.network.packet.SpawnSmokeParticlesPacket;
import moriyashiine.bewitchment.common.entity.living.util.BWHostileEntity;
import moriyashiine.bewitchment.common.misc.BWUtil;
import moriyashiine.bewitchment.common.network.packet.TransformationAbilityPacket;
import moriyashiine.bewitchment.common.registry.*;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
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
import net.minecraft.entity.projectile.WitherSkullEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class LilithEntity extends BWHostileEntity implements Pledgeable {
	private final ServerBossBar bossBar;
	
	private final Set<UUID> pledgedPlayerUUIDS = new HashSet<>();
	private int timeSinceLastAttack = 0;
	
	public LilithEntity(EntityType<? extends HostileEntity> entityType, World world) {
		super(entityType, world);
		bossBar = new ServerBossBar(getDisplayName(), BossBar.Color.RED, BossBar.Style.PROGRESS);
		setPathfindingPenalty(PathNodeType.DANGER_FIRE, 0);
		setPathfindingPenalty(PathNodeType.DAMAGE_FIRE, 0);
		experiencePoints = 75;
	}
	
	public static DefaultAttributeContainer.Builder createAttributes() {
		return MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 500).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 16).add(EntityAttributes.GENERIC_ARMOR, 10).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3).add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 1).add(EntityAttributes.GENERIC_FOLLOW_RANGE, 32);
	}
	
	@Override
	public void tick() {
		super.tick();
		if (!world.isClient) {
			bossBar.setPercent(getHealth() / getMaxHealth());
			LivingEntity target = getTarget();
			int timer = age + getId();
			if (timer % 10 == 0) {
				heal(1);
			}
			if (target != null) {
				timeSinceLastAttack++;
				if (timeSinceLastAttack >= 600) {
					BWUtil.teleport(this, target.getX(), target.getY(), target.getZ(), true);
					timeSinceLastAttack = 0;
				}
				lookAtEntity(target, 360, 360);
				if (timer % 40 == 0) {
					for (int i = -1; i <= 1; i++) {
						WitherSkullEntity witherSkull = new WitherSkullEntity(world, this, target.getX() - getX() + (i * 2), target.getBodyY(0.5) - getBodyY(0.5), target.getZ() - getZ() + (i * 2));
						witherSkull.updatePosition(witherSkull.getX(), getBodyY(0.5), witherSkull.getZ());
						witherSkull.setOwner(this);
						world.playSound(null, getBlockPos(), SoundEvents.ENTITY_WITHER_SHOOT, getSoundCategory(), getSoundVolume(), getSoundPitch());
						world.spawnEntity(witherSkull);
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
		return BWPledges.LILITH;
	}
	
	@Override
	public Collection<UUID> getPledgedPlayerUUIDs() {
		return pledgedPlayerUUIDS;
	}
	
	@Override
	public EntityType<?> getMinionType() {
		return BWEntityTypes.VAMPIRE;
	}
	
	@Override
	public Collection<StatusEffectInstance> getMinionBuffs() {
		return Sets.newHashSet(new StatusEffectInstance(StatusEffects.STRENGTH, Integer.MAX_VALUE, 1), new StatusEffectInstance(StatusEffects.RESISTANCE, Integer.MAX_VALUE, 1), new StatusEffectInstance(BWStatusEffects.HARDENING, Integer.MAX_VALUE, 1));
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
		return BWSoundEvents.ENTITY_LILITH_AMBIENT;
	}
	
	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return BWSoundEvents.ENTITY_LILITH_HURT;
	}
	
	@Override
	protected SoundEvent getDeathSound() {
		return BWSoundEvents.ENTITY_LILITH_DEATH;
	}
	
	@Override
	protected ActionResult interactMob(PlayerEntity player, Hand hand) {
		if (isAlive() && getTarget() == null && BewitchmentAPI.isVampire(player, true)) {
			ItemStack stack = player.getStackInHand(hand);
			if (stack.getItem() == BWObjects.GARLIC) {
				boolean client = world.isClient;
				if (!client) {
					if (!player.isCreative()) {
						stack.decrement(1);
					}
					PlayerLookup.tracking(player).forEach(foundPlayer -> SpawnSmokeParticlesPacket.send(foundPlayer, player));
					SpawnSmokeParticlesPacket.send(player, player);
					world.playSound(null, getBlockPos(), BWSoundEvents.ENTITY_GENERIC_PLING, player.getSoundCategory(), 1, 1);
					if (((TransformationAccessor) player).getAlternateForm()) {
						TransformationAbilityPacket.useAbility(player, true);
					}
					((TransformationAccessor) player).getTransformation().onRemoved(player);
					((TransformationAccessor) player).setTransformation(BWTransformations.HUMAN);
					((TransformationAccessor) player).getTransformation().onAdded(player);
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
			((LivingEntity) target).addStatusEffect(new StatusEffectInstance(BWStatusEffects.MORTAL_COIL, 1200));
			target.setOnFireFor(16);
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
	public void setTarget(@Nullable LivingEntity target) {
		if (world.isDay()) {
			target = null;
		}
		super.setTarget(target);
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
	}
	
	@Override
	public void writeCustomDataToNbt(NbtCompound nbt) {
		super.writeCustomDataToNbt(nbt);
		toNbtPledgeable(nbt);
	}
	
	@Override
	protected void initGoals() {
		goalSelector.add(0, new SwimGoal(this));
		goalSelector.add(1, new MeleeAttackGoal(this, 1, true));
		goalSelector.add(2, new WanderAroundFarGoal(this, 1));
		goalSelector.add(3, new LookAtEntityGoal(this, PlayerEntity.class, 8));
		goalSelector.add(3, new LookAroundGoal(this));
		targetSelector.add(0, new RevengeGoal(this));
		targetSelector.add(1, BWUtil.createGenericPledgeableTargetGoal(this));
	}
}
