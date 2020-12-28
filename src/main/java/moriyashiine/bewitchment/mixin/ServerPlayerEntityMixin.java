package moriyashiine.bewitchment.mixin;

import com.mojang.authlib.GameProfile;
import moriyashiine.bewitchment.api.interfaces.BloodAccessor;
import moriyashiine.bewitchment.api.interfaces.MagicAccessor;
import moriyashiine.bewitchment.api.interfaces.PolymorphAccessor;
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
			PolymorphAccessor.of(this).ifPresent(polymorphAccessor -> {
				PolymorphAccessor.of(oldPlayer).ifPresent(oldPolymorphAccessor -> polymorphAccessor.setPolymorphUUID(oldPolymorphAccessor.getPolymorphUUID()));
				PolymorphAccessor.of(oldPlayer).ifPresent(oldPolymorphAccessor -> polymorphAccessor.setPolymorphName(oldPolymorphAccessor.getPolymorphName()));
			});
		}
	}
}
