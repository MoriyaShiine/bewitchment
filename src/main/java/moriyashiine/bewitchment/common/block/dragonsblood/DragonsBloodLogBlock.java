package moriyashiine.bewitchment.common.block.dragonsblood;

import com.terraformersmc.terraform.wood.block.StrippableLogBlock;
import moriyashiine.bewitchment.common.item.AthameItem;
import moriyashiine.bewitchment.common.registry.BWObjects;
import moriyashiine.bewitchment.common.registry.BWProperties;
import moriyashiine.bewitchment.common.registry.BWSoundEvents;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.MapColor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.Random;
import java.util.function.Supplier;

public class DragonsBloodLogBlock extends StrippableLogBlock {
	public DragonsBloodLogBlock(Supplier<Block> stripped, MapColor top, Settings settings) {
		super(stripped, top, settings);
		setDefaultState(getDefaultState().with(BWProperties.NATURAL, false).with(BWProperties.CUT, false));
	}
	
	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		ItemStack stack = player.getStackInHand(hand);
		if (state.get(BWProperties.NATURAL) && !state.get(BWProperties.CUT) && stack.getItem() instanceof AthameItem) {
			boolean client = world.isClient;
			if (!client) {
				world.setBlockState(pos, state.with(BWProperties.CUT, true));
				world.playSound(null, pos, BWSoundEvents.ITEM_ATHAME_STRIP, SoundCategory.BLOCKS, 1, 1);
				stack.damage(1, player, (user) -> user.sendToolBreakStatus(hand));
			}
			return ActionResult.success(client);
		}
		return super.onUse(state, world, pos, player, hand, hit);
	}
	
	@Override
	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		if (state.get(BWProperties.CUT)) {
			if (random.nextInt(10) == 0) {
				for (int i = 0; i < 8; i++) {
					BlockPos offset = pos.offset(Direction.Type.HORIZONTAL.random(random));
					if (world.getBlockState(offset).isAir()) {
						ItemScatterer.spawn(world, offset.getX() + 0.5, offset.getY() + 0.5, offset.getZ() + 0.5, new ItemStack(BWObjects.DRAGONS_BLOOD_RESIN));
						world.playSound(null, pos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 1, 1);
						break;
					}
				}
			}
			if (random.nextInt(50) == 0) {
				world.setBlockState(pos, state.with(BWProperties.CUT, false));
			}
		}
	}
	
	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(BWProperties.NATURAL, BWProperties.CUT);
	}
}
