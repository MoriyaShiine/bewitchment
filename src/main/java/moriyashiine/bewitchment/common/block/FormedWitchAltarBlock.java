package moriyashiine.bewitchment.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

public class FormedWitchAltarBlock extends Block {
	private final Block unformed;
	
	public FormedWitchAltarBlock(Block unformed, Settings settings) {
		super(settings);
		this.unformed = unformed;
		setDefaultState(getDefaultState().with(BWProperties.ALTAR_CORE, false));
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		return super.onUse(state, world, pos, player, hand, hit);
	}
	
	@Override
	public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
		return new ItemStack(unformed);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public PistonBehavior getPistonBehavior(BlockState state) {
		return PistonBehavior.BLOCK;
	}
	
	@Override
	public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		super.onBreak(world, pos, state, player);
		breakAltar(world, pos);
	}
	
	@Override
	public void onDestroyedByExplosion(World world, BlockPos pos, Explosion explosion) {
		super.onDestroyedByExplosion(world, pos, explosion);
		breakAltar(world, pos);
	}
	
	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(BWProperties.ALTAR_CORE);
	}
	
	private void breakAltar(World world, BlockPos pos) {
		for (Direction direction : Direction.Type.HORIZONTAL) {
			BlockPos offset = pos.offset(direction);
			if (world.getBlockState(offset).getBlock() == this) {
				world.breakBlock(offset, true);
				breakAltar(world, offset);
			}
		}
	}
}