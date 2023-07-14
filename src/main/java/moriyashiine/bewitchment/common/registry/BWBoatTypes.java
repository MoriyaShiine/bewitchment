/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.registry;

import com.terraformersmc.terraform.boat.api.TerraformBoatType;
import com.terraformersmc.terraform.boat.api.TerraformBoatTypeRegistry;
import com.terraformersmc.terraform.boat.api.item.TerraformBoatItemHelper;
import moriyashiine.bewitchment.common.Bewitchment;
import net.minecraft.item.Item;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

public class BWBoatTypes {
	public static void init() {
		registerBoat("juniper", BWObjects.JUNIPER_PLANKS.asItem());
		registerBoat("cypress", BWObjects.CYPRESS_PLANKS.asItem());
		registerBoat("elder", BWObjects.ELDER_PLANKS.asItem());
		registerBoat("dragons_blood", BWObjects.DRAGONS_BLOOD_PLANKS.asItem());
	}

	private static void registerBoat(String name, Item planks) {
		Identifier boatId = new Identifier(Bewitchment.MODID, name + "_boat");
		RegistryKey<TerraformBoatType> key = TerraformBoatTypeRegistry.createKey(boatId);
		Item boat = TerraformBoatItemHelper.registerBoatItem(boatId, key, false);
		Item chest_boat = TerraformBoatItemHelper.registerBoatItem(new Identifier(Bewitchment.MODID, name + "_chest_boat"), key, true);
		TerraformBoatType boatType = new TerraformBoatType.Builder()
				.item(boat)
				.chestItem(chest_boat)
				.planks(planks)
				.build();
		Registry.register(TerraformBoatTypeRegistry.INSTANCE, key, boatType);
		BWObjects.BOATS.add(boat);
		BWObjects.BOATS.add(chest_boat);
	}
}
