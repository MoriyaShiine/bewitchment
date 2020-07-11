package moriyashiine.bewitchment.common.misc;

import blue.endless.jankson.Comment;
import io.github.cottonmc.cotton.config.annotations.ConfigFile;
import moriyashiine.bewitchment.Bewitchment;
import net.minecraft.world.biome.Biome;

import java.util.Arrays;
import java.util.List;

@ConfigFile(name = Bewitchment.MODID)
public class BWConfig {
	public static final BWConfig INSTANCE = new BWConfig();
	
	@Comment("The size of Silver Ore veins")
	public int silverOreSize = 8;
	@Comment("The amount of Silver Ore veins in a chunk")
	public int silverOreCount = 3;
	@Comment("The maximum height for Silver Ore veins to spawn")
	public int silverOreMaxHeight = 64;
	
	@Comment("The size of Salt Ore veins")
	public int saltOreSize = 8;
	@Comment("The amount of Salt Ore veins in a chunk")
	public int saltOreCount = 3;
	@Comment("The maximum height for Salt Ore veins to spawn")
	public int saltOreMaxHeight = 64;
	
	@Comment("The list of biome categories Owls can spawn in")
	public List<String> owlBiomeCategories = Arrays.asList(Biome.Category.TAIGA.getName(), Biome.Category.FOREST.getName());
	@Comment("The spawn weight of Owls")
	public int owlWeight = 10;
	@Comment("The minimum amount of Owls to spawn in a group")
	public int owlMinGroupCount = 1;
	@Comment("The maximum amount of Owls to spawn in a group")
	public int owlMaxGroupCount = 2;
	
	@Comment("The list of biome categories Ravens can spawn in")
	public List<String> ravenBiomeCategories = Arrays.asList(Biome.Category.PLAINS.getName(), Biome.Category.FOREST.getName());
	@Comment("The spawn weight of Ravens")
	public int ravenWeight = 10;
	@Comment("The minimum amount of Ravens to spawn in a group")
	public int ravenMinGroupCount = 1;
	@Comment("The maximum amount of Ravens to spawn in a group")
	public int ravenMaxGroupCount = 4;
	
	@Comment("The list of biome categories Snakes can spawn in")
	public List<String> snakeBiomeCategories = Arrays.asList(Biome.Category.PLAINS.getName(), Biome.Category.SAVANNA.getName(), Biome.Category.DESERT.getName());
	@Comment("The spawn weight of Snakes")
	public int snakeWeight = 10;
	@Comment("The minimum amount of Snakes to spawn in a group")
	public int snakeMinGroupCount = 1;
	@Comment("The maximum amount of Snakes to spawn in a group")
	public int snakeMaxGroupCount = 2;
	
	@Comment("The list of biome categories Toads can spawn in")
	public List<String> toadBiomeCategories = Arrays.asList(Biome.Category.JUNGLE.getName(), Biome.Category.SWAMP.getName());
	@Comment("The spawn weight of Toads")
	public int toadWeight = 10;
	@Comment("The minimum amount of Toads to spawn in a group")
	public int toadMinGroupCount = 1;
	@Comment("The maximum amount of Toads to spawn in a group")
	public int toadMaxGroupCount = 3;
}