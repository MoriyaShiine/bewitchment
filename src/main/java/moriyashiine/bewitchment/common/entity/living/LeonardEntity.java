package moriyashiine.bewitchment.common.entity.living;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.api.interfaces.MasterAccessor;
import moriyashiine.bewitchment.api.interfaces.Pledgeable;
import moriyashiine.bewitchment.common.entity.living.util.BWHostileEntity;
import moriyashiine.bewitchment.common.registry.BWMaterials;
import moriyashiine.bewitchment.common.registry.BWPledges;
import moriyashiine.bewitchment.common.registry.BWSoundEvents;
import moriyashiine.bewitchment.common.registry.BWStatusEffects;
import moriyashiine.bewitchment.mixin.StatusEffectAccessor;
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
import net.minecraft.entity.mob.WitchEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.PotionEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class LeonardEntity extends BWHostileEntity implements Pledgeable {
	private final ServerBossBar bossBar;
	
	public LeonardEntity(EntityType<? extends HostileEntity> entityType, World world) {
		super(entityType, world);
		bossBar = new ServerBossBar(getDisplayName(), BossBar.Color.RED, BossBar.Style.PROGRESS);
		setPathfindingPenalty(PathNodeType.DANGER_FIRE, 0);
		setPathfindingPenalty(PathNodeType.DAMAGE_FIRE, 0);
		experiencePoints = 50;
	}
	
	public static DefaultAttributeContainer.Builder createAttributes() {
		return MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 375).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 10).add(EntityAttributes.GENERIC_ARMOR, 6).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25).add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0.75);
	}
	
	@Override
	public UUID getPledgeUUID() {
		return BWPledges.LEONARD_UUID;
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
	public void tick() {
		super.tick();
		if (!world.isClient) {
			bossBar.setPercent(getHealth() / getMaxHealth());
			LivingEntity target = getTarget();
			int timer = age + getEntityId();
			if (timer % 300 == 0 && getHealth() < getMaxHealth() && (target == null || distanceTo(target) < 4)) {
				spawnPotion(getBlockPos(), Potions.STRONG_HEALING);
				addStatusEffect(new StatusEffectInstance(StatusEffects.INSTANT_HEALTH, 1, 1));
			}
			if (target != null) {
				lookAtEntity(target, 360, 360);
				if (timer % 80 == 0) {
					spawnPotion(target.getBlockPos(), target.isUndead() ? Potions.STRONG_HEALING : Potions.STRONG_HARMING);
				}
				if (timer % 600 == 0 && world.getEntitiesByType(EntityType.WITCH, new Box(getBlockPos()).expand(32), entity -> getUuid().equals(((MasterAccessor) entity).getMasterUUID())).size() < 3) {
					summonMinions();
				}
			}
		}
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
		if (target != null) {
			if (BewitchmentAPI.isPledged(world, getPledgeUUID(), target.getUuid())) {
				BewitchmentAPI.unpledge(world, getPledgeUUID(), target.getUuid());
			}
			if (target instanceof MasterAccessor && getUuid().equals(((MasterAccessor) target).getMasterUUID())) {
				return;
			}
			if (world.getOtherEntities(this, new Box(getBlockPos()).expand(16), entity -> entity instanceof WitchEntity && !entity.removed && getUuid().equals(((MasterAccessor) entity).getMasterUUID())).size() < 3) {
				summonMinions();
			}
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
	public void readCustomDataFromTag(CompoundTag tag) {
		super.readCustomDataFromTag(tag);
		if (hasCustomName()) {
			bossBar.setName(getDisplayName());
		}
	}
	
	@Override
	protected void initGoals() {
		goalSelector.add(0, new SwimGoal(this));
		goalSelector.add(1, new MeleeAttackGoal(this, 1, true));
		goalSelector.add(2, new WanderAroundFarGoal(this, 1));
		goalSelector.add(3, new LookAtEntityGoal(this, PlayerEntity.class, 8));
		goalSelector.add(3, new LookAroundGoal(this));
		targetSelector.add(0, new RevengeGoal(this));
		targetSelector.add(1, new FollowTargetGoal<>(this, LivingEntity.class, 10, true, false, entity -> entity.getGroup() != BewitchmentAPI.DEMON && BewitchmentAPI.getArmorPieces(entity, stack -> stack.getItem() instanceof ArmorItem && ((ArmorItem) stack.getItem()).getMaterial() == BWMaterials.BESMIRCHED_ARMOR) < 3 && !(entity instanceof PlayerEntity && BewitchmentAPI.isPledged(world, getPledgeUUID(), entity.getUuid()))));
	}
	
	private void summonMinions() {
		if (!world.isClient) {
			for (int i = 0; i < MathHelper.nextInt(random, 2, 3); i++) {
				WitchEntity witch = EntityType.WITCH.create(world);
				if (witch != null) {
					BewitchmentAPI.attemptTeleport(witch, getBlockPos(), 3);
					witch.pitch = random.nextInt(360);
					witch.setTarget(getTarget());
					MasterAccessor.of(witch).ifPresent(masterAccessor -> masterAccessor.setMasterUUID(getUuid()));
					witch.setPersistent();
					witch.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, Integer.MAX_VALUE));
					witch.addStatusEffect(new StatusEffectInstance(BWStatusEffects.MAGIC_RESISTANCE, Integer.MAX_VALUE));
					witch.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, Integer.MAX_VALUE));
					witch.addStatusEffect(new StatusEffectInstance(BWStatusEffects.HARDENING, Integer.MAX_VALUE, 1));
					world.spawnEntity(witch);
				}
			}
		}
	}
	
	private void spawnPotion(BlockPos target, Potion potionType) {
		PotionEntity potion = new PotionEntity(world, this);
		potion.setItem(PotionUtil.setPotion(new ItemStack(Items.SPLASH_POTION), potionType));
		potion.updatePosition(potion.getX(), getBodyY(0.5), potion.getZ());
		double targetX = target.getX() - getX();
		double targetY = target.getY() - 1 - getY();
		double targetZ = target.getZ() - getZ();
		potion.setVelocity(targetX, targetY + (MathHelper.sqrt(targetX * targetX + targetZ * targetZ) * 0.4), targetZ, 1, 0);
		world.playSound(null, getBlockPos(), SoundEvents.ENTITY_SPLASH_POTION_THROW, SoundCategory.HOSTILE, 1, 1);
		world.spawnEntity(potion);
		swingHand(Hand.MAIN_HAND);
	}
}
