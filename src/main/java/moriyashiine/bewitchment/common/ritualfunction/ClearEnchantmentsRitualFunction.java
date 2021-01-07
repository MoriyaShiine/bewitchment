package moriyashiine.bewitchment.common.ritualfunction;

import moriyashiine.bewitchment.api.registry.RitualFunction;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;

import java.util.HashMap;
import java.util.function.Predicate;

public class ClearEnchantmentsRitualFunction extends RitualFunction {
	public ClearEnchantmentsRitualFunction(ParticleType<?> startParticle, Predicate<LivingEntity> sacrifice) {
		super(startParticle, sacrifice);
	}
	
	@Override
	public void start(ServerWorld world, BlockPos pos, Inventory inventory) {
		for (ItemEntity itemEntity : world.getEntitiesByType(EntityType.ITEM, new Box(pos).expand(2, 0, 2), entity -> entity.getStack().hasEnchantments())) {
			ItemStack stack = itemEntity.getStack();
			int enchantments = 0;
			for (Enchantment enchantment : EnchantmentHelper.get(stack).keySet()) {
				enchantments += EnchantmentHelper.getLevel(enchantment, stack);
			}
			EnchantmentHelper.set(new HashMap<>(), stack);
			if (stack.isDamaged()) {
				stack.setDamage(Math.max(0, stack.getDamage() - (enchantments * 64)));
			}
			stack.setRepairCost(0);
			ItemScatterer.spawn(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, stack.copy());
			stack.decrement(1);
		}
		super.start(world, pos, inventory);
	}
}
