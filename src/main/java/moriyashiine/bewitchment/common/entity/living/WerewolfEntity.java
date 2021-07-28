package moriyashiine.bewitchment.common.entity.living;

import com.jamieswhiteshirt.reachentityattributes.ReachEntityAttributes;
import dev.emi.stepheightentityattribute.StepHeightEntityAttributeMain;
import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.api.interfaces.entity.CurseAccessor;
import moriyashiine.bewitchment.client.network.packet.SpawnSmokeParticlesPacket;
import moriyashiine.bewitchment.common.entity.interfaces.VillagerWerewolfAccessor;
import moriyashiine.bewitchment.common.entity.living.util.BWHostileEntity;
import moriyashiine.bewitchment.common.misc.BWUtil;
import moriyashiine.bewitchment.common.registry.BWSoundEvents;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundEvent;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("ConstantConditions")
public class WerewolfEntity extends BWHostileEntity {
	public NbtCompound storedVillager;
	
	public WerewolfEntity(EntityType<? extends HostileEntity> entityType, World world) {
		super(entityType, world);
	}
	
	public static DefaultAttributeContainer.Builder createAttributes() {
		return MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 20).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 5).add(EntityAttributes.GENERIC_ARMOR, 20).add(EntityAttributes.GENERIC_ARMOR_TOUGHNESS, 10).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.4).add(ReachEntityAttributes.ATTACK_RANGE, 1).add(StepHeightEntityAttributeMain.STEP_HEIGHT, 1);
	}
	
	@Override
	public void tick() {
		super.tick();
		if (!world.isClient) {
			getArmorItems().forEach(stack -> dropStack(stack.split(1)));
			if (BWUtil.isTool(getMainHandStack())) {
				dropStack(getMainHandStack().split(1));
			}
			if (BWUtil.isTool(getOffHandStack())) {
				dropStack(getOffHandStack().split(1));
			}
			if (storedVillager != null && age % 20 == 0 && (world.isDay() || BewitchmentAPI.getMoonPhase(world) != 0)) {
				VillagerEntity entity = EntityType.VILLAGER.create(world);
				if (entity instanceof VillagerWerewolfAccessor) {
					PlayerLookup.tracking(this).forEach(player -> SpawnSmokeParticlesPacket.send(player, this));
					world.playSound(null, getX(), getY(), getZ(), BWSoundEvents.ENTITY_GENERIC_TRANSFORM, getSoundCategory(), getSoundVolume(), getSoundPitch());
					entity.readNbt(storedVillager);
					entity.updatePositionAndAngles(getX(), getY(), getZ(), random.nextFloat() * 360, 0);
					entity.setHealth(entity.getMaxHealth() * (getHealth() / getMaxHealth()));
					entity.setFireTicks(getFireTicks());
					entity.clearStatusEffects();
					getStatusEffects().forEach(entity::addStatusEffect);
					((CurseAccessor) entity).getCurses().clear();
					((CurseAccessor) this).getCurses().forEach(((CurseAccessor) entity)::addCurse);
					((VillagerWerewolfAccessor) entity).setStoredWerewolf(writeNbt(new NbtCompound()));
					world.spawnEntity(entity);
					remove(RemovalReason.DISCARDED);
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
		return getVariantsStatic();
	}
	
	@Override
	public EntityGroup getGroup() {
		return BewitchmentAPI.DEMON;
	}
	
	@Nullable
	@Override
	protected SoundEvent getAmbientSound() {
		return BWSoundEvents.ENTITY_WEREWOLF_AMBIENT;
	}
	
	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return BWSoundEvents.ENTITY_WEREWOLF_HURT;
	}
	
	@Override
	protected SoundEvent getDeathSound() {
		return BWSoundEvents.ENTITY_WEREWOLF_DEATH;
	}
	
	@Override
	public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable NbtCompound entityTag) {
		EntityData data = super.initialize(world, difficulty, spawnReason, entityData, entityTag);
		if (dataTracker.get(VARIANT) != 0) {
			switch (world.getBiome(getBlockPos()).getCategory()) {
				case FOREST -> dataTracker.set(VARIANT, random.nextBoolean() ? 1 : 2);
				case TAIGA -> dataTracker.set(VARIANT, random.nextBoolean() ? 3 : 4);
				case ICY -> dataTracker.set(VARIANT, random.nextBoolean() ? 5 : 6);
				default -> dataTracker.set(VARIANT, random.nextInt(getVariants() - 1) + 1);
			}
		}
		if (spawnReason == SpawnReason.NATURAL) {
			storedVillager = EntityType.VILLAGER.create((World) world).writeNbt(new NbtCompound());
		}
		return data;
	}
	
	@Override
	public void readCustomDataFromNbt(NbtCompound nbt) {
		super.readCustomDataFromNbt(nbt);
		if (nbt.contains("StoredVillager")) {
			storedVillager = nbt.getCompound("StoredVillager");
		}
	}
	
	@Override
	public void writeCustomDataToNbt(NbtCompound nbt) {
		super.writeCustomDataToNbt(nbt);
		if (storedVillager != null) {
			nbt.put("StoredVillager", storedVillager);
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
		targetSelector.add(1, new FollowTargetGoal<>(this, LivingEntity.class, 10, true, false, entity -> entity instanceof PlayerEntity || entity instanceof SheepEntity || entity instanceof MerchantEntity || entity.getGroup() == EntityGroup.ILLAGER));
	}
	
	public static int getVariantsStatic() {
		return 7;
	}
}
