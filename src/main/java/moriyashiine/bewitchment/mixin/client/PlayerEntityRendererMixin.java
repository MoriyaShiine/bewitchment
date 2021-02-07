package moriyashiine.bewitchment.mixin.client;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.api.entity.BroomEntity;
import moriyashiine.bewitchment.common.block.CoffinBlock;
import moriyashiine.bewitchment.common.entity.interfaces.WerewolfAccessor;
import moriyashiine.bewitchment.common.entity.living.util.BWHostileEntity;
import moriyashiine.bewitchment.common.registry.BWEntityTypes;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.BatEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@SuppressWarnings("ConstantConditions")
@Environment(EnvType.CLIENT)
@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerEntityRendererMixin {
	@Inject(method = "render", at = @At("HEAD"), cancellable = true)
	private void render(AbstractClientPlayerEntity player, float yaw, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, CallbackInfo callbackInfo) {
		Optional<BlockPos> pos = player.getSleepingPosition();
		if (pos.isPresent() && player.world.getBlockState(pos.get()).getBlock() instanceof CoffinBlock) {
			callbackInfo.cancel();
			return;
		}
		if (player.getVehicle() instanceof BroomEntity) {
			matrixStack.translate(0, MathHelper.sin((player.getVehicle().age + player.getVehicle().getEntityId()) / 4f) / 16f, 0);
		}
		LivingEntity entity = null;
		if (BewitchmentAPI.isVampire(player, false)) {
			entity = EntityType.BAT.create(player.world);
			((BatEntity) entity).setRoosting(false);
			matrixStack.scale(1.2f, EntityType.BAT.getHeight() * EntityType.PLAYER.getHeight() * 1.2f, 1.2f);
		}
		else if (BewitchmentAPI.isWerewolf(player, false)) {
			entity = BWEntityTypes.WEREWOLF.create(player.world);
			entity.getDataTracker().set(BWHostileEntity.VARIANT, ((WerewolfAccessor) player).getWerewolfVariant());
			matrixStack.scale(0.8f, 0.65f, 0.8f);
			if (player.isInSneakingPose()) {
				matrixStack.translate(0, 0.25f, 0);
			}
		}
		if (entity != null) {
			entity.age = player.age;
			entity.limbDistance = player.limbDistance;
			entity.lastLimbDistance = player.lastLimbDistance;
			entity.limbAngle = player.limbAngle;
			entity.headYaw = player.headYaw;
			entity.prevHeadYaw = player.prevHeadYaw;
			entity.bodyYaw = player.bodyYaw;
			entity.prevBodyYaw = player.prevBodyYaw;
			entity.handSwinging = player.handSwinging;
			entity.handSwingTicks = player.handSwingTicks;
			entity.handSwingProgress = player.handSwingProgress;
			entity.lastHandSwingProgress = player.lastHandSwingProgress;
			entity.pitch = player.pitch;
			entity.prevPitch = player.prevPitch;
			entity.preferredHand = player.preferredHand;
			entity.setStackInHand(Hand.MAIN_HAND, player.getMainHandStack());
			entity.setStackInHand(Hand.OFF_HAND, player.getOffHandStack());
			entity.setCurrentHand(player.getActiveHand() == null ? Hand.MAIN_HAND : player.getActiveHand());
			EntityRenderer<? super LivingEntity> renderer = MinecraftClient.getInstance().getEntityRenderDispatcher().getRenderer(entity);
			renderer.render(entity, yaw, tickDelta, matrixStack, vertexConsumerProvider, light);
			callbackInfo.cancel();
		}
	}
}
