package moriyashiine.bewitchment.common.block.entity;

import moriyashiine.bewitchment.common.registry.BWBlockEntityTypes;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.ChestBlockEntity;

public class BWChestBlockEntity extends ChestBlockEntity {
	public final Type type;
	public final boolean trapped;
	
	public BWChestBlockEntity() {
		this(BWBlockEntityTypes.BW_CHEST, Type.JUNIPER, false);
	}
	
	public BWChestBlockEntity(BlockEntityType<?> blockEntityType, Type type, boolean trapped) {
		super(blockEntityType);
		this.type = type;
		this.trapped = trapped;
	}
	
	@Override
	protected void onInvOpenOrClose() {
		super.onInvOpenOrClose();
		if (trapped && world != null) {
			world.updateNeighborsAlways(pos.down(), getCachedState().getBlock());
		}
	}
	
	public enum Type {
		JUNIPER, CYPRESS, ELDER, DRAGONS_BLOOD
	}
}
