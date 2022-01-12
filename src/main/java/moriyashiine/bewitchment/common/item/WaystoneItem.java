/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.bewitchment.common.item;

import moriyashiine.bewitchment.common.Bewitchment;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@SuppressWarnings("ConstantConditions")
public class WaystoneItem extends Item {
	public WaystoneItem(Settings settings) {
		super(settings);
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		World world = context.getWorld();
		boolean client = world.isClient;
		if (!client) {
			context.getStack().getOrCreateNbt().putLong("LocationPos", context.getBlockPos().offset(context.getSide()).asLong());
			context.getStack().getOrCreateNbt().putString("LocationWorld", world.getRegistryKey().getValue().toString());
		}
		return ActionResult.success(client);
	}

	@Override
	public boolean isEnchantable(ItemStack stack) {
		return false;
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		if (stack.hasNbt() && stack.getNbt().contains("LocationPos")) {
			BlockPos pos = BlockPos.fromLong(stack.getNbt().getLong("LocationPos"));
			tooltip.add(new TranslatableText(Bewitchment.MODID + ".tooltip.location", pos.getX(), pos.getY(), pos.getZ(), stack.getNbt().getString("LocationWorld")).formatted(Formatting.GRAY));
		}
	}
}
