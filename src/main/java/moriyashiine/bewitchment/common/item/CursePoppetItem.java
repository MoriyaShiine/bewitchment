package moriyashiine.bewitchment.common.item;

import moriyashiine.bewitchment.api.interfaces.CurseAccessor;
import moriyashiine.bewitchment.api.item.PoppetItem;
import moriyashiine.bewitchment.api.registry.Curse;
import moriyashiine.bewitchment.common.Bewitchment;
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
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.*;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class CursePoppetItem extends PoppetItem {
	public CursePoppetItem(Settings settings, boolean worksInShelf) {
		super(settings, worksInShelf);
	}
	
	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		return ItemUsage.consumeHeldItem(world, user, hand);
	}
	
	@Override
	public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
		if (!world.isClient && stack.hasTag()) {
			MinecraftServer server = world.getServer();
			if (server != null && stack.getOrCreateTag().contains("Cursed")) {
				UUID uuid = stack.getOrCreateTag().getUuid("OwnerUUID");
				for (ServerWorld serverWorld : server.getWorlds()) {
					Entity entity = serverWorld.getEntity(uuid);
					if (entity != null) {
						Curse curse = BWRegistries.CURSES.get(new Identifier(stack.getOrCreateTag().getString("Curse")));
						if (curse != null) {
							CurseAccessor.of(entity).ifPresent(curseAccessor -> {
								curseAccessor.addCurse(new Curse.Instance(curse, 168000));
								world.playSound(null, user.getBlockPos(), BWSoundEvents.ENTITY_GENERIC_CURSE, SoundCategory.PLAYERS, 1, 1);
								if (!(user instanceof PlayerEntity && ((PlayerEntity) user).isCreative())) {
									stack.decrement(1);
								}
							});
							return stack;
						}
					}
				}
				if (user instanceof PlayerEntity) {
					((PlayerEntity) user).sendMessage(new TranslatableText(Bewitchment.MODID + ".invalid_entity", stack.getOrCreateTag().getString("OwnerName")), true);
				}
			}
		}
		return stack;
	}
	
	@Override
	public UseAction getUseAction(ItemStack stack) {
		return stack.hasTag() && stack.getOrCreateTag().getBoolean("Cursed") ? UseAction.BOW : super.getUseAction(stack);
	}
	
	@Override
	public int getMaxUseTime(ItemStack stack) {
		return 32;
	}
	
	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		super.appendTooltip(stack, world, tooltip, context);
		if (stack.hasTag() && stack.getOrCreateTag().contains("Curse")) {
			tooltip.add(new TranslatableText("curse." + stack.getOrCreateTag().getString("Curse").replace(":", ".")).setStyle(Style.EMPTY.withColor(Formatting.DARK_RED)));
		}
	}
}
