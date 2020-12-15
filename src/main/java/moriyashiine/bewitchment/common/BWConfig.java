package moriyashiine.bewitchment.common;

import me.sargunvohra.mcmods.autoconfig1u.ConfigData;
import me.sargunvohra.mcmods.autoconfig1u.annotation.Config;

@Config(name = Bewitchment.MODID)
public class BWConfig implements ConfigData {
	public final int silverSize = 6;
	public final int silverMaxHeight = 48;
	public final int silverCount = 5;
	
	public final int saltSize = 12;
	public final int saltMaxHeight = 96;
	public final int saltCount = 8;
}
