package moriyashiine.bewitchment.common.network.packet;

import io.netty.buffer.Unpooled;
import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.api.interfaces.entity.BloodAccessor;
import moriyashiine.bewitchment.api.interfaces.entity.TransformationAccessor;
import moriyashiine.bewitchment.client.network.packet.SpawnSmokeParticlesPacket;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.registry.BWPledges;
import moriyashiine.bewitchment.common.registry.BWSoundEvents;
import moriyashiine.bewitchment.common.registry.BWTransformations;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import virtuoel.pehkui.api.ScaleType;

public class TransformationAbilityPacket {
	public static final Identifier ID = new Identifier(Bewitchment.MODID, "transformation_ability");
	
	public static void send() {
		PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
		ClientPlayNetworking.send(ID, buf);
	}
	
	public static void handle(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler network, PacketByteBuf buf, PacketSender sender) {
		server.execute(() -> {
			if (canUseAbility(player)) {
				useAbility(player);
			}
		});
	}
	
	private static boolean canUseAbility(PlayerEntity player) {
		return ((TransformationAccessor) player).getTransformation() == BWTransformations.VAMPIRE || ((TransformationAccessor) player).getTransformation() == BWTransformations.WEREWOLF;
	}
	
	public static void useAbility(PlayerEntity player) {
		World world = player.world;
		boolean isInAlternateForm = ((TransformationAccessor) player).getAlternateForm();
		if (((TransformationAccessor) player).getTransformation() == BWTransformations.VAMPIRE && BewitchmentAPI.isPledged(world, BWPledges.LILITH, player.getUuid()) && (isInAlternateForm || ((BloodAccessor) player).getBlood() > 0)) {
			PlayerLookup.tracking(player).forEach(foundPlayer -> SpawnSmokeParticlesPacket.send(foundPlayer, player));
			SpawnSmokeParticlesPacket.send(player, player);
			world.playSound(null, player.getBlockPos(), BWSoundEvents.ENTITY_GENERIC_TRANSFORM, player.getSoundCategory(), 1, 1);
			((TransformationAccessor) player).setAlternateForm(!isInAlternateForm);
			if (isInAlternateForm) {
				ScaleType.WIDTH.getScaleData(player).setScale(ScaleType.WIDTH.getScaleData(player).getScale() / (EntityType.BAT.getWidth() / EntityType.PLAYER.getWidth()));
				ScaleType.HEIGHT.getScaleData(player).setScale(ScaleType.HEIGHT.getScaleData(player).getScale() / (EntityType.BAT.getHeight() / EntityType.PLAYER.getHeight()));
				player.abilities.flying = false;
				player.sendAbilitiesUpdate();
			}
			else {
				ScaleType.WIDTH.getScaleData(player).setScale(ScaleType.WIDTH.getScaleData(player).getScale() * (EntityType.BAT.getWidth() / EntityType.PLAYER.getWidth()));
				ScaleType.HEIGHT.getScaleData(player).setScale(ScaleType.HEIGHT.getScaleData(player).getScale() * (EntityType.BAT.getHeight() / EntityType.PLAYER.getHeight()));
			}
		}
	}
}
