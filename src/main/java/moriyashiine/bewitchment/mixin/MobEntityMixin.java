package moriyashiine.bewitchment.mixin;

import moriyashiine.bewitchment.common.entity.component.MinionComponent;
import moriyashiine.bewitchment.common.entity.living.GhostEntity;
import moriyashiine.bewitchment.common.item.TaglockItem;
import moriyashiine.bewitchment.common.misc.BWUtil;
import moriyashiine.bewitchment.common.registry.BWTags;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(MobEntity.class)
public abstract class MobEntityMixin extends LivingEntity {
	protected MobEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}
	
	@ModifyVariable(method = "setTarget", at = @At("HEAD"))
	private LivingEntity modifyTarget(LivingEntity target) {
		if (!world.isClient && target != null) {
			if (target instanceof GhostEntity) {
				return null;
			}
			if (target instanceof MobEntity mob && getUuid().equals(MinionComponent.get(mob).getMaster())) {
				return null;
			}
			if (isUndead() && !BWUtil.getBlockPoses(target.getBlockPos(), 2, foundPos -> BWTags.UNDEAD_MASK.contains(world.getBlockState(foundPos).getBlock())).isEmpty()) {
				return null;
			}
		}
		return target;
	}
	
	@Inject(method = "interactWithItem", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/entity/player/PlayerEntity;getStackInHand(Lnet/minecraft/util/Hand;)Lnet/minecraft/item/ItemStack;"), cancellable = true, locals = LocalCapture.CAPTURE_FAILEXCEPTION)
	private void interactWithItem(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> callbackInfoReturnable, ItemStack heldStack) {
		if (heldStack.getItem() instanceof TaglockItem) {
			callbackInfoReturnable.setReturnValue(TaglockItem.useTaglock(player, this, hand, true, false));
		}
	}
}
