package moriyashiine.bewitchment.common.item;

import dev.emi.trinkets.api.SlotGroups;
import dev.emi.trinkets.api.Slots;
import dev.emi.trinkets.api.TrinketItem;
import moriyashiine.bewitchment.common.Bewitchment;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PricklyBeltItem extends TrinketItem {
	public PricklyBeltItem(Settings settings) {
		super(settings);
	}
	
	@Override
	public boolean canWearInSlot(String group, String slot) {
		return group.equals(SlotGroups.LEGS) && slot.equals(Slots.BELT);
	}
	
	@Environment(EnvType.CLIENT)
	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		tooltip.add(new TranslatableText(Bewitchment.MODID + ".tooltip.uses_left", stack.getOrCreateTag().getInt("PotionUses")).formatted(Formatting.GRAY));
		Items.POTION.appendTooltip(stack, world, tooltip, context);
	}
}
