package moriyashiine.bewitchment.common.registry;

import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.block.entity.BWChestBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.LinkedHashMap;
import java.util.Map;

public class BWBlockEntityTypes {
	private static final Map<BlockEntityType<?>, Identifier> BLOCK_ENTITY_TYPES = new LinkedHashMap<>();
	
	public static final BlockEntityType<BWChestBlockEntity> BW_CHEST = create("bw_chest", BlockEntityType.Builder.create(BWChestBlockEntity::new, BWObjects.JUNIPER_CHEST, BWObjects.TRAPPED_JUNIPER_CHEST, BWObjects.CYPRESS_CHEST, BWObjects.TRAPPED_CYPRESS_CHEST, BWObjects.ELDER_CHEST, BWObjects.TRAPPED_ELDER_CHEST, BWObjects.DRAGONS_BLOOD_CHEST, BWObjects.TRAPPED_DRAGONS_BLOOD_CHEST).build(null));
	
	private static <T extends BlockEntity> BlockEntityType<T> create(String name, BlockEntityType<T> type) {
		BLOCK_ENTITY_TYPES.put(type, new Identifier(Bewitchment.MODID, name));
		return type;
	}
	
	public static void init() {
		BLOCK_ENTITY_TYPES.keySet().forEach(blockEntityType -> Registry.register(Registry.BLOCK_ENTITY_TYPE, BLOCK_ENTITY_TYPES.get(blockEntityType), blockEntityType));
	}
}
