/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.bewitchment.client.particle;

import moriyashiine.bewitchment.common.block.WitchCauldronBlock;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.BlockPos;

@Environment(EnvType.CLIENT)
public class CauldronBubbleParticle extends SpriteBillboardParticle {
	public CauldronBubbleParticle(ClientWorld clientWorld, double posX, double posY, double posZ, double velocityX, double velocityY, double velocityZ) {
		super(clientWorld, posX, posY, posZ, velocityX, velocityY, velocityZ);
		setBoundingBoxSpacing(0.02f, 0.02f);
		scale *= random.nextFloat() * 0.3 + 0.3;
		this.velocityX *= 0.1;
		this.velocityY *= 0.1;
		this.velocityZ *= 0.1;
		red = (float) velocityX;
		green = (float) velocityY;
		blue = (float) velocityZ;
		maxAge = (int) (4 / (Math.random() * 0.8 + 0.2));
	}

	@Override
	public ParticleTextureSheet getType() {
		return ParticleTextureSheet.PARTICLE_SHEET_OPAQUE;
	}

	@Override
	public void tick() {
		prevPosX = x;
		prevPosY = y;
		prevPosZ = z;
		if (maxAge-- <= 0) {
			markDead();
		} else {
			move(velocityX, velocityY, velocityZ);
			velocityX *= 0.7;
			velocityY *= 0.7;
			velocityZ *= 0.7;
			if (!(world.getBlockState(new BlockPos((int) x, (int) y, (int) z)).getBlock() instanceof WitchCauldronBlock)) {
				markDead();
			}
		}
	}

	@Environment(EnvType.CLIENT)
	public static class Factory implements ParticleFactory<DefaultParticleType> {
		private final SpriteProvider spriteProvider;

		public Factory(SpriteProvider spriteProvider) {
			this.spriteProvider = spriteProvider;
		}

		@Override
		public Particle createParticle(DefaultParticleType defaultParticleType, ClientWorld clientWorld, double posX, double posY, double posZ, double velocityX, double velocityY, double velocityZ) {
			CauldronBubbleParticle particle = new CauldronBubbleParticle(clientWorld, posX, posY, posZ, velocityX, velocityY, velocityZ);
			particle.setSprite(spriteProvider);
			return particle;
		}
	}
}
