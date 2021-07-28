package moriyashiine.bewitchment.common.misc;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.api.interfaces.entity.CurseAccessor;
import moriyashiine.bewitchment.api.interfaces.entity.Pledgeable;
import moriyashiine.bewitchment.client.network.packet.SpawnPortalParticlesPacket;
import moriyashiine.bewitchment.common.item.ScepterItem;
import moriyashiine.bewitchment.common.registry.BWCurses;
import moriyashiine.bewitchment.common.registry.BWMaterials;
import moriyashiine.bewitchment.common.registry.BWSoundEvents;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.FollowTargetGoal;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.village.TradeOfferList;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

public class BWUtil {
	public static final TradeOfferList EMPTY_TRADES = new TradeOfferList();
	
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
	
	public static FollowTargetGoal<LivingEntity> createGenericPledgeableTargetGoal(MobEntity entity) {
		return new FollowTargetGoal<>(entity, LivingEntity.class, 10, true, false, foundEntity -> {
			if (foundEntity instanceof ArmorStandEntity) {
				return false;
			}
			if (foundEntity instanceof PlayerEntity) {
				if (BewitchmentAPI.isPledged((PlayerEntity) foundEntity, ((Pledgeable) entity).getPledgeID())) {
					return false;
				}
			}
			else if (foundEntity.getGroup() == BewitchmentAPI.DEMON) {
				return false;
			}
			return BWUtil.getArmorPieces(foundEntity, stack -> stack.getItem() instanceof ArmorItem && ((ArmorItem) stack.getItem()).getMaterial() == BWMaterials.BESMIRCHED_ARMOR) < 3;
		});
	}
	
	public static boolean isTool(ItemStack stack) {
		Item item = stack.getItem();
		return item instanceof ToolItem || item instanceof RangedWeaponItem || item instanceof ScepterItem || item instanceof ShieldItem || item instanceof TridentItem;
	}
	
	public static boolean rejectTrades(LivingEntity merchant) {
		return !merchant.world.getEntitiesByClass(PlayerEntity.class, new Box(merchant.getBlockPos()).expand(8), entity -> merchant.canSee(entity) && entity.isAlive() && ((CurseAccessor) entity).hasCurse(BWCurses.APATHY)).isEmpty();
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
			if (!player.getInventory().insertStack(toAdd)) {
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
