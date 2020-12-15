package moriyashiine.bewitchment.api;

import moriyashiine.bewitchment.api.registry.AthameDropEntry;
import moriyashiine.bewitchment.common.entity.SilverArrowEntity;
import moriyashiine.bewitchment.common.registry.BWTags;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.EntityDamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

public class BewitchmentAPI {
	public static Set<AthameDropEntry> ATHAME_DROP_ENTRIES = new HashSet<>();
	
	public static boolean isSourceFromSilver(DamageSource source) {
		Entity attacker = source.getAttacker();
		return !(source instanceof EntityDamageSource && ((EntityDamageSource) source).isThorns()) && (attacker instanceof LivingEntity && BewitchmentAPI.isHoldingSilver((LivingEntity) attacker, Hand.MAIN_HAND)) || attacker instanceof SilverArrowEntity;
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
}
