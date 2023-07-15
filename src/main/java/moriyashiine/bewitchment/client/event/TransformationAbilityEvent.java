package moriyashiine.bewitchment.client.event;

import moriyashiine.bewitchment.api.entity.BroomEntity;
import moriyashiine.bewitchment.client.BewitchmentClient;
import moriyashiine.bewitchment.common.packet.TogglePressingForwardPacket;
import moriyashiine.bewitchment.common.registry.BWComponents;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;

@Environment(EnvType.CLIENT)
public class TransformationAbilityEvent implements ClientTickEvents.EndWorldTick {
	private int transformationAbilityCooldown = 0;

	@Override
	public void onEndTick(ClientWorld world) {
		PlayerEntity player = MinecraftClient.getInstance().player;
		if (player != null) {
			if (transformationAbilityCooldown > 0) {
				transformationAbilityCooldown--;
			} else if (BewitchmentClient.TRANSFORMATION_ABILITY.isPressed()) {
				transformationAbilityCooldown = 20;
				moriyashiine.bewitchment.common.packet.TransformationAbilityPacket.send();
			}
			if (BWComponents.BROOM_USER_COMPONENT.get(player).isPressingForward()) {
				TogglePressingForwardPacket.send(false);
			}
			if (MinecraftClient.getInstance().options.forwardKey.isPressed() && player.getVehicle() instanceof BroomEntity) {
				TogglePressingForwardPacket.send(true);
			}
		}
	}
}
