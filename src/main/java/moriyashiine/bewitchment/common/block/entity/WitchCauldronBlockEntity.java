package moriyashiine.bewitchment.common.block.entity;

import moriyashiine.bewitchment.common.registry.BWBlockEntityTypes;
import moriyashiine.bewitchment.common.registry.BWObjects;
import moriyashiine.bewitchment.common.registry.BWParticleTypes;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Nameable;
import net.minecraft.util.Tickable;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;

public class WitchCauldronBlockEntity extends BlockEntity implements BlockEntityClientSerializable, Tickable, Inventory, Nameable {
	private static final TranslatableText DEFAULT_NAME = new TranslatableText(BWObjects.WITCH_CAULDRON.getTranslationKey());
	
	private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(4, ItemStack.EMPTY);
	
	public Text customName;
	
	public int color = 0x3f76e4, heatTimer = 0;
	
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
		if (customName != null) {
			tag.putString("CustomName", Text.Serializer.toJson(customName));
		}
		tag.putInt("Color", color);
		tag.putInt("HeatTimer", heatTimer);
		return tag;
	}
	
	@Override
	public void fromClientTag(CompoundTag tag) {
		Inventories.fromTag(tag, inventory);
		if (tag.contains("CustomName", NbtType.STRING)) {
			customName = Text.Serializer.fromJson(tag.getString("CustomName"));
		}
		if (tag.contains("Color")) {
			color = tag.getInt("Color");
		}
		heatTimer = tag.getInt("HeatTimer");
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
				if (world.random.nextFloat() <= 0.075f && getCachedState().get(Properties.LEVEL_3) > 0 && heatTimer >= 60) {
					world.playSound(null, pos, SoundEvents.BLOCK_BUBBLE_COLUMN_UPWARDS_AMBIENT, SoundCategory.BLOCKS, 1, 1);
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
	
	public void setColor(int color) {
		if (world != null) {
			this.color = color;
			world.setBlockState(pos, getCachedState().with(Properties.LIT, !getCachedState().get(Properties.LIT)));
			world.setBlockState(pos, getCachedState().with(Properties.LIT, !getCachedState().get(Properties.LIT)));
		}
	}
}
