package moriyashiine.bewitchment.common.entity.projectile;

import moriyashiine.bewitchment.client.network.packet.CreateNonLivingEntityPacket;
import moriyashiine.bewitchment.common.registry.BWObjects;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.world.World;

public class SilverArrowEntity extends PersistentProjectileEntity {
	public SilverArrowEntity(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
		super(entityType, world);
	}
	
	public SilverArrowEntity(EntityType<? extends PersistentProjectileEntity> type, LivingEntity owner, World world) {
		super(type, owner, world);
	}
	
	@Override
	protected ItemStack asItemStack() {
		return new ItemStack(BWObjects.SILVER_ARROW);
	}
	
	@Override
	public Packet<?> createSpawnPacket() {
		return CreateNonLivingEntityPacket.send(this);
	}
}
