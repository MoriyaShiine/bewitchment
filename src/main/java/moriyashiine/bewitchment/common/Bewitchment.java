/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common;

import eu.midnightdust.lib.config.MidnightConfig;
import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.api.component.BloodComponent;
import moriyashiine.bewitchment.api.event.BloodSuckEvents;
import moriyashiine.bewitchment.common.block.CoffinBlock;
import moriyashiine.bewitchment.common.block.entity.BrazierBlockEntity;
import moriyashiine.bewitchment.common.block.entity.GlyphBlockEntity;
import moriyashiine.bewitchment.common.block.entity.SigilBlockEntity;
import moriyashiine.bewitchment.common.block.entity.interfaces.SigilHolder;
import moriyashiine.bewitchment.common.item.AthameItem;
import moriyashiine.bewitchment.common.misc.BWUtil;
import moriyashiine.bewitchment.common.network.packet.CauldronTeleportPacket;
import moriyashiine.bewitchment.common.network.packet.TogglePressingForwardPacket;
import moriyashiine.bewitchment.common.network.packet.TransformationAbilityPacket;
import moriyashiine.bewitchment.common.recipe.AthameDropRecipe;
import moriyashiine.bewitchment.common.recipe.IncenseRecipe;
import moriyashiine.bewitchment.common.registry.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityCombatEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalBiomeTags;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

public class Bewitchment implements ModInitializer {
	public static final String MODID = "bewitchment";

	public static ItemGroup GROUP;

	@SuppressWarnings("ConstantConditions")
	@Override
	public void onInitialize() {
		MidnightConfig.init(MODID, BWConfig.class);
		ServerPlayNetworking.registerGlobalReceiver(CauldronTeleportPacket.ID, CauldronTeleportPacket::handle);
		ServerPlayNetworking.registerGlobalReceiver(TransformationAbilityPacket.ID, TransformationAbilityPacket::handle);
		ServerPlayNetworking.registerGlobalReceiver(TogglePressingForwardPacket.ID, TogglePressingForwardPacket::handle);
		CommandRegistrationCallback.EVENT.register(BWCommands::init);
		ServerLifecycleEvents.SERVER_STOPPED.register(server -> BewitchmentAPI.fakePlayer = null);
		ServerPlayerEvents.AFTER_RESPAWN.register((oldPlayer, newPlayer, alive) -> {
			BWComponents.TRANSFORMATION_COMPONENT.get(newPlayer).setAlternateForm(false);
			BWComponents.ADDITIONAL_WEREWOLF_DATA_COMPONENT.get(newPlayer).setForcedTransformation(false);
		});
		ServerPlayerEvents.COPY_FROM.register((oldPlayer, newPlayer, alive) -> {
			if (alive && BWComponents.TRANSFORMATION_COMPONENT.get(oldPlayer).isAlternateForm()) {
				TransformationAbilityPacket.useAbility(newPlayer, true);
			}
		});
		ServerEntityCombatEvents.AFTER_KILLED_OTHER_ENTITY.register((world, entity, killedEntity) -> {
			if (entity instanceof LivingEntity livingEntity) {
				if (livingEntity instanceof PlayerEntity player && killedEntity.getGroup() == EntityGroup.ARTHROPOD && BewitchmentAPI.getFamiliar(player) == BWEntityTypes.TOAD) {
					livingEntity.heal(livingEntity.getMaxHealth() * 1 / 4f);
				}
				if (livingEntity instanceof PlayerEntity player && BWComponents.CONTRACTS_COMPONENT.get(player).hasContract(BWContracts.DEATH)) {
					livingEntity.heal(livingEntity.getMaxHealth() / 4f);
				}
				if (livingEntity.getMainHandStack().getItem() instanceof AthameItem) {
					BlockPos glyphPos = BWUtil.getClosestBlockPos(killedEntity.getBlockPos(), 6, currentPos -> world.getBlockEntity(currentPos) instanceof GlyphBlockEntity);
					if (glyphPos != null) {
						((GlyphBlockEntity) world.getBlockEntity(glyphPos)).onUse(world, glyphPos, livingEntity, Hand.MAIN_HAND, killedEntity);
					}
					if (world.isNight()) {
						boolean chicken = killedEntity instanceof ChickenEntity, wolf = killedEntity instanceof WolfEntity;
						if (chicken || wolf) {
							RegistryEntry<Biome> biome = world.getBiome(killedEntity.getBlockPos());
							MobEntity boss = null;
							if (chicken) {
								if (biome.isIn(ConventionalBiomeTags.EXTREME_HILLS) || biome.isIn(ConventionalBiomeTags.ICY) || biome.isIn(ConventionalBiomeTags.MOUNTAIN)) {
									boss = BWEntityTypes.LILITH.create(world);
								}
							}
							if (wolf) {
								if (biome.isIn(ConventionalBiomeTags.TAIGA) || biome.isIn(ConventionalBiomeTags.FOREST)) {
									boss = BWEntityTypes.HERNE.create(world);
								}
							}
							if (boss != null) {
								BlockPos brazierPos = BWUtil.getClosestBlockPos(killedEntity.getBlockPos(), 8, currentPos -> world.getBlockEntity(currentPos) instanceof BrazierBlockEntity brazier && brazier.incenseRecipe.effect == BWStatusEffects.MORTAL_COIL);
								if (brazierPos != null) {
									world.breakBlock(brazierPos, false);
									world.createExplosion(null, brazierPos.getX() + 0.5, brazierPos.getY() + 0.5, brazierPos.getZ() + 0.5, 3, World.ExplosionSourceType.TNT);
									boss.initialize(world, world.getLocalDifficulty(brazierPos), SpawnReason.EVENT, null, null);
									boss.updatePositionAndAngles(brazierPos.getX() + 0.5, brazierPos.getY(), brazierPos.getZ() + 0.5, world.random.nextFloat() * 360, 0);
									world.spawnEntity(boss);
								}
							}
						}
					}
					for (AthameDropRecipe recipe : world.getRecipeManager().listAllOfType(BWRecipeTypes.ATHAME_DROP_RECIPE_TYPE)) {
						if (recipe.entity_type.equals(killedEntity.getType()) && world.random.nextFloat() < recipe.chance * (EnchantmentHelper.getLooting(livingEntity) + 1)) {
							ItemStack drop = recipe.getOutput(entity.getWorld().getRegistryManager()).copy();
							if (recipe.entity_type == EntityType.PLAYER) {
								drop.getOrCreateNbt().putString("SkullOwner", killedEntity.getName().getString());
							}
							ItemScatterer.spawn(world, killedEntity.getX() + 0.5, killedEntity.getY() + 0.5, killedEntity.getZ() + 0.5, drop);
						}
					}
					if (livingEntity instanceof PlayerEntity player && livingEntity.getOffHandStack().getItem() == Items.GLASS_BOTTLE && BWComponents.BLOOD_COMPONENT.get(killedEntity).getBlood() > 20 && killedEntity.getType().isIn(BWTags.HAS_BLOOD)) {
						world.playSound(null, livingEntity.getBlockPos(), SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.PLAYERS, 1, 0.5f);
						BWUtil.addItemToInventoryAndConsume(player, Hand.OFF_HAND, new ItemStack(BWObjects.BOTTLE_OF_BLOOD));
					}
				}
			}
		});
		UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
			if (entity instanceof LivingEntity livingEntity && hand == Hand.MAIN_HAND && player.isSneaking() && entity.isAlive() && BewitchmentAPI.isVampire(player, true) && player.getStackInHand(hand).isEmpty()) {
				int toGive = entity.getType().isIn(BWTags.HAS_BLOOD) ? 5 : entity instanceof AnimalEntity ? 1 : 0;
				toGive = BloodSuckEvents.BLOOD_AMOUNT.invoker().onBloodSuck(player, livingEntity, toGive);
				if (toGive > 0) {
					BloodComponent playerBloodComponent = BWComponents.BLOOD_COMPONENT.get(player);
					BloodComponent entityBloodComponent = BWComponents.BLOOD_COMPONENT.get(livingEntity);
					if (playerBloodComponent.fillBlood(toGive, true) && entityBloodComponent.drainBlood(10, true)) {
						if (!world.isClient && livingEntity.hurtTime == 0) {
							BloodSuckEvents.ON_BLOOD_SUCK.invoker().onBloodSuck(player, livingEntity, toGive);
							world.playSound(null, player.getBlockPos(), SoundEvents.ITEM_HONEY_BOTTLE_DRINK, player.getSoundCategory(), 1, 0.5f);
							if (!livingEntity.isSleeping() || entityBloodComponent.getBlood() < BloodComponent.MAX_BLOOD / 2) {
								entity.damage(BWDamageSources.create(entity.getWorld(), BWDamageSources.VAMPIRE), 2);
							}
							playerBloodComponent.fillBlood(toGive, false);
							entityBloodComponent.drainBlood(10, false);
						}
						return ActionResult.success(world.isClient);
					}
				}
			}
			return ActionResult.PASS;
		});
		EntitySleepEvents.ALLOW_SLEEP_TIME.register((player, sleepingPos, vanillaResult) -> player.getWorld().getBlockState(sleepingPos).getBlock() instanceof CoffinBlock && player.getWorld().isDay() ? ActionResult.success(player.getWorld().isClient) : ActionResult.PASS);
		EntitySleepEvents.ALLOW_SLEEPING.register((player, sleepingPos) -> {
			if (BWComponents.TRANSFORMATION_COMPONENT.get(player).isAlternateForm()) {
				player.sendMessage(Text.translatable("block.minecraft.bed.transformation"), true);
				return PlayerEntity.SleepFailureReason.OTHER_PROBLEM;
			}
			if (player.getWorld().getBlockState(sleepingPos).getBlock() instanceof CoffinBlock) {
				if (player.getWorld().isNight()) {
					player.sendMessage(Text.translatable("block.minecraft.bed.coffin"), true);
					return PlayerEntity.SleepFailureReason.OTHER_PROBLEM;
				}
				return null;
			}
			return null;
		});
		EntitySleepEvents.STOP_SLEEPING.register((entity, sleepingPos) -> {
			if (!entity.getWorld().isClient) {
				BWUtil.getBlockPoses(entity.getBlockPos(), 12, currentPos -> entity.getWorld().getBlockEntity(currentPos) instanceof BrazierBlockEntity brazier && brazier.incenseRecipe != null).forEach(foundPos -> {
					IncenseRecipe recipe = ((BrazierBlockEntity) entity.getWorld().getBlockEntity(foundPos)).incenseRecipe;
					int durationMultiplier = 1;
					BlockPos nearestSigil = BWUtil.getClosestBlockPos(entity.getBlockPos(), 16, currentPos -> entity.getWorld().getBlockEntity(currentPos) instanceof SigilBlockEntity sigil && sigil.getSigil() == BWSigils.EXTENDING);
					if (nearestSigil != null) {
						BlockEntity blockEntity = entity.getWorld().getBlockEntity(nearestSigil);
						SigilHolder sigilHolder = ((SigilHolder) blockEntity);
						if (sigilHolder.test(entity)) {
							sigilHolder.setUses(sigilHolder.getUses() - 1);
							blockEntity.markDirty();
							durationMultiplier = 2;
						}
					}
					entity.addStatusEffect(new StatusEffectInstance(recipe.effect, 24000 * durationMultiplier, recipe.amplifier, true, false));
				});
			}
		});
		BWScaleTypes.init();
		BWObjects.init();
		BWBoatTypes.init();
		BWBlockEntityTypes.init();
		BWEntityTypes.init();
		BWStatusEffects.init();
		BWEnchantments.init();
		BWRitualFunctions.init();
		BWFortunes.init();
		BWSigils.init();
		BWTransformations.init();
		BWContracts.init();
		BWCurses.init();
		BWSoundEvents.init();
		BWParticleTypes.init();
		BWRecipeTypes.init();
		BWWorldGenerators.init();
		BWScreenHandlerTypes.init();
		BewitchmentAPI.registerAltarMapEntries(BWObjects.STONE_WITCH_ALTAR);
		BewitchmentAPI.registerAltarMapEntries(BWObjects.MOSSY_COBBLESTONE_WITCH_ALTAR);
		BewitchmentAPI.registerAltarMapEntries(BWObjects.PRISMARINE_WITCH_ALTAR);
		BewitchmentAPI.registerAltarMapEntries(BWObjects.NETHER_BRICK_WITCH_ALTAR);
		BewitchmentAPI.registerAltarMapEntries(BWObjects.BLACKSTONE_WITCH_ALTAR);
		BewitchmentAPI.registerAltarMapEntries(BWObjects.GOLDEN_WITCH_ALTAR);
		BewitchmentAPI.registerAltarMapEntries(BWObjects.END_STONE_WITCH_ALTAR);
		BewitchmentAPI.registerAltarMapEntries(BWObjects.OBSIDIAN_WITCH_ALTAR);
		BewitchmentAPI.registerAltarMapEntries(BWObjects.PURPUR_WITCH_ALTAR);
	}
}
