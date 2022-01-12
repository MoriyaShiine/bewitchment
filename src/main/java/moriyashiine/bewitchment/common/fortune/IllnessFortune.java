package moriyashiine.bewitchment.common.fortune;

import moriyashiine.bewitchment.api.registry.Fortune;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;

public class IllnessFortune extends Fortune {
	public IllnessFortune(boolean positive) {
		super(positive);
	}

	@Override
	public boolean finish(ServerWorld world, PlayerEntity target) {
		target.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, 600, 1));
		target.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 600, 1));
		return super.finish(world, target);
	}
}
