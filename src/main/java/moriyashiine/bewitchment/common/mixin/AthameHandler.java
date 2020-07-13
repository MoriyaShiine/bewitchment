package moriyashiine.bewitchment.common.mixin;

import moriyashiine.bewitchment.common.item.tool.AthameItem;
import moriyashiine.bewitchment.common.misc.BWDataTrackers;
import moriyashiine.bewitchment.common.misc.BWUtil;
import moriyashiine.bewitchment.common.recipe.AthameDropRecipe;
import moriyashiine.bewitchment.common.registry.BWObjects;
import moriyashiine.bewitchment.common.registry.BWRecipeTypes;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.GlassBottleItem;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Recipe;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
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
	
	@Inject(method = "onKilledBy", at = @At("HEAD"))
	private void dropBlood(LivingEntity adversary, CallbackInfo callbackInfo) {
		if (BWDataTrackers.hasBlood(this)) {
			if (!world.isClient) {
				if (adversary instanceof PlayerEntity) {
					PlayerEntity playerAttacker = (PlayerEntity) adversary;
					ItemStack offhand = playerAttacker.getOffHandStack();
					if (offhand.getItem() instanceof GlassBottleItem && playerAttacker.getMainHandStack().getItem() instanceof AthameItem && playerAttacker.preferredHand == Hand.MAIN_HAND && BWDataTrackers.drainBlood(this, 25, false)) {
						BWUtil.giveStackToPlayer(playerAttacker, new ItemStack(BWObjects.bottle_of_blood));
						world.playSound(null, playerAttacker.getBlockPos(), SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.PLAYERS, 1, 1);
						offhand.decrement(1);
					}
				}
			}
		}
	}
	
	@Inject(method = "drop", at = @At("HEAD"))
	private void dropLoot(DamageSource source, CallbackInfo callbackInfo) {
		if (!world.isClient) {
			Entity attacker = source.getSource();
			if (attacker instanceof LivingEntity) {
				LivingEntity livingAttacker = (LivingEntity) attacker;
				ItemStack stack = livingAttacker.getMainHandStack();
				if (stack.getItem() instanceof AthameItem && livingAttacker.preferredHand == Hand.MAIN_HAND) {
					for (Recipe<?> recipe : world.getRecipeManager().method_30027(BWRecipeTypes.athame_drop_type)) {
						if (recipe instanceof AthameDropRecipe) {
							AthameDropRecipe athameDrop = (AthameDropRecipe) recipe;
							if (athameDrop.entity_type.equals(getType()) && world.random.nextFloat() < athameDrop.chance * (EnchantmentHelper.getLevel(Enchantments.LOOTING, stack) + 1)) {
								ItemStack drop = new ItemStack(athameDrop.drop);
								if (athameDrop.entity_type == EntityType.PLAYER) {
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
}