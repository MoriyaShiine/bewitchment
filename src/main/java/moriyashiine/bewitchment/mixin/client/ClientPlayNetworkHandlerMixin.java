package moriyashiine.bewitchment.mixin.client;

import moriyashiine.bewitchment.api.entity.BroomEntity;
import moriyashiine.bewitchment.common.entity.DragonsBloodBroomEntity;
import moriyashiine.bewitchment.common.entity.ElderBroomEntity;
import moriyashiine.bewitchment.common.entity.JuniperBroomEntity;
import moriyashiine.bewitchment.common.entity.projectile.HornedSpearEntity;
import moriyashiine.bewitchment.common.entity.projectile.SilverArrowEntity;
import moriyashiine.bewitchment.common.registry.BWEntityTypes;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {
	@Shadow
	private ClientWorld world;
	
	@Inject(method = "onEntitySpawn", at = @At("TAIL"))
	private void onEntitySpawn(EntitySpawnS2CPacket packet, CallbackInfo callbackInfo) {
		EntityType<?> type = packet.getEntityTypeId();
		double x = packet.getX();
		double y = packet.getY();
		double z = packet.getZ();
		Entity entity = null;
		if (type == BWEntityTypes.JUNIPER_BROOM) {
			entity = new JuniperBroomEntity(BWEntityTypes.JUNIPER_BROOM, world);
		}
		else if (type == BWEntityTypes.CYPRESS_BROOM) {
			entity = new BroomEntity(BWEntityTypes.CYPRESS_BROOM, world);
		}
		else if (type == BWEntityTypes.ELDER_BROOM) {
			entity = new ElderBroomEntity(BWEntityTypes.ELDER_BROOM, world);
		}
		else if (type == BWEntityTypes.DRAGONS_BLOOD_BROOM) {
			entity = new DragonsBloodBroomEntity(BWEntityTypes.DRAGONS_BLOOD_BROOM, world);
		}
		else if (type == BWEntityTypes.SILVER_ARROW) {
			entity = new SilverArrowEntity(world, x, y, z);
		}
		else if (type == BWEntityTypes.HORNED_SPEAR) {
			entity = new HornedSpearEntity(world, x, y, z);
		}
		if (entity != null) {
			if (entity instanceof PersistentProjectileEntity) {
				Entity owner = world.getEntityById(packet.getEntityData());
				if (owner != null) {
					((PersistentProjectileEntity) entity).setOwner(owner);
				}
			}
			int id = packet.getId();
			entity.updateTrackedPosition(x, y, z);
			entity.refreshPositionAfterTeleport(x, y, z);
			entity.setPitch((float) (packet.getPitch() * 360) / 256f);
			entity.setYaw((float) (packet.getYaw() * 360) / 256f);
			entity.setId(id);
			entity.setUuid(packet.getUuid());
			world.addEntity(id, entity);
		}
	}
}
