package moriyashiine.bewitchment.common.entity;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.api.entity.BroomEntity;
import moriyashiine.bewitchment.api.item.SigilItem;
import moriyashiine.bewitchment.api.registry.Sigil;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.item.AthameItem;
import moriyashiine.bewitchment.common.item.TaglockItem;
import moriyashiine.bewitchment.common.registry.BWEntityTypes;
import moriyashiine.bewitchment.common.registry.BWRegistries;
import moriyashiine.bewitchment.common.registry.BWSoundEvents;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SuppressWarnings("ConstantConditions")
public class DragonsBloodBroomEntity extends BroomEntity {
	private final List<UUID> entities = new ArrayList<>();
	
	private Sigil sigil = null;
	private int uses = 0;
	
	private boolean modeOnWhitelist = false;
	
	public DragonsBloodBroomEntity(EntityType<?> type, World world) {
		super(type, world);
	}
	
	@Override
	public void tick() {
		super.tick();
		if (!world.isClient && sigil != null && uses <= 0) {
			entities.clear();
			sigil = null;
			uses = 0;
			modeOnWhitelist = false;
		}
	}
	
	@Override
	public ActionResult interact(PlayerEntity player, Hand hand) {
		boolean client = world.isClient;
		if (player.isSneaking()) {
			if (!client && player.getUuid().equals(getOwner())) {
				ItemStack stack = player.getStackInHand(hand);
				if (stack.getItem() instanceof SigilItem && ((SigilItem) stack.getItem()).sigil.active) {
					sigil = ((SigilItem) stack.getItem()).sigil;
					uses = sigil.uses * (BewitchmentAPI.getFamiliar(player) == BWEntityTypes.SNAKE ? 2 : 1);
					modeOnWhitelist = true;
					stack.decrement(1);
				}
				else if (sigil != null) {
					if (stack.getItem() instanceof TaglockItem && TaglockItem.hasTaglock(stack)) {
						entities.add(TaglockItem.getTaglockUUID(stack));
						stack.decrement(1);
					}
					else if (stack.getItem() instanceof AthameItem && !entities.isEmpty()) {
						world.playSound(null, getBlockPos(), BWSoundEvents.BLOCK_SIGIL_PLING, SoundCategory.NEUTRAL, 1, modeOnWhitelist ? 0.5f : 1);
						player.sendMessage(new TranslatableText(Bewitchment.MODID + ".message.toggle_" + (!modeOnWhitelist ? "whitelist" : "blacklist")), true);
						modeOnWhitelist = !modeOnWhitelist;
					}
				}
			}
			return ActionResult.success(client);
		}
		if (!world.isClient && hand == Hand.MAIN_HAND && sigil != null) {
			if (sigil != null && sigil.active && test(player)) {
				ActionResult result = sigil.use(world, getBlockPos(), player, hand);
				if (result == ActionResult.SUCCESS) {
					uses--;
				}
			}
		}
		return super.interact(player, hand);
	}
	
	@Override
	protected void readCustomDataFromTag(CompoundTag tag) {
		super.readCustomDataFromTag(tag);
		readFromTag(tag);
	}
	
	@Override
	protected void writeCustomDataToTag(CompoundTag tag) {
		super.writeCustomDataToTag(tag);
		if (sigil != null) {
			writeToTag(tag);
		}
	}
	
	@Override
	public void init(ItemStack stack) {
		if (stack.hasTag()) {
			readFromTag(stack.getTag());
		}
	}
	
	@Override
	protected ItemStack getDroppedStack() {
		ItemStack stack = super.getDroppedStack();
		if (sigil != null) {
			writeToTag(stack.getOrCreateTag());
		}
		else if (stack.hasTag()) {
			stack.getTag().remove("Entities");
			stack.getTag().remove("Sigil");
			stack.getTag().remove("Uses");
			stack.getTag().remove("ModeOnWhitelist");
		}
		return stack;
	}
	
	private void readFromTag(CompoundTag tag) {
		if (tag.contains("Sigil")) {
			ListTag entities = tag.getList("Entities", NbtType.STRING);
			for (int i = 0; i < entities.size(); i++) {
				this.entities.add(UUID.fromString(entities.getString(i)));
			}
			sigil = BWRegistries.SIGILS.get(new Identifier(tag.getString("Sigil")));
			uses = tag.getInt("Uses");
			modeOnWhitelist = tag.getBoolean("ModeOnWhitelist");
		}
	}
	
	private void writeToTag(CompoundTag tag) {
		if (sigil != null) {
			ListTag entities = new ListTag();
			for (UUID entity : this.entities) {
				entities.add(StringTag.of(entity.toString()));
			}
			tag.put("Entities", entities);
			tag.putString("Sigil", BWRegistries.SIGILS.getId(sigil).toString());
			tag.putInt("Uses", uses);
			tag.putBoolean("ModeOnWhitelist", modeOnWhitelist);
		}
	}
	
	private boolean test(Entity entity) {
		if (!entities.isEmpty()) {
			if (modeOnWhitelist) {
				return entities.contains(entity.getUuid());
			}
			return !entities.contains(entity.getUuid());
		}
		return true;
	}
}
