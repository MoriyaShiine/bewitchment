package moriyashiine.bewitchment.common.block.entity;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.api.interfaces.block.entity.UsesAltarPower;
import moriyashiine.bewitchment.api.registry.RitualFunction;
import moriyashiine.bewitchment.client.network.packet.SpawnSmokeParticlesPacket;
import moriyashiine.bewitchment.client.network.packet.SyncClientSerializableBlockEntity;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.block.GlyphBlock;
import moriyashiine.bewitchment.common.item.WaystoneItem;
import moriyashiine.bewitchment.common.recipe.RitualRecipe;
import moriyashiine.bewitchment.common.registry.*;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.Tickable;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.List;

@SuppressWarnings("ConstantConditions")
public class GlyphBlockEntity extends BlockEntity implements BlockEntityClientSerializable, Tickable, Inventory, UsesAltarPower {
	private static final byte[][] inner = {{0, 0, 1, 1, 1, 0, 0}, {0, 1, 0, 0, 0, 1, 0}, {1, 0, 0, 0, 0, 0, 1}, {1, 0, 0, 0, 0, 0, 1}, {1, 0, 0, 0, 0, 0, 1}, {0, 1, 0, 0, 0, 1, 0}, {0, 0, 1, 1, 1, 0, 0}};
	private static final byte[][] outer = {{0, 0, 0, 1, 1, 1, 1, 1, 0, 0, 0}, {0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0}, {0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0}, {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1}, {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1}, {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1}, {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1}, {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1}, {0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0}, {0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0}, {0, 0, 0, 1, 1, 1, 1, 1, 0, 0, 0}};
	
	private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(6, ItemStack.EMPTY);
	
	private BlockPos altarPos = null, effectivePos = null;
	
	public RitualFunction ritualFunction = null;
	private int timer = 0, endTime = 0;
	
	private boolean catFamiliar = false;
	
	private boolean loaded = false;
	
	public GlyphBlockEntity(BlockEntityType<?> type) {
		super(type);
	}
	
	public GlyphBlockEntity() {
		this(BWBlockEntityTypes.GLYPH);
	}
	
	@Override
	public void fromClientTag(CompoundTag tag) {
		if (tag.contains("AltarPos")) {
			setAltarPos(BlockPos.fromLong(tag.getLong("AltarPos")));
		}
		if (tag.contains("EffectivePos")) {
			effectivePos = BlockPos.fromLong(tag.getLong("EffectivePos"));
		}
		Inventories.fromTag(tag, inventory);
		ritualFunction = BWRegistries.RITUAL_FUNCTIONS.get(new Identifier(tag.getString("RitualFunction")));
		timer = tag.getInt("Timer");
		endTime = tag.getInt("EndTime");
		catFamiliar = tag.getBoolean("CatFamiliar");
	}
	
	@Override
	public CompoundTag toClientTag(CompoundTag tag) {
		if (getAltarPos() != null) {
			tag.putLong("AltarPos", getAltarPos().asLong());
		}
		if (effectivePos != null) {
			tag.putLong("EffectivePos", effectivePos.asLong());
		}
		Inventories.toTag(tag, inventory);
		if (ritualFunction != null) {
			tag.putString("RitualFunction", BWRegistries.RITUAL_FUNCTIONS.getId(ritualFunction).toString());
		}
		tag.putInt("Timer", timer);
		tag.putInt("EndTime", endTime);
		tag.putBoolean("CatFamiliar", catFamiliar);
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
				markDirty();
				loaded = true;
			}
			if (ritualFunction != null) {
				BlockPos targetPos = effectivePos == null ? pos : effectivePos;
				timer++;
				if (world.isClient) {
					world.addParticle(ParticleTypes.END_ROD, true, pos.getX() + 0.5 + MathHelper.nextFloat(world.random, -0.2f, 0.2f), pos.getY() + 0.5 + MathHelper.nextFloat(world.random, -0.2f, 0.2f), pos.getZ() + 0.5 + MathHelper.nextFloat(world.random, -0.2f, 0.2f), 0, 0, 0);
					if (timer < 0) {
						for (int i = 0; i < 3; i++) {
							world.addParticle((ParticleEffect) ritualFunction.startParticle, true, targetPos.getX() + 0.5 + MathHelper.nextFloat(world.random, -0.2f, 0.2f), targetPos.getY() + 0.5 + MathHelper.nextFloat(world.random, -0.2f, 0.2f), targetPos.getZ() + 0.5 + MathHelper.nextFloat(world.random, -0.2f, 0.2f), MathHelper.nextFloat(world.random, -1, 1), MathHelper.nextFloat(world.random, 0.125f, 1), MathHelper.nextFloat(world.random, -1, 1));
						}
					}
				}
				if (timer >= 0) {
					ritualFunction.tick(world, pos, targetPos, catFamiliar);
					if (!world.isClient) {
						world.getWorldChunk(effectivePos);
						if (timer == 0) {
							ritualFunction.start((ServerWorld) world, pos, targetPos, this, catFamiliar);
							world.playSound(null, pos, BWSoundEvents.BLOCK_GLYPH_PLING, SoundCategory.BLOCKS, 1, 1);
							ItemScatterer.spawn(world, pos, this);
						}
						if (timer >= endTime) {
							effectivePos = null;
							ritualFunction = null;
							timer = 0;
							endTime = 0;
							catFamiliar = false;
							syncGlyph();
						}
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
	
	public void syncGlyph() {
		if (world instanceof ServerWorld) {
			PlayerLookup.tracking(this).forEach(playerEntity -> SyncClientSerializableBlockEntity.send(playerEntity, this));
		}
	}
	
	public void onUse(World world, BlockPos pos, PlayerEntity player, Hand hand, LivingEntity sacrifice) {
		ItemStack stack = player.getStackInHand(hand);
		if (ritualFunction != null && pos.equals(effectivePos) && stack.getItem() instanceof WaystoneItem && stack.hasTag() && stack.getOrCreateTag().contains("LocationPos")) {
			effectivePos = BlockPos.fromLong(stack.getOrCreateTag().getLong("LocationPos"));
			stack.damage(1, player, user -> user.sendToolBreakStatus(hand));
			syncGlyph();
		}
		else {
			if (ritualFunction == null) {
				SimpleInventory test = new SimpleInventory(size());
				List<ItemEntity> items = world.getEntitiesByType(EntityType.ITEM, new Box(pos).expand(2, 0, 2), entity -> true);
				for (ItemEntity entity : items) {
					test.addStack(entity.getStack().copy().split(1));
				}
				RitualRecipe recipe = world.getRecipeManager().listAllOfType(BWRecipeTypes.RITUAL_RECIPE_TYPE).stream().filter(ritualRecipe -> ritualRecipe.matches(test, world)).findFirst().orElse(null);
				if (recipe != null && recipe.input.size() == items.size() && hasValidChalk(recipe)) {
					if (recipe.ritualFunction.isValid((ServerWorld) world, pos, test) || (recipe.ritualFunction.sacrifice != null && sacrifice != null && recipe.ritualFunction.sacrifice.test(sacrifice))) {
						boolean cat = BewitchmentAPI.getFamiliar(player) == EntityType.CAT;
						if (altarPos != null && ((WitchAltarBlockEntity) world.getBlockEntity(altarPos)).drain((int) (recipe.cost * (cat ? 0.75f : 1)), false)) {
							world.playSound(null, pos, BWSoundEvents.BLOCK_GLYPH_FIRE, SoundCategory.BLOCKS, 1, 1);
							player.sendMessage(new TranslatableText("ritual." + recipe.getId().toString().replace(":", ".").replace("/", ".")), true);
							for (int i = 0; i < items.size(); i++) {
								for (PlayerEntity foundPlayer : PlayerLookup.tracking(items.get(i))) {
									SpawnSmokeParticlesPacket.send(foundPlayer, items.get(i));
								}
								setStack(i, items.get(i).getStack().split(1));
							}
							effectivePos = pos;
							ritualFunction = recipe.ritualFunction;
							timer = -100;
							endTime = recipe.runningTime;
							catFamiliar = cat;
							syncGlyph();
							return;
						}
						world.playSound(null, pos, BWSoundEvents.BLOCK_GLYPH_FAIL, SoundCategory.BLOCKS, 1, 1);
						player.sendMessage(new TranslatableText(Bewitchment.MODID + ".message.insufficent_altar_power"), true);
						return;
					}
					world.playSound(null, pos, BWSoundEvents.BLOCK_GLYPH_FAIL, SoundCategory.BLOCKS, 1, 1);
					player.sendMessage(new TranslatableText(recipe.ritualFunction.getInvalidMessage()), true);
					return;
				}
				world.playSound(null, pos, BWSoundEvents.BLOCK_GLYPH_FAIL, SoundCategory.BLOCKS, 1, 1);
				player.sendMessage(new TranslatableText("ritual.none"), true);
			}
			else if (sacrifice == null) {
				world.playSound(null, pos, BWSoundEvents.BLOCK_GLYPH_FAIL, SoundCategory.BLOCKS, 1, 1);
				ItemScatterer.spawn(world, pos, this);
				effectivePos = null;
				ritualFunction = null;
				timer = 0;
				endTime = 0;
				catFamiliar = false;
				syncGlyph();
			}
		}
	}
	
	private boolean hasValidChalk(RitualRecipe recipe) {
		BlockPos.Mutable mutable = new BlockPos.Mutable();
		for (int x = 0; x < inner.length; x++) {
			for (int z = 0; z < inner.length; z++) {
				if (inner[x][z] == 1 && !isValidGlyph(recipe.inner, world.getBlockState(mutable.set(pos.getX() + (x - inner.length / 2), pos.getY(), pos.getZ() + (z - inner.length / 2))).getBlock())) {
					return false;
				}
			}
		}
		if (!recipe.outer.isEmpty()) {
			for (int x = 0; x < outer.length; x++) {
				for (int z = 0; z < outer.length; z++) {
					if (outer[x][z] == 1 && !isValidGlyph(recipe.outer, world.getBlockState(mutable.set(pos.getX() + (x - outer.length / 2), pos.getY(), pos.getZ() + (z - outer.length / 2))).getBlock())) {
						return false;
					}
				}
			}
		}
		return true;
	}
	
	@SuppressWarnings("BooleanMethodIsAlwaysInverted")
	private boolean isValidGlyph(String name, Block block) {
		if (name.equals("normal") && block == BWObjects.GLYPH) {
			return true;
		}
		if (name.equals("fiery") && block == BWObjects.FIERY_GLYPH) {
			return true;
		}
		if (name.equals("eldritch") && block == BWObjects.ELDRITCH_GLYPH) {
			return true;
		}
		return name.equals("any") && block instanceof GlyphBlock && block != BWObjects.GOLDEN_GLYPH;
	}
}
