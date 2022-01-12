/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.bewitchment.client.screen;

import moriyashiine.bewitchment.common.entity.DemonMerchant;
import moriyashiine.bewitchment.common.registry.BWScreenHandlers;

public class BaphometScreenHandler extends DemonScreenHandler {
	public BaphometScreenHandler(int syncId) {
		this(syncId, new DemonMerchantImpl());
	}

	public BaphometScreenHandler(int syncId, DemonMerchant merchant) {
		super(BWScreenHandlers.BAPHOMET_SCREEN_HANDLER, syncId, merchant);
		addSlot(new DemonTradeSlot(demonInventory, 3, 26, 64));
		addSlot(new DemonTradeSlot(demonInventory, 4, 134, 64));
	}

	public int getOfferCount() {
		return 5;
	}
}
