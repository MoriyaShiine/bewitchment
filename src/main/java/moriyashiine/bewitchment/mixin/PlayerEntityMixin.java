package moriyashiine.bewitchment.mixin;

import moriyashiine.bewitchment.api.interfaces.MagicAccessor;
import moriyashiine.bewitchment.api.interfaces.PolymorphAccessor;
import moriyashiine.bewitchment.common.registry.BWStatusEffects;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;
import java.util.UUID;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements MagicAccessor, PolymorphAccessor {
	private static final TrackedData<Integer> MAGIC = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.INTEGER);
	private static final TrackedData<Integer> MAGIC_TIMER = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.INTEGER);
	
	private static final TrackedData<Optional<UUID>> POLYMORPH_UUID = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.OPTIONAL_UUID);
	private static final TrackedData<String> POLYMORPH_NAME = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.STRING);
	
	@Shadow
	public abstract HungerManager getHungerManager();
	
	protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}
	
	@Override
	public int getMagic() {
		return dataTracker.get(MAGIC);
	}
	
	@Override
	public void setMagic(int magic) {
		dataTracker.set(MAGIC, magic);
	}
	
	@Override
	public int getMagicTimer() {
		return dataTracker.get(MAGIC_TIMER);
	}
	
	@Override
	public void setMagicTimer(int magicTimer) {
		dataTracker.set(MAGIC_TIMER, magicTimer);
	}
	
	@Override
	public Optional<UUID> getPolymorphUUID() {
		return dataTracker.get(POLYMORPH_UUID);
	}
	
	@Override
	public void setPolymorphUUID(Optional<UUID> uuid) {
		dataTracker.set(POLYMORPH_UUID, uuid);
	}
	
	@Override
	public String getPolymorphName() {
		return dataTracker.get(POLYMORPH_NAME);
	}
	
	@Override
	public void setPolymorphName(String name) {
		dataTracker.set(POLYMORPH_NAME, name);
	}
	
	@Inject(method = "tick", at = @At("TAIL"))
	private void tick(CallbackInfo callbackInfo) {
		if (getMagicTimer() > 0) {
			setMagicTimer(getMagicTimer() - 1);
		}
	}
	
	@Inject(method = "eatFood", at = @At("HEAD"))
	private void eatFood(World world, ItemStack stack, CallbackInfoReturnable<ItemStack> callbackInfo) {
		if (hasStatusEffect(BWStatusEffects.NOURISHING)) {
			//noinspection ConstantConditions
			getHungerManager().add(getStatusEffect(BWStatusEffects.NOURISHING).getAmplifier() + 2, 0.5f);
		}
	}
	
	@Inject(method = "readCustomDataFromTag", at = @At("TAIL"))
	private void readCustomDataFromTag(CompoundTag tag, CallbackInfo callbackInfo) {
		setMagic(tag.getInt("Magic"));
		setPolymorphUUID(tag.getString("PolymorphUUID").isEmpty() ? Optional.empty() : Optional.of(UUID.fromString(tag.getString("PolymorphUUID"))));
		setPolymorphName(tag.getString("PolymorphName"));
	}
	
	@Inject(method = "writeCustomDataToTag", at = @At("TAIL"))
	private void writeCustomDataToTag(CompoundTag tag, CallbackInfo callbackInfo) {
		tag.putInt("Magic", getMagic());
		boolean present = getPolymorphUUID().isPresent();
		tag.putString("PolymorphUUID", present ? getPolymorphUUID().get().toString() : "");
		tag.putString("PolymorphName", getPolymorphName());
	}
	
	@Inject(method = "initDataTracker", at = @At("TAIL"))
	private void initDataTracker(CallbackInfo callbackInfo) {
		dataTracker.startTracking(MAGIC, 0);
		dataTracker.startTracking(MAGIC_TIMER, 60);
		dataTracker.startTracking(POLYMORPH_UUID, Optional.empty());
		dataTracker.startTracking(POLYMORPH_NAME, "");
	}
}
