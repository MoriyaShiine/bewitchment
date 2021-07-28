package moriyashiine.bewitchment.common.registry;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import moriyashiine.bewitchment.api.interfaces.entity.ContractAccessor;
import moriyashiine.bewitchment.api.interfaces.entity.CurseAccessor;
import moriyashiine.bewitchment.api.interfaces.entity.FortuneAccessor;
import moriyashiine.bewitchment.api.interfaces.entity.TransformationAccessor;
import moriyashiine.bewitchment.api.registry.Contract;
import moriyashiine.bewitchment.api.registry.Curse;
import moriyashiine.bewitchment.api.registry.Fortune;
import moriyashiine.bewitchment.api.registry.Transformation;
import moriyashiine.bewitchment.common.network.packet.TransformationAbilityPacket;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.ArgumentTypes;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.serialize.ConstantArgumentSerializer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;

@SuppressWarnings("ConstantConditions")
public class BWCommands {
	public static void init(CommandDispatcher<ServerCommandSource> dispatcher, boolean dedicated) {
		dispatcher.register(CommandManager.literal("fortune").requires(serverCommandSource -> serverCommandSource.hasPermissionLevel(3)).then(CommandManager.literal("get").then(CommandManager.argument("player", EntityArgumentType.player()).executes(context -> {
			PlayerEntity player = EntityArgumentType.getPlayer(context, "player");
			Fortune.Instance fortune = ((FortuneAccessor) player).getFortune();
			if (fortune != null) {
				context.getSource().sendFeedback(new TranslatableText("commands.fortune.get", player.getEntityName(), BWRegistries.FORTUNES.getId(fortune.fortune)), false);
				return Command.SINGLE_SUCCESS;
			}
			throw FortuneArgumentType.GET_NO_FORTUNE_EXCEPTION.create(player.getEntityName());
		}))).then(CommandManager.literal("set").then(CommandManager.argument("player", EntityArgumentType.player()).then(CommandManager.argument("fortune", FortuneArgumentType.fortune()).executes(context -> {
			PlayerEntity player = EntityArgumentType.getPlayer(context, "player");
			Fortune fortune = FortuneArgumentType.getFortune(context, "fortune");
			((FortuneAccessor) player).setFortune(new Fortune.Instance(fortune, player.getRandom().nextInt(120000)));
			context.getSource().sendFeedback(new TranslatableText("commands.fortune.set", player.getEntityName(), BWRegistries.FORTUNES.getId(fortune)), true);
			return Command.SINGLE_SUCCESS;
		})))).then(CommandManager.literal("remove").then(CommandManager.argument("player", EntityArgumentType.player()).executes(context -> {
			PlayerEntity player = EntityArgumentType.getPlayer(context, "player");
			if (((FortuneAccessor) player).getFortune() != null) {
				((FortuneAccessor) player).setFortune(null);
				context.getSource().sendFeedback(new TranslatableText("commands.fortune.remove", player.getEntityName()), true);
				return Command.SINGLE_SUCCESS;
			}
			throw FortuneArgumentType.REMOVE_NO_FORTUNE_EXCEPTION.create(player.getEntityName());
		}))));
		dispatcher.register(CommandManager.literal("transformation").requires(serverCommandSource -> serverCommandSource.hasPermissionLevel(3)).then(CommandManager.literal("get").then(CommandManager.argument("player", EntityArgumentType.player()).executes(context -> {
			PlayerEntity player = EntityArgumentType.getPlayer(context, "player");
			context.getSource().sendFeedback(new TranslatableText("commands.transformation.get", player.getEntityName(), BWRegistries.TRANSFORMATIONS.getId(((TransformationAccessor) player).getTransformation())), false);
			return Command.SINGLE_SUCCESS;
		}))).then(CommandManager.literal("set").then(CommandManager.argument("player", EntityArgumentType.player()).then(CommandManager.argument("transformation", TransformationArgumentType.transformation()).executes(context -> {
			PlayerEntity player = EntityArgumentType.getPlayer(context, "player");
			TransformationAccessor transformationAccessor = (TransformationAccessor) player;
			Transformation transformation = TransformationArgumentType.getTransformation(context, "transformation");
			if (transformationAccessor.getAlternateForm()) {
				TransformationAbilityPacket.useAbility(player, true);
			}
			transformationAccessor.getTransformation().onRemoved(player);
			transformationAccessor.setTransformation(transformation);
			transformationAccessor.getTransformation().onAdded(player);
			context.getSource().sendFeedback(new TranslatableText("commands.transformation.set", player.getEntityName(), BWRegistries.TRANSFORMATIONS.getId(transformation)), true);
			return Command.SINGLE_SUCCESS;
		})))));
		dispatcher.register(CommandManager.literal("contract").requires(serverCommandSource -> serverCommandSource.hasPermissionLevel(3)).then(CommandManager.literal("get").then(CommandManager.argument("player", EntityArgumentType.player()).executes(context -> {
			PlayerEntity player = EntityArgumentType.getPlayer(context, "player");
			ContractAccessor contractAccessor = (ContractAccessor) player;
			if (!contractAccessor.getContracts().isEmpty()) {
				StringBuilder contracts = new StringBuilder();
				for (Contract.Instance instance : contractAccessor.getContracts()) {
					contracts.append(BWRegistries.CONTRACTS.getId(instance.contract).toString()).append(", ");
				}
				context.getSource().sendFeedback(new TranslatableText("commands.contract.get.multiple", player.getEntityName(), contracts.delete(contracts.lastIndexOf(","), contracts.capacity())), false);
				return Command.SINGLE_SUCCESS;
			}
			throw ContractArgumentType.GET_NO_CONTRACTS_EXCEPTION.create(player.getEntityName());
		}).then(CommandManager.argument("contract", ContractArgumentType.contract()).executes(context -> {
			PlayerEntity player = EntityArgumentType.getPlayer(context, "player");
			ContractAccessor contractAccessor = (ContractAccessor) player;
			Contract contract = ContractArgumentType.getContract(context, "contract");
			if (contractAccessor.hasContract(contract)) {
				int days = Math.round(contractAccessor.getContracts().stream().filter(instance -> instance.contract == contract).findFirst().orElse(null).duration / 24000f);
				context.getSource().sendFeedback(new TranslatableText("commands.contract.get.single", player.getEntityName(), days, BWRegistries.CONTRACTS.getId(contract)), false);
				return Command.SINGLE_SUCCESS;
			}
			throw ContractArgumentType.GET_NO_CONTRACT_EXCEPTION.create(player.getEntityName());
		})))).then(CommandManager.literal("add").then(CommandManager.argument("player", EntityArgumentType.player()).then(CommandManager.argument("contract", ContractArgumentType.contract()).executes(context -> {
			PlayerEntity player = EntityArgumentType.getPlayer(context, "player");
			Contract contract = ContractArgumentType.getContract(context, "contract");
			((ContractAccessor) player).addContract(new Contract.Instance(contract, 168000, 0));
			context.getSource().sendFeedback(new TranslatableText("commands.contract.add", player.getEntityName(), 7, BWRegistries.CONTRACTS.getId(contract)), true);
			return Command.SINGLE_SUCCESS;
		}).then(CommandManager.argument("days", IntegerArgumentType.integer(1, 365)).executes(context -> {
			PlayerEntity player = EntityArgumentType.getPlayer(context, "player");
			Contract contract = ContractArgumentType.getContract(context, "contract");
			int days = IntegerArgumentType.getInteger(context, "days");
			((ContractAccessor) player).addContract(new Contract.Instance(contract, days * 24000, 0));
			context.getSource().sendFeedback(new TranslatableText("commands.contract.add", player.getEntityName(), days, BWRegistries.CONTRACTS.getId(contract)), true);
			return Command.SINGLE_SUCCESS;
		}))))).then(CommandManager.literal("remove").then(CommandManager.argument("player", EntityArgumentType.player()).then(CommandManager.argument("contract", ContractArgumentType.contract()).executes(context -> {
			PlayerEntity player = EntityArgumentType.getPlayer(context, "player");
			ContractAccessor contractAccessor = (ContractAccessor) player;
			Contract contract = ContractArgumentType.getContract(context, "contract");
			if (contractAccessor.hasContract(contract)) {
				contractAccessor.removeContract(contract);
				context.getSource().sendFeedback(new TranslatableText("commands.contract.remove", player.getEntityName(), BWRegistries.CONTRACTS.getId(contract)), true);
				return Command.SINGLE_SUCCESS;
			}
			throw ContractArgumentType.REMOVE_NO_CONTRACT_EXCEPTION.create(player.getEntityName());
		})))).then(CommandManager.literal("clear").then(CommandManager.argument("player", EntityArgumentType.player()).executes(context -> {
			PlayerEntity player = EntityArgumentType.getPlayer(context, "player");
			ContractAccessor contractAccessor = (ContractAccessor) player;
			if (!contractAccessor.getContracts().isEmpty()) {
				contractAccessor.getContracts().clear();
				context.getSource().sendFeedback(new TranslatableText("commands.contract.clear", player.getEntityName()), true);
				return Command.SINGLE_SUCCESS;
			}
			throw ContractArgumentType.CLEAR_NO_CONTRACTS_EXCPETION.create(player.getEntityName());
		}))));
		dispatcher.register(CommandManager.literal("curse").requires(serverCommandSource -> serverCommandSource.hasPermissionLevel(3)).then(CommandManager.literal("get").then(CommandManager.argument("entity", EntityArgumentType.entity()).executes(context -> {
			Entity entity = EntityArgumentType.getEntity(context, "entity");
			if (entity instanceof CurseAccessor curseAccessor) {
				if (!curseAccessor.getCurses().isEmpty()) {
					StringBuilder curses = new StringBuilder();
					for (Curse.Instance instance : curseAccessor.getCurses()) {
						curses.append(BWRegistries.CURSES.getId(instance.curse).toString()).append(", ");
					}
					context.getSource().sendFeedback(new TranslatableText("commands.curse.get.multiple", entity.getEntityName(), curses.delete(curses.lastIndexOf(","), curses.capacity())), false);
					return Command.SINGLE_SUCCESS;
				}
				throw CurseArgumentType.GET_NO_CURSES_EXCEPTION.create(entity.getEntityName());
			}
			return 0;
		}).then(CommandManager.argument("curse", CurseArgumentType.curse()).executes(context -> {
			Entity entity = EntityArgumentType.getEntity(context, "entity");
			if (entity instanceof CurseAccessor curseAccessor) {
				Curse curse = CurseArgumentType.getCurse(context, "curse");
				if (curseAccessor.hasCurse(curse)) {
					int days = Math.round(curseAccessor.getCurses().stream().filter(instance -> instance.curse == curse).findFirst().orElse(null).duration / 24000f);
					context.getSource().sendFeedback(new TranslatableText("commands.curse.get.single", entity.getEntityName(), days, BWRegistries.CURSES.getId(curse)), false);
					return Command.SINGLE_SUCCESS;
				}
				throw CurseArgumentType.GET_NO_CURSE_EXCEPTION.create(entity.getEntityName());
			}
			return 0;
		})))).then(CommandManager.literal("add").then(CommandManager.argument("entity", EntityArgumentType.entity()).then(CommandManager.argument("curse", CurseArgumentType.curse()).executes(context -> {
			Entity entity = EntityArgumentType.getEntity(context, "entity");
			if (entity instanceof CurseAccessor) {
				Curse curse = CurseArgumentType.getCurse(context, "curse");
				((CurseAccessor) entity).addCurse(new Curse.Instance(curse, 168000));
				context.getSource().sendFeedback(new TranslatableText("commands.curse.add", entity.getEntityName(), 7, BWRegistries.CURSES.getId(curse)), true);
				return Command.SINGLE_SUCCESS;
			}
			return 0;
		}).then(CommandManager.argument("days", IntegerArgumentType.integer(1, 365)).executes(context -> {
			Entity entity = EntityArgumentType.getEntity(context, "entity");
			if (entity instanceof CurseAccessor) {
				Curse curse = CurseArgumentType.getCurse(context, "curse");
				int days = IntegerArgumentType.getInteger(context, "days");
				((CurseAccessor) entity).addCurse(new Curse.Instance(curse, days * 24000));
				context.getSource().sendFeedback(new TranslatableText("commands.curse.add", entity.getEntityName(), days, BWRegistries.CURSES.getId(curse)), true);
				return Command.SINGLE_SUCCESS;
			}
			return 0;
		}))))).then(CommandManager.literal("remove").then(CommandManager.argument("entity", EntityArgumentType.entity()).then(CommandManager.argument("curse", CurseArgumentType.curse()).executes(context -> {
			Entity entity = EntityArgumentType.getEntity(context, "entity");
			if (entity instanceof CurseAccessor curseAccessor) {
				Curse curse = CurseArgumentType.getCurse(context, "curse");
				if (curseAccessor.hasCurse(curse)) {
					curseAccessor.removeCurse(curse);
					context.getSource().sendFeedback(new TranslatableText("commands.curse.remove", entity.getEntityName(), BWRegistries.CURSES.getId(curse)), true);
					return Command.SINGLE_SUCCESS;
				}
				throw CurseArgumentType.REMOVE_NO_CURSE_EXCEPTION.create(entity.getEntityName());
			}
			return 0;
		})))).then(CommandManager.literal("clear").then(CommandManager.argument("entity", EntityArgumentType.entity()).executes(context -> {
			Entity entity = EntityArgumentType.getEntity(context, "entity");
			if (entity instanceof CurseAccessor curseAccessor) {
				if (!curseAccessor.getCurses().isEmpty()) {
					curseAccessor.getCurses().clear();
					context.getSource().sendFeedback(new TranslatableText("commands.curse.clear", entity.getEntityName()), true);
					return Command.SINGLE_SUCCESS;
				}
				throw CurseArgumentType.CLEAR_NO_CURSES_EXCPETION.create(entity.getEntityName());
			}
			return 0;
		}))));
	}
	
	public static void registerArgumentTypes() {
		ArgumentTypes.register("bewitchment:fortune", FortuneArgumentType.class, new ConstantArgumentSerializer<>(FortuneArgumentType::fortune));
		ArgumentTypes.register("bewitchment:transformation", TransformationArgumentType.class, new ConstantArgumentSerializer<>(TransformationArgumentType::transformation));
		ArgumentTypes.register("bewitchment:contract", ContractArgumentType.class, new ConstantArgumentSerializer<>(ContractArgumentType::contract));
		ArgumentTypes.register("bewitchment:curse", CurseArgumentType.class, new ConstantArgumentSerializer<>(CurseArgumentType::curse));
	}
	
	private static class FortuneArgumentType implements ArgumentType<Fortune> {
		public static final DynamicCommandExceptionType INVALID_FORTUNE_EXCEPTION = new DynamicCommandExceptionType(object -> new TranslatableText("commands.fortune.not_found", object));
		public static final DynamicCommandExceptionType GET_NO_FORTUNE_EXCEPTION = new DynamicCommandExceptionType(object -> new TranslatableText("commands.fortune.get.no_fortune", object));
		public static final DynamicCommandExceptionType REMOVE_NO_FORTUNE_EXCEPTION = new DynamicCommandExceptionType(object -> new TranslatableText("commands.fortune.remove.no_fortune", object));
		
		public static FortuneArgumentType fortune() {
			return new FortuneArgumentType();
		}
		
		public static Fortune getFortune(CommandContext<ServerCommandSource> commandContext, String string) {
			return commandContext.getArgument(string, Fortune.class);
		}
		
		@Override
		public Fortune parse(StringReader reader) throws CommandSyntaxException {
			Identifier identifier = Identifier.fromCommandInput(reader);
			return BWRegistries.FORTUNES.getOrEmpty(identifier).orElseThrow(() -> INVALID_FORTUNE_EXCEPTION.create(identifier));
		}
		
		@Override
		public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
			return CommandSource.suggestIdentifiers(BWRegistries.FORTUNES.getIds(), builder);
		}
	}
	
	private static class TransformationArgumentType implements ArgumentType<Transformation> {
		public static final DynamicCommandExceptionType INVALID_TRANSFORMATION_EXCEPTION = new DynamicCommandExceptionType(object -> new TranslatableText("commands.transformation.not_found", object));
		
		public static TransformationArgumentType transformation() {
			return new TransformationArgumentType();
		}
		
		public static Transformation getTransformation(CommandContext<ServerCommandSource> commandContext, String string) {
			return commandContext.getArgument(string, Transformation.class);
		}
		
		@Override
		public Transformation parse(StringReader reader) throws CommandSyntaxException {
			Identifier identifier = Identifier.fromCommandInput(reader);
			return BWRegistries.TRANSFORMATIONS.getOrEmpty(identifier).orElseThrow(() -> INVALID_TRANSFORMATION_EXCEPTION.create(identifier));
		}
		
		@Override
		public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
			return CommandSource.suggestIdentifiers(BWRegistries.TRANSFORMATIONS.getIds(), builder);
		}
	}
	
	private static class ContractArgumentType implements ArgumentType<Contract> {
		public static final DynamicCommandExceptionType INVALID_CONTRACT_EXCEPTION = new DynamicCommandExceptionType(object -> new TranslatableText("commands.contract.not_found", object));
		public static final DynamicCommandExceptionType GET_NO_CONTRACTS_EXCEPTION = new DynamicCommandExceptionType(object -> new TranslatableText("commands.contract.get.no_contracts", object));
		public static final DynamicCommandExceptionType GET_NO_CONTRACT_EXCEPTION = new DynamicCommandExceptionType(object -> new TranslatableText("commands.contract.get.no_contract", object));
		public static final DynamicCommandExceptionType REMOVE_NO_CONTRACT_EXCEPTION = new DynamicCommandExceptionType(object -> new TranslatableText("commands.contract.remove.no_contract", object));
		public static final DynamicCommandExceptionType CLEAR_NO_CONTRACTS_EXCPETION = new DynamicCommandExceptionType(object -> new TranslatableText("commands.contract.clear.no_contracts", object));
		
		public static ContractArgumentType contract() {
			return new ContractArgumentType();
		}
		
		public static Contract getContract(CommandContext<ServerCommandSource> commandContext, String string) {
			return commandContext.getArgument(string, Contract.class);
		}
		
		@Override
		public Contract parse(StringReader reader) throws CommandSyntaxException {
			Identifier identifier = Identifier.fromCommandInput(reader);
			return BWRegistries.CONTRACTS.getOrEmpty(identifier).orElseThrow(() -> INVALID_CONTRACT_EXCEPTION.create(identifier));
		}
		
		@Override
		public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
			return CommandSource.suggestIdentifiers(BWRegistries.CONTRACTS.getIds(), builder);
		}
	}
	
	private static class CurseArgumentType implements ArgumentType<Curse> {
		public static final DynamicCommandExceptionType INVALID_CURSE_EXCEPTION = new DynamicCommandExceptionType(object -> new TranslatableText("commands.curse.not_found", object));
		public static final DynamicCommandExceptionType GET_NO_CURSES_EXCEPTION = new DynamicCommandExceptionType(object -> new TranslatableText("commands.curse.get.no_curses", object));
		public static final DynamicCommandExceptionType GET_NO_CURSE_EXCEPTION = new DynamicCommandExceptionType(object -> new TranslatableText("commands.curse.get.no_curse", object));
		public static final DynamicCommandExceptionType REMOVE_NO_CURSE_EXCEPTION = new DynamicCommandExceptionType(object -> new TranslatableText("commands.curse.remove.no_curse", object));
		public static final DynamicCommandExceptionType CLEAR_NO_CURSES_EXCPETION = new DynamicCommandExceptionType(object -> new TranslatableText("commands.curse.clear.no_curses", object));
		
		public static CurseArgumentType curse() {
			return new CurseArgumentType();
		}
		
		public static Curse getCurse(CommandContext<ServerCommandSource> commandContext, String string) {
			return commandContext.getArgument(string, Curse.class);
		}
		
		@Override
		public Curse parse(StringReader reader) throws CommandSyntaxException {
			Identifier identifier = Identifier.fromCommandInput(reader);
			return BWRegistries.CURSES.getOrEmpty(identifier).orElseThrow(() -> INVALID_CURSE_EXCEPTION.create(identifier));
		}
		
		@Override
		public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
			return CommandSource.suggestIdentifiers(BWRegistries.CURSES.getIds(), builder);
		}
	}
}
