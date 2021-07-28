package moriyashiine.bewitchment.common.item;

import com.google.common.collect.Multimap;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.ItemStack;

import java.util.UUID;

public class ZephyrHarnessItem extends TrinketItem {
	private static EntityAttributeModifier KNOCKBACK_RESISTANCE;
	
	public ZephyrHarnessItem(Settings settings) {
		super(settings);
	}
	
	@Override
	public Multimap<EntityAttribute, EntityAttributeModifier> getModifiers(ItemStack stack, SlotReference slot, LivingEntity entity, UUID uuid) {
		Multimap<EntityAttribute, EntityAttributeModifier> modifiers = super.getModifiers(stack, slot, entity, uuid);
		if (KNOCKBACK_RESISTANCE == null) {
			KNOCKBACK_RESISTANCE = new EntityAttributeModifier(uuid, "Trinket modifier", 1, EntityAttributeModifier.Operation.ADDITION);
		}
		modifiers.put(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, KNOCKBACK_RESISTANCE);
		return modifiers;
	}
}
