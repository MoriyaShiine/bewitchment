package moriyashiine.bewitchment.mixin;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.api.interfaces.entity.Pledgeable;
import moriyashiine.bewitchment.common.entity.interfaces.MasterAccessor;
import moriyashiine.bewitchment.common.world.BWUniversalWorldState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.util.Pair;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;
import java.util.UUID;

@SuppressWarnings("ConstantConditions")
@Mixin(Entity.class)
public abstract class EntityMixin {
	@Shadow
	public abstract UUID getUuid();
	
	@Shadow
	public World world;
	
	@Shadow
	@Final
	protected Random random;
	
	@Inject(method = "isInvulnerableTo", at = @At("RETURN"), cancellable = true)
	private void isInvulnerableTo(DamageSource source, CallbackInfoReturnable<Boolean> callbackInfo) {
		if (!callbackInfo.getReturnValue() && !world.isClient && this instanceof MasterAccessor) {
			Entity attacker = source.getAttacker();
			if (attacker instanceof LivingEntity) {
				if (this instanceof MasterAccessor) {
					if (attacker.getUuid().equals(((MasterAccessor) this).getMasterUUID())) {
						callbackInfo.setReturnValue(true);
					}
				}
				if (attacker instanceof MasterAccessor) {
					if (getUuid().equals(((MasterAccessor) attacker).getMasterUUID())) {
						callbackInfo.setReturnValue(true);
					}
				}
			}
		}
	}
	
	@Inject(method = "remove", at = @At("TAIL"))
	private void remove(CallbackInfo callbackInfo) {
		if (!world.isClient) {
			BWUniversalWorldState worldState = BWUniversalWorldState.get(world);
			if (this instanceof Pledgeable) {
				for (int i = worldState.specificPledges.size() - 1; i >= 0; i--) {
					Pair<UUID, UUID> pair = worldState.specificPledges.get(i);
					if (pair.getRight().equals(getUuid())) {
						BewitchmentAPI.unpledge(world, ((Pledgeable) this).getPledgeID(), pair.getLeft());
						worldState.specificPledges.remove(i);
						worldState.markDirty();
					}
				}
			}
		}
	}
}
