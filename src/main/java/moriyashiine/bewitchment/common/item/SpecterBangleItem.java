package moriyashiine.bewitchment.common.item;

import dev.emi.trinkets.api.SlotGroups;
import dev.emi.trinkets.api.Slots;
import dev.emi.trinkets.api.Trinket;
import dev.emi.trinkets.api.TrinketItem;
import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.client.model.equipment.trinket.SpecterBangleModel;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.entity.interfaces.TrueInvisibleAccessor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.Identifier;

public class SpecterBangleItem extends TrinketItem {
	@Environment(EnvType.CLIENT)
	private static final Identifier TEXTURE = new Identifier(Bewitchment.MODID, "textures/entity/trinket/specter_bangle.png");
	
	public SpecterBangleItem(Settings settings) {
		super(settings);
	}
	
	@Override
	public boolean canWearInSlot(String group, String slot) {
		return group.equals(SlotGroups.FEET) && slot.equals(Slots.AGLET);
	}
	
	@Override
	public void tick(PlayerEntity player, ItemStack stack) {
		if (player.isSneaking() && BewitchmentAPI.drainMagic(player, 1, true)) {
			if (!player.world.isClient) {
				if (player.getRandom().nextFloat() < 1 / 40f) {
					BewitchmentAPI.drainMagic(player, 1, false);
				}
				if (!((TrueInvisibleAccessor) player).getTrueInvisible()) {
					((TrueInvisibleAccessor) player).setTrueInvisible(true);
				}
			}
			else if (player.getRandom().nextFloat() < 1 / 6f) {
				player.world.addParticle(ParticleTypes.SMOKE, player.getParticleX(1), player.getY(), player.getParticleZ(1), 0, 0, 0);
			}
		}
	}
	
	@Environment(EnvType.CLIENT)
	@Override
	public void render(String slot, MatrixStack matrixStack, VertexConsumerProvider vertexConsumer, int light, PlayerEntityModel<AbstractClientPlayerEntity> model, AbstractClientPlayerEntity player, float headYaw, float headPitch) {
		Trinket.translateToRightLeg(matrixStack, model, player, headYaw, headPitch);
		new SpecterBangleModel().render(matrixStack, vertexConsumer.getBuffer(RenderLayer.getArmorCutoutNoCull(TEXTURE)), light, OverlayTexture.DEFAULT_UV, 1, 1, 1, 1);
	}
}
