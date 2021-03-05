package moriyashiine.bewitchment.common.item;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import dev.emi.trinkets.api.SlotGroups;
import dev.emi.trinkets.api.Slots;
import dev.emi.trinkets.api.Trinket;
import dev.emi.trinkets.api.TrinketItem;
import moriyashiine.bewitchment.client.model.equipment.trinket.ZephyrHarnessModel;
import moriyashiine.bewitchment.common.Bewitchment;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import java.util.UUID;

public class ZephyrHarnessItem extends TrinketItem {
	private static final Multimap<EntityAttribute, EntityAttributeModifier> MODIFIER_MAP = LinkedHashMultimap.create();
	
	@Environment(EnvType.CLIENT)
	private static final Identifier TEXTURE = new Identifier(Bewitchment.MODID, "textures/entity/trinket/zephyr_harness.png");
	
	static {
		MODIFIER_MAP.put(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, new EntityAttributeModifier(UUID.fromString("eec5a75f-44e6-4a02-ac8b-096144b57f10"), "Trinket modifier", 1, EntityAttributeModifier.Operation.ADDITION));
	}
	
	public ZephyrHarnessItem(Settings settings) {
		super(settings);
	}
	
	@Override
	public boolean canWearInSlot(String group, String slot) {
		return group.equals(SlotGroups.LEGS) && slot.equals(Slots.BELT);
	}
	
	@Override
	public Multimap<EntityAttribute, EntityAttributeModifier> getTrinketModifiers(String group, String slot, UUID uuid, ItemStack stack) {
		return MODIFIER_MAP;
	}
	
	@Environment(EnvType.CLIENT)
	@Override
	public void render(String slot, MatrixStack matrixStack, VertexConsumerProvider vertexConsumer, int light, PlayerEntityModel<AbstractClientPlayerEntity> model, AbstractClientPlayerEntity player, float headYaw, float headPitch) {
		Trinket.translateToChest(matrixStack, model, player, headYaw, headPitch);
		new ZephyrHarnessModel().render(matrixStack, vertexConsumer.getBuffer(RenderLayer.getEntityCutout(TEXTURE)), light, OverlayTexture.DEFAULT_UV, 1, 1, 1, 1);
	}
}
