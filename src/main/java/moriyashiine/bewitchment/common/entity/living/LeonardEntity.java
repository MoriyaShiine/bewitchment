package moriyashiine.bewitchment.common.entity.living;

import com.google.common.collect.Sets;
import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.api.interfaces.entity.Pledgeable;
import moriyashiine.bewitchment.common.entity.living.util.BWHostileEntity;
import moriyashiine.bewitchment.common.misc.BWUtil;
import moriyashiine.bewitchment.common.registry.BWPledges;
import moriyashiine.bewitchment.common.registry.BWSoundEvents;
import moriyashiine.bewitchment.common.registry.BWStatusEffects;
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
import net.minecraft.entity.projectile.thrown.PotionEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class LeonardEntity extends BWHostileEntity implements Pledgeable {
	private final ServerBossBar bossBar;
	
	private final Set<UUID> pledgedPlayerUUIDS = new HashSet<>();
	private int timeSinceLastAttack = 0;
	
	public LeonardEntity(EntityType<? extends HostileEntity> entityType, World world) {
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
		if (!world.isClient) {
			bossBar.setPercent(getHealth() / getMaxHealth());
			LivingEntity target = getTarget();
			int timer = age + getId();
			if (timer % 300 == 0 && getHealth() < getMaxHealth() && (target == null || distanceTo(target) < 4)) {
				spawnPotion(getBlockPos(), Potions.AWKWARD);
				addStatusEffect(new StatusEffectInstance(StatusEffects.INSTANT_HEALTH, 1, 1));
			}
			if (target != null) {
				timeSinceLastAttack++;
				if (timeSinceLastAttack >= 600) {
					BWUtil.teleport(this, target.getX(), target.getY(), target.getZ(), true);
					timeSinceLastAttack = 0;
				}
				lookAtEntity(target, 360, 360);
				if (timer % 40 == 0) {
					spawnPotion(target.getBlockPos(), target.isUndead() ? Potions.STRONG_HEALING : Potions.STRONG_HARMING);
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
		return BWPledges.LEONARD;
	}
	
	@Override
	public Collection<UUID> getPledgedPlayerUUIDs() {
		return pledgedPlayerUUIDS;
	}
	
	@Override
	public EntityType<?> getMinionType() {
		return EntityType.WITCH;
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
		return BWSoundEvents.ENTITY_LEONARD_AMBIENT;
	}
	
	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return BWSoundEvents.ENTITY_LEONARD_HURT;
	}
	
	@Override
	protected SoundEvent getDeathSound() {
		return BWSoundEvents.ENTITY_LEONARD_DEATH;
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
	
	private void spawnPotion(BlockPos target, Potion potionType) {
		PotionEntity potion = new PotionEntity(world, this);
		potion.setItem(PotionUtil.setPotion(new ItemStack(Items.SPLASH_POTION), potionType));
		potion.updatePosition(potion.getX(), getBodyY(0.5), potion.getZ());
		double targetX = target.getX() - getX();
		double targetY = target.getY() - 1 - getY();
		double targetZ = target.getZ() - getZ();
		potion.setVelocity(targetX, targetY + (MathHelper.sqrt((float) (targetX * targetX + targetZ * targetZ)) * 0.4), targetZ, 1, 0);
		world.playSound(null, getBlockPos(), SoundEvents.ENTITY_SPLASH_POTION_THROW, getSoundCategory(), getSoundVolume(), getSoundPitch());
		world.spawnEntity(potion);
		swingHand(Hand.MAIN_HAND);
	}
}
