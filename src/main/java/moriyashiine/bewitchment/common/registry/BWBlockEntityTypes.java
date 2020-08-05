package moriyashiine.bewitchment.common.registry;

import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.block.FormedWitchAltarBlock;
import moriyashiine.bewitchment.common.block.entity.*;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class BWBlockEntityTypes {
	private static final Map<BlockEntityType<?>, Identifier> BLOCK_ENTITY_TYPES = new LinkedHashMap<>();
	
	public static final BlockEntityType<WitchAltarBlockEntity> witch_altar = create("witch_altar", WitchAltarBlockEntity::new, block -> block instanceof FormedWitchAltarBlock);
	public static final BlockEntityType<PlacedItemBlockEntity> placed_item = create("placed_item", PlacedItemBlockEntity::new, BWObjects.placed_item);
	public static final BlockEntityType<FocalChalkBlockEntity> focal_chalk = create("focal_chalk", FocalChalkBlockEntity::new, BWObjects.focal_chalk_block);
	public static final BlockEntityType<DistilleryBlockEntity> distillery = create("distillery", DistilleryBlockEntity::new, BWObjects.distillery);
	public static final BlockEntityType<SpinningWheelBlockEntity> spinning_wheel = create("spinning_wheel", SpinningWheelBlockEntity::new, BWObjects.spinning_wheel);
	
	private static <T extends BlockEntity> BlockEntityType<T> create(String name, Supplier<T> factory, Block... validBlocks) {
		BlockEntityType<T> type = BlockEntityType.Builder.create(factory, validBlocks).build(null);
		BLOCK_ENTITY_TYPES.put(type, new Identifier(Bewitchment.MODID, name));
		return type;
	}
	
	private static <T extends BlockEntity> BlockEntityType<T> create(String name, Supplier<T> factory, Predicate<Block> predicate) {
		List<Block> validBlocks = new ArrayList<>();
		for (Block block : Registry.BLOCK) {
			if (predicate.test(block)) {
				validBlocks.add(block);
			}
		}
		return create(name, factory, validBlocks.toArray(new Block[0]));
	}
	
	public static void init() {
		BLOCK_ENTITY_TYPES.keySet().forEach(type -> Registry.register(Registry.BLOCK_ENTITY_TYPE, BLOCK_ENTITY_TYPES.get(type), type));
	}
}
