/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.registry;

import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.enchantment.MagicProtectionEnchantment;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.ProtectionEnchantment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class BWEnchantments {
	public static final Enchantment MAGIC_PROTECTION = new MagicProtectionEnchantment(Enchantment.Rarity.UNCOMMON, ProtectionEnchantment.Type.FIRE, EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET);

	public static void init() {
		Registry.register(Registries.ENCHANTMENT, Bewitchment.id("magic_protection"), MAGIC_PROTECTION);
	}
}
