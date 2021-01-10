package moriyashiine.bewitchment.common.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.LanternBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

public class BrazierBlock extends LanternBlock {
	private static final VoxelShape STANDING_SHAPE = VoxelShapes.union(createCuboidShape(7, 0, 5, 9, 13, 6), createCuboidShape(5, 0, 7, 6, 13, 9), createCuboidShape(10, 0, 7, 11, 13, 9), createCuboidShape(7, 0, 10, 9, 13, 11), createCuboidShape(7.01, 0, 11, 9.01, 1, 13), createCuboidShape(7.01, 0, 3, 9.01, 1, 5), createCuboidShape(3, 0, 7.01, 5, 1, 9.01), createCuboidShape(11, 0, 7.01, 13, 1, 9.01), createCuboidShape(7, 4.09823, 8.05128, 9, 5.09823, 10.05128), createCuboidShape(6, 4.08, 7, 8, 5.08, 9), createCuboidShape(8, 4.08, 7, 10, 5.08, 9), createCuboidShape(7, 4.09823, 5.94872, 9, 5.09823, 7.94872), createCuboidShape(4.5, 13.5, 4.5, 11.5, 14.5, 11.5), createCuboidShape(4.5, 14.5, 11.5, 11.5, 15.5, 12.5), createCuboidShape(4.5, 14.5, 3.5, 11.5, 15.5, 4.5), createCuboidShape(11.5, 14.5, 3.5, 12.5, 15.5, 12.5), createCuboidShape(3.5, 14.5, 3.5, 4.5, 15.5, 12.5), createCuboidShape(3.5, 15.5, 2.5, 12.5, 16.5, 3.5), createCuboidShape(12.5, 15.5, 3.5, 13.5, 16.5, 12.5), createCuboidShape(3.5, 15.5, 12.5, 12.5, 16.5, 13.5), createCuboidShape(2.5, 15.5, 3.5, 3.5, 16.5, 12.5));
	private static final VoxelShape HANGING_SHAPE = VoxelShapes.union(createCuboidShape(2.5, 2.5, 3.5, 3.5, 3.5, 12.5), createCuboidShape(3.5, 2.5, 12.5, 12.5, 3.5, 13.5), createCuboidShape(12.5, 2.5, 3.5, 13.5, 3.5, 12.5), createCuboidShape(3.5, 2.5, 2.5, 12.5, 3.5, 3.5), createCuboidShape(3.5, 1.5, 3.5, 4.5, 2.5, 12.5), createCuboidShape(4.5, 1.5, 3.5, 11.5, 2.5, 4.5), createCuboidShape(11.5, 1.5, 3.5, 12.5, 2.5, 12.5), createCuboidShape(4.5, 1.5, 11.5, 11.5, 2.5, 12.5), createCuboidShape(4.5, 0.5, 4.5, 11.5, 1.5, 11.5), createCuboidShape(7, -0.7, 5.3, 9, 0.3, 7.3), createCuboidShape(8.7, -0.7, 7, 10.7, 0.3, 9), createCuboidShape(5.3, -0.7, 7, 7.3, 0.3, 9), createCuboidShape(7, -0.7, 8.7, 9, 0.3, 10.7), createCuboidShape(7.5, 3.5, 2.5, 8.5, 5.5, 3.5), createCuboidShape(2.5, 3.5, 7.5, 3.5, 5.5, 8.5), createCuboidShape(7.5, 3.5, 12.5, 8.5, 5.5, 13.5), createCuboidShape(12.5, 3.5, 7.5, 13.5, 5.5, 8.5), createCuboidShape(7, 14, 7, 9, 16, 9));
	
	public BrazierBlock(Settings settings) {
		super(settings);
	}
	
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return state.get(Properties.HANGING) ? HANGING_SHAPE : STANDING_SHAPE;
	}
}
