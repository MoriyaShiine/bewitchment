package moriyashiine.bewitchment.common.block.entity;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.api.interfaces.UsesAltarPower;
import moriyashiine.bewitchment.client.network.packet.SyncClientSerializableBlockEntity;
import moriyashiine.bewitchment.common.item.TaglockItem;
import moriyashiine.bewitchment.common.recipe.CauldronBrewingRecipe;
import moriyashiine.bewitchment.common.recipe.OilRecipe;
import moriyashiine.bewitchment.common.registry.*;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
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
import net.minecraft.nbt.CompoundTag;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Nameable;
import net.minecraft.util.Tickable;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ConstantConditions")
public class WitchCauldronBlockEntity extends BlockEntity implements BlockEntityClientSerializable, Tickable, Inventory, Nameable, UsesAltarPower {
	private static final TranslatableText DEFAULT_NAME = new TranslatableText(BWObjects.WITCH_CAULDRON.getTranslationKey());
	
	private Box box;
	
	private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(4, ItemStack.EMPTY);
	
	public OilRecipe oilRecipe = null;
	
	public Mode mode = Mode.NORMAL;
	
	public Text customName;
	
	public int color = 0x3f76e4, heatTimer = 0;
	
	private BlockPos altarPos = null;
	
	private boolean loaded = false;
	
	public WitchCauldronBlockEntity(BlockEntityType<?> type) {
		super(type);
	}
	
	public WitchCauldronBlockEntity() {
		this(BWBlockEntityTypes.WITCH_CAULDRON);
	}
	
	@Override
	public void fromClientTag(CompoundTag tag) {
		Inventories.fromTag(tag, inventory);
		mode = Mode.valueOf(tag.getString("Mode"));
		if (tag.contains("CustomName", NbtType.STRING)) {
			customName = Text.Serializer.fromJson(tag.getString("CustomName"));
		}
		if (tag.contains("Color")) {
			color = tag.getInt("Color");
		}
		heatTimer = tag.getInt("HeatTimer");
		if (tag.contains("AltarPos")) {
			setAltarPos(BlockPos.fromLong(tag.getLong("AltarPos")));
		}
	}
	
	@Override
	public CompoundTag toClientTag(CompoundTag tag) {
		Inventories.toTag(tag, inventory);
		tag.putString("Mode", mode.name);
		if (customName != null) {
			tag.putString("CustomName", Text.Serializer.toJson(customName));
		}
		tag.putInt("Color", color);
		tag.putInt("HeatTimer", heatTimer);
		if (getAltarPos() != null) {
			tag.putLong("AltarPos", getAltarPos().asLong());
		}
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
	public Text getName() {
		return hasCustomName() ? getCustomName() : DEFAULT_NAME;
	}
	
	@Nullable
	@Override
	public Text getCustomName() {
		return customName;
	}
	
	@Override
	public BlockPos getAltarPos() {
		return altarPos;
	}
	
	@Override
	public void setAltarPos(BlockPos pos) {
		altarPos = pos;
	}
	
	@Override
	public void tick() {
		if (world != null) {
			if (!loaded) {
				markDirty();
				box = new Box(pos).contract(0.75);
				oilRecipe = world.getRecipeManager().listAllOfType(BWRecipeTypes.OIL_RECIPE_TYPE).stream().filter(recipe -> recipe.matches(this, world)).findFirst().orElse(null);
				loaded = true;
			}
			heatTimer = MathHelper.clamp(heatTimer + (getCachedState().get(Properties.LIT) && getCachedState().get(Properties.LEVEL_3) > 0 ? 1 : -1), 0, 160);
			if (world.isClient) {
				if (heatTimer >= 60) {
					float fluidHeight = 0;
					float width = 0.35f;
					switch (getCachedState().get(Properties.LEVEL_3)) {
						case 1:
							fluidHeight = 0.225f;
							break;
						case 2:
							fluidHeight = 0.425f;
							width = 0.3f;
							break;
						case 3:
							fluidHeight = 0.625f;
					}
					if (fluidHeight > 0) {
						if (mode == Mode.TELEPORTATION) {
							world.addParticle(new DustParticleEffect(((color >> 16) & 0xff) / 255f, ((color >> 8) & 0xff) / 255f, (color & 0xff) / 255f, 1), pos.getX() + 0.5 + MathHelper.nextDouble(world.random, -width, width), pos.getY() + fluidHeight, pos.getZ() + 0.5 + MathHelper.nextDouble(world.random, -width, width), 0, 0, 0);
						}
						if (mode == Mode.OIL_CRAFTING && color != 0xff3fff) {
							world.addParticle(ParticleTypes.ENCHANTED_HIT, pos.getX() + 0.5 + MathHelper.nextDouble(world.random, -width, width), pos.getY() + fluidHeight, pos.getZ() + 0.5 + MathHelper.nextDouble(world.random, -width, width), 0, 0, 0);
						}
						if (mode == Mode.BREWING) {
							world.addParticle(ParticleTypes.ENTITY_EFFECT, pos.getX() + 0.5 + MathHelper.nextDouble(world.random, -width, width), pos.getY() + fluidHeight, pos.getZ() + 0.5 + MathHelper.nextDouble(world.random, -width, width), ((color >> 16) & 0xff) / 255f, ((color >> 8) & 0xff) / 255f, (color & 0xff) / 255f);
						}
						world.addParticle((ParticleEffect) BWParticleTypes.CAULDRON_BUBBLE, pos.getX() + 0.5 + MathHelper.nextDouble(world.random, -width, width), pos.getY() + fluidHeight, pos.getZ() + 0.5 + MathHelper.nextDouble(world.random, -width, width), ((color >> 16) & 0xff) / 255f, ((color >> 8) & 0xff) / 255f, (color & 0xff) / 255f);
					}
				}
			}
			else {
				if (getCachedState().get(Properties.LEVEL_3) > 0 && heatTimer >= 60) {
					if (world.random.nextFloat() <= 0.075f) {
						world.playSound(null, pos, SoundEvents.BLOCK_BUBBLE_COLUMN_UPWARDS_AMBIENT, SoundCategory.BLOCKS, 1 / 3f, mode == Mode.FAILED ? 0.5f : 1);
					}
					if (world.getTime() % 5 == 0) {
						world.getEntitiesByType(EntityType.ITEM, box, entity -> true).forEach(itemEntity -> {
							if (itemEntity.getStack().getItem() == BWObjects.WOOD_ASH || getCachedState().get(Properties.LEVEL_3) == 3) {
								world.playSound(null, pos, SoundEvents.ENTITY_GENERIC_SPLASH, SoundCategory.BLOCKS, 1 / 3f, 1);
								ItemStack stack = itemEntity.getStack();
								if (stack.getItem().hasRecipeRemainder()) {
									ItemEntity remainder = new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, new ItemStack(stack.getItem().getRecipeRemainder()));
									remainder.setVelocity(Vec3d.ZERO);
									remainder.setNoGravity(true);
									world.spawnEntity(remainder);
								}
								mode = insertStack(stack.split(1));
								syncCauldron();
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
	
	public void syncCauldron() {
		if (world instanceof ServerWorld) {
			PlayerLookup.tracking(this).forEach(playerEntity -> SyncClientSerializableBlockEntity.send(playerEntity, this));
		}
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
			world.setBlockState(pos, getCachedState().with(Properties.LEVEL_3, 0));
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
			}
			else if (mode != Mode.FAILED && mode != Mode.TELEPORTATION) {
				int firstEmpty = getFirstEmptySlot();
				if (firstEmpty != -1) {
					setStack(firstEmpty, stack);
					if (mode == Mode.NORMAL && stack.getItem() == BWObjects.MANDRAKE_ROOT) {
						clear();
						return Mode.BREWING;
					}
					if (mode == Mode.BREWING) {
						CauldronBrewingRecipe cauldronBrewingRecipe = world.getRecipeManager().listAllOfType(BWRecipeTypes.CAULDRON_BREWING_RECIPE_TYPE).stream().filter(recipe -> recipe.input.test(stack)).findFirst().orElse(null);
						if (cauldronBrewingRecipe != null || (firstEmpty > 0 && (stack.getItem() == Items.REDSTONE || stack.getItem() == Items.GLOWSTONE_DUST))) {
							BlockPos altarPos = getAltarPos();
							if (altarPos != null && ((WitchAltarBlockEntity) world.getBlockEntity(altarPos)).drain(getBrewCost(), true)) {
								setColor(PotionUtil.getColor(getPotion(null)));
								return Mode.BREWING;
							}
						}
					}
					else {
						oilRecipe = world.getRecipeManager().listAllOfType(BWRecipeTypes.OIL_RECIPE_TYPE).stream().filter(recipe -> recipe.matches(this, world)).findFirst().orElse(null);
						if (oilRecipe != null) {
							setColor(oilRecipe.color);
							return Mode.OIL_CRAFTING;
						}
						else if (firstEmpty == 0 && stack.getItem() == Items.ENDER_PEARL) {
							clear();
							setColor(0x319a89);
							return Mode.TELEPORTATION;
						}
						setColor(0xde6fa1);
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
			int redstone = 0, glowstone = 0;
			for (int i = 0; i < size(); i++) {
				ItemStack stackInSlot = getStack(i);
				if (stackInSlot.getItem() instanceof TaglockItem && TaglockItem.hasTaglock(stackInSlot) && stackInSlot.getOrCreateTag().getBoolean("FromPlayer")) {
					stack.getOrCreateTag().putUuid("PolymorphUUID", stackInSlot.getOrCreateTag().getUuid("OwnerUUID"));
					stack.getOrCreateTag().putString("PolymorphName", stackInSlot.getOrCreateTag().getString("OwnerName"));
				}
				CauldronBrewingRecipe cauldronBrewingRecipe = world.getRecipeManager().listAllOfType(BWRecipeTypes.CAULDRON_BREWING_RECIPE_TYPE).stream().filter(recipe -> recipe.input.test(stackInSlot)).findFirst().orElse(null);
				if (cauldronBrewingRecipe != null) {
					effects.add(new StatusEffectInstance(cauldronBrewingRecipe.output, cauldronBrewingRecipe.time));
				}
				else if (stackInSlot.getItem() == Items.REDSTONE) {
					redstone++;
				}
				else if (stackInSlot.getItem() == Items.GLOWSTONE_DUST) {
					glowstone++;
				}
			}
			for (int i = 0; i < effects.size(); i++) {
				for (int j = 0; j < redstone; j++) {
					effects.set(i, new StatusEffectInstance(effects.get(i).getEffectType(), effects.get(i).getDuration() * 2));
				}
				for (int j = 0; j < glowstone; j++) {
					effects.set(i, new StatusEffectInstance(effects.get(i).getEffectType(), effects.get(i).getDuration() / 2, effects.get(i).getAmplifier() + 1));
				}
			}
			List<StatusEffectInstance> finalEffects = new ArrayList<>();
			for (int i = effects.size() - 1; i >= 0; i--) {
				if (effects.get(i).getEffectType() == BWStatusEffects.ABSENCE || effects.get(i).getEffectType() == BWStatusEffects.CORRUPTION) {
					finalEffects.add(effects.remove(i));
				}
			}
			finalEffects.addAll(effects);
			if (creator != null) {
				boolean wearingAlchemistRobes = BewitchmentAPI.getArmorPieces(creator, armorStack -> armorStack.getItem() instanceof ArmorItem && ((ArmorItem) armorStack.getItem()).getMaterial() == BWMaterials.ALCHEMIST_ARMOR) >= 3;
				boolean pledgedToLeonard = BewitchmentAPI.isPledged(world, BWPledges.LEONARD_UUID, creator.getUuid());
				if (wearingAlchemistRobes || pledgedToLeonard) {
					for (int i = 0; i < finalEffects.size(); i++) {
						StatusEffect type = finalEffects.get(i).getEffectType();
						int duration = finalEffects.get(i).getDuration();
						finalEffects.set(i, new StatusEffectInstance(type, type.isInstant() || !wearingAlchemistRobes ? duration : duration * 2, finalEffects.get(i).getAmplifier() + (pledgedToLeonard ? 1 : 0)));
					}
				}
			}
			PotionUtil.setCustomPotionEffects(stack, finalEffects);
			stack.getOrCreateTag().putInt("CustomPotionColor", PotionUtil.getColor(finalEffects));
			stack.getOrCreateTag().putBoolean("BewitchmentBrew", true);
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
		int level = getCachedState().get(Properties.LEVEL_3);
		if (mode == Mode.NORMAL) {
			if (item == Items.BUCKET && level == 3) {
				return 0;
			}
			else if (item == Items.WATER_BUCKET && level == 0) {
				return 3;
			}
			else if (item == Items.GLASS_BOTTLE) {
				return level - 1;
			}
			else if (item == Items.POTION && level < 3 && PotionUtil.getPotion(stack) == Potions.WATER) {
				return level + 1;
			}
		}
		else if (mode == Mode.OIL_CRAFTING) {
			if (oilRecipe != null && item == Items.GLASS_BOTTLE) {
				return level - 1;
			}
		}
		else if (mode == Mode.BREWING) {
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
