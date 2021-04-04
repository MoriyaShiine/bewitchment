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
	public final List<UUID> pledgesToRemove = new ArrayList<>();
	public final List<Pair<UUID, CompoundTag>> familiars = new ArrayList<>();
	
	public BWUniversalWorldState(String key) {
		super(key);
	}
	
	@Override
	public CompoundTag toTag(CompoundTag tag) {
		ListTag pledgesToRemove = new ListTag();
		for (UUID uuid : this.pledgesToRemove) {
			CompoundTag toRemove = new CompoundTag();
			toRemove.putUuid("UUID", uuid);
			pledgesToRemove.add(toRemove);
		}
		tag.put("PledgesToRemove", pledgesToRemove);
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
		ListTag pledgesToRemove = tag.getList("PledgesToRemove", NbtType.COMPOUND);
		for (int i = 0; i < pledgesToRemove.size(); i++) {
			this.pledgesToRemove.add(pledgesToRemove.getCompound(i).getUuid("UUID"));
		}
		ListTag familiars = tag.getList("Familiars", NbtType.COMPOUND);
		for (int i = 0; i < familiars.size(); i++) {
			CompoundTag familiarTag = familiars.getCompound(i);
			this.familiars.add(new Pair<>(familiarTag.getUuid("Player"), familiarTag.getCompound("Familiar")));
		}
	}
	
	public static BWUniversalWorldState get(World world) {
		return ((ServerWorld) world).getServer().getOverworld().getPersistentStateManager().getOrCreate(() -> new BWUniversalWorldState(Bewitchment.MODID + "_universal"), Bewitchment.MODID + "_universal");
	}
}
