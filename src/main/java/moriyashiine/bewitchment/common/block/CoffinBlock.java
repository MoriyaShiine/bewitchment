package moriyashiine.bewitchment.common.block;

import moriyashiine.bewitchment.common.registry.BWObjects;
import net.minecraft.block.BedBlock;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.Entity;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;

public class CoffinBlock extends BedBlock {
	private static final VoxelShape SHAPE = createCuboidShape(0, 0, 0, 16, 10, 16);
	
	public CoffinBlock(DyeColor color, Settings settings) {
		super(color, settings);
	}
	
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return SHAPE;
	}
	
	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}
	
	@Override
	public void onEntityLand(BlockView world, Entity entity) {
		BWObjects.CYPRESS_PLANKS.onEntityLand(world, entity);
	}
}
