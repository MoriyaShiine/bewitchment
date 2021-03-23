package moriyashiine.bewitchment.common.integration.requiem;

import ladysnake.requiem.api.v1.RequiemApi;
import ladysnake.requiem.api.v1.RequiemPlugin;
import ladysnake.requiem.api.v1.event.requiem.PossessionStateChangeCallback;
import ladysnake.requiem.api.v1.event.requiem.RemnantStateChangeCallback;
import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.api.interfaces.entity.TransformationAccessor;
import moriyashiine.bewitchment.common.integration.requiem.interfaces.RequiemCompatAccessor;
import moriyashiine.bewitchment.common.registry.BWTransformations;

public class BWRequiemCompat {
	public static void init() {
		RequiemApi.registerPlugin(new RequiemPlugin() {
			@Override
			public void onRequiemInitialize() {
				PossessionStateChangeCallback.EVENT.register((player, possessed) -> ((RequiemCompatAccessor) player).setWeakToSilverFromRequiem(possessed != null && BewitchmentAPI.isWeakToSilver(possessed)));
				RemnantStateChangeCallback.EVENT.register((player, state) -> {
					((TransformationAccessor) player).setAlternateForm(false);
					if (state.isVagrant()) {
						((RequiemCompatAccessor) player).setCachedTransformationForRequiem(((TransformationAccessor) player).getTransformation());
						((TransformationAccessor) player).setTransformation(BWTransformations.HUMAN);
					}
					else {
						((TransformationAccessor) player).setTransformation(((RequiemCompatAccessor) player).getCachedTransformationForRequiem());
						((RequiemCompatAccessor) player).setCachedTransformationForRequiem(BWTransformations.HUMAN);
					}
				});
			}
		});
	}
}
