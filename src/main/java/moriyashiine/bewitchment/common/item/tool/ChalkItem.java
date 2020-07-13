package moriyashiine.bewitchment.common.item.tool;

import moriyashiine.bewitchment.common.block.ChalkBlock;
import moriyashiine.bewitchment.common.registry.BWObjects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.List;
import java.util.Objects;

public class ChalkItem extends Item {
	private final ChalkBlock chalk_block;
	
	public ChalkItem(ChalkBlock chalk_block, Settings properties) {
		super(properties);
		this.chalk_block = chalk_block;
		this.chalk_block.chalk_item = this;
	}
	
	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		ItemPlacementContext itemPlacementContext = new ItemPlacementContext(context);
		World world = context.getWorld();
		BlockPos pos = context.getBlockPos();
		if (!world.getBlockState(pos).canReplace(itemPlacementContext)) {
			pos = pos.offset(context.getSide());
		}
		PlayerEntity player = context.getPlayer();
		BlockState chalk = chalk_block.getPlacementState(itemPlacementContext);
		if (chalk != null && chalk.canPlaceAt(world, pos)) {
			world.playSound(null, pos, chalk.getSoundGroup().getPlaceSound(), SoundCategory.BLOCKS, 1, MathHelper.nextFloat(world.random, 0.8f, 1.2f));
			world.setBlockState(pos, chalk);
			ItemStack stack = context.getStack();
			if (player instanceof ServerPlayerEntity) {
				Criteria.PLACED_BLOCK.trigger((ServerPlayerEntity) player, pos, stack);
				stack.damage(1, player, user -> user.sendToolBreakStatus(context.getHand()));
			}
			return ActionResult.success(world.isClient);
		}
		return super.useOnBlock(context);
	}
	
	@Override
	public boolean isEnchantable(ItemStack stack) {
		return false;
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
		if (this == BWObjects.focal_chalk && stack.hasTag()) {
			CompoundTag tag = stack.getTag();
			if (Objects.requireNonNull(tag).contains("inner_circle")) {
				String inner_circle = tag.getString("inner_circle");
				String middle_circle = tag.getString("middle_circle");
				if (middle_circle.isEmpty()) {
					middle_circle = "none";
				}
				String outer_circle = tag.getString("outer_circle");
				if (outer_circle.isEmpty()) {
					outer_circle = "none";
				}
				int cost = tag.getInt("cost");
				int time = tag.getInt("time");
				tooltip.add(new TranslatableText("jei.ritual.inner_circle", inner_circle));
				tooltip.add(new TranslatableText("jei.ritual.middle_circle", middle_circle));
				tooltip.add(new TranslatableText("jei.ritual.outer_circle", outer_circle));
				tooltip.add(new TranslatableText("jei.ritual.cost", cost));
				tooltip.add(new TranslatableText("jei.ritual.time", time / 20f));
			}
		}
	}
}