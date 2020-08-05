package moriyashiine.bewitchment.common.item.tool;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.util.ActionResult;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;

public class AthameItem extends SwordItem {
	public static final Map<Block, Item> LOG_TO_BARK_MAP = new HashMap<>();
	
	public AthameItem(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
		super(toolMaterial, attackDamage, attackSpeed, settings);
	}
	
	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		World world = context.getWorld();
		PlayerEntity player = context.getPlayer();
		Item item = LOG_TO_BARK_MAP.get(world.getBlockState(context.getBlockPos()).getBlock());
		if (player != null && item != null && Items.WOODEN_AXE.useOnBlock(context).isAccepted()) {
			boolean client = world.isClient;
			if (!client) {
				BewitchmentAPI.giveStackToPlayer(player, new ItemStack(item, world.random.nextInt(3) + 1));
			}
			return ActionResult.success(client);
		}
		return super.useOnBlock(context);
	}
}
