package moriyashiine.bewitchment.api.registry;

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
	
	public void tick(World world, BlockPos pos) {
		if (world.isClient) {
			world.addParticle(ParticleTypes.END_ROD, true, pos.getX() + 0.5 + MathHelper.nextFloat(world.random, -0.2f, 0.2f), pos.getY() + 0.5 + MathHelper.nextFloat(world.random, -0.2f, 0.2f), pos.getZ() + 0.5 + MathHelper.nextFloat(world.random, -0.2f, 0.2f), 0, 0, 0);
		}
	}
	
	public void start(World world, BlockPos pos) {
	}
	
	public void finish(World world, BlockPos pos) {
	}
}
