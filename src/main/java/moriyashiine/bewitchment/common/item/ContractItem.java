/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.bewitchment.common.item;

import moriyashiine.bewitchment.api.registry.Contract;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.registry.BWComponents;
import moriyashiine.bewitchment.common.registry.BWRegistries;
import moriyashiine.bewitchment.common.registry.BWSoundEvents;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.*;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@SuppressWarnings("ConstantConditions")
public class ContractItem extends Item {
	public ContractItem(Settings settings) {
		super(settings);
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		return ItemUsage.consumeHeldItem(world, user, hand);
	}

	@Override
	public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
		if (!world.isClient && stack.hasNbt() && user instanceof PlayerEntity player) {
			Contract contract = BWRegistries.CONTRACTS.get(new Identifier(stack.getOrCreateNbt().getString("Contract")));
			if (contract != null) {
				BWComponents.CONTRACTS_COMPONENT.get(player).addContract(new Contract.Instance(contract, stack.getNbt().getInt("Duration"), 0));
				world.playSound(null, player.getBlockPos(), BWSoundEvents.ITEM_CONTRACT_USE, SoundCategory.PLAYERS, 1, 1);
				if (!player.isCreative()) {
					stack.decrement(1);
				}
				return stack;
			}
		}
		return stack;
	}

	@Override
	public UseAction getUseAction(ItemStack stack) {
		return UseAction.BOW;
	}

	@Override
	public int getMaxUseTime(ItemStack stack) {
		return 32;
	}

	@Override
	public void appendStacks(ItemGroup group, DefaultedList<ItemStack> stacks) {
		if (isIn(group)) {
			BWRegistries.CONTRACTS.forEach(contract -> {
				ItemStack stack = new ItemStack(this);
				stack.getOrCreateNbt().putString("Contract", BWRegistries.CONTRACTS.getId(contract).toString());
				stack.getOrCreateNbt().putInt("Duration", 168000);
				stacks.add(stack);
			});
		}
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		if (stack.hasNbt() && stack.getOrCreateNbt().contains("Contract")) {
			tooltip.add(new TranslatableText("contract." + stack.getOrCreateNbt().getString("Contract").replace(":", ".")).formatted(Formatting.DARK_RED));
			tooltip.add(new TranslatableText(Bewitchment.MODID + ".tooltip.days", stack.getOrCreateNbt().getInt("Duration") / 24000).formatted(Formatting.DARK_RED));
		}
	}
}
