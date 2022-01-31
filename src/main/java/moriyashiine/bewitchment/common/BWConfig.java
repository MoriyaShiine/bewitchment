/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.bewitchment.common;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import net.minecraft.world.biome.Biome;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Config(name = Bewitchment.MODID)
public class BWConfig implements ConfigData {
	public List<String> disabledPoppets = new ArrayList<>();
	public boolean enableCurses = true;

	public int altarDistributionRadius = 24;

	public boolean generateSilver = true;
	public boolean generateSalt = true;

	public List<String> owlBiomeCategories = Arrays.asList(Biome.Category.TAIGA.getName(), Biome.Category.FOREST.getName());
	public int owlWeight = 10;
	public int owlMinGroupCount = 1;
	public int owlMaxGroupCount = 2;

	public List<String> ravenBiomeCategories = Arrays.asList(Biome.Category.PLAINS.getName(), Biome.Category.FOREST.getName());
	public int ravenWeight = 10;
	public int ravenMinGroupCount = 1;
	public int ravenMaxGroupCount = 3;

	public List<String> snakeBiomeCategories = Arrays.asList(Biome.Category.PLAINS.getName(), Biome.Category.SAVANNA.getName(), Biome.Category.DESERT.getName());
	public int snakeWeight = 6;
	public int snakeMinGroupCount = 1;
	public int snakeMaxGroupCount = 2;

	public List<String> toadBiomeCategories = Arrays.asList(Biome.Category.JUNGLE.getName(), Biome.Category.SWAMP.getName());
	public int toadWeight = 10;
	public int toadMinGroupCount = 1;
	public int toadMaxGroupCount = 3;

	public int ghostWeight = 20;
	public int ghostMinGroupCount = 1;
	public int ghostMaxGroupCount = 1;

	public int vampireWeight = 20;
	public int vampireMinGroupCount = 1;
	public int vampireMaxGroupCount = 1;

	public int werewolfWeight = 20;
	public int werewolfMinGroupCount = 1;
	public int werewolfMaxGroupCount = 1;

	public int hellhoundWeight = 6;
	public int hellhoundMinGroupCount = 1;
	public int hellhoundMaxGroupCount = 1;
}
