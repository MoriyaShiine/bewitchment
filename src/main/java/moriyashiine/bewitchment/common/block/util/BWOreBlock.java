package moriyashiine.bewitchment.common.block.util;

import net.minecraft.block.OreBlock;
import net.minecraft.util.math.MathHelper;

import java.util.Random;

public class BWOreBlock extends OreBlock {
	public BWOreBlock(Settings settings) {
		super(settings);
	}
	
	@Override
	protected int getExperienceWhenMined(Random random) {
		return MathHelper.nextInt(random, 0, 2);
	}
}
