package moriyashiine.bewitchment.common.misc;

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
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
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
import java.util.function.Predicate;

public class BWUtil {
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
			hungerManager.addExhaustion(0.5f);
			if (!pledgedToLilith) {
				TransformationAbilityPacket.useAbility(player, true);
			}
		}
	}
	
	public static void doWerewolfLogic(PlayerEntity player, boolean alternateForm) {
		boolean forced = ((WerewolfAccessor) player).getForcedTransformation();
		if (!alternateForm && player.world.isNight() && BewitchmentAPI.getMoonPhase(player.world) == 0 && player.world.isSkyVisible(player.getBlockPos())) {
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
			if (isTool(player.getMainHandStack())) {
				player.dropStack(player.getMainHandStack().split(1));
			}
			if (isTool(player.getOffHandStack())) {
				player.dropStack(player.getOffHandStack().split(1));
			}
			if (!forced && !BewitchmentAPI.isPledged(player.world, BWPledges.HERNE, player.getUuid())) {
				TransformationAbilityPacket.useAbility(player, true);
			}
		}
	}
	
	public static boolean isTool(ItemStack stack) {
		Item item = stack.getItem();
		return item instanceof ToolItem || item instanceof RangedWeaponItem || item instanceof ScepterItem || item instanceof ShieldItem || item instanceof TridentItem;
	}
	
	public static int getArmorPieces(LivingEntity livingEntity, Predicate<ItemStack> predicate) {
		int amount = 0;
		for (EquipmentSlot slot : EquipmentSlot.values()) {
			if (slot.getType() == EquipmentSlot.Type.ARMOR && predicate.test(livingEntity.getEquippedStack(slot))) {
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
