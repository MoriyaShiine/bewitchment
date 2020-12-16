package moriyashiine.bewitchment.common.block;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.api.registry.AltarMapEntry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class WitchAltarBlock extends Block {
	private static final VoxelShape SHAPE = VoxelShapes.union(createCuboidShape(0, 0, 0, 16, 2, 16), createCuboidShape(1, 2, 1, 15, 5, 15), createCuboidShape(2, 5, 2, 14, 10, 14), createCuboidShape(1, 10, 1, 15, 12, 15), createCuboidShape(0, 12, 0, 16, 16, 16));
	
	private final Block unformed;
	private final boolean formed;
	
	public WitchAltarBlock(Settings settings, Block unformed, boolean formed) {
		super(settings);
		this.unformed = unformed;
		this.formed = formed;
	}
	
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return SHAPE;
	}
	
	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		boolean client = world.isClient;
		if (!formed) {
			ItemStack stack = player.getStackInHand(hand);
			AltarMapEntry entry = BewitchmentAPI.ALTAR_MAP_ENTRIES.stream().filter(e -> e.unformed == this && e.carpet == stack.getItem()).findFirst().orElse(null);
			if (entry != null) {
				if (!client) {
					if (!player.isCreative()) {
						stack.decrement(1);
					}
					world.breakBlock(pos, false);
					world.setBlockState(pos, entry.formed.getDefaultState());
				}
				return ActionResult.success(client);
			}
		}
		return super.onUse(state, world, pos, player, hand, hit);
	}
	
	@Environment(EnvType.CLIENT)
	@Override
	public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
		if (unformed != null) {
			return new ItemStack(unformed);
		}
		return super.getPickStack(world, pos, state);
	}
}
