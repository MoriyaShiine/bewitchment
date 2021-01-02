package moriyashiine.bewitchment.common.world;

import moriyashiine.bewitchment.common.Bewitchment;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.PersistentState;
import net.minecraft.world.World;

import java.util.*;

public class BWUniversalWorldState extends PersistentState {
	public final Map<UUID, List<UUID>> pledges = new HashMap<>();
	public final Map<UUID, UUID> specificPledges = new HashMap<>();
	
	public BWUniversalWorldState(String key) {
		super(key);
	}
	
	@Override
	public CompoundTag toTag(CompoundTag tag) {
		ListTag pledges = new ListTag();
		for (UUID pledgeable : this.pledges.keySet()) {
			CompoundTag pledgeTag = new CompoundTag();
			pledgeTag.putUuid("PledgeUUID", pledgeable);
			ListTag players = new ListTag();
			for (int i = 0; i < this.pledges.get(pledgeable).size(); i++) {
				CompoundTag playerTag = new CompoundTag();
				playerTag.putUuid("PlayerUUID" + i, this.pledges.get(pledgeable).get(i));
				players.add(playerTag);
			}
			pledgeTag.put("Players", players);
			pledges.add(pledgeTag);
		}
		tag.put("Pledges", pledges);
		
		
		ListTag specificPledges = new ListTag();
		for (UUID player : this.specificPledges.keySet()) {
			CompoundTag pledgeTag = new CompoundTag();
			pledgeTag.putUuid("PlayerUUID", player);
			pledgeTag.putUuid("PledgeUUID", this.specificPledges.get(player));
			specificPledges.add(pledgeTag);
		}
		tag.put("SpecificPledges", specificPledges);
		return tag;
	}
	
	@Override
	public void fromTag(CompoundTag tag) {
		ListTag pledges = tag.getList("Pledges", NbtType.COMPOUND);
		for (int i = 0; i < pledges.size(); i++) {
			CompoundTag pledgeTag = pledges.getCompound(i);
			List<UUID> players = new ArrayList<>();
			ListTag playersTag = pledgeTag.getList("Players", NbtType.COMPOUND);
			for (int j = 0; j < playersTag.size(); j++) {
				CompoundTag player = playersTag.getCompound(j);
				players.add(player.getUuid("PlayerUUID" + j));
			}
			this.pledges.put(pledgeTag.getUuid("PledgeUUID"), players);
		}
		ListTag specificPledges = tag.getList("SpecificPledges", NbtType.COMPOUND);
		for (int i = 0; i < specificPledges.size(); i++) {
			CompoundTag pledgeTag = specificPledges.getCompound(i);
			this.specificPledges.put(pledgeTag.getUuid("PlayerUUID"), pledgeTag.getUuid("PledgeUUID"));
		}
	}
	
	public static BWUniversalWorldState get(World world) {
		return ((ServerWorld) world).getPersistentStateManager().getOrCreate(() -> new BWUniversalWorldState(Bewitchment.MODID + "_universal"), Bewitchment.MODID + "_universal");
	}
}
