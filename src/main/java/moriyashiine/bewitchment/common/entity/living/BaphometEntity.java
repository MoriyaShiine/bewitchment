package moriyashiine.bewitchment.common.entity.living;

import com.google.common.collect.Sets;
import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.api.interfaces.entity.ContractAccessor;
import moriyashiine.bewitchment.api.interfaces.entity.Pledgeable;
import moriyashiine.bewitchment.api.registry.Contract;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.entity.living.util.BWHostileEntity;
import moriyashiine.bewitchment.common.registry.*;
import moriyashiine.bewitchment.mixin.StatusEffectAccessor;
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
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.UUID;

@SuppressWarnings("ConstantConditions")
public class BaphometEntity extends BWHostileEntity implements Pledgeable {
	private final ServerBossBar bossBar;
	
	public int flameIndex = random.nextInt(8);
	
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
			LivingEntity target = getTarget();
			int timer = age + getEntityId();
			if (timer % 20 == 0) {
				heal(1);
			}
			if (target != null) {
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
				heal(8);
			}
		}
	}
	
	@Override
	public UUID getPledgeUUID() {
		return BWPledges.BAPHOMET_UUID;
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
		if (isAlive() && getTarget() == null) {
			boolean client = world.isClient;
			if (!client) {
				if (BewitchmentAPI.isPledged(world, getPledgeUUID(), player.getUuid())) {
					if (player.experienceLevel >= 20 || player.isCreative()) {
						Contract contract = null;
						while (contract == null || !contract.canBeGiven) {
							contract = BWRegistries.CONTRACTS.get(random.nextInt(BWRegistries.CONTRACTS.getEntries().size()));
						}
						Contract.Instance instance = new Contract.Instance(contract, 168000);
						((ContractAccessor) player).addContract(instance);
						player.sendMessage(new TranslatableText(Bewitchment.MODID + ".baphomet_contract", new TranslatableText("contract." + BWRegistries.CONTRACTS.getId(contract).toString().replace(":", "."))), true);
						if (!player.isCreative()) {
							player.addExperience(-551);
							world.playSound(null, player.getBlockPos(), SoundEvents.ENTITY_PLAYER_LEVELUP, player.getSoundCategory(), 1, 0.5f);
						}
					}
				}
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
}
