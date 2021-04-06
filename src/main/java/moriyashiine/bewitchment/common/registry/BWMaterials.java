package moriyashiine.bewitchment.common.registry;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ArmorMaterials;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.ToolMaterials;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvent;

public class BWMaterials {
	public static final ArmorMaterial HEDGEWITCH_ARMOR = new ArmorMaterial() {
		@Override
		public int getDurability(EquipmentSlot slot) {
			return ArmorMaterials.CHAIN.getDurability(slot);
		}
		
		@Override
		public int getProtectionAmount(EquipmentSlot slot) {
			return ArmorMaterials.CHAIN.getProtectionAmount(slot);
		}
		
		@Override
		public int getEnchantability() {
			return ArmorMaterials.GOLD.getEnchantability();
		}
		
		@Override
		public SoundEvent getEquipSound() {
			return ArmorMaterials.LEATHER.getEquipSound();
		}
		
		@Override
		public Ingredient getRepairIngredient() {
			return Ingredient.ofItems(BWObjects.HEDGEWITCH_WOOL);
		}
		
		@Override
		public String getName() {
			return "hedgewitch";
		}
		
		@Override
		public float getToughness() {
			return ArmorMaterials.CHAIN.getToughness();
		}
		
		@Override
		public float getKnockbackResistance() {
			return ArmorMaterials.CHAIN.getKnockbackResistance();
		}
	};
	
	public static final ArmorMaterial ALCHEMIST_ARMOR = new ArmorMaterial() {
		@Override
		public int getDurability(EquipmentSlot slot) {
			return HEDGEWITCH_ARMOR.getDurability(slot);
		}
		
		@Override
		public int getProtectionAmount(EquipmentSlot slot) {
			return HEDGEWITCH_ARMOR.getProtectionAmount(slot);
		}
		
		@Override
		public int getEnchantability() {
			return HEDGEWITCH_ARMOR.getEnchantability();
		}
		
		@Override
		public SoundEvent getEquipSound() {
			return HEDGEWITCH_ARMOR.getEquipSound();
		}
		
		@Override
		public Ingredient getRepairIngredient() {
			return Ingredient.ofItems(BWObjects.ALCHEMIST_WOOL);
		}
		
		@Override
		public String getName() {
			return "alchemist";
		}
		
		@Override
		public float getToughness() {
			return HEDGEWITCH_ARMOR.getToughness();
		}
		
		@Override
		public float getKnockbackResistance() {
			return HEDGEWITCH_ARMOR.getKnockbackResistance();
		}
	};
	
	public static final ArmorMaterial BESMIRCHED_ARMOR = new ArmorMaterial() {
		@Override
		public int getDurability(EquipmentSlot slot) {
			return HEDGEWITCH_ARMOR.getDurability(slot);
		}
		
		@Override
		public int getProtectionAmount(EquipmentSlot slot) {
			return HEDGEWITCH_ARMOR.getProtectionAmount(slot);
		}
		
		@Override
		public int getEnchantability() {
			return HEDGEWITCH_ARMOR.getEnchantability();
		}
		
		@Override
		public SoundEvent getEquipSound() {
			return HEDGEWITCH_ARMOR.getEquipSound();
		}
		
		@Override
		public Ingredient getRepairIngredient() {
			return Ingredient.ofItems(BWObjects.BESMIRCHED_WOOL);
		}
		
		@Override
		public String getName() {
			return "besmirched";
		}
		
		@Override
		public float getToughness() {
			return HEDGEWITCH_ARMOR.getToughness();
		}
		
		@Override
		public float getKnockbackResistance() {
			return HEDGEWITCH_ARMOR.getKnockbackResistance();
		}
	};
	
	public static final ArmorMaterial HARBINGER_ARMOR = new ArmorMaterial() {
		@Override
		public int getDurability(EquipmentSlot slot) {
			return ArmorMaterials.NETHERITE.getDurability(slot);
		}
		
		@Override
		public int getProtectionAmount(EquipmentSlot slot) {
			return ArmorMaterials.NETHERITE.getProtectionAmount(slot);
		}
		
		@Override
		public int getEnchantability() {
			return HEDGEWITCH_ARMOR.getEnchantability();
		}
		
		@Override
		public SoundEvent getEquipSound() {
			return HEDGEWITCH_ARMOR.getEquipSound();
		}
		
		@Override
		public Ingredient getRepairIngredient() {
			return ArmorMaterials.NETHERITE.getRepairIngredient();
		}
		
		@Override
		public String getName() {
			return "harbinger";
		}
		
		@Override
		public float getToughness() {
			return ArmorMaterials.NETHERITE.getToughness();
		}
		
		@Override
		public float getKnockbackResistance() {
			return ArmorMaterials.NETHERITE.getKnockbackResistance();
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
