package moriyashiine.bewitchment.mixin.client;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.api.entity.BroomEntity;
import moriyashiine.bewitchment.client.renderer.ContributorHornsFeatureRenderer;
import moriyashiine.bewitchment.common.block.CoffinBlock;
import moriyashiine.bewitchment.common.entity.interfaces.TrueInvisibleAccessor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Environment(EnvType.CLIENT)
@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerEntityRendererMixin extends LivingEntityRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {
	public PlayerEntityRendererMixin(EntityRenderDispatcher dispatcher, PlayerEntityModel<AbstractClientPlayerEntity> model, float shadowRadius) {
		super(dispatcher, model, shadowRadius);
	}
	
	@Inject(method = "<init>(Lnet/minecraft/client/render/entity/EntityRenderDispatcher;Z)V", at = @At("TAIL"))
	private void PlayerEntityRenderer(EntityRenderDispatcher dispatcher, boolean bl, CallbackInfo callbackInfo) {
		addFeature(new ContributorHornsFeatureRenderer(this));
	}
	
	@Inject(method = "render", at = @At("HEAD"), cancellable = true)
	private void render(AbstractClientPlayerEntity player, float yaw, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, CallbackInfo callbackInfo) {
		Optional<BlockPos> pos = player.getSleepingPosition();
		if (pos.isPresent() && player.world.getBlockState(pos.get()).getBlock() instanceof CoffinBlock) {
			callbackInfo.cancel();
			return;
		}
		if (((TrueInvisibleAccessor) player).getTrueInvisible()) {
			callbackInfo.cancel();
			return;
		}
		if (player.getVehicle() instanceof BroomEntity) {
			matrixStack.translate(0, MathHelper.sin((player.getVehicle().age + player.getVehicle().getEntityId()) / 4f) / 16f, 0);
		}
		LivingEntity entity = BewitchmentAPI.getTransformedPlayerEntity(player);
		if (entity != null) {
			entity.age = player.age;
			entity.hurtTime = player.hurtTime;
			entity.maxHurtTime = Integer.MAX_VALUE;
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
			entity.setSneaking(player.isSneaking());
			entity.setPose(player.getPose());
			if (player.hasVehicle()) {
				entity.startRiding(player.getVehicle(), true);
			}
			float width = 1 / (entity.getType().getWidth() / EntityType.PLAYER.getWidth());
			matrixStack.scale(width, 1 / (entity.getType().getHeight() / EntityType.PLAYER.getHeight()), width);
			MinecraftClient.getInstance().getEntityRenderDispatcher().getRenderer(entity).render(entity, yaw, tickDelta, matrixStack, vertexConsumerProvider, light);
			callbackInfo.cancel();
		}
	}
}
