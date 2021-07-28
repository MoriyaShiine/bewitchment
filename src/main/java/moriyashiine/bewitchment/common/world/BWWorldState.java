package moriyashiine.bewitchment.common.world;

import moriyashiine.bewitchment.common.Bewitchment;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.PersistentState;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class BWWorldState extends PersistentState {
	public final List<Long> potentialCandelabras = new ArrayList<>();
	public final List<Long> potentialSigils = new ArrayList<>();
	public final List<Long> witchCauldrons = new ArrayList<>();
	public final List<Long> poppetShelves = new ArrayList<>();
	public final List<Long> glowingBrambles = new ArrayList<>();
	
	public static BWWorldState readNbt(NbtCompound nbt) {
		BWWorldState worldState = new BWWorldState();
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
		NbtList poppetShelves = nbt.getList("PoppetShelves", NbtType.COMPOUND);
		for (int i = 0; i < poppetShelves.size(); i++) {
			NbtCompound posTag = poppetShelves.getCompound(i);
			worldState.poppetShelves.add(posTag.getLong("Pos"));
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
		NbtList poppetShelves = new NbtList();
		for (long pos : this.poppetShelves) {
			NbtCompound posTag = new NbtCompound();
			posTag.putLong("Pos", pos);
			poppetShelves.add(posTag);
		}
		nbt.put("PoppetShelves", poppetShelves);
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
