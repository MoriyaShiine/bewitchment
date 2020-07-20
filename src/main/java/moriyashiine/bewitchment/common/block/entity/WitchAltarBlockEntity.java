package moriyashiine.bewitchment.common.block.entity;

import moriyashiine.bewitchment.api.interfaces.MagicAccessor;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.registry.BWBlockEntityTypes;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.BlockPos;

import java.util.HashMap;
import java.util.Map;

public class WitchAltarBlockEntity extends BlockEntity implements Tickable, MagicAccessor {
	private static final Tag<Block> GIVES_ALTAR_POWER = TagRegistry.block(new Identifier(Bewitchment.MODID, "gives_altar_power"));
	
	public int magic = 0, maxMagic = 0, gain = 1;
	
	public WitchAltarBlockEntity() {
		super(BWBlockEntityTypes.witch_altar);
	}
	
	@Override
	public BlockEntityUpdateS2CPacket toUpdatePacket() {
		return new BlockEntityUpdateS2CPacket(pos, 0, toTag(new CompoundTag()));
	}
	
	@Override
	public void tick() {
		if (world != null) {
			long time = world.getTime();
			if (time % 200 == 0) {
				refreshAltar();
			}
			if (time % 20 == 0) {
				fillMagic(maxMagic, gain, false);
			}
		}
	}
	
	@Override
	public void fromTag(BlockState state, CompoundTag tag) {
		magic = tag.getInt("Magic");
		maxMagic = tag.getInt("MaxMagic");
		gain = tag.getInt("Gain");
		super.fromTag(state, tag);
	}
	
	@Override
	public CompoundTag toTag(CompoundTag tag) {
		tag.putInt("Magic", magic);
		tag.putInt("MaxMagic", maxMagic);
		tag.putInt("Gain", gain);
		return super.toTag(tag);
	}
	
	@Override
	public int getMagic() {
		return magic;
	}
	
	@Override
	public void setMagic(int magic) {
		this.magic = magic;
	}
	
	public void refreshAltar() {
		if (world != null) {
			maxMagic = 0;
			gain = 1;
			Map<Block, Integer> foundBlocks = new HashMap<>();
			BlockPos.Mutable searchPos = new BlockPos.Mutable();
			for (int x = -24; x <= 24; x++) {
				for (int y = -24; y <= 24; y++) {
					for (int z = -24; z <= 24; z++) {
						searchPos.set(pos.getX() + x, pos.getY() + y, pos.getZ() + z);
						Block block = world.getBlockState(searchPos).getBlock();
						if (block == Blocks.CARVED_PUMPKIN || block == Blocks.JACK_O_LANTERN) {
							block = Blocks.PUMPKIN;
						}
						if (GIVES_ALTAR_POWER.contains(block)) {
							foundBlocks.put(block, foundBlocks.getOrDefault(block, 0) + 1);
							if (foundBlocks.get(block) < foundBlocks.size() * 2)
							{
								maxMagic += 1;
							}
						}
					}
				}
			}
			magic = Math.min(magic, maxMagic);
			markDirty();
		}
	}
}