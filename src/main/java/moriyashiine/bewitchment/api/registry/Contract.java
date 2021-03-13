package moriyashiine.bewitchment.api.registry;

import net.minecraft.entity.LivingEntity;

public class Contract {
	public final boolean doesBaphometOffer;
	
	public Contract(boolean doesBaphometOffer) {
		this.doesBaphometOffer = doesBaphometOffer;
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
