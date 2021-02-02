package moriyashiine.bewitchment.mixin;

import moriyashiine.bewitchment.api.interfaces.entity.CurseAccessor;
import moriyashiine.bewitchment.common.block.CoffinBlock;
import moriyashiine.bewitchment.common.registry.BWCurses;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Box;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.GameRules;
import net.minecraft.world.MutableWorldProperties;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin extends World {
	@Shadow
	public abstract void setTimeOfDay(long timeOfDay);
	
	@Shadow
	protected abstract void wakeSleepingPlayers();
	
	@Shadow
	protected abstract void resetWeather();
	
	@Shadow
	@Final
	private List<ServerPlayerEntity> players;
	
	protected ServerWorldMixin(MutableWorldProperties properties, RegistryKey<World> registryRef, DimensionType dimensionType, Supplier<Profiler> profiler, boolean isClient, boolean debugWorld, long seed) {
		super(properties, registryRef, dimensionType, profiler, isClient, debugWorld, seed);
	}
	
	@Inject(method = "spawnEntity", at = @At("HEAD"))
	private void spawnEntity(Entity entity, CallbackInfoReturnable<Boolean> callbackInfo) {
		if (entity instanceof LightningEntity) {
			LivingEntity closest = null;
			for (LivingEntity found : entity.world.getEntitiesByClass(LivingEntity.class, new Box(entity.getBlockPos()).expand(256), foundEntity -> foundEntity.isAlive() && ((CurseAccessor) foundEntity).hasCurse(BWCurses.LIGHTNING_ROD))) {
				if (closest == null || found.distanceTo(entity) < closest.distanceTo(entity)) {
					closest = found;
				}
			}
			if (closest != null) {
				entity.updatePositionAndAngles(closest.getX(), closest.getY(), closest.getZ(), entity.world.random.nextFloat() * 360, 0);
			}
		}
	}
	
	@Inject(method = "tick", at = @At("HEAD"))
	private void tick(BooleanSupplier shouldKeepTicking, CallbackInfo callbackInfo) {
		boolean areAllPlayersAsleep = false;
		for (PlayerEntity playerEntity : players) {
			if (playerEntity.getSleepingPosition().isPresent() && playerEntity.isSleepingLongEnough() && getBlockState(playerEntity.getSleepingPosition().get()).getBlock() instanceof CoffinBlock) {
				areAllPlayersAsleep = true;
			}
			else {
				areAllPlayersAsleep = false;
				break;
			}
		}
		if (areAllPlayersAsleep) {
			if (getGameRules().getBoolean(GameRules.DO_DAYLIGHT_CYCLE)) {
				while (getTimeOfDay() % 24000 < 13000) {
					setTimeOfDay(getTimeOfDay() + 1);
				}
			}
			wakeSleepingPlayers();
			if (getGameRules().getBoolean(GameRules.DO_WEATHER_CYCLE)) {
				resetWeather();
			}
		}
	}
}
