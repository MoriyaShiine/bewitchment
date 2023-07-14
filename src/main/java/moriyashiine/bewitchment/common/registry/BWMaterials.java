/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.registry;

import net.minecraft.item.*;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvent;

public class BWMaterials {
	public static final ArmorMaterial HEDGEWITCH_ARMOR = new ArmorMaterial() {
		@Override
		public int getDurability(ArmorItem.Type type) {
			return ArmorMaterials.CHAIN.getDurability(type);
		}

		@Override
		public int getProtection(ArmorItem.Type type) {
			return ArmorMaterials.CHAIN.getProtection(type);
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
		public int getDurability(ArmorItem.Type type) {
			return HEDGEWITCH_ARMOR.getDurability(type);
		}

		@Override
		public int getProtection(ArmorItem.Type type) {
			return HEDGEWITCH_ARMOR.getProtection(type);
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
		public int getDurability(ArmorItem.Type type) {
			return HEDGEWITCH_ARMOR.getDurability(type);
		}

		@Override
		public int getProtection(ArmorItem.Type type) {
			return HEDGEWITCH_ARMOR.getProtection(type);
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
		public int getDurability(ArmorItem.Type type) {
			return HEDGEWITCH_ARMOR.getDurability(type);
		}

		@Override
		public int getProtection(ArmorItem.Type type) {
			return HEDGEWITCH_ARMOR.getProtection(type);
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
