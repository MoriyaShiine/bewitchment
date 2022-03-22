/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.bewitchment.common.block.entity;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.api.block.entity.UsesAltarPower;
import moriyashiine.bewitchment.api.registry.RitualFunction;
import moriyashiine.bewitchment.client.network.packet.SpawnSmokeParticlesPacket;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.block.GlyphBlock;
import moriyashiine.bewitchment.common.item.WaystoneItem;
import moriyashiine.bewitchment.common.recipe.RitualRecipe;
import moriyashiine.bewitchment.common.registry.*;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@SuppressWarnings("ConstantConditions")
public class GlyphBlockEntity extends BlockEntity implements Inventory, UsesAltarPower {
	private static final byte[][] inner = {{0, 0, 1, 1, 1, 0, 0}, {0, 1, 0, 0, 0, 1, 0}, {1, 0, 0, 0, 0, 0, 1}, {1, 0, 0, 0, 0, 0, 1}, {1, 0, 0, 0, 0, 0, 1}, {0, 1, 0, 0, 0, 1, 0}, {0, 0, 1, 1, 1, 0, 0}};
	private static final byte[][] outer = {{0, 0, 0, 1, 1, 1, 1, 1, 0, 0, 0}, {0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0}, {0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0}, {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1}, {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1}, {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1}, {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1}, {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1}, {0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0}, {0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0}, {0, 0, 0, 1, 1, 1, 1, 1, 0, 0, 0}};

	private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(6, ItemStack.EMPTY);

	private BlockPos altarPos = null, effectivePos = null;

	public RitualFunction ritualFunction = null;
	private int timer = 0, endTime = 0;

	private boolean catFamiliar = false;

	private boolean loaded = false;

	public GlyphBlockEntity(BlockPos pos, BlockState state) {
		super(BWBlockEntityTypes.GLYPH, pos, state);
	}

	@Override
	public NbtCompound toInitialChunkDataNbt() {
		NbtCompound nbt = super.toInitialChunkDataNbt();
		writeNbt(nbt);
		return nbt;
	}

	@Nullable
	@Override
	public Packet<ClientPlayPacketListener> toUpdatePacket() {
		return BlockEntityUpdateS2CPacket.create(this);
	}

	@Override
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);
		if (nbt.contains("AltarPos")) {
			setAltarPos(BlockPos.fromLong(nbt.getLong("AltarPos")));
		}
		if (nbt.contains("EffectivePos")) {
			effectivePos = BlockPos.fromLong(nbt.getLong("EffectivePos"));
		}
		inventory.clear();
		Inventories.readNbt(nbt, inventory);
		ritualFunction = BWRegistries.RITUAL_FUNCTIONS.get(new Identifier(nbt.getString("RitualFunction")));
		timer = nbt.getInt("Timer");
		endTime = nbt.getInt("EndTime");
		catFamiliar = nbt.getBoolean("CatFamiliar");
	}

	@Override
	protected void writeNbt(NbtCompound nbt) {
		super.writeNbt(nbt);
		if (getAltarPos() != null) {
			nbt.putLong("AltarPos", getAltarPos().asLong());
		}
		if (effectivePos != null) {
			nbt.putLong("EffectivePos", effectivePos.asLong());
		}
		Inventories.writeNbt(nbt, inventory);
		if (ritualFunction != null) {
			nbt.putString("RitualFunction", BWRegistries.RITUAL_FUNCTIONS.getId(ritualFunction).toString());
		}
		nbt.putInt("Timer", timer);
		nbt.putInt("EndTime", endTime);
		nbt.putBoolean("CatFamiliar", catFamiliar);
	}

	public void sync() {
		if (world != null && !world.isClient) {
			world.updateListeners(pos, getCachedState(), getCachedState(), Block.NOTIFY_LISTENERS);
		}
	}

	@Override
	public BlockPos getAltarPos() {
		return altarPos;
	}

	@Override
	public void setAltarPos(BlockPos pos) {
		this.altarPos = pos;
	}

	public static void tick(World world, BlockPos pos, BlockState state, GlyphBlockEntity blockEntity) {
		if (world != null) {
			if (!blockEntity.loaded) {
				blockEntity.markDirty();
				blockEntity.loaded = true;
			}
			if (blockEntity.ritualFunction != null) {
				BlockPos targetPos = blockEntity.effectivePos == null ? pos : blockEntity.effectivePos;
				blockEntity.timer++;
				if (world.isClient) {
					world.addParticle(ParticleTypes.END_ROD, true, pos.getX() + 0.5 + MathHelper.nextFloat(world.random, -0.2f, 0.2f), pos.getY() + 0.5 + MathHelper.nextFloat(world.random, -0.2f, 0.2f), pos.getZ() + 0.5 + MathHelper.nextFloat(world.random, -0.2f, 0.2f), 0, 0, 0);
					if (blockEntity.timer < 0) {
						for (int i = 0; i < 3; i++) {
							world.addParticle((ParticleEffect) blockEntity.ritualFunction.startParticle, true, targetPos.getX() + 0.5 + MathHelper.nextFloat(world.random, -0.2f, 0.2f), targetPos.getY() + 0.5 + MathHelper.nextFloat(world.random, -0.2f, 0.2f), targetPos.getZ() + 0.5 + MathHelper.nextFloat(world.random, -0.2f, 0.2f), MathHelper.nextFloat(world.random, -1, 1), MathHelper.nextFloat(world.random, 0.125f, 1), MathHelper.nextFloat(world.random, -1, 1));
						}
					}
				}
				if (blockEntity.timer >= 0) {
					blockEntity.ritualFunction.tick(world, pos, targetPos, blockEntity.catFamiliar);
					if (!world.isClient) {
						world.getWorldChunk(blockEntity.effectivePos);
						if (blockEntity.timer == 0) {
							blockEntity.ritualFunction.start((ServerWorld) world, pos, targetPos, blockEntity, blockEntity.catFamiliar);
							world.playSound(null, pos, BWSoundEvents.BLOCK_GLYPH_PLING, SoundCategory.BLOCKS, 1, 1);
							ItemScatterer.spawn(world, pos, blockEntity);
						}
						if (blockEntity.timer >= blockEntity.endTime) {
							blockEntity.effectivePos = null;
							blockEntity.ritualFunction = null;
							blockEntity.timer = 0;
							blockEntity.endTime = 0;
							blockEntity.catFamiliar = false;
							blockEntity.syncGlyph();
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
		sync();
	}

	public void onUse(World world, BlockPos pos, LivingEntity user, Hand hand, LivingEntity sacrifice) {
		ItemStack stack = user.getStackInHand(hand);
		if (ritualFunction != null && pos.equals(effectivePos) && stack.getItem() instanceof WaystoneItem && stack.hasNbt() && stack.getOrCreateNbt().contains("LocationPos") && world.getRegistryKey().getValue().toString().equals(stack.getOrCreateNbt().getString("LocationWorld"))) {
			effectivePos = BlockPos.fromLong(stack.getOrCreateNbt().getLong("LocationPos"));
			stack.damage(1, user, stackUser -> stackUser.sendToolBreakStatus(hand));
			syncGlyph();
		} else {
			if (ritualFunction == null) {
				SimpleInventory test = new SimpleInventory(size());
				List<ItemEntity> items = world.getEntitiesByType(EntityType.ITEM, new Box(pos).expand(2, 0, 2), entity -> true);
				for (ItemEntity entity : items) {
					test.addStack(entity.getStack().copy().split(1));
				}
				RitualRecipe recipe = world.getRecipeManager().listAllOfType(BWRecipeTypes.RITUAL_RECIPE_TYPE).stream().filter(ritualRecipe -> ritualRecipe.matches(test, world)).findFirst().orElse(null);
				if (recipe != null && recipe.input.size() == items.size() && hasValidChalk(recipe)) {
					if (recipe.ritualFunction.isValid((ServerWorld) world, pos, test) || (recipe.ritualFunction.sacrifice != null && sacrifice != null && recipe.ritualFunction.sacrifice.test(sacrifice))) {
						boolean cat = user instanceof PlayerEntity player && BewitchmentAPI.getFamiliar(player) == EntityType.CAT;
						if (altarPos != null && ((WitchAltarBlockEntity) world.getBlockEntity(altarPos)).drain((int) (recipe.cost * (cat ? 0.75f : 1)), false)) {
							world.playSound(null, pos, BWSoundEvents.BLOCK_GLYPH_FIRE, SoundCategory.BLOCKS, 1, 1);
							if (user instanceof PlayerEntity player) {
								player.sendMessage(new TranslatableText("ritual." + recipe.getId().toString().replace(":", ".").replace("/", ".")), true);
							}
							for (int i = 0; i < items.size(); i++) {
								for (PlayerEntity trackingPlayer : PlayerLookup.tracking(items.get(i))) {
									SpawnSmokeParticlesPacket.send(trackingPlayer, items.get(i));
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
						if (user instanceof PlayerEntity player) {
							player.sendMessage(new TranslatableText(Bewitchment.MODID + ".message.insufficent_altar_power"), true);
						}
						return;
					}
					world.playSound(null, pos, BWSoundEvents.BLOCK_GLYPH_FAIL, SoundCategory.BLOCKS, 1, 1);
					if (user instanceof PlayerEntity player) {
						player.sendMessage(new TranslatableText(recipe.ritualFunction.getInvalidMessage()), true);
					}
					return;
				}
				world.playSound(null, pos, BWSoundEvents.BLOCK_GLYPH_FAIL, SoundCategory.BLOCKS, 1, 1);
				if (user instanceof PlayerEntity player) {
					player.sendMessage(new TranslatableText("ritual.none"), true);
				}
			} else if (sacrifice == null) {
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
