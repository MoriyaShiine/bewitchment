package moriyashiine.bewitchment.common.ritualfunction;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.api.registry.RitualFunction;
import moriyashiine.bewitchment.common.item.TaglockItem;
import moriyashiine.bewitchment.common.item.WaystoneItem;
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
	public void start(ServerWorld world, BlockPos pos, Inventory inventory) {
		BlockPos location = null;
		for (int i = 0; i < inventory.size(); i++) {
			ItemStack stack = inventory.getStack(i);
			if (stack.getItem() instanceof TaglockItem && stack.hasTag() && stack.getOrCreateTag().contains("OwnerUUID")) {
				LivingEntity livingEntity = BewitchmentAPI.getTaglockOwner(world, stack);
				if (livingEntity != null) {
					location = livingEntity.getBlockPos();
					break;
				}
			}
			else if (stack.getItem() instanceof WaystoneItem && stack.hasTag() && stack.getOrCreateTag().contains("LocationPos") && world.getRegistryKey().getValue().toString().equals(stack.getOrCreateTag().getString("LocationWorld"))) {
				location = BlockPos.fromLong(stack.getOrCreateTag().getLong("LocationPos"));
				break;
			}
		}
		if (location != null) {
			int radius = 3;
			for (Entity entity : world.getNonSpectatingEntities(Entity.class, new Box(pos).expand(radius))) {
				BewitchmentAPI.teleport(entity, location);
			}
		}
		else {
			ItemScatterer.spawn(world, pos, inventory);
		}
		super.start(world, pos, inventory);
	}
}
