package moriyashiine.bewitchment.common.registry;

import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.enchantment.MagicProtectionEnchantment;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.ProtectionEnchantment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.LinkedHashMap;
import java.util.Map;

public class BWEnchantments {
	private static final Map<Enchantment, Identifier> ENCHANTMENTS = new LinkedHashMap<>();
	
	public static final Enchantment MAGIC_PROTECTION = create("magic_protection", new MagicProtectionEnchantment(Enchantment.Rarity.UNCOMMON, ProtectionEnchantment.Type.FIRE, EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET));
	
	private static <T extends Enchantment> T create(String name, T enchantment) {
		ENCHANTMENTS.put(enchantment, new Identifier(Bewitchment.MODID, name));
		return enchantment;
	}
	
	public static void init() {
		ENCHANTMENTS.keySet().forEach(particleType -> Registry.register(Registry.ENCHANTMENT, ENCHANTMENTS.get(particleType), particleType));
	}
}
