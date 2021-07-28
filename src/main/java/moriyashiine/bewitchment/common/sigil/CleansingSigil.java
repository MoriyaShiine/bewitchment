package moriyashiine.bewitchment.common.sigil;

import moriyashiine.bewitchment.api.registry.Sigil;
import net.minecraft.block.PressurePlateBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
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
		if (flag) {
			flag = false;
			for (StatusEffect effect : Registry.STATUS_EFFECT) {
				if (user.hasStatusEffect(effect) && effect.getType() == StatusEffectType.HARMFUL) {
					if (!world.isClient) {
						user.removeStatusEffect(effect);
					}
					flag = true;
				}
			}
			if (flag) {
				return ActionResult.SUCCESS;
			}
		}
		return super.use(world, pos, user, hand);
	}
}
