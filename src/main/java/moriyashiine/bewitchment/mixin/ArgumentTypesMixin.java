package moriyashiine.bewitchment.mixin;

import com.mojang.brigadier.arguments.ArgumentType;
import moriyashiine.bewitchment.common.registry.BWCommands;
import net.minecraft.command.argument.ArgumentTypes;
import net.minecraft.command.argument.serialize.ArgumentSerializer;
import net.minecraft.command.argument.serialize.ConstantArgumentSerializer;
import net.minecraft.registry.Registry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ArgumentTypes.class)
public abstract class ArgumentTypesMixin {
	@Shadow
	private static <A extends ArgumentType<?>, T extends ArgumentSerializer.ArgumentTypeProperties<A>> ArgumentSerializer<A, T> register(Registry<ArgumentSerializer<?, ?>> registry, String id, Class<? extends A> clazz, ArgumentSerializer<A, T> serializer) {
		throw new UnsupportedOperationException();
	}

	@Inject(method = "register(Lnet/minecraft/registry/Registry;)Lnet/minecraft/command/argument/serialize/ArgumentSerializer;", at = @At("RETURN"))
	private static void bewitchemnt$registerArgumentTypes(Registry<ArgumentSerializer<?, ?>> registry, CallbackInfoReturnable<ArgumentSerializer<?, ?>> cir) {
		register(registry, "bewitchment:fortune", BWCommands.FortuneArgumentType.class, ConstantArgumentSerializer.of(BWCommands.FortuneArgumentType::fortune));
		register(registry, "bewitchment:transformation", BWCommands.TransformationArgumentType.class, ConstantArgumentSerializer.of(BWCommands.TransformationArgumentType::transformation));
		register(registry, "bewitchment:contract", BWCommands.ContractArgumentType.class, ConstantArgumentSerializer.of(BWCommands.ContractArgumentType::contract));
		register(registry, "bewitchment:curse", BWCommands.CurseArgumentType.class, ConstantArgumentSerializer.of(BWCommands.CurseArgumentType::curse));
	}
}
