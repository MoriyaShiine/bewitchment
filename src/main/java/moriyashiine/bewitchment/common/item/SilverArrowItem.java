/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.bewitchment.common.item;

import moriyashiine.bewitchment.common.entity.projectile.SilverArrowEntity;
import moriyashiine.bewitchment.common.registry.BWEntityTypes;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.block.dispenser.ProjectileDispenserBehavior;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Position;
import net.minecraft.world.World;

public class SilverArrowItem extends ArrowItem {
	private static final DispenserBehavior DISPENSER_BEHAVIOR = new ProjectileDispenserBehavior() {
		protected ProjectileEntity createProjectile(World world, Position position, ItemStack stack) {
			SilverArrowEntity arrowEntity = new SilverArrowEntity(world, position.getX(), position.getY(), position.getZ());
			arrowEntity.pickupType = PersistentProjectileEntity.PickupPermission.ALLOWED;
			return arrowEntity;
		}
	};

	public SilverArrowItem(Settings settings) {
		super(settings);
		DispenserBlock.registerBehavior(this, DISPENSER_BEHAVIOR);
	}

	@Override
	public PersistentProjectileEntity createArrow(World world, ItemStack stack, LivingEntity shooter) {
		return new SilverArrowEntity(BWEntityTypes.SILVER_ARROW, shooter, world);
	}
}
