package moriyashiine.bewitchment.common.entity.component;

import dev.onyxstudios.cca.api.v3.component.ComponentV3;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import moriyashiine.bewitchment.client.network.packet.SpawnSmokeParticlesPacket;
import moriyashiine.bewitchment.common.registry.BWComponents;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;

import java.util.Optional;
import java.util.UUID;

public class MinionComponent implements ComponentV3, ServerTickingComponent {
	private final MobEntity obj;
	private UUID master = null;
	
	public MinionComponent(MobEntity obj) {
		this.obj = obj;
	}
	
	@Override
	public void readFromNbt(NbtCompound tag) {
		setMaster(tag.getString("MasterUUID").isEmpty() ? null : UUID.fromString(tag.getString("MasterUUID")));
	}
	
	@Override
	public void writeToNbt(NbtCompound tag) {
		tag.putString("MasterUUID", getMaster() == null ? "" : getMaster().toString());
	}
	
	@Override
	public void serverTick() {
		if (getMaster() != null) {
			Entity master = ((ServerWorld) obj.world).getEntity(getMaster());
			if (master instanceof MobEntity mob && !mob.isDead() && mob.getTarget() != null) {
				obj.setTarget(mob.getTarget());
			}
			else {
				PlayerLookup.tracking(obj).forEach(playerEntity -> SpawnSmokeParticlesPacket.send(playerEntity, obj));
				obj.remove(Entity.RemovalReason.DISCARDED);
			}
		}
	}
	
	public UUID getMaster() {
		return master;
	}
	
	public void setMaster(UUID master) {
		this.master = master;
		BWComponents.MINION_COMPONENT.sync(obj);
	}
	
	public static MinionComponent get(MobEntity obj) {
		return BWComponents.MINION_COMPONENT.get(obj);
	}
	
	public static Optional<MinionComponent> maybeGet(MobEntity obj) {
		return BWComponents.MINION_COMPONENT.maybeGet(obj);
	}
}
