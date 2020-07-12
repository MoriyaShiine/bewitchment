package moriyashiine.bewitchment.common.registry;

import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.mixin.TreeDecoratorTypeAccessor;
import moriyashiine.bewitchment.common.world.generator.decorator.LeaveSpanishMossTreeDecorator;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.decorator.TreeDecorator;
import net.minecraft.world.gen.decorator.TreeDecoratorType;

import java.util.HashMap;
import java.util.Map;

public class BWTreeDecoratorTypes
{
	private static final Map<TreeDecoratorType<TreeDecorator>, Identifier> TREE_DECORATOR_TYPES = new HashMap<>();
	
	public static final TreeDecoratorType<TreeDecorator> LEAVE_SPANISH_MOSS = create("leave_spanish_moss", TreeDecoratorTypeAccessor.createTreeDecoratorType(LeaveSpanishMossTreeDecorator.CODEC));
	
	private static TreeDecoratorType<TreeDecorator> create(String name, TreeDecoratorType<TreeDecorator> decorator)
	{
		TREE_DECORATOR_TYPES.put(decorator, new Identifier(Bewitchment.MODID, name));
		return decorator;
	}
	
	public static void init()
	{
		TREE_DECORATOR_TYPES.keySet().forEach(decorator -> Registry.register(Registry.TREE_DECORATOR_TYPE, TREE_DECORATOR_TYPES.get(decorator), decorator));
	}
}