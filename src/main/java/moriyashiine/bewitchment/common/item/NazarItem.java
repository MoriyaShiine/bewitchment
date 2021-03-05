package moriyashiine.bewitchment.common.item;

import dev.emi.trinkets.api.SlotGroups;
import dev.emi.trinkets.api.Slots;
import dev.emi.trinkets.api.Trinket;
import dev.emi.trinkets.api.TrinketItem;
import moriyashiine.bewitchment.common.registry.BWObjects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.item.ItemStack;

public class NazarItem extends TrinketItem {
	public NazarItem(Settings settings) {
		super(settings);
	}
	
	@Override
	public boolean canWearInSlot(String group, String slot) {
		return group.equals(SlotGroups.CHEST) && slot.equals(Slots.NECKLACE);
	}
	
	@Environment(EnvType.CLIENT)
	@Override
	public void render(String slot, MatrixStack matrixStack, VertexConsumerProvider vertexConsumer, int light, PlayerEntityModel<AbstractClientPlayerEntity> model, AbstractClientPlayerEntity player, float headYaw, float headPitch) {
		ItemStack stack = new ItemStack(BWObjects.NAZAR);
		stack.getOrCreateTag().putBoolean("Worn", true);
		Trinket.translateToChest(matrixStack, model, player, headYaw, headPitch);
		matrixStack.translate(0, -1 / 4.25f, 1 / 48f);
		matrixStack.scale(1 / 3f, 1 / 3f, 1 / 3f);
		matrixStack.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(180));
		MinecraftClient.getInstance().getItemRenderer().renderItem(stack, ModelTransformation.Mode.FIXED, light, OverlayTexture.DEFAULT_UV, matrixStack, vertexConsumer);
	}
}
