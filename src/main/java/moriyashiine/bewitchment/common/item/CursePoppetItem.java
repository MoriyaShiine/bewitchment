/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.bewitchment.common.item;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.api.item.PoppetItem;
import moriyashiine.bewitchment.api.misc.PoppetData;
import moriyashiine.bewitchment.api.registry.Curse;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.registry.BWComponents;
import moriyashiine.bewitchment.common.registry.BWObjects;
import moriyashiine.bewitchment.common.registry.BWRegistries;
import moriyashiine.bewitchment.common.registry.BWSoundEvents;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.*;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

@SuppressWarnings("ConstantConditions")
public class CursePoppetItem extends PoppetItem {
	public CursePoppetItem(Settings settings, boolean worksInShelf) {
		super(settings, worksInShelf);
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		ItemStack stack = user.getStackInHand(hand);
		return stack.hasNbt() && stack.getOrCreateNbt().getBoolean("Cursed") && stack.getOrCreateNbt().contains("OwnerUUID") ? ItemUsage.consumeHeldItem(world, user, hand) : super.use(world, user, hand);
	}

	@Override
	public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
		if (!world.isClient && stack.hasNbt()) {
			MinecraftServer server = world.getServer();
			if (server != null && stack.getOrCreateNbt().contains("Cursed")) {
				UUID uuid = TaglockItem.getTaglockUUID(stack);
				for (ServerWorld serverWorld : server.getWorlds()) {
					Entity entity = serverWorld.getEntity(uuid);
					if (entity instanceof LivingEntity livingEntity) {
						boolean failed = false;
						Curse curse = BWRegistries.CURSES.get(new Identifier(stack.getOrCreateNbt().getString("Curse")));
						PoppetData poppetData = BewitchmentAPI.getPoppet(world, BWObjects.CURSE_POPPET, entity);
						if (!poppetData.stack.isEmpty() && poppetData.stack.hasNbt() && !poppetData.stack.getNbt().getBoolean("Cursed")) {
							poppetData.stack.getNbt().putString("Curse", BWRegistries.CURSES.getId(curse).toString());
							poppetData.stack.getNbt().putBoolean("Cursed", true);
							TaglockItem.removeTaglock(poppetData.stack);
							poppetData.update(world, true);
							failed = true;
						}
						if (curse != null) {
							if (!failed) {
								BWComponents.CURSES_COMPONENT.get(livingEntity).addCurse(new Curse.Instance(curse, 168000));
							}
							world.playSound(null, user.getBlockPos(), BWSoundEvents.ENTITY_GENERIC_CURSE, SoundCategory.PLAYERS, 1, 1);
							if (!(user instanceof PlayerEntity && ((PlayerEntity) user).isCreative())) {
								stack.decrement(1);
							}
							return stack;
						}
					}
				}
				if (user instanceof PlayerEntity player) {
					player.sendMessage(new TranslatableText(Bewitchment.MODID + ".message.invalid_entity", stack.getOrCreateNbt().getString("OwnerName")), true);
				}
			}
		}
		return stack;
	}

	@Override
	public UseAction getUseAction(ItemStack stack) {
		return stack.hasNbt() && stack.getOrCreateNbt().getBoolean("Cursed") && stack.getOrCreateNbt().contains("OwnerUUID") ? UseAction.BOW : super.getUseAction(stack);
	}

	@Override
	public int getMaxUseTime(ItemStack stack) {
		return 32;
	}

	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		super.appendTooltip(stack, world, tooltip, context);
		if (stack.hasNbt() && stack.getOrCreateNbt().contains("Curse")) {
			tooltip.add(new TranslatableText("curse." + stack.getOrCreateNbt().getString("Curse").replace(":", ".")).formatted(Formatting.DARK_RED));
		}
	}
}
