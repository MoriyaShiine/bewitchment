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
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

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

	public static final EntityType<OwlEntity> OWL = create("owl", FabricEntityTypeBuilder.createMob().spawnGroup(SpawnGroup.CREATURE).entityFactory(OwlEntity::new).defaultAttributes(OwlEntity::createAttributes).dimensions(EntityDimensions.fixed(0.5f, 0.75f)).build());
	public static final EntityType<RavenEntity> RAVEN = create("raven", FabricEntityTypeBuilder.createMob().spawnGroup(SpawnGroup.CREATURE).entityFactory(RavenEntity::new).defaultAttributes(RavenEntity::createAttributes).dimensions(EntityDimensions.fixed(0.4f, 0.4f)).build());
	public static final EntityType<SnakeEntity> SNAKE = create("snake", FabricEntityTypeBuilder.createMob().spawnGroup(SpawnGroup.CREATURE).entityFactory(SnakeEntity::new).defaultAttributes(SnakeEntity::createAttributes).dimensions(EntityDimensions.fixed(0.75f, 0.25f)).build());
	public static final EntityType<ToadEntity> TOAD = create("toad", FabricEntityTypeBuilder.createMob().spawnGroup(SpawnGroup.CREATURE).entityFactory(ToadEntity::new).defaultAttributes(ToadEntity::createAttributes).dimensions(EntityDimensions.fixed(0.5f, 0.5f)).build());

	public static final EntityType<GhostEntity> GHOST = create("ghost", FabricEntityTypeBuilder.createMob().spawnGroup(SpawnGroup.MONSTER).entityFactory(GhostEntity::new).defaultAttributes(GhostEntity::createAttributes).dimensions(EntityDimensions.fixed(0.6f, 1.8f)).fireImmune().build());
	public static final EntityType<VampireEntity> VAMPIRE = create("vampire", FabricEntityTypeBuilder.createMob().spawnGroup(SpawnGroup.MONSTER).entityFactory(VampireEntity::new).defaultAttributes(VampireEntity::createAttributes).dimensions(EntityType.PILLAGER.getDimensions()).build());
	public static final EntityType<WerewolfEntity> WEREWOLF = create("werewolf", FabricEntityTypeBuilder.createMob().spawnGroup(SpawnGroup.MONSTER).entityFactory(WerewolfEntity::new).defaultAttributes(WerewolfEntity::createAttributes).dimensions(EntityDimensions.fixed(0.8f, 2.8f)).build());
	public static final EntityType<HellhoundEntity> HELLHOUND = create("hellhound", FabricEntityTypeBuilder.createMob().spawnGroup(SpawnGroup.MONSTER).entityFactory(HellhoundEntity::new).defaultAttributes(HellhoundEntity::createAttributes).dimensions(EntityType.WOLF.getDimensions()).fireImmune().build());
	public static final EntityType<DemonEntity> DEMON = create("demon", FabricEntityTypeBuilder.createMob().spawnGroup(SpawnGroup.MONSTER).entityFactory(DemonEntity::new).defaultAttributes(DemonEntity::createAttributes).dimensions(EntityDimensions.fixed(0.8f, 2.4f)).fireImmune().build());

	public static final EntityType<LeonardEntity> LEONARD = create("leonard", FabricEntityTypeBuilder.createMob().spawnGroup(SpawnGroup.MONSTER).entityFactory(LeonardEntity::new).defaultAttributes(LeonardEntity::createAttributes).dimensions(EntityDimensions.fixed(0.8f, 2.8f)).fireImmune().build());
	public static final EntityType<BaphometEntity> BAPHOMET = create("baphomet", FabricEntityTypeBuilder.createMob().spawnGroup(SpawnGroup.MONSTER).entityFactory(BaphometEntity::new).defaultAttributes(BaphometEntity::createAttributes).dimensions(EntityDimensions.fixed(0.8f, 2.8f)).fireImmune().build());
	public static final EntityType<LilithEntity> LILITH = create("lilith", FabricEntityTypeBuilder.createMob().spawnGroup(SpawnGroup.MONSTER).entityFactory(LilithEntity::new).defaultAttributes(LilithEntity::createAttributes).dimensions(EntityDimensions.fixed(0.8f, 2.8f)).fireImmune().build());
	public static final EntityType<HerneEntity> HERNE = create("herne", FabricEntityTypeBuilder.createMob().spawnGroup(SpawnGroup.MONSTER).entityFactory(HerneEntity::new).defaultAttributes(HerneEntity::createAttributes).dimensions(EntityDimensions.fixed(0.8f, 2.8f)).fireImmune().build());

	private static <T extends Entity> EntityType<T> create(String name, EntityType<T> type) {
		ENTITY_TYPES.put(type, new Identifier(Bewitchment.MODID, name));
		return type;
	}

	public static void init() {
		ENTITY_TYPES.keySet().forEach(entityType -> Registry.register(Registries.ENTITY_TYPE, ENTITY_TYPES.get(entityType), entityType));
	}
}
