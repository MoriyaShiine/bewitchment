package moriyashiine.bewitchment.common.entity.component;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import moriyashiine.bewitchment.common.registry.BWComponents;
import moriyashiine.bewitchment.common.registry.BWPledges;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;

import java.util.Optional;

public class PledgeComponent implements AutoSyncedComponent {
	private final PlayerEntity obj;
	private String pledge = BWPledges.NONE;
	
	public PledgeComponent(PlayerEntity obj) {
		this.obj = obj;
	}
	
	@Override
	public void readFromNbt(NbtCompound tag) {
		String pledge = tag.getString("Pledge");
		if (pledge.isEmpty()) {
			pledge = BWPledges.NONE;
		}
		setPledge(pledge);
	}
	
	@Override
	public void writeToNbt(NbtCompound tag) {
		tag.putString("Pledge", getPledge());
	}
	
	public String getPledge() {
		return pledge;
	}
	
	public void setPledge(String pledge) {
		this.pledge = pledge;
		BWComponents.PLEDGE_COMPONENT.sync(obj);
	}
	
	public static PledgeComponent get(PlayerEntity obj) {
		return BWComponents.PLEDGE_COMPONENT.get(obj);
	}
	
	public static Optional<PledgeComponent> maybeGet(PlayerEntity obj) {
		return BWComponents.PLEDGE_COMPONENT.maybeGet(obj);
	}
}
