package moriyashiine.bewitchment.common.entity;

import moriyashiine.bewitchment.api.entity.BroomEntity;
import moriyashiine.bewitchment.common.item.AthameItem;
import moriyashiine.bewitchment.common.item.TaglockItem;
import moriyashiine.bewitchment.common.registry.BWSoundEvents;
import moriyashiine.bewitchment.common.registry.BWTags;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SuppressWarnings("ConstantConditions")
public class ElderBroomEntity extends BroomEntity {
	private final List<UUID> entities = new ArrayList<>();
	
	private boolean modeOnWhitelist = false;
	private boolean locked = false;
	
	public ElderBroomEntity(EntityType<?> type, World world) {
		super(type, world);
	}
	
	@Override
	public ActionResult interact(PlayerEntity player, Hand hand) {
		boolean client = world.isClient;
		if (player.isSneaking()) {
			if (!client && player.getUuid().equals(getOwner())) {
				ItemStack stack = player.getStackInHand(hand);
				if (BWTags.SILVER_INGOTS.contains(stack.getItem()) && !locked) {
					modeOnWhitelist = true;
					locked = true;
					stack.decrement(1);
				}
				else if (locked) {
					if (stack.getItem() instanceof TaglockItem && TaglockItem.hasTaglock(stack)) {
						entities.add(TaglockItem.getTaglockUUID(stack));
						stack.decrement(1);
					}
					else if (stack.getItem() instanceof AthameItem && !entities.isEmpty()) {
						world.playSound(null, getBlockPos(), BWSoundEvents.ENTITY_GENERIC_PLING, SoundCategory.NEUTRAL, 1, modeOnWhitelist ? 0.5f : 1);
						modeOnWhitelist = !modeOnWhitelist;
					}
				}
			}
			return ActionResult.success(client);
		}
		boolean allowed = true;
		if (locked && !player.getUuid().equals(getOwner())) {
			allowed = !entities.isEmpty() && modeOnWhitelist && entities.contains(player.getUuid());
		}
		if (allowed) {
			return super.interact(player, hand);
		}
		return ActionResult.FAIL;
	}
	
	@Override
	protected void readCustomDataFromTag(CompoundTag tag) {
		super.readCustomDataFromTag(tag);
		ListTag entities = tag.getList("Entities", NbtType.STRING);
		for (int i = 0; i < entities.size(); i++) {
			this.entities.add(UUID.fromString(entities.getString(i)));
		}
		modeOnWhitelist = tag.getBoolean("ModeOnWhitelist");
		locked = tag.getBoolean("Locked");
	}
	
	@Override
	protected void writeCustomDataToTag(CompoundTag tag) {
		super.writeCustomDataToTag(tag);
		ListTag entities = new ListTag();
		for (UUID entity : this.entities) {
			entities.add(StringTag.of(entity.toString()));
		}
		tag.put("Entities", entities);
		tag.putBoolean("ModeOnWhitelist", modeOnWhitelist);
		tag.putBoolean("Locked", locked);
	}
	
	@Override
	public void init(ItemStack stack) {
		if (stack.hasTag()) {
			CompoundTag tag = stack.getTag();
			ListTag entities = tag.getList("Entities", NbtType.STRING);
			for (int i = 0; i < entities.size(); i++) {
				this.entities.add(UUID.fromString(entities.getString(i)));
			}
			modeOnWhitelist = tag.getBoolean("ModeOnWhitelist");
			locked = tag.getBoolean("Locked");
		}
	}
	
	@Override
	protected ItemStack getDroppedStack() {
		ItemStack stack = super.getDroppedStack();
		if (locked) {
			CompoundTag tag = stack.getOrCreateTag();
			ListTag entities = new ListTag();
			for (UUID entity : this.entities) {
				entities.add(StringTag.of(entity.toString()));
			}
			tag.put("Entities", entities);
			tag.putBoolean("ModeOnWhitelist", modeOnWhitelist);
			tag.putBoolean("Locked", locked);
		}
		return stack;
	}
}
