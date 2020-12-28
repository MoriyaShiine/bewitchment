package moriyashiine.bewitchment.common.statuseffect;

import moriyashiine.bewitchment.client.network.packet.SpawnPortalParticlesPacket;
import net.fabricmc.fabric.api.server.PlayerStream;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;

public class DisjunctionStatusEffect extends StatusEffect {
	public DisjunctionStatusEffect(StatusEffectType type, int color) {
		super(type, color);
	}
	
	@Override
	public boolean isInstant() {
		return true;
	}
	
	@Override
	public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier) {
		if (!entity.world.isClient) {
			int distance = 8 * (amplifier + 1);
			for (int i = 0; i < 32; i++) {
				BlockPos.Mutable mutable = new BlockPos.Mutable(entity.getX() + MathHelper.nextDouble(entity.getRandom(), -distance, distance), entity.getY() + MathHelper.nextDouble(entity.getRandom(), -distance / 2f, distance / 2f), entity.getZ() + MathHelper.nextDouble(entity.getRandom(), -distance, distance));
				if (!entity.world.getBlockState(mutable).getMaterial().isSolid()) {
					while (mutable.getY() > 0 && !entity.world.getBlockState(mutable).getMaterial().isSolid()) {
						mutable.move(Direction.DOWN);
					}
					if (entity.world.getBlockState(mutable).getMaterial().blocksMovement()) {
						if (!entity.isSilent()) {
							entity.world.playSound(null, entity.getBlockPos(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.NEUTRAL, 1, 1);
						}
						PlayerStream.watching(entity).forEach(playerEntity -> SpawnPortalParticlesPacket.send(playerEntity, entity));
						if (entity instanceof PlayerEntity) {
							SpawnPortalParticlesPacket.send((PlayerEntity) entity, entity);
						}
						entity.teleport(mutable.getX(), mutable.getY() + 1, mutable.getZ());
						if (!entity.isSilent()) {
							entity.world.playSound(null, entity.getBlockPos(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.NEUTRAL, 1, 1);
						}
						PlayerStream.watching(entity).forEach(playerEntity -> SpawnPortalParticlesPacket.send(playerEntity, entity));
						if (entity instanceof PlayerEntity) {
							SpawnPortalParticlesPacket.send((PlayerEntity) entity, entity);
						}
						break;
					}
				}
			}
		}
	}
}
