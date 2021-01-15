package moriyashiine.bewitchment.common.block;

import moriyashiine.bewitchment.api.block.WitchAltarBlock;
import moriyashiine.bewitchment.api.interfaces.CurseAccessor;
import moriyashiine.bewitchment.api.interfaces.FortuneAccessor;
import moriyashiine.bewitchment.api.registry.Fortune;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.block.entity.WitchAltarBlockEntity;
import moriyashiine.bewitchment.common.registry.BWCurses;
import moriyashiine.bewitchment.common.registry.BWParticleTypes;
import moriyashiine.bewitchment.common.registry.BWRegistries;
import moriyashiine.bewitchment.common.registry.BWSoundEvents;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.text.TranslatableText;
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
		BlockPos nearestAltarPos = WitchAltarBlock.getClosestAltarPos(world, pos);
		for (int i = 0; i < 10; i++) {
			world.addParticle(new DustParticleEffect(1, 1, 1, 1.0F), (double)pos.getX() + world.random.nextFloat(), (double)pos.getY() + world.random.nextFloat(), (double)pos.getZ() + world.random.nextFloat(), 0, 0, 0);
		}

		if (nearestAltarPos != null && ((WitchAltarBlockEntity) world.getBlockEntity(nearestAltarPos)).drain(500, false)) {
			FortuneAccessor.of(player).ifPresent(fortuneAccessor -> {
				if (fortuneAccessor.getFortune() == null) {
					world.playSound(null, pos, BWSoundEvents.BLOCK_CRYSTAL_BALL_FIRE, SoundCategory.BLOCKS, 1, 1);
					Fortune fortune = BWRegistries.FORTUNES.get(world.random.nextInt(BWRegistries.FORTUNES.getEntries().size()));
					CurseAccessor curseAccessor = CurseAccessor.of(player).orElse(null);
					if (curseAccessor.hasCurse(BWCurses.UNLUCKY)) {
						while (fortune.positive) {
							fortune = BWRegistries.FORTUNES.get(world.random.nextInt(BWRegistries.FORTUNES.getEntries().size()));
						}
					}
					fortuneAccessor.setFortune(new Fortune.Instance(fortune, world.random.nextInt(120000)));
					player.sendMessage(new TranslatableText("fortune." + BWRegistries.FORTUNES.getId(fortune).toString().replace(":", ".")), true);
					
				}
				else {
					world.playSound(null, pos, BWSoundEvents.BLOCK_CRYSTAL_BALL_FAIL, SoundCategory.BLOCKS, 1, 1);
					player.sendMessage(new TranslatableText(Bewitchment.MODID + ".has_fortune"), true);
				}
			});
		}
		else if (!client) {
			world.playSound(null, pos, BWSoundEvents.BLOCK_CRYSTAL_BALL_FAIL, SoundCategory.BLOCKS, 1, 1);
			player.sendMessage(new TranslatableText(Bewitchment.MODID + ".insufficent_altar_power"), true);
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
			world.getFluidTickScheduler().schedule(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
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
