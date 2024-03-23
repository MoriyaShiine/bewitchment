/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.misc;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.api.entity.Pledgeable;
import moriyashiine.bewitchment.client.packet.SpawnPortalParticlesPacket;
import moriyashiine.bewitchment.common.item.ScepterItem;
import moriyashiine.bewitchment.common.registry.BWComponents;
import moriyashiine.bewitchment.common.registry.BWCurses;
import moriyashiine.bewitchment.common.registry.BWMaterials;
import moriyashiine.bewitchment.common.registry.BWSoundEvents;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.village.TradeOfferList;

import java.util.function.Predicate;

public class BWUtil {
	public static final TradeOfferList EMPTY_TRADES = new TradeOfferList();

	public static Iterable<BlockPos> getBlockPoses(BlockPos origin, int radius) {
		return BlockPos.iterateOutwards(origin, radius, radius, radius);
	}

	public static BlockPos getClosestBlockPos(BlockPos origin, int radius, Predicate<BlockPos> provider) {
		return BlockPos.findClosest(origin, radius, radius, provider).orElse(null);
	}

	public static ActiveTargetGoal<LivingEntity> createGenericPledgeableTargetGoal(MobEntity entity) {
		return new ActiveTargetGoal<>(entity, LivingEntity.class, 10, true, false, foundEntity -> {
			if (foundEntity instanceof ArmorStandEntity) {
				return false;
			}
			if (foundEntity instanceof PlayerEntity player) {
				if (BewitchmentAPI.isPledged(player, ((Pledgeable) entity).getPledgeID())) {
					return false;
				}
			} else if (foundEntity.getGroup() == BewitchmentAPI.DEMON) {
				return false;
			}
			return BWUtil.getArmorPieces(foundEntity, stack -> stack.getItem() instanceof ArmorItem armorItem && armorItem.getMaterial() == BWMaterials.BESMIRCHED_ARMOR) < 3;
		});
	}

	public static boolean isTool(ItemStack stack) {
		Item item = stack.getItem();
		return item instanceof ToolItem || item instanceof RangedWeaponItem || item instanceof ScepterItem || item instanceof ShieldItem || item instanceof TridentItem;
	}

	public static boolean rejectTrades(LivingEntity merchant) {
		return !merchant.getWorld().getEntitiesByClass(PlayerEntity.class, new Box(merchant.getBlockPos()).expand(8), foundEntity -> merchant.canSee(foundEntity) && foundEntity.isAlive() && BWComponents.CURSES_COMPONENT.get(foundEntity).hasCurse(BWCurses.APATHY)).isEmpty();
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
			} else {
				player.setStackInHand(hand, toAdd);
			}
		} else {
			stack.decrement(1);
			shouldAdd = true;
		}
		if (shouldAdd) {
			if (!player.getInventory().insertStack(toAdd)) {
				player.dropItem(toAdd, false, true);
			}
		}
	}

	public static void attemptTeleport(Entity entity, BlockPos origin, int distance, boolean hasEffects) {
		for (int i = 0; i < 32; i++) {
			BlockPos.Mutable mutable = new BlockPos.Mutable(origin.getX() + MathHelper.nextDouble(entity.getWorld().random, -distance, distance), origin.getY() + MathHelper.nextDouble(entity.getWorld().random, -distance / 2f, distance / 2f), origin.getZ() + MathHelper.nextDouble(entity.getWorld().random, -distance, distance));
			if (!entity.getWorld().getBlockState(mutable).isSolid()) {
				while (mutable.getY() > 0 && !entity.getWorld().getBlockState(mutable).isSolid()) {
					mutable.move(Direction.DOWN);
				}
				if (entity.getWorld().getBlockState(mutable).blocksMovement()) {
					teleport(entity, mutable.getX() + 0.5, mutable.getY() + 0.5, mutable.getZ() + 0.5, hasEffects);
					break;
				}
			}
		}
	}

	public static void teleport(Entity entity, double x, double y, double z, boolean hasEffects) {
		if (hasEffects) {
			if (!entity.isSilent()) {
				entity.getWorld().playSound(null, entity.getBlockPos(), BWSoundEvents.ENTITY_GENERIC_TELEPORT, entity.getSoundCategory(), 1, 1);
			}
			PlayerLookup.tracking(entity).forEach(trackingPlayer -> SpawnPortalParticlesPacket.send(trackingPlayer, entity));
			if (entity instanceof ServerPlayerEntity player) {
				SpawnPortalParticlesPacket.send(player, entity);
			}
		}
		if (entity instanceof LivingEntity livingEntity) {
			if (entity instanceof PlayerEntity player) {
				player.wakeUp(true, false);
			} else {
				livingEntity.wakeUp();
			}
		}
		entity.teleport(x, y + 0.5, z);
		if (hasEffects) {
			if (!entity.isSilent()) {
				entity.getWorld().playSound(null, entity.getBlockPos(), BWSoundEvents.ENTITY_GENERIC_TELEPORT, entity.getSoundCategory(), 1, 1);
			}
			PlayerLookup.tracking(entity).forEach(trackingPlayer -> SpawnPortalParticlesPacket.send(trackingPlayer, entity));
			if (entity instanceof ServerPlayerEntity player) {
				SpawnPortalParticlesPacket.send(player, entity);
			}
		}
	}
}
