/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.bewitchment.mixin.vampirehack;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.common.registry.BWDamageSources;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin({Entity.class, LivingEntity.class, PlayerEntity.class, ServerPlayerEntity.class})
public class FireToSunDamageMixin {
	@ModifyVariable(method = "damage", at = @At("HEAD"), argsOnly = true)
	private DamageSource bewitchment$turnFireDamageIntoSunDamageForVampires(DamageSource source) {
		if (source.isFire() && BewitchmentAPI.isVampire(Entity.class.cast(this), true)) {
			return BWDamageSources.SUN;
		}
		return source;
	}
}
