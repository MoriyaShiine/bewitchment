package moriyashiine.bewitchment.common.sigil;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.api.registry.Sigil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class JudgmentSigil extends Sigil {
	public JudgmentSigil(boolean active, int uses) {
		super(active, uses);
	}
	
	@Override
	public ActionResult use(World world, BlockPos pos, LivingEntity user, Hand hand) {
		if (BewitchmentAPI.isWeakToSilver(user) && user.damage(DamageSource.MAGIC, 8)) {
			return ActionResult.SUCCESS;
		}
		return super.use(world, pos, user, hand);
	}
}
