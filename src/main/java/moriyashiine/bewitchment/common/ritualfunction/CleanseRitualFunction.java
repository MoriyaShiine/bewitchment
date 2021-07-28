package moriyashiine.bewitchment.common.ritualfunction;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.api.interfaces.entity.CurseAccessor;
import moriyashiine.bewitchment.api.registry.Curse;
import moriyashiine.bewitchment.api.registry.RitualFunction;
import moriyashiine.bewitchment.common.item.TaglockItem;
import moriyashiine.bewitchment.mixin.ritual.ZombieVillagerEntityAccessor;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.ZombieVillagerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

import java.util.function.Predicate;

public class CleanseRitualFunction extends RitualFunction {
	public CleanseRitualFunction(ParticleType<?> startParticle, Predicate<LivingEntity> sacrifice) {
		super(startParticle, sacrifice);
	}
	
	@Override
	public String getInvalidMessage() {
		return "ritual.precondition.found_entity";
	}
	
	@Override
	public boolean isValid(ServerWorld world, BlockPos pos, Inventory inventory) {
		ItemStack taglock = null;
		for (int i = 0; i < inventory.size(); i++) {
			ItemStack stack = inventory.getStack(i);
			if (inventory.getStack(i).getItem() instanceof TaglockItem && TaglockItem.hasTaglock(stack)) {
				taglock = stack;
				break;
			}
		}
		return taglock != null && BewitchmentAPI.getTaglockOwner(world, taglock) != null;
	}
	
	@Override
	public void start(ServerWorld world, BlockPos glyphPos, BlockPos effectivePos, Inventory inventory, boolean catFamiliar) {
		ItemStack taglock = null;
		for (int i = 0; i < inventory.size(); i++) {
			ItemStack stack = inventory.getStack(i);
			if (inventory.getStack(i).getItem() instanceof TaglockItem) {
				taglock = stack;
				break;
			}
		}
		if (taglock != null) {
			LivingEntity livingEntity = BewitchmentAPI.getTaglockOwner(world, taglock);
			if (livingEntity instanceof CurseAccessor curseAccessor) {
				for (Curse.Instance instance : curseAccessor.getCurses()) {
					if (catFamiliar || (world.random.nextFloat() < (instance.curse.type == Curse.Type.LESSER ? 7.5f / 10f : 5 / 10f))) {
						curseAccessor.removeCurse(instance.curse);
					}
				}
			}
			if (livingEntity instanceof ZombieVillagerEntity) {
				((ZombieVillagerEntityAccessor) livingEntity).bw_finishConversion(world);
			}
		}
		super.start(world, glyphPos, effectivePos, inventory, catFamiliar);
	}
}
