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

public class DecaySigil extends Sigil {
	public DecaySigil(boolean active, int uses) {
		super(active, uses);
	}
	
	@Override
	public ActionResult use(World world, BlockPos pos, LivingEntity user, Hand hand) {
		boolean flag = true;
		if (world.getBlockState(pos).getBlock() instanceof PressurePlateBlock) {
			flag = user.age % 20 == 0;
		}
		if (flag) {
			StatusEffectInstance effect = new StatusEffectInstance(StatusEffects.WITHER, 200, 1);
			if (user.canHaveStatusEffect(effect)) {
				if (!world.isClient) {
					user.addStatusEffect(effect);
				}
				return ActionResult.SUCCESS;
			}
		}
		return super.use(world, pos, user, hand);
	}
}
