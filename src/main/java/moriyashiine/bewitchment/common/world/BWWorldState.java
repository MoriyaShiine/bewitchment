package moriyashiine.bewitchment.common.world;

import moriyashiine.bewitchment.common.Bewitchment;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
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
	
	public BWWorldState(String key) {
		super(key);
	}
	
	@Override
	public CompoundTag toTag(CompoundTag tag) {
		ListTag potentialCandelabras = new ListTag();
		for (long pos : this.potentialCandelabras) {
			CompoundTag posTag = new CompoundTag();
			posTag.putLong("Pos", pos);
			potentialCandelabras.add(posTag);
		}
		tag.put("PotentialCandelabras", potentialCandelabras);
		ListTag potentialSigils = new ListTag();
		for (long pos : this.potentialSigils) {
			CompoundTag posTag = new CompoundTag();
			posTag.putLong("Pos", pos);
			potentialSigils.add(posTag);
		}
		tag.put("PotentialSigils", potentialSigils);
		ListTag witchCauldrons = new ListTag();
		for (long pos : this.witchCauldrons) {
			CompoundTag posTag = new CompoundTag();
			posTag.putLong("Pos", pos);
			witchCauldrons.add(posTag);
		}
		tag.put("WitchCauldrons", witchCauldrons);
		ListTag poppetShelves = new ListTag();
		for (long pos : this.poppetShelves) {
			CompoundTag posTag = new CompoundTag();
			posTag.putLong("Pos", pos);
			poppetShelves.add(posTag);
		}
		tag.put("PoppetShelves", poppetShelves);
		ListTag glowingBrambles = new ListTag();
		for (long pos : this.glowingBrambles) {
			CompoundTag posTag = new CompoundTag();
			posTag.putLong("Pos", pos);
			glowingBrambles.add(posTag);
		}
		tag.put("GlowingBrambles", glowingBrambles);
		return tag;
	}
	
	@Override
	public void fromTag(CompoundTag tag) {
		ListTag potentialCandelabras = tag.getList("PotentialCandelabras", NbtType.COMPOUND);
		for (int i = 0; i < potentialCandelabras.size(); i++) {
			CompoundTag posTag = potentialCandelabras.getCompound(i);
			this.potentialCandelabras.add(posTag.getLong("Pos"));
		}
		ListTag potentialSigils = tag.getList("PotentialSigils", NbtType.COMPOUND);
		for (int i = 0; i < potentialSigils.size(); i++) {
			CompoundTag posTag = potentialSigils.getCompound(i);
			this.potentialSigils.add(posTag.getLong("Pos"));
		}
		ListTag witchCauldrons = tag.getList("WitchCauldrons", NbtType.COMPOUND);
		for (int i = 0; i < witchCauldrons.size(); i++) {
			CompoundTag posTag = witchCauldrons.getCompound(i);
			this.witchCauldrons.add(posTag.getLong("Pos"));
		}
		ListTag poppetShelves = tag.getList("PoppetShelves", NbtType.COMPOUND);
		for (int i = 0; i < poppetShelves.size(); i++) {
			CompoundTag posTag = poppetShelves.getCompound(i);
			this.poppetShelves.add(posTag.getLong("Pos"));
		}
		ListTag glowingBrambles = tag.getList("GlowingBrambles", NbtType.COMPOUND);
		for (int i = 0; i < glowingBrambles.size(); i++) {
			CompoundTag posTag = glowingBrambles.getCompound(i);
			this.glowingBrambles.add(posTag.getLong("Pos"));
		}
	}
	
	public static BWWorldState get(World world) {
		return ((ServerWorld) world).getPersistentStateManager().getOrCreate(() -> new BWWorldState(Bewitchment.MODID), Bewitchment.MODID);
	}
}
