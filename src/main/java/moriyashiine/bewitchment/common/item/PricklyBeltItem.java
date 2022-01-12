/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.bewitchment.common.item;

import dev.emi.trinkets.api.TrinketItem;
import moriyashiine.bewitchment.common.Bewitchment;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionUtil;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@SuppressWarnings("ConstantConditions")
public class PricklyBeltItem extends TrinketItem {
	public PricklyBeltItem(Settings settings) {
		super(settings);
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		int uses = 0;
		if (stack.hasNbt()) {
			uses = stack.getNbt().getInt("PotionUses");
		}
		tooltip.add(new TranslatableText(Bewitchment.MODID + ".tooltip.uses_left", uses).formatted(Formatting.GRAY));
		PotionUtil.buildTooltip(stack, tooltip, 1);
	}
}
