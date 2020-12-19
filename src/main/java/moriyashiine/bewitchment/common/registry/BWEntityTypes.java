package moriyashiine.bewitchment.common.registry;

import com.terraformersmc.terraform.boat.TerraformBoat;
import com.terraformersmc.terraform.boat.TerraformBoatEntity;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.entity.living.OwlEntity;
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
	public static final EntityType<TerraformBoatEntity> DRAGONS_BLOOD_BOAT = create("dragons_blood_boat", FabricEntityTypeBuilder.<TerraformBoatEntity>create(SpawnGroup.MISC, (type, world) -> new TerraformBoatEntity(type, world, new TerraformBoat(BWObjects.DRAGONS_BLOOD_BOAT, BWObjects.DRAGONS_BLOOD_PLANKS.asItem(), new Identifier(Bewitchment.MODID, "textures/type/boat/dragons_blood.png")))).dimensions(EntityType.BOAT.getDimensions()).build());
	
	public static final EntityType<SilverArrowEntity> SILVER_ARROW = create("silver_arrow", FabricEntityTypeBuilder.<SilverArrowEntity>create(SpawnGroup.MISC, SilverArrowEntity::new).dimensions(EntityType.ARROW.getDimensions()).build());
	
	public static final EntityType<OwlEntity> OWL = create("owl", OwlEntity.createAttributes(), FabricEntityTypeBuilder.create(SpawnGroup.MISC, OwlEntity::new).dimensions(EntityDimensions.fixed(0.5f, 0.75f)).build());
	
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
