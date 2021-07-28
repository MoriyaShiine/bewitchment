package moriyashiine.bewitchment.common.statuseffect;

import moriyashiine.bewitchment.client.network.packet.SpawnExplosionParticlesPacket;
import moriyashiine.bewitchment.common.entity.living.ToadEntity;
import moriyashiine.bewitchment.common.registry.BWDamageSources;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;

public class WednesdayStatusEffect extends StatusEffect {
	public WednesdayStatusEffect(StatusEffectType type, int color) {
		super(type, color);
	}
	
	@Override
	public void onRemoved(LivingEntity entity, AttributeContainer attributes, int amplifier) {
		super.onRemoved(entity, attributes, amplifier);
		if (entity instanceof ToadEntity && ((ToadEntity) entity).isFromWednesdayRitual) {
			entity.remove(Entity.RemovalReason.DISCARDED);
		}
		else {
			entity.damage(BWDamageSources.WEDNESDAY, Float.MAX_VALUE);
		}
		entity.world.playSound(null, entity.getBlockPos(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.NEUTRAL, 1, 1);
		PlayerLookup.tracking(entity).forEach(playerEntity -> SpawnExplosionParticlesPacket.send(playerEntity, entity));
		if (entity instanceof PlayerEntity) {
			SpawnExplosionParticlesPacket.send((PlayerEntity) entity, entity);
		}
	}
}
