package moriyashiine.bewitchment.common.statuseffect;

import moriyashiine.bewitchment.api.interfaces.entity.PolymorphAccessor;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.entity.player.PlayerEntity;

import java.util.Optional;

public class PolymorphStatusEffect extends StatusEffect {
	//	private static final Identifier IMPERSONATE_IDENTIFIER = new Identifier(Bewitchment.MODID, "polymorph");
	
	public PolymorphStatusEffect(StatusEffectType type, int color) {
		super(type, color);
	}
	
	@Override
	public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier) {
		if (entity instanceof PlayerEntity) {
			//			PolymorphAccessor.of(entity).ifPresent(polymorphAccessor -> polymorphAccessor.getPolymorphUUID().ifPresent(uuid -> Impersonator.get((PlayerEntity) entity).impersonate(IMPERSONATE_IDENTIFIER, new GameProfile(uuid, polymorphAccessor.getPolymorphName()))));
		}
	}
	
	@Override
	public void onRemoved(LivingEntity entity, AttributeContainer attributes, int amplifier) {
		PolymorphAccessor.of(entity).ifPresent(polymorphAccessor -> {
			polymorphAccessor.setPolymorphUUID(Optional.empty());
			polymorphAccessor.setPolymorphName("");
			if (entity instanceof PlayerEntity) {
				//			Impersonator.get((PlayerEntity) entity).stopImpersonation(IMPERSONATE_IDENTIFIER);
			}
		});
	}
}
