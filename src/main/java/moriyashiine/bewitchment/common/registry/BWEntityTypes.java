/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.bewitchment.common.registry;

import moriyashiine.bewitchment.api.entity.BroomEntity;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.entity.DragonsBloodBroomEntity;
import moriyashiine.bewitchment.common.entity.ElderBroomEntity;
import moriyashiine.bewitchment.common.entity.JuniperBroomEntity;
import moriyashiine.bewitchment.common.entity.living.*;
import moriyashiine.bewitchment.common.entity.projectile.HornedSpearEntity;
import moriyashiine.bewitchment.common.entity.projectile.SilverArrowEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.LinkedHashMap;
import java.util.Map;

public class BWEntityTypes {
	private static final Map<EntityType<?>, Identifier> ENTITY_TYPES = new LinkedHashMap<>();

	public static final EntityType<JuniperBroomEntity> JUNIPER_BROOM = create("juniper_broom", FabricEntityTypeBuilder.create(SpawnGroup.MISC, JuniperBroomEntity::new).dimensions(EntityType.ARROW.getDimensions()).build());
	public static final EntityType<BroomEntity> CYPRESS_BROOM = create("cypress_broom", FabricEntityTypeBuilder.create(SpawnGroup.MISC, BroomEntity::new).dimensions(JUNIPER_BROOM.getDimensions()).build());
	public static final EntityType<ElderBroomEntity> ELDER_BROOM = create("elder_broom", FabricEntityTypeBuilder.create(SpawnGroup.MISC, ElderBroomEntity::new).dimensions(JUNIPER_BROOM.getDimensions()).build());
	public static final EntityType<DragonsBloodBroomEntity> DRAGONS_BLOOD_BROOM = create("dragons_blood_broom", FabricEntityTypeBuilder.create(SpawnGroup.MISC, DragonsBloodBroomEntity::new).dimensions(JUNIPER_BROOM.getDimensions()).build());

	public static final EntityType<SilverArrowEntity> SILVER_ARROW = create("silver_arrow", FabricEntityTypeBuilder.<SilverArrowEntity>create(SpawnGroup.MISC, SilverArrowEntity::new).dimensions(EntityType.ARROW.getDimensions()).build());
	public static final EntityType<HornedSpearEntity> HORNED_SPEAR = create("horned_spear", FabricEntityTypeBuilder.<HornedSpearEntity>create(SpawnGroup.MISC, HornedSpearEntity::new).dimensions(EntityType.TRIDENT.getDimensions()).build());

	public static final EntityType<OwlEntity> OWL = create("owl", OwlEntity.createAttributes(), FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, OwlEntity::new).dimensions(EntityDimensions.changing(0.5f, 0.75f)).build());
	public static final EntityType<RavenEntity> RAVEN = create("raven", RavenEntity.createAttributes(), FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, RavenEntity::new).dimensions(EntityDimensions.changing(0.4f, 0.4f)).build());
	public static final EntityType<SnakeEntity> SNAKE = create("snake", SnakeEntity.createAttributes(), FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, SnakeEntity::new).dimensions(EntityDimensions.changing(0.75f, 0.25f)).build());
	public static final EntityType<ToadEntity> TOAD = create("toad", ToadEntity.createAttributes(), FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, ToadEntity::new).dimensions(EntityDimensions.changing(0.5f, 0.5f)).build());

	public static final EntityType<GhostEntity> GHOST = create("ghost", GhostEntity.createAttributes(), FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, GhostEntity::new).dimensions(EntityDimensions.fixed(0.6f, 1.8f)).fireImmune().build());
	public static final EntityType<VampireEntity> VAMPIRE = create("vampire", VampireEntity.createAttributes(), FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, VampireEntity::new).dimensions(EntityType.PILLAGER.getDimensions()).build());
	public static final EntityType<WerewolfEntity> WEREWOLF = create("werewolf", WerewolfEntity.createAttributes(), FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, WerewolfEntity::new).dimensions(EntityDimensions.fixed(0.8f, 2.8f)).build());
	public static final EntityType<HellhoundEntity> HELLHOUND = create("hellhound", HellhoundEntity.createAttributes(), FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, HellhoundEntity::new).dimensions(EntityType.WOLF.getDimensions()).fireImmune().build());
	public static final EntityType<DemonEntity> DEMON = create("demon", DemonEntity.createAttributes(), FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, DemonEntity::new).dimensions(EntityDimensions.fixed(0.8f, 2.4f)).fireImmune().build());

	public static final EntityType<LeonardEntity> LEONARD = create("leonard", LeonardEntity.createAttributes(), FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, LeonardEntity::new).dimensions(EntityDimensions.fixed(0.8f, 2.8f)).fireImmune().build());
	public static final EntityType<BaphometEntity> BAPHOMET = create("baphomet", BaphometEntity.createAttributes(), FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, BaphometEntity::new).dimensions(EntityDimensions.fixed(0.8f, 2.8f)).fireImmune().build());
	public static final EntityType<LilithEntity> LILITH = create("lilith", LilithEntity.createAttributes(), FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, LilithEntity::new).dimensions(EntityDimensions.fixed(0.8f, 2.8f)).fireImmune().build());
	public static final EntityType<HerneEntity> HERNE = create("herne", HerneEntity.createAttributes(), FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, HerneEntity::new).dimensions(EntityDimensions.fixed(0.8f, 2.8f)).fireImmune().build());

	private static <T extends LivingEntity> EntityType<T> create(String name, DefaultAttributeContainer.Builder attributes, EntityType<T> type) {
		FabricDefaultAttributeRegistry.register(type, attributes);
		ENTITY_TYPES.put(type, new Identifier(Bewitchment.MODID, name));
		return type;
	}

	private static <T extends Entity> EntityType<T> create(String name, EntityType<T> type) {
		ENTITY_TYPES.put(type, new Identifier(Bewitchment.MODID, name));
		return type;
	}

	public static void init() {
		ENTITY_TYPES.keySet().forEach(entityType -> Registry.register(Registry.ENTITY_TYPE, ENTITY_TYPES.get(entityType), entityType));
	}
}
