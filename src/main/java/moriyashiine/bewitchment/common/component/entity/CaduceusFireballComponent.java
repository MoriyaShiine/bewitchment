/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.component.entity;

import dev.onyxstudios.cca.api.v3.component.Component;
import net.minecraft.nbt.NbtCompound;

public class CaduceusFireballComponent implements Component {
	private boolean fromCaduceus = false;

	@Override
	public void readFromNbt(NbtCompound tag) {
		setFromCaduceus(tag.getBoolean("FromCaduceus"));
	}

	@Override
	public void writeToNbt(NbtCompound tag) {
		tag.putBoolean("FromCaduceus", isFromCaduceus());
	}

	public boolean isFromCaduceus() {
		return fromCaduceus;
	}

	public void setFromCaduceus(boolean fromCaduceus) {
		this.fromCaduceus = fromCaduceus;
	}
}
