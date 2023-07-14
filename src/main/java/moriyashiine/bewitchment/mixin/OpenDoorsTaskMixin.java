/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.mixin;

import com.mojang.datafixers.kinds.OptionalBox;
import moriyashiine.bewitchment.common.block.util.interfaces.SpecialDoor;
import net.minecraft.block.BlockState;
import net.minecraft.block.DoorBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.MemoryQueryResult;
import net.minecraft.entity.ai.brain.task.OpenDoorsTask;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.entity.ai.brain.task.TaskTriggerer;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.entity.ai.pathing.PathNode;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.world.World;
import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.commons.lang3.mutable.MutableObject;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.Optional;
import java.util.Set;

@Mixin(OpenDoorsTask.class)
public abstract class OpenDoorsTaskMixin {
	@Shadow
	private static Optional<Set<GlobalPos>> storePos(MemoryQueryResult<OptionalBox.Mu, Set<GlobalPos>> queryResult, Optional<Set<GlobalPos>> doors, ServerWorld world, BlockPos pos) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @author MoriyaShiine
	 * @reason help
	 */
	@Overwrite
	public static Task<LivingEntity> create() {
		MutableObject<PathNode> mutableNode = new MutableObject<>(null);
		MutableInt mutableInt = new MutableInt(0);
		return TaskTriggerer.task(context -> context.group(context.queryMemoryValue(MemoryModuleType.PATH), context.queryMemoryOptional(MemoryModuleType.DOORS_TO_CLOSE), context.queryMemoryOptional(MemoryModuleType.MOBS)).apply(context, (path, doorsToClose, mobs) -> (world, entity, time) -> {
			Optional<Set<GlobalPos>> optional = context.getOptionalValue(doorsToClose);
			Path newPath = context.getValue(path);
			if (newPath.isStart() || newPath.isFinished()) {
				return false;
			}
			if (mutableNode.getValue() == newPath.getCurrentNode()) {
				mutableInt.setValue(20);
			} else if (mutableInt.decrementAndGet() > 0) {
				return false;
			}
			PathNode lastNode = newPath.getLastNode();
			PathNode currentNode = newPath.getCurrentNode();
			mutableNode.setValue(currentNode);
			BlockPos blockPos = lastNode.getBlockPos();
			BlockState blockState = world.getBlockState(blockPos);
			boolean failed = failed(world, entity, blockState, blockPos);
			if (blockState.isIn(BlockTags.WOODEN_DOORS, foundState -> foundState.getBlock() instanceof DoorBlock)) {
				DoorBlock doorBlock = (DoorBlock) blockState.getBlock();
				if (!doorBlock.isOpen(blockState) && !failed) {
					doorBlock.setOpen(entity, world, blockState, blockPos, true);
				}
				optional = storePos(doorsToClose, optional, world, blockPos);
			}
			blockPos = currentNode.getBlockPos();
			blockState = world.getBlockState(blockPos);
			if (blockState.isIn(BlockTags.WOODEN_DOORS, foundState -> foundState.getBlock() instanceof DoorBlock) && blockState.getBlock() instanceof DoorBlock doorBlock && !doorBlock.isOpen(blockState)) {
				if (!failed) {
					doorBlock.setOpen(entity, world, blockState, blockPos, true);
					optional = storePos(doorsToClose, optional, world, blockPos);
				}
			}
			optional.ifPresent(doors -> OpenDoorsTask.pathToDoor(world, entity, lastNode, currentNode, doors, context.getOptionalValue(mobs)));
			return true;
		}));
	}

	@Unique
	private static boolean failed(World world, LivingEntity entity, BlockState blockState, BlockPos blockPos) {
		return blockState.getBlock() instanceof SpecialDoor specialDoor && specialDoor.onSpecialUse(blockState, world, blockPos, entity, Hand.MAIN_HAND) == ActionResult.FAIL;
	}
}
