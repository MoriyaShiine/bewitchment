/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.bewitchment.common.block;

import moriyashiine.bewitchment.api.block.WitchAltarBlock;
import moriyashiine.bewitchment.api.block.entity.UsesAltarPower;
import moriyashiine.bewitchment.common.block.entity.BrazierBlockEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.potion.PotionUtil;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Random;

@SuppressWarnings("ConstantConditions")
public class BrazierBlock extends LanternBlock implements BlockEntityProvider {
	private static final VoxelShape STANDING_SHAPE = VoxelShapes.union(createCuboidShape(7, 0, 5, 9, 13, 6), createCuboidShape(5, 0, 7, 6, 13, 9), createCuboidShape(10, 0, 7, 11, 13, 9), createCuboidShape(7, 0, 10, 9, 13, 11), createCuboidShape(7.01, 0, 11, 9.01, 1, 13), createCuboidShape(7.01, 0, 3, 9.01, 1, 5), createCuboidShape(3, 0, 7.01, 5, 1, 9.01), createCuboidShape(11, 0, 7.01, 13, 1, 9.01), createCuboidShape(7, 4.09823, 8.05128, 9, 5.09823, 10.05128), createCuboidShape(6, 4.08, 7, 8, 5.08, 9), createCuboidShape(8, 4.08, 7, 10, 5.08, 9), createCuboidShape(7, 4.09823, 5.94872, 9, 5.09823, 7.94872), createCuboidShape(4.5, 13.5, 4.5, 11.5, 14.5, 11.5), createCuboidShape(4.5, 14.5, 11.5, 11.5, 15.5, 12.5), createCuboidShape(4.5, 14.5, 3.5, 11.5, 15.5, 4.5), createCuboidShape(11.5, 14.5, 3.5, 12.5, 15.5, 12.5), createCuboidShape(3.5, 14.5, 3.5, 4.5, 15.5, 12.5), createCuboidShape(3.5, 15.5, 2.5, 12.5, 16.5, 3.5), createCuboidShape(12.5, 15.5, 3.5, 13.5, 16.5, 12.5), createCuboidShape(3.5, 15.5, 12.5, 12.5, 16.5, 13.5), createCuboidShape(2.5, 15.5, 3.5, 3.5, 16.5, 12.5));
	private static final VoxelShape HANGING_SHAPE = VoxelShapes.union(createCuboidShape(2.5, 2.5, 3.5, 3.5, 3.5, 12.5), createCuboidShape(3.5, 2.5, 12.5, 12.5, 3.5, 13.5), createCuboidShape(12.5, 2.5, 3.5, 13.5, 3.5, 12.5), createCuboidShape(3.5, 2.5, 2.5, 12.5, 3.5, 3.5), createCuboidShape(3.5, 1.5, 3.5, 4.5, 2.5, 12.5), createCuboidShape(4.5, 1.5, 3.5, 11.5, 2.5, 4.5), createCuboidShape(11.5, 1.5, 3.5, 12.5, 2.5, 12.5), createCuboidShape(4.5, 1.5, 11.5, 11.5, 2.5, 12.5), createCuboidShape(4.5, 0.5, 4.5, 11.5, 1.5, 11.5), createCuboidShape(7, -0.7, 5.3, 9, 0.3, 7.3), createCuboidShape(8.7, -0.7, 7, 10.7, 0.3, 9), createCuboidShape(5.3, -0.7, 7, 7.3, 0.3, 9), createCuboidShape(7, -0.7, 8.7, 9, 0.3, 10.7), createCuboidShape(7.5, 3.5, 2.5, 8.5, 5.5, 3.5), createCuboidShape(2.5, 3.5, 7.5, 3.5, 5.5, 8.5), createCuboidShape(7.5, 3.5, 12.5, 8.5, 5.5, 13.5), createCuboidShape(12.5, 3.5, 7.5, 13.5, 5.5, 8.5), createCuboidShape(7, 14, 7, 9, 16, 9));

	public BrazierBlock(Settings settings) {
		super(settings);
		setDefaultState(getDefaultState().with(Properties.LIT, false));
	}

	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new BrazierBlockEntity(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		return (tickerWorld, pos, tickerState, blockEntity) -> BrazierBlockEntity.tick(tickerWorld, pos, tickerState, (BrazierBlockEntity) blockEntity);
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return state.get(Properties.HANGING) ? HANGING_SHAPE : STANDING_SHAPE;
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		boolean client = world.isClient;
		if (!client) {
			((BrazierBlockEntity) world.getBlockEntity(pos)).onUse(world, pos, player, hand);
		}
		return ActionResult.success(client);
	}

	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom) {
		if (state.get(Properties.WATERLOGGED)) {
			if (state.get(Properties.LIT)) {
				world.playSound(null, pos, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 1, 2);
				state = state.with(Properties.LIT, false);
			}
		}
		return super.getStateForNeighborUpdate(state, direction, newState, world, pos, posFrom);
	}

	@Override
	public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
		if (!world.isClient && state.getBlock() != oldState.getBlock()) {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof UsesAltarPower usesAltarPower) {
				usesAltarPower.setAltarPos(WitchAltarBlock.getClosestAltarPos(world, pos));
				blockEntity.markDirty();
			}
		}
	}

	@Override
	public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
		if (!world.isClient && state.getBlock() != newState.getBlock() && world.getBlockEntity(pos) instanceof BrazierBlockEntity brazier && brazier.incenseRecipe == null) {
			ItemScatterer.spawn(world, pos, brazier);
		}
		super.onStateReplaced(state, world, pos, newState, moved);
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
		if (state.get(Properties.LIT)) {
			double y = pos.getY() + (state.get(Properties.HANGING) ? 0.3 : 1.15);
			world.addParticle(ParticleTypes.FLAME, pos.getX() + 0.5 + MathHelper.nextDouble(random, -0.2, 0.2), y, pos.getZ() + 0.5 + MathHelper.nextDouble(random, -0.2, 0.2), 0, 0, 0);
		}
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
		if (!PotionUtil.getCustomPotionEffects(stack).isEmpty()) {
			PotionUtil.buildTooltip(stack, tooltip, 1);
		}
		if (stack.hasNbt() && stack.getNbt().contains("Cost")) {
			tooltip.add(new LiteralText("Cost: " + stack.getOrCreateNbt().getInt("Cost")).formatted(Formatting.GRAY));
		}
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		super.appendProperties(builder.add(Properties.LIT));
	}
}
