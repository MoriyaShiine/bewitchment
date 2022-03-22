/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.bewitchment.common.block;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.api.block.WitchAltarBlock;
import moriyashiine.bewitchment.api.component.FortuneComponent;
import moriyashiine.bewitchment.api.registry.Fortune;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.block.entity.WitchAltarBlockEntity;
import moriyashiine.bewitchment.common.block.entity.interfaces.SigilHolder;
import moriyashiine.bewitchment.common.item.TaglockItem;
import moriyashiine.bewitchment.common.misc.BWUtil;
import moriyashiine.bewitchment.common.registry.*;
import moriyashiine.bewitchment.common.world.BWUniversalWorldState;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.Waterloggable;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3f;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("ConstantConditions")
public class CrystalBallBlock extends Block implements Waterloggable {
	private static final VoxelShape SHAPE = VoxelShapes.union(createCuboidShape(5, 11, 5, 11, 12, 11), createCuboidShape(3.5, 2.5, 4, 4.5, 3.5, 12), createCuboidShape(5, 4, 3, 11, 10, 4), createCuboidShape(3, 4, 5, 4, 10, 11), createCuboidShape(4, 3, 4, 12, 11, 12), createCuboidShape(12, 4, 5, 13, 10, 11), createCuboidShape(3.6, 0, 11.4, 4.6, 3, 12.4), createCuboidShape(11.4, 0, 11.4, 12.4, 3, 12.4), createCuboidShape(3.5, 2.5, 11.5, 12.5, 3.5, 12.5), createCuboidShape(11.5, 2.5, 4, 12.5, 3.5, 12), createCuboidShape(11.4, 0, 3.6, 12.4, 3, 4.6), createCuboidShape(5, 2, 5, 11, 3, 11), createCuboidShape(5, 4, 12, 11, 10, 13), createCuboidShape(3.5, 2.5, 3.5, 12.5, 3.5, 4.5), createCuboidShape(3.6, 0, 3.6, 4.6, 3, 4.6));

	public CrystalBallBlock(Settings settings) {
		super(settings);
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return SHAPE;
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		boolean client = world.isClient;
		if (client) {
			for (int i = 0; i < 10; i++) {
				world.addParticle(new DustParticleEffect(new Vec3f(1, 1, 1), 1), pos.getX() + world.random.nextFloat(), pos.getY() + world.random.nextFloat(), pos.getZ() + world.random.nextFloat(), 0, 0, 0);
			}
		} else {
			SoundEvent sound = BWSoundEvents.BLOCK_CRYSTAL_BALL_FAIL;
			BlockPos nearestAltarPos = WitchAltarBlock.getClosestAltarPos(world, pos);
			if (nearestAltarPos != null && ((WitchAltarBlockEntity) world.getBlockEntity(nearestAltarPos)).drain(500, false)) {
				ItemStack stack = player.getStackInHand(hand);
				if (stack.getItem() instanceof TaglockItem && TaglockItem.isTaglockFromPlayer(stack) && !stack.getNbt().contains("UsedForScrying")) {
					if (BewitchmentAPI.getTaglockOwner(world, stack) instanceof PlayerEntity owner) {
						boolean failed = false;
						BlockPos sigilPos = BWUtil.getClosestBlockPos(owner.getBlockPos(), 16, currentPos -> world.getBlockEntity(currentPos) instanceof SigilHolder sigilHolder && sigilHolder.getSigil() == BWSigils.SHADOWS);
						if (sigilPos == null) {
							sigilPos = BWUtil.getClosestBlockPos(pos, 16, currentPos -> world.getBlockEntity(currentPos) instanceof SigilHolder sigilHolder && sigilHolder.getSigil() == BWSigils.SHADOWS);
						}
						if (sigilPos != null) {
							BlockEntity blockEntity = world.getBlockEntity(sigilPos);
							SigilHolder sigilHolder = (SigilHolder) blockEntity;
							if (sigilHolder.test(player)) {
								sigilHolder.setUses(sigilHolder.getUses() - 1);
								blockEntity.markDirty();
								failed = true;
							}
						}
						ItemStack newTaglock = new ItemStack(BWObjects.TAGLOCK);
						NbtCompound taglockCompound = newTaglock.getOrCreateNbt().copyFrom(stack.getNbt());
						taglockCompound.putBoolean("UsedForScrying", true);
						if (!failed) {
							taglockCompound.putLong("LocationPos", owner.getBlockPos().asLong());
							taglockCompound.putString("LocationWorld", world.getRegistryKey().getValue().toString());
							taglockCompound.putInt("Level", owner.experienceLevel);
							taglockCompound.put("Curses", BWComponents.CURSES_COMPONENT.get(owner).toNbtCurse());
							taglockCompound.put("Contracts", BWComponents.CONTRACTS_COMPONENT.get(owner).toNbtContract());
							taglockCompound.putString("Transformation", "transformation." + BWRegistries.TRANSFORMATIONS.getId(BWComponents.TRANSFORMATION_COMPONENT.get(owner).getTransformation()).toString().replace(":", "."));
							BWUniversalWorldState universalWorldState = BWUniversalWorldState.get(world);
							String familiar = "none";
							for (int i = 0; i < universalWorldState.familiars.size(); i++) {
								if (universalWorldState.familiars.get(i).getLeft().equals(owner.getUuid())) {
									familiar = universalWorldState.familiars.get(i).getRight().getString("id");
									break;
								}
							}
							taglockCompound.putString("Familiar", familiar);
							taglockCompound.putString("Pledge", BWComponents.PLEDGE_COMPONENT.get(owner).getPledge());
							sound = BWSoundEvents.BLOCK_CRYSTAL_BALL_FIRE;
						} else {
							taglockCompound.putBoolean("Failed", true);
							player.sendMessage(new TranslatableText(Bewitchment.MODID + ".message.blocked_by_shadows"), true);
						}
						BWUtil.addItemToInventoryAndConsume(player, hand, newTaglock);
					} else {
						player.sendMessage(new TranslatableText(Bewitchment.MODID + ".message.invalid_entity"), true);
					}
				} else {
					FortuneComponent fortuneComponent = BWComponents.FORTUNE_COMPONENT.get(player);
					if (fortuneComponent.getFortune() == null) {
						sound = BWSoundEvents.BLOCK_CRYSTAL_BALL_FIRE;
						Fortune fortune = BWRegistries.FORTUNES.get(world.random.nextInt(BWRegistries.FORTUNES.size()));
						if (BWComponents.CURSES_COMPONENT.get(player).hasCurse(BWCurses.UNLUCKY)) {
							while (fortune.positive) {
								fortune = BWRegistries.FORTUNES.get(world.random.nextInt(BWRegistries.FORTUNES.size()));
							}
						}
						fortuneComponent.setFortune(new Fortune.Instance(fortune, world.random.nextInt(120000)));
						player.sendMessage(new TranslatableText("fortune." + BWRegistries.FORTUNES.getId(fortune).toString().replace(":", ".")), true);

					} else {
						player.sendMessage(new TranslatableText(Bewitchment.MODID + ".message.has_fortune"), true);
					}
				}
			} else {
				player.sendMessage(new TranslatableText(Bewitchment.MODID + ".message.insufficent_altar_power"), true);
			}
			world.playSound(null, pos, sound, SoundCategory.BLOCKS, 1, 1);
		}
		return ActionResult.success(client);
	}

	@SuppressWarnings("ConstantConditions")
	@Nullable
	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		return super.getPlacementState(ctx).with(Properties.WATERLOGGED, ctx.getWorld().getFluidState(ctx.getBlockPos()).getFluid() == Fluids.WATER);
	}

	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom) {
		if (state.get(Properties.WATERLOGGED)) {
			world.createAndScheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
		}
		return super.getStateForNeighborUpdate(state, direction, newState, world, pos, posFrom);
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return state.get(Properties.WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(Properties.WATERLOGGED);
	}
}
