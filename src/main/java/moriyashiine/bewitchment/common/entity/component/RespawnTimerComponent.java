package moriyashiine.bewitchment.common.entity.component;

import dev.onyxstudios.cca.api.v3.component.ComponentV3;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import moriyashiine.bewitchment.common.registry.BWComponents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;

import java.util.Optional;

public class RespawnTimerComponent implements ComponentV3, ServerTickingComponent {
	private int respawnTimer = 400;
	
	@Override
	public void readFromNbt(NbtCompound tag) {
		setRespawnTimer(tag.getInt("RespawnTimer"));
	}
	
	@Override
	public void writeToNbt(NbtCompound tag) {
		tag.putInt("RespawnTimer", getRespawnTimer());
	}
	
	@Override
	public void serverTick() {
		if (getRespawnTimer() > 0) {
			setRespawnTimer(getRespawnTimer() - 1);
		}
	}
	
	public int getRespawnTimer() {
		return respawnTimer;
	}
	
	public void setRespawnTimer(int respawnTimer) {
		this.respawnTimer = respawnTimer;
	}
	
	public static RespawnTimerComponent get(PlayerEntity obj) {
		return BWComponents.RESPAWN_TIMER_COMPONENT.get(obj);
	}
	
	public static Optional<RespawnTimerComponent> maybeGet(PlayerEntity obj) {
		return BWComponents.RESPAWN_TIMER_COMPONENT.maybeGet(obj);
	}
}
