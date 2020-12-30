package moriyashiine.bewitchment.common;

import me.sargunvohra.mcmods.autoconfig1u.ConfigData;
import me.sargunvohra.mcmods.autoconfig1u.annotation.Config;
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
	public final int saltCount = 8;
	
	public List<String> owlBiomeCategories = Arrays.asList(Biome.Category.TAIGA.getName(), Biome.Category.FOREST.getName());
	public int owlWeight = 10;
	public int owlMinGroupCount = 1;
	public int owlMaxGroupCount = 2;
	
	public List<String> ravenBiomeCategories = Arrays.asList(Biome.Category.PLAINS.getName(), Biome.Category.FOREST.getName());
	public int ravenWeight = 10;
	public int ravenMinGroupCount = 1;
	public int ravenMaxGroupCount = 3;
	
	public List<String> snakeBiomeCategories = Arrays.asList(Biome.Category.PLAINS.getName(), Biome.Category.SAVANNA.getName(), Biome.Category.DESERT.getName());
	public int snakeWeight = 10;
	public int snakeMinGroupCount = 1;
	public int snakeMaxGroupCount = 2;
	
	public List<String> toadBiomeCategories = Arrays.asList(Biome.Category.JUNGLE.getName(), Biome.Category.SWAMP.getName());
	public int toadWeight = 10;
	public int toadMinGroupCount = 1;
	public int toadMaxGroupCount = 3;
	
	public int ghostWeight = 8;
	public int ghostMinGroupCount = 1;
	public int ghostMaxGroupCount = 1;
	
	public int blackDogWeight = 10;
	public int blackDogMinGroupCount = 1;
	public int blackDogMaxGroupCount = 1;
}
