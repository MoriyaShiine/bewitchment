/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.registry;

import moriyashiine.bewitchment.common.Bewitchment;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.LinkedHashMap;
import java.util.Map;

public class BWParticleTypes {
	private static final Map<ParticleType<?>, Identifier> PARTICLE_TYPES = new LinkedHashMap<>();

	public static final ParticleType<DefaultParticleType> CAULDRON_BUBBLE = create("cauldron_bubble", FabricParticleTypes.simple());
	public static final ParticleType<DefaultParticleType> INCENSE_SMOKE = create("incense_smoke", FabricParticleTypes.simple());

	private static <T extends ParticleEffect> ParticleType<T> create(String name, ParticleType<T> type) {
		PARTICLE_TYPES.put(type, new Identifier(Bewitchment.MODID, name));
		return type;
	}

	public static void init() {
		PARTICLE_TYPES.keySet().forEach(particleType -> Registry.register(Registries.PARTICLE_TYPE, PARTICLE_TYPES.get(particleType), particleType));
	}
}
