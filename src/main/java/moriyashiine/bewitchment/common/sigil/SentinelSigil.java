package moriyashiine.bewitchment.common.sigil;

import moriyashiine.bewitchment.api.registry.Sigil;
import moriyashiine.bewitchment.common.entity.living.GhostEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SentinelSigil extends Sigil {
	public SentinelSigil(boolean active, int uses) {
		super(active, uses);
	}
	
	@Override
	public ActionResult use(World world, BlockPos pos, LivingEntity user, Hand hand) {
		if (user.addStatusEffect(GhostEntity.getEffect(world.random.nextInt(4)))) {
			return ActionResult.SUCCESS;
		}
		return super.use(world, pos, user, hand);
	}
}
