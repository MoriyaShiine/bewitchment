package moriyashiine.bewitchment.common.recipe;

import moriyashiine.bewitchment.common.item.PricklyBeltItem;
import moriyashiine.bewitchment.common.registry.BWRecipeTypes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.PotionUtil;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.List;

@SuppressWarnings("ConstantConditions")
public class PricklyBeltCraftingRecipe extends SpecialCraftingRecipe {
	public PricklyBeltCraftingRecipe(Identifier id) {
		super(id);
	}
	
	@Override
	public boolean matches(CraftingInventory inv, World world) {
		boolean foundPricklyBelt = false, foundPotion = false;
		int foundItems = 0;
		for (int i = 0; i < inv.size(); i++) {
			ItemStack stack = inv.getStack(i);
			if (stack.getItem() instanceof PricklyBeltItem) {
				if (!foundPricklyBelt) {
					foundPricklyBelt = true;
				}
				foundItems++;
			}
			else if (stack.getItem() == Items.POTION && (!PotionUtil.getPotionEffects(stack).isEmpty() || !PotionUtil.getCustomPotionEffects(stack).isEmpty())) {
				if (!foundPotion) {
					foundPotion = true;
				}
				foundItems++;
			}
		}
		return foundPricklyBelt && foundPotion && foundItems == 2;
	}
	
	@Override
	public ItemStack craft(CraftingInventory inv) {
		ItemStack pricklyBelt = null, potion = null;
		for (int i = 0; i < inv.size(); i++) {
			ItemStack stack = inv.getStack(i);
			if (stack.getItem() instanceof PricklyBeltItem) {
				pricklyBelt = stack.copy();
			}
			else if (stack.getItem() == Items.POTION) {
				potion = stack.copy();
			}
		}
		int uses = 1;
		List<StatusEffectInstance> effects = !PotionUtil.getCustomPotionEffects(potion).isEmpty() ? PotionUtil.getCustomPotionEffects(potion) : PotionUtil.getPotionEffects(potion);
		for (int i = 0; i < effects.size(); i++) {
			StatusEffectInstance effect = effects.get(i);
			int duration = effect.getDuration();
			int amplifier = effect.getAmplifier();
			while (duration > 600) {
				uses++;
				duration /= 2;
			}
			while (amplifier > 0) {
				uses += 2;
				amplifier--;
			}
			effects.set(i, new StatusEffectInstance(effect.getEffectType(), duration, amplifier, effect.isAmbient(), effect.shouldShowParticles(), effect.shouldShowIcon()));
		}
		if (potion.getOrCreateTag().contains("PolymorphUUID")) {
			pricklyBelt.getOrCreateTag().putUuid("PolymorphUUID", potion.getTag().getUuid("PolymorphUUID"));
			pricklyBelt.getOrCreateTag().putString("PolymorphName", potion.getTag().getString("PolymorphName"));
		}
		PotionUtil.setCustomPotionEffects(pricklyBelt, effects);
		pricklyBelt.getOrCreateTag().putInt("PotionUses", uses);
		return pricklyBelt;
	}
	
	@Override
	public boolean fits(int width, int height) {
		return true;
	}
	
	@Override
	public RecipeSerializer<?> getSerializer() {
		return BWRecipeTypes.PRICKLY_BELT_CRAFTING_SERIALIZER;
	}
}
