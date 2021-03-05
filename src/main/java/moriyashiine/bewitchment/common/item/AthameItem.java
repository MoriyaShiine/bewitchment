package moriyashiine.bewitchment.common.item;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import com.jamieswhiteshirt.reachentityattributes.ReachEntityAttributes;
import moriyashiine.bewitchment.api.event.AthameSacrificeCallback;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.block.dragonsblood.DragonsBloodLogBlock;
import moriyashiine.bewitchment.common.block.entity.BrazierBlockEntity;
import moriyashiine.bewitchment.common.block.entity.GlyphBlockEntity;
import moriyashiine.bewitchment.common.block.entity.interfaces.Lockable;
import moriyashiine.bewitchment.common.block.entity.interfaces.SigilHolder;
import moriyashiine.bewitchment.common.block.entity.interfaces.TaglockHolder;
import moriyashiine.bewitchment.common.misc.BWUtil;
import moriyashiine.bewitchment.common.recipe.AthameStrippingRecipe;
import moriyashiine.bewitchment.common.registry.*;
import net.minecraft.block.BlockState;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.DoorBlock;
import net.minecraft.block.PillarBlock;
import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.block.dispenser.FallibleItemDispenserBehavior;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.property.Properties;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.explosion.Explosion;

import java.util.UUID;

@SuppressWarnings("ConstantConditions")
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
		BlockEntity blockEntity = world.getBlockEntity(state.getBlock() instanceof DoorBlock && state.get(DoorBlock.HALF) == DoubleBlockHalf.UPPER ? pos.down() : pos);
		if (blockEntity instanceof SigilHolder) {
			SigilHolder sigil = (SigilHolder) blockEntity;
			if (player != null && player.getUuid().equals(sigil.getOwner())) {
				if (!client && !sigil.getEntities().isEmpty()) {
					boolean whitelist = sigil.getModeOnWhitelist();
					world.playSound(null, pos, BWSoundEvents.BLOCK_SIGIL_PLING, SoundCategory.BLOCKS, 1, whitelist ? 0.5f : 1);
					player.sendMessage(new TranslatableText(Bewitchment.MODID + ".message.toggle_" +  (!whitelist ? "whitelist" : "blacklist")), true);
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
					player.sendMessage(new TranslatableText(Bewitchment.MODID + ".message.toggle_" +  (!whitelist ? "whitelist" : "blacklist")), true);
					lockable.setModeOnWhitelist(!whitelist);
					blockEntity.markDirty();
				}
				return ActionResult.success(client);
			}
		}
		return super.useOnBlock(context);
	}
	
	@Override
	public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
		if (attacker instanceof PlayerEntity) {
			World world = attacker.world;
			if (!world.isClient && target.isDead()) {
				BlockPos pos = target.getBlockPos();
				BlockPos glyph = BWUtil.getClosestBlockPos(pos, 6, currentPos -> world.getBlockEntity(currentPos) instanceof GlyphBlockEntity);
				if (glyph != null) {
					((GlyphBlockEntity) world.getBlockEntity(glyph)).onUse(world, glyph, (PlayerEntity) attacker, Hand.MAIN_HAND, target);
				}
				if(AthameSacrificeCallback.EVENT.invoker().sacrifice(stack, target, attacker) == ActionResult.FAIL){
					return super.postHit(stack, target, attacker);
				}
				if (world.isNight()) {
					boolean chicken = target instanceof ChickenEntity;
					if ((chicken && world.getBiome(pos).getCategory() == Biome.Category.EXTREME_HILLS) || (target instanceof WolfEntity && (world.getBiome(pos).getCategory() == Biome.Category.FOREST || world.getBiome(pos).getCategory() == Biome.Category.TAIGA))) {
						BlockPos brazierPos = BWUtil.getClosestBlockPos(pos, 8, currentPos -> {
							BlockEntity blockEntity = world.getBlockEntity(currentPos);
							return blockEntity instanceof BrazierBlockEntity && ((BrazierBlockEntity) blockEntity).incenseRecipe.effect == BWStatusEffects.MORTAL_COIL;
						});
						if (brazierPos != null) {
							world.breakBlock(brazierPos, false);
							world.createExplosion(target, brazierPos.getX() + 0.5, brazierPos.getY() + 0.5, brazierPos.getZ() + 0.5, 3, Explosion.DestructionType.BREAK);
							Entity entity = chicken ? BWEntityTypes.LILITH.create(world) : BWEntityTypes.HERNE.create(world);
							if (entity instanceof MobEntity) {
								((MobEntity) entity).initialize((ServerWorldAccess) world, world.getLocalDifficulty(brazierPos), SpawnReason.EVENT, null, null);
								entity.updatePositionAndAngles(brazierPos.getX() + 0.5, brazierPos.getY(), brazierPos.getZ() + 0.5, world.random.nextFloat() * 360, 0);
								world.spawnEntity(entity);
							}
						}
					}
				}
			}
		}
		return super.postHit(stack, target, attacker);
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
