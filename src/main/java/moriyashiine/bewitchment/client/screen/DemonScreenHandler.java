package moriyashiine.bewitchment.client.screen;

import moriyashiine.bewitchment.client.network.packet.SyncDemonTradesPacket;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.entity.interfaces.DemonMerchant;
import moriyashiine.bewitchment.common.entity.living.DemonEntity;
import moriyashiine.bewitchment.common.registry.BWObjects;
import moriyashiine.bewitchment.common.registry.BWRegistries;
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
        this(syncId, new DemonMerchantImpl()); //dummy implementation that is later synced
    }

    public DemonScreenHandler(int syncId, DemonMerchant demonMerchant) {
        this(Bewitchment.DEMON_SCREEN_HANDLER, syncId, demonMerchant);
    }

    protected DemonScreenHandler(ScreenHandlerType<?> type, int syncId, DemonMerchant demonMerchant) {
        super(type, syncId);
        this.demonMerchant = demonMerchant;
        this.addSlot(new DemonTradeSlot(demonInventory, 0, 41, 96));
        this.addSlot(new DemonTradeSlot(demonInventory, 1, 80, 104));
        this.addSlot(new DemonTradeSlot(demonInventory, 2, 119, 96));
    }

    public int getOfferCount(){
        return 3;
    }

    @Override
    public void close(PlayerEntity player) {
        super.close(player);
        this.demonMerchant.setCurrentCustomer(null);
    }

    @Override
    public ItemStack transferSlot(PlayerEntity player, int index) {
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack onSlotClick(int i, int j, SlotActionType actionType, PlayerEntity playerEntity) {
        if (i == -999){
            return super.onSlotClick(i, j, actionType, playerEntity);
        }
        Slot slot = slots.get(i);
        if (slot instanceof DemonTradeSlot){
            DemonEntity.DemonTradeOffer offer = ((DemonTradeSlot) slot).getOffer();
            if (offer.isUsable()) {
                demonMerchant.onSell(offer);
                demonMerchant.trade(offer);
                if (!demonMerchant.getDemonTrader().world.isClient) {
                    SyncDemonTradesPacket.send(playerEntity, demonMerchant, syncId);
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

    public class DemonTradeSlot extends Slot {
        private final int contractIndex;
        public DemonTradeSlot(Inventory inventory, int index, int x, int y) {
            super(inventory, index, x, y);
            this.contractIndex = index;
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
                if (!getOffer().isUsable()) {
                    contract.getOrCreateTag().putBoolean("Signed", true);
                }
                return contract;
            }
            return ItemStack.EMPTY;
        }

        public DemonEntity.DemonTradeOffer getOffer(){
            return demonMerchant.getOffers().size() > contractIndex ? demonMerchant.getOffers().get(contractIndex) : null;
        }
    }
    protected static class DemonMerchantImpl implements DemonMerchant{
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
            this.costumer = customer;
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
        public void setDemonTraderClientside(LivingEntity trader) {
            this.trader = trader;
        }

        @Override
        @Environment(EnvType.CLIENT)
        public void setOffersClientside(List<DemonEntity.DemonTradeOffer> offers) {
            this.offers = offers;
        }

        @Override
        public void setDiscountClientside(boolean discount) {
            this.discount = discount;
        }
    }
}
