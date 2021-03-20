package moriyashiine.bewitchment.mixin.contract;

import moriyashiine.bewitchment.api.interfaces.entity.ContractAccessor;
import moriyashiine.bewitchment.common.registry.BWContracts;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements ContractAccessor {
	@Shadow
	public abstract HungerManager getHungerManager();
	
	@Shadow public abstract boolean damage(DamageSource source, float amount);
	
	protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}
	
	@ModifyVariable(method = "addExhaustion", at = @At("HEAD"))
	private float modifyExhaustion(float exhaustion) {
		if (!world.isClient && hasNegativeEffects() && hasContract(BWContracts.GLUTTONY)) {
			exhaustion *= 1.5f;
		}
		return exhaustion;
	}
	
	@ModifyVariable(method = "addExperience", at = @At("HEAD"))
	private int modifyAddExperience(int experience) {
		if (hasContract(BWContracts.PRIDE)) {
			experience *= 2;
		}
		return experience;
	}
	
	@Inject(method = "addExperience", at = @At(value = "INVOKE", ordinal = 2, target = "Lnet/minecraft/entity/player/PlayerEntity;addExperienceLevels(I)V"))
	private void addExperience(int experience, CallbackInfo callbackInfo) {
		if (!world.isClient && hasContract(BWContracts.PRIDE) && hasNegativeEffects()) {
			damage(DamageSource.explosion(this), getMaxHealth() / 2);
			world.createExplosion(this, getX(), getY(), getZ(), 2, Explosion.DestructionType.NONE);
		}
	}
	
	@Inject(method = "eatFood", at = @At("HEAD"))
	private void eatFood(World world, ItemStack stack, CallbackInfoReturnable<ItemStack> callbackInfo) {
		if (!world.isClient) {
			FoodComponent foodComponent = stack.getItem().getFoodComponent();
			if (foodComponent != null) {
				if (hasContract(BWContracts.GLUTTONY)) {
					getHungerManager().add(foodComponent.getHunger(), foodComponent.getSaturationModifier());
				}
			}
		}
	}
}
