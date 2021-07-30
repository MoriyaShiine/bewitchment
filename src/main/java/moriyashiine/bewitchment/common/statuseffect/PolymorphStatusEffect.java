package moriyashiine.bewitchment.common.statuseffect;

import com.mojang.authlib.GameProfile;
import io.github.ladysnake.impersonate.Impersonator;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.entity.component.PolymorphComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class PolymorphStatusEffect extends StatusEffect {
	public static final Identifier IMPERSONATE_IDENTIFIER = new Identifier(Bewitchment.MODID, "polymorph");
	
	public PolymorphStatusEffect(StatusEffectType type, int color) {
		super(type, color);
	}
	
	@Override
	public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier) {
		if (entity instanceof ServerPlayerEntity player) {
			PolymorphComponent.maybeGet(player).ifPresent(polymorphComponent -> {
				if (polymorphComponent.getUuid() != null) {
					Impersonator.get(player).impersonate(IMPERSONATE_IDENTIFIER, new GameProfile(polymorphComponent.getUuid(), polymorphComponent.getName()));
				}
			});
		}
	}
}
