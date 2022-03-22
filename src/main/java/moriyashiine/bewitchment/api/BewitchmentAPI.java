/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.bewitchment.api;

import com.mojang.authlib.GameProfile;
import moriyashiine.bewitchment.api.component.PledgeComponent;
import moriyashiine.bewitchment.api.component.TransformationComponent;
import moriyashiine.bewitchment.api.item.PoppetItem;
import moriyashiine.bewitchment.api.misc.PoppetData;
import moriyashiine.bewitchment.api.registry.AltarMapEntry;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.block.entity.PoppetShelfBlockEntity;
import moriyashiine.bewitchment.common.entity.living.VampireEntity;
import moriyashiine.bewitchment.common.entity.living.WerewolfEntity;
import moriyashiine.bewitchment.common.entity.living.util.BWHostileEntity;
import moriyashiine.bewitchment.common.entity.projectile.SilverArrowEntity;
import moriyashiine.bewitchment.common.item.AthameItem;
import moriyashiine.bewitchment.common.item.TaglockItem;
import moriyashiine.bewitchment.common.registry.*;
import moriyashiine.bewitchment.common.world.BWUniversalWorldState;
import moriyashiine.bewitchment.common.world.BWWorldState;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.passive.BatEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.NetworkSide;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings("ConstantConditions")
public class BewitchmentAPI {
	public static final Set<AltarMapEntry> ALTAR_MAP_ENTRIES = new HashSet<>();

	@SuppressWarnings("InstantiationOfUtilityClass")
	public static final EntityGroup DEMON = new EntityGroup();

	public static ServerPlayerEntity fakePlayer = null;

	public static LivingEntity getTaglockOwner(World world, ItemStack taglock) {
		if (world instanceof ServerWorld && (taglock.getItem() instanceof TaglockItem || taglock.getItem() instanceof PoppetItem) && TaglockItem.hasTaglock(taglock)) {
			UUID ownerUUID = TaglockItem.getTaglockUUID(taglock);
			for (ServerWorld serverWorld : world.getServer().getWorlds()) {
				if (serverWorld.getEntity(ownerUUID) instanceof LivingEntity livingEntity && livingEntity.isAlive()) {
					return livingEntity;
				}
			}
		}
		return null;
	}

	public static PoppetData getPoppetFromInventory(World world, PoppetItem item, Entity owner, List<ItemStack> inventory) {
		if (inventory == null) {
			return PoppetData.EMPTY;
		}
		for (int i = 0; i < inventory.size(); i++) {
			ItemStack stack = inventory.get(i);
			if (Bewitchment.config.disabledPoppets.contains(Registry.ITEM.getId(stack.getItem()).toString())) {
				continue;
			}
			if (stack.getItem() == item && TaglockItem.hasTaglock(stack)) {
				UUID uuid = null;
				if (owner != null) {
					uuid = owner.getUuid();
				} else {
					LivingEntity taglockOwner = getTaglockOwner(world, stack);
					if (taglockOwner != null) {
						uuid = taglockOwner.getUuid();
					}
				}
				if (TaglockItem.getTaglockUUID(stack).equals(uuid)) {
					return new PoppetData(stack, null, i);
				}
			}
		}
		return PoppetData.EMPTY;
	}

	public static PoppetData getPoppet(World world, PoppetItem item, Entity owner) {
		if (world.isClient) {
			return PoppetData.EMPTY;
		}
		if (item.worksInShelf) {
			for (Map.Entry<Long, DefaultedList<ItemStack>> entry : BWWorldState.get(world).poppetShelves.entrySet()) {
				PoppetData result = getPoppetFromInventory(world, item, owner, entry.getValue());
				if (result != PoppetData.EMPTY) {
					BlockPos pos = BlockPos.fromLong(entry.getKey());
					if (world.isChunkLoaded(pos) && world.getBlockEntity(pos) instanceof PoppetShelfBlockEntity poppetShelf) {
						poppetShelf.sync();
					}
					return new PoppetData(result.stack, entry.getKey(), result.index);
				}
			}
		}
		for (PlayerEntity player : ((ServerWorld) world).getPlayers()) {
			PoppetData result = getPoppetFromInventory(world, item, owner, Stream.concat(player.getInventory().main.stream(), player.getInventory().offHand.stream()).collect(Collectors.toList()));
			if (result != PoppetData.EMPTY) {
				return result;
			}
		}
		return PoppetData.EMPTY;
	}

	public static ServerPlayerEntity getFakePlayer(World world) {
		if (!world.isClient) {
			if (fakePlayer == null) {
				fakePlayer = new ServerPlayerEntity(world.getServer(), (ServerWorld) world, new GameProfile(UUID.randomUUID(), "FAKE_PLAYER"));
				fakePlayer.networkHandler = new ServerPlayNetworkHandler(world.getServer(), new ClientConnection(NetworkSide.SERVERBOUND), fakePlayer);
				ItemStack axe = new ItemStack(Items.WOODEN_AXE);
				axe.getOrCreateNbt().putBoolean("Unbreakable", true);
				fakePlayer.setStackInHand(Hand.MAIN_HAND, axe);
			}
			return fakePlayer;
		}
		return null;
	}

	public static LivingEntity getTransformedPlayerEntity(PlayerEntity player) {
		if (BewitchmentAPI.isVampire(player, false)) {
			BatEntity entity = EntityType.BAT.create(player.world);
			entity.setRoosting(false);
			return entity;
		} else if (BewitchmentAPI.isWerewolf(player, false)) {
			WerewolfEntity entity = BWEntityTypes.WEREWOLF.create(player.world);
			entity.getDataTracker().set(BWHostileEntity.VARIANT, BWComponents.ADDITIONAL_WEREWOLF_DATA_COMPONENT.get(player).getVariant());
			return entity;
		}
		return null;
	}

	public static EntityType<?> getFamiliar(PlayerEntity player) {
		if (!player.world.isClient) {
			BWUniversalWorldState universalWorldState = BWUniversalWorldState.get(player.world);
			for (Pair<UUID, NbtCompound> pair : universalWorldState.familiars) {
				if (player.getUuid().equals(pair.getLeft())) {
					return Registry.ENTITY_TYPE.get(new Identifier(pair.getRight().getString("id")));
				}
			}
		}
		return null;
	}

	public static boolean fillMagic(PlayerEntity player, int amount, boolean simulate) {
		if (player.world.isClient) {
			return false;
		}
		return BWComponents.MAGIC_COMPONENT.get(player).fillMagic(amount, simulate);
	}

	public static boolean drainMagic(PlayerEntity player, int amount, boolean simulate) {
		if (player.world.isClient) {
			return false;
		}
		if (player.isCreative()) {
			return true;
		}
		if (player.hasStatusEffect(BWStatusEffects.INHIBITED)) {
			return false;
		}
		return BWComponents.MAGIC_COMPONENT.get(player).drainMagic(amount, simulate);
	}

	public static boolean isVampire(Entity entity, boolean includeHumanForm) {
		if (entity instanceof PlayerEntity player) {
			TransformationComponent transformationComponent = BWComponents.TRANSFORMATION_COMPONENT.get(player);
			if (transformationComponent.getTransformation() == BWTransformations.VAMPIRE) {
				return includeHumanForm || transformationComponent.isAlternateForm();
			}
		}
		return entity instanceof VampireEntity;
	}

	public static boolean isWerewolf(Entity entity, boolean includeHumanForm) {
		if (entity instanceof PlayerEntity player) {
			TransformationComponent transformationComponent = BWComponents.TRANSFORMATION_COMPONENT.get(player);
			if (transformationComponent.getTransformation() == BWTransformations.WEREWOLF) {
				return includeHumanForm || transformationComponent.isAlternateForm();
			}
		}
		return entity instanceof WerewolfEntity;
	}

	public static boolean isSourceFromSilver(DamageSource source) {
		if (source.getSource() instanceof LivingEntity livingEntity && livingEntity.getMainHandStack().getItem() instanceof AthameItem) {
			return true;
		}
		return source.getSource() instanceof SilverArrowEntity;
	}

	public static boolean isWeakToSilver(LivingEntity livingEntity) {
		if (livingEntity.getType().isIn(BWTags.IMMUNE_TO_SILVER)) {
			return false;
		}
		return livingEntity.isUndead() || livingEntity.getGroup() == DEMON || livingEntity.getType().isIn(BWTags.VULNERABLE_TO_SILVER);
	}

	public static boolean isPledged(PlayerEntity player, String pledge) {
		PledgeComponent pledgeComponent = BWComponents.PLEDGE_COMPONENT.get(player);
		if (!player.world.isClient) {
			BWUniversalWorldState universalWorldState = BWUniversalWorldState.get(player.world);
			for (int i = universalWorldState.pledgesToRemove.size() - 1; i >= 0; i--) {
				if (universalWorldState.pledgesToRemove.get(i).equals(player.getUuid())) {
					pledgeComponent.setPledgeNextTick(BWPledges.NONE);
					universalWorldState.pledgesToRemove.remove(i);
					return false;
				}
			}
		}
		return pledgeComponent.getPledge().equals(pledge);
	}

	public static boolean hasVoodooProtection(LivingEntity living, int damage) {
		PoppetData poppetData = BewitchmentAPI.getPoppet(living.world, BWObjects.VOODOO_PROTECTION_POPPET, living);
		if (!poppetData.stack.isEmpty()) {
			boolean sync = false;
			if (poppetData.stack.damage(damage, living.getRandom(), null) && poppetData.stack.getDamage() >= poppetData.stack.getMaxDamage()) {
				poppetData.stack.decrement(1);
				sync = true;
			}
			poppetData.update(living.world, sync);
			return true;
		}
		return false;
	}

	public static void unpledge(PlayerEntity player) {
		BWComponents.PLEDGE_COMPONENT.get(player).setPledge(BWPledges.NONE);
	}

	public static int getMoonPhase(WorldAccess world) {
		return world.getDimension().getMoonPhase(world.getLunarTime());
	}

	public static void registerAltarMapEntries(Block[]... altarArray) {
		for (Block[] altars : altarArray) {
			ALTAR_MAP_ENTRIES.add(new AltarMapEntry(altars[0], altars[1], Blocks.MOSS_CARPET.asItem()));
		}
		for (int i = 0; i < DyeColor.values().length; i++) {
			Item carpet = switch (DyeColor.byId(i)) {
				case WHITE -> Items.WHITE_CARPET;
				case ORANGE -> Items.ORANGE_CARPET;
				case MAGENTA -> Items.MAGENTA_CARPET;
				case LIGHT_BLUE -> Items.LIGHT_BLUE_CARPET;
				case YELLOW -> Items.YELLOW_CARPET;
				case LIME -> Items.LIME_CARPET;
				case PINK -> Items.PINK_CARPET;
				case GRAY -> Items.GRAY_CARPET;
				case LIGHT_GRAY -> Items.LIGHT_GRAY_CARPET;
				case CYAN -> Items.CYAN_CARPET;
				case PURPLE -> Items.PURPLE_CARPET;
				case BLUE -> Items.BLUE_CARPET;
				case BROWN -> Items.BROWN_CARPET;
				case GREEN -> Items.GREEN_CARPET;
				case RED -> Items.RED_CARPET;
				case BLACK -> Items.BLACK_CARPET;
			};
			for (Block[] altars : altarArray) {
				ALTAR_MAP_ENTRIES.add(new AltarMapEntry(altars[0], altars[i + 2], carpet));
			}
		}
		for (Block[] altars : altarArray) {
			ALTAR_MAP_ENTRIES.add(new AltarMapEntry(altars[0], altars[18], BWObjects.HEDGEWITCH_CARPET.asItem()));
			ALTAR_MAP_ENTRIES.add(new AltarMapEntry(altars[0], altars[19], BWObjects.ALCHEMIST_CARPET.asItem()));
			ALTAR_MAP_ENTRIES.add(new AltarMapEntry(altars[0], altars[20], BWObjects.BESMIRCHED_CARPET.asItem()));
		}
	}
}
