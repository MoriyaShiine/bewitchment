package moriyashiine.bewitchment.common.fortune;

import moriyashiine.bewitchment.api.registry.Fortune;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;

public class PowerFortune extends Fortune {
	public PowerFortune(boolean positive) {
		super(positive);
	}

	@Override
	public boolean tick(ServerWorld world, PlayerEntity target) {
		if (target.getAttacker() != null) {
			target.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 600));
			target.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 600));
			return true;
		}
		return super.tick(world, target);
	}
}
