package moriyashiine.bewitchment.common.component.entity;

import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.client.network.packet.SpawnSmokeParticlesPacket;
import moriyashiine.bewitchment.common.entity.living.WerewolfEntity;
import moriyashiine.bewitchment.common.registry.BWComponents;
import moriyashiine.bewitchment.common.registry.BWEntityTypes;
import moriyashiine.bewitchment.common.registry.BWSoundEvents;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.NotNull;

public class WerewolfVillagerComponent implements ServerTickingComponent {
	private final VillagerEntity obj;
	private NbtCompound storedWerewolf = null;
	private int despawnTimer = 2400;

	public WerewolfVillagerComponent(VillagerEntity obj) {
		this.obj = obj;
	}

	@Override
	public void readFromNbt(NbtCompound tag) {
		if (tag.contains("StoredWerewolf")) {
			storedWerewolf = tag.getCompound("StoredWerewolf");
		}
		if (tag.contains("DespawnTimer")) {
			despawnTimer = tag.getInt("DespawnTimer");
		}
	}

	@Override
	public void writeToNbt(@NotNull NbtCompound tag) {
		if (storedWerewolf != null) {
			tag.put("StoredWerewolf", storedWerewolf);
		}
		tag.putInt("DespawnTimer", despawnTimer);
	}

	@Override
	public void serverTick() {
		if (getStoredWerewolf() != null) {
			if (getDespawnTimer() > 0) {
				setDespawnTimer(getDespawnTimer() - 1);
				if (getDespawnTimer() == 0) {
					obj.remove(Entity.RemovalReason.DISCARDED);
				}
			}
			if (obj.age % 20 == 0 && obj.world.isNight() && BewitchmentAPI.getMoonPhase(obj.world) == 0 && obj.world.isSkyVisible(obj.getBlockPos())) {
				WerewolfEntity entity = BWEntityTypes.WEREWOLF.create(obj.world);
				if (entity != null) {
					PlayerLookup.tracking(obj).forEach(trackingPlayer -> SpawnSmokeParticlesPacket.send(trackingPlayer, obj));
					obj.world.playSound(null, obj.getX(), obj.getY(), obj.getZ(), BWSoundEvents.ENTITY_GENERIC_TRANSFORM, obj.getSoundCategory(), 1, obj.getSoundPitch());
					entity.readNbt(getStoredWerewolf());
					entity.updatePositionAndAngles(obj.getX(), obj.getY(), obj.getZ(), obj.getRandom().nextFloat() * 360, 0);
					entity.setHealth(entity.getMaxHealth() * (obj.getHealth() / obj.getMaxHealth()));
					entity.setFireTicks(obj.getFireTicks());
					entity.clearStatusEffects();
					obj.getStatusEffects().forEach(entity::addStatusEffect);
					BWComponents.CURSES_COMPONENT.maybeGet(entity).ifPresent(entityCursesComponent -> BWComponents.CURSES_COMPONENT.maybeGet(obj).ifPresent(thisCursesComponent -> {
						entityCursesComponent.getCurses().clear();
						thisCursesComponent.getCurses().forEach(entityCursesComponent::addCurse);
					}));
					if (getDespawnTimer() >= 0) {
						setDespawnTimer(2400);
					}
					entity.storedVillager = obj.writeNbt(new NbtCompound());
					obj.world.spawnEntity(entity);
					obj.remove(Entity.RemovalReason.DISCARDED);
				}
			}
		}
	}

	public NbtCompound getStoredWerewolf() {
		return storedWerewolf;
	}

	public void setStoredWerewolf(NbtCompound storedWerewolf) {
		this.storedWerewolf = storedWerewolf;
	}

	public int getDespawnTimer() {
		return despawnTimer;
	}

	public void setDespawnTimer(int despawnTimer) {
		this.despawnTimer = despawnTimer;
	}
}
