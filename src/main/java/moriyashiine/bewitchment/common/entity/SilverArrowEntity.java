package moriyashiine.bewitchment.common.entity;

import moriyashiine.bewitchment.client.network.message.CreateNonLivingEntityPacket;
import moriyashiine.bewitchment.common.registry.BWEntityTypes;
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
	
	private SilverArrowEntity(LivingEntity owner, World world) {
		super(BWEntityTypes.silver_arrow, owner, world);
	}
	
	@Override
	protected ItemStack asItemStack() {
		return new ItemStack(BWObjects.silver_arrow);
	}
	
	@Override
	public Packet<?> createSpawnPacket() {
		return CreateNonLivingEntityPacket.send(this);
	}
	
	public static SilverArrowEntity createWithOwner(LivingEntity owner, World world) {
		return new SilverArrowEntity(owner, world);
	}
}