package moriyashiine.bewitchment.common.fortune;

import moriyashiine.bewitchment.api.registry.Fortune;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;

public class CourageFortune extends Fortune {
	public CourageFortune(boolean positive) {
		super(positive);
	}
	
	@Override
	public boolean tick(ServerWorld world, PlayerEntity target) {
		if (target.getAttacker() != null) {
			target.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 600, 1));
			target.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 600, 1));
			return true;
		}
		return super.tick(world, target);
	}
}
