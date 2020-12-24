package moriyashiine.bewitchment.mixin;

import moriyashiine.bewitchment.api.registry.AthameDropRecipe;
import moriyashiine.bewitchment.common.item.tool.AthameItem;
import moriyashiine.bewitchment.common.registry.BWRecipeTypes;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class AthameHandler extends Entity {
	public AthameHandler(EntityType<?> type, World world) {
		super(type, world);
	}
	
	@Inject(method = "drop", at = @At("HEAD"))
	private void dropLoot(DamageSource source, CallbackInfo callbackInfo) {
		if (!world.isClient) {
			Entity attacker = source.getSource();
			if (attacker instanceof LivingEntity) {
				LivingEntity livingAttacker = (LivingEntity) attacker;
				ItemStack stack = livingAttacker.getMainHandStack();
				if (stack.getItem() instanceof AthameItem && livingAttacker.preferredHand == Hand.MAIN_HAND) {
					for (AthameDropRecipe recipe : world.getRecipeManager().listAllOfType(BWRecipeTypes.ATHAME_DROP_RECIPE_TYPE)) {
						if (recipe.entity_type.equals(getType()) && world.random.nextFloat() < recipe.chance * (EnchantmentHelper.getLevel(Enchantments.LOOTING, stack) + 1)) {
							ItemStack drop = recipe.getOutput().copy();
							if (recipe.entity_type == EntityType.PLAYER) {
								drop.getOrCreateTag().putString("SkullOwner", getName().getString());
							}
							world.spawnEntity(new ItemEntity(world, getX() + 0.5, getY() + 0.5, getZ() + 0.5, drop));
						}
					}
				}
			}
		}
	}
}
