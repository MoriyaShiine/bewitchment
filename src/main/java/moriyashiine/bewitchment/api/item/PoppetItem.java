package moriyashiine.bewitchment.api.item;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.api.event.PoppetCallback;
import moriyashiine.bewitchment.common.item.TaglockItem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PoppetItem extends Item {
	public final boolean worksInShelf;
	
	public PoppetItem(Settings settings, boolean worksInShelf) {
		super(settings);
		this.worksInShelf = worksInShelf;
	}
	
	@Override
	public boolean isEnchantable(ItemStack stack) {
		return false;
	}
	
	@Environment(EnvType.CLIENT)
	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		if (TaglockItem.hasTaglock(stack)) {
			tooltip.add(new LiteralText(TaglockItem.getTaglockName(stack)).formatted(Formatting.GRAY));
		}
	}

	public static boolean usePoppet(LivingEntity user, ItemStack poppet){
		if (PoppetCallback.EVENT.invoker().use(user, poppet) != ActionResult.FAIL) {
			if (poppet.damage(user instanceof PlayerEntity && BewitchmentAPI.getFamiliar((PlayerEntity) user) == EntityType.WOLF && user.getRandom().nextBoolean() ? 0 : 1, user.getRandom(), null) && poppet.getDamage() >= poppet.getMaxDamage()) {
				poppet.decrement(1);
			}
			return true;
		}
		return false;
	}
}
