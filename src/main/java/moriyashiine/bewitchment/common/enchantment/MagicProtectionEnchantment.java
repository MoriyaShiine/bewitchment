/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.enchantment;

import net.minecraft.enchantment.ProtectionEnchantment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.registry.tag.DamageTypeTags;

public class MagicProtectionEnchantment extends ProtectionEnchantment {
	public MagicProtectionEnchantment(Rarity weight, Type type, EquipmentSlot... equipmentSlots) {
		super(weight, type, equipmentSlots);
	}

	@Override
	public int getProtectionAmount(int level, DamageSource source) {
		return source.isIn(DamageTypeTags.WITCH_RESISTANT_TO) ? level * 2 : 0;
	}
}
