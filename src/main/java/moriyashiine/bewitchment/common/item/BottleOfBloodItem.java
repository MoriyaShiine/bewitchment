package moriyashiine.bewitchment.common.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BottleOfBloodItem extends Item {
	public BottleOfBloodItem(Settings settings) {
		super(settings);
	}
	
	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		return Items.POTION.use(world, user, hand);
	}
	
	@Override
	public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
		user.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, 200, 1));
		user.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 200, 1));
		return Items.POTION.finishUsing(stack, world, user);
	}
	
	@Override
	public UseAction getUseAction(ItemStack stack) {
		return Items.POTION.getUseAction(stack);
	}
	
	@Override
	public int getMaxUseTime(ItemStack stack) {
		return Items.POTION.getMaxUseTime(stack);
	}
	
	@Environment(EnvType.CLIENT)
	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		if (stack.hasTag() && stack.getOrCreateTag().contains("OwnerName")) {
			tooltip.add(new LiteralText(stack.getOrCreateTag().getString("OwnerName")).setStyle(Style.EMPTY.withColor(Formatting.GRAY)));
		}
	}
}
