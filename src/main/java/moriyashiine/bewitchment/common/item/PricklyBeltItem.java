package moriyashiine.bewitchment.common.item;

import dev.emi.trinkets.api.SlotGroups;
import dev.emi.trinkets.api.Slots;
import dev.emi.trinkets.api.Trinket;
import dev.emi.trinkets.api.TrinketItem;
import moriyashiine.bewitchment.client.model.equipment.trinket.PricklyBeltModel;
import moriyashiine.bewitchment.common.Bewitchment;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@SuppressWarnings("ConstantConditions")
public class PricklyBeltItem extends TrinketItem {
	@Environment(EnvType.CLIENT)
	private static final Identifier TEXTURE = new Identifier(Bewitchment.MODID, "textures/entity/trinket/prickly_belt.png");
	
	public PricklyBeltItem(Settings settings) {
		super(settings);
	}
	
	@Override
	public boolean canWearInSlot(String group, String slot) {
		return group.equals(SlotGroups.LEGS) && slot.equals(Slots.BELT);
	}
	
	@Environment(EnvType.CLIENT)
	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		int uses = 0;
		if (stack.hasTag()) {
			uses = stack.getTag().getInt("PotionUses");
		}
		tooltip.add(new TranslatableText(Bewitchment.MODID + ".tooltip.uses_left", uses).formatted(Formatting.GRAY));
		Items.POTION.appendTooltip(stack, world, tooltip, context);
	}
	
	@Environment(EnvType.CLIENT)
	@Override
	public void render(String slot, MatrixStack matrixStack, VertexConsumerProvider vertexConsumer, int light, PlayerEntityModel<AbstractClientPlayerEntity> model, AbstractClientPlayerEntity player, float headYaw, float headPitch) {
		Trinket.translateToChest(matrixStack, model, player, headYaw, headPitch);
		new PricklyBeltModel().render(matrixStack, vertexConsumer.getBuffer(RenderLayer.getEntityCutout(TEXTURE)), light, OverlayTexture.DEFAULT_UV, 1, 1, 1, 1);
	}
}
