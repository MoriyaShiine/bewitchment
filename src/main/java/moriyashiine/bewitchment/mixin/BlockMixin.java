package moriyashiine.bewitchment.mixin;

import moriyashiine.bewitchment.api.interfaces.ContractAccessor;
import moriyashiine.bewitchment.common.registry.BWContracts;
import moriyashiine.bewitchment.common.registry.BWTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.explosion.Explosion;
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
		if (!EnchantmentHelper.get(stack).containsKey(Enchantments.SILK_TOUCH) && entity instanceof LivingEntity) {
			ContractAccessor.of((LivingEntity) entity).ifPresent(contractAccessor -> {
				List<ItemStack> drops = callbackInfo.getReturnValue();
				if (contractAccessor.hasContract(BWContracts.GREED)) {
					if (world.random.nextFloat() < 1 / 8f && contractAccessor.hasNegativeEffects()) {
						drops.clear();
					}
					else {
						for (int i = 0; i < drops.size(); i++) {
							if (BWTags.ORES.contains(state.getBlock().asItem())) {
								drops.set(i, new ItemStack(drops.get(i).getItem(), drops.get(i).getCount() * 2));
							}
						}
					}
				}
				if (contractAccessor.hasContract(BWContracts.PRIDE)) {
					world.getRecipeManager().listAllOfType(RecipeType.SMELTING).forEach(smeltingRecipe -> {
						for (int i = 0; i < drops.size(); i++) {
							if (BWTags.ORES.contains(drops.get(i).getItem())) {
								for (Ingredient ingredient : smeltingRecipe.getPreviewInputs()) {
									if (ingredient.test(drops.get(i))) {
										drops.set(i, new ItemStack(smeltingRecipe.getOutput().getItem(), smeltingRecipe.getOutput().getCount() * drops.get(i).getCount()));
									}
								}
							}
						}
					});
					if (contractAccessor.hasNegativeEffects() && world.random.nextFloat() < 1 / 16f) {
						world.createExplosion(entity, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 3, Explosion.DestructionType.BREAK);
						world.getEntitiesByClass(LivingEntity.class, new Box(pos).expand(4), livingEntity -> true).forEach(livingEntity -> livingEntity.damage(DamageSource.explosion((LivingEntity) entity), 12));
					}
				}
			});
		}
	}
}
