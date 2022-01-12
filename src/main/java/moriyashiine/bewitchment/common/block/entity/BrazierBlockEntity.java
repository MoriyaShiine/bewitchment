/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.bewitchment.common.block.entity;

import dev.emi.trinkets.api.TrinketsApi;
import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.api.block.entity.UsesAltarPower;
import moriyashiine.bewitchment.api.misc.PoppetData;
import moriyashiine.bewitchment.api.registry.Curse;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.item.TaglockItem;
import moriyashiine.bewitchment.common.recipe.CurseRecipe;
import moriyashiine.bewitchment.common.recipe.IncenseRecipe;
import moriyashiine.bewitchment.common.registry.*;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.FlintAndSteelItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings({"ConstantConditions", "OptionalGetWithoutIsPresent"})
public class BrazierBlockEntity extends BlockEntity implements Inventory, UsesAltarPower {
	private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(4, ItemStack.EMPTY);

	private BlockPos altarPos = null;

	public IncenseRecipe incenseRecipe = null;
	public CurseRecipe curseRecipe = null;
	private int timer = 0;

	private boolean loaded = false, hasIncense;

	public BrazierBlockEntity(BlockPos pos, BlockState state) {
		super(BWBlockEntityTypes.BRAZIER, pos, state);
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
		inventory.clear();
		Inventories.readNbt(nbt, inventory);
		timer = nbt.getInt("Timer");
		hasIncense = nbt.getBoolean("HasIncense");
	}

	@Override
	protected void writeNbt(NbtCompound nbt) {
		super.writeNbt(nbt);
		if (getAltarPos() != null) {
			nbt.putLong("AltarPos", getAltarPos().asLong());
		}
		Inventories.writeNbt(nbt, inventory);
		nbt.putInt("Timer", timer);
		nbt.putBoolean("HasIncense", hasIncense);
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
		this.altarPos = pos;
	}

	public static void tick(World world, BlockPos pos, BlockState state, BrazierBlockEntity blockEntity) {
		if (world != null) {
			if (!blockEntity.loaded) {
				if (!world.isClient && state.get(Properties.LIT)) {
					blockEntity.incenseRecipe = world.getRecipeManager().listAllOfType(BWRecipeTypes.INCENSE_RECIPE_TYPE).stream().filter(recipe -> recipe.matches(blockEntity, world)).findFirst().orElse(null);
					if (Bewitchment.config.enableCurses) {
						blockEntity.curseRecipe = world.getRecipeManager().listAllOfType(BWRecipeTypes.CURSE_RECIPE_TYPE).stream().filter(recipe -> recipe.matches(blockEntity, world)).findFirst().orElse(null);
					}
					blockEntity.hasIncense = blockEntity.incenseRecipe != null;
					blockEntity.markDirty();
					blockEntity.syncBrazier();
				}
				blockEntity.loaded = true;
			}
			if (blockEntity.timer < 0) {
				blockEntity.timer++;
				if (world.isClient) {
					if (world.random.nextBoolean()) {
						world.addParticle(blockEntity.hasIncense ? (ParticleEffect) BWParticleTypes.INCENSE_SMOKE : ParticleTypes.LARGE_SMOKE, pos.getX() + 0.5 + MathHelper.nextDouble(world.random, -0.2, 0.2), pos.getY() + (state.get(Properties.HANGING) ? 0.4 : 1.25), pos.getZ() + 0.5 + MathHelper.nextDouble(world.random, -0.2, 0.2), 0, 0.05, 0);
					}
				} else if (blockEntity.timer == 0) {
					boolean clear = blockEntity.hasIncense;
					if (blockEntity.curseRecipe != null) {
						PlayerEntity closestPlayer = world.getClosestPlayer(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 24, false);
						if (blockEntity.altarPos != null && ((WitchAltarBlockEntity) world.getBlockEntity(blockEntity.altarPos)).drain(blockEntity.curseRecipe.cost, false)) {
							if (closestPlayer != null) {
								Entity target = blockEntity.getTarget();
								if (target instanceof PlayerEntity player && BewitchmentAPI.getFamiliar(player) == BWEntityTypes.RAVEN && world.random.nextBoolean()) {
									target = closestPlayer;
								}
								if (target instanceof LivingEntity livingEntity) {
									PoppetData poppetData = BewitchmentAPI.getPoppet(world, BWObjects.CURSE_POPPET, target);
									if (!poppetData.stack.isEmpty() && poppetData.stack.hasNbt() && !poppetData.stack.getNbt().getBoolean("Cursed")) {
										poppetData.stack.getNbt().putString("Curse", BWRegistries.CURSES.getId(blockEntity.curseRecipe.curse).toString());
										poppetData.stack.getNbt().putBoolean("Cursed", true);
										TaglockItem.removeTaglock(poppetData.stack);
										poppetData.update(world, true);
									} else {
										int duration = 168000;
										if (BewitchmentAPI.getFamiliar(closestPlayer) == BWEntityTypes.RAVEN) {
											duration *= 2;
										}
										if (target instanceof PlayerEntity player && TrinketsApi.getTrinketComponent((LivingEntity) target).get().isEquipped(BWObjects.NAZAR) && BewitchmentAPI.drainMagic(player, 50, false)) {
											duration /= 2;
										}
										BWComponents.CURSES_COMPONENT.get(livingEntity).addCurse(new Curse.Instance(blockEntity.curseRecipe.curse, duration));
									}
									world.playSound(null, pos, BWSoundEvents.ENTITY_GENERIC_CURSE, SoundCategory.BLOCKS, 1, 1);
									clear = true;
								}
							} else {
								world.playSound(null, pos, BWSoundEvents.BLOCK_BRAZIER_FAIL, SoundCategory.BLOCKS, 1, 1);
								if (closestPlayer != null) {
									String entityName = "";
									for (int i = 0; i < blockEntity.size(); i++) {
										ItemStack stack = blockEntity.getStack(i);
										if (stack.getItem() instanceof TaglockItem && TaglockItem.hasTaglock(stack)) {
											entityName = TaglockItem.getTaglockName(stack);
											break;
										}
									}
									closestPlayer.sendMessage(new TranslatableText(Bewitchment.MODID + ".message.invalid_entity", entityName), true);
								}
							}
						} else {
							if (closestPlayer != null) {
								world.playSound(null, pos, BWSoundEvents.BLOCK_BRAZIER_FAIL, SoundCategory.BLOCKS, 1, 1);
								closestPlayer.sendMessage(new TranslatableText(Bewitchment.MODID + ".message.insufficent_altar_power"), true);
							}
						}
					}
					blockEntity.reset(clear);
					blockEntity.syncBrazier();
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

	public void syncBrazier() {
		sync();
	}

	private int getFirstEmptySlot() {
		for (int i = 0; i < size(); i++) {
			if (getStack(i).isEmpty()) {
				return i;
			}
		}
		return -1;
	}

	public void onUse(World world, BlockPos pos, PlayerEntity player, Hand hand) {
		if (!getCachedState().get(Properties.WATERLOGGED)) {
			ItemStack stack = player.getStackInHand(hand);
			if (getCachedState().get(Properties.LIT)) {
				world.setBlockState(pos, getCachedState().with(Properties.LIT, false));
				world.playSound(null, pos, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 1, 2);
				reset(hasIncense);
				syncBrazier();
			} else {
				if (stack.getItem() instanceof FlintAndSteelItem) {
					world.setBlockState(pos, getCachedState().with(Properties.LIT, true));
					world.playSound(null, pos, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1, 1);
					stack.damage(1, player, stackUser -> stackUser.sendToolBreakStatus(hand));
					IncenseRecipe foundIncenseRecipe = world.getRecipeManager().listAllOfType(BWRecipeTypes.INCENSE_RECIPE_TYPE).stream().filter(recipe -> recipe.matches(this, world)).findFirst().orElse(null);
					if (foundIncenseRecipe != null) {
						incenseRecipe = foundIncenseRecipe;
						timer = -6000;
						hasIncense = true;
						syncBrazier();
					} else if (Bewitchment.config.enableCurses) {
						CurseRecipe foundCurseRecipe = world.getRecipeManager().listAllOfType(BWRecipeTypes.CURSE_RECIPE_TYPE).stream().filter(recipe -> recipe.matches(this, world)).findFirst().orElse(null);
						if (foundCurseRecipe != null && getTarget() != null) {
							curseRecipe = foundCurseRecipe;
							timer = -100;
							syncBrazier();
						}
					}
				} else if (!stack.isEmpty()) {
					int firstEmpty = getFirstEmptySlot();
					if (firstEmpty != -1) {
						setStack(firstEmpty, stack.split(1));
						syncBrazier();
					}
				} else {
					reset(hasIncense);
					syncBrazier();
				}
			}
		}
		markDirty();
	}

	private Entity getTarget() {
		if (world != null && !world.isClient) {
			for (int i = 0; i < size(); i++) {
				Entity entity = BewitchmentAPI.getTaglockOwner(world, getStack(i));
				if (entity != null) {
					return entity;
				}
			}
		}
		return null;
	}

	private void reset(boolean clear) {
		if (world != null) {
			if (clear) {
				cleanInventory();
			}
			ItemScatterer.spawn(world, pos.up(getCachedState().get(Properties.HANGING) ? 0 : 1), this);
			incenseRecipe = null;
			curseRecipe = null;
			timer = 0;
			hasIncense = false;
		}
	}

	private void cleanInventory() {
		for (int i = 0; i < inventory.size(); i++) {
			ItemStack stack = getStack(i);
			if (stack.isDamageable()) {
				if (stack.damage(1, world.random, null) && stack.getDamage() == stack.getMaxDamage()) {
					stack.decrement(1);
				}
			} else {
				Item item = stack.getItem();
				setStack(i, item.hasRecipeRemainder() ? new ItemStack(item.getRecipeRemainder()) : ItemStack.EMPTY);
			}
		}
	}
}
