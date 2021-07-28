package moriyashiine.bewitchment.common.item;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.api.interfaces.entity.MagicAccessor;
import moriyashiine.bewitchment.common.entity.interfaces.CaduceusFireballAccessor;
import moriyashiine.bewitchment.common.registry.BWSoundEvents;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.MiningToolItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.sound.SoundCategory;
import net.minecraft.tag.Tag;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

@SuppressWarnings("ConstantConditions")
public class CaduceusItem extends MiningToolItem {
	public CaduceusItem(float attackDamage, float attackSpeed, ToolMaterial material, Tag<Block> effectiveBlocks, Settings settings) {
		super(attackDamage, attackSpeed, material, effectiveBlocks, settings);
	}
	
	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		return ItemUsage.consumeHeldItem(world, user, hand);
	}
	
	@Override
	public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
		if (user instanceof MagicAccessor) {
			if (!world.isClient && user instanceof PlayerEntity && BewitchmentAPI.drainMagic((PlayerEntity) user, 2, false)) {
				FireballEntity fireball = new FireballEntity(world, user, user.getRotationVector().x, user.getRotationVector().y, user.getRotationVector().z, 1);
				fireball.setOwner(user);
				fireball.setPos(fireball.getX(), fireball.getY() + 1, fireball.getZ());
				((CaduceusFireballAccessor) fireball).setFromCaduceus(true);
				world.playSound(null, user.getBlockPos(), BWSoundEvents.ENTITY_GENERIC_SHOOT, SoundCategory.HOSTILE, 1, 1);
				world.spawnEntity(fireball);
				stack.damage(1, user, stackUser -> stackUser.sendToolBreakStatus(user.getActiveHand()));
			}
		}
		return stack;
	}
	
	@Override
	public UseAction getUseAction(ItemStack stack) {
		return UseAction.BOW;
	}
	
	@Override
	public int getMaxUseTime(ItemStack stack) {
		return 24;
	}
	
	@Override
	public boolean isSuitableFor(BlockState state) {
		return true;
	}
	
	@Override
	public float getMiningSpeedMultiplier(ItemStack stack, BlockState state) {
		return miningSpeed * 0.75f;
	}
}
