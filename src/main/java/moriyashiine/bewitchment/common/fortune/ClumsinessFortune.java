package moriyashiine.bewitchment.common.fortune;

import moriyashiine.bewitchment.api.registry.Fortune;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;

public class ClumsinessFortune extends Fortune {
	public ClumsinessFortune(boolean positive) {
		super(positive);
	}
	
	@Override
	public boolean finish(ServerWorld world, PlayerEntity target) {
		boolean drop = target.dropItem(target.getMainHandStack(), false, true) != null;
		if (drop) {
			target.setStackInHand(Hand.MAIN_HAND, ItemStack.EMPTY);
			return true;
		}
		return false;
	}
}
