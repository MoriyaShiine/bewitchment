package moriyashiine.bewitchment.common.world.generator.decorator;

import com.mojang.serialization.Codec;
import moriyashiine.bewitchment.common.registry.BWObjects;
import moriyashiine.bewitchment.common.registry.BWTreeDecoratorTypes;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ModifiableWorld;
import net.minecraft.world.gen.decorator.LeaveVineTreeDecorator;
import net.minecraft.world.gen.decorator.TreeDecorator;
import net.minecraft.world.gen.decorator.TreeDecoratorType;

import java.util.Set;

public class LeaveSpanishMossTreeDecorator extends LeaveVineTreeDecorator {
	public static final TreeDecorator INSTANCE = new LeaveSpanishMossTreeDecorator();
	
	public static final Codec<TreeDecorator> CODEC = Codec.unit(INSTANCE);
	
	public LeaveSpanishMossTreeDecorator() {
		super();
	}
	
	@Override
	protected TreeDecoratorType<?> getType() {
		return BWTreeDecoratorTypes.LEAVE_SPANISH_MOSS;
	}
	
	@Override
	protected void placeVine(ModifiableWorld world, BlockPos pos, BooleanProperty directionProperty, Set<BlockPos> set, BlockBox box) {
		setBlockStateAndEncompassPosition(world, pos, BWObjects.spanish_moss.getDefaultState().with(directionProperty, true), set, box);
	}
}