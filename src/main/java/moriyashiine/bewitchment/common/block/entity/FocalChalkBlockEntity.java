package moriyashiine.bewitchment.common.block.entity;

import moriyashiine.bewitchment.client.network.message.SmokePuffMessage;
import moriyashiine.bewitchment.client.network.message.SyncFocalChalkBlockEntityMessage;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.block.ChalkBlock;
import moriyashiine.bewitchment.common.recipe.Ritual;
import moriyashiine.bewitchment.common.registry.BWBlockEntityTypes;
import moriyashiine.bewitchment.common.registry.BWObjects;
import moriyashiine.bewitchment.common.registry.BWRecipeTypes;
import net.fabricmc.fabric.api.server.PlayerStream;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.Lazy;
import net.minecraft.util.Tickable;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FocalChalkBlockEntity extends BlockEntity implements Inventory, Tickable {
	private static final byte[][] inner = {{0, 0, 1, 1, 1, 0, 0}, {0, 1, 0, 0, 0, 1, 0}, {1, 0, 0, 0, 0, 0, 1}, {1, 0, 0, 0, 0, 0, 1}, {1, 0, 0, 0, 0, 0, 1}, {0, 1, 0, 0, 0, 1, 0}, {0, 0, 1, 1, 1, 0, 0}};
	private static final byte[][] middle = {{0, 0, 0, 1, 1, 1, 1, 1, 0, 0, 0}, {0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0}, {0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0}, {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1}, {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1}, {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1}, {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1}, {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1}, {0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0}, {0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0}, {0, 0, 0, 1, 1, 1, 1, 1, 0, 0, 0}};
	private static final byte[][] outer = {{0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0}, {0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0}, {0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0}, {0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0}, {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1}, {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1}, {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1}, {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1}, {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1}, {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1}, {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1}, {0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0}, {0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0}, {0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0}, {0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0}};
	
	protected final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(12, ItemStack.EMPTY);
	
	private Lazy<Ritual> lazyRitual = new Lazy<>(() -> null);
	public int time = 0;
	
	public FocalChalkBlockEntity() {
		super(BWBlockEntityTypes.focal_chalk);
	}
	
	@Override
	public BlockEntityUpdateS2CPacket toUpdatePacket() {
		return new BlockEntityUpdateS2CPacket(pos, 0, toTag(new CompoundTag()));
	}
	
	@Override
	public void tick() {
		if (world != null) {
			Ritual ritual = getRitual();
			if (ritual != null) {
				ritual.function.runningTick(ritual, world, pos);
				if (!world.isClient) {
					time--;
					if (time == 0) {
						ritual.function.finish(ritual, world, pos);
						finishRitual(false);
						syncToClientAndMarkDirty();
					}
				}
			}
		}
	}
	
	@Override
	public void fromTag(BlockState state, CompoundTag tag) {
		lazyRitual = new Lazy<>(() -> Objects.requireNonNull(world).getRecipeManager().method_30027(BWRecipeTypes.ritual_type).stream().filter(ritual -> ritual.getId().toString().equals(tag.getString("Ritual"))).findFirst().orElse(null));
		Inventories.fromTag(tag, inventory);
		time = tag.getInt("Time");
		super.fromTag(state, tag);
	}
	
	@Override
	public CompoundTag toTag(CompoundTag tag) {
		Ritual ritual = getRitual();
		if (ritual != null) {
			tag.putString("Ritual", ritual.getId().toString());
		}
		Inventories.toTag(tag, inventory);
		tag.putInt("Time", time);
		return super.toTag(tag);
	}
	
	public void onUse(PlayerEntity player, Hand hand) {
		if (world != null && !world.isClient) {
			if (getRitual() == null) {
				if (hand == Hand.MAIN_HAND) {
					List<ItemEntity> itemsOnGround = new ArrayList<>(world.getNonSpectatingEntities(ItemEntity.class, new Box(pos).expand(3, 0, 3)));
					Ritual rit = null;
					for (Recipe<?> recipe : world.getRecipeManager().method_30027(BWRecipeTypes.ritual_type)) {
						if (recipe instanceof Ritual) {
							Ritual foundRit = (Ritual) recipe;
							if (foundRit.input.size() == itemsOnGround.size()) {
								List<ItemStack> items = new ArrayList<>();
								for (ItemEntity itemEntity : itemsOnGround) {
									items.add(itemEntity.getStack());
								}
								for (Ingredient ingredient : foundRit.input) {
									for (int i = items.size() - 1; i >= 0; i--) {
										if (ingredient.test(items.get(i))) {
											items.remove(i);
										}
									}
								}
								if (items.isEmpty() && doesRitualHaveValidChalk(foundRit)) {
									rit = foundRit;
									break;
								}
							}
						}
					}
					if (rit != null) {
						if (rit.function.isValid(world, pos)) {
							//todo: drain
							for (ItemEntity itemEntity : itemsOnGround) {
								world.playSound(null, itemEntity.getBlockPos(), SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 1, 1);
								PlayerStream.around(world, pos, 32).forEach(foundPlayer -> SmokePuffMessage.send(foundPlayer, itemEntity.getEntityId()));
								setStack(-1, itemEntity.getStack().split(1));
							}
							world.playSound(null, pos, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 1, 1);
							player.sendMessage(new TranslatableText(rit.getId().toString().replaceAll(":", ".").replaceAll("/", ".")), true);
							setRitual(rit);
							time = rit.time;
							syncToClientAndMarkDirty();
						}
						else {
							world.playSound(null, pos, SoundEvents.BLOCK_NOTE_BLOCK_SNARE, SoundCategory.BLOCKS, 1, 0.8f);
							player.sendMessage(new TranslatableText(rit.function.getPreconditionMessage()), true);
						}
					}
					else {
						world.playSound(null, pos, SoundEvents.BLOCK_NOTE_BLOCK_SNARE, SoundCategory.BLOCKS, 1, 0.8f);
						player.sendMessage(new TranslatableText(Bewitchment.MODID + ".ritual.null"), true);
					}
				}
			}
			else {
				finishRitual(true);
				syncToClientAndMarkDirty();
			}
		}
	}
	
	public void setRitual(Ritual ritual) {
		this.lazyRitual = new Lazy<>(() -> ritual);
	}
	
	private Ritual getRitual() {
		return lazyRitual.get();
	}
	
	private boolean doesRitualHaveValidChalk(Ritual ritual) {
		if (world != null) {
			BlockPos.Mutable mutablePos = new BlockPos.Mutable();
			String ritualInner = ritual.inner;
			for (int x = 0; x < inner.length; x++) {
				for (int z = 0; z < inner.length; z++) {
					Block block = world.getBlockState(mutablePos.set(pos.getX() + (x - inner.length / 2), pos.getY(), pos.getZ() + (z - inner.length / 2))).getBlock();
					if (inner[x][z] == 1 && !isValidChalk(ritualInner, block)) {
						return false;
					}
				}
			}
			String ritualMiddle = ritual.middle;
			if (!ritualMiddle.isEmpty()) {
				for (int x = 0; x < middle.length; x++) {
					for (int z = 0; z < middle.length; z++) {
						Block block = world.getBlockState(mutablePos.set(pos.getX() + (x - middle.length / 2), pos.getY(), pos.getZ() + (z - middle.length / 2))).getBlock();
						if (middle[x][z] == 1 && !isValidChalk(ritualMiddle, block)) {
							return false;
						}
					}
				}
				String ritualOuter = ritual.outer;
				if (!ritualOuter.isEmpty()) {
					for (int x = 0; x < outer.length; x++) {
						for (int z = 0; z < outer.length; z++) {
							Block block = world.getBlockState(mutablePos.set(pos.getX() + (x - outer.length / 2), pos.getY(), pos.getZ() + (z - outer.length / 2))).getBlock();
							if (outer[x][z] == 1 && !isValidChalk(ritualOuter, block)) {
								return false;
							}
						}
					}
				}
			}
			return true;
		}
		return false;
	}
	
	@SuppressWarnings("BooleanMethodIsAlwaysInverted")
	private boolean isValidChalk(String name, Block block) {
		if (name.equals("chalk") && block == BWObjects.chalk_block) {
			return true;
		}
		if (name.equals("infernal_chalk") && block == BWObjects.infernal_chalk_block) {
			return true;
		}
		if (name.equals("eldritch_chalk") && block == BWObjects.eldritch_chalk_block) {
			return true;
		}
		return name.equals("any_chalk") && block instanceof ChalkBlock && block != BWObjects.focal_chalk_block;
	}
	
	private void finishRitual(boolean dropItems) {
		if (world != null) {
			boolean playSound = dropItems;
			for (int i = 0; i < 12; i++) {
				if (!dropItems) {
					ItemStack stack = inventory.get(i);
					ItemStack toSet = ItemStack.EMPTY;
					if (stack.getItem().hasRecipeRemainder()) {
						toSet = new ItemStack(stack.getItem().getRecipeRemainder());
					}
					setStack(i, toSet);
				}
				ItemStack extracted = removeStack(i);
				playSound |= !extracted.isEmpty();
				ItemScatterer.spawn(world, pos.getX(), pos.getY(), pos.getZ(), extracted);
			}
			if (playSound) {
				world.playSound(null, pos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 1, 0.8f);
			}
		}
		setRitual(null);
		time = 0;
	}
	
	@Override
	public boolean isEmpty() {
		for (ItemStack stack : inventory) {
			if (!stack.isEmpty()) {
				return false;
			}
		}
		return true;
	}
	
	@Override
	public int size() {
		return inventory.size();
	}
	
	@Override
	public void clear() {
		inventory.clear();
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
	public ItemStack getStack(int slot) {
		return inventory.get(slot);
	}
	
	@Override
	public void setStack(int slot, ItemStack stack) {
		if (slot < 0) {
			for (int i = 0; i < size(); i++) {
				if (getStack(i).isEmpty()) {
					slot = i;
					break;
				}
			}
		}
		inventory.set(slot, stack);
		int maxCount = getMaxCountPerStack();
		if (stack.getCount() > maxCount) {
			stack.setCount(maxCount);
		}
		markDirty();
	}
	
	@Override
	public boolean canPlayerUse(PlayerEntity player) {
		return (world == null || world.getBlockEntity(pos) == this) && player.squaredDistanceTo(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5) <= 74;
	}
	
	private void syncToClientAndMarkDirty() {
		if (world != null) {
			markDirty();
			PlayerStream.around(world, pos, 32).forEach(player -> SyncFocalChalkBlockEntityMessage.send(player, pos, getRitual(), time));
		}
	}
}