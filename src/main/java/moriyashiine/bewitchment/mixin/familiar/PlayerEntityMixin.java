package moriyashiine.bewitchment.mixin.familiar;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@SuppressWarnings("ConstantConditions")
@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
	private static final EntityAttributeModifier WOLF_FAMILIAR_ARMOR_MODIFIER = new EntityAttributeModifier(UUID.fromString("1b2866e6-ca04-43e4-b643-1142c0791e6d"), "Familiar modifier", 6, EntityAttributeModifier.Operation.ADDITION);
	private static final EntityAttributeModifier WOLF_FAMILIAR_ARMOR_TOUGHNESS_MODIFIER = new EntityAttributeModifier(UUID.fromString("ec7f7a2e-d5c5-40c4-9338-c2808946f7c4"), "Familiar modifier", 6, EntityAttributeModifier.Operation.ADDITION);
	
	protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}
	
	@Inject(method = "tick", at = @At("TAIL"))
	private void tick(CallbackInfo callbackInfo) {
		if (!world.isClient) {
			boolean shouldHave = BewitchmentAPI.getFamiliar((PlayerEntity) (Object) this) == EntityType.WOLF;
			EntityAttributeInstance armorAttribute = getAttributeInstance(EntityAttributes.GENERIC_ARMOR);
			EntityAttributeInstance armorToughnessAttribute = getAttributeInstance(EntityAttributes.GENERIC_ARMOR_TOUGHNESS);
			if (shouldHave && !armorAttribute.hasModifier(WOLF_FAMILIAR_ARMOR_MODIFIER)) {
				armorAttribute.addPersistentModifier(WOLF_FAMILIAR_ARMOR_MODIFIER);
				armorToughnessAttribute.addPersistentModifier(WOLF_FAMILIAR_ARMOR_TOUGHNESS_MODIFIER);
			}
			else if (!shouldHave && armorAttribute.hasModifier(WOLF_FAMILIAR_ARMOR_MODIFIER)) {
				armorAttribute.removeModifier(WOLF_FAMILIAR_ARMOR_MODIFIER);
				armorToughnessAttribute.removeModifier(WOLF_FAMILIAR_ARMOR_TOUGHNESS_MODIFIER);
			}
		}
	}
}
