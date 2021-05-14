package moriyashiine.bewitchment.common.block.util.interfaces;

import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface SpecialDoor {
	ActionResult onSpecialUse(BlockState state, World world, BlockPos pos, LivingEntity user, Hand hand);
}
