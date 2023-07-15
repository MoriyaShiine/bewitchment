/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.statuseffect;

import com.mojang.authlib.GameProfile;
import io.github.ladysnake.impersonate.Impersonator;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.registry.BWComponents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class PolymorphStatusEffect extends StatusEffect {
	public static final Identifier IMPERSONATE_IDENTIFIER = new Identifier(Bewitchment.MOD_ID, "polymorph");

	public PolymorphStatusEffect(StatusEffectCategory category, int color) {
		super(category, color);
	}

	@Override
	public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier) {
		if (entity instanceof ServerPlayerEntity player) {
			BWComponents.POLYMORPH_COMPONENT.maybeGet(player).ifPresent(polymorphComponent -> {
				if (polymorphComponent.getUuid() != null) {
					Impersonator.get(player).impersonate(IMPERSONATE_IDENTIFIER, new GameProfile(polymorphComponent.getUuid(), polymorphComponent.getName()));
				}
			});
		}
	}
}
