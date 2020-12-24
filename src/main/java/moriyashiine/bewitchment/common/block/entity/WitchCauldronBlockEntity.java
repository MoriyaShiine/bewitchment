package moriyashiine.bewitchment.common.block.entity;

import moriyashiine.bewitchment.api.interfaces.UsesAltarPower;
import moriyashiine.bewitchment.api.registry.OilRecipe;
import moriyashiine.bewitchment.client.network.packet.SyncClientSerializableBlockEntity;
import moriyashiine.bewitchment.common.registry.BWBlockEntityTypes;
import moriyashiine.bewitchment.common.registry.BWObjects;
import moriyashiine.bewitchment.common.registry.BWParticleTypes;
import moriyashiine.bewitchment.common.registry.BWRecipeTypes;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.fabricmc.fabric.api.server.PlayerStream;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.particle.ParticleEffect;
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
import org.jetbrains.annotations.Nullable;

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
	public CompoundTag toTag(CompoundTag tag) {
		return super.toTag(toClientTag(tag));
	}
	
	@Override
	public void fromTag(BlockState state, CompoundTag tag) {
		fromClientTag(tag);
		super.fromTag(state, tag);
	}
	
	@Override
	public void tick() {
		if (world != null) {
			if (!loaded) {
				markDirty();
				box = new Box(pos);
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
								mode = insertStack(itemEntity.getStack().split(1));
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
			PlayerStream.watching(this).forEach(playerEntity -> SyncClientSerializableBlockEntity.send(playerEntity, this));
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
					oilRecipe = world.getRecipeManager().listAllOfType(BWRecipeTypes.OIL_RECIPE_TYPE).stream().filter(recipe -> recipe.matches(this, world)).findFirst().orElse(null);
					if (oilRecipe != null) {
						setColor(oilRecipe.color);
						return Mode.OIL_CRAFTING;
					}
					else if (firstEmpty == 0 && stack.getItem() == Items.ENDER_PEARL) {
						setColor(0x7fff7f);
						return Mode.TELEPORTATION;
					}
					setColor(0xff3fff);
					return Mode.OIL_CRAFTING;
				}
			}
		}
		setColor(0x3f0000);
		return Mode.FAILED;
	}
	
	public enum Mode {
		NORMAL("NORMAL"), OIL_CRAFTING("OIL_CRAFTING"), TELEPORTATION("TELEPORTATION"), FAILED("FAILED");
		
		public final String name;
		
		Mode(String name) {
			this.name = name;
		}
	}
}
