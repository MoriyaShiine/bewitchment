package moriyashiine.bewitchment.common.ritualfunction;

import moriyashiine.bewitchment.api.registry.RitualFunction;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.particle.ParticleType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;

import java.util.function.Predicate;

public class EnchantRitualFunction extends RitualFunction {
	public EnchantRitualFunction(ParticleType<?> startParticle, Predicate<LivingEntity> sacrifice) {
		super(startParticle, sacrifice);
	}
	
	@Override
	public void start(ServerWorld world, BlockPos pos, Inventory inventory) {
		for (ItemEntity itemEntity : world.getEntitiesByType(EntityType.ITEM, new Box(pos).expand(2, 0, 2), entity -> entity.getStack().isEnchantable())) {
			PlayerEntity closestPlayer = world.getClosestPlayer(itemEntity, 8);
			if (closestPlayer != null && closestPlayer.experienceLevel >= 5) {
				closestPlayer.addExperienceLevels(-5);
				EnchantmentHelper.enchant(world.random, itemEntity.getStack(), 40, false);
			}
			ItemScatterer.spawn(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, itemEntity.getStack().copy());
			itemEntity.getStack().decrement(1);
		}
		super.start(world, pos, inventory);
	}
}
