package moriyashiine.bewitchment.common.item;

import moriyashiine.bewitchment.common.registry.BWObjects;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class SigilItem extends Item {
	public SigilItem(Settings settings) {
		super(settings);
	}
	
	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		ItemPlacementContext placementContext = new ItemPlacementContext(context);
		World world = context.getWorld();
		BlockPos pos = context.getBlockPos();
		if (!world.getBlockState(pos).canReplace(placementContext)) {
			pos = pos.offset(context.getSide());
		}
		PlayerEntity player = context.getPlayer();
		BlockState sigilBlock = BWObjects.SIGIL.getPlacementState(placementContext);
		if (sigilBlock != null && sigilBlock.canPlaceAt(world, pos)) {
			if (!world.isClient) {
				world.playSound(null, pos, sigilBlock.getSoundGroup().getPlaceSound(), SoundCategory.BLOCKS, 1, MathHelper.nextFloat(world.random, 0.8f, 1.2f));
				world.setBlockState(pos, sigilBlock);
				ItemStack stack = context.getStack();
				if (player instanceof ServerPlayerEntity) {
					Criteria.PLACED_BLOCK.trigger((ServerPlayerEntity) player, pos, stack);
					stack.damage(1, player, user -> user.sendToolBreakStatus(context.getHand()));
				}
			}
			return ActionResult.success(world.isClient);
		}
		return super.useOnBlock(context);
	}
	
	@Override
	public String getTranslationKey() {
		return BWObjects.SIGIL.getTranslationKey();
	}
}
