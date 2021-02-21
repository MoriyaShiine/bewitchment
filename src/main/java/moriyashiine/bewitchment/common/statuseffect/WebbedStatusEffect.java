package moriyashiine.bewitchment.common.statuseffect;

import moriyashiine.bewitchment.common.misc.BWUtil;
import moriyashiine.bewitchment.common.registry.BWObjects;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class WebbedStatusEffect extends StatusEffect {
	public WebbedStatusEffect(StatusEffectType type, int color) {
		super(type, color);
	}
	
	@Override
	public boolean isInstant() {
		return true;
	}
	
	@Override
	public void applyInstantEffect(@Nullable Entity source, @Nullable Entity attacker, LivingEntity target, int amplifier, double proximity) {
		if (!target.world.isClient) {
			for (BlockPos air : BWUtil.getBlockPoses(target.getBlockPos(), Math.min(3, amplifier + 1), currentPos -> target.world.getBlockState(currentPos).isAir())) {
				target.world.setBlockState(air, BWObjects.TEMPORARY_COBWEB.getDefaultState());
			}
		}
	}
}
