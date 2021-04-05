package moriyashiine.bewitchment.mixin.contract;

import moriyashiine.bewitchment.api.interfaces.entity.ContractAccessor;
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
	
	@Inject(method = "breed", at = @At("HEAD"))
	private void breed(CallbackInfo callbackInfo) {
		ServerPlayerEntity player = animal.getLovingPlayer();
		if (player != null) {
			ContractAccessor contractAccessor = (ContractAccessor) player;
			if (contractAccessor.hasContract(BWContracts.LUST)) {
				if (contractAccessor.hasNegativeEffects() && player.getRandom().nextFloat() < 1 / 4f) {
					for (int i = 0; i < 3; i++) {
						HellhoundEntity hellhound = BWEntityTypes.HELLHOUND.create(player.world);
						if (hellhound != null) {
							hellhound.initialize((ServerWorldAccess) player.world, player.world.getLocalDifficulty(animal.getBlockPos()), SpawnReason.EVENT, null, null);
							hellhound.updatePositionAndAngles(animal.getX(), animal.getY(), animal.getZ(), animal.getRandom().nextFloat() * 360, 0);
							hellhound.setTarget(player);
							player.world.spawnEntity(hellhound);
						}
					}
				}
				else {
					animal.breed(player.getServerWorld(), mate);
					animal.breed(player.getServerWorld(), mate);
				}
			}
		}
	}
}
