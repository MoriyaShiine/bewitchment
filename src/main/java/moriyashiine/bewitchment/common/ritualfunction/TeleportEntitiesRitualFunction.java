package moriyashiine.bewitchment.common.ritualfunction;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.api.registry.RitualFunction;
import moriyashiine.bewitchment.common.item.TaglockItem;
import moriyashiine.bewitchment.common.item.WaystoneItem;
import moriyashiine.bewitchment.common.misc.BWUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;

import java.util.function.Predicate;

public class TeleportEntitiesRitualFunction extends RitualFunction {
	public TeleportEntitiesRitualFunction(ParticleType<?> startParticle, Predicate<LivingEntity> sacrifice) {
		super(startParticle, sacrifice);
	}
	
	@Override
	public void start(ServerWorld world, BlockPos glyphPos, BlockPos effectivePos, Inventory inventory, boolean catFamiliar) {
		BlockPos location = null;
		for (int i = 0; i < inventory.size(); i++) {
			ItemStack stack = inventory.getStack(i);
			if (stack.getItem() instanceof TaglockItem && TaglockItem.hasTaglock(stack)) {
				LivingEntity livingEntity = BewitchmentAPI.getTaglockOwner(world, stack);
				if (livingEntity != null && world.getRegistryKey().equals(livingEntity.getEntityWorld().getRegistryKey())) {
					location = livingEntity.getBlockPos();
					break;
				}
			}
			else if (stack.getItem() instanceof WaystoneItem && stack.hasTag() && stack.getOrCreateTag().contains("LocationPos") && world.getRegistryKey().getValue().toString().equals(stack.getOrCreateTag().getString("LocationWorld"))) {
				location = BlockPos.fromLong(stack.getOrCreateTag().getLong("LocationPos"));
				break;
			}
		}
		if (location != null && world.getWorldBorder().contains(location)) {
			int radius = catFamiliar ? 9 : 3;
			for (Entity entity : world.getNonSpectatingEntities(Entity.class, new Box(effectivePos).expand(radius))) {
				BWUtil.teleport(entity, location.getX() + 0.5, location.getY() - 0.5, location.getZ() + 0.5, true);
			}
		}
		else {
			ItemScatterer.spawn(world, glyphPos, inventory);
		}
		super.start(world, glyphPos, effectivePos, inventory, catFamiliar);
	}
}
