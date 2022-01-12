/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.bewitchment.common.item;

import moriyashiine.bewitchment.api.item.BroomItem;
import moriyashiine.bewitchment.common.Bewitchment;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@SuppressWarnings("ConstantConditions")
public class DragonsBloodBroomItem extends BroomItem {
	public DragonsBloodBroomItem(Settings settings, EntityType<?> broom) {
		super(settings, broom);
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		if (stack.hasNbt() && stack.getNbt().contains("Sigil")) {
			tooltip.add(new TranslatableText("sigil." + stack.getNbt().getString("Sigil").replace(":", ".")).formatted(Formatting.GRAY));
			tooltip.add(new TranslatableText(Bewitchment.MODID + ".tooltip.uses_left", stack.getNbt().getInt("Uses")).formatted(Formatting.GRAY));
		}
	}
}
