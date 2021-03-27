package moriyashiine.bewitchment.api;

import moriyashiine.bewitchment.api.interfaces.entity.MagicAccessor;
import moriyashiine.bewitchment.api.interfaces.entity.TransformationAccessor;
import moriyashiine.bewitchment.api.item.PoppetItem;
import moriyashiine.bewitchment.api.registry.AltarMapEntry;
import moriyashiine.bewitchment.common.block.entity.PoppetShelfBlockEntity;
import moriyashiine.bewitchment.common.entity.interfaces.WerewolfAccessor;
import moriyashiine.bewitchment.common.entity.living.VampireEntity;
import moriyashiine.bewitchment.common.entity.living.WerewolfEntity;
import moriyashiine.bewitchment.common.entity.living.util.BWHostileEntity;
import moriyashiine.bewitchment.common.entity.projectile.SilverArrowEntity;
import moriyashiine.bewitchment.common.item.TaglockItem;
import moriyashiine.bewitchment.common.registry.*;
import moriyashiine.bewitchment.common.world.BWUniversalWorldState;
import moriyashiine.bewitchment.common.world.BWWorldState;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.EntityDamageSource;
import net.minecraft.entity.passive.BatEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import java.util.*;

@SuppressWarnings("ConstantConditions")
public class BewitchmentAPI {
	public static final Set<AltarMapEntry> ALTAR_MAP_ENTRIES = new HashSet<>();
	
	@SuppressWarnings("InstantiationOfUtilityClass")
	public static final EntityGroup DEMON = new EntityGroup();
	
	public static LivingEntity getTaglockOwner(World world, ItemStack taglock) {
		if (world instanceof ServerWorld && (taglock.getItem() instanceof TaglockItem || taglock.getItem() instanceof PoppetItem) && TaglockItem.hasTaglock(taglock)) {
			UUID ownerUUID = TaglockItem.getTaglockUUID(taglock);
			for (ServerWorld serverWorld : world.getServer().getWorlds()) {
				Entity entity = serverWorld.getEntity(ownerUUID);
				if (entity instanceof LivingEntity) {
					return (LivingEntity) entity;
				}
			}
		}
		return null;
	}
	
	public static ItemStack getPoppet(World world, PoppetItem item, Entity owner, PlayerEntity specificInventory) {
		if (!world.isClient) {
			Map<ItemStack, PoppetShelfBlockEntity> toSearch = new HashMap<>();
			if (specificInventory != null) {
				for (int i = 0; i < specificInventory.inventory.size(); i++) {
					toSearch.put(specificInventory.inventory.getStack(i), null);
				}
			}
			else {
				for (long longPos : BWWorldState.get(world).poppetShelves) {
					BlockPos pos = BlockPos.fromLong(longPos);
					if (world.getBlockEntity(pos) instanceof PoppetShelfBlockEntity) {
						PoppetShelfBlockEntity poppetShelf = ((PoppetShelfBlockEntity) world.getBlockEntity(BlockPos.fromLong(longPos)));
						for (int i = 0; i < poppetShelf.size(); i++) {
							toSearch.put(poppetShelf.getStack(i), poppetShelf);
						}
					}
				}
				for (PlayerEntity player : ((ServerWorld) world).getPlayers()) {
					for (int i = 0; i < player.inventory.size(); i++) {
						toSearch.put(player.inventory.getStack(i), null);
					}
				}
			}
			for (ItemStack stack : toSearch.keySet()) {
				if (stack.getItem() == item && TaglockItem.hasTaglock(stack)) {
					UUID uuid = null;
					if (owner != null) {
						uuid = owner.getUuid();
					}
					else {
						LivingEntity taglockOwner = getTaglockOwner(world, stack);
						if (taglockOwner != null) {
							uuid = taglockOwner.getUuid();
						}
					}
					if (TaglockItem.getTaglockUUID(stack).equals(uuid)) {
						PoppetShelfBlockEntity poppetShelf = toSearch.get(stack);
						if (poppetShelf != null) {
							poppetShelf.markedForSync = true;
							poppetShelf.markDirty();
						}
						return stack;
					}
				}
			}
		}
		return ItemStack.EMPTY;
	}
	
	public static LivingEntity getTransformedPlayerEntity(PlayerEntity player) {
		if (BewitchmentAPI.isVampire(player, false)) {
			BatEntity entity = EntityType.BAT.create(player.world);
			entity.setRoosting(false);
			return entity;
		}
		else if (BewitchmentAPI.isWerewolf(player, false)) {
			WerewolfEntity entity = BWEntityTypes.WEREWOLF.create(player.world);
			entity.getDataTracker().set(BWHostileEntity.VARIANT, ((WerewolfAccessor) player).getWerewolfVariant());
			return entity;
		}
		return null;
	}
	
	public static EntityType<?> getFamiliar(PlayerEntity player) {
		World world = player.world;
		if (!world.isClient) {
			for (Pair<UUID, CompoundTag> pair : BWUniversalWorldState.get(world).familiars) {
				if (player.getUuid().equals(pair.getLeft())) {
					return Registry.ENTITY_TYPE.get(new Identifier(pair.getRight().getString("id")));
				}
			}
		}
		return null;
	}
	
	public static boolean usePlayerMagic(PlayerEntity player, int amount, boolean simulate) {
		if (player.world.isClient) {
			return false;
		}
		if (player.isCreative()) {
			return true;
		}
		if (player.hasStatusEffect(BWStatusEffects.INHIBITED)) {
			return false;
		}
		return ((MagicAccessor) player).drainMagic(amount, simulate);
	}
	
	public static boolean isVampire(Entity entity, boolean includeHumanForm) {
		if (entity instanceof TransformationAccessor && ((TransformationAccessor) entity).getTransformation() == BWTransformations.VAMPIRE) {
			return includeHumanForm || ((TransformationAccessor) entity).getAlternateForm();
		}
		return entity instanceof VampireEntity;
	}
	
	public static boolean isWerewolf(Entity entity, boolean includeHumanForm) {
		if (entity instanceof TransformationAccessor && ((TransformationAccessor) entity).getTransformation() == BWTransformations.WEREWOLF) {
			return includeHumanForm || ((TransformationAccessor) entity).getAlternateForm();
		}
		return entity instanceof WerewolfEntity;
	}
	
	public static boolean isSourceFromSilver(DamageSource source) {
		Entity attacker = source.getSource();
		if (source instanceof EntityDamageSource && ((EntityDamageSource) source).isThorns()) {
			return false;
		}
		return (attacker instanceof LivingEntity && isHoldingSilver((LivingEntity) attacker, Hand.MAIN_HAND)) || attacker instanceof SilverArrowEntity;
	}
	
	public static boolean isHoldingSilver(LivingEntity livingEntity, Hand hand) {
		return BWTags.SILVER_TOOLS.contains(livingEntity.getStackInHand(hand).getItem());
	}
	
	public static boolean isWeakToSilver(LivingEntity livingEntity) {
		if (BWTags.IMMUNE_TO_SILVER.contains(livingEntity.getType())) {
			return false;
		}
		return livingEntity.isUndead() || livingEntity.getGroup() == DEMON || isWerewolf(livingEntity, true) || BWTags.VULNERABLE_TO_SILVER.contains(livingEntity.getType());
	}
	
	public static boolean hasPledge(World world, UUID entity) {
		BWUniversalWorldState worldState = BWUniversalWorldState.get(world);
		for (Pair<String, List<UUID>> pair : worldState.pledges) {
			for (UUID player : pair.getRight()) {
				if (player.equals(entity)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public static boolean isPledged(World world, String pledgeable, UUID entity) {
		BWUniversalWorldState worldState = BWUniversalWorldState.get(world);
		for (Pair<String, List<UUID>> pair : worldState.pledges) {
			if (pair.getLeft().equals(pledgeable)) {
				for (UUID livingUUID : pair.getRight()) {
					if (livingUUID.equals(entity)) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public static void pledge(World world, String pledgeable, UUID entity) {
		BWUniversalWorldState worldState = BWUniversalWorldState.get(world);
		List<UUID> currentPlayers = new ArrayList<>();
		for (Pair<String, List<UUID>> pair : worldState.pledges) {
			if (pair.getLeft().equals(pledgeable)) {
				currentPlayers = pair.getRight();
				break;
			}
		}
		currentPlayers.add(entity);
		boolean found = false;
		for (int i = 0; i < worldState.pledges.size(); i++) {
			if (worldState.pledges.get(i).getLeft().equals(pledgeable)) {
				worldState.pledges.set(i, new Pair<>(pledgeable, currentPlayers));
				found = true;
				break;
			}
		}
		if (!found) {
			worldState.pledges.add(new Pair<>(pledgeable, currentPlayers));
		}
		worldState.markDirty();
	}
	
	public static void unpledge(World world, String pledgeable, UUID entity) {
		BWUniversalWorldState worldState = BWUniversalWorldState.get(world);
		for (int i = worldState.pledges.size() - 1; i >= 0; i--) {
			if (worldState.pledges.get(i).getLeft().equals(pledgeable)) {
				for (int j = worldState.pledges.get(i).getRight().size() - 1; j >= 0; j--) {
					if (worldState.pledges.get(i).getRight().get(j).equals(entity)) {
						worldState.pledges.get(i).getRight().remove(j);
					}
				}
				if (worldState.pledges.get(i).getRight().isEmpty()) {
					worldState.pledges.remove(i);
				}
			}
		}
		worldState.markDirty();
	}
	
	public static int getMoonPhase(WorldAccess world) {
		return world.getDimension().getMoonPhase(world.getLunarTime());
	}
	
	public static void registerAltarMapEntries(Block[]... altarArray) {
		for (int i = 0; i < DyeColor.values().length; i++) {
			Item carpet = null;
			switch (DyeColor.byId(i)) {
				case WHITE:
					carpet = Items.WHITE_CARPET;
					break;
				case ORANGE:
					carpet = Items.ORANGE_CARPET;
					break;
				case MAGENTA:
					carpet = Items.MAGENTA_CARPET;
					break;
				case LIGHT_BLUE:
					carpet = Items.LIGHT_BLUE_CARPET;
					break;
				case YELLOW:
					carpet = Items.YELLOW_CARPET;
					break;
				case LIME:
					carpet = Items.LIME_CARPET;
					break;
				case PINK:
					carpet = Items.PINK_CARPET;
					break;
				case GRAY:
					carpet = Items.GRAY_CARPET;
					break;
				case LIGHT_GRAY:
					carpet = Items.LIGHT_GRAY_CARPET;
					break;
				case CYAN:
					carpet = Items.CYAN_CARPET;
					break;
				case PURPLE:
					carpet = Items.PURPLE_CARPET;
					break;
				case BLUE:
					carpet = Items.BLUE_CARPET;
					break;
				case BROWN:
					carpet = Items.BROWN_CARPET;
					break;
				case GREEN:
					carpet = Items.GREEN_CARPET;
					break;
				case RED:
					carpet = Items.RED_CARPET;
					break;
				case BLACK:
					carpet = Items.BLACK_CARPET;
			}
			for (Block[] altars : altarArray) {
				ALTAR_MAP_ENTRIES.add(new AltarMapEntry(altars[0], altars[i + 1], carpet));
			}
		}
		for (Block[] altars : altarArray) {
			ALTAR_MAP_ENTRIES.add(new AltarMapEntry(altars[0], altars[17], BWObjects.HEDGEWITCH_CARPET.asItem()));
			ALTAR_MAP_ENTRIES.add(new AltarMapEntry(altars[0], altars[18], BWObjects.ALCHEMIST_CARPET.asItem()));
			ALTAR_MAP_ENTRIES.add(new AltarMapEntry(altars[0], altars[19], BWObjects.BESMIRCHED_CARPET.asItem()));
		}
	}
}
