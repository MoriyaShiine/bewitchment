package moriyashiine.bewitchment.mixin;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.api.interfaces.MasterAccessor;
import moriyashiine.bewitchment.api.interfaces.Pledgeable;
import moriyashiine.bewitchment.common.world.BWUniversalWorldState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;

@Mixin(Entity.class)
public abstract class EntityMixin {
	@Shadow
	public abstract UUID getUuid();
	
	@Shadow
	public World world;
	
	@Inject(method = "isInvulnerableTo", at = @At("HEAD"), cancellable = true)
	private void isInvulnerableTo(DamageSource source, CallbackInfoReturnable<Boolean> callbackInfo) {
		Entity attacker = source.getAttacker();
		if (attacker instanceof LivingEntity) {
			Entity entity = (Entity) (Object) this;
			if (entity instanceof MasterAccessor) {
				MasterAccessor masterAccessor = (MasterAccessor) entity;
				UUID masterUUID = masterAccessor.getMasterUUID();
				if (masterUUID != null && masterUUID.equals(attacker.getUuid())) {
					callbackInfo.setReturnValue(true);
				}
			}
			if (attacker instanceof MasterAccessor) {
				MasterAccessor masterAccessor = (MasterAccessor) attacker;
				UUID masterUUID = masterAccessor.getMasterUUID();
				if (masterUUID != null && masterUUID.equals(getUuid())) {
					callbackInfo.setReturnValue(true);
				}
			}
		}
	}
	
	@Inject(method = "remove", at = @At("HEAD"))
	private void remove(CallbackInfo callbackInfo) {
		if (!world.isClient && this instanceof Pledgeable) {
			BWUniversalWorldState worldState = BWUniversalWorldState.get(world);
			for (UUID player : worldState.specificPledges.keySet()) {
				if (worldState.specificPledges.get(player).equals(getUuid())) {
					BewitchmentAPI.unpledge(world, ((Pledgeable) this).getPledgeUUID(), player);
					worldState.specificPledges.remove(player);
					worldState.markDirty();
				}
			}
		}
	}
}
