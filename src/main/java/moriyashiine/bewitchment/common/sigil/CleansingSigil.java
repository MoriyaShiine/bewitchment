package moriyashiine.bewitchment.common.sigil;

import moriyashiine.bewitchment.api.registry.Sigil;
import moriyashiine.bewitchment.common.registry.BWStatusEffects;
import moriyashiine.bewitchment.mixin.StatusEffectAccessor;
import net.minecraft.block.PressurePlateBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CleansingSigil extends Sigil {
	public CleansingSigil(boolean active, int uses) {
		super(active, uses);
	}
	
	@Override
	public ActionResult use(World world, BlockPos pos, LivingEntity user, Hand hand) {
		boolean flag = true;
		if (world.getBlockState(pos).getBlock() instanceof PressurePlateBlock) {
			flag = user.age % 20 == 0;
		}
		if (flag && user.getStatusEffects().stream().anyMatch(effect -> ((StatusEffectAccessor) effect.getEffectType()).bw_getType() == StatusEffectType.HARMFUL)) {
			StatusEffectInstance effect = new StatusEffectInstance(BWStatusEffects.PURITY, 1, 1, true, false);
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
