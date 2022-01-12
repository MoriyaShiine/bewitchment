/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.bewitchment.api.registry;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;

public class Fortune {
	public final boolean positive;

	public Fortune(boolean positive) {
		this.positive = positive;
	}

	public boolean tick(ServerWorld world, PlayerEntity target) {
		return false;
	}

	public boolean finish(ServerWorld world, PlayerEntity target) {
		return true;
	}

	public static class Instance {
		public final Fortune fortune;
		public int duration;

		public Instance(Fortune fortune, int duration) {

			this.fortune = fortune;
			this.duration = duration;
		}
	}
}
