package moriyashiine.bewitchment.common.world.generator.tree.decorator;

import com.mojang.serialization.Codec;
import moriyashiine.bewitchment.common.registry.BWObjects;
import moriyashiine.bewitchment.common.registry.BWWorldGenerators;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ModifiableWorld;
import net.minecraft.world.gen.tree.LeaveVineTreeDecorator;
import net.minecraft.world.gen.tree.TreeDecoratorType;

import java.util.Set;

public class LeaveSpanishMossTreeDecorator extends LeaveVineTreeDecorator {
	public static final LeaveSpanishMossTreeDecorator INSTANCE = new LeaveSpanishMossTreeDecorator();
	public static final Codec<LeaveSpanishMossTreeDecorator> CODEC = Codec.unit(() -> INSTANCE);
	
	@Override
	protected TreeDecoratorType<?> getType() {
		return BWWorldGenerators.LEAVE_SPANISH_MOSS;
	}
	
	@Override
	protected void placeVine(ModifiableWorld world, BlockPos pos, BooleanProperty directionProperty, Set<BlockPos> placedStates, BlockBox box) {
		setBlockStateAndEncompassPosition(world, pos, BWObjects.SPANISH_MOSS.getDefaultState().with(directionProperty, true), placedStates, box);
	}
}
