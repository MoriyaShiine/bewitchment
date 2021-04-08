package moriyashiine.bewitchment.api.registry;

import net.minecraft.entity.player.PlayerEntity;

public class Contract {
	public void tick(PlayerEntity player) {
	}
	
	public static class Instance {
		public final Contract contract;
		public int duration, cost;
		
		public Instance(Contract contract, int duration, int cost) {
			this.contract = contract;
			this.duration = duration;
			this.cost = cost;
		}
	}
}
