package moriyashiine.bewitchment.common.item;

import dev.emi.trinkets.api.SlotGroups;
import dev.emi.trinkets.api.Slots;
import dev.emi.trinkets.api.Trinket;
import dev.emi.trinkets.api.TrinketItem;
import moriyashiine.bewitchment.api.BewitchmentAPI;
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
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvents;

public class HellishBaubleItem extends TrinketItem {
	public HellishBaubleItem(Settings settings) {
		super(settings);
	}
	
	@Override
	public boolean canWearInSlot(String group, String slot) {
		return group.equals(SlotGroups.CHEST) && slot.equals(Slots.NECKLACE);
	}
	
	@Override
	public void tick(PlayerEntity player, ItemStack stack) {
		if (!player.world.isClient && player.age % 20 == 0 && player.isOnFire() && BewitchmentAPI.drainMagic(player, 1, false)) {
			player.world.playSound(null, player.getBlockPos(), SoundEvents.ENTITY_GENERIC_EXTINGUISH_FIRE, player.getSoundCategory(), 1, 1);
			player.extinguish();
		}
	}
	
	@Environment(EnvType.CLIENT)
	@Override
	public void render(String slot, MatrixStack matrixStack, VertexConsumerProvider vertexConsumer, int light, PlayerEntityModel<AbstractClientPlayerEntity> model, AbstractClientPlayerEntity player, float headYaw, float headPitch) {
		Trinket.translateToChest(matrixStack, model, player, headYaw, headPitch);
		matrixStack.translate(0, -1 / 4.25f, 1 / 48f);
		matrixStack.scale(1 / 3f, 1 / 3f, 1 / 3f);
		matrixStack.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(180));
		MinecraftClient.getInstance().getItemRenderer().renderItem(new ItemStack(BWObjects.HELLISH_BAUBLE), ModelTransformation.Mode.FIXED, light, OverlayTexture.DEFAULT_UV, matrixStack, vertexConsumer);
	}
}
