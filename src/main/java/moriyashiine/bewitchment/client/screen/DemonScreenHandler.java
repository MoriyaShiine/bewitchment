package moriyashiine.bewitchment.client.screen;

import moriyashiine.bewitchment.client.network.packet.SyncContractsPacket;
import moriyashiine.bewitchment.client.network.packet.SyncDemonTradesPacket;
import moriyashiine.bewitchment.common.entity.DemonMerchant;
import moriyashiine.bewitchment.common.entity.living.DemonEntity;
import moriyashiine.bewitchment.common.registry.*;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DemonScreenHandler extends ScreenHandler {
	public final DemonMerchant demonMerchant;
	public final Inventory demonInventory = new SimpleInventory(getOfferCount());

	public DemonScreenHandler(int syncId) {
		this(syncId, new DemonMerchantImpl());
	}

	public DemonScreenHandler(int syncId, DemonMerchant merchant) {
		this(BWScreenHandlers.DEMON_SCREEN_HANDLER, syncId, merchant);
	}

	protected DemonScreenHandler(ScreenHandlerType<?> type, int syncId, DemonMerchant merchant) {
		super(type, syncId);
		this.demonMerchant = merchant;
		addSlot(new DemonTradeSlot(demonInventory, 0, 41, 96));
		addSlot(new DemonTradeSlot(demonInventory, 1, 80, 104));
		addSlot(new DemonTradeSlot(demonInventory, 2, 119, 96));
	}

	public int getOfferCount() {
		return 3;
	}

	@Override
	public void close(PlayerEntity player) {
		super.close(player);
		demonMerchant.setCurrentCustomer(null);
	}

	@Override
	public ItemStack transferSlot(PlayerEntity player, int index) {
		return ItemStack.EMPTY;
	}

	@Override
	public void onSlotClick(int slotIndex, int button, SlotActionType actionType, PlayerEntity player) {
		if (slotIndex == -999) {
			return;
		}
		Slot slot = slots.get(slotIndex);
		if (slot instanceof DemonTradeSlot tradeSlot) {
			DemonEntity.DemonTradeOffer offer = tradeSlot.getOffer();
			BWComponents.CONTRACTS_COMPONENT.maybeGet(player).ifPresent(contractsComponent -> {
				if (!contractsComponent.hasContract(offer.getContract())) {
					demonMerchant.onSell(offer);
					demonMerchant.trade(offer);
					if (!demonMerchant.getDemonTrader().world.isClient) {
						SyncContractsPacket.send(player);
						SyncDemonTradesPacket.send(player, demonMerchant, syncId);
						if (player.getMaxHealth() - offer.getCost(demonMerchant) <= 0) {
							contractsComponent.getContracts().clear();
							player.damage(BWDamageSources.DEATH, Float.MAX_VALUE);
						}
					}
				}
			});
			return;
		}
		super.onSlotClick(slotIndex, button, actionType, player);
	}

	@Override
	public void onContentChanged(Inventory inventory) {
		super.onContentChanged(inventory);
	}

	@Override
	public boolean canUse(PlayerEntity player) {
		return !player.isSpectator();
	}

	@SuppressWarnings("ConstantConditions")
	public class DemonTradeSlot extends Slot {
		private final int contractIndex;

		public DemonTradeSlot(Inventory inventory, int index, int x, int y) {
			super(inventory, index, x, y);
			contractIndex = index;
		}

		@Override
		public boolean isEnabled() {
			return getOffer() != null && super.isEnabled();
		}

		@Override
		public ItemStack getStack() {
			DemonEntity.DemonTradeOffer offer = getOffer();
			if (offer != null) {
				ItemStack contract = new ItemStack(BWObjects.DEMONIC_CONTRACT);
				contract.getOrCreateNbt().putString("Contract", BWRegistries.CONTRACTS.getId(getOffer().getContract()).toString());
				contract.getOrCreateNbt().putInt("Duration", getOffer().getDuration());
				return contract;
			}
			return ItemStack.EMPTY;
		}

		public DemonEntity.DemonTradeOffer getOffer() {
			return demonMerchant.getOffers().size() > contractIndex ? demonMerchant.getOffers().get(contractIndex) : null;
		}
	}

	protected static class DemonMerchantImpl implements DemonMerchant {
		private List<DemonEntity.DemonTradeOffer> offers = new ArrayList<>();
		private PlayerEntity costumer;
		private LivingEntity trader;
		private boolean discount;

		@Override
		public List<DemonEntity.DemonTradeOffer> getOffers() {
			return offers;
		}

		@Override
		public void trade(DemonEntity.DemonTradeOffer offer) {
		}

		@Override
		public LivingEntity getDemonTrader() {
			return trader;
		}

		@Override
		public void onSell(DemonEntity.DemonTradeOffer offer) {
		}

		@Override
		public void setCurrentCustomer(PlayerEntity customer) {
			costumer = customer;
		}

		@Override
		public @Nullable PlayerEntity getCurrentCustomer() {
			return costumer;
		}

		@Override
		public boolean isDiscount() {
			return discount;
		}

		@Override
		@Environment(EnvType.CLIENT)
		public void setOffersClientside(List<DemonEntity.DemonTradeOffer> offers) {
			this.offers = offers;
		}

		@Override
		@Environment(EnvType.CLIENT)
		public void setDemonTraderClientside(LivingEntity trader) {
			this.trader = trader;
		}

		@Override
		public void setDiscountClientside(boolean discount) {
			this.discount = discount;
		}
	}
}
