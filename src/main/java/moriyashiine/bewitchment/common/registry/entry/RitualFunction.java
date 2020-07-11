package moriyashiine.bewitchment.common.registry.entry;

import moriyashiine.bewitchment.common.recipe.Ritual;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class RitualFunction {
	public String getPreconditionMessage() {
		return "";
	}
	
	public boolean isValid(World world, BlockPos pos) {
		return true;
	}
	
	public void runningTick(Ritual ritual, World world, BlockPos pos) {
		if (world.isClient) {
			world.addParticle(ParticleTypes.END_ROD, true, pos.getX() + 0.5 + MathHelper.nextFloat(world.random, -0.2f, 0.2f), pos.getY() + 0.5 + MathHelper.nextFloat(world.random, -0.2f, 0.2f), pos.getZ() + 0.5 + MathHelper.nextFloat(world.random, -0.2f, 0.2f), 0, 0, 0);
		}
	}
	
	public void finish(Ritual ritual, World world, BlockPos pos) {
		if (!world.isClient) {
			world.playSound(null, pos, SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.BLOCKS, 1, 1);
			ItemStack output = ritual.output.copy();
			if (output.getItem() != Ritual.Serializer.EMPTY) {
				ItemScatterer.spawn(world, pos.getX(), pos.getY(), pos.getZ(), output);
			}
		}
	}
}