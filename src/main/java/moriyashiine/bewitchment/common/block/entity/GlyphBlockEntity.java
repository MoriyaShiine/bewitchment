package moriyashiine.bewitchment.common.block.entity;

import moriyashiine.bewitchment.api.interfaces.UsesAltarPower;
import moriyashiine.bewitchment.common.registry.BWBlockEntityTypes;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.BlockPos;

public class GlyphBlockEntity extends BlockEntity implements BlockEntityClientSerializable, Tickable, UsesAltarPower {
	private BlockPos altarPos = null;
	
	public GlyphBlockEntity(BlockEntityType<?> type) {
		super(type);
	}
	
	public GlyphBlockEntity() {
		this(BWBlockEntityTypes.GLYPH);
	}
	
	@Override
	public void fromClientTag(CompoundTag tag) {
	}
	
	@Override
	public CompoundTag toClientTag(CompoundTag tag) {
		return tag;
	}
	
	@Override
	public BlockPos getAltarPos() {
		return altarPos;
	}
	
	@Override
	public void setAltarPos(BlockPos pos) {
		this.altarPos = pos;
	}
	
	@Override
	public void tick() {
	}
}
