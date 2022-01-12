package moriyashiine.bewitchment.common.sigil;

import moriyashiine.bewitchment.api.registry.Sigil;
import net.minecraft.block.PressurePlateBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class MendingSigil extends Sigil {
	public MendingSigil(boolean active, int uses) {
		super(active, uses);
	}

	@Override
	public ActionResult use(World world, BlockPos pos, LivingEntity user, Hand hand) {
		ActionResult result = super.use(world, pos, user, hand);
		boolean flag = true;
		if (world.getBlockState(pos).getBlock() instanceof PressurePlateBlock) {
			flag = user.age % 20 == 0;
		}
		if (flag) {
			StatusEffectInstance regeneration = new StatusEffectInstance(StatusEffects.REGENERATION, 200);
			if (user.canHaveStatusEffect(regeneration)) {
				if (!world.isClient) {
					user.addStatusEffect(regeneration);
				}
				result = ActionResult.SUCCESS;
			}
			StatusEffectInstance resistance = new StatusEffectInstance(StatusEffects.RESISTANCE, 200);
			if (user.canHaveStatusEffect(resistance)) {
				if (!world.isClient) {
					user.addStatusEffect(resistance);
				}
				result = ActionResult.SUCCESS;
			}
		}
		return result;
	}
}
