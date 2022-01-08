package moriyashiine.bewitchment.mixin.pledge;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.api.entity.Pledgeable;
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
	
	@ModifyVariable(method = "setTarget", at = @At("HEAD"), argsOnly = true)
	private LivingEntity modifyTarget(LivingEntity target) {
		if (!world.isClient && target != null) {
			if (this instanceof Pledgeable pledgeable) {
				if (target instanceof PlayerEntity player && BewitchmentAPI.isPledged(player, pledgeable.getPledgeID())) {
					BewitchmentAPI.unpledge(player);
				}
				pledgeable.summonMinions((MobEntity) (Object) this);
			}
		}
		return target;
	}
}
