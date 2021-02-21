package moriyashiine.bewitchment.mixin;

import moriyashiine.bewitchment.common.misc.interfaces.EntityShapeContextAdditionAccessor;
import net.minecraft.block.EntityShapeContext;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityShapeContext.class)
public abstract class EntityShapeContextMixin implements ShapeContext, EntityShapeContextAdditionAccessor {
	private Entity entity;
	
	@Override
	public Entity bw_getEntity() {
		return entity;
	}
	
	@Inject(method = "<init>(Lnet/minecraft/entity/Entity;)V", at = @At("TAIL"))
	private void init(Entity entity, CallbackInfo callbackInfo) {
		this.entity = entity;
	}
}
