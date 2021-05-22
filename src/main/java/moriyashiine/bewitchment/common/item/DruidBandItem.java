package moriyashiine.bewitchment.common.item;

import dev.emi.trinkets.api.SlotGroups;
import dev.emi.trinkets.api.Slots;
import dev.emi.trinkets.api.Trinket;
import dev.emi.trinkets.api.TrinketItem;
import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.client.model.equipment.trinket.DruidBandModel;
import moriyashiine.bewitchment.common.Bewitchment;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Fertilizable;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class DruidBandItem extends TrinketItem {
	@Environment(EnvType.CLIENT)
	private static final Identifier TEXTURE = new Identifier(Bewitchment.MODID, "textures/entity/trinket/druid_band.png");
	
	public DruidBandItem(Settings settings) {
		super(settings);
	}
	
	@Override
	public boolean canWearInSlot(String group, String slot) {
		return group.equals(SlotGroups.FEET) && slot.equals(Slots.AGLET);
	}
	
	@Override
	public void tick(PlayerEntity player, ItemStack stack) {
		if (!player.world.isClient && player.age % 10 == 0 && player.world.getBlockState(player.getBlockPos().down()).getBlock() instanceof Fertilizable) {
			StatusEffectInstance speed = new StatusEffectInstance(StatusEffects.SPEED, 300, 0, true, false);
			StatusEffectInstance regeneration = new StatusEffectInstance(StatusEffects.REGENERATION, 300, 0, true, false);
			boolean canApply = player.canHaveStatusEffect(speed) && !player.hasStatusEffect(StatusEffects.SPEED);
			canApply |= player.canHaveStatusEffect(regeneration) && !player.hasStatusEffect(StatusEffects.REGENERATION);
			if (canApply && BewitchmentAPI.drainMagic(player, 1, false)) {
				player.addStatusEffect(speed);
				player.addStatusEffect(regeneration);
			}
		}
	}
	
	@Environment(EnvType.CLIENT)
	@Override
	public void render(String slot, MatrixStack matrixStack, VertexConsumerProvider vertexConsumer, int light, PlayerEntityModel<AbstractClientPlayerEntity> model, AbstractClientPlayerEntity player, float headYaw, float headPitch) {
		Trinket.translateToLeftLeg(matrixStack, model, player, headYaw, headPitch);
		new DruidBandModel().render(matrixStack, vertexConsumer.getBuffer(RenderLayer.getArmorCutoutNoCull(TEXTURE)), light, OverlayTexture.DEFAULT_UV, 1, 1, 1, 1);
	}
}
