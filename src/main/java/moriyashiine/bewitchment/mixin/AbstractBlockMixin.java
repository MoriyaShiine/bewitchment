package moriyashiine.bewitchment.mixin;

import moriyashiine.bewitchment.common.block.entity.interfaces.Lockable;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.DoorBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractBlock.class)
public abstract class AbstractBlockMixin {
	@Inject(method = "calcBlockBreakingDelta", at = @At("RETURN"), cancellable = true)
	private void calcBlockBreakingDelta(BlockState state, PlayerEntity player, BlockView world, BlockPos pos, CallbackInfoReturnable<Float> callbackInfo) {
		if (callbackInfo.getReturnValue() > 0) {
			BlockEntity entity = world.getBlockEntity(state.getBlock() instanceof DoorBlock && state.get(DoorBlock.HALF) == DoubleBlockHalf.UPPER ? pos.down() : pos);
			if (entity instanceof Lockable && ((Lockable) entity).getLocked() && !((Lockable) entity).test(player)) {
				callbackInfo.setReturnValue(0f);
			}
		}
	}
}
