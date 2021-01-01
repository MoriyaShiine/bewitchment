package moriyashiine.bewitchment.api;

import moriyashiine.bewitchment.api.registry.AltarMapEntry;
import moriyashiine.bewitchment.common.entity.projectile.SilverArrowEntity;
import moriyashiine.bewitchment.common.registry.BWObjects;
import moriyashiine.bewitchment.common.registry.BWTags;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.EntityDamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Hand;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

public class BewitchmentAPI {
	public static Set<AltarMapEntry> ALTAR_MAP_ENTRIES = new HashSet<>();
	
	@SuppressWarnings("InstantiationOfUtilityClass")
	public static EntityGroup DEMON = new EntityGroup();
	
	public static boolean isSourceFromSilver(DamageSource source) {
		Entity attacker = source.getAttacker();
		return !(source instanceof EntityDamageSource && ((EntityDamageSource) source).isThorns()) && (attacker instanceof LivingEntity && isHoldingSilver((LivingEntity) attacker, Hand.MAIN_HAND)) || attacker instanceof SilverArrowEntity;
	}
	
	public static boolean isHoldingSilver(LivingEntity livingEntity, Hand hand) {
		return BWTags.SILVER_TOOLS.contains(livingEntity.getStackInHand(hand).getItem());
	}
	
	public static boolean isWeakToSilver(LivingEntity livingEntity) {
		return BWTags.WEAK_TO_SILVER.contains(livingEntity.getType());
	}
	
	public static int getArmorPieces(LivingEntity livingEntity, Predicate<ItemStack> predicate) {
		int amount = 0;
		for (ItemStack stack : livingEntity.getArmorItems()) {
			if (predicate.test(stack)) {
				amount++;
			}
		}
		return amount;
	}
	
	public static void addItemToInventoryAndConsume(LivingEntity entity, Hand hand, ItemStack toAdd) {
		ItemStack stack = entity.getStackInHand(hand);
		stack.decrement(entity instanceof PlayerEntity && !((PlayerEntity) entity).isCreative() ? 1 : 0);
		if (stack.isEmpty()) {
			entity.setStackInHand(hand, toAdd);
		}
		else if (!(entity instanceof PlayerEntity) || !((PlayerEntity) entity).inventory.insertStack(toAdd)) {
			entity.dropStack(toAdd);
		}
	}
	
	public static void registerAltarMapEntries(Block[]... altarArray) {
		for (int i = 0; i < DyeColor.values().length; i++) {
			Item carpet = null;
			switch (DyeColor.byId(i)) {
				case WHITE:
					carpet = Items.WHITE_CARPET;
					break;
				case ORANGE:
					carpet = Items.ORANGE_CARPET;
					break;
				case MAGENTA:
					carpet = Items.MAGENTA_CARPET;
					break;
				case LIGHT_BLUE:
					carpet = Items.LIGHT_BLUE_CARPET;
					break;
				case YELLOW:
					carpet = Items.YELLOW_CARPET;
					break;
				case LIME:
					carpet = Items.LIME_CARPET;
					break;
				case PINK:
					carpet = Items.PINK_CARPET;
					break;
				case GRAY:
					carpet = Items.GRAY_CARPET;
					break;
				case LIGHT_GRAY:
					carpet = Items.LIGHT_GRAY_CARPET;
					break;
				case CYAN:
					carpet = Items.CYAN_CARPET;
					break;
				case PURPLE:
					carpet = Items.PURPLE_CARPET;
					break;
				case BLUE:
					carpet = Items.BLUE_CARPET;
					break;
				case BROWN:
					carpet = Items.BROWN_CARPET;
					break;
				case GREEN:
					carpet = Items.GREEN_CARPET;
					break;
				case RED:
					carpet = Items.RED_CARPET;
					break;
				case BLACK:
					carpet = Items.BLACK_CARPET;
			}
			for (Block[] altars : altarArray) {
				ALTAR_MAP_ENTRIES.add(new AltarMapEntry(altars[0], altars[i + 1], carpet));
			}
		}
		for (Block[] altars : altarArray) {
			ALTAR_MAP_ENTRIES.add(new AltarMapEntry(altars[0], altars[17], BWObjects.HEDGEWITCH_CARPET.asItem()));
			ALTAR_MAP_ENTRIES.add(new AltarMapEntry(altars[0], altars[18], BWObjects.ALCHEMIST_CARPET.asItem()));
			ALTAR_MAP_ENTRIES.add(new AltarMapEntry(altars[0], altars[19], BWObjects.BESMIRCHED_CARPET.asItem()));
		}
	}
}
