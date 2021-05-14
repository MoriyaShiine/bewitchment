package moriyashiine.bewitchment.mixin;

import moriyashiine.bewitchment.common.block.util.interfaces.SpecialDoor;
import net.minecraft.block.BlockState;
import net.minecraft.block.DoorBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.task.OpenDoorsTask;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.entity.ai.pathing.PathNode;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(OpenDoorsTask.class)
public abstract class OpenDoorsTaskMixin {
	@Shadow
	protected abstract void method_30767(ServerWorld serverWorld, LivingEntity livingEntity, BlockPos blockPos);
	
	@Inject(method = "run", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/server/world/ServerWorld;getBlockState(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/BlockState;", ordinal = 1), cancellable = true, locals = LocalCapture.CAPTURE_FAILSOFT)
	private void run(ServerWorld world, LivingEntity entity, long time, CallbackInfo ci, Path path, PathNode pathNode, PathNode pathNode2, BlockPos pos, BlockState state) {
		if (state.getBlock() instanceof SpecialDoor) {
			ActionResult result = ((SpecialDoor) state.getBlock()).onSpecialUse(state, world, pos, entity, Hand.MAIN_HAND);
			if (state.isIn(BlockTags.WOODEN_DOORS)) {
				DoorBlock doorBlock = (DoorBlock) state.getBlock();
				if (!doorBlock.method_30841(state) && result != ActionResult.FAIL) {
					doorBlock.setOpen(world, state, pos, true);
				}
				method_30767(world, entity, pos);
			}
			BlockPos blockPos2 = pathNode2.getPos();
			BlockState blockState2 = world.getBlockState(blockPos2);
			if (blockState2.isIn(BlockTags.WOODEN_DOORS)) {
				DoorBlock doorBlock2 = (DoorBlock) blockState2.getBlock();
				if (!doorBlock2.method_30841(blockState2) && result != ActionResult.FAIL) {
					doorBlock2.setOpen(world, blockState2, blockPos2, true);
					this.method_30767(world, entity, blockPos2);
				}
			}
			OpenDoorsTask.method_30760(world, entity, pathNode, pathNode2);
			ci.cancel();
		}
	}
}
