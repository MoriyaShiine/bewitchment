package moriyashiine.bewitchment.common.entity.component;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.CommonTickingComponent;
import moriyashiine.bewitchment.common.registry.BWComponents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.MathHelper;

public class FamiliarComponent implements AutoSyncedComponent, CommonTickingComponent {
	private final LivingEntity obj;
	private boolean familiar = false;
	
	public FamiliarComponent(LivingEntity obj) {
		this.obj = obj;
	}
	
	@Override
	public void readFromNbt(NbtCompound tag) {
		setFamiliar(tag.getBoolean("Familiar"));
	}
	
	@Override
	public void writeToNbt(NbtCompound tag) {
		tag.putBoolean("Familiar", isFamiliar());
	}
	
	@Override
	public void tick() {
		if (isFamiliar()) {
			if (!obj.world.isClient) {
				if (obj.age % 100 == 0) {
					obj.heal(1);
				}
			}
			else if (obj.getRandom().nextFloat() < 0.25f) {
				obj.world.addParticle(ParticleTypes.ENCHANT, obj.getParticleX(obj.getWidth()), obj.getY() + MathHelper.nextFloat(obj.getRandom(), 0, obj.getHeight()), obj.getParticleZ(obj.getWidth()), 0, 0, 0);
			}
		}
	}
	
	public boolean isFamiliar() {
		return familiar;
	}
	
	public void setFamiliar(boolean familiar) {
		this.familiar = familiar;
		BWComponents.FAMILIAR_COMPONENT.sync(obj);
	}
}
