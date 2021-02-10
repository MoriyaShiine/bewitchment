package moriyashiine.bewitchment.common.item;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.api.interfaces.entity.BloodAccessor;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

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
		if (BewitchmentAPI.isVampire(user, true)) {
			((BloodAccessor) user).fillBlood(20, false);
		}
		else {
			user.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, 200));
			user.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 200));
		}
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
}
