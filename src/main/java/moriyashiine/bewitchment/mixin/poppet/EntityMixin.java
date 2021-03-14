package moriyashiine.bewitchment.mixin.poppet;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.common.registry.BWObjects;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;
import java.util.UUID;

@SuppressWarnings("ConstantConditions")
@Mixin(Entity.class)
public abstract class EntityMixin {
	@Shadow
	public abstract UUID getUuid();
	
	@Shadow
	public abstract boolean isOnFire();
	
	@Shadow
	public World world;
	
	@Shadow
	@Final
	protected Random random;
	
	@Inject(method = "setOnFireFor", at = @At("HEAD"), cancellable = true)
	private void setOnFireFor(int seconds, CallbackInfo callbackInfo) {
		if (!world.isClient) {
			if (seconds > 0 && !isOnFire() && (Object) this instanceof PlayerEntity) {
				ItemStack poppet = BewitchmentAPI.getPoppet(world, BWObjects.VOODOO_POPPET, null, (PlayerEntity) (Object) this);
				if (!poppet.isEmpty()) {
					LivingEntity owner = BewitchmentAPI.getTaglockOwner(world, poppet);
					if (!owner.getUuid().equals(getUuid()) && !owner.isOnFire()) {
						if (poppet.damage(BewitchmentAPI.getFamiliar((PlayerEntity) (Object) this) == EntityType.WOLF && random.nextBoolean() ? 0 : 1, random, null) && poppet.getDamage() >= poppet.getMaxDamage()) {
							poppet.decrement(1);
						}
						ItemStack potentialPoppet = BewitchmentAPI.getPoppet(world, BWObjects.VOODOO_PROTECTION_POPPET, owner, null);
						if (!potentialPoppet.isEmpty()) {
							if (potentialPoppet.damage(owner instanceof PlayerEntity && BewitchmentAPI.getFamiliar((PlayerEntity) owner) == EntityType.WOLF && random.nextBoolean() ? 0 : 1, random, null) && potentialPoppet.getDamage() >= potentialPoppet.getMaxDamage()) {
								potentialPoppet.decrement(1);
							}
							return;
						}
						owner.setOnFireFor(seconds);
					}
				}
			}
		}
	}
}
