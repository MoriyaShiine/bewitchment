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
	protected abstract void rememberToCloseDoor(ServerWorld world, LivingEntity entity, BlockPos pos);
	
	@Inject(method = "run", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/server/world/ServerWorld;getBlockState(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/BlockState;", ordinal = 1), cancellable = true, locals = LocalCapture.CAPTURE_FAILSOFT)
	private void run(ServerWorld world, LivingEntity entity, long time, CallbackInfo ci, Path path, PathNode pathNode, PathNode pathNode2, BlockPos pos, BlockState state) {
		if (state.getBlock() instanceof SpecialDoor specialDoor) {
			ActionResult result = specialDoor.onSpecialUse(state, world, pos, entity, Hand.MAIN_HAND);
			if (state.isIn(BlockTags.WOODEN_DOORS)) {
				DoorBlock doorBlock = (DoorBlock) state.getBlock();
				if (!doorBlock.isOpen(state) && result != ActionResult.FAIL) {
					doorBlock.setOpen(entity, world, state, pos, true);
				}
				rememberToCloseDoor(world, entity, pos);
			}
			BlockPos blockPos2 = new BlockPos(pathNode2.getPos());
			BlockState blockState2 = world.getBlockState(blockPos2);
			if (blockState2.isIn(BlockTags.WOODEN_DOORS)) {
				DoorBlock doorBlock2 = (DoorBlock) blockState2.getBlock();
				if (!doorBlock2.isOpen(blockState2) && result != ActionResult.FAIL) {
					doorBlock2.setOpen(entity, world, blockState2, blockPos2, true);
					rememberToCloseDoor(world, entity, blockPos2);
				}
			}
			OpenDoorsTask.pathToDoor(world, entity, pathNode, pathNode2);
			ci.cancel();
		}
	}
}
