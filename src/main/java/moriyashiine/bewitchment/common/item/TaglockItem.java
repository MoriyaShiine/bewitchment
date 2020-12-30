package moriyashiine.bewitchment.common.item;

import moriyashiine.bewitchment.common.registry.BWTags;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TaglockItem extends Item {
	public TaglockItem(Settings settings) {
		super(settings);
	}
	
	@Override
	public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
		if (!entity.isDead() && !BWTags.BOSSES.contains(entity.getType()) && !(stack.hasTag() && !stack.getOrCreateTag().getString("OwnerName").isEmpty())) {
			double targetYaw = entity.getHeadYaw() % 360;
			double userYaw = user.getHeadYaw() % 360;
			if (userYaw < 0) {
				userYaw += 360;
			}
			if (targetYaw < 0) {
				targetYaw += 360;
			}
			if (Math.abs(targetYaw - userYaw) < 120) {
				boolean client = user.world.isClient;
				if (!client) {
					ItemStack taglock = new ItemStack(this);
					taglock.getOrCreateTag().putUuid("OwnerUUID", entity.getUuid());
					taglock.getOrCreateTag().putString("OwnerName", entity.getDisplayName().getString());
					taglock.getOrCreateTag().putBoolean("FromPlayer", entity instanceof PlayerEntity);
					if (stack.getCount() == 1) {
						user.setStackInHand(hand, taglock);
					}
					else if (user.inventory.insertStack(taglock)) {
						user.dropStack(taglock);
					}
				}
				return ActionResult.success(client);
			}
			if (entity instanceof PlayerEntity) {
				((PlayerEntity) entity).sendMessage(new TranslatableText("bewitchment.taglock_fail", user.getDisplayName().getString()), false);
			}
			user.world.playSound(null, entity.getBlockPos(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 1, 1);
			return ActionResult.FAIL;
		}
		return super.useOnEntity(stack, user, entity, hand);
	}
	
	@Environment(EnvType.CLIENT)
	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		if (stack.hasTag()) {
			tooltip.add(new LiteralText(stack.getOrCreateTag().getString("OwnerName")).setStyle(Style.EMPTY.withColor(Formatting.GRAY)));
		}
	}
}
