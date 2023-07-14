/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.sigil;

import moriyashiine.bewitchment.api.registry.Sigil;
import net.minecraft.block.PressurePlateBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ShriekingSigil extends Sigil {
	public ShriekingSigil(boolean active, int uses) {
		super(active, uses);
	}

	@Override
	public ActionResult use(World world, BlockPos pos, LivingEntity user, Hand hand) {
		boolean flag = true;
		if (world.getBlockState(pos).getBlock() instanceof PressurePlateBlock) {
			flag = user.age % 20 == 0;
		}
		if (flag) {
			if (!world.isClient) {
				world.playSound(null, pos, SoundEvents.ENTITY_GHAST_HURT, SoundCategory.BLOCKS, 1, 1);
			}
			return ActionResult.SUCCESS;
		}
		return super.use(world, pos, user, hand);
	}
}
