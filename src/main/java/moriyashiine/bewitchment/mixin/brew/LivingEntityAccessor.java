package moriyashiine.bewitchment.mixin.brew;

import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(LivingEntity.class)
public interface LivingEntityAccessor {
	@Invoker("getNextAirUnderwater")
	int bw_getNextAirUnderwater(int air);
	
	@Invoker("getNextAirOnLand")
	int bw_getNextAirOnLand(int air);
}
