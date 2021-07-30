package moriyashiine.bewitchment.common.entity.component;

import dev.onyxstudios.cca.api.v3.component.ComponentV3;
import moriyashiine.bewitchment.common.registry.BWComponents;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.nbt.NbtCompound;

import java.util.Optional;

public class CaduceusFireballComponent implements ComponentV3 {
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
	
	public static CaduceusFireballComponent get(FireballEntity obj) {
		return BWComponents.CADUCEUS_FIREBALL_COMPONENT.get(obj);
	}
	
	public static Optional<CaduceusFireballComponent> maybeGet(FireballEntity obj) {
		return BWComponents.CADUCEUS_FIREBALL_COMPONENT.maybeGet(obj);
	}
}
