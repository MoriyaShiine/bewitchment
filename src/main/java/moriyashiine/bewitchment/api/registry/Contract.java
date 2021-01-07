package moriyashiine.bewitchment.api.registry;

import net.minecraft.entity.LivingEntity;

public class Contract {
	public final boolean canBeGiven;
	
	public Contract(boolean canBeGiven) {
		this.canBeGiven = canBeGiven;
	}
	
	public void tick(LivingEntity target, boolean includeNegative) {
	}
	
	public void finishUsing(LivingEntity user, boolean includeNegative) {
	}
	
	public static class Instance {
		public final Contract contract;
		public int duration;
		
		public Instance(Contract contract, int duration) {
			
			this.contract = contract;
			this.duration = duration;
		}
	}
}
