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
		NbtList poppetShelves = nbt.getList("PoppetShelves", NbtType.COMPOUND);
		for (int i = 0; i < poppetShelves.size(); i++) {
			NbtCompound shelfTag = poppetShelves.getCompound(i);
			DefaultedList<ItemStack> inventory = null;
			if (shelfTag.contains("Inventory")) {
				inventory = DefaultedList.ofSize(9, ItemStack.EMPTY);
				Inventories.readNbt(shelfTag.getCompound("Inventory"), inventory);
			}
			worldState.poppetShelves.put(shelfTag.getLong("Pos"), inventory);
		}
		NbtList potentialCandelabras = nbt.getList("PotentialCandelabras", NbtType.COMPOUND);
		for (int i = 0; i < potentialCandelabras.size(); i++) {
			NbtCompound posTag = potentialCandelabras.getCompound(i);
			worldState.potentialCandelabras.add(posTag.getLong("Pos"));
		}
		NbtList potentialSigils = nbt.getList("PotentialSigils", NbtType.COMPOUND);
		for (int i = 0; i < potentialSigils.size(); i++) {
			NbtCompound posTag = potentialSigils.getCompound(i);
			worldState.potentialSigils.add(posTag.getLong("Pos"));
		}
		NbtList witchCauldrons = nbt.getList("WitchCauldrons", NbtType.COMPOUND);
		for (int i = 0; i < witchCauldrons.size(); i++) {
			NbtCompound posTag = witchCauldrons.getCompound(i);
			worldState.witchCauldrons.add(posTag.getLong("Pos"));
		}
		NbtList glowingBrambles = nbt.getList("GlowingBrambles", NbtType.COMPOUND);
		for (int i = 0; i < glowingBrambles.size(); i++) {
			NbtCompound posTag = glowingBrambles.getCompound(i);
			worldState.glowingBrambles.add(posTag.getLong("Pos"));
		}
		return worldState;
	}
	
	@Override
	public NbtCompound writeNbt(NbtCompound nbt) {
		NbtList poppetShelves = new NbtList();
		this.poppetShelves.forEach((pos, inventory) -> {
			NbtCompound shelfTag = new NbtCompound();
			shelfTag.putLong("Pos", pos);
			if (inventory != null) {
				shelfTag.put("Inventory", Inventories.writeNbt(new NbtCompound(), inventory));
			}
			poppetShelves.add(shelfTag);
		});
		nbt.put("PoppetShelves", poppetShelves);
		NbtList potentialCandelabras = new NbtList();
		for (long pos : this.potentialCandelabras) {
			NbtCompound posTag = new NbtCompound();
			posTag.putLong("Pos", pos);
			potentialCandelabras.add(posTag);
		}
		nbt.put("PotentialCandelabras", potentialCandelabras);
		NbtList potentialSigils = new NbtList();
		for (long pos : this.potentialSigils) {
			NbtCompound posTag = new NbtCompound();
			posTag.putLong("Pos", pos);
			potentialSigils.add(posTag);
		}
		nbt.put("PotentialSigils", potentialSigils);
		NbtList witchCauldrons = new NbtList();
		for (long pos : this.witchCauldrons) {
			NbtCompound posTag = new NbtCompound();
			posTag.putLong("Pos", pos);
			witchCauldrons.add(posTag);
		}
		nbt.put("WitchCauldrons", witchCauldrons);
		NbtList glowingBrambles = new NbtList();
		for (long pos : this.glowingBrambles) {
			NbtCompound posTag = new NbtCompound();
			posTag.putLong("Pos", pos);
			glowingBrambles.add(posTag);
		}
		nbt.put("GlowingBrambles", glowingBrambles);
		return nbt;
	}
	
	public static BWWorldState get(World world) {
		return ((ServerWorld) world).getPersistentStateManager().getOrCreate(BWWorldState::readNbt, BWWorldState::new, Bewitchment.MODID + "_universal");
	}
}
