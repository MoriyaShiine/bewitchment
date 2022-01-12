package moriyashiine.bewitchment.common.fortune;

import moriyashiine.bewitchment.api.registry.Fortune;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;

public class ThunderboltFortune extends Fortune {
	public ThunderboltFortune(boolean positive) {
		super(positive);
	}

	@Override
	public boolean finish(ServerWorld world, PlayerEntity target) {
		LightningEntity entity = EntityType.LIGHTNING_BOLT.create(world);
		if (entity != null) {
			entity.updatePositionAndAngles(target.getX(), target.getY(), target.getZ(), world.random.nextFloat() * 360, 0);
			world.spawnEntity(entity);
		}
		return super.finish(world, target);
	}
}
