/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.bewitchment.common.registry;

import com.terraformersmc.terraform.boat.api.TerraformBoatType;
import com.terraformersmc.terraform.boat.api.TerraformBoatTypeRegistry;
import com.terraformersmc.terraform.boat.api.item.TerraformBoatItemHelper;
import com.terraformersmc.terraform.boat.impl.TerraformBoatTypeImpl;
import moriyashiine.bewitchment.common.Bewitchment;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.concurrent.atomic.AtomicReference;

public class BWBoatTypes {
	public static void init() {
		registerBoat("juniper");
		registerBoat("cypress");
		registerBoat("elder");
		registerBoat("dragons_blood");
	}

	private static void registerBoat(String name) {
		AtomicReference<TerraformBoatType> type = new AtomicReference<>();
		Item boat = TerraformBoatItemHelper.registerBoatItem(new Identifier(Bewitchment.MODID, name + "_boat"), type::get, false, Bewitchment.BEWITCHMENT_GROUP);
		Item chest_boat = TerraformBoatItemHelper.registerBoatItem(new Identifier(Bewitchment.MODID, name + "_chest_boat"), type::get, true, Bewitchment.BEWITCHMENT_GROUP);
		type.set(new TerraformBoatTypeImpl(boat, chest_boat));
		Registry.register(TerraformBoatTypeRegistry.INSTANCE, new Identifier(Bewitchment.MODID, name), type.get());
	}
}
