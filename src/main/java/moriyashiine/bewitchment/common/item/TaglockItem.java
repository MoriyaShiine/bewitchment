package moriyashiine.bewitchment.common.item;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.common.registry.BWTags;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BedBlock;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
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

public class TaglockItem extends Item {
	public TaglockItem(Settings settings) {
		super(settings);
	}
	
	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		World world = context.getWorld();
		BlockPos pos = context.getBlockPos();
		if (world.getBlockState(pos).getBlock() instanceof BedBlock) {
			PlayerEntity player = context.getPlayer();
			if (player != null && player.isSneaking()) {
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
						return useTaglock(player, earliestSleeper, context.getHand(), false);
					}
				}
			}
			return ActionResult.success(world.isClient);
		}
		return super.useOnBlock(context);
	}
	
	@Override
	public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
		return useTaglock(user, entity, hand, true);
	}
	
	@Environment(EnvType.CLIENT)
	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		if (stack.hasTag()) {
			tooltip.add(new LiteralText(stack.getOrCreateTag().getString("OwnerName")).setStyle(Style.EMPTY.withColor(Formatting.GRAY)));
		}
	}
	
	private ActionResult useTaglock(PlayerEntity user, LivingEntity entity, Hand hand, boolean checkVisibility) {
		ItemStack stack = user.getStackInHand(hand);
		if (entity.isAlive() && !BWTags.BOSSES.contains(entity.getType()) && !hasTaglock(stack)) {
			if (checkVisibility) {
				double targetYaw = entity.getHeadYaw() % 360;
				double userYaw = user.getHeadYaw() % 360;
				if (userYaw < 0) {
					userYaw += 360;
				}
				if (targetYaw < 0) {
					targetYaw += 360;
				}
				if (Math.abs(targetYaw - userYaw) > 120) {
					if (entity instanceof PlayerEntity) {
						((PlayerEntity) entity).sendMessage(new TranslatableText("bewitchment.taglock_fail", user.getDisplayName().getString()), false);
					}
					user.world.playSound(null, entity.getBlockPos(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 1, 1);
					return ActionResult.FAIL;
				}
			}
			boolean client = user.world.isClient;
			if (!client) {
				if (entity instanceof MobEntity) {
					((MobEntity) entity).setPersistent();
				}
				ItemStack taglock = new ItemStack(this);
				taglock.getOrCreateTag().putUuid("OwnerUUID", entity.getUuid());
				taglock.getOrCreateTag().putString("OwnerName", entity.getDisplayName().getString());
				taglock.getOrCreateTag().putBoolean("FromPlayer", entity instanceof PlayerEntity);
				BewitchmentAPI.addItemToInventoryAndConsume(user, hand, taglock);
			}
			return ActionResult.success(client);
		}
		return ActionResult.FAIL;
	}
	
	public static boolean hasTaglock(ItemStack stack) {
		return stack.hasTag() && !stack.getOrCreateTag().getString("OwnerName").isEmpty();
	}
}
