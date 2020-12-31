package moriyashiine.bewitchment.common.block.entity;

import com.mojang.authlib.GameProfile;
import moriyashiine.bewitchment.api.interfaces.MagicAccessor;
import moriyashiine.bewitchment.common.registry.BWBlockEntityTypes;
import moriyashiine.bewitchment.common.registry.BWTags;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.fabricmc.fabric.api.server.PlayerStream;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.Hand;
import net.minecraft.util.Tickable;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;

import java.util.HashMap;
import java.util.Map;

public class WitchAltarBlockEntity extends BlockEntity implements BlockEntityClientSerializable, Tickable, Inventory {
	private static final GameProfile FAKE_PLAYER_PROFILE = new GameProfile(null, "FAKE_PLAYER");
	
	private int loadingTimer = 20;
	private PlayerEntity fakePlayer = null;
	
	private final Map<Block, Integer> checked = new HashMap<>();
	private final BlockPos.Mutable checking = new BlockPos.Mutable();
	private int counter = 0;
	
	public int power = 0, maxPower = 0, gain = 1;
	
	public boolean markedForScan = false;
	
	private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(3, ItemStack.EMPTY);
	
	public WitchAltarBlockEntity(BlockEntityType<?> type) {
		super(type);
	}
	
	public WitchAltarBlockEntity() {
		this(BWBlockEntityTypes.WITCH_ALTAR);
	}
	
	@Override
	public void fromClientTag(CompoundTag tag) {
		Inventories.fromTag(tag, inventory);
		power = tag.getInt("Power");
		maxPower = tag.getInt("MaxPower");
		gain = tag.getInt("Gain");
	}
	
	@Override
	public CompoundTag toClientTag(CompoundTag tag) {
		Inventories.toTag(tag, inventory);
		tag.putInt("Power", power);
		tag.putInt("MaxPower", maxPower);
		tag.putInt("Gain", gain);
		return tag;
	}
	
	@Override
	public void fromTag(BlockState state, CompoundTag tag) {
		fromClientTag(tag);
		super.fromTag(state, tag);
	}
	
	@Override
	public CompoundTag toTag(CompoundTag tag) {
		return super.toTag(toClientTag(tag));
	}
	
	@Override
	public void tick() {
		if (world != null && !world.isClient) {
			if (loadingTimer > 0) {
				loadingTimer--;
				if (loadingTimer == 0) {
					markDirty();
					MinecraftServer server = world.getServer();
					//noinspection ConstantConditions
					ServerWorld overworld = server.getOverworld();
					fakePlayer = new ServerPlayerEntity(server, overworld, FAKE_PLAYER_PROFILE, new ServerPlayerInteractionManager(overworld));
					fakePlayer.setStackInHand(Hand.MAIN_HAND, new ItemStack(Items.WOODEN_AXE));
					markedForScan = true;
				}
			}
			else {
				if (markedForScan) {
					counter = 0;
					checked.clear();
					scan(Short.MAX_VALUE);
					markedForScan = false;
				}
				scan(80);
				if (world.getTime() % 20 == 0) {
					power = Math.min(power + gain, maxPower);
					PlayerStream.around(world, pos, 24).forEach(playerEntity -> MagicAccessor.of(playerEntity).ifPresent(magicAccessor -> {
						if (magicAccessor.fillMagic(100, true) && drain(10, true)) {
							magicAccessor.fillMagic(100, false);
							drain(10, false);
						}
					}));
				}
			}
		}
	}
	
	@Override
	public int size() {
		return inventory.size();
	}
	
	@Override
	public boolean isEmpty() {
		return inventory.isEmpty();
	}
	
	@Override
	public ItemStack getStack(int slot) {
		return inventory.get(slot);
	}
	
	@Override
	public ItemStack removeStack(int slot, int amount) {
		return Inventories.splitStack(inventory, slot, amount);
	}
	
	@Override
	public ItemStack removeStack(int slot) {
		return Inventories.removeStack(inventory, slot);
	}
	
	@Override
	public void setStack(int slot, ItemStack stack) {
		inventory.set(slot, stack);
	}
	
	@Override
	public boolean canPlayerUse(PlayerEntity player) {
		return player.squaredDistanceTo(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5) < 16;
	}
	
	@Override
	public void clear() {
		inventory.clear();
	}
	
	public boolean drain(int amount, boolean simulate) {
		if (power - amount >= 0) {
			if (!simulate) {
				power -= amount;
			}
			return true;
		}
		return false;
	}
	
	@SuppressWarnings("ConstantConditions")
	private void scan(int times) {
		if (world != null) {
			for (int i = 0; i < times; i++) {
				counter = ++counter % Short.MAX_VALUE;
				int x = counter & 31;
				int y = (counter >> 5) & 31;
				int z = (counter >> 10) & 31;
				Block checkedBlock = world.getBlockState(checking.set(pos.getX() + x - 15, pos.getY() + y - 15, pos.getZ() + z - 15)).getBlock();
				if (BWTags.GIVES_ALTAR_POWER.contains(checkedBlock)) {
					boolean strippedLog = false;
					if (BlockTags.LOGS.contains(checkedBlock)) {
						MinecraftServer server = getWorld().getServer();
						ServerWorld overworld = server.getOverworld();
						checking.set(BlockPos.ORIGIN);
						BlockState original = overworld.getBlockState(checking);
						overworld.setBlockState(checking, checkedBlock.getDefaultState());
						BlockState checkingState = overworld.getBlockState(checking);
						checkingState.getBlock().onUse(checkingState, world, checking, fakePlayer, Hand.MAIN_HAND, new BlockHitResult(Vec3d.ZERO, Direction.UP, checking, false));
						if (Registry.BLOCK.getId(overworld.getBlockState(checking).getBlock()).getPath().contains("stripped")) {
							strippedLog = true;
						}
						overworld.setBlockState(checking, original);
					}
					if (!strippedLog) {
						checked.put(checkedBlock, Math.min(checked.getOrDefault(checkedBlock, 0) + 1, 256));
					}
				}
				if (counter == Short.MAX_VALUE - 1) {
					gain = getPentacleValue(getStack(1).getItem());
					maxPower = 0;
					float varietyMultiplier = 1;
					for (Block block : checked.keySet()) {
						if (checked.get(block) > 3) {
							varietyMultiplier *= 1.1f;
						}
						maxPower += checked.get(block);
					}
					maxPower = (int) ((maxPower * Math.min(varietyMultiplier, 4) / 10) * getSwordValue(getStack(0).getItem()) + getWandValue(getStack(2).getItem()));
					power = Math.min(power, maxPower);
					checked.clear();
				}
			}
		}
	}
	
	private static float getSwordValue(Item item) {
		return BWTags.WEAK_SWORDS.contains(item) ? 1.1f : BWTags.AVERAGE_SWORDS.contains(item) ? 1.2f : BWTags.STRONG_SWORDS.contains(item) ? 1.3f : 1;
	}
	
	private static int getPentacleValue(Item item) {
		return BWTags.WEAK_PENTACLES.contains(item) ? 2 : BWTags.AVERAGE_PENTACLES.contains(item) ? 3 : BWTags.STRONG_PENTACLES.contains(item) ? 5 : 1;
	}
	
	private static int getWandValue(Item item) {
		return BWTags.WEAK_WANDS.contains(item) ? 40 : BWTags.AVERAGE_WANDS.contains(item) ? 80 : BWTags.STRONG_WANDS.contains(item) ? 120 : 0;
	}
}
