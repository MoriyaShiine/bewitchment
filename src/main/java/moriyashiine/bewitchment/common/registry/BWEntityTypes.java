package moriyashiine.bewitchment.common.registry;

import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.entity.SilverArrowEntity;
import moriyashiine.bewitchment.common.entity.living.OwlEntity;
import moriyashiine.bewitchment.common.entity.living.RavenEntity;
import moriyashiine.bewitchment.common.entity.living.SnakeEntity;
import moriyashiine.bewitchment.common.entity.living.ToadEntity;
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
	
	//misc
	public static final EntityType<SilverArrowEntity> silver_arrow = create("silver_arrow", FabricEntityTypeBuilder.create(SpawnGroup.MISC, SilverArrowEntity::new).dimensions(EntityDimensions.fixed(0.5f, 0.5f)).build());
	//animal
	public static final EntityType<OwlEntity> owl = create("owl", OwlEntity.createAttributes(), FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, OwlEntity::new).dimensions(EntityDimensions.fixed(0.5f, 0.75f)).build());
	public static final EntityType<RavenEntity> raven = create("raven", RavenEntity.createAttributes(), FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, RavenEntity::new).dimensions(EntityDimensions.fixed(0.4f, 0.4f)).build());
	public static final EntityType<SnakeEntity> snake = create("snake", SnakeEntity.createAttributes(), FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, SnakeEntity::new).dimensions(EntityDimensions.fixed(0.75f, 0.25f)).build());
	public static final EntityType<ToadEntity> toad = create("toad", ToadEntity.createAttributes(), FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, ToadEntity::new).dimensions(EntityDimensions.fixed(0.5f, 0.5f)).build());
	
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
