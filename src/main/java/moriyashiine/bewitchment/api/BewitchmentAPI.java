package moriyashiine.bewitchment.api;

import moriyashiine.bewitchment.common.block.UnformedWitchAltarBlock;
import moriyashiine.bewitchment.common.item.tool.AthameItem;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ItemScatterer;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

public class BewitchmentAPI {
	public static final Set<Predicate<LivingEntity>> ADDITIONAL_SILVER_WEAKNESSES = new HashSet<>();
	
	/**
	 * the demon entity group
	 */
	@SuppressWarnings("InstantiationOfUtilityClass")
	public static final EntityGroup DEMON = new EntityGroup();
	
	/**
	 * registers bark to be associated with a log block. make sure the log block exists in AxeItem#BLOCK_STRIPPING_MAP
	 * @param log the log block to drop the bark
	 * @param bark the bark to drop
	 */
	public static void registerLogToBark(Block log, Item bark) {
		AthameItem.LOG_TO_BARK_MAP.put(log, bark);
	}
	
	/**
	 * adds an additional check if a given entity should be weak to silver aside from the tags
	 * @param predicate the predicate to test
	 */
	public static void registerAdditionalSilverWeakness(Predicate<LivingEntity> predicate) {
		ADDITIONAL_SILVER_WEAKNESSES.add(predicate);
	}
	
	/**
	 * registers a set of blocks to the altar map. this is used to determine what unformed altar becomes what formed altar with which carpet
	 * @param unformed the unformed altar
	 * @param formed the formed altar
	 * @param core the altar core, should be the same as the formed altar but has a tile
	 * @param carpet the carpet
	 */
	public static void registerAltarMap(Block unformed, Block formed, Block core, Block carpet) {
		UnformedWitchAltarBlock.ALTAR_MAP.add(new UnformedWitchAltarBlock.AltarGroup(unformed, formed, core, carpet));
	}
	
	/**
	 * gives a player an ItemStack, and drops it on the ground if the inventory is full
	 * @param player the player
	 * @param stack the stack to give
	 */
	public static void giveStackToPlayer(PlayerEntity player, ItemStack stack) {
		if (!player.giveItemStack(stack)) {
			ItemScatterer.spawn(player.world, player.getX() + 0.5, player.getY() + 0.5, player.getZ() + 0.5, stack);
		}
	}
	
	/**
	 * @param entity the entity to check
	 * @return true if the entity is a player vampire or vampire mob, false otherwise
	 */
	public static boolean isVampire(Entity entity) {
		return false;
	}
	
	/**
	 * @param entity the entity to check
	 * @return true if the entity is a player werewolf or werewolf mob, false otherwise
	 */
	public static boolean isWerewolf(Entity entity) {
		return false;
	}
}