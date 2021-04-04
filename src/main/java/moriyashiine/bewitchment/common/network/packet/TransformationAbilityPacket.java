package moriyashiine.bewitchment.common.network.packet;

import io.github.ladysnake.pal.AbilitySource;
import io.github.ladysnake.pal.Pal;
import io.github.ladysnake.pal.VanillaAbilities;
import io.netty.buffer.Unpooled;
import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.api.interfaces.entity.BloodAccessor;
import moriyashiine.bewitchment.api.interfaces.entity.TransformationAccessor;
import moriyashiine.bewitchment.client.network.packet.SpawnSmokeParticlesPacket;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.entity.interfaces.WerewolfAccessor;
import moriyashiine.bewitchment.common.registry.*;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import virtuoel.pehkui.api.ScaleData;

@SuppressWarnings("ConstantConditions")
public class TransformationAbilityPacket {
	public static final Identifier ID = new Identifier(Bewitchment.MODID, "transformation_ability");
	
	public static final AbilitySource VAMPIRE_FLIGHT_SOURCE = Pal.getAbilitySource(new Identifier(Bewitchment.MODID, "vampire_flight"));
	
	private static final float VAMPIRE_WIDTH = EntityType.BAT.getWidth() / EntityType.PLAYER.getWidth();
	private static final float VAMPIRE_HEIGHT = EntityType.BAT.getHeight() / EntityType.PLAYER.getHeight();
	private static final float WEREWOLF_WIDTH = BWEntityTypes.WEREWOLF.getWidth() / EntityType.PLAYER.getWidth();
	private static final float WEREWOLF_HEIGHT = BWEntityTypes.WEREWOLF.getHeight() / EntityType.PLAYER.getHeight();
	
	public static void send() {
		PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
		ClientPlayNetworking.send(ID, buf);
	}
	
	public static void handle(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler network, PacketByteBuf buf, PacketSender sender) {
		server.execute(() -> {
			if (canUseAbility(player)) {
				useAbility(player, false);
			}
		});
	}
	
	private static boolean canUseAbility(PlayerEntity player) {
		if (((TransformationAccessor) player).getTransformation() == BWTransformations.VAMPIRE) {
			return true;
		}
		if (((TransformationAccessor) player).getTransformation() == BWTransformations.WEREWOLF) {
			return !((WerewolfAccessor) player).getForcedTransformation();
		}
		return false;
	}
	
	public static void useAbility(PlayerEntity player, boolean forced) {
		World world = player.world;
		boolean isInAlternateForm = ((TransformationAccessor) player).getAlternateForm();
		ScaleData width = BWScaleTypes.MODIFY_WIDTH_TYPE.getScaleData(player);
		ScaleData height = BWScaleTypes.MODIFY_HEIGHT_TYPE.getScaleData(player);
		if (((TransformationAccessor) player).getTransformation() == BWTransformations.VAMPIRE && (forced || (BewitchmentAPI.isPledged(player, BWPledges.LILITH) && ((BloodAccessor) player).getBlood() > 0))) {
			PlayerLookup.tracking(player).forEach(foundPlayer -> SpawnSmokeParticlesPacket.send(foundPlayer, player));
			SpawnSmokeParticlesPacket.send(player, player);
			world.playSound(null, player.getBlockPos(), BWSoundEvents.ENTITY_GENERIC_TRANSFORM, player.getSoundCategory(), 1, 1);
			((TransformationAccessor) player).setAlternateForm(!isInAlternateForm);
			if (isInAlternateForm) {
				width.setScale(width.getBaseScale() / VAMPIRE_WIDTH);
				height.setScale(height.getBaseScale() / VAMPIRE_HEIGHT);
				VAMPIRE_FLIGHT_SOURCE.revokeFrom(player, VanillaAbilities.ALLOW_FLYING);
				VAMPIRE_FLIGHT_SOURCE.revokeFrom(player, VanillaAbilities.FLYING);
			}
			else {
				width.setScale(width.getBaseScale() * VAMPIRE_WIDTH);
				height.setScale(height.getBaseScale() * VAMPIRE_HEIGHT);
				VAMPIRE_FLIGHT_SOURCE.grantTo(player, VanillaAbilities.ALLOW_FLYING);
				VAMPIRE_FLIGHT_SOURCE.grantTo(player, VanillaAbilities.FLYING);
			}
		}
		else if (((TransformationAccessor) player).getTransformation() == BWTransformations.WEREWOLF && (forced || BewitchmentAPI.isPledged(player, BWPledges.HERNE))) {
			PlayerLookup.tracking(player).forEach(foundPlayer -> SpawnSmokeParticlesPacket.send(foundPlayer, player));
			SpawnSmokeParticlesPacket.send(player, player);
			world.playSound(null, player.getBlockPos(), BWSoundEvents.ENTITY_GENERIC_TRANSFORM, player.getSoundCategory(), 1, 1);
			((TransformationAccessor) player).setAlternateForm(!isInAlternateForm);
			if (isInAlternateForm) {
				width.setScale(width.getBaseScale() / WEREWOLF_WIDTH);
				height.setScale(height.getBaseScale() / WEREWOLF_HEIGHT);
				if (player.hasStatusEffect(StatusEffects.NIGHT_VISION) && player.getStatusEffect(StatusEffects.NIGHT_VISION).isAmbient()) {
					player.removeStatusEffect(StatusEffects.NIGHT_VISION);
				}
			}
			else {
				width.setScale(width.getBaseScale() * WEREWOLF_WIDTH);
				height.setScale(height.getBaseScale() * WEREWOLF_HEIGHT);
			}
		}
	}
}
