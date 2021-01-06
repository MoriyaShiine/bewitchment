package moriyashiine.bewitchment.api.registry;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.api.interfaces.WetAccessor;
import moriyashiine.bewitchment.common.item.TaglockItem;
import moriyashiine.bewitchment.common.item.tool.AthameItem;
import moriyashiine.bewitchment.common.registry.BWMaterials;
import moriyashiine.bewitchment.common.registry.BWRitualFunctions;
import moriyashiine.bewitchment.mixin.ZombieVillagerEntityAccessor;
import net.minecraft.block.*;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.ZombieVillagerEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.HashMap;

public class RitualFunction {
	public final ParticleType<?> startParticle;
	
	public RitualFunction(ParticleType<?> startParticle) {
		this.startParticle = startParticle;
	}
	
	public String getInvalidMessage() {
		if (this == BWRitualFunctions.TURN_TO_DAY) {
			return "ritual.precondition.night";
		}
		else if (this == BWRitualFunctions.CLEANSE) {
			return "ritual.precondition.found_entity";
		}
		else if (this == BWRitualFunctions.TURN_TO_NIGHT) {
			return "ritual.precondition.day";
		}
		else if (this == BWRitualFunctions.CLEAR_ENCHANTMENTS || this == BWRitualFunctions.ENCHANT) {
			return "ritual.precondition.found_items";
		}
		else if (this == BWRitualFunctions.START_RAIN) {
			return "ritual.precondition.no_rain";
		}
		else if (this == BWRitualFunctions.STOP_RAIN) {
			return "ritual.precondition.rain";
		}
		return "";
	}
	
	public boolean isValid(ServerWorld world, BlockPos pos, Inventory inventory) {
		if (this == BWRitualFunctions.TURN_TO_DAY) {
			return world.isNight();
		}
		else if (this == BWRitualFunctions.CLEANSE) {
			ItemStack taglock = null;
			for (int i = 0; i < inventory.size(); i++) {
				ItemStack stack = inventory.getStack(i);
				if (inventory.getStack(i).getItem() instanceof TaglockItem && stack.hasTag() && stack.getOrCreateTag().contains("OwnerUUID")) {
					taglock = stack;
					break;
				}
			}
			return taglock != null && BewitchmentAPI.getTaglockOwner(world, taglock) != null;
		}
		else if (this == BWRitualFunctions.TURN_TO_NIGHT) {
			return world.isDay();
		}
		else if (this == BWRitualFunctions.START_RAIN) {
			return !world.isRaining();
		}
		else if (this == BWRitualFunctions.STOP_RAIN) {
			return world.isRaining();
		}
		return true;
	}
	
	public void start(ServerWorld world, BlockPos pos, Inventory inventory) {
		for (int i = 0; i < inventory.size(); i++) {
			ItemStack stack = inventory.getStack(i);
			if (stack.getItem() instanceof AthameItem) {
				stack.damage(50, world.random, null);
				if (stack.getDamage() == BWMaterials.SILVER_TOOL.getDurability()) {
					stack.decrement(1);
				}
			}
			else {
				Item item = stack.getItem();
				inventory.setStack(i, item.hasRecipeRemainder() ? new ItemStack(item.getRecipeRemainder()) : ItemStack.EMPTY);
			}
		}
		if (this == BWRitualFunctions.TURN_TO_DAY) {
			while (world.getTimeOfDay() % 24000 != 0) {
				world.setTimeOfDay(world.getTimeOfDay() + 1);
			}
		}
		else if (this == BWRitualFunctions.CLEANSE) {
			ItemStack taglock = null;
			for (int i = 0; i < inventory.size(); i++) {
				ItemStack stack = inventory.getStack(i);
				if (inventory.getStack(i).getItem() instanceof TaglockItem && stack.hasTag() && stack.getOrCreateTag().contains("OwnerUUID")) {
					taglock = stack;
					break;
				}
			}
			if (taglock != null) {
				LivingEntity livingEntity = BewitchmentAPI.getTaglockOwner(world, taglock);
				if (livingEntity instanceof ZombieVillagerEntity) {
					((ZombieVillagerEntityAccessor) livingEntity).bw_finishConversion(world);
				}
			}
		}
		else if (this == BWRitualFunctions.TURN_TO_NIGHT) {
			while (world.getTimeOfDay() % 24000 < 13000) {
				world.setTimeOfDay(world.getTimeOfDay() + 1);
			}
		}
		else if (this == BWRitualFunctions.DESTROY_LIGHTS) {
			BlockPos.Mutable mutable = new BlockPos.Mutable();
			int radius = 8;
			for (int x = -radius; x <= radius; x++) {
				for (int y = -radius; y <= radius; y++) {
					for (int z = -radius; z <= radius; z++) {
						if (world.getBlockState(mutable.set(pos.getX() + x, pos.getY() + y, pos.getZ() + z)).getLuminance() > 0 && world.getBlockState(mutable).getHardness(world, mutable) == 0) {
							world.breakBlock(mutable, true);
						}
					}
				}
			}
		}
		else if (this == BWRitualFunctions.CLEAR_ENCHANTMENTS) {
			for (ItemEntity itemEntity : world.getEntitiesByType(EntityType.ITEM, new Box(pos).expand(2, 0, 2), entity -> entity.getStack().hasEnchantments())) {
				ItemStack stack = itemEntity.getStack();
				int enchantments = 0;
				for (Enchantment enchantment : EnchantmentHelper.get(stack).keySet()) {
					enchantments += EnchantmentHelper.getLevel(enchantment, stack);
				}
				EnchantmentHelper.set(new HashMap<>(), stack);
				if (stack.isDamaged()) {
					stack.setDamage(Math.max(0, stack.getDamage() - (enchantments * 64)));
				}
				stack.setRepairCost(0);
				ItemScatterer.spawn(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, stack.copy());
				stack.decrement(1);
			}
		}
		else if (this == BWRitualFunctions.START_RAIN) {
			world.setWeather(0, world.random.nextInt(6000) + 6000, true, false);
		}
		else if (this == BWRitualFunctions.MAKE_ENTITIES_WET) {
			int radius = 2;
			world.getEntitiesByClass(Entity.class, new Box(pos).expand(radius), entity -> true).forEach(entity -> WetAccessor.of(entity).ifPresent(wetAccessor -> wetAccessor.setWetTimer(6000)));
		}
		else if (this == BWRitualFunctions.ENCHANT) {
			for (ItemEntity itemEntity : world.getEntitiesByType(EntityType.ITEM, new Box(pos).expand(2, 0, 2), entity -> entity.getStack().isEnchantable())) {
				PlayerEntity closestPlayer = world.getClosestPlayer(itemEntity, 8);
				if (closestPlayer != null && closestPlayer.experienceLevel >= 5) {
					closestPlayer.addExperienceLevels(-5);
					EnchantmentHelper.enchant(world.random, itemEntity.getStack(), 40, false);
				}
				ItemScatterer.spawn(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, itemEntity.getStack().copy());
				itemEntity.getStack().decrement(1);
			}
		}
		else if (this == BWRitualFunctions.STOP_RAIN) {
			world.setWeather(world.random.nextInt(6000) + 6000, 0, false, false);
		}
		else if (this == BWRitualFunctions.DRAIN_WATER) {
			BlockPos.Mutable mutable = new BlockPos.Mutable();
			int radius = 6;
			for (int x = -radius; x <= radius; x++) {
				for (int y = -radius; y <= radius; y++) {
					for (int z = -radius; z <= radius; z++) {
						if (world.getFluidState(mutable.set(pos.getX() + x, pos.getY() + y, pos.getZ() + z)).getFluid() == Fluids.WATER) {
							world.setBlockState(mutable, Blocks.AIR.getDefaultState());
						}
					}
				}
			}
		}
		else if (this == BWRitualFunctions.TELEPORT_ENTITIES) {
			BlockPos location = null;
			for (int i = 0; i < inventory.size(); i++) {
				ItemStack stack = inventory.getStack(i);
				if (stack.getItem() instanceof TaglockItem && stack.hasTag() && stack.getOrCreateTag().contains("OwnerUUID")) {
					LivingEntity livingEntity = BewitchmentAPI.getTaglockOwner(world, stack);
					if (livingEntity != null) {
						location = livingEntity.getBlockPos();
						break;
					}
				}
			}
			if (location != null) {
				int radius = 3;
				for (Entity entity : world.getNonSpectatingEntities(Entity.class, new Box(pos).expand(radius))) {
					BewitchmentAPI.teleport(entity, location);
				}
			}
			else {
				ItemScatterer.spawn(world, pos, inventory);
			}
		}
		else if (this == BWRitualFunctions.SEARCH_BLOCKS) {
			Block block = world.getBlockState(pos.down(2)).getBlock();
			int found = 0;
			BlockPos.Mutable mutable = new BlockPos.Mutable();
			int radius = 16;
			for (int x = -radius; x <= radius; x++) {
				for (int y = -radius; y <= radius; y++) {
					for (int z = -radius; z <= radius; z++) {
						if (world.getBlockState(mutable.set(pos.getX() + x, pos.getY() + y, pos.getZ() + z)).getBlock() == block) {
							found++;
						}
					}
				}
			}
			PlayerEntity closestPlayer = world.getClosestPlayer(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 8, false);
			if (closestPlayer != null) {
				closestPlayer.sendMessage(new TranslatableText("bewitchment.found_blocks", found, block.getName()), true);
			}
		}
		else if (this == BWRitualFunctions.SPAWN_LIGHTNING) {
			LightningEntity lightningEntity = EntityType.LIGHTNING_BOLT.create(world);
			if (lightningEntity != null) {
				lightningEntity.updatePositionAndAngles(pos.getX(), pos.getY(), pos.getZ(), 0, world.random.nextInt(360));
				world.spawnEntity(lightningEntity);
			}
		}
		else if (this == BWRitualFunctions.DESTROY_PLANTS) {
			BlockPos.Mutable mutable = new BlockPos.Mutable();
			int radius = 10;
			for (int x = -radius; x <= radius; x++) {
				for (int y = -radius; y <= radius; y++) {
					for (int z = -radius; z <= radius; z++) {
						Block block = world.getBlockState(mutable.set(pos.getX() + x, pos.getY() + y, pos.getZ() + z)).getBlock();
						if (block instanceof PlantBlock || block instanceof LeavesBlock) {
							world.breakBlock(mutable, true);
						}
					}
				}
			}
		}
	}
	
	public void tick(World world, BlockPos pos) {
		if (this == BWRitualFunctions.SMELT_ITEMS) {
			int radius = 3;
			if (!world.isClient) {
				if (world.getTime() % 20 == 0) {
					for (ItemEntity itemEntity : world.getEntitiesByType(EntityType.ITEM, new Box(pos).expand(radius, 0, radius), entity -> true)) {
						if (world.random.nextFloat() < 1 / 4f) {
							world.getRecipeManager().listAllOfType(RecipeType.SMELTING).forEach(smeltingRecipe -> {
								for (Ingredient ingredient : smeltingRecipe.getPreviewInputs()) {
									if (ingredient.test(itemEntity.getStack())) {
										world.playSound(null, itemEntity.getBlockPos(), SoundEvents.BLOCK_FURNACE_FIRE_CRACKLE, SoundCategory.BLOCKS, 1, 1);
										ItemScatterer.spawn(world, itemEntity.getX(), itemEntity.getY(), itemEntity.getZ(), smeltingRecipe.getOutput().copy());
										world.spawnEntity(new ExperienceOrbEntity(world, itemEntity.getX(), itemEntity.getY(), itemEntity.getZ(), 1));
										itemEntity.getStack().decrement(1);
									}
								}
							});
						}
					}
				}
			}
			else {
				world.addParticle(ParticleTypes.FLAME, pos.getX() + MathHelper.nextDouble(world.random, -radius, radius), pos.getY() + 0.5, pos.getZ() + MathHelper.nextDouble(world.random, -radius, radius), 0, 0, 0);
			}
		}
		else if (this == BWRitualFunctions.GROW) {
			int radius = 3;
			if (!world.isClient) {
				if (world.getTime() % 20 == 0) {
					for (PassiveEntity passiveEntity : world.getEntitiesByClass(PassiveEntity.class, new Box(pos).expand(radius, 0, radius), PassiveEntity::isBaby)) {
						if (world.random.nextFloat() < 1 / 4f) {
							passiveEntity.growUp(world.random.nextInt(), true);
						}
					}
					BlockPos.Mutable mutable = new BlockPos.Mutable();
					for (int x = -radius; x <= radius; x++) {
						for (int y = -radius; y <= radius; y++) {
							for (int z = -radius; z <= radius; z++) {
								BlockState state = world.getBlockState(mutable.set(pos.getX() + x, pos.getY() + y, pos.getZ() + z));
								if (!(state.getBlock() instanceof GrassBlock) && state.getBlock() instanceof Fertilizable && ((Fertilizable) state.getBlock()).canGrow(world, world.random, mutable, state) && world.random.nextFloat() < 1 / 4f) {
									((Fertilizable) state.getBlock()).grow((ServerWorld) world, world.random, mutable, state);
								}
							}
						}
					}
				}
			}
			else {
				world.addParticle(ParticleTypes.HAPPY_VILLAGER, pos.getX() + MathHelper.nextDouble(world.random, -radius, radius), pos.getY() + 0.5, pos.getZ() + MathHelper.nextDouble(world.random, -radius, radius), 0, 0, 0);
			}
		}
		else if (this == BWRitualFunctions.PUSH_MOBS) {
			if (!world.isClient) {
				int radius = 8;
				if (world.getTime() % 5 == 0) {
					for (HostileEntity hostileEntity : world.getEntitiesByClass(HostileEntity.class, new Box(pos).expand(radius, 0, radius), entity -> true)) {
						double distanceX = pos.getX() - hostileEntity.getX();
						double distanceZ = pos.getZ() - hostileEntity.getZ();
						double max = MathHelper.absMax(distanceX, distanceZ);
						if (max >= 0) {
							max = MathHelper.sqrt(max);
							distanceX /= max;
							distanceZ /= max;
							distanceX *= Math.min(1, 1 / max);
							distanceZ *= Math.min(1, 1 / max);
							distanceX /= 2;
							distanceZ /= 2;
							hostileEntity.addVelocity(-distanceX, 0, -distanceZ);
						}
					}
				}
			}
		}
	}
}
