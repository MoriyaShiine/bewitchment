package moriyashiine.bewitchment.common.entity;

import moriyashiine.bewitchment.api.entity.BroomEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.world.World;

public class JuniperBroomEntity extends BroomEntity {
	public JuniperBroomEntity(EntityType<?> type, World world) {
		super(type, world);
	}
	
	@Override
	protected float getSpeed() {
		return 1.5f;
	}
}
