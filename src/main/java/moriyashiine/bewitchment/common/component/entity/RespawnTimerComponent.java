package moriyashiine.bewitchment.common.component.entity;

import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import net.minecraft.nbt.NbtCompound;

public class RespawnTimerComponent implements ServerTickingComponent {
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
}
