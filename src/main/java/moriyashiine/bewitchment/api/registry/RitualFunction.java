package moriyashiine.bewitchment.api.registry;

import moriyashiine.bewitchment.common.item.tool.AthameItem;
import moriyashiine.bewitchment.common.registry.BWMaterials;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class RitualFunction {
	public final ParticleType<?> startParticle;
	
	public RitualFunction(ParticleType<?> startParticle) {
		this.startParticle = startParticle;
	}
	
	public boolean isValid(World world, BlockPos pos) {
		return true;
	}
	
	public void start(World world, BlockPos pos, Inventory inventory) {
	}
	
	public void finish(World world, BlockPos pos, Inventory inventory) {
		for (int i = 0; i < inventory.size(); i++) {
			ItemStack stack = inventory.getStack(i);
			if (stack.getItem() instanceof AthameItem) {
				stack.damage(50, world.random, null);
				if (stack.getDamage() == BWMaterials.SILVER_TOOL.getDurability()) {
					stack.decrement(1);
				}
			}
			else {
				stack.decrement(1);
			}
		}
	}
	
	public void tick(World world, BlockPos pos) {
		if (world.isClient) {
			world.addParticle(ParticleTypes.END_ROD, true, pos.getX() + 0.5 + MathHelper.nextFloat(world.random, -0.2f, 0.2f), pos.getY() + 0.5 + MathHelper.nextFloat(world.random, -0.2f, 0.2f), pos.getZ() + 0.5 + MathHelper.nextFloat(world.random, -0.2f, 0.2f), 0, 0, 0);
		}
	}
}
