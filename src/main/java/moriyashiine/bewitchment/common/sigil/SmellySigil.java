package moriyashiine.bewitchment.common.sigil;

import moriyashiine.bewitchment.api.interfaces.HasSigil;
import moriyashiine.bewitchment.api.registry.Sigil;
import moriyashiine.bewitchment.common.registry.BWObjects;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

@SuppressWarnings("ConstantConditions")
public class SmellySigil extends Sigil {
	public SmellySigil(boolean active, int uses) {
		super(active, uses);
	}
	
	@Override
	public int tick(World world, BlockPos pos) {
		int amount = 0;
		if (world.getTime() % 20 == 0) {
			double x = pos.getX() + 0.5, y = pos.getY() + 0.5, z = pos.getZ() + 0.5;
			for (HostileEntity entity : world.getEntitiesByClass(HostileEntity.class, new Box(pos).expand(16, 6, 16), entity -> ((HasSigil) world.getBlockEntity(pos)).test(entity) && entity.getNavigation().getTargetPos() == null || world.getBlockState(entity.getNavigation().getTargetPos()).getBlock() != BWObjects.SIGIL)) {
				if (entity.getTarget() == null && Math.sqrt(entity.squaredDistanceTo(new Vec3d(x, y, z))) > 3 && (entity.getNavigation().getTargetPos() == null || entity.getNavigation().getTargetPos().getSquaredDistance(pos) > 1)) {
					entity.getNavigation().startMovingTo(x, y, z, 1);
					amount++;
				}
			}
		}
		return amount;
	}
}
