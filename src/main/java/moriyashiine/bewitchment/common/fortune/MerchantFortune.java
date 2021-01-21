package moriyashiine.bewitchment.common.fortune;

import moriyashiine.bewitchment.api.registry.Fortune;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.passive.WanderingTraderEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class MerchantFortune extends Fortune {
	public MerchantFortune(boolean positive) {
		super(positive);
	}
	
	@Override
	public boolean finish(ServerWorld world, PlayerEntity target) {
		WanderingTraderEntity entity = EntityType.WANDERING_TRADER.create(world);
		if (entity != null) {
			for (int i = 0; i < 8; i++) {
				BlockPos pos = target.getBlockPos().add(MathHelper.nextInt(world.random, -3, 3), 0, MathHelper.nextInt(world.random, -3, 3));
				if (!world.getBlockState(pos).getMaterial().blocksMovement()) {
					entity.initialize(world, world.getLocalDifficulty(pos), SpawnReason.EVENT, null, null);
					entity.updatePositionAndAngles(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, world.random.nextFloat() * 360, 0);
					world.spawnEntity(entity);
					break;
				}
			}
		}
		return super.finish(world, target);
	}
}
