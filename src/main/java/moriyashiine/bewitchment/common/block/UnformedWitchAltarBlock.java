package moriyashiine.bewitchment.common.block;

import moriyashiine.bewitchment.common.block.entity.WitchAltarBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.block.pattern.BlockPatternBuilder;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.*;

public class UnformedWitchAltarBlock extends Block {
	public static final Set<AltarGroup> ALTAR_MAP = new HashSet<>();
	
	private final BlockPattern pattern = BlockPatternBuilder.start().aisle("AAAAA", "ABBBA", "ABBBA", "AAAAA").where('A', state -> !(state.getBlockState().getBlock() instanceof UnformedWitchAltarBlock || state.getBlockState().getBlock() instanceof FormedWitchAltarBlock)).where('B', state -> state.getBlockState().getBlock() == this).build();
	
	public UnformedWitchAltarBlock(Settings settings) {
		super(settings);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		boolean client = world.isClient;
		if (!client) {
			boolean failed = true;
			BlockPattern.Result match = pattern.searchAround(world, pos);
			if (match != null && match.getForwards().getAxis() == Direction.Axis.Y) {
				for (AltarGroup group : ALTAR_MAP) {
					if (group.unformed == this) {
						Map<BlockPos, Boolean> altarPoses = getAltarBlockPoses(world, pos);
						boolean wrongCarpet = false;
						for (BlockPos altarPos : altarPoses.keySet()) {
							if (world.getBlockState(altarPos.up()).getBlock() != group.carpet) {
								wrongCarpet = true;
								break;
							}
						}
						if (!wrongCarpet) {
							altarPoses.keySet().forEach(altarPos -> {
								boolean core = altarPoses.get(altarPos);
								world.setBlockState(altarPos, (core ? group.core : group.formed).getDefaultState());
								if (core) {
									((WitchAltarBlockEntity) Objects.requireNonNull(world.getBlockEntity(altarPos))).refreshAltar();
									FormedWitchAltarBlock.refreshAltarPoses(world, altarPos);
								}
								world.breakBlock(altarPos.up(), false);
							});
							failed = false;
							break;
						}
					}
				}
			}
			if (failed) {
				world.playSound(null, pos, SoundEvents.BLOCK_NOTE_BLOCK_SNARE, SoundCategory.BLOCKS, 1, 0.8f);
				player.sendMessage(new TranslatableText("altar.invalid"), true);
			}
			else {
				world.playSound(null, pos, SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.BLOCKS, 1, 1);
			}
		}
		return ActionResult.success(client);
	}
	
	private Map<BlockPos, Boolean> getAltarBlockPoses(World world, BlockPos pos) {
		Map<BlockPos, Boolean> altarPoses = new HashMap<>();
		BlockPattern.Result match = pattern.searchAround(world, pos);
		if (match != null) {
			for (int w = 1; w < pattern.getWidth() - 1; w++) {
				for (int h = 1; h < pattern.getHeight() - 1; h++) {
					altarPoses.put(match.translate(w, h, 0).getBlockPos(), w == 2 && h == 2);
				}
			}
		}
		return altarPoses;
	}
	
	public static class AltarGroup {
		public final Block unformed, formed, core, carpet;
		
		public AltarGroup(Block unformed, Block formed, Block core, Block carpet) {
			this.unformed = unformed;
			this.formed = formed;
			this.core = core;
			this.carpet = carpet;
		}
	}
}
