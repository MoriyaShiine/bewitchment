package moriyashiine.bewitchment.client.render.trinket;

import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.client.TrinketRenderer;
import moriyashiine.bewitchment.client.BewitchmentClient;
import moriyashiine.bewitchment.client.model.equipment.trinket.DruidBandModel;
import moriyashiine.bewitchment.common.Bewitchment;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.Model;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class DruidBandRenderer implements TrinketRenderer {
	private static final Identifier TEXTURE = Bewitchment.id("textures/entity/trinket/druid_band.png");
	private static Model model;

	@Override
	public void render(ItemStack stack, SlotReference slotReference, EntityModel<? extends LivingEntity> contextModel, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, LivingEntity entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
		if (model == null) {
			model = new DruidBandModel(BewitchmentClient.getPart(BewitchmentClient.DRUID_BAND_MODEL_LAYER));
		}
		TrinketRenderer.translateToLeftLeg(matrices, (PlayerEntityModel<AbstractClientPlayerEntity>) contextModel, (AbstractClientPlayerEntity) entity);
		model.render(matrices, vertexConsumers.getBuffer(RenderLayer.getArmorCutoutNoCull(TEXTURE)), light, OverlayTexture.DEFAULT_UV, 1, 1, 1, 1);
	}
}
