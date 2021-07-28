package moriyashiine.bewitchment.mixin.fortune;

import moriyashiine.bewitchment.api.interfaces.entity.FortuneAccessor;
import moriyashiine.bewitchment.api.registry.Fortune;
import moriyashiine.bewitchment.common.registry.BWRegistries;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("ConstantConditions")
@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements FortuneAccessor {
	private Fortune.Instance fortune = null;
	
	protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}
	
	@Override
	public Fortune.Instance getFortune() {
		return fortune;
	}
	
	@Override
	public void setFortune(Fortune.Instance fortune) {
		this.fortune = fortune;
	}
	
	@Inject(method = "tick", at = @At("TAIL"))
	private void tick(CallbackInfo callbackInfo) {
		if (!world.isClient && getFortune() != null) {
			if (getFortune().fortune.tick((ServerWorld) world, (PlayerEntity) (Object) this)) {
				getFortune().duration = 0;
			}
			else {
				getFortune().duration--;
			}
			if (getFortune().duration <= 0) {
				if (getFortune().fortune.finish((ServerWorld) world, (PlayerEntity) (Object) this)) {
					setFortune(null);
				}
				else {
					getFortune().duration = world.random.nextInt(120000);
				}
			}
		}
	}
	
	@Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
	private void readCustomDataFromNbt(NbtCompound nbt, CallbackInfo callbackInfo) {
		if (nbt.contains("Fortune")) {
			setFortune(new Fortune.Instance(BWRegistries.FORTUNES.get(new Identifier(nbt.getString("Fortune"))), nbt.getInt("FortuneDuration")));
		}
	}
	
	@Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
	private void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo callbackInfo) {
		if (getFortune() != null) {
			nbt.putString("Fortune", BWRegistries.FORTUNES.getId(getFortune().fortune).toString());
			nbt.putInt("FortuneDuration", getFortune().duration);
		}
	}
}
