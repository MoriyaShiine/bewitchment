package moriyashiine.bewitchment.mixin;

import com.mojang.authlib.GameProfile;
import moriyashiine.bewitchment.api.interfaces.entity.*;
import moriyashiine.bewitchment.common.entity.interfaces.PolymorphAccessor;
import moriyashiine.bewitchment.common.entity.interfaces.RespawnTimerAccessor;
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
			((MagicAccessor) this).setMagic(((MagicAccessor) oldPlayer).getMagic());
			((BloodAccessor) this).setBlood(((BloodAccessor) oldPlayer).getBlood());
			PolymorphAccessor thisPoly = ((PolymorphAccessor) this);
			PolymorphAccessor oldPoly = ((PolymorphAccessor) oldPlayer);
			thisPoly.setPolymorphUUID(oldPoly.getPolymorphUUID());
			thisPoly.setPolymorphName(oldPoly.getPolymorphName());
			((RespawnTimerAccessor) this).setRespawnTimer(((RespawnTimerAccessor) oldPlayer).getRespawnTimer());
		}
		((FortuneAccessor) this).setFortune(((FortuneAccessor) oldPlayer).getFortune());
		((CurseAccessor) this).getCurses().addAll(((CurseAccessor) oldPlayer).getCurses());
		((ContractAccessor) this).getContracts().addAll(((ContractAccessor) oldPlayer).getContracts());
	}
}
