package moriyashiine.bewitchment.api.registry;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Sigil {
	public final boolean active;
	public final int uses;

	public Sigil(boolean active, int uses) {
		this.active = active;
		this.uses = uses;
	}

	public int tick(World world, BlockPos pos) {
		return 0;
	}

	public ActionResult use(World world, BlockPos pos, LivingEntity user, Hand hand) {
		return ActionResult.PASS;
	}
}
