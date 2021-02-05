package moriyashiine.bewitchment.common.statuseffect;

import com.mojang.authlib.GameProfile;
import io.github.ladysnake.impersonate.Impersonator;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.entity.interfaces.PolymorphAccessor;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.UUID;

public class PolymorphStatusEffect extends StatusEffect {
	private static final Identifier IMPERSONATE_IDENTIFIER = new Identifier(Bewitchment.MODID, "polymorph");
	
	public PolymorphStatusEffect(StatusEffectType type, int color) {
		super(type, color);
	}
	
	@Override
	public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier) {
		if (entity instanceof ServerPlayerEntity && !((PolymorphAccessor) entity).getPolymorphUUID().isEmpty()) {
			Impersonator.get((PlayerEntity) entity).impersonate(IMPERSONATE_IDENTIFIER, new GameProfile(UUID.fromString(((PolymorphAccessor) entity).getPolymorphUUID()), ((PolymorphAccessor) entity).getPolymorphName()));
		}
	}
	
	@Override
	public void onRemoved(LivingEntity entity, AttributeContainer attributes, int amplifier) {
		if (entity instanceof PolymorphAccessor) {
			PolymorphAccessor polymorphAccessor = (PolymorphAccessor) entity;
			polymorphAccessor.setPolymorphUUID("");
			polymorphAccessor.setPolymorphName("");
			if (entity instanceof ServerPlayerEntity) {
				Impersonator.get((PlayerEntity) entity).stopImpersonation(IMPERSONATE_IDENTIFIER);
			}
		}
	}
}
