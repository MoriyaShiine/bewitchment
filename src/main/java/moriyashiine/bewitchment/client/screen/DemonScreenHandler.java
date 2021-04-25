package moriyashiine.bewitchment.client.screen;

import moriyashiine.bewitchment.api.interfaces.entity.ContractAccessor;
import moriyashiine.bewitchment.client.network.packet.SyncContractsPacket;
import moriyashiine.bewitchment.client.network.packet.SyncDemonTradesPacket;
import moriyashiine.bewitchment.common.entity.interfaces.DemonMerchant;
import moriyashiine.bewitchment.common.entity.living.DemonEntity;
import moriyashiine.bewitchment.common.registry.BWDamageSources;
import moriyashiine.bewitchment.common.registry.BWObjects;
import moriyashiine.bewitchment.common.registry.BWRegistries;
import moriyashiine.bewitchment.common.registry.BWScreenHandlers;
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
	public DemonMerchant demonMerchant;
	public Inventory demonInventory = new SimpleInventory(getOfferCount());
	
	public DemonScreenHandler(int syncId) {
		this(syncId, new DemonMerchantImpl());
	}
	
	public DemonScreenHandler(int syncId, DemonMerchant demonMerchant) {
		this(BWScreenHandlers.DEMON_SCREEN_HANDLER, syncId, demonMerchant);
	}
	
	protected DemonScreenHandler(ScreenHandlerType<?> type, int syncId, DemonMerchant demonMerchant) {
		super(type, syncId);
		this.demonMerchant = demonMerchant;
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
	public ItemStack onSlotClick(int i, int j, SlotActionType actionType, PlayerEntity playerEntity) {
		if (i == -999) {
			return super.onSlotClick(i, j, actionType, playerEntity);
		}
		Slot slot = slots.get(i);
		if (slot instanceof DemonTradeSlot) {
			DemonEntity.DemonTradeOffer offer = ((DemonTradeSlot) slot).getOffer();
			if (!((ContractAccessor) playerEntity).hasContract(offer.getContract())) {
				demonMerchant.onSell(offer);
				demonMerchant.trade(offer);
				if (!demonMerchant.getDemonTrader().world.isClient) {
					SyncContractsPacket.send(playerEntity);
					SyncDemonTradesPacket.send(playerEntity, demonMerchant, syncId);
					int cost = offer.getCost(demonMerchant);
					if (playerEntity.getMaxHealth() - cost <= 0) {
						((ContractAccessor) playerEntity).getContracts().clear();
						playerEntity.damage(BWDamageSources.DEATH, Float.MAX_VALUE);
					}
					else if (playerEntity.getHealth() > playerEntity.getMaxHealth() - cost) {
						playerEntity.setHealth(playerEntity.getMaxHealth() - cost);
					}
				}
			}
			return ItemStack.EMPTY;
		}
		return super.onSlotClick(i, j, actionType, playerEntity);
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public void updateSlotStacks(List<ItemStack> stacks) {
		super.updateSlotStacks(stacks);
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
		public boolean doDrawHoveringEffect() {
			return getOffer() != null && super.doDrawHoveringEffect();
		}
		
		@Override
		public ItemStack getStack() {
			DemonEntity.DemonTradeOffer offer = getOffer();
			if (offer != null) {
				ItemStack contract = new ItemStack(BWObjects.DEMONIC_CONTRACT);
				contract.getOrCreateTag().putString("Contract", BWRegistries.CONTRACTS.getId(getOffer().getContract()).toString());
				contract.getOrCreateTag().putInt("Duration", getOffer().getDuration());
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
