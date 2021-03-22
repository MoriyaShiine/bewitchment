package moriyashiine.bewitchment.common.integration.nourish;

import dev.emi.nourish.NourishComponent;
import dev.emi.nourish.NourishMain;
import dev.emi.nourish.groups.NourishGroup;
import dev.emi.nourish.groups.NourishGroups;
import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.api.interfaces.entity.BloodAccessor;
import moriyashiine.bewitchment.common.Bewitchment;
import net.minecraft.entity.player.PlayerEntity;

public class BWNourishRuntimeJank {
	public static void doVampireLogic(PlayerEntity player) {
		if (Bewitchment.isNourishLoaded && BewitchmentAPI.isVampire(player, true)) {
			NourishComponent nourishComponent = NourishMain.NOURISH.get(player);
			for (NourishGroup group : NourishGroups.groups) {
				float targetValue = ((BloodAccessor) player).getBlood() > 0 ? group.getDefaultValue() : 0;
				if (nourishComponent.getValue(group) != targetValue) {
					nourishComponent.setValue(group, targetValue);
				}
			}
		}
	}
}
