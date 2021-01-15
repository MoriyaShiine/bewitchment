package moriyashiine.bewitchment.client.particle;

import moriyashiine.bewitchment.common.block.WitchCauldronBlock;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.BlockPos;

@Environment(EnvType.CLIENT)
public class IncenseSmokeParticle extends LargeFireSmokeParticle {
	public IncenseSmokeParticle(ClientWorld clientWorld, double posX, double posY, double posZ, double velocityX, double velocityY, double velocityZ, SpriteProvider spriteProvider) {
		super(clientWorld, posX, posY, posZ, velocityX, velocityY, velocityZ, spriteProvider);
		colorRed = ((float)(Math.random() * 0.20000000298023224D) + 0.8F);
		colorGreen = ((float)(Math.random() * 0.20000000298023224D) + 0.8F);
		colorBlue = ((float)(Math.random() * 0.20000000298023224D) + 0.8F);
	}
	
	@Override
	public ParticleTextureSheet getType() {
		return ParticleTextureSheet.PARTICLE_SHEET_OPAQUE;
	}
	
	@Override
	public void tick() {
		super.tick();
	}
	
	@Environment(EnvType.CLIENT)
	public static class Factory implements ParticleFactory<DefaultParticleType> {
		private final SpriteProvider spriteProvider;

		public Factory(SpriteProvider spriteProvider) {
			this.spriteProvider = spriteProvider;
		}

		public Particle createParticle(DefaultParticleType defaultParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
			return new IncenseSmokeParticle(clientWorld, d, e, f, g, h, i, this.spriteProvider);
		}
	}
}
