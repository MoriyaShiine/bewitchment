package moriyashiine.bewitchment.mixin;

import com.mojang.serialization.Codec;
import net.minecraft.world.gen.tree.TreeDecorator;
import net.minecraft.world.gen.tree.TreeDecoratorType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(TreeDecoratorType.class)
public interface TreeDecoratorTypeAccessor {
	@Invoker
	static <T extends TreeDecorator> TreeDecoratorType<T> callRegister(String id, Codec<T> codec)
	{
		throw new UnsupportedOperationException();
	}
}
