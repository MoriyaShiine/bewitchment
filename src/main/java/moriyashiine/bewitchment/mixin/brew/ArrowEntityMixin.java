package moriyashiine.bewitchment.mixin.brew;

import moriyashiine.bewitchment.common.entity.interfaces.PolymorphAccessor;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.UUID;

@SuppressWarnings("ConstantConditions")
@Mixin(ArrowEntity.class)
public class ArrowEntityMixin implements PolymorphAccessor {
	private UUID polymorphUUID;
	private String polymorphName;
	
	@Override
	public UUID getPolymorphUUID() {
		return polymorphUUID;
	}
	
	@Override
	public void setPolymorphUUID(UUID uuid) {
		this.polymorphUUID = uuid;
	}
	
	@Override
	public String getPolymorphName() {
		return polymorphName;
	}
	
	@Override
	public void setPolymorphName(String name) {
		this.polymorphName = name;
	}
	
	@Inject(method = "asItemStack", at = @At(value = "RETURN", ordinal = 1), locals = LocalCapture.CAPTURE_FAILSOFT)
	private void asItemStack(CallbackInfoReturnable<ItemStack> callbackInfo, ItemStack stack) {
		if (getPolymorphUUID() != null) {
			stack.getOrCreateTag().putUuid("PolymorphUUID", getPolymorphUUID());
			stack.getOrCreateTag().putString("PolymorphName", getPolymorphName());
		}
	}
	
	@Inject(method = "onHit", at = @At("HEAD"))
	private void onHit(LivingEntity target, CallbackInfo callbackInfo) {
		if (getPolymorphUUID() != null && target instanceof PolymorphAccessor polymorphAccessor) {
			polymorphAccessor.setPolymorphUUID(getPolymorphUUID());
			polymorphAccessor.setPolymorphName(getPolymorphName());
		}
	}
	
	@Inject(method = "initFromStack", at = @At("TAIL"))
	private void initFromStack(ItemStack stack, CallbackInfo callbackInfo) {
		if (stack.hasTag() && stack.getTag().contains("PolymorphUUID")) {
			setPolymorphUUID(stack.getTag().getUuid("PolymorphUUID"));
			setPolymorphName(stack.getTag().getString("PolymorphName"));
		}
	}
	
	@Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
	private void readCustomDataFromNbt(NbtCompound nbt, CallbackInfo callbackInfo) {
		if (nbt.contains("PolymorphUUID")) {
			polymorphUUID = nbt.getUuid("PolymorphUUID");
			polymorphName = nbt.getString("PolymorphName");
		}
	}
	
	@Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
	private void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo callbackInfo) {
		if (polymorphUUID != null) {
			nbt.putUuid("PolymorphUUID", polymorphUUID);
			nbt.putString("PolymorphName", polymorphName);
		}
	}
}
