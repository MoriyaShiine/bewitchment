/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.client.particle;

import net.minecraft.client.particle.LargeFireSmokeParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;

public class IncenseSmokeParticle extends LargeFireSmokeParticle {
	public IncenseSmokeParticle(ClientWorld clientWorld, double posX, double posY, double posZ, double velocityX, double velocityY, double velocityZ, SpriteProvider spriteProvider) {
		super(clientWorld, posX, posY, posZ, velocityX, velocityY, velocityZ, spriteProvider);
		red = ((float) (Math.random() * 0.2) + 0.8f);
		green = ((float) (Math.random() * 0.2) + 0.8f);
		blue = ((float) (Math.random() * 0.2) + 0.8f);
	}

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
