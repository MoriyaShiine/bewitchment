package moriyashiine.bewitchment.mixin.ritual;

import net.minecraft.entity.mob.ZombieVillagerEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ZombieVillagerEntity.class)
public interface ZombieVillagerEntityAccessor {
	@Invoker("finishConversion")
	void bw_finishConversion(ServerWorld world);
}
