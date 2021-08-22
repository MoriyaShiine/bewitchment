package moriyashiine.bewitchment.common.entity.component;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import moriyashiine.bewitchment.api.component.CursesComponent;
import moriyashiine.bewitchment.common.registry.BWComponents;
import moriyashiine.bewitchment.common.registry.BWCurses;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;

import java.util.Optional;
import java.util.UUID;

public class FakeMobComponent implements AutoSyncedComponent, ServerTickingComponent {
	private final MobEntity obj;
	private UUID target = null;
	
	public FakeMobComponent(MobEntity obj) {
		this.obj = obj;
	}
	
	@Override
	public void readFromNbt(NbtCompound tag) {
		setTarget(tag.getString("TargetUUID").isEmpty() ? null : UUID.fromString(tag.getString("TargetUUID")));
	}
	
	@Override
	public void writeToNbt(NbtCompound tag) {
		tag.putString("TargetUUID", getTarget() == null ? "" : getTarget().toString());
	}
	
	@Override
	public void serverTick() {
		if (getTarget() != null) {
			LivingEntity entity = (LivingEntity) ((ServerWorld) obj.world).getEntity(getTarget());
			if (entity == null || (obj.age % 20 == 0 && (obj.getRandom().nextFloat() < 1 / 100f || !CursesComponent.get(entity).hasCurse(BWCurses.INSANITY)))) {
				obj.remove(Entity.RemovalReason.DISCARDED);
			}
			else if (obj.getTarget() == null || !obj.getTarget().getUuid().equals(getTarget())) {
				obj.setTarget(entity);
			}
		}
	}
	
	public UUID getTarget() {
		return target;
	}
	
	public void setTarget(UUID target) {
		this.target = target;
		BWComponents.FAKE_MOB_COMPONENT.sync(obj);
	}
	
	public static FakeMobComponent get(MobEntity obj) {
		return BWComponents.FAKE_MOB_COMPONENT.get(obj);
	}
	
	public static Optional<FakeMobComponent> maybeGet(MobEntity obj) {
		return BWComponents.FAKE_MOB_COMPONENT.maybeGet(obj);
	}
}
