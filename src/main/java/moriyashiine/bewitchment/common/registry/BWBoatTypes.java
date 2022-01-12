/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.bewitchment.common.registry;

import com.terraformersmc.terraform.boat.api.TerraformBoatType;
import com.terraformersmc.terraform.boat.api.TerraformBoatTypeRegistry;
import com.terraformersmc.terraform.boat.api.item.TerraformBoatItemHelper;
import moriyashiine.bewitchment.common.Bewitchment;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class BWBoatTypes {
	public static TerraformBoatType juniper, cypress, elder, dragons_blood;

	public static void init() {
		Item juniper_boat = TerraformBoatItemHelper.registerBoatItem(new Identifier(Bewitchment.MODID, "juniper_boat"), () -> juniper, Bewitchment.BEWITCHMENT_GROUP);
		juniper = new TerraformBoatType.Builder().item(juniper_boat).build();
		Registry.register(TerraformBoatTypeRegistry.INSTANCE, new Identifier(Bewitchment.MODID, "juniper"), juniper);

		Item cypress_boat = TerraformBoatItemHelper.registerBoatItem(new Identifier(Bewitchment.MODID, "cypress_boat"), () -> cypress, Bewitchment.BEWITCHMENT_GROUP);
		cypress = new TerraformBoatType.Builder().item(cypress_boat).build();
		Registry.register(TerraformBoatTypeRegistry.INSTANCE, new Identifier(Bewitchment.MODID, "cypress"), cypress);

		Item elder_boat = TerraformBoatItemHelper.registerBoatItem(new Identifier(Bewitchment.MODID, "elder_boat"), () -> elder, Bewitchment.BEWITCHMENT_GROUP);
		elder = new TerraformBoatType.Builder().item(elder_boat).build();
		Registry.register(TerraformBoatTypeRegistry.INSTANCE, new Identifier(Bewitchment.MODID, "elder"), elder);

		Item dragons_blood_boat = TerraformBoatItemHelper.registerBoatItem(new Identifier(Bewitchment.MODID, "dragons_blood_boat"), () -> dragons_blood, Bewitchment.BEWITCHMENT_GROUP);
		dragons_blood = new TerraformBoatType.Builder().item(dragons_blood_boat).build();
		Registry.register(TerraformBoatTypeRegistry.INSTANCE, new Identifier(Bewitchment.MODID, "dragons_blood"), dragons_blood);
	}
}
