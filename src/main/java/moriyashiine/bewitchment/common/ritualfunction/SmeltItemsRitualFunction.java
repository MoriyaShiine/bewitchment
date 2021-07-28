package moriyashiine.bewitchment.common.ritualfunction;

import moriyashiine.bewitchment.api.registry.RitualFunction;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particle.ParticleType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeType;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.function.Predicate;

public class SmeltItemsRitualFunction extends RitualFunction {
	public SmeltItemsRitualFunction(ParticleType<?> startParticle, Predicate<LivingEntity> sacrifice) {
		super(startParticle, sacrifice);
	}
	
	@Override
	public void tick(World world, BlockPos glyphPos, BlockPos effectivePos, boolean catFamiliar) {
		int radius = catFamiliar ? 9 : 3;
		if (!world.isClient) {
			if (world.getTime() % 20 == 0) {
				for (ItemEntity itemEntity : world.getEntitiesByType(EntityType.ITEM, new Box(effectivePos).expand(radius, 0, radius), entity -> true)) {
					if (world.random.nextFloat() < 1 / 4f) {
						world.getRecipeManager().listAllOfType(RecipeType.SMELTING).forEach(smeltingRecipe -> {
							for (Ingredient ingredient : smeltingRecipe.getIngredients()) {
								if (ingredient.test(itemEntity.getStack())) {
									world.playSound(null, itemEntity.getBlockPos(), SoundEvents.BLOCK_FURNACE_FIRE_CRACKLE, SoundCategory.BLOCKS, 1, 1);
									ItemScatterer.spawn(world, itemEntity.getX(), itemEntity.getY(), itemEntity.getZ(), smeltingRecipe.getOutput().copy());
									world.spawnEntity(new ExperienceOrbEntity(world, itemEntity.getX(), itemEntity.getY(), itemEntity.getZ(), 1));
									itemEntity.getStack().decrement(1);
								}
							}
						});
					}
				}
			}
		}
		else {
			world.addParticle(ParticleTypes.FLAME, effectivePos.getX() + MathHelper.nextDouble(world.random, -radius, radius), effectivePos.getY() + 0.5, effectivePos.getZ() + MathHelper.nextDouble(world.random, -radius, radius), 0, 0, 0);
		}
	}
}
