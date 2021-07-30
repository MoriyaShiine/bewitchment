package moriyashiine.bewitchment.common.registry;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistryV3;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import moriyashiine.bewitchment.api.component.*;
import moriyashiine.bewitchment.common.Bewitchment;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;

public class BWComponents implements EntityComponentInitializer {
	public static final ComponentKey<MagicComponent> MAGIC_COMPONENT = ComponentRegistryV3.INSTANCE.getOrCreate(new Identifier(Bewitchment.MODID, "magic"), MagicComponent.class);
	public static final ComponentKey<FortuneComponent> FORTUNE_COMPONENT = ComponentRegistryV3.INSTANCE.getOrCreate(new Identifier(Bewitchment.MODID, "fortune"), FortuneComponent.class);
	public static final ComponentKey<TransformationComponent> TRANSFORMATION_COMPONENT = ComponentRegistryV3.INSTANCE.getOrCreate(new Identifier(Bewitchment.MODID, "transformation"), TransformationComponent.class);
	public static final ComponentKey<ContractsComponent> CONTRACTS_COMPONENT = ComponentRegistryV3.INSTANCE.getOrCreate(new Identifier(Bewitchment.MODID, "contracts"), ContractsComponent.class);
	public static final ComponentKey<CursesComponent> CURSES_COMPONENT = ComponentRegistryV3.INSTANCE.getOrCreate(new Identifier(Bewitchment.MODID, "curses"), CursesComponent.class);
	public static final ComponentKey<BloodComponent> BLOOD_COMPONENT = ComponentRegistryV3.INSTANCE.getOrCreate(new Identifier(Bewitchment.MODID, "blood"), BloodComponent.class);
	
	@Override
	public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
		registry.registerForPlayers(MAGIC_COMPONENT, MagicComponent::new, RespawnCopyStrategy.LOSSLESS_ONLY);
		registry.registerForPlayers(FORTUNE_COMPONENT, FortuneComponent::new, RespawnCopyStrategy.ALWAYS_COPY);
		registry.registerForPlayers(TRANSFORMATION_COMPONENT, TransformationComponent::new, RespawnCopyStrategy.ALWAYS_COPY);
		registry.registerForPlayers(CONTRACTS_COMPONENT, ContractsComponent::new, RespawnCopyStrategy.ALWAYS_COPY);
		registry.beginRegistration(LivingEntity.class, CURSES_COMPONENT).impl(CursesComponent.class).respawnStrategy(RespawnCopyStrategy.ALWAYS_COPY).end(CursesComponent::new);
		registry.beginRegistration(LivingEntity.class, BLOOD_COMPONENT).impl(BloodComponent.class).respawnStrategy(RespawnCopyStrategy.LOSSLESS_ONLY).end(BloodComponent::new);
	}
}
