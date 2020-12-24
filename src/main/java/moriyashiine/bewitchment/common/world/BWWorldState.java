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
	public final List<Long> witchCauldrons = new ArrayList<>();
	
	public BWWorldState(String key) {
		super(key);
	}
	
	@Override
	public CompoundTag toTag(CompoundTag tag) {
		ListTag witchCauldrons = new ListTag();
		for (long pos : this.witchCauldrons) {
			CompoundTag posTag = new CompoundTag();
			posTag.putLong("Pos", pos);
			witchCauldrons.add(posTag);
		}
		tag.put("WitchCauldrons", witchCauldrons);
		return tag;
	}
	
	@Override
	public void fromTag(CompoundTag tag) {
		ListTag witchCauldrons = tag.getList("WitchCauldrons", NbtType.COMPOUND);
		for (int i = 0; i < witchCauldrons.size(); i++) {
			CompoundTag posTag = witchCauldrons.getCompound(i);
			this.witchCauldrons.add(posTag.getLong("Pos"));
		}
	}
	
	public static BWWorldState get(World world) {
		return ((ServerWorld) world).getPersistentStateManager().getOrCreate(() -> new BWWorldState(Bewitchment.MODID), Bewitchment.MODID);
	}
}
