package moriyashiine.bewitchment.common;

import dev.emi.trinkets.api.SlotGroups;
import dev.emi.trinkets.api.Slots;
import dev.emi.trinkets.api.TrinketSlots;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.api.event.BloodSuckEvents;
import moriyashiine.bewitchment.api.interfaces.entity.*;
import moriyashiine.bewitchment.common.block.CoffinBlock;
import moriyashiine.bewitchment.common.block.entity.BrazierBlockEntity;
import moriyashiine.bewitchment.common.block.entity.GlyphBlockEntity;
import moriyashiine.bewitchment.common.block.entity.SigilBlockEntity;
import moriyashiine.bewitchment.common.block.entity.interfaces.SigilHolder;
import moriyashiine.bewitchment.common.entity.interfaces.*;
import moriyashiine.bewitchment.common.integration.requiem.BWRequiemCompat;
import moriyashiine.bewitchment.common.item.AthameItem;
import moriyashiine.bewitchment.common.misc.BWUtil;
import moriyashiine.bewitchment.common.network.packet.CauldronTeleportPacket;
import moriyashiine.bewitchment.common.network.packet.TogglePressingForwardPacket;
import moriyashiine.bewitchment.common.network.packet.TransformationAbilityPacket;
import moriyashiine.bewitchment.common.recipe.AthameDropRecipe;
import moriyashiine.bewitchment.common.recipe.IncenseRecipe;
import moriyashiine.bewitchment.common.registry.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityCombatEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.util.TriState;
import net.fabricmc.loader.api.FabricLoader;
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
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.explosion.Explosion;
import top.theillusivec4.somnus.api.PlayerSleepEvents;
import top.theillusivec4.somnus.api.WorldSleepEvents;

public class Bewitchment implements ModInitializer {
	public static final String MODID = "bewitchment";
	
	public static BWConfig config;
	
	public static final ItemGroup BEWITCHMENT_GROUP = FabricItemGroupBuilder.build(new Identifier(MODID, MODID), () -> new ItemStack(BWObjects.ATHAME));
	
	public static boolean isNourishLoaded, isRequiemLoaded;
	
	@SuppressWarnings("ConstantConditions")
	@Override
	public void onInitialize() {
		AutoConfig.register(BWConfig.class, GsonConfigSerializer::new);
		config = AutoConfig.getConfigHolder(BWConfig.class).getConfig();
		TrinketSlots.addSlot(SlotGroups.CHEST, Slots.NECKLACE, new Identifier("trinkets", "textures/item/empty_trinket_slot_necklace.png"));
		TrinketSlots.addSlot(SlotGroups.LEGS, Slots.BELT, new Identifier("trinkets", "textures/item/empty_trinket_slot_belt.png"));
		TrinketSlots.addSlot(SlotGroups.FEET, Slots.AGLET, new Identifier("trinkets", "textures/item/empty_trinket_slot_aglet.png"));
		ServerPlayNetworking.registerGlobalReceiver(CauldronTeleportPacket.ID, CauldronTeleportPacket::handle);
		ServerPlayNetworking.registerGlobalReceiver(TransformationAbilityPacket.ID, TransformationAbilityPacket::handle);
		ServerPlayNetworking.registerGlobalReceiver(TogglePressingForwardPacket.ID, TogglePressingForwardPacket::handle);
		CommandRegistrationCallback.EVENT.register(BWCommands::init);
		BWCommands.registerArgumentTypes();
		ServerLifecycleEvents.SERVER_STOPPED.register(server -> BewitchmentAPI.fakePlayer = null);
		ServerPlayerEvents.COPY_FROM.register((oldPlayer, newPlayer, alive) -> {
			if (alive) {
				((MagicAccessor) newPlayer).setMagic(((MagicAccessor) oldPlayer).getMagic());
				((BloodAccessor) newPlayer).setBlood(((BloodAccessor) oldPlayer).getBlood());
				((RespawnTimerAccessor) newPlayer).setRespawnTimer(((RespawnTimerAccessor) oldPlayer).getRespawnTimer());
				((TrueInvisibleAccessor) newPlayer).setTrueInvisible(((TrueInvisibleAccessor) oldPlayer).getTrueInvisible());
				((SubmergedInWaterAccessor) newPlayer).setSubmergedInWater(((SubmergedInWaterAccessor) oldPlayer).getSubmergedInWater());
				((TransformationAccessor) newPlayer).setAlternateForm(((TransformationAccessor) oldPlayer).getAlternateForm());
				((WerewolfAccessor) newPlayer).setForcedTransformation(((WerewolfAccessor) oldPlayer).getForcedTransformation());
			}
			((PolymorphAccessor) newPlayer).setPolymorphUUID(((PolymorphAccessor) oldPlayer).getPolymorphUUID());
			((PolymorphAccessor) newPlayer).setPolymorphName(((PolymorphAccessor) oldPlayer).getPolymorphName());
			((FortuneAccessor) newPlayer).setFortune(((FortuneAccessor) oldPlayer).getFortune());
			((PledgeAccessor) newPlayer).setPledge(((PledgeAccessor) oldPlayer).getPledge());
			((TransformationAccessor) newPlayer).setTransformation(((TransformationAccessor) oldPlayer).getTransformation());
			((WerewolfAccessor) newPlayer).setWerewolfVariant(((WerewolfAccessor) oldPlayer).getWerewolfVariant());
			((ContractAccessor) newPlayer).getContracts().addAll(((ContractAccessor) oldPlayer).getContracts());
			((CurseAccessor) newPlayer).getCurses().addAll(((CurseAccessor) oldPlayer).getCurses());
		});
		ServerEntityCombatEvents.AFTER_KILLED_OTHER_ENTITY.register((world, entity, killedEntity) -> {
			if (entity instanceof PlayerEntity) {
				PlayerEntity player = (PlayerEntity) entity;
				if (killedEntity.getGroup() == EntityGroup.ARTHROPOD && BewitchmentAPI.getFamiliar(player) == BWEntityTypes.TOAD) {
					player.heal(player.getMaxHealth() * 1 / 4f);
				}
				if (player.getMainHandStack().getItem() instanceof AthameItem) {
					BlockPos glyph = BWUtil.getClosestBlockPos(killedEntity.getBlockPos(), 6, currentPos -> world.getBlockEntity(currentPos) instanceof GlyphBlockEntity);
					if (glyph != null) {
						((GlyphBlockEntity) world.getBlockEntity(glyph)).onUse(world, glyph, player, Hand.MAIN_HAND, killedEntity);
					}
					if (world.isNight()) {
						boolean chicken = killedEntity instanceof ChickenEntity;
						if ((chicken && (world.getBiome(killedEntity.getBlockPos()).getCategory() == Biome.Category.EXTREME_HILLS || world.getBiome(killedEntity.getBlockPos()).getCategory() == Biome.Category.ICY)) || (killedEntity instanceof WolfEntity && (world.getBiome(killedEntity.getBlockPos()).getCategory() == Biome.Category.TAIGA || world.getBiome(killedEntity.getBlockPos()).getCategory() == Biome.Category.FOREST))) {
							BlockPos brazierPos = BWUtil.getClosestBlockPos(killedEntity.getBlockPos(), 8, currentPos -> {
								BlockEntity blockEntity = world.getBlockEntity(currentPos);
								return blockEntity instanceof BrazierBlockEntity && ((BrazierBlockEntity) blockEntity).incenseRecipe.effect == BWStatusEffects.MORTAL_COIL;
							});
							if (brazierPos != null) {
								world.breakBlock(brazierPos, false);
								world.createExplosion(null, brazierPos.getX() + 0.5, brazierPos.getY() + 0.5, brazierPos.getZ() + 0.5, 3, Explosion.DestructionType.BREAK);
								MobEntity boss = chicken ? BWEntityTypes.LILITH.create(world) : BWEntityTypes.HERNE.create(world);
								if (boss != null) {
									boss.initialize(world, world.getLocalDifficulty(brazierPos), SpawnReason.EVENT, null, null);
									boss.updatePositionAndAngles(brazierPos.getX() + 0.5, brazierPos.getY(), brazierPos.getZ() + 0.5, world.random.nextFloat() * 360, 0);
									world.spawnEntity(boss);
								}
							}
						}
					}
					for (AthameDropRecipe recipe : world.getRecipeManager().listAllOfType(BWRecipeTypes.ATHAME_DROP_RECIPE_TYPE)) {
						if (recipe.entity_type.equals(killedEntity.getType()) && world.random.nextFloat() < recipe.chance * (EnchantmentHelper.getLooting(player) + 1)) {
							ItemStack drop = recipe.getOutput().copy();
							if (recipe.entity_type == EntityType.PLAYER) {
								drop.getOrCreateTag().putString("SkullOwner", killedEntity.getName().getString());
							}
							ItemScatterer.spawn(world, killedEntity.getX() + 0.5, killedEntity.getY() + 0.5, killedEntity.getZ() + 0.5, drop);
						}
					}
					if (player.getOffHandStack().getItem() == Items.GLASS_BOTTLE && ((BloodAccessor) killedEntity).getBlood() > 20 && BWTags.HAS_BLOOD.contains(killedEntity.getType())) {
						world.playSound(null, entity.getBlockPos(), SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.PLAYERS, 1, 0.5f);
						BWUtil.addItemToInventoryAndConsume(player, Hand.OFF_HAND, new ItemStack(BWObjects.BOTTLE_OF_BLOOD));
					}
				}
				if (((ContractAccessor) entity).hasContract(BWContracts.DEATH)) {
					player.heal(player.getMaxHealth() / 4f);
				}
			}
		});
		UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
			if (entity instanceof LivingEntity && hand == Hand.MAIN_HAND && player.isSneaking() && entity.isAlive() && BewitchmentAPI.isVampire(player, true) && player.getStackInHand(hand).isEmpty()) {
				int toGive = BWTags.HAS_BLOOD.contains(entity.getType()) ? 5 : entity instanceof AnimalEntity ? 1 : 0;
				toGive = BloodSuckEvents.BLOOD_AMOUNT.invoker().onBloodSuck(player, (LivingEntity) entity, toGive);
				if (toGive > 0) {
					BloodAccessor playerBlood = (BloodAccessor) player;
					BloodAccessor entityBlood = (BloodAccessor) entity;
					if (playerBlood.fillBlood(toGive, true) && entityBlood.drainBlood(10, true)) {
						if (!world.isClient && ((LivingEntity) entity).hurtTime == 0) {
							BloodSuckEvents.ON_BLOOD_SUCK.invoker().onBloodSuck(player, (LivingEntity) entity, toGive);
							world.playSound(null, player.getBlockPos(), SoundEvents.ITEM_HONEY_BOTTLE_DRINK, player.getSoundCategory(), 1, 0.5f);
							if (!((LivingEntity) entity).isSleeping() || entityBlood.getBlood() < entityBlood.MAX_BLOOD / 2) {
								entity.damage(BWDamageSources.VAMPIRE, 2);
							}
							playerBlood.fillBlood(toGive, false);
							entityBlood.drainBlood(10, false);
						}
						return ActionResult.success(world.isClient);
					}
				}
			}
			return ActionResult.PASS;
		});
		WorldSleepEvents.WORLD_WAKE_TIME.register((world, newTime, curTime) -> {
			if (world.isDay()) {
				long time = curTime;
				while (time % 24000 < 13000) {
					time++;
				}
				return time;
			}
			return newTime;
		});
		PlayerSleepEvents.CAN_SLEEP_NOW.register((player, pos) -> {
			if (player.world.getBlockState(pos).getBlock() instanceof CoffinBlock) {
				return player.world.isDay() ? TriState.TRUE : TriState.FALSE;
			}
			return TriState.DEFAULT;
		});
		PlayerSleepEvents.TRY_SLEEP.register((player, pos) -> {
			if (player.world.getBlockState(pos).getBlock() instanceof CoffinBlock && player.world.isNight()) {
				player.sendMessage(new TranslatableText("block.minecraft.bed.coffin"), true);
				return PlayerEntity.SleepFailureReason.OTHER_PROBLEM;
			}
			return null;
		});
		PlayerSleepEvents.WAKE_UP.register((player, reset, update) -> {
			if (!player.world.isClient) {
				BWUtil.getBlockPoses(player.getBlockPos(), 12, foundPos -> player.world.getBlockEntity(foundPos) instanceof BrazierBlockEntity && ((BrazierBlockEntity) player.world.getBlockEntity(foundPos)).incenseRecipe != null).forEach(foundPos -> {
					IncenseRecipe recipe = ((BrazierBlockEntity) player.world.getBlockEntity(foundPos)).incenseRecipe;
					int durationMultiplier = 1;
					BlockPos nearestSigil = BWUtil.getClosestBlockPos(player.getBlockPos(), 16, foundSigil -> player.world.getBlockEntity(foundSigil) instanceof SigilBlockEntity && ((SigilBlockEntity) player.world.getBlockEntity(foundSigil)).getSigil() == BWSigils.EXTENDING);
					if (nearestSigil != null) {
						BlockEntity blockEntity = player.world.getBlockEntity(nearestSigil);
						SigilHolder sigil = ((SigilHolder) blockEntity);
						if (sigil.test(player)) {
							sigil.setUses(sigil.getUses() - 1);
							blockEntity.markDirty();
							durationMultiplier = 2;
						}
					}
					player.addStatusEffect(new StatusEffectInstance(recipe.effect, 24000 * durationMultiplier, recipe.amplifier, true, false));
				});
			}
		});
		BWScaleTypes.init();
		BWObjects.init();
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
		BewitchmentAPI.registerAltarMapEntries(BWObjects.STONE_WITCH_ALTAR);
		BewitchmentAPI.registerAltarMapEntries(BWObjects.MOSSY_COBBLESTONE_WITCH_ALTAR);
		BewitchmentAPI.registerAltarMapEntries(BWObjects.PRISMARINE_WITCH_ALTAR);
		BewitchmentAPI.registerAltarMapEntries(BWObjects.NETHER_BRICK_WITCH_ALTAR);
		BewitchmentAPI.registerAltarMapEntries(BWObjects.BLACKSTONE_WITCH_ALTAR);
		BewitchmentAPI.registerAltarMapEntries(BWObjects.GOLDEN_WITCH_ALTAR);
		BewitchmentAPI.registerAltarMapEntries(BWObjects.END_STONE_WITCH_ALTAR);
		BewitchmentAPI.registerAltarMapEntries(BWObjects.OBSIDIAN_WITCH_ALTAR);
		BewitchmentAPI.registerAltarMapEntries(BWObjects.PURPUR_WITCH_ALTAR);
		isNourishLoaded = FabricLoader.getInstance().isModLoaded("nourish");
		isRequiemLoaded = FabricLoader.getInstance().isModLoaded("requiem");
		if (isRequiemLoaded) {
			BWRequiemCompat.init();
		}
	}
}
