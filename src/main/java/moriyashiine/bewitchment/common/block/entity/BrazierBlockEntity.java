package moriyashiine.bewitchment.common.block.entity;

import dev.emi.trinkets.api.TrinketsApi;
import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.api.interfaces.block.entity.UsesAltarPower;
import moriyashiine.bewitchment.api.interfaces.entity.CurseAccessor;
import moriyashiine.bewitchment.api.registry.Curse;
import moriyashiine.bewitchment.client.network.packet.SyncBrazierBlockEntity;
import moriyashiine.bewitchment.client.network.packet.SyncClientSerializableBlockEntity;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.item.TaglockItem;
import moriyashiine.bewitchment.common.recipe.CurseRecipe;
import moriyashiine.bewitchment.common.recipe.IncenseRecipe;
import moriyashiine.bewitchment.common.registry.*;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.FlintAndSteelItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.Tickable;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.Collections;

@SuppressWarnings("ConstantConditions")
public class BrazierBlockEntity extends BlockEntity implements BlockEntityClientSerializable, Tickable, Inventory, UsesAltarPower {
	private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(4, ItemStack.EMPTY);
	
	private BlockPos altarPos = null;
	
	public IncenseRecipe incenseRecipe = null;
	public CurseRecipe curseRecipe = null;
	private int timer = 0;
	
	private boolean loaded = false, hasIncense;
	
	public BrazierBlockEntity(BlockEntityType<?> type) {
		super(type);
	}
	
	public BrazierBlockEntity() {
		this(BWBlockEntityTypes.BRAZIER);
	}
	
	@Override
	public void fromClientTag(CompoundTag tag) {
		if (tag.contains("AltarPos")) {
			setAltarPos(BlockPos.fromLong(tag.getLong("AltarPos")));
		}
		Inventories.fromTag(tag, inventory);
		timer = tag.getInt("Timer");
		hasIncense = tag.getBoolean("HasIncense");
	}
	
	@Override
	public CompoundTag toClientTag(CompoundTag tag) {
		if (getAltarPos() != null) {
			tag.putLong("AltarPos", getAltarPos().asLong());
		}
		Inventories.toTag(tag, inventory);
		tag.putInt("Timer", timer);
		tag.putBoolean("HasIncense", hasIncense);
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
	public BlockPos getAltarPos() {
		return altarPos;
	}
	
	@Override
	public void setAltarPos(BlockPos pos) {
		this.altarPos = pos;
	}
	
	@Override
	public void tick() {
		if (world != null) {
			if (!loaded) {
				if (!world.isClient && getCachedState().get(Properties.LIT)) {
					incenseRecipe = world.getRecipeManager().listAllOfType(BWRecipeTypes.INCENSE_RECIPE_TYPE).stream().filter(recipe -> recipe.matches(this, world)).findFirst().orElse(null);
					curseRecipe = world.getRecipeManager().listAllOfType(BWRecipeTypes.CURSE_RECIPE_TYPE).stream().filter(recipe -> recipe.matches(this, world)).findFirst().orElse(null);
					hasIncense = incenseRecipe != null;
					markDirty();
					syncBrazier();
				}
				loaded = true;
			}
			if (timer < 0) {
				timer++;
				if (world.isClient) {
					if (world.random.nextBoolean()) {
						world.addParticle(hasIncense ? (ParticleEffect) BWParticleTypes.INCENSE_SMOKE : ParticleTypes.LARGE_SMOKE, pos.getX() + 0.5 + MathHelper.nextDouble(world.random, -0.2, 0.2), pos.getY() + (getCachedState().get(Properties.HANGING) ? 0.4 : 1.25), pos.getZ() + 0.5 + MathHelper.nextDouble(world.random, -0.2, 0.2), 0, 0.05, 0);
					}
				}
				else if (timer == 0) {
					boolean clear = hasIncense;
					if (curseRecipe != null) {
						PlayerEntity closestPlayer = world.getClosestPlayer(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 24, false);
						if (altarPos != null && ((WitchAltarBlockEntity) world.getBlockEntity(altarPos)).drain(curseRecipe.cost, false)) {
							if (closestPlayer != null) {
								Entity target = getTarget();
								if (target instanceof PlayerEntity && BewitchmentAPI.getFamiliar((PlayerEntity) target) == BWEntityTypes.RAVEN && world.random.nextBoolean()) {
									target = closestPlayer;
								}
								if (target instanceof CurseAccessor) {
									ItemStack poppet = BewitchmentAPI.getPoppet(world, BWObjects.CURSE_POPPET, target, null);
									if (!poppet.isEmpty() && poppet.hasTag() && !poppet.getTag().getBoolean("Cursed")) {
										poppet.getTag().putString("Curse", BWRegistries.CURSES.getId(curseRecipe.curse).toString());
										poppet.getTag().putBoolean("Cursed", true);
										TaglockItem.removeTaglock(poppet);
									}
									else {
										int duration = 168000;
										if (BewitchmentAPI.getFamiliar(closestPlayer) == BWEntityTypes.RAVEN) {
											duration *= 2;
										}
										if (target instanceof PlayerEntity && TrinketsApi.getTrinketsInventory((PlayerEntity) target).containsAny(Collections.singleton(BWObjects.NAZAR)) && BewitchmentAPI.drainMagic((PlayerEntity) target, 50, false)) {
											duration /= 2;
										}
										((CurseAccessor) target).addCurse(new Curse.Instance(curseRecipe.curse, duration));
									}
									world.playSound(null, pos, BWSoundEvents.ENTITY_GENERIC_CURSE, SoundCategory.BLOCKS, 1, 1);
									clear = true;
								}
							}
							else {
								world.playSound(null, pos, BWSoundEvents.BLOCK_BRAZIER_FAIL, SoundCategory.BLOCKS, 1, 1);
								if (closestPlayer != null) {
									String entityName = "";
									for (int i = 0; i < size(); i++) {
										ItemStack stack = getStack(i);
										if (stack.getItem() instanceof TaglockItem && TaglockItem.hasTaglock(stack)) {
											entityName = TaglockItem.getTaglockName(stack);
											break;
										}
									}
									closestPlayer.sendMessage(new TranslatableText(Bewitchment.MODID + ".message.invalid_entity", entityName), true);
								}
							}
						}
						else {
							if (closestPlayer != null) {
								world.playSound(null, pos, BWSoundEvents.BLOCK_BRAZIER_FAIL, SoundCategory.BLOCKS, 1, 1);
								closestPlayer.sendMessage(new TranslatableText(Bewitchment.MODID + ".message.insufficent_altar_power"), true);
							}
						}
					}
					reset(clear);
					syncBrazier();
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
		if (world instanceof ServerWorld) {
			PlayerLookup.tracking(this).forEach(playerEntity -> {
				SyncClientSerializableBlockEntity.send(playerEntity, this);
				SyncBrazierBlockEntity.send(playerEntity, this);
			});
		}
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
			}
			else {
				if (stack.getItem() instanceof FlintAndSteelItem) {
					world.setBlockState(pos, getCachedState().with(Properties.LIT, true));
					world.playSound(null, pos, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1, 1);
					stack.damage(1, player, user -> user.sendToolBreakStatus(hand));
					IncenseRecipe foundIncenseRecipe = world.getRecipeManager().listAllOfType(BWRecipeTypes.INCENSE_RECIPE_TYPE).stream().filter(recipe -> recipe.matches(this, world)).findFirst().orElse(null);
					if (foundIncenseRecipe != null) {
						incenseRecipe = foundIncenseRecipe;
						timer = -6000;
						hasIncense = true;
						syncBrazier();
					}
					else {
						CurseRecipe foundCurseRecipe = world.getRecipeManager().listAllOfType(BWRecipeTypes.CURSE_RECIPE_TYPE).stream().filter(recipe -> recipe.matches(this, world)).findFirst().orElse(null);
						if (foundCurseRecipe != null && getTarget() != null) {
							curseRecipe = foundCurseRecipe;
							timer = -100;
							syncBrazier();
						}
					}
				}
				else if (!stack.isEmpty()) {
					int firstEmpty = getFirstEmptySlot();
					if (firstEmpty != -1) {
						setStack(firstEmpty, stack.split(1));
						syncBrazier();
					}
				}
				else {
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
			}
			else {
				Item item = stack.getItem();
				setStack(i, item.hasRecipeRemainder() ? new ItemStack(item.getRecipeRemainder()) : ItemStack.EMPTY);
			}
		}
	}
}
