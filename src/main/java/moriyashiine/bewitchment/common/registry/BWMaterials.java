package moriyashiine.bewitchment.common.registry;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ArmorMaterials;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.ToolMaterials;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvent;

public class BWMaterials {
	public static final ArmorMaterial SILVER_ARMOR = new ArmorMaterial() {
		@Override
		public int getDurability(EquipmentSlot slot) {
			return ArmorMaterials.IRON.getDurability(slot);
		}
		
		@Override
		public int getProtectionAmount(EquipmentSlot slot) {
			return ArmorMaterials.IRON.getProtectionAmount(slot);
		}
		
		@Override
		public int getEnchantability() {
			return ArmorMaterials.GOLD.getEnchantability();
		}
		
		@Override
		public SoundEvent getEquipSound() {
			return ArmorMaterials.GOLD.getEquipSound();
		}
		
		@Override
		public Ingredient getRepairIngredient() {
			return Ingredient.ofItems(BWObjects.SILVER_INGOT);
		}
		
		@Override
		public String getName() {
			return "silver";
		}
		
		@Override
		public float getToughness() {
			return ArmorMaterials.IRON.getToughness();
		}
		
		@Override
		public float getKnockbackResistance() {
			return ArmorMaterials.IRON.getKnockbackResistance();
		}
	};
	
	public static final ToolMaterial SILVER_TOOL = new ToolMaterial() {
		@Override
		public int getDurability() {
			return ToolMaterials.IRON.getDurability();
		}
		
		@Override
		public float getMiningSpeedMultiplier() {
			return ToolMaterials.GOLD.getMiningSpeedMultiplier();
		}
		
		@Override
		public float getAttackDamage() {
			return ToolMaterials.IRON.getAttackDamage();
		}
		
		@Override
		public int getMiningLevel() {
			return ToolMaterials.IRON.getMiningLevel();
		}
		
		@Override
		public int getEnchantability() {
			return ToolMaterials.GOLD.getEnchantability();
		}
		
		@Override
		public Ingredient getRepairIngredient() {
			return Ingredient.ofItems(BWObjects.SILVER_INGOT);
		}
	};
}
