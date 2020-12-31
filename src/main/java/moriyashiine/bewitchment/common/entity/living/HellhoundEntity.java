package moriyashiine.bewitchment.common.entity.living;

import moriyashiine.bewitchment.client.network.packet.SpawnSmokeParticlesPacket;
import moriyashiine.bewitchment.common.entity.living.util.BWHostileEntity;
import net.fabricmc.fabric.api.server.PlayerStream;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class HellhoundEntity extends BWHostileEntity {
	public HellhoundEntity(EntityType<? extends HostileEntity> entityType, World world) {
		super(entityType, world);
	}
	
	public static DefaultAttributeContainer.Builder createAttributes() {
		return MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 30).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 3).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.425);
	}
	
	@Override
	public void tick() {
		super.tick();
		if (!world.isClient && isWet() && damage(DamageSource.MAGIC, 1)) {
			PlayerStream.watching(this).forEach(playerEntity -> SpawnSmokeParticlesPacket.send(playerEntity, this));
			world.playSound(null, getBlockPos(), SoundEvents.ENTITY_GENERIC_EXTINGUISH_FIRE, SoundCategory.HOSTILE, 1, 1);
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
	
	@Nullable
	@Override
	protected SoundEvent getAmbientSound() {
		return SoundEvents.ENTITY_WOLF_GROWL;
	}
	
	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return SoundEvents.ENTITY_WOLF_HURT;
	}
	
	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.ENTITY_WOLF_DEATH;
	}
	
	@Override
	public void playAmbientSound() {
		super.playAmbientSound();
		playSound(SoundEvents.ENTITY_BLAZE_AMBIENT, getSoundVolume(), getSoundPitch());
	}
	
	@Override
	protected void playHurtSound(DamageSource source) {
		super.playHurtSound(source);
		playSound(SoundEvents.ENTITY_BLAZE_HURT, getSoundVolume(), getSoundPitch());
	}
	
	@Override
	protected float getSoundPitch() {
		return 2 / 3f;
	}
	
	@Override
	public boolean tryAttack(Entity target) {
		boolean flag = super.tryAttack(target);
		if (flag && target instanceof LivingEntity) {
			target.setOnFireFor(3);
		}
		return flag;
	}
	
	@Override
	public void onDeath(DamageSource source) {
		super.onDeath(source);
		playSound(SoundEvents.ENTITY_BLAZE_DEATH, getSoundVolume(), getSoundPitch());
	}
	
	@Override
	protected void initGoals() {
		goalSelector.add(0, new SwimGoal(this));
		goalSelector.add(1, new PounceAtTargetGoal(this, 0.4f));
		goalSelector.add(2, new MeleeAttackGoal(this, 1, true));
		goalSelector.add(3, new WanderAroundFarGoal(this, 1));
		goalSelector.add(4, new LookAtEntityGoal(this, PlayerEntity.class, 8));
		goalSelector.add(4, new LookAroundGoal(this));
		targetSelector.add(0, new RevengeGoal(this).setGroupRevenge());
		targetSelector.add(1, new FollowTargetGoal<>(this, PlayerEntity.class, true));
	}
}
