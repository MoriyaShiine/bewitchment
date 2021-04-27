package moriyashiine.bewitchment.common.block;

import moriyashiine.bewitchment.api.item.SigilItem;
import moriyashiine.bewitchment.common.block.entity.SigilBlockEntity;
import moriyashiine.bewitchment.common.block.entity.interfaces.SigilHolder;
import moriyashiine.bewitchment.common.registry.BWObjects;
import moriyashiine.bewitchment.common.registry.BWProperties;
import moriyashiine.bewitchment.common.world.BWWorldState;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("ConstantConditions")
public class SigilBlock extends HorizontalFacingBlock implements BlockEntityProvider {
	private static final VoxelShape SHAPE = createCuboidShape(0, 0, 0, 16, 0.125, 16);
	
	public SigilBlock(Settings settings) {
		super(settings);
	}
	
	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockView world) {
		return new SigilBlockEntity();
	}
	
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return SHAPE;
	}
	
	@Override
	public PistonBehavior getPistonBehavior(BlockState state) {
		return PistonBehavior.DESTROY;
	}
	
	@Nullable
	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		return super.getPlacementState(ctx).with(FACING, ctx.getPlayerFacing()).with(BWProperties.TYPE, ctx.getWorld().random.nextInt(10));
	}
	
	@Environment(EnvType.CLIENT)
	@Override
	public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof SigilHolder) {
			for (Item sigil : Registry.ITEM) {
				if (sigil instanceof SigilItem) {
					if (((SigilHolder) blockEntity).getSigil() == ((SigilItem) sigil).sigil) {
						return new ItemStack(sigil);
					}
				}
			}
		}
		return ItemStack.EMPTY;
	}
	
	@Override
	public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
		return BWObjects.SALT_LINE.canPlaceAt(state, world, pos);
	}
	
	@Override
	public boolean canReplace(BlockState state, ItemPlacementContext context) {
		return true;
	}
	
	@Override
	public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
		BWObjects.SALT_LINE.neighborUpdate(state, world, pos, block, fromPos, notify);
	}
	
	@Override
	public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
		if (!world.isClient && state.getBlock() != oldState.getBlock()) {
			BWWorldState worldState = BWWorldState.get(world);
			worldState.potentialSigils.add(pos.asLong());
			worldState.markDirty();
		}
	}
	
	@Override
	public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
		if (!world.isClient && state.getBlock() != newState.getBlock()) {
			BWWorldState worldState = BWWorldState.get(world);
			for (int i = worldState.potentialSigils.size() - 1; i >= 0; i--) {
				if (worldState.potentialSigils.get(i) == pos.asLong()) {
					worldState.potentialSigils.remove(i);
					worldState.markDirty();
				}
			}
		}
		super.onStateReplaced(state, world, pos, newState, moved);
	}
	
	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(FACING, BWProperties.TYPE);
	}
}
