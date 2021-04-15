package moriyashiine.bewitchment.common.item;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import com.jamieswhiteshirt.reachentityattributes.ReachEntityAttributes;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.block.dragonsblood.DragonsBloodLogBlock;
import moriyashiine.bewitchment.common.block.entity.interfaces.Lockable;
import moriyashiine.bewitchment.common.block.entity.interfaces.SigilHolder;
import moriyashiine.bewitchment.common.block.entity.interfaces.TaglockHolder;
import moriyashiine.bewitchment.common.registry.BWProperties;
import moriyashiine.bewitchment.common.registry.BWSoundEvents;
import net.minecraft.block.BlockState;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.DoorBlock;
import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.block.dispenser.FallibleItemDispenserBehavior;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.property.Properties;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.UUID;

public class AthameItem extends SwordItem {
	private static final EntityAttributeModifier REACH_MODIFIER = new EntityAttributeModifier(UUID.fromString("1f362972-c5c5-4e9d-b69f-1fd13bd269e3"), "Weapon modifier", -1, EntityAttributeModifier.Operation.ADDITION);
	
	private static final DispenserBehavior DISPENSER_BEHAVIOR = new FallibleItemDispenserBehavior() {
		@Override
		protected ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
			World world = pointer.getWorld();
			BlockPos pos = pointer.getBlockPos();
			setSuccess(cutLog(world, pos.offset(pointer.getBlockState().get(Properties.FACING)), stack));
			return stack;
		}
	};
	
	public AthameItem(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
		super(toolMaterial, attackDamage, attackSpeed, settings);
		DispenserBlock.registerBehavior(this, DISPENSER_BEHAVIOR);
	}
	
	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		World world = context.getWorld();
		BlockPos pos = context.getBlockPos();
		BlockState state = world.getBlockState(pos);
		PlayerEntity player = context.getPlayer();
		boolean client = world.isClient;
		BlockEntity blockEntity = world.getBlockEntity(state.getBlock() instanceof DoorBlock && state.get(DoorBlock.HALF) == DoubleBlockHalf.UPPER ? pos.down() : pos);
		if (blockEntity instanceof SigilHolder) {
			SigilHolder sigil = (SigilHolder) blockEntity;
			if (player != null && player.getUuid().equals(sigil.getOwner())) {
				if (!client && !sigil.getEntities().isEmpty()) {
					boolean whitelist = sigil.getModeOnWhitelist();
					world.playSound(null, pos, BWSoundEvents.BLOCK_SIGIL_PLING, SoundCategory.BLOCKS, 1, whitelist ? 0.5f : 1);
					player.sendMessage(new TranslatableText(Bewitchment.MODID + ".message.toggle_" + (!whitelist ? "whitelist" : "blacklist")), true);
					sigil.setModeOnWhitelist(!whitelist);
					blockEntity.markDirty();
				}
				return ActionResult.success(client);
			}
		}
		else if (blockEntity instanceof TaglockHolder) {
			TaglockHolder taglockHolder = (TaglockHolder) blockEntity;
			if (player != null && player.getUuid().equals(taglockHolder.getOwner()) && taglockHolder.getFirstEmptySlot() != 0) {
				if (!client) {
					ItemScatterer.spawn(world, pos, taglockHolder.getTaglockInventory());
					taglockHolder.syncTaglockHolder(world, blockEntity);
					blockEntity.markDirty();
				}
				return ActionResult.success(client);
			}
		}
		else if (blockEntity instanceof Lockable) {
			Lockable lockable = (Lockable) blockEntity;
			if (player != null && player.getUuid().equals(lockable.getOwner()) && !lockable.getEntities().isEmpty()) {
				if (!client) {
					boolean whitelist = lockable.getModeOnWhitelist();
					world.playSound(null, pos, BWSoundEvents.BLOCK_SIGIL_PLING, SoundCategory.BLOCKS, 1, whitelist ? 0.5f : 1);
					player.sendMessage(new TranslatableText(Bewitchment.MODID + ".message.toggle_" + (!whitelist ? "whitelist" : "blacklist")), true);
					lockable.setModeOnWhitelist(!whitelist);
					blockEntity.markDirty();
				}
				return ActionResult.success(client);
			}
		}
		return super.useOnBlock(context);
	}
	
	@Override
	public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot slot) {
		Multimap<EntityAttribute, EntityAttributeModifier> map = LinkedHashMultimap.create(super.getAttributeModifiers(slot));
		if (slot == EquipmentSlot.MAINHAND) {
			map.put(ReachEntityAttributes.ATTACK_RANGE, REACH_MODIFIER);
		}
		return map;
	}
	
	private static boolean cutLog(World world, BlockPos pos, ItemStack stack) {
		BlockState state = world.getBlockState(pos);
		if (state.getBlock() instanceof DragonsBloodLogBlock && state.get(BWProperties.NATURAL) && !state.get(BWProperties.CUT)) {
			world.playSound(null, pos, BWSoundEvents.ITEM_ATHAME_STRIP, SoundCategory.BLOCKS, 1, 1);
			world.setBlockState(pos, state.with(BWProperties.CUT, true));
			if (stack.damage(1, world.random, null) && stack.getDamage() >= stack.getMaxDamage()) {
				stack.decrement(1);
			}
			return true;
		}
		return false;
	}
}
