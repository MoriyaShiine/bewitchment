package moriyashiine.bewitchment.common.enchantment;

import net.minecraft.enchantment.ProtectionEnchantment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.damage.DamageSource;

public class MagicProtectionEnchantment extends ProtectionEnchantment {
	public MagicProtectionEnchantment(Rarity weight, Type type, EquipmentSlot... equipmentSlots) {
		super(weight, type, equipmentSlots);
	}
	
	@Override
	public int getProtectionAmount(int level, DamageSource source) {
		return source.isMagic() ? level * 2 : 0;
	}
}
