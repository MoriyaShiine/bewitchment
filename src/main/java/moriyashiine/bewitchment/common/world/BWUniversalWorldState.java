package moriyashiine.bewitchment.common.world;

import moriyashiine.bewitchment.common.Bewitchment;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.Pair;
import net.minecraft.world.PersistentState;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BWUniversalWorldState extends PersistentState {
	public final List<UUID> pledgesToRemove = new ArrayList<>();
	public final List<Pair<UUID, NbtCompound>> familiars = new ArrayList<>();
	
	public static BWUniversalWorldState readNbt(NbtCompound nbt) {
		BWUniversalWorldState worldState = new BWUniversalWorldState();
		NbtList pledgesToRemove = nbt.getList("PledgesToRemove", NbtType.COMPOUND);
		for (int i = 0; i < pledgesToRemove.size(); i++) {
			worldState.pledgesToRemove.add(pledgesToRemove.getCompound(i).getUuid("UUID"));
		}
		NbtList familiars = nbt.getList("Familiars", NbtType.COMPOUND);
		for (int i = 0; i < familiars.size(); i++) {
			NbtCompound familiarTag = familiars.getCompound(i);
			worldState.familiars.add(new Pair<>(familiarTag.getUuid("Player"), familiarTag.getCompound("Familiar")));
		}
		return worldState;
	}
	
	@Override
	public NbtCompound writeNbt(NbtCompound nbt) {
		NbtList pledgesToRemove = new NbtList();
		for (UUID uuid : this.pledgesToRemove) {
			NbtCompound toRemove = new NbtCompound();
			toRemove.putUuid("UUID", uuid);
			pledgesToRemove.add(toRemove);
		}
		nbt.put("PledgesToRemove", pledgesToRemove);
		NbtList familiars = new NbtList();
		for (Pair<UUID, NbtCompound> pair : this.familiars) {
			NbtCompound familiarTag = new NbtCompound();
			familiarTag.putUuid("Player", pair.getLeft());
			familiarTag.put("Familiar", pair.getRight());
			familiars.add(familiarTag);
		}
		nbt.put("Familiars", familiars);
		return nbt;
	}
	
	@SuppressWarnings("ConstantConditions")
	public static BWUniversalWorldState get(World world) {
		return world.getServer().getOverworld().getPersistentStateManager().getOrCreate(BWUniversalWorldState::readNbt, BWUniversalWorldState::new, Bewitchment.MODID);
	}
}
