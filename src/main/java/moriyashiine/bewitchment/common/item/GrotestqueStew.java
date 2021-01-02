package moriyashiine.bewitchment.common.item;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.api.interfaces.Pledgeable;
import moriyashiine.bewitchment.common.world.BWUniversalWorldState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

public class GrotestqueStew extends Item {
	public GrotestqueStew(Settings settings) {
		super(settings);
	}
	
	@Override
	public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
		if (!world.isClient) {
			if (!BewitchmentAPI.hasPledge(world, user.getUuid())) {
				LivingEntity closest = null;
				for (LivingEntity livingEntity : world.getEntitiesByClass(LivingEntity.class, new Box(user.getBlockPos()).expand(8), entity -> entity instanceof Pledgeable)) {
					if (closest == null || user.distanceTo(livingEntity) < user.distanceTo(closest)) {
						closest = livingEntity;
					}
				}
				if (closest instanceof Pledgeable) {
					BewitchmentAPI.pledge(world, ((Pledgeable) closest).getPledgeUUID(), user.getUuid());
					BWUniversalWorldState worldState = BWUniversalWorldState.get(world);
					worldState.specificPledges.put(user.getUuid(), closest.getUuid());
					worldState.markDirty();
					world.playSound(null, user.getBlockPos(), SoundEvents.ENTITY_WITHER_SPAWN, SoundCategory.PLAYERS, 1, 1);
				}
			}
		}
		return super.finishUsing(stack, world, user);
	}
}
