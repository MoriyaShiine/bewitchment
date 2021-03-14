package moriyashiine.bewitchment.mixin;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.common.block.SaltLineBlock;
import moriyashiine.bewitchment.common.misc.BWUtil;
import moriyashiine.bewitchment.common.registry.BWMaterials;
import moriyashiine.bewitchment.common.registry.BWObjects;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(Block.class)
public abstract class BlockMixin {
	@Inject(method = "getDroppedStacks(Lnet/minecraft/block/BlockState;Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/entity/BlockEntity;Lnet/minecraft/entity/Entity;Lnet/minecraft/item/ItemStack;)Ljava/util/List;", at = @At("RETURN"), cancellable = true)
	private static void getDroppedStacks(BlockState state, ServerWorld world, BlockPos pos, @Nullable BlockEntity blockEntity, @Nullable Entity entity, ItemStack stack, CallbackInfoReturnable<List<ItemStack>> callbackInfo) {
		if (entity instanceof LivingEntity) {
			boolean damage = false;
			if (state.getBlock() instanceof SaltLineBlock && BewitchmentAPI.isWeakToSilver((LivingEntity) entity)) {
				damage = true;
			}
			else if (state.getBlock() == BWObjects.GARLIC_CROP && BewitchmentAPI.isVampire(entity, true)) {
				damage = true;
			}
			else if (state.getBlock() == BWObjects.ACONITE_CROP && BewitchmentAPI.isWerewolf(entity, true)) {
				damage = true;
			}
			if (damage) {
				entity.damage(DamageSource.MAGIC, ((LivingEntity) entity).getMaxHealth() * 1 / 2f);
			}
			List<ItemStack> drops = callbackInfo.getReturnValue();
			if (!drops.isEmpty() && !EnchantmentHelper.get(stack).containsKey(Enchantments.SILK_TOUCH)) {
				if (state.getBlock() instanceof CropBlock && state.get(((CropBlock) state.getBlock()).getAgeProperty()) == ((CropBlock) state.getBlock()).getMaxAge() && BWUtil.getArmorPieces((LivingEntity) entity, armorStack -> armorStack.getItem() instanceof ArmorItem && ((ArmorItem) armorStack.getItem()).getMaterial() == BWMaterials.HEDGEWITCH_ARMOR) >= 3) {
					for (int i = 0; i < drops.size(); i++) {
						drops.set(i, new ItemStack(drops.get(i).getItem(), drops.get(i).getCount() + 1));
					}
				}
			}
		}
	}
}
