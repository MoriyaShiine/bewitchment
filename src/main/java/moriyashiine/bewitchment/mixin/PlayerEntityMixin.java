package moriyashiine.bewitchment.mixin;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.common.entity.interfaces.BroomUserAccessor;
import moriyashiine.bewitchment.common.entity.interfaces.RespawnTimerAccessor;
import moriyashiine.bewitchment.common.registry.BWTags;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements RespawnTimerAccessor, BroomUserAccessor {
	private static final TrackedData<Boolean> PRESSING_FORWARD = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
	
	private int respawnTimer = 400;
	
	protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}
	
	@Override
	public int getRespawnTimer() {
		return respawnTimer;
	}
	
	@Override
	public void setRespawnTimer(int respawnTimer) {
		this.respawnTimer = respawnTimer;
	}
	
	@Override
	public boolean getPressingForward() {
		return dataTracker.get(PRESSING_FORWARD);
	}
	
	@Override
	public void setPressingForward(boolean pressingForward) {
		dataTracker.set(PRESSING_FORWARD, pressingForward);
	}
	
	@Inject(method = "tick", at = @At("TAIL"))
	private void tick(CallbackInfo callbackInfo) {
		if (!world.isClient && getRespawnTimer() > 0) {
			setRespawnTimer(getRespawnTimer() - 1);
		}
	}
	
	@Inject(method = "eatFood", at = @At("HEAD"))
	private void eat(World world, ItemStack stack, CallbackInfoReturnable<ItemStack> callbackInfo) {
		if (!world.isClient) {
			FoodComponent foodComponent = stack.getItem().getFoodComponent();
			if (foodComponent != null && BWTags.WITCHBERRY_FOODS.contains(stack.getItem())) {
				BewitchmentAPI.fillMagic((PlayerEntity) (Object) this, foodComponent.getHunger(), false);
			}
		}
	}
	
	@Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
	private void readCustomDataFromNbt(NbtCompound nbt, CallbackInfo callbackInfo) {
		setRespawnTimer(nbt.getInt("RespawnTimer"));
		setPressingForward(nbt.getBoolean("PressingForward"));
	}
	
	@Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
	private void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo callbackInfo) {
		nbt.putInt("RespawnTimer", getRespawnTimer());
		nbt.putBoolean("PressingForward", getPressingForward());
	}
	
	@Inject(method = "initDataTracker", at = @At("TAIL"))
	private void initDataTracker(CallbackInfo callbackInfo) {
		dataTracker.startTracking(PRESSING_FORWARD, false);
	}
}
