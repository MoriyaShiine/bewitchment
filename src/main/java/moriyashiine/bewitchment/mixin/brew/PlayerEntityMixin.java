package moriyashiine.bewitchment.mixin.brew;

import io.github.ladysnake.impersonate.Impersonator;
import moriyashiine.bewitchment.common.entity.interfaces.PolymorphAccessor;
import moriyashiine.bewitchment.common.registry.BWStatusEffects;
import moriyashiine.bewitchment.common.statuseffect.PolymorphStatusEffect;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;

@SuppressWarnings("ConstantConditions")
@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements PolymorphAccessor {
	private UUID polymorphUUID;
	private String polymorphName;
	
	@Shadow
	public abstract HungerManager getHungerManager();
	
	protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}
	
	@Override
	public UUID getPolymorphUUID() {
		return polymorphUUID;
	}
	
	@Override
	public void setPolymorphUUID(UUID uuid) {
		polymorphUUID = uuid;
	}
	
	@Override
	public String getPolymorphName() {
		return polymorphName;
	}
	
	@Override
	public void setPolymorphName(String name) {
		polymorphName = name;
	}
	
	@Inject(method = "tick", at = @At("TAIL"))
	private void tick(CallbackInfo callbackInfo) {
		if (!world.isClient && getPolymorphUUID() != null && !hasStatusEffect(BWStatusEffects.POLYMORPH)) {
			setPolymorphUUID(null);
			setPolymorphName(null);
			Impersonator.get((PlayerEntity) (Object) this).stopImpersonation(PolymorphStatusEffect.IMPERSONATE_IDENTIFIER);
		}
	}
	
	@Inject(method = "eatFood", at = @At("HEAD"))
	private void eatFood(World world, ItemStack stack, CallbackInfoReturnable<ItemStack> callbackInfo) {
		if (!world.isClient && hasStatusEffect(BWStatusEffects.NOURISHING)) {
			getHungerManager().add(getStatusEffect(BWStatusEffects.NOURISHING).getAmplifier() + 2, 0.5f);
		}
	}
	
	@Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
	private void readCustomDataFromNbt(NbtCompound nbt, CallbackInfo callbackInfo) {
		if (nbt.contains("PolymorphUUID")) {
			setPolymorphUUID(nbt.getUuid("PolymorphUUID"));
			setPolymorphName(nbt.getString("PolymorphName"));
		}
	}
	
	@Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
	private void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo callbackInfo) {
		if (getPolymorphUUID() != null) {
			nbt.putUuid("PolymorphUUID", getPolymorphUUID());
			nbt.putString("PolymorphName", getPolymorphName());
		}
	}
}
