package moriyashiine.bewitchment.common;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import net.minecraft.world.biome.Biome;

import java.util.Arrays;
import java.util.List;

@Config(name = Bewitchment.MODID)
public class BWConfig implements ConfigData {
	public final int silverSize = 6;
	public final int silverMaxHeight = 48;
	public final int silverCount = 5;
	
	public final int saltSize = 12;
	public final int saltMaxHeight = 96;
	public final int saltCount = 6;
	
	public final List<String> owlBiomeCategories = Arrays.asList(Biome.Category.TAIGA.getName(), Biome.Category.FOREST.getName());
	public final int owlWeight = 10;
	public final int owlMinGroupCount = 1;
	public final int owlMaxGroupCount = 2;
	
	public final List<String> ravenBiomeCategories = Arrays.asList(Biome.Category.PLAINS.getName(), Biome.Category.FOREST.getName());
	public final int ravenWeight = 10;
	public final int ravenMinGroupCount = 1;
	public final int ravenMaxGroupCount = 3;
	
	public final List<String> snakeBiomeCategories = Arrays.asList(Biome.Category.PLAINS.getName(), Biome.Category.SAVANNA.getName(), Biome.Category.DESERT.getName());
	public final int snakeWeight = 10;
	public final int snakeMinGroupCount = 1;
	public final int snakeMaxGroupCount = 2;
	
	public final List<String> toadBiomeCategories = Arrays.asList(Biome.Category.JUNGLE.getName(), Biome.Category.SWAMP.getName());
	public final int toadWeight = 10;
	public final int toadMinGroupCount = 1;
	public final int toadMaxGroupCount = 3;
	
	public final int ghostWeight = 8;
	public final int ghostMinGroupCount = 1;
	public final int ghostMaxGroupCount = 1;
	
	public final int vampireWeight = 10;
	public final int vampireMinGroupCount = 1;
	public final int vampireMaxGroupCount = 1;
	
	public final int werewolfWeight = 10;
	public final int werewolfMinGroupCount = 1;
	public final int werewolfMaxGroupCount = 1;
	
	public final int hellhoundWeight = 6;
	public final int hellhoundMinGroupCount = 1;
	public final int hellhoundMaxGroupCount = 1;
	
	public final boolean doDemonTradesRefresh = true;
}
