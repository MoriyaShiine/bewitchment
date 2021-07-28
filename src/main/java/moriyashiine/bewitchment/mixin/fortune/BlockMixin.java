package moriyashiine.bewitchment.mixin.fortune;

import moriyashiine.bewitchment.api.interfaces.entity.FortuneAccessor;
import moriyashiine.bewitchment.common.registry.BWFortunes;
import moriyashiine.bewitchment.common.registry.BWObjects;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.SilverfishEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Mixin(Block.class)
public abstract class BlockMixin {
	@Inject(method = "getDroppedStacks(Lnet/minecraft/block/BlockState;Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/entity/BlockEntity;Lnet/minecraft/entity/Entity;Lnet/minecraft/item/ItemStack;)Ljava/util/List;", at = @At("RETURN"), cancellable = true)
	private static void getDroppedStacks(BlockState state, ServerWorld world, BlockPos pos, @Nullable BlockEntity blockEntity, @Nullable Entity entity, ItemStack stack, CallbackInfoReturnable<List<ItemStack>> callbackInfo) {
		if (entity instanceof LivingEntity) {
			List<ItemStack> drops = callbackInfo.getReturnValue();
			if (!drops.isEmpty() && !EnchantmentHelper.get(stack).containsKey(Enchantments.SILK_TOUCH) && entity instanceof FortuneAccessor fortuneAccessor) {
				if (fortuneAccessor.getFortune() != null) {
					if (fortuneAccessor.getFortune().fortune == BWFortunes.TREASURE && world.random.nextFloat() < 1 / 25f) {
						Set<ItemStack> treasure = new HashSet<>();
						for (int i = 0; i < world.random.nextInt(1) + 1; i++) {
							switch (world.random.nextInt(4)) {
								case 0 -> treasure.add(new ItemStack(Items.DIAMOND, MathHelper.nextInt(world.random, 1, 3)));
								case 1 -> treasure.add(new ItemStack(Items.GOLD_INGOT, MathHelper.nextInt(world.random, 1, 5)));
								case 2 -> treasure.add(new ItemStack(Items.IRON_INGOT, MathHelper.nextInt(world.random, 2, 10)));
								case 3 -> treasure.add(new ItemStack(BWObjects.SILVER_INGOT, MathHelper.nextInt(world.random, 1, 5)));
								default -> treasure.add(ItemStack.EMPTY);
							}
						}
						drops.addAll(treasure);
						fortuneAccessor.getFortune().duration = 0;
					}
					else if (fortuneAccessor.getFortune().fortune == BWFortunes.INFESTED && world.random.nextFloat() < 1 / 25f) {
						SilverfishEntity silverfishEntity = EntityType.SILVERFISH.create(world);
						if (silverfishEntity != null) {
							silverfishEntity.initialize(world, world.getLocalDifficulty(pos), SpawnReason.EVENT, null, null);
							silverfishEntity.updatePositionAndAngles(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, world.random.nextFloat() * 360, 0);
							world.spawnEntity(silverfishEntity);
							fortuneAccessor.getFortune().duration = 0;
						}
					}
				}
			}
		}
	}
}
