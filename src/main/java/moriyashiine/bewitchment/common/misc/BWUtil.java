package moriyashiine.bewitchment.common.misc;

import com.jamieswhiteshirt.reachentityattributes.ReachEntityAttributes;
import dev.emi.nourish.NourishComponent;
import dev.emi.nourish.NourishMain;
import dev.emi.nourish.groups.NourishGroup;
import dev.emi.nourish.groups.NourishGroups;
import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.api.interfaces.entity.BloodAccessor;
import moriyashiine.bewitchment.client.network.packet.SpawnPortalParticlesPacket;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.entity.interfaces.RespawnTimerAccessor;
import moriyashiine.bewitchment.common.entity.interfaces.WerewolfAccessor;
import moriyashiine.bewitchment.common.item.ScepterItem;
import moriyashiine.bewitchment.common.network.packet.TransformationAbilityPacket;
import moriyashiine.bewitchment.common.registry.BWPledges;
import moriyashiine.bewitchment.common.registry.BWSoundEvents;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;

@SuppressWarnings("ConstantConditions")
public class BWUtil {
	private static final EntityAttributeModifier WOLF_FAMILIAR_ARMOR_MODIFIER = new EntityAttributeModifier(UUID.fromString("1b2866e6-ca04-43e4-b643-1142c0791e6d"), "Familiar modifier", 6, EntityAttributeModifier.Operation.ADDITION);
	private static final EntityAttributeModifier WOLF_FAMILIAR_ARMOR_TOUGHNESS_MODIFIER = new EntityAttributeModifier(UUID.fromString("ec7f7a2e-d5c5-40c4-9338-c2808946f7c4"), "Familiar modifier", 6, EntityAttributeModifier.Operation.ADDITION);
	
	private static final EntityAttributeModifier VAMPIRE_ATTACK_DAMAGE_MODIFIER_0 = new EntityAttributeModifier(UUID.fromString("066862f6-989c-4f35-ac6d-2696b91a1a5b"), "Transformation modifier", 2, EntityAttributeModifier.Operation.ADDITION);
	private static final EntityAttributeModifier VAMPIRE_ATTACK_DAMAGE_MODIFIER_1 = new EntityAttributeModifier(UUID.fromString("d2be3564-97e7-42c9-88c5-6b753472e37f"), "Transformation modifier", 4, EntityAttributeModifier.Operation.ADDITION);
	private static final EntityAttributeModifier VAMPIRE_MOVEMENT_SPEED_MODIFIER_0 = new EntityAttributeModifier(UUID.fromString("a782c03d-af7b-4eb7-b997-dd396bfdc7a0"), "Transformation modifier", 0.04, EntityAttributeModifier.Operation.ADDITION);
	private static final EntityAttributeModifier VAMPIRE_MOVEMENT_SPEED_MODIFIER_1 = new EntityAttributeModifier(UUID.fromString("7c7a61eb-83e8-4e85-94d6-a410a4153d6d"), "Transformation modifier", 0.08, EntityAttributeModifier.Operation.ADDITION);
	
	private static final EntityAttributeModifier WEREWOLF_ATTACK_SPEED_MODIFIER = new EntityAttributeModifier(UUID.fromString("db2512a4-655d-4843-8b06-619748a33954"), "Transformation modifier", -2, EntityAttributeModifier.Operation.ADDITION);
	private static final EntityAttributeModifier WEREWOLF_ARMOR_MODIFIER = new EntityAttributeModifier(UUID.fromString("f00b0b0f-8ad6-4a2f-bdf5-6c337ffee56c"), "Transformation modifier", 20, EntityAttributeModifier.Operation.ADDITION);
	private static final EntityAttributeModifier WEREWOLF_ATTACK_RANGE_MODIFIER = new EntityAttributeModifier(UUID.fromString("ae0e4c0a-971f-4629-99ad-60c115112c1d"), "Transformation modifier", 1, EntityAttributeModifier.Operation.ADDITION);
	private static final EntityAttributeModifier WEREWOLF_REACH_MODIFIER = new EntityAttributeModifier(UUID.fromString("4c6d90ab-41ad-4d8a-b77a-7329361d3a7b"), "Transformation modifier", 1, EntityAttributeModifier.Operation.ADDITION);
	
	private static final EntityAttributeModifier WEREWOLF_ATTACK_DAMAGE_MODIFIER_0 = new EntityAttributeModifier(UUID.fromString("06861902-ebc4-4e6e-956c-59c1ae3085c7"), "Transformation modifier", 7, EntityAttributeModifier.Operation.ADDITION);
	private static final EntityAttributeModifier WEREWOLF_ATTACK_DAMAGE_MODIFIER_1 = new EntityAttributeModifier(UUID.fromString("10c0bedf-bde5-4cae-8acc-90b1204731dd"), "Transformation modifier", 14, EntityAttributeModifier.Operation.ADDITION);
	private static final EntityAttributeModifier WEREWOLF_ARMOR_TOUGHNESS_MODIFIER_0 = new EntityAttributeModifier(UUID.fromString("44f17821-1e30-426f-81d7-cd1da88fa584"), "Transformation modifier", 10, EntityAttributeModifier.Operation.ADDITION);
	private static final EntityAttributeModifier WEREWOLF_ARMOR_TOUGHNESS_MODIFIER_1 = new EntityAttributeModifier(UUID.fromString("edfd078d-e25c-4e27-ad91-c2b32037c8be"), "Transformation modifier", 20, EntityAttributeModifier.Operation.ADDITION);
	private static final EntityAttributeModifier WEREWOLF_MOVEMENT_SPEED_MODIFIER_0 = new EntityAttributeModifier(UUID.fromString("e26a276a-86cd-44db-9091-acd42fc00d95"), "Transformation modifier", 0.08, EntityAttributeModifier.Operation.ADDITION);
	private static final EntityAttributeModifier WEREWOLF_MOVEMENT_SPEED_MODIFIER_1 = new EntityAttributeModifier(UUID.fromString("718104a6-aa19-4b53-bad9-1f9edd46d38a"), "Transformation modifier", 0.16, EntityAttributeModifier.Operation.ADDITION);
	
	
	public static void updateAttributeModifiers(PlayerEntity player) {
		boolean vampire = BewitchmentAPI.isVampire(player, true);
		boolean werewolfBeast = BewitchmentAPI.isWerewolf(player, false);
		EntityType<?> familiar = BewitchmentAPI.getFamiliar(player);
		boolean shouldHave = familiar == EntityType.WOLF;
		EntityAttributeInstance attackDamageAttribute = player.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE);
		EntityAttributeInstance attackSpeedAttribute = player.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_SPEED);
		EntityAttributeInstance armorAttribute = player.getAttributeInstance(EntityAttributes.GENERIC_ARMOR);
		EntityAttributeInstance armorToughnessAttribute = player.getAttributeInstance(EntityAttributes.GENERIC_ARMOR_TOUGHNESS);
		EntityAttributeInstance movementSpeedAttribute = player.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED);
		EntityAttributeInstance attackRange = player.getAttributeInstance(ReachEntityAttributes.ATTACK_RANGE);
		EntityAttributeInstance reach = player.getAttributeInstance(ReachEntityAttributes.REACH);
		if (shouldHave && !armorAttribute.hasModifier(WOLF_FAMILIAR_ARMOR_MODIFIER)) {
			armorAttribute.addPersistentModifier(WOLF_FAMILIAR_ARMOR_MODIFIER);
			armorToughnessAttribute.addPersistentModifier(WOLF_FAMILIAR_ARMOR_TOUGHNESS_MODIFIER);
		}
		else if (!shouldHave && armorAttribute.hasModifier(WOLF_FAMILIAR_ARMOR_MODIFIER)) {
			armorAttribute.removeModifier(WOLF_FAMILIAR_ARMOR_MODIFIER);
			armorToughnessAttribute.removeModifier(WOLF_FAMILIAR_ARMOR_TOUGHNESS_MODIFIER);
		}
		shouldHave = vampire && !BewitchmentAPI.isPledged(player.world, BWPledges.LILITH, player.getUuid());
		if (shouldHave && !attackDamageAttribute.hasModifier(VAMPIRE_ATTACK_DAMAGE_MODIFIER_0)) {
			attackDamageAttribute.addPersistentModifier(VAMPIRE_ATTACK_DAMAGE_MODIFIER_0);
			movementSpeedAttribute.addPersistentModifier(VAMPIRE_MOVEMENT_SPEED_MODIFIER_0);
		}
		else if (!shouldHave && attackDamageAttribute.hasModifier(VAMPIRE_ATTACK_DAMAGE_MODIFIER_0)) {
			attackDamageAttribute.removeModifier(VAMPIRE_ATTACK_DAMAGE_MODIFIER_0);
			movementSpeedAttribute.removeModifier(VAMPIRE_MOVEMENT_SPEED_MODIFIER_0);
		}
		shouldHave = vampire && BewitchmentAPI.isPledged(player.world, BWPledges.LILITH, player.getUuid());
		if (shouldHave && !attackDamageAttribute.hasModifier(VAMPIRE_ATTACK_DAMAGE_MODIFIER_1)) {
			attackDamageAttribute.addPersistentModifier(VAMPIRE_ATTACK_DAMAGE_MODIFIER_1);
			movementSpeedAttribute.addPersistentModifier(VAMPIRE_MOVEMENT_SPEED_MODIFIER_1);
		}
		else if (!shouldHave && attackDamageAttribute.hasModifier(VAMPIRE_ATTACK_DAMAGE_MODIFIER_1)) {
			attackDamageAttribute.removeModifier(VAMPIRE_ATTACK_DAMAGE_MODIFIER_1);
			movementSpeedAttribute.removeModifier(VAMPIRE_MOVEMENT_SPEED_MODIFIER_1);
		}
		shouldHave = werewolfBeast;
		if (shouldHave && !attackSpeedAttribute.hasModifier(WEREWOLF_ATTACK_SPEED_MODIFIER)) {
			attackSpeedAttribute.addPersistentModifier(WEREWOLF_ATTACK_SPEED_MODIFIER);
			armorAttribute.addPersistentModifier(WEREWOLF_ARMOR_MODIFIER);
			attackRange.addPersistentModifier(WEREWOLF_ATTACK_RANGE_MODIFIER);
			reach.addPersistentModifier(WEREWOLF_REACH_MODIFIER);
		}
		else if (!shouldHave && attackSpeedAttribute.hasModifier(WEREWOLF_ATTACK_SPEED_MODIFIER)) {
			attackSpeedAttribute.removeModifier(WEREWOLF_ATTACK_SPEED_MODIFIER);
			armorAttribute.removeModifier(WEREWOLF_ARMOR_MODIFIER);
			attackRange.removeModifier(WEREWOLF_ATTACK_RANGE_MODIFIER);
			reach.removeModifier(WEREWOLF_REACH_MODIFIER);
		}
		shouldHave = werewolfBeast && !BewitchmentAPI.isPledged(player.world, BWPledges.HERNE, player.getUuid());
		if (shouldHave && !attackDamageAttribute.hasModifier(WEREWOLF_ATTACK_DAMAGE_MODIFIER_0)) {
			attackDamageAttribute.addPersistentModifier(WEREWOLF_ATTACK_DAMAGE_MODIFIER_0);
			armorToughnessAttribute.addPersistentModifier(WEREWOLF_ARMOR_TOUGHNESS_MODIFIER_0);
			movementSpeedAttribute.addPersistentModifier(WEREWOLF_MOVEMENT_SPEED_MODIFIER_0);
		}
		else if (!shouldHave && attackDamageAttribute.hasModifier(WEREWOLF_ATTACK_DAMAGE_MODIFIER_0)) {
			attackDamageAttribute.removeModifier(WEREWOLF_ATTACK_DAMAGE_MODIFIER_0);
			armorToughnessAttribute.removeModifier(WEREWOLF_ARMOR_TOUGHNESS_MODIFIER_0);
			movementSpeedAttribute.removeModifier(WEREWOLF_MOVEMENT_SPEED_MODIFIER_0);
		}
		shouldHave = werewolfBeast && BewitchmentAPI.isPledged(player.world, BWPledges.HERNE, player.getUuid());
		if (shouldHave && !attackDamageAttribute.hasModifier(WEREWOLF_ATTACK_DAMAGE_MODIFIER_1)) {
			attackDamageAttribute.addPersistentModifier(WEREWOLF_ATTACK_DAMAGE_MODIFIER_1);
			armorToughnessAttribute.addPersistentModifier(WEREWOLF_ARMOR_TOUGHNESS_MODIFIER_1);
			movementSpeedAttribute.addPersistentModifier(WEREWOLF_MOVEMENT_SPEED_MODIFIER_1);
		}
		else if (!shouldHave && attackDamageAttribute.hasModifier(WEREWOLF_ATTACK_DAMAGE_MODIFIER_1)) {
			attackDamageAttribute.removeModifier(WEREWOLF_ATTACK_DAMAGE_MODIFIER_1);
			armorToughnessAttribute.removeModifier(WEREWOLF_ARMOR_TOUGHNESS_MODIFIER_1);
			movementSpeedAttribute.removeModifier(WEREWOLF_MOVEMENT_SPEED_MODIFIER_1);
		}
	}
	
	public static void doVampireLogic(PlayerEntity player, boolean alternateForm) {
		boolean pledgedToLilith = BewitchmentAPI.isPledged(player.world, BWPledges.LILITH, player.getUuid());
		player.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, Integer.MAX_VALUE, 0, true, false));
		if (((RespawnTimerAccessor) player).getRespawnTimer() <= 0 && player.world.isDay() && !player.world.isRaining() && player.world.isSkyVisible(player.getBlockPos())) {
			player.setOnFireFor(8);
		}
		HungerManager hungerManager = player.getHungerManager();
		if (((BloodAccessor) player).getBlood() > 0) {
			if (player.age % (pledgedToLilith ? 30 : 40) == 0) {
				if (player.getHealth() < player.getMaxHealth()) {
					player.heal(1);
					hungerManager.addExhaustion(3);
				}
				if ((hungerManager.isNotFull() || hungerManager.getSaturationLevel() < 10) && ((BloodAccessor) player).drainBlood(1, false)) {
					hungerManager.add(1, 20);
				}
			}
			if (Bewitchment.isNourishLoaded) {
				NourishComponent nourishComponent = NourishMain.NOURISH.get(player);
				for (NourishGroup group : NourishGroups.groups) {
					if (nourishComponent.getValue(group) != group.getDefaultValue()) {
						nourishComponent.setValue(group, group.getDefaultValue());
					}
				}
			}
		}
		else {
			if (alternateForm) {
				TransformationAbilityPacket.useAbility(player, true);
			}
			hungerManager.addExhaustion(Float.MAX_VALUE);
			if (Bewitchment.isNourishLoaded) {
				NourishComponent nourishComponent = NourishMain.NOURISH.get(player);
				for (NourishGroup group : NourishGroups.groups) {
					if (nourishComponent.getValue(group) != 0) {
						nourishComponent.setValue(group, 0);
					}
				}
			}
		}
		if (alternateForm) {
			if (!player.abilities.flying) {
				player.abilities.flying = true;
				player.sendAbilitiesUpdate();
			}
			hungerManager.addExhaustion(0.5f);
			if (!pledgedToLilith) {
				TransformationAbilityPacket.useAbility(player, true);
			}
		}
	}
	
	public static void doWerewolfLogic(PlayerEntity player, boolean alternateForm) {
		boolean forced = ((WerewolfAccessor) player).getForcedTransformation();
		if (!alternateForm && BewitchmentAPI.getMoonPhase(player.world) == 0 && player.world.isNight() && player.world.isSkyVisible(player.getBlockPos())) {
			TransformationAbilityPacket.useAbility(player, true);
			((WerewolfAccessor) player).setForcedTransformation(true);
		}
		else if (alternateForm && forced && (player.world.isDay() || BewitchmentAPI.getMoonPhase(player.world) != 0)) {
			TransformationAbilityPacket.useAbility(player, true);
			((WerewolfAccessor) player).setForcedTransformation(false);
		}
		if (alternateForm) {
			player.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, Integer.MAX_VALUE, 0, true, false));
			player.getArmorItems().forEach(stack -> player.dropStack(stack.split(1)));
			if (BWUtil.isTool(player.getMainHandStack())) {
				player.dropStack(player.getMainHandStack().split(1));
			}
			if (BWUtil.isTool(player.getOffHandStack())) {
				player.dropStack(player.getOffHandStack().split(1));
			}
			if (!forced && !BewitchmentAPI.isPledged(player.world, BWPledges.HERNE, player.getUuid())) {
				TransformationAbilityPacket.useAbility(player, true);
			}
		}
	}
	
	public static Set<BlockPos> getBlockPoses(BlockPos origin, int radius, Predicate<BlockPos> provider) {
		Set<BlockPos> blockPoses = new HashSet<>();
		BlockPos.Mutable mutable = new BlockPos.Mutable();
		for (int x = -radius; x <= radius; x++) {
			for (int y = -radius; y <= radius; y++) {
				for (int z = -radius; z <= radius; z++) {
					if (provider.test(mutable.set(origin.getX() + x, origin.getY() + y, origin.getZ() + z))) {
						blockPoses.add(mutable.toImmutable());
					}
				}
			}
		}
		return blockPoses;
	}
	
	public static BlockPos getClosestBlockPos(BlockPos origin, int radius, Predicate<BlockPos> provider) {
		BlockPos pos = null;
		for (BlockPos foundPos : getBlockPoses(origin, radius, provider)) {
			if (pos == null || foundPos.getSquaredDistance(origin) < pos.getSquaredDistance(origin)) {
				pos = foundPos;
			}
		}
		return pos;
	}
	
	public static boolean isTool(ItemStack stack) {
		Item item = stack.getItem();
		return item instanceof ToolItem || item instanceof RangedWeaponItem || item instanceof ScepterItem || item instanceof ShieldItem || item instanceof TridentItem;
	}
	
	public static int getArmorPieces(LivingEntity livingEntity, Predicate<ItemStack> predicate) {
		int amount = 0;
		for (ItemStack stack : livingEntity.getArmorItems()) {
			if (predicate.test(stack)) {
				amount++;
			}
		}
		return amount;
	}
	
	public static void addItemToInventoryAndConsume(PlayerEntity player, Hand hand, ItemStack toAdd) {
		boolean shouldAdd = false;
		ItemStack stack = player.getStackInHand(hand);
		if (stack.getCount() == 1) {
			if (player.isCreative()) {
				shouldAdd = true;
			}
			else {
				player.setStackInHand(hand, toAdd);
			}
		}
		else {
			stack.decrement(1);
			shouldAdd = true;
		}
		if (shouldAdd) {
			if (!player.inventory.insertStack(toAdd)) {
				player.dropItem(toAdd, false, true);
			}
		}
	}
	
	public static void attemptTeleport(Entity entity, BlockPos origin, int distance, boolean hasEffects) {
		for (int i = 0; i < 32; i++) {
			BlockPos.Mutable mutable = new BlockPos.Mutable(origin.getX() + MathHelper.nextDouble(entity.world.random, -distance, distance), origin.getY() + MathHelper.nextDouble(entity.world.random, -distance / 2f, distance / 2f), origin.getZ() + MathHelper.nextDouble(entity.world.random, -distance, distance));
			if (!entity.world.getBlockState(mutable).getMaterial().isSolid()) {
				while (mutable.getY() > 0 && !entity.world.getBlockState(mutable).getMaterial().isSolid()) {
					mutable.move(Direction.DOWN);
				}
				if (entity.world.getBlockState(mutable).getMaterial().blocksMovement()) {
					teleport(entity, mutable.getX() + 0.5, mutable.getY() + 0.5, mutable.getZ() + 0.5, hasEffects);
					break;
				}
			}
		}
	}
	
	public static void teleport(Entity entity, double x, double y, double z, boolean hasEffects) {
		if (hasEffects) {
			if (!entity.isSilent()) {
				entity.world.playSound(null, entity.getBlockPos(), BWSoundEvents.ENTITY_GENERIC_TELEPORT, entity.getSoundCategory(), 1, 1);
			}
			PlayerLookup.tracking(entity).forEach(playerEntity -> SpawnPortalParticlesPacket.send(playerEntity, entity));
			if (entity instanceof PlayerEntity) {
				SpawnPortalParticlesPacket.send((PlayerEntity) entity, entity);
			}
		}
		if (entity instanceof LivingEntity) {
			if (entity instanceof PlayerEntity) {
				((PlayerEntity) entity).wakeUp(true, false);
			}
			else {
				((LivingEntity) entity).wakeUp();
			}
		}
		entity.teleport(x, y + 0.5, z);
		if (hasEffects) {
			if (!entity.isSilent()) {
				entity.world.playSound(null, entity.getBlockPos(), BWSoundEvents.ENTITY_GENERIC_TELEPORT, entity.getSoundCategory(), 1, 1);
			}
			PlayerLookup.tracking(entity).forEach(playerEntity -> SpawnPortalParticlesPacket.send(playerEntity, entity));
			if (entity instanceof PlayerEntity) {
				SpawnPortalParticlesPacket.send((PlayerEntity) entity, entity);
			}
		}
	}
}
