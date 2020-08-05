package moriyashiine.bewitchment.common.block.entity;

import moriyashiine.bewitchment.api.interfaces.MagicAccessor;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.block.FormedWitchAltarBlock;
import moriyashiine.bewitchment.common.block.PlacedItemBlock;
import moriyashiine.bewitchment.common.registry.BWBlockEntityTypes;
import net.fabricmc.fabric.api.server.PlayerStream;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowerPotBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.Item;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.BlockPos;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class WitchAltarBlockEntity extends BlockEntity implements Tickable, MagicAccessor {
	private static final Tag<Block> GIVES_ALTAR_POWER = TagRegistry.block(new Identifier(Bewitchment.MODID, "gives_altar_power"));
	
	private static final Tag<Item> WEAK_SWORD_MODIFIERS = TagRegistry.item(new Identifier(Bewitchment.MODID, "weak_sword_modifiers"));
	private static final Tag<Item> MODERATE_SWORD_MODIFIERS = TagRegistry.item(new Identifier(Bewitchment.MODID, "moderate_sword_modifiers"));
	private static final Tag<Item> STRONG_SWORD_MODIFIERS = TagRegistry.item(new Identifier(Bewitchment.MODID, "strong_sword_modifiers"));
	
	private static final Tag<Item> WEAK_CUP_MODIFIERS = TagRegistry.item(new Identifier(Bewitchment.MODID, "weak_cup_modifiers"));
	private static final Tag<Item> MODERATE_CUP_MODIFIERS = TagRegistry.item(new Identifier(Bewitchment.MODID, "moderate_cup_modifiers"));
	private static final Tag<Item> STRONG_CUP_MODIFIERS = TagRegistry.item(new Identifier(Bewitchment.MODID, "strong_cup_modifiers"));
	
	private static final Tag<Item> WEAK_WAND_MODIFIERS = TagRegistry.item(new Identifier(Bewitchment.MODID, "weak_wand_modifiers"));
	private static final Tag<Item> MODERATE_WAND_MODIFIERS = TagRegistry.item(new Identifier(Bewitchment.MODID, "moderate_wand_modifiers"));
	private static final Tag<Item> STRONG_WAND_MODIFIERS = TagRegistry.item(new Identifier(Bewitchment.MODID, "strong_wand_modifiers"));
	
	private static final Tag<Item> WEAK_PENTACLE_MODIFIERS = TagRegistry.item(new Identifier(Bewitchment.MODID, "weak_pentacle_modifiers"));
	private static final Tag<Item> MODERATE_PENTACLE_MODIFIERS = TagRegistry.item(new Identifier(Bewitchment.MODID, "moderate_pentacle_modifiers"));
	private static final Tag<Item> STRONG_PENTACLE_MODIFIERS = TagRegistry.item(new Identifier(Bewitchment.MODID, "strong_pentacle_modifiers"));
	
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
		if (world != null && !world.isClient) {
			long time = world.getTime();
			if (time % 200 == 0) {
				refreshAltar();
			}
			if (time % 20 == 0) {
				fillMagic(maxMagic, gain, false);
				PlayerStream.around(world, pos, 24).forEach(player -> {
					MagicAccessor playerMagic = ((MagicAccessor) player);
					if (drainMagic(5, true) && playerMagic.fillMagicPlayer(5, true)) {
						drainMagic(5, false);
						playerMagic.fillMagicPlayer(5, false);
					}
				});
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
			BlockPos.Mutable searchPos = new BlockPos.Mutable();
			maxMagic = 0;
			gain = 1;
			float multiplier = 1;
			int flatBonus = 0;
			int varietyCapBonus = 0;
			boolean foundSword = false, foundCup = false, foundWand = false, foundPentacle = false;
			for (byte x = -1; x <= 1; x++) {
				for (byte z = -1; z <= 1; z++) {
					searchPos.set(pos.getX() + x, pos.getY(), pos.getZ() + z);
					Block block = world.getBlockState(searchPos).getBlock();
					if (block instanceof FormedWitchAltarBlock && pos.equals(FormedWitchAltarBlock.findAltarPos(world, searchPos))) {
						block = world.getBlockState(searchPos.up()).getBlock();
						if (block instanceof FlowerPotBlock) {
							block = Blocks.FLOWER_POT;
						}
						Item item = block.asItem();
						if (block instanceof PlacedItemBlock) {
							item = ((PlacedItemBlockEntity) Objects.requireNonNull(world.getBlockEntity(searchPos.up()))).stack.getItem();
						}
						boolean weak = WEAK_SWORD_MODIFIERS.contains(item);
						boolean moderate = MODERATE_SWORD_MODIFIERS.contains(item);
						boolean strong = STRONG_SWORD_MODIFIERS.contains(item);
						if (!foundSword && (weak || moderate || strong)) {
							foundSword = true;
							if (weak) {
								multiplier = 1.1f;
							}
							else if (moderate) {
								multiplier = 1.2f;
							}
							else {
								multiplier = 1.3f;
							}
						}
						weak = WEAK_CUP_MODIFIERS.contains(item);
						moderate = MODERATE_CUP_MODIFIERS.contains(item);
						strong = STRONG_CUP_MODIFIERS.contains(item);
						if (!foundCup && (weak || moderate || strong)) {
							foundCup = true;
							if (weak) {
								varietyCapBonus = 1;
							}
							else if (moderate) {
								varietyCapBonus = 2;
							}
							else {
								varietyCapBonus = 4;
							}
						}
						weak = WEAK_WAND_MODIFIERS.contains(item);
						moderate = MODERATE_WAND_MODIFIERS.contains(item);
						strong = STRONG_WAND_MODIFIERS.contains(item);
						if (!foundWand && (weak || moderate || strong)) {
							foundWand = true;
							if (weak) {
								flatBonus = 40;
							}
							else if (moderate) {
								flatBonus = 80;
							}
							else {
								flatBonus = 120;
							}
						}
						weak = WEAK_PENTACLE_MODIFIERS.contains(item);
						moderate = MODERATE_PENTACLE_MODIFIERS.contains(item);
						strong = STRONG_PENTACLE_MODIFIERS.contains(item);
						if (!foundPentacle && (weak || moderate || strong)) {
							foundPentacle = true;
							if (weak) {
								gain = 3;
							}
							else if (moderate) {
								gain = 6;
							}
							else {
								gain = 9;
							}
						}
					}
				}
			}
			Map<Block, Integer> foundBlocks = new HashMap<>();
			for (byte x = -24; x <= 24; x++) {
				for (byte y = -24; y <= 24; y++) {
					for (byte z = -24; z <= 24; z++) {
						searchPos.set(pos.getX() + x, pos.getY() + y, pos.getZ() + z);
						Block block = world.getBlockState(searchPos).getBlock();
						if (block == Blocks.CARVED_PUMPKIN || block == Blocks.JACK_O_LANTERN) {
							block = Blocks.PUMPKIN;
						}
						if (GIVES_ALTAR_POWER.contains(block)) {
							foundBlocks.put(block, foundBlocks.getOrDefault(block, 0) + 1);
							maxMagic++;
						}
					}
				}
			}
			float varietyMultiplier = 1;
			for (Block block : foundBlocks.keySet()) {
				if (foundBlocks.get(block) > 3) {
					varietyMultiplier *= 1.1f;
				}
			}
			maxMagic = (int) ((maxMagic * Math.min(varietyMultiplier, 4 + varietyCapBonus) / 10) * multiplier + flatBonus);
			magic = Math.min(magic, maxMagic);
			markDirty();
		}
	}
}
