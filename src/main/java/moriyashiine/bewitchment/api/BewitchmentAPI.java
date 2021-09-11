package moriyashiine.bewitchment.api;

import com.mojang.authlib.GameProfile;
import moriyashiine.bewitchment.api.component.MagicComponent;
import moriyashiine.bewitchment.api.component.PledgeComponent;
import moriyashiine.bewitchment.api.component.TransformationComponent;
import moriyashiine.bewitchment.api.item.PoppetItem;
import moriyashiine.bewitchment.api.misc.PoppetData;
import moriyashiine.bewitchment.api.registry.AltarMapEntry;
import moriyashiine.bewitchment.common.block.entity.PoppetShelfBlockEntity;
import moriyashiine.bewitchment.common.entity.component.AdditionalWerewolfDataComponent;
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
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

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
				Entity entity = serverWorld.getEntity(ownerUUID);
				if (entity instanceof LivingEntity) {
					return (LivingEntity) entity;
				}
			}
		}
		return null;
	}

	/*
	*	Search through inventory for Poppet matching input.
	* 	Will early out if it finds valid poppet.
	 */
	public static PoppetData getPoppetFromInventory(World world, PoppetItem item, Entity owner, DefaultedList<ItemStack> inventory)
	{
		for(int i = 0; i < inventory.size(); i++)
		{
			ItemStack stack = inventory.get(i);
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
		if (world.isClient)
			return PoppetData.EMPTY;

		if(item.worksInShelf) {
			for (Map.Entry<Long, DefaultedList<ItemStack>> entry : BWWorldState.get(world).poppetShelves.entrySet()) {
				PoppetData result = getPoppetFromInventory(world, item, owner, entry.getValue());

				if (result != PoppetData.EMPTY) {
					BlockPos pos = BlockPos.fromLong(entry.getKey());

					// Only sync block entities from loaded chunks.
					if(world.isChunkLoaded(ChunkSectionPos.getSectionCoord(pos.getX()), ChunkSectionPos.getSectionCoord(pos.getY()))) {
						if(world.getBlockEntity(pos) instanceof PoppetShelfBlockEntity poppetShelf) {
							poppetShelf.sync();
						}
					}

					return new PoppetData(result.stack, entry.getKey(), result.index);
				}
			}
		}
		for (PlayerEntity player : ((ServerWorld) world).getPlayers()) {
			PoppetData result = getPoppetFromInventory(world, item, owner, player.getInventory().main);

			if (result != PoppetData.EMPTY)
				return result;
		}

		return PoppetData.EMPTY;
	}

	public static ServerPlayerEntity getFakePlayer(World world) {
		if (!world.isClient) {
			if (fakePlayer == null || fakePlayer.getMainHandStack().getItem() != Items.WOODEN_AXE) {
				fakePlayer = new ServerPlayerEntity(world.getServer(), (ServerWorld) world, new GameProfile(UUID.randomUUID(), "FAKE_PLAYER"));
				fakePlayer.setStackInHand(Hand.MAIN_HAND, new ItemStack(Items.WOODEN_AXE));
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
		}
		else if (BewitchmentAPI.isWerewolf(player, false)) {
			WerewolfEntity entity = BWEntityTypes.WEREWOLF.create(player.world);
			entity.getDataTracker().set(BWHostileEntity.VARIANT, AdditionalWerewolfDataComponent.get(player).getVariant());
			return entity;
		}
		return null;
	}

	public static EntityType<?> getFamiliar(PlayerEntity player) {
		World world = player.world;
		if (!world.isClient) {
			for (Pair<UUID, NbtCompound> pair : BWUniversalWorldState.get(world).familiars) {
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
		return MagicComponent.get(player).fillMagic(amount, simulate);
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
		return MagicComponent.get(player).drainMagic(amount, simulate);
	}

	public static boolean isVampire(Entity entity, boolean includeHumanForm) {
		if (entity instanceof PlayerEntity player) {
			TransformationComponent transformationComponent = TransformationComponent.get(player);
			if (transformationComponent.getTransformation() == BWTransformations.VAMPIRE) {
				return includeHumanForm || transformationComponent.isAlternateForm();
			}
		}
		return entity instanceof VampireEntity;
	}

	public static boolean isWerewolf(Entity entity, boolean includeHumanForm) {
		if (entity instanceof PlayerEntity player) {
			TransformationComponent transformationComponent = TransformationComponent.get(player);
			if (transformationComponent.getTransformation() == BWTransformations.WEREWOLF) {
				return includeHumanForm || transformationComponent.isAlternateForm();
			}
		}
		return entity instanceof WerewolfEntity;
	}

	public static boolean isSourceFromSilver(DamageSource source) {
		if (source.getSource() instanceof LivingEntity && ((LivingEntity) source.getSource()).getMainHandStack().getItem() instanceof AthameItem) {
			return true;
		}
		return source.getSource() instanceof SilverArrowEntity;
	}

	public static boolean isWeakToSilver(LivingEntity livingEntity) {
		if (BWTags.IMMUNE_TO_SILVER.contains(livingEntity.getType())) {
			return false;
		}
		return livingEntity.isUndead() || livingEntity.getGroup() == DEMON || BWTags.VULNERABLE_TO_SILVER.contains(livingEntity.getType());
	}

	public static boolean isPledged(PlayerEntity player, String pledge) {
		PledgeComponent pledgeComponent = PledgeComponent.get(player);
		if (!player.world.isClient) {
			BWUniversalWorldState worldState = BWUniversalWorldState.get(player.world);
			for (int i = worldState.pledgesToRemove.size() - 1; i >= 0; i--) {
				if (worldState.pledgesToRemove.get(i).equals(player.getUuid())) {
					pledgeComponent.setPledgeNextTick(BWPledges.NONE);
					worldState.pledgesToRemove.remove(i);
					return false;
				}
			}
		}
		return pledgeComponent.getPledge().equals(pledge);
	}

	public static void unpledge(PlayerEntity player) {
		PledgeComponent.get(player).setPledge(BWPledges.NONE);
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
