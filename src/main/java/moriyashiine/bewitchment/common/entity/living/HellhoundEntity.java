/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.bewitchment.common.entity.living;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.client.network.packet.SpawnSmokeParticlesPacket;
import moriyashiine.bewitchment.common.entity.living.util.BWHostileEntity;
import moriyashiine.bewitchment.common.misc.BWUtil;
import moriyashiine.bewitchment.common.registry.BWMaterials;
import moriyashiine.bewitchment.common.registry.BWSoundEvents;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class HellhoundEntity extends BWHostileEntity {
	public HellhoundEntity(EntityType<? extends HostileEntity> entityType, World world) {
		super(entityType, world);
	}

	public static DefaultAttributeContainer.Builder createAttributes() {
		return MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 30).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 3).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.425);
	}

	public static boolean canSpawn(EntityType<HellhoundEntity> type, WorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
		return !world.getBlockState(pos.down()).isOf(Blocks.NETHER_WART_BLOCK);
	}

	@Override
	public void tick() {
		super.tick();
		if (!world.isClient && isWet() && damage(DamageSource.MAGIC, 1)) {
			PlayerLookup.tracking(this).forEach(trackingPlayer -> SpawnSmokeParticlesPacket.send(trackingPlayer, this));
			world.playSound(null, getBlockPos(), SoundEvents.ENTITY_GENERIC_EXTINGUISH_FIRE, getSoundCategory(), getSoundVolume(), getSoundPitch());
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
		return BWSoundEvents.ENTITY_HELLHOUND_AMBIENT;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return BWSoundEvents.ENTITY_HELLHOUND_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return BWSoundEvents.ENTITY_HELLHOUND_DEATH;
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
	protected void initGoals() {
		goalSelector.add(0, new SwimGoal(this));
		goalSelector.add(1, new PounceAtTargetGoal(this, 0.4f));
		goalSelector.add(2, new MeleeAttackGoal(this, 1, true));
		goalSelector.add(3, new WanderAroundFarGoal(this, 1));
		goalSelector.add(4, new LookAtEntityGoal(this, PlayerEntity.class, 8));
		goalSelector.add(4, new LookAroundGoal(this));
		targetSelector.add(0, new RevengeGoal(this).setGroupRevenge());
		targetSelector.add(1, new ActiveTargetGoal<>(this, PlayerEntity.class, 10, true, false, entity -> BWUtil.getArmorPieces(entity, stack -> stack.getItem() instanceof ArmorItem && ((ArmorItem) stack.getItem()).getMaterial() == BWMaterials.BESMIRCHED_ARMOR) < 3));
	}
}
