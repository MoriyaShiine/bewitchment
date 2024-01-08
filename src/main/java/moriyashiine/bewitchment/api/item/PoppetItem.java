/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.api.item;

import moriyashiine.bewitchment.common.item.TaglockItem;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;

public class PoppetItem extends Item {
	public static final Predicate<DamageSource> PROTECTION_POPPET_SOURCES = source -> source.isIn(DamageTypeTags.IS_FIRE) || source.isOf(DamageTypes.FALLING_BLOCK) || source.isOf(DamageTypes.FALLING_STALACTITE) || source.isOf(DamageTypes.FALLING_ANVIL) || source.isIn(DamageTypeTags.IS_FALL) || source.isOf(DamageTypes.CACTUS) || source.isOf(DamageTypes.CRAMMING) || source.isOf(DamageTypes.DROWN) || source.isOf(DamageTypes.DRY_OUT) || source.isOf(DamageTypes.FLY_INTO_WALL) || source.isOf(DamageTypes.FREEZE) || source.isOf(DamageTypes.IN_WALL) || source.isOf(DamageTypes.LIGHTNING_BOLT) || source.isOf(DamageTypes.SWEET_BERRY_BUSH) || source.isOf(DamageTypes.STARVE);

	public final boolean worksInShelf;

	public PoppetItem(Settings settings, boolean worksInShelf) {
		super(settings);
		this.worksInShelf = worksInShelf;
	}

	@Override
	public boolean isEnchantable(ItemStack stack) {
		return false;
	}

	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		if (TaglockItem.hasTaglock(stack)) {
			tooltip.add(Text.literal(TaglockItem.getTaglockName(stack)).formatted(Formatting.GRAY));
		}
	}
}
