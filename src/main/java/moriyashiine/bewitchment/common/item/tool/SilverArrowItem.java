package moriyashiine.bewitchment.common.item.tool;

import moriyashiine.bewitchment.common.entity.SilverArrowEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class SilverArrowItem extends ArrowItem {
	public SilverArrowItem(Settings settings) {
		super(settings);
	}
	
	@Override
	public PersistentProjectileEntity createArrow(World world, ItemStack stack, LivingEntity shooter) {
		return SilverArrowEntity.createWithOwner(shooter, world);
	}
}