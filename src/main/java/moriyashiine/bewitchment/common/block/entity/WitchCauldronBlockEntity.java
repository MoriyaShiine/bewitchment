/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.bewitchment.common.block.entity;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.api.block.entity.UsesAltarPower;
import moriyashiine.bewitchment.common.item.TaglockItem;
import moriyashiine.bewitchment.common.misc.BWUtil;
import moriyashiine.bewitchment.common.recipe.CauldronBrewingRecipe;
import moriyashiine.bewitchment.common.recipe.OilRecipe;
import moriyashiine.bewitchment.common.registry.*;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ConstantConditions")
public class WitchCauldronBlockEntity extends BlockEntity implements Inventory, UsesAltarPower {
	private static final TranslatableText DEFAULT_NAME = new TranslatableText(BWObjects.WITCH_CAULDRON.getTranslationKey());

	private Box box;

	private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(4, ItemStack.EMPTY);

	private BlockPos altarPos = null;

	public OilRecipe oilRecipe = null;

	public Mode mode = Mode.NORMAL;

	public String name;

	public int color = 0x3f76e4, heatTimer = 0;

	private boolean loaded = false;

	public WitchCauldronBlockEntity(BlockPos pos, BlockState state) {
		super(BWBlockEntityTypes.WITCH_CAULDRON, pos, state);
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
		if (nbt.contains("AltarPos")) {
			setAltarPos(BlockPos.fromLong(nbt.getLong("AltarPos")));
		}
		Inventories.readNbt(nbt, inventory);
		mode = Mode.valueOf(nbt.getString("Mode"));
		if (nbt.contains("Name")) {
			name = nbt.getString("Name");
		}
		if (nbt.contains("Color")) {
			color = nbt.getInt("Color");
		}
		heatTimer = nbt.getInt("HeatTimer");
	}

	@Override
	protected void writeNbt(NbtCompound nbt) {
		super.writeNbt(nbt);
		if (getAltarPos() != null) {
			nbt.putLong("AltarPos", getAltarPos().asLong());
		}
		Inventories.writeNbt(nbt, inventory);
		nbt.putString("Mode", mode.name);
		if (name != null) {
			nbt.putString("Name", name);
		}
		nbt.putInt("Color", color);
		nbt.putInt("HeatTimer", heatTimer);
	}

	public void sync() {
		if (world != null && !world.isClient) {
			world.updateListeners(pos, getCachedState(), getCachedState(), Block.NOTIFY_LISTENERS);
		}
	}

	@Override
	public BlockPos getAltarPos() {
		return altarPos;
	}

	@Override
	public void setAltarPos(BlockPos pos) {
		altarPos = pos;
	}

	public static void tick(World world, BlockPos pos, BlockState state, WitchCauldronBlockEntity blockEntity) {
		if (world != null) {
			if (!blockEntity.loaded) {
				blockEntity.markDirty();
				blockEntity.box = new Box(pos).contract(0.75);
				blockEntity.oilRecipe = world.getRecipeManager().listAllOfType(BWRecipeTypes.OIL_RECIPE_TYPE).stream().filter(recipe -> recipe.matches(blockEntity, world)).findFirst().orElse(null);
				blockEntity.loaded = true;
			}
			blockEntity.heatTimer = MathHelper.clamp(blockEntity.heatTimer + (state.get(Properties.LIT) && state.get(BWProperties.LEVEL) > 0 ? 1 : -1), 0, 160);
			if (!world.isClient) {
				if (blockEntity.heatTimer >= 60 && state.get(BWProperties.LEVEL) > 0) {
					if (world.random.nextFloat() <= 0.075f) {
						world.playSound(null, pos, SoundEvents.BLOCK_BUBBLE_COLUMN_UPWARDS_AMBIENT, SoundCategory.BLOCKS, 1 / 3f, blockEntity.mode == Mode.FAILED ? 0.5f : 1);
					}
					if (world.getTime() % 5 == 0) {
						world.getEntitiesByType(EntityType.ITEM, blockEntity.box, entity -> true).forEach(itemEntity -> {
							if (itemEntity.getStack().getItem() == BWObjects.WOOD_ASH || state.get(BWProperties.LEVEL) == 3) {
								world.playSound(null, pos, SoundEvents.ENTITY_GENERIC_SPLASH, SoundCategory.BLOCKS, 1 / 3f, 1);
								ItemStack stack = itemEntity.getStack();
								if (stack.getItem().hasRecipeRemainder()) {
									ItemEntity remainder = new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, new ItemStack(stack.getItem().getRecipeRemainder()));
									remainder.setVelocity(Vec3d.ZERO);
									remainder.setNoGravity(true);
									world.spawnEntity(remainder);
								}
								blockEntity.mode = blockEntity.insertStack(stack.split(1));
								blockEntity.markDirty();
								blockEntity.syncCauldron();
							}
						});
					}
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

	public void syncCauldron() {
		sync();
	}

	public void setColor(int color) {
		if (world != null) {
			this.color = color;
		}
	}

	public Mode reset() {
		if (world != null) {
			setColor(0x3f76e4);
			clear();
			oilRecipe = null;
			world.setBlockState(pos, getCachedState().with(BWProperties.LEVEL, 0));
		}
		return Mode.NORMAL;
	}

	public Mode fail() {
		setColor(0x6b4423);
		return Mode.FAILED;
	}

	private int getFirstEmptySlot() {
		for (int i = 0; i < size(); i++) {
			if (getStack(i).isEmpty()) {
				return i;
			}
		}
		return -1;
	}

	private Mode insertStack(ItemStack stack) {
		if (world != null) {
			if (stack.getItem() == BWObjects.WOOD_ASH) {
				Mode reset = reset();
				syncCauldron();
				return reset;
			} else if (mode != Mode.FAILED && mode != Mode.TELEPORTATION) {
				int firstEmpty = getFirstEmptySlot();
				if (firstEmpty != -1) {
					setStack(firstEmpty, stack);
					if (mode == Mode.NORMAL && stack.getItem() == BWObjects.MANDRAKE_ROOT) {
						clear();
						return Mode.BREWING;
					}
					if (mode == Mode.BREWING) {
						CauldronBrewingRecipe cauldronBrewingRecipe = world.getRecipeManager().listAllOfType(BWRecipeTypes.CAULDRON_BREWING_RECIPE_TYPE).stream().filter(recipe -> recipe.input.test(stack)).findFirst().orElse(null);
						if (cauldronBrewingRecipe != null || stack.getItem() == Items.REDSTONE || stack.getItem() == Items.GLOWSTONE_DUST) {
							BlockPos altarPos = getAltarPos();
							if (altarPos != null && ((WitchAltarBlockEntity) world.getBlockEntity(altarPos)).drain(getBrewCost(), true)) {
								setColor(PotionUtil.getColor(getPotion(null)));
								return Mode.BREWING;
							}
						}
					} else {
						oilRecipe = world.getRecipeManager().listAllOfType(BWRecipeTypes.OIL_RECIPE_TYPE).stream().filter(recipe -> recipe.matches(this, world)).findFirst().orElse(null);
						if (oilRecipe != null) {
							setColor(oilRecipe.color);
							return Mode.OIL_CRAFTING;
						} else if (firstEmpty == 0 && stack.getItem() == Items.ENDER_PEARL) {
							clear();
							setColor(0x319a89);
							return Mode.TELEPORTATION;
						}
						setColor(0xfc00fc);
						return Mode.OIL_CRAFTING;
					}
				}
			}
		}
		return fail();
	}

	public ItemStack getPotion(LivingEntity creator) {
		ItemStack stack = new ItemStack(Items.POTION);
		if (world != null) {
			List<StatusEffectInstance> effects = new ArrayList<>();
			int durationBoost = creator != null && BWUtil.getArmorPieces(creator, armorStack -> armorStack.getItem() instanceof ArmorItem armorItem && armorItem.getMaterial() == BWMaterials.ALCHEMIST_ARMOR) >= 3 ? 1 : 0;
			int amplifierBoost = 0;
			boolean leonard = creator instanceof PlayerEntity player && BewitchmentAPI.isPledged(player, BWPledges.LEONARD);
			for (int i = 0; i < size(); i++) {
				ItemStack stackInSlot = getStack(i);
				if (stackInSlot.getItem() instanceof TaglockItem && TaglockItem.isTaglockFromPlayer(stackInSlot)) {
					stack.getOrCreateNbt().putUuid("PolymorphUUID", TaglockItem.getTaglockUUID(stackInSlot));
					stack.getOrCreateNbt().putString("PolymorphName", TaglockItem.getTaglockName(stackInSlot));
				}
				CauldronBrewingRecipe cauldronBrewingRecipe = world.getRecipeManager().listAllOfType(BWRecipeTypes.CAULDRON_BREWING_RECIPE_TYPE).stream().filter(recipe -> recipe.input.test(stackInSlot)).findFirst().orElse(null);
				if (cauldronBrewingRecipe != null && effects.stream().noneMatch(effect -> effect.getEffectType() == cauldronBrewingRecipe.output)) {
					effects.add(new StatusEffectInstance(cauldronBrewingRecipe.output, cauldronBrewingRecipe.time));
				} else if (stackInSlot.getItem() == Items.REDSTONE) {
					durationBoost++;
				} else if (stackInSlot.getItem() == Items.GLOWSTONE_DUST) {
					amplifierBoost++;
				}
			}
			for (int i = 0; i < effects.size(); i++) {
				for (int j = 0; j < durationBoost; j++) {
					StatusEffect type = effects.get(i).getEffectType();
					int duration = effects.get(i).getDuration();
					effects.set(i, new StatusEffectInstance(type, type.isInstant() ? duration : duration * 2));
				}
				for (int j = 0; j < amplifierBoost && j < 2; j++) {
					if (j == 1 && !leonard) {
						break;
					}
					StatusEffect type = effects.get(i).getEffectType();
					int duration = effects.get(i).getDuration();
					effects.set(i, new StatusEffectInstance(type, type.isInstant() ? duration : duration / 2, effects.get(i).getAmplifier() + 1));
				}
			}
			List<StatusEffectInstance> finalEffects = new ArrayList<>();
			for (int i = effects.size() - 1; i >= 0; i--) {
				if (effects.get(i).getEffectType() == BWStatusEffects.CORRUPTION) {
					finalEffects.add(effects.remove(i));
				} else if (effects.get(i).getEffectType() == BWStatusEffects.POLYMORPH) {
					StatusEffectInstance removed = effects.remove(i);
					finalEffects.add(new StatusEffectInstance(removed.getEffectType(), removed.getDuration(), removed.getAmplifier(), removed.isAmbient(), false, removed.shouldShowIcon()));
				}
			}
			finalEffects.addAll(effects);
			PotionUtil.setCustomPotionEffects(stack, finalEffects);
			stack.getOrCreateNbt().putInt("CustomPotionColor", PotionUtil.getColor(finalEffects));
			stack.getOrCreateNbt().putBoolean("BewitchmentBrew", true);
		}
		return stack;
	}

	public int getBrewCost() {
		int cost = 0;
		for (int i = 0; i < size(); i++) {
			if (!getStack(i).isEmpty()) {
				cost += 150;
			}
		}
		return cost;
	}

	public int getTargetLevel(ItemStack stack) {
		Item item = stack.getItem();
		int level = getCachedState().get(BWProperties.LEVEL);
		if (mode == Mode.NORMAL) {
			if (item == Items.BUCKET && level == 3) {
				return 0;
			} else if (item == Items.WATER_BUCKET && level == 0) {
				return 3;
			} else if (item == Items.GLASS_BOTTLE) {
				return level - 1;
			} else if (item == Items.POTION && level < 3 && PotionUtil.getPotion(stack) == Potions.WATER) {
				return level + 1;
			}
		} else if (mode == Mode.OIL_CRAFTING) {
			if (oilRecipe != null && item == Items.GLASS_BOTTLE) {
				return level - 1;
			}
		} else if (mode == Mode.BREWING) {
			if (item == Items.GLASS_BOTTLE) {
				return level - 1;
			}
		}
		return -1;
	}

	public enum Mode {
		NORMAL("NORMAL"), OIL_CRAFTING("OIL_CRAFTING"), BREWING("BREWING"), TELEPORTATION("TELEPORTATION"), FAILED("FAILED");

		public final String name;

		Mode(String name) {
			this.name = name;
		}
	}
}
