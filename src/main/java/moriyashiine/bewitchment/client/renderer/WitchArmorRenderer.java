/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.bewitchment.client.renderer;

import moriyashiine.bewitchment.client.BewitchmentClient;
import moriyashiine.bewitchment.client.model.equipment.armor.WitchArmorModel;
import moriyashiine.bewitchment.common.Bewitchment;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class WitchArmorRenderer implements ArmorRenderer {
	private static final Identifier WITCH_HAT_VARIANT_TEXTURE = new Identifier(Bewitchment.MODID, "textures/entity/armor/witch_hat_variant.png");
	private static WitchArmorModel<LivingEntity> armorModel;
	private static WitchArmorModel<LivingEntity> bootsModel;

	private final Identifier texture;
	private final Item hatItem;

	public WitchArmorRenderer(Identifier texture, @Nullable Item hatItem) {
		this.texture = texture;
		this.hatItem = hatItem;
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, ItemStack stack, LivingEntity entity, EquipmentSlot slot, int light, BipedEntityModel<LivingEntity> contextModel) {
		if (armorModel == null) {
			armorModel = new WitchArmorModel<>(MinecraftClient.getInstance().getEntityModelLoader().getModelPart(BewitchmentClient.WITCH_ARMOR_MODEL_LAYER));
			bootsModel = new WitchArmorModel<>(MinecraftClient.getInstance().getEntityModelLoader().getModelPart(BewitchmentClient.WITCH_ARMOR_MODEL_LAYER));
		}
		if (slot == EquipmentSlot.FEET) {
			contextModel.setAttributes(bootsModel);
			bootsModel.setVisible(false);
			bootsModel.leftLeg.visible = true;
			bootsModel.rightLeg.visible = true;
			ArmorRenderer.renderPart(matrices, vertexConsumers, light, stack, bootsModel, texture);
		} else {
			boolean hat = stack.getItem() == hatItem;
			contextModel.setAttributes(armorModel);
			armorModel.setVisible(false);
			armorModel.hood01.visible = !hat;
			armorModel.hat1.visible = hat;
			armorModel.head.visible = slot == EquipmentSlot.HEAD;
			armorModel.body.visible = slot == EquipmentSlot.CHEST;
			armorModel.leftArm.visible = slot == EquipmentSlot.CHEST;
			armorModel.rightArm.visible = slot == EquipmentSlot.CHEST;
			armorModel.leftLeg.visible = slot == EquipmentSlot.LEGS;
			armorModel.rightLeg.visible = slot == EquipmentSlot.LEGS;
			armorModel.lowerLeftSkirt.visible = entity.getEquippedStack(EquipmentSlot.FEET).isEmpty();
			armorModel.lowerRightSkirt.visible = armorModel.lowerLeftSkirt.visible;
			ArmorRenderer.renderPart(matrices, vertexConsumers, light, stack, armorModel, hat && stack.getName().asString().toLowerCase().contains("faith") ? WITCH_HAT_VARIANT_TEXTURE : texture);
		}
	}
}
