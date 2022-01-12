/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.bewitchment.common.item;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.common.Bewitchment;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.PotionEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtList;
import net.minecraft.potion.PotionUtil;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@SuppressWarnings("ConstantConditions")
public class ScepterItem extends Item {
	public ScepterItem(Settings settings) {
		super(settings);
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		if (!PotionUtil.getCustomPotionEffects(user.getStackInHand(hand)).isEmpty()) {
			return ItemUsage.consumeHeldItem(world, user, hand);
		}
		return TypedActionResult.fail(user.getStackInHand(hand));
	}

	@Override
	public boolean canRepair(ItemStack stack, ItemStack ingredient) {
		return ingredient.getItem().equals(Items.NETHERITE_INGOT);
	}

	@Override
	public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
		if (user instanceof PlayerEntity player) {
			if (!world.isClient && BewitchmentAPI.drainMagic(player, 2, false)) {
				PotionEntity potion = new PotionEntity(world, user);
				List<StatusEffectInstance> effects = PotionUtil.getCustomPotionEffects(stack);
				ItemStack potionStack = PotionUtil.setCustomPotionEffects(new ItemStack(Items.SPLASH_POTION), effects);
				potionStack.getOrCreateNbt().putInt("CustomPotionColor", PotionUtil.getColor(effects));
				if (stack.getOrCreateNbt().contains("PolymorphUUID")) {
					potionStack.getOrCreateNbt().putUuid("PolymorphUUID", stack.getOrCreateNbt().getUuid("PolymorphUUID"));
					potionStack.getOrCreateNbt().putString("PolymorphName", stack.getOrCreateNbt().getString("PolymorphName"));
				}
				potion.setItem(potionStack);
				potion.setVelocity(user, user.getPitch(), user.getYaw(), -20, 0.5f, 1);
				world.spawnEntity(potion);
				world.playSound(null, user.getBlockPos(), SoundEvents.ENTITY_SPLASH_POTION_THROW, SoundCategory.PLAYERS, 1, 1);
				if (!player.isCreative()) {
					stack.getOrCreateNbt().putInt("PotionUses", stack.getOrCreateNbt().getInt("PotionUses") - 1);
					if (stack.getOrCreateNbt().getInt("PotionUses") <= 0) {
						if (stack.getOrCreateNbt().contains("PolymorphUUID")) {
							potionStack.getOrCreateNbt().remove("PolymorphUUID");
							potionStack.getOrCreateNbt().remove("PolymorphName");
						}
						stack.getOrCreateNbt().put("CustomPotionEffects", new NbtList());
					}

					stack.damage(1, user, stackUser -> stackUser.sendToolBreakStatus(user.getActiveHand()));
				}
			}
		}
		return stack;
	}

	@Override
	public UseAction getUseAction(ItemStack stack) {
		return !PotionUtil.getCustomPotionEffects(stack).isEmpty() ? UseAction.BOW : UseAction.NONE;
	}

	@Override
	public int getMaxUseTime(ItemStack stack) {
		return 16;
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
