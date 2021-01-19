package moriyashiine.bewitchment.common.registry;

import com.google.common.collect.Sets;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.block.entity.*;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class BWBlockEntityTypes {
	private static final Map<BlockEntityType<?>, Identifier> BLOCK_ENTITY_TYPES = new LinkedHashMap<>();
	
	public static final BlockEntityType<BWChestBlockEntity> BW_CHEST = create("bw_chest", BlockEntityType.Builder.create(BWChestBlockEntity::new, BWObjects.JUNIPER_CHEST, BWObjects.TRAPPED_JUNIPER_CHEST, BWObjects.CYPRESS_CHEST, BWObjects.TRAPPED_CYPRESS_CHEST, BWObjects.ELDER_CHEST, BWObjects.TRAPPED_ELDER_CHEST).build(null));
	public static final BlockEntityType<WitchAltarBlockEntity> WITCH_ALTAR = create("witch_altar", BlockEntityType.Builder.create(WitchAltarBlockEntity::new, merge(BWObjects.STONE_WITCH_ALTAR, BWObjects.MOSSY_COBBLESTONE_WITCH_ALTAR, BWObjects.PRISMARINE_WITCH_ALTAR, BWObjects.NETHER_BRICK_WITCH_ALTAR, BWObjects.BLACKSTONE_WITCH_ALTAR, BWObjects.GOLDEN_WITCH_ALTAR, BWObjects.END_STONE_WITCH_ALTAR, BWObjects.OBSIDIAN_WITCH_ALTAR, BWObjects.PURPUR_WITCH_ALTAR)).build(null));
	public static final BlockEntityType<WitchCauldronBlockEntity> WITCH_CAULDRON = create("witch_cauldron", BlockEntityType.Builder.create(WitchCauldronBlockEntity::new, BWObjects.WITCH_CAULDRON).build(null));
	public static final BlockEntityType<GlyphBlockEntity> GLYPH = create("glyph", BlockEntityType.Builder.create(GlyphBlockEntity::new, BWObjects.GOLDEN_GLYPH).build(null));
	public static final BlockEntityType<BrazierBlockEntity> BRAZIER = create("brazier", BlockEntityType.Builder.create(BrazierBlockEntity::new, BWObjects.BRAZIER).build(null));
	public static final BlockEntityType<TaglockHolderBlockEntity> TAGLOCK_HOLDER = create("taglock_holder", BlockEntityType.Builder.create(TaglockHolderBlockEntity::new, BWObjects.ELDER_FENCE_GATE, BWObjects.ELDER_PRESSURE_PLATE, BWObjects.ELDER_BUTTON, BWObjects.ELDER_TRAPDOOR, BWObjects.ELDER_DOOR).build(null));
	public static final BlockEntityType<SigilBlockEntity> SIGIL = create("sigil", BlockEntityType.Builder.create(SigilBlockEntity::new, BWObjects.SIGIL, BWObjects.DRAGONS_BLOOD_FENCE_GATE, BWObjects.DRAGONS_BLOOD_PRESSURE_PLATE, BWObjects.DRAGONS_BLOOD_BUTTON, BWObjects.DRAGONS_BLOOD_TRAPDOOR, BWObjects.DRAGONS_BLOOD_DOOR).build(null));
	public static final BlockEntityType<PoppetShelfBlockEntity> POPPET_SHELF = create("poppet_shelf", BlockEntityType.Builder.create(PoppetShelfBlockEntity::new, BWObjects.OAK_POPPET_SHELF, BWObjects.SPRUCE_POPPET_SHELF, BWObjects.BIRCH_POPPET_SHELF, BWObjects.JUNGLE_POPPET_SHELF, BWObjects.ACACIA_POPPET_SHELF, BWObjects.DARK_OAK_POPPET_SHELF, BWObjects.CRIMSON_POPPET_SHELF, BWObjects.WARPED_POPPET_SHELF, BWObjects.JUNIPER_POPPET_SHELF, BWObjects.CYPRESS_POPPET_SHELF, BWObjects.ELDER_POPPET_SHELF, BWObjects.DRAGONS_BLOOD_POPPET_SHELF).build(null));
	public static final BlockEntityType<JuniperChestBlockEntity> JUNIPER_CHEST = create("juniper_chest", BlockEntityType.Builder.create(JuniperChestBlockEntity::new, BWObjects.JUNIPER_CHEST, BWObjects.TRAPPED_JUNIPER_CHEST).build(null));
	public static final BlockEntityType<DragonsBloodChestBlockEntity> DRAGONS_BLOOD_CHEST = create("dragons_blood_chest", BlockEntityType.Builder.create(DragonsBloodChestBlockEntity::new, BWObjects.DRAGONS_BLOOD_CHEST, BWObjects.TRAPPED_DRAGONS_BLOOD_CHEST).build(null));
	
	private static <T extends BlockEntity> BlockEntityType<T> create(String name, BlockEntityType<T> type) {
		BLOCK_ENTITY_TYPES.put(type, new Identifier(Bewitchment.MODID, name));
		return type;
	}
	
	private static Block[] merge(Block[]... blockArrays) {
		Set<Block> merged = new HashSet<>();
		for (Block[] blockArray : blockArrays) {
			merged.addAll(Sets.newHashSet(blockArray));
		}
		return merged.toArray(new Block[0]);
	}
	
	public static void init() {
		BLOCK_ENTITY_TYPES.keySet().forEach(blockEntityType -> Registry.register(Registry.BLOCK_ENTITY_TYPE, BLOCK_ENTITY_TYPES.get(blockEntityType), blockEntityType));
	}
}
