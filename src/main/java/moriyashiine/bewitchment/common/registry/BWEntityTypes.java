package moriyashiine.bewitchment.common.registry;

import com.terraformersmc.terraform.boat.TerraformBoat;
import com.terraformersmc.terraform.boat.TerraformBoatEntity;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.entity.living.*;
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
	
	public static final EntityType<TerraformBoatEntity> JUNIPER_BOAT = create("juniper_boat", FabricEntityTypeBuilder.<TerraformBoatEntity>create(SpawnGroup.MISC, (type, world) -> new TerraformBoatEntity(type, world, new TerraformBoat(BWObjects.JUNIPER_BOAT, BWObjects.JUNIPER_PLANKS.asItem(), new Identifier(Bewitchment.MODID, "textures/entity/boat/juniper.png")))).dimensions(EntityType.BOAT.getDimensions()).build());
	public static final EntityType<TerraformBoatEntity> CYPRESS_BOAT = create("cypress_boat", FabricEntityTypeBuilder.<TerraformBoatEntity>create(SpawnGroup.MISC, (type, world) -> new TerraformBoatEntity(type, world, new TerraformBoat(BWObjects.CYPRESS_BOAT, BWObjects.CYPRESS_PLANKS.asItem(), new Identifier(Bewitchment.MODID, "textures/entity/boat/cypress.png")))).dimensions(EntityType.BOAT.getDimensions()).build());
	public static final EntityType<TerraformBoatEntity> ELDER_BOAT = create("elder_boat", FabricEntityTypeBuilder.<TerraformBoatEntity>create(SpawnGroup.MISC, (type, world) -> new TerraformBoatEntity(type, world, new TerraformBoat(BWObjects.ELDER_BOAT, BWObjects.ELDER_PLANKS.asItem(), new Identifier(Bewitchment.MODID, "textures/entity/boat/elder.png")))).dimensions(EntityType.BOAT.getDimensions()).build());
	public static final EntityType<TerraformBoatEntity> DRAGONS_BLOOD_BOAT = create("dragons_blood_boat", FabricEntityTypeBuilder.<TerraformBoatEntity>create(SpawnGroup.MISC, (type, world) -> new TerraformBoatEntity(type, world, new TerraformBoat(BWObjects.DRAGONS_BLOOD_BOAT, BWObjects.DRAGONS_BLOOD_PLANKS.asItem(), new Identifier(Bewitchment.MODID, "textures/entity/boat/dragons_blood.png")))).dimensions(EntityType.BOAT.getDimensions()).build());
	
	public static final EntityType<SilverArrowEntity> SILVER_ARROW = create("silver_arrow", FabricEntityTypeBuilder.<SilverArrowEntity>create(SpawnGroup.MISC, SilverArrowEntity::new).dimensions(EntityType.ARROW.getDimensions()).build());
	
	public static final EntityType<OwlEntity> OWL = create("owl", OwlEntity.createAttributes(), FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, OwlEntity::new).dimensions(EntityDimensions.changing(0.5f, 0.75f)).build());
	public static final EntityType<RavenEntity> RAVEN = create("raven", RavenEntity.createAttributes(), FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, RavenEntity::new).dimensions(EntityDimensions.changing(0.4f, 0.4f)).build());
	public static final EntityType<SnakeEntity> SNAKE = create("snake", SnakeEntity.createAttributes(), FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, SnakeEntity::new).dimensions(EntityDimensions.changing(0.75f, 0.25f)).build());
	public static final EntityType<ToadEntity> TOAD = create("toad", ToadEntity.createAttributes(), FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, ToadEntity::new).dimensions(EntityDimensions.changing(0.5f, 0.5f)).build());
	
	public static final EntityType<GhostEntity> GHOST = create("ghost", GhostEntity.createAttributes(), FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, GhostEntity::new).dimensions(EntityDimensions.fixed(0.6f, 1.8f)).fireImmune().build());
	public static final EntityType<BlackDogEntity> BLACK_DOG = create("black_dog", BlackDogEntity.createAttributes(), FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, BlackDogEntity::new).dimensions(EntityDimensions.fixed(0.6f, 0.6f)).fireImmune().build());
	
	public static final EntityType<HellhoundEntity> HELLHOUND = create("hellhound", HellhoundEntity.createAttributes(), FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, HellhoundEntity::new).dimensions(EntityType.WOLF.getDimensions()).fireImmune().build());
	public static final EntityType<DemonEntity> DEMON = create("demon", DemonEntity.createAttributes(), FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, DemonEntity::new).dimensions(EntityDimensions.fixed(0.8f, 2.4f)).fireImmune().build());
	
	public static final EntityType<LeonardEntity> LEONARD = create("leonard", LeonardEntity.createAttributes(), FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, LeonardEntity::new).dimensions(EntityDimensions.fixed(0.8f, 2.8f)).fireImmune().build());
	public static final EntityType<BaphometEntity> BAPHOMET = create("baphomet", BaphometEntity.createAttributes(), FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, BaphometEntity::new).dimensions(EntityDimensions.fixed(0.8f, 2.8f)).fireImmune().build());
	
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
