package moriyashiine.bewitchment.common.item;

import moriyashiine.bewitchment.api.interfaces.entity.ContractAccessor;
import moriyashiine.bewitchment.api.registry.Contract;
import moriyashiine.bewitchment.common.Bewitchment;
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
		if (!world.isClient && stack.hasTag() && user instanceof PlayerEntity) {
			Contract contract = BWRegistries.CONTRACTS.get(new Identifier(stack.getOrCreateTag().getString("Contract")));
			if (contract != null) {
				((ContractAccessor) user).addContract(new Contract.Instance(contract, stack.getTag().getInt("Duration"), 0));
				world.playSound(null, user.getBlockPos(), BWSoundEvents.ITEM_CONTRACT_USE, SoundCategory.PLAYERS, 1, 1);
				if (!((PlayerEntity) user).isCreative()) {
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
				stack.getOrCreateTag().putString("Contract", BWRegistries.CONTRACTS.getId(contract).toString());
				stack.getOrCreateTag().putInt("Duration", 168000);
				stacks.add(stack);
			});
		}
	}
	
	@Environment(EnvType.CLIENT)
	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		if (stack.hasTag() && stack.getOrCreateTag().contains("Contract")) {
			tooltip.add(new TranslatableText("contract." + stack.getOrCreateTag().getString("Contract").replace(":", ".")).formatted(Formatting.DARK_RED));
			tooltip.add(new TranslatableText(Bewitchment.MODID + ".tooltip.days", stack.getOrCreateTag().getInt("Duration") / 24000).formatted(Formatting.DARK_RED));
		}
	}
}
