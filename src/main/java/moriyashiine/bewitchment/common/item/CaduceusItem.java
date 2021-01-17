package moriyashiine.bewitchment.common.item;

import moriyashiine.bewitchment.api.interfaces.CaduceusFireballAccessor;
import moriyashiine.bewitchment.api.interfaces.MagicAccessor;
import moriyashiine.bewitchment.common.registry.BWSoundEvents;
import moriyashiine.bewitchment.common.registry.BWStatusEffects;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

@SuppressWarnings("ConstantConditions")
public class CaduceusItem extends SwordItem {
	public CaduceusItem(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
		super(toolMaterial, attackDamage, attackSpeed, settings);
	}
	
	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		return ItemUsage.consumeHeldItem(world, user, hand);
	}
	
	@Override
	public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
		MagicAccessor.of(user).ifPresent(magicAccessor -> {
			if (!world.isClient && !user.hasStatusEffect(BWStatusEffects.INHIBITED) && ((user instanceof PlayerEntity && ((PlayerEntity) user).isCreative()) || magicAccessor.drainMagic(250, false))) {
				FireballEntity fireball = new FireballEntity(world, user, user.getRotationVector().x, user.getRotationVector().y, user.getRotationVector().z);
				fireball.setOwner(user);
				fireball.setPos(fireball.getX(), fireball.getY() + 1, fireball.getZ());
				((CaduceusFireballAccessor) fireball).setFromCaduceus(true);
				world.playSound(null, user.getBlockPos(), BWSoundEvents.ENTITY_GENERIC_SHOOT, SoundCategory.HOSTILE, 1, 1);
				world.spawnEntity(fireball);
				stack.damage(1, user, stackUser -> stackUser.sendToolBreakStatus(user.getActiveHand()));
			}
		});
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
}
