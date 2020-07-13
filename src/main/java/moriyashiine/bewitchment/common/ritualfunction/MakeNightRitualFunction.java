package moriyashiine.bewitchment.common.ritualfunction;

import moriyashiine.bewitchment.common.recipe.Ritual;
import moriyashiine.bewitchment.common.registry.entry.RitualFunction;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;

public class MakeNightRitualFunction extends RitualFunction {
	@Override
	public String getPreconditionMessage() {
		return "ritual.precondition.day";
	}
	
	@Override
	public boolean isValid(World world, BlockPos pos) {
		return world.isDay() && world.getLevelProperties().getGameRules().getBoolean(GameRules.DO_DAYLIGHT_CYCLE);
	}
	
	public void runningTick(Ritual ritual, World world, BlockPos pos) {
		if (world.isClient) {
			world.addParticle(ParticleTypes.ENCHANTED_HIT, true, pos.getX() + 0.5 + MathHelper.nextFloat(world.random, -0.2f, 0.2f), pos.getY() + 0.5 + MathHelper.nextFloat(world.random, -0.2f, 0.2f), pos.getZ() + 0.5 + MathHelper.nextFloat(world.random, -0.2f, 0.2f), 0, 0, 0);
		}
	}
	
	public void finish(Ritual ritual, World world, BlockPos pos) {
		super.finish(ritual, world, pos);
		if (world instanceof ServerWorld) {
			ServerWorld serverWorld = ((ServerWorld) world);
			while (world.getTimeOfDay() % 24000 < 13000) {
				serverWorld.method_29199(world.getTimeOfDay() + 1);
			}
		}
	}
}