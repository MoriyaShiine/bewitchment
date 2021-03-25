package moriyashiine.bewitchment.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;

@Environment(EnvType.CLIENT)
public class IncenseSmokeParticle extends LargeFireSmokeParticle {
	public IncenseSmokeParticle(ClientWorld clientWorld, double posX, double posY, double posZ, double velocityX, double velocityY, double velocityZ, SpriteProvider spriteProvider) {
		super(clientWorld, posX, posY, posZ, velocityX, velocityY, velocityZ, spriteProvider);
		colorRed = ((float) (Math.random() * 0.2) + 0.8f);
		colorGreen = ((float) (Math.random() * 0.2) + 0.8f);
		colorBlue = ((float) (Math.random() * 0.2) + 0.8f);
	}
	
	@Override
	public ParticleTextureSheet getType() {
		return ParticleTextureSheet.PARTICLE_SHEET_OPAQUE;
	}
	
	@Environment(EnvType.CLIENT)
	public static class Factory implements ParticleFactory<DefaultParticleType> {
		private final SpriteProvider spriteProvider;
		
		public Factory(SpriteProvider spriteProvider) {
			this.spriteProvider = spriteProvider;
		}
		
		@Override
		public Particle createParticle(DefaultParticleType defaultParticleType, ClientWorld clientWorld, double posX, double posY, double posZ, double velocityX, double velocityY, double velocityZ) {
			return new IncenseSmokeParticle(clientWorld, posX, posY, posZ, velocityX, velocityY, velocityZ, this.spriteProvider);
		}
	}
}
