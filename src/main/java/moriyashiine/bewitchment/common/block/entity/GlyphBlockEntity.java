package moriyashiine.bewitchment.common.block.entity;

import moriyashiine.bewitchment.api.interfaces.UsesAltarPower;
import moriyashiine.bewitchment.api.registry.RitualFunction;
import moriyashiine.bewitchment.client.network.packet.SyncClientSerializableBlockEntity;
import moriyashiine.bewitchment.common.registry.BWBlockEntityTypes;
import moriyashiine.bewitchment.common.registry.BWRegistries;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.BlockPos;

@SuppressWarnings("ConstantConditions")
public class GlyphBlockEntity extends BlockEntity implements BlockEntityClientSerializable, Tickable, UsesAltarPower {
	private BlockPos altarPos = null;
	
	private RitualFunction ritualFunction = null;
	private int timer = 0, endTime = 0;
	
	private boolean loaded = false;
	
	public GlyphBlockEntity(BlockEntityType<?> type) {
		super(type);
	}
	
	public GlyphBlockEntity() {
		this(BWBlockEntityTypes.GLYPH);
	}
	
	@Override
	public void fromClientTag(CompoundTag tag) {
		ritualFunction = BWRegistries.RITUAL_FUNCTIONS.get(new Identifier(tag.getString("RitualFunction")));
		timer = tag.getInt("Timer");
		endTime = tag.getInt("EndTime");
	}
	
	@Override
	public CompoundTag toClientTag(CompoundTag tag) {
		if (ritualFunction != null) {
			tag.putString("RitualFunction", BWRegistries.RITUAL_FUNCTIONS.getId(ritualFunction).toString());
		}
		tag.putInt("Timer", timer);
		tag.putInt("EndTime", endTime);
		return tag;
	}
	
	@Override
	public void fromTag(BlockState state, CompoundTag tag) {
		fromClientTag(tag);
		super.fromTag(state, tag);
	}
	
	@Override
	public CompoundTag toTag(CompoundTag tag) {
		return super.toTag(toClientTag(tag));
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
		if (world != null) {
			if (!loaded) {
				markDirty();
				loaded = true;
			}
			if (ritualFunction != null) {
				timer++;
				if (timer < 0) {
					if (world.isClient) {
					}
				}
				else {
					ritualFunction.tick(world, pos);
					if (!world.isClient) {
						if (timer == 0) {
							ritualFunction.start(world, pos);
						}
						if (timer >= endTime) {
							ritualFunction.finish(world, pos);
							ritualFunction = null;
							timer = 0;
							syncGlyph();
						}
					}
				}
			}
		}
	}
	
	public void syncGlyph() {
		if (world instanceof ServerWorld) {
			PlayerLookup.tracking(this).forEach(playerEntity -> SyncClientSerializableBlockEntity.send(playerEntity, this));
		}
	}
}
