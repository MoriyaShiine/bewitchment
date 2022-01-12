/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.bewitchment.api.registry;

import net.minecraft.block.Block;
import net.minecraft.item.Item;

public class AltarMapEntry {
	public final Block unformed, formed;
	public final Item carpet;

	public AltarMapEntry(Block unformed, Block formed, Item carpet) {
		this.unformed = unformed;
		this.formed = formed;
		this.carpet = carpet;
	}
}
