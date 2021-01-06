package moriyashiine.bewitchment.common.statuseffect;

import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;

public class SinkingStatusEffect extends StatusEffect {
	public SinkingStatusEffect(StatusEffectType type, int color) {
		super(type, color);
	}
	
	@Override
	public boolean canApplyUpdateEffect(int duration, int amplifier) {
		return true;
	}
	
	@Override
	public void applyUpdateEffect(LivingEntity entity, int amplifier) {
		if (!entity.world.isClient) {
			Vec3d velocity = entity.getVelocity();
			float amount = 0.05f * (amplifier + 1);
			if (!entity.isOnGround() && velocity.getY() < 0) {
				entity.setVelocity(velocity.multiply(1, 1 + amount, 1));
				PlayerLookup.tracking(entity).forEach(playerEntity -> playerEntity.networkHandler.sendPacket(new EntityVelocityUpdateS2CPacket(entity)));
				if (entity instanceof PlayerEntity) {
					((ServerPlayerEntity) entity).networkHandler.sendPacket(new EntityVelocityUpdateS2CPacket(entity));
				}
			}
			if (entity.isTouchingWater()) {
				entity.setVelocity(velocity.add(0, -amount / 2, 0));
				PlayerLookup.tracking(entity).forEach(playerEntity -> playerEntity.networkHandler.sendPacket(new EntityVelocityUpdateS2CPacket(entity)));
				if (entity instanceof PlayerEntity) {
					((ServerPlayerEntity) entity).networkHandler.sendPacket(new EntityVelocityUpdateS2CPacket(entity));
				}
			}
		}
	}
}
