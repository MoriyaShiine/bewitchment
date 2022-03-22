/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.bewitchment.common.block;

import moriyashiine.bewitchment.api.block.WitchAltarBlock;
import moriyashiine.bewitchment.api.block.entity.UsesAltarPower;
import moriyashiine.bewitchment.common.block.entity.WitchAltarBlockEntity;
import moriyashiine.bewitchment.common.block.entity.WitchCauldronBlockEntity;
import moriyashiine.bewitchment.common.misc.BWUtil;
import moriyashiine.bewitchment.common.recipe.OilRecipe;
import moriyashiine.bewitchment.common.registry.BWProperties;
import moriyashiine.bewitchment.common.registry.BWTags;
import moriyashiine.bewitchment.common.world.BWWorldState;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.NameTagItem;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("ConstantConditions")
public class WitchCauldronBlock extends Block implements BlockEntityProvider, Waterloggable {
	private static final VoxelShape SHAPE = VoxelShapes.union(createCuboidShape(2, 1, 2, 14, 2, 14), createCuboidShape(14, 2, 1, 15, 6, 15), createCuboidShape(1, 2, 1, 2, 6, 15), createCuboidShape(2, 2, 14, 14, 6, 15), createCuboidShape(13, 5, 3, 14, 8.5, 13), createCuboidShape(2, 2, 1, 14, 6, 2), createCuboidShape(2, 5, 2, 14, 8.5, 3), createCuboidShape(1, 8.5, 14, 15, 11, 15), createCuboidShape(2, 5, 3, 3, 8.5, 13), createCuboidShape(2, 5, 13, 14, 8.5, 14), createCuboidShape(14, 8.5, 2, 15, 11, 14), createCuboidShape(1, 8.5, 1, 15, 11, 2), createCuboidShape(1, 8.5, 2, 2, 11, 14), createCuboidShape(11, 0, 3, 13, 1, 5), createCuboidShape(3, 0, 3, 5, 1, 5), createCuboidShape(3, 0, 11, 5, 1, 13), createCuboidShape(11, 0, 11, 13, 1, 13));

	public WitchCauldronBlock(Settings settings) {
		super(settings);
		setDefaultState(getDefaultState().with(Properties.WATERLOGGED, false).with(BWProperties.LEVEL, 0));
	}

	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new WitchCauldronBlockEntity(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		return (tickerWorld, pos, tickerState, blockEntity) -> WitchCauldronBlockEntity.tick(tickerWorld, pos, tickerState, (WitchCauldronBlockEntity) blockEntity);
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return SHAPE;
	}

	@Override
	public PistonBehavior getPistonBehavior(BlockState state) {
		return PistonBehavior.BLOCK;
	}

	@Nullable
	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		return super.getPlacementState(ctx).with(Properties.WATERLOGGED, ctx.getWorld().getFluidState(ctx.getBlockPos()).getFluid() == Fluids.WATER).with(Properties.LIT, ctx.getWorld().getBlockState(ctx.getBlockPos().down()).isIn(BWTags.HEATS_CAULDRON));
	}

	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom) {
		if (state.get(Properties.WATERLOGGED)) {
			world.createAndScheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
			state = state.with(BWProperties.LEVEL, 0);
			state = state.with(Properties.LIT, false);
		} else {
			state = state.with(Properties.LIT, world.getBlockState(pos.down()).isIn(BWTags.HEATS_CAULDRON));
		}
		return super.getStateForNeighborUpdate(state, direction, newState, world, pos, posFrom);
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return state.get(Properties.WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (world.getBlockEntity(pos) instanceof WitchCauldronBlockEntity cauldron) {
			boolean client = world.isClient;
			ItemStack stack = player.getStackInHand(hand);
			boolean nameTag = stack.getItem() instanceof NameTagItem, bucket = stack.getItem() == Items.BUCKET, waterBucket = stack.getItem() == Items.WATER_BUCKET, glassBottle = stack.getItem() == Items.GLASS_BOTTLE, waterBottle = stack.getItem() == Items.POTION && PotionUtil.getPotion(stack) == Potions.WATER;
			if (nameTag || bucket || waterBucket || glassBottle || waterBottle) {
				if (!client) {
					if (nameTag && stack.hasCustomName()) {
						cauldron.name = stack.getName().asString();
						cauldron.markDirty();
						BWWorldState worldState = BWWorldState.get(world);
						worldState.witchCauldrons.put(pos.asLong(), cauldron.name);
						worldState.markDirty();
						if (!player.isCreative()) {
							stack.decrement(1);
						}
					} else {
						int targetLevel = cauldron.getTargetLevel(stack);
						if (targetLevel > -1) {
							if (bucket) {
								BWUtil.addItemToInventoryAndConsume(player, hand, new ItemStack(Items.WATER_BUCKET));
							} else if (waterBucket) {
								BWUtil.addItemToInventoryAndConsume(player, hand, new ItemStack(Items.BUCKET));
							} else if (glassBottle) {
								ItemStack bottle = null;
								if (cauldron.mode == WitchCauldronBlockEntity.Mode.NORMAL) {
									bottle = PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.WATER);
								} else if (cauldron.mode == WitchCauldronBlockEntity.Mode.OIL_CRAFTING) {
									OilRecipe recipe = cauldron.oilRecipe;
									if (recipe != null) {
										bottle = recipe.getOutput().copy();
									}
								} else {
									bottle = cauldron.getPotion(player);
									if (targetLevel == 2) {
										boolean failed = true;
										BlockPos altarPos = cauldron.getAltarPos();
										if (altarPos != null && ((WitchAltarBlockEntity) world.getBlockEntity(altarPos)).drain(cauldron.getBrewCost(), false)) {
											failed = false;
										}
										if (failed) {
											cauldron.mode = cauldron.fail();
											cauldron.syncCauldron();
											return ActionResult.FAIL;
										}
									}
								}
								if (bottle != null) {
									BWUtil.addItemToInventoryAndConsume(player, hand, bottle);
								}
							} else if (waterBottle) {
								BWUtil.addItemToInventoryAndConsume(player, hand, new ItemStack(Items.GLASS_BOTTLE));
							}
							if (targetLevel == 0) {
								cauldron.mode = cauldron.reset();
							}
							world.setBlockState(pos, state.with(BWProperties.LEVEL, targetLevel));
							world.playSound(null, pos, bucket ? SoundEvents.ITEM_BUCKET_FILL : waterBucket ? SoundEvents.ITEM_BUCKET_EMPTY : glassBottle ? SoundEvents.ITEM_BOTTLE_FILL : SoundEvents.ITEM_BOTTLE_EMPTY, SoundCategory.BLOCKS, 1, 1);
						}
					}
					cauldron.syncCauldron();
				}
			}
			return ActionResult.success(client);
		}
		return super.onUse(state, world, pos, player, hand, hit);
	}

	@Override
	public boolean hasComparatorOutput(BlockState state) {
		return true;
	}

	@Override
	public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
		return state.get(BWProperties.LEVEL);
	}

	@Override
	public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
		if (!world.isClient && state.getBlock() != oldState.getBlock()) {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			((UsesAltarPower) blockEntity).setAltarPos(WitchAltarBlock.getClosestAltarPos(world, pos));
			blockEntity.markDirty();
		}
	}

	@Override
	public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
		if (!world.isClient && state.getBlock() != newState.getBlock()) {
			BWWorldState worldState = BWWorldState.get(world);
			if (worldState.witchCauldrons.remove(pos.asLong()) != null) {
				worldState.markDirty();
			}
		}
		super.onStateReplaced(state, world, pos, newState, moved);
	}

	@Override
	public void onSteppedOn(World world, BlockPos pos, BlockState state, Entity entity) {
		if (world.getBlockState(pos).get(BWProperties.LEVEL) > 0 && entity instanceof LivingEntity) {
			WitchCauldronBlockEntity blockEntity = (WitchCauldronBlockEntity) world.getBlockEntity(pos);
			if (blockEntity.heatTimer >= 60 && blockEntity.mode != WitchCauldronBlockEntity.Mode.TELEPORTATION) {
				entity.damage(DamageSource.HOT_FLOOR, 1);
			}
		}
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(Properties.WATERLOGGED, BWProperties.LEVEL, Properties.LIT);
	}
}
