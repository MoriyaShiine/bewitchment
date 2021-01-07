package moriyashiine.bewitchment.mixin;

import moriyashiine.bewitchment.api.interfaces.ContractAccessor;
import moriyashiine.bewitchment.common.entity.living.HellhoundEntity;
import moriyashiine.bewitchment.common.registry.BWContracts;
import moriyashiine.bewitchment.common.registry.BWEntityTypes;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.AnimalMateGoal;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.ServerWorldAccess;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AnimalMateGoal.class)
public abstract class AnimalMateGoalMixin extends Goal {
	@Shadow
	@Final
	protected AnimalEntity animal;
	
	@Shadow
	protected AnimalEntity mate;
	
	@Inject(method = "breed", at = @At("HEAD"), cancellable = true)
	private void breed(CallbackInfo callbackInfo) {
		ServerPlayerEntity player = animal.getLovingPlayer();
		if (player != null) {
			ContractAccessor.of(player).ifPresent(contractAccessor -> {
				if (contractAccessor.hasContract(BWContracts.LUST)) {
					if (contractAccessor.hasNegativeEffects() && player.getRandom().nextFloat() < 1 / 4f) {
						for (int i = 0; i < 3; i++) {
							HellhoundEntity hellhound = BWEntityTypes.HELLHOUND.create(player.world);
							if (hellhound != null) {
								hellhound.initialize((ServerWorldAccess) animal.world, animal.world.getLocalDifficulty(animal.getBlockPos()), SpawnReason.EVENT, null, null);
								hellhound.refreshPositionAndAngles(animal.getX(), animal.getY(), animal.getZ(), 0, animal.getRandom().nextInt(360));
								hellhound.setTarget(player);
								player.world.spawnEntity(hellhound);
							}
						}
						callbackInfo.cancel();
					}
					else {
						animal.breed(player.getServerWorld(), mate);
						animal.breed(player.getServerWorld(), mate);
					}
				}
			});
		}
	}
}
