package moriyashiine.bewitchment.mixin.contract;

import moriyashiine.bewitchment.common.registry.BWComponents;
import moriyashiine.bewitchment.common.registry.BWContracts;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(MobEntity.class)
public abstract class MobEntityMixin extends LivingEntity {
	protected MobEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

	@ModifyVariable(method = "setTarget", at = @At("HEAD"), argsOnly = true)
	private LivingEntity modifyTarget(LivingEntity target) {
		if (!world.isClient && target instanceof PlayerEntity player) {
			if (BWComponents.CONTRACTS_COMPONENT.get(player).hasContract(BWContracts.WAR)) {
				Entity nearest = null;
				for (Entity entity : world.getEntitiesByType(getType(), new Box(getBlockPos()).expand(16), entity -> entity != this)) {
					if (nearest == null || entity.distanceTo(this) < nearest.distanceTo(this)) {
						nearest = entity;
					}
				}
				if (nearest != null) {
					return (LivingEntity) nearest;
				}
			}
		}
		return target;
	}
}
