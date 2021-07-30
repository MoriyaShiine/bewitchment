package moriyashiine.bewitchment.api.component;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import moriyashiine.bewitchment.common.registry.BWComponents;
import moriyashiine.bewitchment.common.registry.BWPledges;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;

import java.util.Optional;

public class PledgeComponent implements AutoSyncedComponent, ServerTickingComponent {
	private final PlayerEntity obj;
	private String pledge = BWPledges.NONE, pledgeNextTick = "";
	
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
		setPledgeNextTick(tag.getString("PledgeNextTick"));
	}
	
	@Override
	public void writeToNbt(NbtCompound tag) {
		tag.putString("Pledge", getPledge());
		tag.putString("PledgeNextTick", getPledgeNextTick());
	}
	
	@Override
	public void serverTick() {
		if (!getPledgeNextTick().isEmpty()) {
			setPledge(getPledgeNextTick());
			setPledgeNextTick("");
		}
	}
	
	public String getPledge() {
		return pledge;
	}
	
	public void setPledge(String pledge) {
		this.pledge = pledge;
		BWComponents.PLEDGE_COMPONENT.sync(obj);
		TransformationComponent.get(obj).updateAttributes();
	}
	
	public String getPledgeNextTick() {
		return pledgeNextTick;
	}
	
	public void setPledgeNextTick(String pledgeNextTick) {
		this.pledgeNextTick = pledgeNextTick;
	}
	
	public static PledgeComponent get(PlayerEntity obj) {
		return BWComponents.PLEDGE_COMPONENT.get(obj);
	}
	
	public static Optional<PledgeComponent> maybeGet(PlayerEntity obj) {
		return BWComponents.PLEDGE_COMPONENT.maybeGet(obj);
	}
}
