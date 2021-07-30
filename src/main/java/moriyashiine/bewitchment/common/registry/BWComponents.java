package moriyashiine.bewitchment.common.registry;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistryV3;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import moriyashiine.bewitchment.api.component.*;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.entity.component.*;
import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.util.Identifier;

public class BWComponents implements EntityComponentInitializer {
	public static final ComponentKey<MagicComponent> MAGIC_COMPONENT = ComponentRegistryV3.INSTANCE.getOrCreate(new Identifier(Bewitchment.MODID, "magic"), MagicComponent.class);
	public static final ComponentKey<FortuneComponent> FORTUNE_COMPONENT = ComponentRegistryV3.INSTANCE.getOrCreate(new Identifier(Bewitchment.MODID, "fortune"), FortuneComponent.class);
	public static final ComponentKey<PledgeComponent> PLEDGE_COMPONENT = ComponentRegistryV3.INSTANCE.getOrCreate(new Identifier(Bewitchment.MODID, "pledge"), PledgeComponent.class);
	public static final ComponentKey<TransformationComponent> TRANSFORMATION_COMPONENT = ComponentRegistryV3.INSTANCE.getOrCreate(new Identifier(Bewitchment.MODID, "transformation"), TransformationComponent.class);
	public static final ComponentKey<ContractsComponent> CONTRACTS_COMPONENT = ComponentRegistryV3.INSTANCE.getOrCreate(new Identifier(Bewitchment.MODID, "contracts"), ContractsComponent.class);
	public static final ComponentKey<CursesComponent> CURSES_COMPONENT = ComponentRegistryV3.INSTANCE.getOrCreate(new Identifier(Bewitchment.MODID, "curses"), CursesComponent.class);
	public static final ComponentKey<BloodComponent> BLOOD_COMPONENT = ComponentRegistryV3.INSTANCE.getOrCreate(new Identifier(Bewitchment.MODID, "blood"), BloodComponent.class);
	
	public static final ComponentKey<AdditionalWerewolfDataComponent> ADDITIONAL_WEREWOLF_DATA_COMPONENT = ComponentRegistryV3.INSTANCE.getOrCreate(new Identifier(Bewitchment.MODID, "additional_werewolf_data"), AdditionalWerewolfDataComponent.class);
	public static final ComponentKey<RespawnTimerComponent> RESPAWN_TIMER_COMPONENT = ComponentRegistryV3.INSTANCE.getOrCreate(new Identifier(Bewitchment.MODID, "respawn_timer"), RespawnTimerComponent.class);
	public static final ComponentKey<FullInvisibilityComponent> FULL_INVISIBILITY_COMPONENT = ComponentRegistryV3.INSTANCE.getOrCreate(new Identifier(Bewitchment.MODID, "full_invisibility"), FullInvisibilityComponent.class);
	public static final ComponentKey<BroomUserComponent> BROOM_USER_COMPONENT = ComponentRegistryV3.INSTANCE.getOrCreate(new Identifier(Bewitchment.MODID, "broom_user"), BroomUserComponent.class);
	public static final ComponentKey<PolymorphComponent> POLYMORPH_COMPONENT = ComponentRegistryV3.INSTANCE.getOrCreate(new Identifier(Bewitchment.MODID, "polymorph"), PolymorphComponent.class);
	public static final ComponentKey<AdditionalWaterDataComponent> ADDITIONAL_WATER_DATA_COMPONENT = ComponentRegistryV3.INSTANCE.getOrCreate(new Identifier(Bewitchment.MODID, "additional_water_data"), AdditionalWaterDataComponent.class);
	public static final ComponentKey<FamiliarComponent> FAMILIAR_COMPONENT = ComponentRegistryV3.INSTANCE.getOrCreate(new Identifier(Bewitchment.MODID, "familiar"), FamiliarComponent.class);
	public static final ComponentKey<MinionComponent> MINION_COMPONENT = ComponentRegistryV3.INSTANCE.getOrCreate(new Identifier(Bewitchment.MODID, "minion"), MinionComponent.class);
	public static final ComponentKey<FakeMobComponent> FAKE_MOB_COMPONENT = ComponentRegistryV3.INSTANCE.getOrCreate(new Identifier(Bewitchment.MODID, "fake_mob"), FakeMobComponent.class);
	public static final ComponentKey<WerewolfVillagerComponent> WEREWOLF_VILLAGER_COMPONENT = ComponentRegistryV3.INSTANCE.getOrCreate(new Identifier(Bewitchment.MODID, "werewolf_villager"), WerewolfVillagerComponent.class);
	public static final ComponentKey<CaduceusFireballComponent> CADUCEUS_FIREBALL_COMPONENT = ComponentRegistryV3.INSTANCE.getOrCreate(new Identifier(Bewitchment.MODID, "caduceus_fireball"), CaduceusFireballComponent.class);
	
	@Override
	public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
		registry.registerForPlayers(MAGIC_COMPONENT, MagicComponent::new, RespawnCopyStrategy.LOSSLESS_ONLY);
		registry.registerForPlayers(FORTUNE_COMPONENT, FortuneComponent::new, RespawnCopyStrategy.ALWAYS_COPY);
		registry.registerForPlayers(PLEDGE_COMPONENT, PledgeComponent::new, RespawnCopyStrategy.ALWAYS_COPY);
		registry.registerForPlayers(TRANSFORMATION_COMPONENT, TransformationComponent::new, RespawnCopyStrategy.ALWAYS_COPY);
		registry.registerForPlayers(CONTRACTS_COMPONENT, ContractsComponent::new, RespawnCopyStrategy.ALWAYS_COPY);
		registry.beginRegistration(LivingEntity.class, CURSES_COMPONENT).impl(CursesComponent.class).respawnStrategy(RespawnCopyStrategy.ALWAYS_COPY).end(CursesComponent::new);
		registry.beginRegistration(LivingEntity.class, BLOOD_COMPONENT).impl(BloodComponent.class).respawnStrategy(RespawnCopyStrategy.LOSSLESS_ONLY).end(BloodComponent::new);
		
		registry.registerForPlayers(ADDITIONAL_WEREWOLF_DATA_COMPONENT, AdditionalWerewolfDataComponent::new, RespawnCopyStrategy.ALWAYS_COPY);
		registry.registerForPlayers(RESPAWN_TIMER_COMPONENT, player -> new RespawnTimerComponent(), RespawnCopyStrategy.LOSSLESS_ONLY);
		registry.registerForPlayers(FULL_INVISIBILITY_COMPONENT, FullInvisibilityComponent::new, RespawnCopyStrategy.LOSSLESS_ONLY);
		registry.registerForPlayers(BROOM_USER_COMPONENT, BroomUserComponent::new, RespawnCopyStrategy.NEVER_COPY);
		registry.registerForPlayers(POLYMORPH_COMPONENT, PolymorphComponent::new, RespawnCopyStrategy.LOSSLESS_ONLY);
		registry.beginRegistration(ArrowEntity.class, POLYMORPH_COMPONENT).impl(PolymorphComponent.class).respawnStrategy(RespawnCopyStrategy.ALWAYS_COPY).end(PolymorphComponent::new);
		registry.beginRegistration(AreaEffectCloudEntity.class, POLYMORPH_COMPONENT).impl(PolymorphComponent.class).respawnStrategy(RespawnCopyStrategy.ALWAYS_COPY).end(PolymorphComponent::new);
		registry.beginRegistration(Entity.class, ADDITIONAL_WATER_DATA_COMPONENT).impl(AdditionalWaterDataComponent.class).respawnStrategy(RespawnCopyStrategy.LOSSLESS_ONLY).end(AdditionalWaterDataComponent::new);
		registry.beginRegistration(LivingEntity.class, FAMILIAR_COMPONENT).impl(FamiliarComponent.class).respawnStrategy(RespawnCopyStrategy.ALWAYS_COPY).end(FamiliarComponent::new);
		registry.beginRegistration(MobEntity.class, MINION_COMPONENT).impl(MinionComponent.class).respawnStrategy(RespawnCopyStrategy.ALWAYS_COPY).end(MinionComponent::new);
		registry.beginRegistration(MobEntity.class, FAKE_MOB_COMPONENT).impl(FakeMobComponent.class).respawnStrategy(RespawnCopyStrategy.ALWAYS_COPY).end(FakeMobComponent::new);
		registry.beginRegistration(VillagerEntity.class, WEREWOLF_VILLAGER_COMPONENT).impl(WerewolfVillagerComponent.class).respawnStrategy(RespawnCopyStrategy.ALWAYS_COPY).end(WerewolfVillagerComponent::new);
		registry.beginRegistration(FireballEntity.class, CADUCEUS_FIREBALL_COMPONENT).impl(CaduceusFireballComponent.class).respawnStrategy(RespawnCopyStrategy.ALWAYS_COPY).end(fireballEntity -> new CaduceusFireballComponent());
	}
}
