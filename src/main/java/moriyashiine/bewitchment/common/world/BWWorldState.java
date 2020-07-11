package moriyashiine.bewitchment.common.world;

import moriyashiine.bewitchment.Bewitchment;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.PersistentState;
import net.minecraft.world.World;

import java.util.Objects;

public class BWWorldState extends PersistentState {
	private static final String KEY = Bewitchment.MODID;
	
	public BWWorldState(String name) {
		super(name);
	}
	
	@Override
	public void fromTag(CompoundTag tag) {
	}
	
	@Override
	public CompoundTag toTag(CompoundTag tag) {
		return tag;
	}
	
	public static BWWorldState get(World world) {
		return Objects.requireNonNull(Objects.requireNonNull(world.getServer()).getWorld(World.OVERWORLD)).getPersistentStateManager().getOrCreate(() -> new BWWorldState(KEY), KEY);
	}
}