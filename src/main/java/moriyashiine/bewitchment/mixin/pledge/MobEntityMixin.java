package moriyashiine.bewitchment.mixin.pledge;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.api.interfaces.entity.Pledgeable;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@SuppressWarnings("ConstantConditions")
@Mixin(MobEntity.class)
public abstract class MobEntityMixin extends LivingEntity {
	protected MobEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}
	
	@ModifyVariable(method = "setTarget", at = @At("HEAD"))
	private LivingEntity modifyTarget(LivingEntity target) {
		if (!world.isClient && target != null) {
			if (this instanceof Pledgeable pledgeable) {
				if (target instanceof PlayerEntity && BewitchmentAPI.isPledged((PlayerEntity) target, pledgeable.getPledgeID())) {
					BewitchmentAPI.unpledge((PlayerEntity) target);
				}
				pledgeable.summonMinions((MobEntity) (Object) this);
			}
		}
		return target;
	}
}
