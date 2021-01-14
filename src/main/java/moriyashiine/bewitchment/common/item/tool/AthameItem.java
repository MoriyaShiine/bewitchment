package moriyashiine.bewitchment.common.item.tool;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import com.jamieswhiteshirt.reachentityattributes.ReachEntityAttributes;
import moriyashiine.bewitchment.api.interfaces.HasSigil;
import moriyashiine.bewitchment.common.block.dragonsblood.DragonsBloodLogBlock;
import moriyashiine.bewitchment.common.recipe.AthameStrippingRecipe;
import moriyashiine.bewitchment.common.registry.BWMaterials;
import moriyashiine.bewitchment.common.registry.BWProperties;
import moriyashiine.bewitchment.common.registry.BWRecipeTypes;
import moriyashiine.bewitchment.common.registry.BWSoundEvents;
import net.minecraft.block.BlockState;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.PillarBlock;
import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.block.dispenser.FallibleItemDispenserBehavior;
import net.minecraft.block.entity.BlockEntity;
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
import net.minecraft.util.ActionResult;
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
		BlockEntity blockEntity = world.getBlockEntity(pos);
		PlayerEntity player = context.getPlayer();
		boolean client = world.isClient;
		if (blockEntity instanceof HasSigil) {
			HasSigil sigil = (HasSigil) blockEntity;
			if (player != null && player.getUuid().equals(sigil.getOwner())) {
				boolean whitelist = sigil.getModeOnWhitelist();
				world.playSound(null, pos, BWSoundEvents.BLOCK_SIGIL_PLING, SoundCategory.BLOCKS, 1, whitelist ? 0.5f : 1);
				sigil.setModeOnWhitelist(!whitelist);
				blockEntity.markDirty();
				return ActionResult.success(client);
			}
		}
		BlockState state = world.getBlockState(pos);
		AthameStrippingRecipe entry = world.getRecipeManager().listAllOfType(BWRecipeTypes.ATHAME_STRIPPING_RECIPE_TYPE).stream().filter(recipe -> recipe.log == state.getBlock()).findFirst().orElse(null);
		if (entry != null) {
			world.playSound(player, pos, BWSoundEvents.ITEM_ATHAME_STRIP, SoundCategory.BLOCKS, 1, 1);
			if (!client) {
				world.setBlockState(pos, entry.strippedLog.getDefaultState().with(PillarBlock.AXIS, state.get(PillarBlock.AXIS)), 11);
				if (player != null) {
					context.getStack().damage(1, player, (user) -> user.sendToolBreakStatus(context.getHand()));
					if (world.random.nextBoolean()) {
						ItemStack bark = entry.getOutput().copy();
						if (!player.inventory.insertStack(bark)) {
							player.dropStack(bark);
						}
					}
				}
			}
			return ActionResult.success(client);
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
			stack.damage(1, world.random, null);
			if (stack.getDamage() == BWMaterials.SILVER_TOOL.getDurability()) {
				stack.decrement(1);
			}
			return true;
		}
		return false;
	}
}
