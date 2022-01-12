package moriyashiine.bewitchment.common.world;

import moriyashiine.bewitchment.common.Bewitchment;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.PersistentState;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class BWWorldState extends PersistentState {
	public final Map<Long, DefaultedList<ItemStack>> poppetShelves = new LinkedHashMap<>();

	public final List<Long> potentialCandelabras = new ArrayList<>();
	public final List<Long> potentialSigils = new ArrayList<>();
	public final List<Long> witchCauldrons = new ArrayList<>();
	public final List<Long> glowingBrambles = new ArrayList<>();

	public static BWWorldState readNbt(NbtCompound nbt) {
		BWWorldState worldState = new BWWorldState();
		NbtList poppetShelvesList = nbt.getList("PoppetShelves", NbtType.COMPOUND);
		for (int i = 0; i < poppetShelvesList.size(); i++) {
			NbtCompound poppetShelfCompound = poppetShelvesList.getCompound(i);
			DefaultedList<ItemStack> inventory = null;
			if (poppetShelfCompound.contains("Inventory")) {
				inventory = DefaultedList.ofSize(9, ItemStack.EMPTY);
				Inventories.readNbt(poppetShelfCompound.getCompound("Inventory"), inventory);
			}
			worldState.poppetShelves.put(poppetShelfCompound.getLong("Pos"), inventory);
		}
		NbtList potentialCandelabrasList = nbt.getList("PotentialCandelabras", NbtType.COMPOUND);
		for (int i = 0; i < potentialCandelabrasList.size(); i++) {
			NbtCompound posCompound = potentialCandelabrasList.getCompound(i);
			worldState.potentialCandelabras.add(posCompound.getLong("Pos"));
		}
		NbtList potentialSigilsList = nbt.getList("PotentialSigils", NbtType.COMPOUND);
		for (int i = 0; i < potentialSigilsList.size(); i++) {
			NbtCompound posCompound = potentialSigilsList.getCompound(i);
			worldState.potentialSigils.add(posCompound.getLong("Pos"));
		}
		NbtList witchCauldronsList = nbt.getList("WitchCauldrons", NbtType.COMPOUND);
		for (int i = 0; i < witchCauldronsList.size(); i++) {
			NbtCompound posCompound = witchCauldronsList.getCompound(i);
			worldState.witchCauldrons.add(posCompound.getLong("Pos"));
		}
		NbtList glowingBramblesList = nbt.getList("GlowingBrambles", NbtType.COMPOUND);
		for (int i = 0; i < glowingBramblesList.size(); i++) {
			NbtCompound posCompound = glowingBramblesList.getCompound(i);
			worldState.glowingBrambles.add(posCompound.getLong("Pos"));
		}
		return worldState;
	}

	@Override
	public NbtCompound writeNbt(NbtCompound nbt) {
		NbtList poppetShelvesList = new NbtList();
		this.poppetShelves.forEach((pos, inventory) -> {
			NbtCompound poppetShelfCompound = new NbtCompound();
			poppetShelfCompound.putLong("Pos", pos);
			if (inventory != null) {
				poppetShelfCompound.put("Inventory", Inventories.writeNbt(new NbtCompound(), inventory));
			}
			poppetShelvesList.add(poppetShelfCompound);
		});
		nbt.put("PoppetShelves", poppetShelvesList);
		NbtList potentialCandelabrasList = new NbtList();
		for (long pos : this.potentialCandelabras) {
			NbtCompound posCompound = new NbtCompound();
			posCompound.putLong("Pos", pos);
			potentialCandelabrasList.add(posCompound);
		}
		nbt.put("PotentialCandelabras", potentialCandelabrasList);
		NbtList potentialSigilsList = new NbtList();
		for (long pos : this.potentialSigils) {
			NbtCompound posCompound = new NbtCompound();
			posCompound.putLong("Pos", pos);
			potentialSigilsList.add(posCompound);
		}
		nbt.put("PotentialSigils", potentialSigilsList);
		NbtList witchCauldronsList = new NbtList();
		for (long pos : this.witchCauldrons) {
			NbtCompound posCompound = new NbtCompound();
			posCompound.putLong("Pos", pos);
			witchCauldronsList.add(posCompound);
		}
		nbt.put("WitchCauldrons", witchCauldronsList);
		NbtList glowingBramblesList = new NbtList();
		for (long pos : this.glowingBrambles) {
			NbtCompound posCompound = new NbtCompound();
			posCompound.putLong("Pos", pos);
			glowingBramblesList.add(posCompound);
		}
		nbt.put("GlowingBrambles", glowingBramblesList);
		return nbt;
	}

	public static BWWorldState get(World world) {
		return ((ServerWorld) world).getPersistentStateManager().getOrCreate(BWWorldState::readNbt, BWWorldState::new, Bewitchment.MODID + "_universal");
	}
}
