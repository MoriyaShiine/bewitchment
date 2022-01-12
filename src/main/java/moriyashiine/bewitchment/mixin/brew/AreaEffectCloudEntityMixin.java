package moriyashiine.bewitchment.mixin.brew;

import moriyashiine.bewitchment.common.registry.BWComponents;
import moriyashiine.bewitchment.common.registry.BWStatusEffects;
import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(AreaEffectCloudEntity.class)
public abstract class AreaEffectCloudEntityMixin extends Entity {
	public AreaEffectCloudEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;addStatusEffect(Lnet/minecraft/entity/effect/StatusEffectInstance;Lnet/minecraft/entity/Entity;)Z"))
	private boolean addStatusEffect(LivingEntity entity, StatusEffectInstance effect, Entity source) {
		if (effect.getEffectType() == BWStatusEffects.POLYMORPH) {
			BWComponents.POLYMORPH_COMPONENT.maybeGet(this).ifPresent(thisPolymorphComponent -> {
				if (thisPolymorphComponent.getUuid() != null) {
					BWComponents.POLYMORPH_COMPONENT.maybeGet(entity).ifPresent(entityPolymorphComponent -> {
						entityPolymorphComponent.setUuid(thisPolymorphComponent.getUuid());
						entityPolymorphComponent.setName(thisPolymorphComponent.getName());
					});
				}
			});
		}
		return entity.addStatusEffect(effect);
	}
}
