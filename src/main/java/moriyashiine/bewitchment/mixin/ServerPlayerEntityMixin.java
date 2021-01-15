package moriyashiine.bewitchment.mixin;

import com.mojang.authlib.GameProfile;
import moriyashiine.bewitchment.api.interfaces.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity {
	public ServerPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile profile) {
		super(world, pos, yaw, profile);
	}
	
	@Inject(method = "copyFrom", at = @At("TAIL"))
	public void copyFrom(ServerPlayerEntity oldPlayer, boolean alive, CallbackInfo callbackInfo) {
		if (alive) {
			MagicAccessor.of(this).ifPresent(magicAccessor -> MagicAccessor.of(oldPlayer).ifPresent(oldMagicAccessor -> magicAccessor.setMagic(oldMagicAccessor.getMagic())));
			BloodAccessor.of(this).ifPresent(bloodAccessor -> BloodAccessor.of(oldPlayer).ifPresent(oldBloodAccessor -> bloodAccessor.setBlood(oldBloodAccessor.getBlood())));
			PolymorphAccessor.of(this).ifPresent(polymorphAccessor -> PolymorphAccessor.of(oldPlayer).ifPresent(oldPolymorphAccessor -> {
				polymorphAccessor.setPolymorphUUID(oldPolymorphAccessor.getPolymorphUUID());
				polymorphAccessor.setPolymorphName(oldPolymorphAccessor.getPolymorphName());
			}));
			RespawnTimerAccessor.of(this).ifPresent(respawnTimerAccessor -> RespawnTimerAccessor.of(oldPlayer).ifPresent(oldRespawnTimerAccessor -> respawnTimerAccessor.setRespawnTimer(oldRespawnTimerAccessor.getRespawnTimer())));
		}
		FortuneAccessor.of(this).ifPresent(fortuneAccessor -> FortuneAccessor.of(oldPlayer).ifPresent(oldFortuneAccessor -> fortuneAccessor.setFortune(oldFortuneAccessor.getFortune())));
		CurseAccessor.of(this).ifPresent(curseAccessor -> CurseAccessor.of(oldPlayer).ifPresent(oldCurseAccessor -> curseAccessor.getCurses().addAll(oldCurseAccessor.getCurses())));
		ContractAccessor.of(this).ifPresent(contractAccessor -> ContractAccessor.of(oldPlayer).ifPresent(oldContractAccessor -> contractAccessor.getContracts().addAll(oldContractAccessor.getContracts())));
	}
}
