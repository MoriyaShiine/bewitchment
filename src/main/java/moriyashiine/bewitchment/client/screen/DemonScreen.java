/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.entity.living.DemonEntity;
import moriyashiine.bewitchment.common.registry.BWComponents;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.util.Window;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

@SuppressWarnings("ConstantConditions")
public class DemonScreen extends HandledScreen<DemonScreenHandler> {
	private static final Identifier HEARTS = new Identifier("minecraft", "textures/gui/icons.png");

	public DemonScreen(DemonScreenHandler handler, PlayerInventory inventory, Text title) {
		super(handler, inventory, title);
	}

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		renderBackground(context);
		super.render(context, mouseX, mouseY, delta);
		for (Slot slot : getScreenHandler().slots) {
			if (slot instanceof DemonScreenHandler.DemonTradeSlot tradeSlot) {
				DemonEntity.DemonTradeOffer offer = tradeSlot.getOffer();
				if (offer != null) {
					drawHearts(context, slot.x, slot.y, offer);
				}
			}
		}
		drawPortrait(context, mouseX, mouseY);
		drawMouseoverTooltip(context, mouseX, mouseY);
	}

	private void drawPortrait(DrawContext context, int mouseX, int mouseY) {
		if (handler.demonMerchant.getDemonTrader() == null) {
			return;
		}
		int x = (width - backgroundWidth) / 2 + 56;
		int y = (height - backgroundHeight) / 2 + 16;
		Window window = client.getWindow();
		int scissorX = unscale(x, window.getScaledWidth(), window.getWidth());
		int scissorY = unscale(window.getScaledHeight() - (y + 68), window.getScaledHeight(), window.getHeight());
		int scissorWidth = unscale(64, window.getScaledWidth(), window.getWidth());
		int scissorHeight = unscale(64, window.getScaledHeight(), window.getHeight());
		RenderSystem.enableScissor(scissorX, scissorY, scissorWidth, scissorHeight);
		context.drawTexture(getBackground(), x, y, 176, 16, 64, 72);
		int height = (int) (handler.demonMerchant.getDemonTrader().getHeight() * 55);
		InventoryScreen.drawEntity(context, x + 32, y + height, 50, (float) (x + 32) - mouseX, (float) (y + 105 - 50) - mouseY, handler.demonMerchant.getDemonTrader());
		RenderSystem.disableScissor();
	}

	private static int unscale(int scaled, float windowScaled, int windowReal) {
		return (int) ((scaled / windowScaled) * windowReal);
	}

	@Override
	protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
		int x = (width - backgroundWidth) / 2;
		int y = (height - backgroundHeight) / 2;
		context.drawTexture(getBackground(), x, y, 0, 0, backgroundWidth, backgroundHeight);
	}

	@Override
	protected void drawForeground(DrawContext context, int mouseX, int mouseY) {
		context.drawText(textRenderer, this.title, this.titleX, this.titleY, 4210752, false);
	}

	private void drawHearts(DrawContext context, int x, int y, DemonEntity.DemonTradeOffer offer) {
		int cost = offer.getCost(handler.demonMerchant);
		int fullGroups = cost / 2;
		int heartX = (this.x + x - 6 - ((cost - 1) / 2 * 4));
		int heartY = this.y + y + 18;
		context.getMatrices().push();
		for (int i = 0; i < cost; i++) {
			heartX += 9;
			context.drawTexture(HEARTS, heartX, heartY, 16, 0, 9, 9);
			if (!BWComponents.CONTRACTS_COMPONENT.get(client.player).hasContract(offer.getContract())) {
				if (fullGroups > 0) {
					fullGroups--;
					i++;
					context.drawTexture(HEARTS, heartX, heartY, 52, 0, 9, 9);
				} else {
					context.drawTexture(HEARTS, heartX, heartY, 61, 0, 9, 9);
				}
			} else if (fullGroups > 0) {
				fullGroups--;
				i++;
			}
		}
		context.getMatrices().pop();
	}

	@Override
	protected void init() {
		super.init();
		titleX = (backgroundWidth - textRenderer.getWidth(title)) / 2;
	}

	private Identifier getBackground() {
		return new Identifier(Bewitchment.MODID, String.format("textures/gui/demon_trade_%d.png", handler.getOfferCount()));
	}
}
