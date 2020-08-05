package moriyashiine.bewitchment.common.block.util;

import moriyashiine.bewitchment.common.registry.BWObjects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class BWCropBlock extends CropBlock {
	private static final VoxelShape[] AGE_TO_SHAPE = {Block.createCuboidShape(0, 0, 0, 16, 2, 16), Block.createCuboidShape(0, 0, 0, 16, 4, 16), Block.createCuboidShape(0, 0, 0, 16, 6, 16), Block.createCuboidShape(0, 0, 0, 16, 8, 16), Block.createCuboidShape(0, 0, 0, 16, 10, 16), Block.createCuboidShape(0, 0, 0, 16, 12, 16), Block.createCuboidShape(0, 0, 0, 16, 14, 16), Block.createCuboidShape(0, 0, 0, 16, 16, 16)};
	
	private final Item seeds;
	
	public BWCropBlock(Item seeds, Settings settings) {
		super(settings);
		this.seeds = seeds;
	}
	
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		int age = getAge(state);
		int modelAge = 0;
		if (this == BWObjects.aconite_crops) {
			modelAge = age == 0 ? 1 : age == 1 ? 3 : age == 2 ? 5 : 7;
		}
		if (this == BWObjects.belladonna_crops) {
			modelAge = age == 0 ? 1 : age == 1 ? 3 : age == 2 ? 5 : 6;
		}
		if (this == BWObjects.garlic_crops) {
			modelAge = age == 0 ? 2 : age == 1 ? 3 : age == 2 ? 5 : 6;
		}
		if (this == BWObjects.mandrake_crops) {
			modelAge = age == 0 ? 1 : age == 1 ? 2 : age == 2 ? 3 : 3;
		}
		return AGE_TO_SHAPE[modelAge];
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	protected ItemConvertible getSeedsItem() {
		return seeds;
	}
	
	@Override
	public IntProperty getAgeProperty() {
		return Properties.AGE_3;
	}
	
	@Override
	public int getMaxAge() {
		return 3;
	}
	
	@Override
	protected int getGrowthAmount(World world) {
		return MathHelper.nextInt(world.random, 1, 2);
	}
	
	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(getAgeProperty());
	}
}
