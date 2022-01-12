/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.bewitchment.common.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@SuppressWarnings("ConstantConditions")
public class ChalkItem extends Item {
	private final Block glyph;

	public ChalkItem(Settings settings, Block glyph) {
		super(settings);
		this.glyph = glyph;
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		World world = context.getWorld();
		BlockPos pos = context.getBlockPos();
		boolean client = world.isClient;
		ItemPlacementContext placementContext = new ItemPlacementContext(context);
		if (!world.getBlockState(pos).canReplace(placementContext)) {
			pos = pos.offset(context.getSide());
		}
		if (!world.getBlockState(pos).canReplace(placementContext)) {
			return ActionResult.PASS;
		}
		BlockState state = glyph.getPlacementState(placementContext);
		if (state.canPlaceAt(world, pos)) {
			if (!client) {
				PlayerEntity player = context.getPlayer();
				ItemStack stack = context.getStack();
				world.playSound(null, pos, state.getSoundGroup().getPlaceSound(), SoundCategory.BLOCKS, 1, MathHelper.nextFloat(world.random, 0.8f, 1.2f));
				world.setBlockState(pos, state);
				if (player instanceof ServerPlayerEntity serverPlayer) {
					Criteria.PLACED_BLOCK.trigger(serverPlayer, pos, stack);
					stack.damage(1, player, stackUser -> stackUser.sendToolBreakStatus(context.getHand()));
				}
			}
			return ActionResult.success(client);
		}
		return super.useOnBlock(context);
	}

	@Override
	public boolean isEnchantable(ItemStack stack) {
		return false;
	}

	@Override
	public String getTranslationKey() {
		return glyph.getTranslationKey();
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		if (stack.hasNbt() && stack.getOrCreateNbt().contains("InnerCircle")) {
			tooltip.add(new TranslatableText("bewitchment.tooltip.inner_circle", new TranslatableText(stack.getOrCreateNbt().getString("InnerCircle"))).formatted(Formatting.GRAY));
			if (stack.getOrCreateNbt().contains("OuterCircle")) {
				tooltip.add(new TranslatableText("bewitchment.tooltip.outer_circle", new TranslatableText(stack.getOrCreateNbt().getString("OuterCircle"))).formatted(Formatting.GRAY));
			}
			tooltip.add(new TranslatableText("bewitchment.tooltip.cost", stack.getOrCreateNbt().getInt("Cost")).formatted(Formatting.GRAY));
			if (stack.getOrCreateNbt().contains("RunningTime")) {
				tooltip.add(new TranslatableText("bewitchment.tooltip.running_time", stack.getOrCreateNbt().getInt("RunningTime") / 20f).formatted(Formatting.GRAY));
			}
		}
	}
}
