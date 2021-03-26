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
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class BaphometScreenHandler extends DemonScreenHandler {
    public BaphometScreenHandler(int syncId){
        this(syncId, new DemonMerchantImpl());
    }
    public BaphometScreenHandler(int syncId, DemonMerchant demonMerchant) {
        super(Bewitchment.BAPHOMET_SCREEN_HANDLER, syncId, demonMerchant);
        this.addSlot(new DemonTradeSlot(demonInventory, 3, 26, 64));
        this.addSlot(new DemonTradeSlot(demonInventory, 4, 134, 64));
    }

    public int getOfferCount(){
        return 5;
    }
}
