package moriyashiine.bewitchment.api.registry;

import net.minecraft.block.Block;
import net.minecraft.item.Item;

public class AthameDropEntry {
	public final Block log, stripped_log;
	public final Item bark;
	
	public AthameDropEntry(Block log, Block stripped_log, Item bark) {
		this.log = log;
		this.stripped_log = stripped_log;
		this.bark = bark;
	}
}
