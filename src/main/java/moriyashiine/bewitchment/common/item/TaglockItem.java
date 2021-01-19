package moriyashiine.bewitchment.common.item;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.api.interfaces.block.entity.HasSigil;
import moriyashiine.bewitchment.common.registry.BWSigils;
import moriyashiine.bewitchment.common.registry.BWSoundEvents;
import moriyashiine.bewitchment.common.registry.BWTags;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BedBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

@SuppressWarnings("ConstantConditions")
public class TaglockItem extends Item {
	public TaglockItem(Settings settings) {
		super(settings);
	}
	
	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		World world = context.getWorld();
		BlockPos pos = context.getBlockPos();
		PlayerEntity player = context.getPlayer();
		boolean client = world.isClient;
		if (world.getBlockState(pos).getBlock() instanceof BedBlock) {
			if (!client && player != null && player.isSneaking()) {
				MinecraftServer server = world.getServer();
				if (server != null) {
					PlayerEntity earliestSleeper = null;
					for (ServerPlayerEntity playerEntity : server.getPlayerManager().getPlayerList()) {
						BlockPos bedPos = playerEntity.getSpawnPointPosition();
						if (bedPos != null && bedPos.equals(pos) && (earliestSleeper == null || playerEntity.getSleepTimer() < earliestSleeper.getSleepTimer())) {
							earliestSleeper = playerEntity;
						}
					}
					if (earliestSleeper != null) {
						return useTaglock(player, earliestSleeper, context.getHand(), false, true);
					}
				}
			}
			return ActionResult.success(client);
		}
		else {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof HasSigil) {
				ItemStack stack = context.getStack();
				if (hasTaglock(stack)) {
					if (!client) {
						HasSigil sigil = (HasSigil) blockEntity;
						if (sigil.getSigil() != null) {
							if (sigil.getEntities().isEmpty()) {
								sigil.setModeOnWhitelist(true);
							}
							UUID uuid = getTaglockUUID(stack);
							if (!sigil.getEntities().contains(uuid)) {
								sigil.getEntities().add(uuid);
								if (!player.isCreative()) {
									stack.decrement(1);
								}
								blockEntity.markDirty();
							}
						}
					}
					return ActionResult.success(client);
				}
			}
		}
		return super.useOnBlock(context);
	}
	
	@Override
	public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
		return useTaglock(user, entity, hand, true, false);
	}
	
	@Environment(EnvType.CLIENT)
	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		if (hasTaglock(stack)) {
			tooltip.add(new LiteralText(getTaglockName(stack)).setStyle(Style.EMPTY.withColor(Formatting.GRAY)));
		}
	}
	
	private ActionResult useTaglock(PlayerEntity user, LivingEntity entity, Hand hand, boolean checkVisibility, boolean bed) {
		ItemStack stack = user.getStackInHand(hand);
		if (entity.isAlive() && !BWTags.BOSSES.contains(entity.getType()) && !hasTaglock(stack)) {
			boolean failed = false;
			BlockPos sigilPos = BewitchmentAPI.getClosestBlockPos(entity.getBlockPos(), 16, currentPos -> user.world.getBlockEntity(currentPos) instanceof HasSigil && ((HasSigil) user.world.getBlockEntity(currentPos)).getSigil() == BWSigils.SLIPPERY);
			if (sigilPos == null && bed) {
				sigilPos = BewitchmentAPI.getClosestBlockPos(user.getBlockPos(), 16, currentPos -> user.world.getBlockEntity(currentPos) instanceof HasSigil && ((HasSigil) user.world.getBlockEntity(currentPos)).getSigil() == BWSigils.SLIPPERY);
			}
			if (sigilPos != null) {
				BlockEntity blockEntity = user.world.getBlockEntity(sigilPos);
				HasSigil sigil = (HasSigil) blockEntity;
				if (sigil.test(entity)) {
					sigil.setUses(sigil.getUses() - 1);
					blockEntity.markDirty();
					failed = true;
				}
			}
			else if (checkVisibility && !entity.isSleeping()) {
				double targetYaw = entity.getHeadYaw() % 360;
				double userYaw = user.getHeadYaw() % 360;
				if (userYaw < 0) {
					userYaw += 360;
				}
				if (targetYaw < 0) {
					targetYaw += 360;
				}
				failed = Math.abs(targetYaw - userYaw) > 120;
			}
			if (failed) {
				if (entity instanceof PlayerEntity) {
					((PlayerEntity) entity).sendMessage(new TranslatableText("bewitchment.taglock_fail", user.getDisplayName().getString()), false);
				}
				user.world.playSound(null, entity.getBlockPos(), BWSoundEvents.ENTITY_GENERIC_PLING, SoundCategory.PLAYERS, 1, 1);
				return ActionResult.FAIL;
			}
			boolean client = user.world.isClient;
			if (!client) {
				if (entity instanceof MobEntity) {
					((MobEntity) entity).setPersistent();
				}
				BewitchmentAPI.addItemToInventoryAndConsume(user, hand, putTaglock(new ItemStack(this), entity));
			}
			return ActionResult.success(client);
		}
		return ActionResult.FAIL;
	}
	
	public static ItemStack copyTo(ItemStack from, ItemStack to) {
		if (hasTaglock(from)) {
			to.getOrCreateTag().putUuid("OwnerUUID", from.getOrCreateTag().getUuid("OwnerUUID"));
			to.getOrCreateTag().putString("OwnerName", from.getOrCreateTag().getString("OwnerName"));
			to.getOrCreateTag().putBoolean("FromPlayer", from.getOrCreateTag().getBoolean("FromPlayer"));
		}
		return to;
	}
	
	public static boolean hasTaglock(ItemStack stack) {
		return stack.hasTag() && stack.getOrCreateTag().contains("OwnerUUID");
	}
	
	public static ItemStack putTaglock(ItemStack stack, Entity entity) {
		stack.getOrCreateTag().putUuid("OwnerUUID", entity.getUuid());
		stack.getOrCreateTag().putString("OwnerName", entity.getDisplayName().getString());
		stack.getOrCreateTag().putBoolean("FromPlayer", entity instanceof PlayerEntity);
		return stack;
	}
	
	public static void removeTaglock(ItemStack stack) {
		if (stack.hasTag()) {
			stack.getOrCreateTag().remove("OwnerUUID");
			stack.getOrCreateTag().remove("OwnerName");
			stack.getOrCreateTag().remove("FromPlayer");
		}
	}
	
	public static UUID getTaglockUUID(ItemStack stack) {
		if (hasTaglock(stack)) {
			return stack.getOrCreateTag().getUuid("OwnerUUID");
		}
		return null;
	}
	
	public static String getTaglockName(ItemStack stack) {
		if (hasTaglock(stack)) {
			return stack.getOrCreateTag().getString("OwnerName");
		}
		return "";
	}
	
	public static boolean isTaglockFromPlayer(ItemStack stack) {
		return hasTaglock(stack) && stack.getOrCreateTag().getBoolean("FromPlayer");
	}
}
