/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.block.entity;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.common.registry.*;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class WitchAltarBlockEntity extends BlockEntity implements Inventory {
	private int loadingTimer = 20;

	private final Map<Block, Integer> checked = new HashMap<>();
	private final BlockPos.Mutable checking = new BlockPos.Mutable();
	private int counter = 0;

	public int power = 0, maxPower = 0, gain = 1;

	public boolean markedForScan = false;

	private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(3, ItemStack.EMPTY);

	public WitchAltarBlockEntity(BlockPos pos, BlockState state) {
		super(BWBlockEntityTypes.WITCH_ALTAR, pos, state);
	}

	@Override
	public NbtCompound toInitialChunkDataNbt() {
		NbtCompound nbt = super.toInitialChunkDataNbt();
		writeNbt(nbt);
		return nbt;
	}

	@Nullable
	@Override
	public Packet<ClientPlayPacketListener> toUpdatePacket() {
		return BlockEntityUpdateS2CPacket.create(this);
	}

	@Override
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);
		inventory.clear();
		Inventories.readNbt(nbt, inventory);
		power = nbt.getInt("Power");
		maxPower = nbt.getInt("MaxPower");
		gain = nbt.getInt("Gain");
	}

	@Override
	protected void writeNbt(NbtCompound nbt) {
		super.writeNbt(nbt);
		Inventories.writeNbt(nbt, inventory);
		nbt.putInt("Power", power);
		nbt.putInt("MaxPower", maxPower);
		nbt.putInt("Gain", gain);
	}

	public void sync() {
		if (world != null && !world.isClient) {
			world.updateListeners(pos, getCachedState(), getCachedState(), Block.NOTIFY_LISTENERS);
		}
	}

	public static void tick(World world, BlockPos pos, BlockState state, WitchAltarBlockEntity blockEntity) {
		if (world != null && !world.isClient) {
			if (blockEntity.loadingTimer > 0) {
				blockEntity.loadingTimer--;
				if (blockEntity.loadingTimer == 0) {
					blockEntity.markedForScan = true;
				}
			} else {
				if (world.getTime() % 20 == 0) {
					blockEntity.markDirty();
				}
				if (blockEntity.markedForScan) {
					blockEntity.counter = 0;
					blockEntity.checked.clear();
					blockEntity.scan(Short.MAX_VALUE);
					blockEntity.markedForScan = false;
				}
				blockEntity.scan(40);
				if (world.getTime() % 20 == 0) {
					if (world.getBlockState(pos.up()).getBlock() == BWObjects.BLESSED_STONE) {
						blockEntity.power = Integer.MAX_VALUE;
					} else {
						blockEntity.power = Math.min(blockEntity.power + blockEntity.gain, blockEntity.maxPower);
					}
					PlayerLookup.around((ServerWorld) world, Vec3d.of(pos), 24).forEach(player -> {
						if (!BWComponents.CURSES_COMPONENT.get(player).hasCurse(BWCurses.APATHY) && BewitchmentAPI.fillMagic(player, 5, true) && blockEntity.drain(10, true)) {
							BewitchmentAPI.fillMagic(player, 5, false);
							blockEntity.drain(10, false);
						}
					});
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
		for (int i = 0; i < size(); i++) {
			if (getStack(i).isEmpty()) {
				return false;
			}
		}
		return true;
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

	private void scan(int times) {
		if (world != null) {
			for (int i = 0; i < times; i++) {
				counter = ++counter % Short.MAX_VALUE;
				int x = counter & 31;
				int y = (counter >> 5) & 31;
				int z = (counter >> 10) & 31;
				BlockState checkedState = world.getBlockState(checking.set(pos.getX() + x - 15, pos.getY() + y - 15, pos.getZ() + z - 15));
				Block checkedBlock = checkedState.getBlock();
				if (givesAltarPower(checkedState) && !(checkedState.isIn(BlockTags.LOGS) && Registries.BLOCK.getId(checkedBlock).getPath().contains("stripped"))) {
					checked.put(checkedBlock, Math.min(checked.getOrDefault(checkedBlock, 0) + 1, 256));
				}
				if (counter == Short.MAX_VALUE - 1) {
					gain = getPentacleValue(getStack(1));
					maxPower = 0;
					float varietyMultiplier = 1;
					for (Block block : checked.keySet()) {
						if (checked.get(block) > 3) {
							varietyMultiplier *= 1.1f;
						}
						maxPower += checked.get(block);
					}
					maxPower = (int) ((maxPower * Math.min(varietyMultiplier, 4) / 10) * getSwordValue(getStack(0)) + getWandValue(getStack(2)));
					power = Math.min(power, maxPower);
					checked.clear();
				}
			}
		}
	}

	private static boolean givesAltarPower(BlockState state) {
		if (state.getBlock() instanceof PlantBlock || state.getBlock() instanceof MushroomBlock || state.getBlock() instanceof Fertilizable && !(state.getBlock() instanceof GrassBlock || state.getBlock() instanceof NyliumBlock)) {
			return true;
		}
		return state.isIn(BWTags.GIVES_ALTAR_POWER);
	}

	private static float getSwordValue(ItemStack stack) {
		return stack.isIn(BWTags.WEAK_SWORDS) ? 1.1F : stack.isIn(BWTags.AVERAGE_SWORDS) ? 1.2F : stack.isIn(BWTags.STRONG_SWORDS) ? 1.3F : 1;
	}

	private static int getPentacleValue(ItemStack stack) {
		return stack.isIn(BWTags.WEAK_PENTACLES) ? 2 : stack.isIn(BWTags.AVERAGE_PENTACLES) ? 3 : stack.isIn(BWTags.STRONG_PENTACLES) ? 5 : 1;
	}

	private static int getWandValue(ItemStack stack) {
		return stack.isIn(BWTags.WEAK_WANDS) ? 40 : stack.isIn(BWTags.AVERAGE_WANDS) ? 80 : stack.isIn(BWTags.STRONG_WANDS) ? 120 : 0;
	}
}
