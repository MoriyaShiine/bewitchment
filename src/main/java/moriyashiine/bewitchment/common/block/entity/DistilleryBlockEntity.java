package moriyashiine.bewitchment.common.block.entity;

import moriyashiine.bewitchment.common.block.BWProperties;
import moriyashiine.bewitchment.common.block.entity.util.BWCraftingBlockEntity;
import moriyashiine.bewitchment.common.recipe.DistillingRecipe;
import moriyashiine.bewitchment.common.registry.BWBlockEntityTypes;
import moriyashiine.bewitchment.common.registry.BWObjects;
import moriyashiine.bewitchment.common.registry.BWRecipeTypes;
import moriyashiine.bewitchment.common.screenhandler.DistilleryScreenHandler;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Lazy;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DistilleryBlockEntity extends BWCraftingBlockEntity {
	private static final Text DEFAULT_NAME = new TranslatableText(BWObjects.distillery.getTranslationKey());
	
	private Lazy<DistillingRecipe> lazyRecipe = new Lazy<>(() -> null);
	
	public DistilleryBlockEntity() {
		super(BWBlockEntityTypes.distillery);
	}
	
	@Override
	protected void fromTagAdditional(CompoundTag tag)
	{
		lazyRecipe = new Lazy<>(() -> Objects.requireNonNull(world).getRecipeManager().listAllOfType(BWRecipeTypes.distilling_type).stream().filter(recipe -> recipe.getId().toString().equals(tag.getString("Recipe"))).findFirst().orElse(null));
		super.fromTagAdditional(tag);
	}
	
	@Override
	protected CompoundTag toTagAdditional(CompoundTag tag)
	{
		DistillingRecipe recipe = getRecipe();
		if (recipe != null) {
			tag.putString("Recipe", recipe.getId().toString());
		}
		return super.toTagAdditional(tag);
	}
	
	@Override
	public void setStack(int slot, ItemStack stack) {
		super.setStack(slot, stack);
		if (world != null) {
			if (slot == 4) {
				world.setBlockState(pos, getCachedState().with(BWProperties.FILLED, !getStack(slot).isEmpty()));
			}
			DistillingRecipe actualRecipe = null;
			for (Recipe<?> recipe : world.getRecipeManager().listAllOfType(BWRecipeTypes.distilling_type)) {
				if (recipe instanceof DistillingRecipe) {
					DistillingRecipe foundRecipe = (DistillingRecipe) recipe;
					List<ItemStack> items = new ArrayList<>();
					for (int i = 0; i < INPUT_SLOTS.length; i++) {
						ItemStack stackInSlot = getStack(i);
						if (!stackInSlot.isEmpty()) {
							items.add(stackInSlot);
						}
					}
					if (items.size() == foundRecipe.input.size()) {
						for (Ingredient ingredient : foundRecipe.input) {
							for (int i = items.size() - 1; i >= 0; i--) {
								if (ingredient.test(items.get(i))) {
									items.remove(i);
								}
							}
						}
						if (items.isEmpty()) {
							actualRecipe = foundRecipe;
							break;
						}
					}
				}
			}
			setRecipe(actualRecipe);
			markDirty();
		}
	}
	
	@Override
	protected Text getContainerName() {
		return DEFAULT_NAME;
	}
	
	@Override
	public void tick() {
		if (world != null) {
			DistillingRecipe recipe = getRecipe();
			//todo: drain
			if (recipe != null && canAcceptRecipeOutput()) {
				recipeTime++;
				if (recipeTime >= 200) {
					recipeTime = 0;
					if (!world.isClient) {
						world.playSound(null, pos, SoundEvents.BLOCK_BREWING_STAND_BREW, SoundCategory.BLOCKS, 1, 1);
						for (int i : INPUT_SLOTS) {
							removeStack(i, 1);
						}
						ItemStack output = getStack(4);
						if (output.isEmpty()) {
							output = recipe.output.copy();
						}
						else {
							output.increment(recipe.output.getCount());
						}
						setStack(4, output);
					}
				}
			}
			else if (recipeTime != 0) {
				recipeTime = 0;
				if (!world.isClient) {
					markDirty();
				}
			}
		}
	}
	
	@Override
	protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
		return new DistilleryScreenHandler(syncId, playerInventory, pos);
	}
	
	public DistillingRecipe getRecipe() {
		return lazyRecipe.get();
	}
	
	public void setRecipe(DistillingRecipe recipe) {
		lazyRecipe = new Lazy<>(() -> recipe);
	}
	
	private boolean canAcceptRecipeOutput() {
		ItemStack recipeOutput = getRecipe().output;
		ItemStack inventoryOutput = inventory.get(4);
		if (inventoryOutput.isEmpty()) {
			return true;
		}
		else if (!inventoryOutput.isItemEqualIgnoreDamage(recipeOutput)) {
			return false;
		}
		int count = inventoryOutput.getCount() + recipeOutput.getCount();
		return count <= recipeOutput.getMaxCount() && count <= getMaxCountPerStack();
	}
}