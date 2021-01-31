package moriyashiine.bewitchment.common.world;

import moriyashiine.bewitchment.common.Bewitchment;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Pair;
import net.minecraft.world.PersistentState;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BWUniversalWorldState extends PersistentState {
	public final List<Pair<String, List<UUID>>> pledges = new ArrayList<>();
	public final List<Pair<UUID, UUID>> specificPledges = new ArrayList<>();
	public final List<Pair<UUID, CompoundTag>> familiars = new ArrayList<>();
	
	public BWUniversalWorldState(String key) {
		super(key);
	}
	
	@Override
	public CompoundTag toTag(CompoundTag tag) {
		ListTag pledges = new ListTag();
		for (Pair<String, List<UUID>> pair : this.pledges) {
			CompoundTag pledgeTag = new CompoundTag();
			pledgeTag.putString("Pledge", pair.getLeft());
			ListTag players = new ListTag();
			for (int i = 0; i < pair.getRight().size(); i++) {
				CompoundTag playerTag = new CompoundTag();
				playerTag.putUuid("Player" + i, pair.getRight().get(i));
				players.add(playerTag);
			}
			pledgeTag.put("Players", players);
			pledges.add(pledgeTag);
		}
		tag.put("Pledges", pledges);
		ListTag specificPledges = new ListTag();
		for (Pair<UUID, UUID> pair : this.specificPledges) {
			CompoundTag pledgeTag = new CompoundTag();
			pledgeTag.putUuid("Player", pair.getLeft());
			pledgeTag.putUuid("Pledge", pair.getRight());
			specificPledges.add(pledgeTag);
		}
		tag.put("SpecificPledges", specificPledges);
		ListTag familiars = new ListTag();
		for (Pair<UUID, CompoundTag> pair : this.familiars) {
			CompoundTag familiarTag = new CompoundTag();
			familiarTag.putUuid("Player", pair.getLeft());
			familiarTag.put("Familiar", pair.getRight());
			familiars.add(familiarTag);
		}
		tag.put("Familiars", familiars);
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
				players.add(player.getUuid("Player" + j));
			}
			this.pledges.add(new Pair<>(pledgeTag.getString("Pledge"), players));
		}
		ListTag specificPledges = tag.getList("SpecificPledges", NbtType.COMPOUND);
		for (int i = 0; i < specificPledges.size(); i++) {
			CompoundTag pledgeTag = specificPledges.getCompound(i);
			this.specificPledges.add(new Pair<>(pledgeTag.getUuid("Player"), pledgeTag.getUuid("Pledge")));
		}
		ListTag familiars = tag.getList("Familiars", NbtType.COMPOUND);
		for (int i = 0; i < familiars.size(); i++) {
			CompoundTag familiarTag = familiars.getCompound(i);
			this.familiars.add(new Pair<>(familiarTag.getUuid("Player"), familiarTag.getCompound("Familiar")));
		}
	}
	
	public static BWUniversalWorldState get(World world) {
		return ((ServerWorld) world).getPersistentStateManager().getOrCreate(() -> new BWUniversalWorldState(Bewitchment.MODID + "_universal"), Bewitchment.MODID + "_universal");
	}
}
