package moriyashiine.bewitchment.common.entity.projectile;

import moriyashiine.bewitchment.common.registry.BWEntityTypes;
import moriyashiine.bewitchment.common.registry.BWObjects;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class SilverArrowEntity extends PersistentProjectileEntity {
	public SilverArrowEntity(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
		super(entityType, world);
	}
	
	public SilverArrowEntity(EntityType<? extends PersistentProjectileEntity> type, LivingEntity owner, World world) {
		super(type, owner, world);
	}
	
	public SilverArrowEntity(World world, double x, double y, double z) {
		super(BWEntityTypes.SILVER_ARROW, x, y, z, world);
	}
	
	@Override
	protected ItemStack asItemStack() {
		return new ItemStack(BWObjects.SILVER_ARROW);
	}
}
