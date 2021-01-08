package moriyashiine.bewitchment.common.block;

import moriyashiine.bewitchment.api.interfaces.HasSigil;
import moriyashiine.bewitchment.common.block.entity.SigilBlockEntity;
import moriyashiine.bewitchment.common.registry.BWObjects;
import moriyashiine.bewitchment.common.registry.BWProperties;
import moriyashiine.bewitchment.common.registry.BWRegistries;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.BlockPos;
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
		return super.getPlacementState(ctx).with(FACING, ctx.getPlayerFacing()).with(BWProperties.TYPE, ctx.getStack().hasTag() && ctx.getStack().getOrCreateTag().contains("Type") ? ctx.getStack().getOrCreateTag().getInt("Type") : 0);
	}
	
	@Environment(EnvType.CLIENT)
	@Override
	public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
		ItemStack stack = new ItemStack(BWObjects.SIGIL_ITEM);
		stack.getOrCreateTag().putInt("Type", state.get(BWProperties.TYPE));
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof HasSigil) {
			stack.getOrCreateTag().putString("Sigil", BWRegistries.SIGILS.getId(((HasSigil) blockEntity).getSigil()).toString());
		}
		return stack;
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
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(FACING, BWProperties.TYPE);
	}
}
