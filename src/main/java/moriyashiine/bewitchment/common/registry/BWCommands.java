package moriyashiine.bewitchment.common.registry;

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
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;

@SuppressWarnings("ConstantConditions")
public class BWCommands {
	public static void init(CommandDispatcher<ServerCommandSource> dispatcher, boolean dedicated) {
		if (dedicated) {
			ArgumentTypes.register("fortune", FortuneArgumentType.class, new ConstantArgumentSerializer<>(FortuneArgumentType::fortune));
			ArgumentTypes.register("curse", CurseArgumentType.class, new ConstantArgumentSerializer<>(CurseArgumentType::curse));
			ArgumentTypes.register("contract", ContractArgumentType.class, new ConstantArgumentSerializer<>(ContractArgumentType::contract));
			ArgumentTypes.register("transformation", TransformationArgumentType.class, new ConstantArgumentSerializer<>(TransformationArgumentType::transformation));
		}
		dispatcher.register(CommandManager.literal("fortune").requires(serverCommandSource -> serverCommandSource.hasPermissionLevel(3)).then(CommandManager.argument("target", EntityArgumentType.player()).then(CommandManager.literal("get").executes(context -> {
			PlayerEntity target = EntityArgumentType.getPlayer(context, "target");
			if (target instanceof FortuneAccessor) {
				Fortune.Instance instance = ((FortuneAccessor) target).getFortune();
				if (instance != null) {
					context.getSource().sendFeedback(new LiteralText(target.getEntityName() + " has the following fortune: " + BWRegistries.FORTUNES.getId(instance.fortune).toString()), true);
					return 1;
				}
				else {
					context.getSource().sendFeedback(new LiteralText(target.getEntityName() + " does not have any fortune"), true);
				}
			}
			return 0;
		})).then(CommandManager.literal("set").then(CommandManager.argument("fortune", FortuneArgumentType.fortune()).executes(context -> {
			PlayerEntity target = EntityArgumentType.getPlayer(context, "target");
			if (target instanceof FortuneAccessor) {
				Fortune fortune = FortuneArgumentType.getFortune(context, "fortune");
				((FortuneAccessor) target).setFortune(new Fortune.Instance(fortune, target.world.random.nextInt(120000)));
				context.getSource().sendFeedback(new LiteralText("Set " + target.getEntityName() + "'s fortune to " + BWRegistries.FORTUNES.getId(fortune).toString()), true);
				return 1;
			}
			return 0;
		}))).then(CommandManager.literal("remove").executes(context -> {
			PlayerEntity target = EntityArgumentType.getPlayer(context, "target");
			if (target instanceof FortuneAccessor) {
				((FortuneAccessor) target).setFortune(null);
				context.getSource().sendFeedback(new LiteralText("Removed fortune from " + target.getEntityName()), true);
				return 1;
			}
			return 0;
		}))));
		dispatcher.register(CommandManager.literal("curse").requires(serverCommandSource -> serverCommandSource.hasPermissionLevel(3)).then(CommandManager.argument("target", EntityArgumentType.entity()).then(CommandManager.literal("get").executes(context -> {
			Entity target = EntityArgumentType.getEntity(context, "target");
			if (target instanceof CurseAccessor) {
				CurseAccessor curseAccessor = (CurseAccessor) target;
				if (!curseAccessor.getCurses().isEmpty()) {
					StringBuilder curses = new StringBuilder();
					for (Curse.Instance instance : curseAccessor.getCurses()) {
						curses.append(BWRegistries.CURSES.getId(instance.curse).toString()).append(", ");
					}
					context.getSource().sendFeedback(new LiteralText(target.getEntityName() + " has the following curses: " + curses.delete(curses.lastIndexOf(","), curses.capacity())), true);
					return 1;
				}
				else {
					context.getSource().sendFeedback(new LiteralText(target.getEntityName() + " does not have any curses"), true);
				}
			}
			return 0;
		}).then(CommandManager.argument("curse", CurseArgumentType.curse()).executes(context -> {
			Entity target = EntityArgumentType.getEntity(context, "target");
			if (target instanceof CurseAccessor) {
				CurseAccessor curseAccessor = (CurseAccessor) target;
				Curse curse = CurseArgumentType.getCurse(context, "curse");
				String curseName = BWRegistries.CURSES.getId(curse).toString();
				if (curseAccessor.hasCurse(curse)) {
					int daysLeft = Math.round(curseAccessor.getCurses().stream().filter(instance -> instance.curse == curse).findFirst().orElse(null).duration / 24000f);
					context.getSource().sendFeedback(new LiteralText(target.getEntityName() + " has curse " + curseName + " for " + daysLeft + " more " + (daysLeft == 1 ? "day" : "days")), true);
					return 1;
				}
				else {
					context.getSource().sendFeedback(new LiteralText(target.getEntityName() + " does not have curse " + curseName), true);
				}
			}
			return 0;
		}))).then(CommandManager.literal("add").then(CommandManager.argument("curse", CurseArgumentType.curse()).executes(context -> {
			Entity target = EntityArgumentType.getEntity(context, "target");
			if (target instanceof CurseAccessor) {
				Curse curse = CurseArgumentType.getCurse(context, "curse");
				((CurseAccessor) target).addCurse(new Curse.Instance(curse, 168000));
				context.getSource().sendFeedback(new LiteralText("Added curse " + BWRegistries.CURSES.getId(curse).toString() + " to " + target.getEntityName() + " for 7 days"), true);
				return 1;
			}
			return 0;
		}).then(CommandManager.argument("days", IntegerArgumentType.integer(0, 365)).executes(context -> {
			Entity target = EntityArgumentType.getEntity(context, "target");
			if (target instanceof CurseAccessor) {
				Curse curse = CurseArgumentType.getCurse(context, "curse");
				int days = IntegerArgumentType.getInteger(context, "days");
				((CurseAccessor) target).addCurse(new Curse.Instance(curse, days * 24000));
				context.getSource().sendFeedback(new LiteralText("Added curse " + BWRegistries.CURSES.getId(curse).toString() + " to " + target.getEntityName() + " for " + days + (days == 1 ? " day" : " days")), true);
				return 1;
			}
			return 0;
		})))).then(CommandManager.literal("remove").then(CommandManager.argument("curse", CurseArgumentType.curse()).executes(context -> {
			Entity target = EntityArgumentType.getEntity(context, "target");
			if (target instanceof CurseAccessor) {
				CurseAccessor curseAccessor = (CurseAccessor) target;
				Curse curse = CurseArgumentType.getCurse(context, "curse");
				String curseName = BWRegistries.CURSES.getId(curse).toString();
				if (curseAccessor.hasCurse(curse)) {
					curseAccessor.removeCurse(curse);
					context.getSource().sendFeedback(new LiteralText("Removed curse " + curseName + " from " + target.getEntityName()), true);
					return 1;
				}
				else {
					context.getSource().sendFeedback(new LiteralText("Could not find curse " + curseName + " on " + target.getEntityName()), true);
				}
			}
			return 0;
		}))).then(CommandManager.literal("clear").executes(context -> {
			Entity target = EntityArgumentType.getEntity(context, "target");
			if (target instanceof CurseAccessor) {
				((CurseAccessor) target).getCurses().clear();
				context.getSource().sendFeedback(new LiteralText("Removed all curses from " + target.getEntityName()), true);
				return 1;
			}
			return 0;
		}))));
		dispatcher.register(CommandManager.literal("contract").requires(serverCommandSource -> serverCommandSource.hasPermissionLevel(3)).then(CommandManager.argument("target", EntityArgumentType.entity()).then(CommandManager.literal("get").executes(context -> {
			Entity target = EntityArgumentType.getEntity(context, "target");
			if (target instanceof ContractAccessor) {
				ContractAccessor contractAccessor = (ContractAccessor) target;
				if (!contractAccessor.getContracts().isEmpty()) {
					StringBuilder contracts = new StringBuilder();
					for (Contract.Instance instance : contractAccessor.getContracts()) {
						contracts.append(BWRegistries.CONTRACTS.getId(instance.contract).toString()).append(", ");
					}
					context.getSource().sendFeedback(new LiteralText(target.getEntityName() + " has the following contracts: " + contracts.delete(contracts.lastIndexOf(","), contracts.capacity())), true);
					return 1;
				}
				else {
					context.getSource().sendFeedback(new LiteralText(target.getEntityName() + " does not have any contracts"), true);
				}
			}
			return 0;
		}).then(CommandManager.argument("contract", ContractArgumentType.contract()).executes(context -> {
			Entity target = EntityArgumentType.getEntity(context, "target");
			if (target instanceof ContractAccessor) {
				ContractAccessor contractAccessor = (ContractAccessor) target;
				Contract contract = ContractArgumentType.getContract(context, "contract");
				String contractName = BWRegistries.CONTRACTS.getId(contract).toString();
				if (contractAccessor.hasContract(contract)) {
					int daysLeft = Math.round(contractAccessor.getContracts().stream().filter(instance -> instance.contract == contract).findFirst().orElse(null).duration / 24000f);
					context.getSource().sendFeedback(new LiteralText(target.getEntityName() + " has contract " + contractName + " for " + daysLeft + " more " + (daysLeft == 1 ? "day" : "days")), true);
					return 1;
				}
				else {
					context.getSource().sendFeedback(new LiteralText(target.getEntityName() + " does not have contract " + contractName), true);
				}
			}
			return 0;
		}))).then(CommandManager.literal("add").then(CommandManager.argument("contract", ContractArgumentType.contract()).executes(context -> {
			Entity target = EntityArgumentType.getEntity(context, "target");
			if (target instanceof ContractAccessor) {
				Contract contract = ContractArgumentType.getContract(context, "contract");
				((ContractAccessor) target).addContract(new Contract.Instance(contract, 168000));
				context.getSource().sendFeedback(new LiteralText("Added contract " + BWRegistries.CONTRACTS.getId(contract).toString() + " to " + target.getEntityName() + " for 7 days"), true);
				return 1;
			}
			return 0;
		}).then(CommandManager.argument("days", IntegerArgumentType.integer(0, 365)).executes(context -> {
			Entity target = EntityArgumentType.getEntity(context, "target");
			if (target instanceof ContractAccessor) {
				Contract contract = ContractArgumentType.getContract(context, "contract");
				int days = IntegerArgumentType.getInteger(context, "days");
				((ContractAccessor) target).addContract(new Contract.Instance(contract, days * 24000));
				context.getSource().sendFeedback(new LiteralText("Added contract " + BWRegistries.CONTRACTS.getId(contract).toString() + " to " + target.getEntityName() + " for " + days + (days == 1 ? " day" : " days")), true);
				return 1;
			}
			return 0;
		})))).then(CommandManager.literal("remove").then(CommandManager.argument("contract", ContractArgumentType.contract()).executes(context -> {
			Entity target = EntityArgumentType.getEntity(context, "target");
			if (target instanceof ContractAccessor) {
				ContractAccessor contractAccessor = (ContractAccessor) target;
				Contract contract = ContractArgumentType.getContract(context, "contract");
				String contractName = BWRegistries.CONTRACTS.getId(contract).toString();
				if (contractAccessor.hasContract(contract)) {
					contractAccessor.removeContract(contract);
					context.getSource().sendFeedback(new LiteralText("Removed contract " + contractName + " from " + target.getEntityName()), true);
					return 1;
				}
				else {
					context.getSource().sendFeedback(new LiteralText("Could not find contract " + contractName + " on " + target.getEntityName()), true);
				}
			}
			return 0;
		}))).then(CommandManager.literal("clear").executes(context -> {
			Entity target = EntityArgumentType.getEntity(context, "target");
			if (target instanceof ContractAccessor) {
				((ContractAccessor) target).getContracts().clear();
				context.getSource().sendFeedback(new LiteralText("Removed all contracts from " + target.getEntityName()), true);
				return 1;
			}
			return 0;
		}))));
		dispatcher.register(CommandManager.literal("transformation").requires(serverCommandSource -> serverCommandSource.hasPermissionLevel(3)).then(CommandManager.argument("target", EntityArgumentType.player()).then(CommandManager.literal("get").executes(context -> {
			PlayerEntity target = EntityArgumentType.getPlayer(context, "target");
			if (target instanceof TransformationAccessor) {
				context.getSource().sendFeedback(new LiteralText(target.getEntityName() + " has the following transformation: " + BWRegistries.TRANSFORMATIONS.getId(((TransformationAccessor) target).getTransformation()).toString()), true);
				return 1;
			}
			return 0;
		})).then(CommandManager.literal("set").then(CommandManager.argument("transformation", TransformationArgumentType.transformation()).executes(context -> {
			PlayerEntity target = EntityArgumentType.getPlayer(context, "target");
			if (target instanceof TransformationAccessor) {
				Transformation transformation = TransformationArgumentType.getTransformation(context, "transformation");
				if (((TransformationAccessor) target).getAlternateForm()) {
					TransformationAbilityPacket.useAbility(target, true);
				}
				((TransformationAccessor) target).getTransformation().onRemoved(target);
				((TransformationAccessor) target).setTransformation(transformation);
				transformation.onAdded(target);
				context.getSource().sendFeedback(new LiteralText("Set " + target.getEntityName() + "'s transformation to " + BWRegistries.TRANSFORMATIONS.getId(transformation).toString()), true);
				return 1;
			}
			return 0;
		})))));
	}
	
	private static class FortuneArgumentType implements ArgumentType<Fortune> {
		public static final DynamicCommandExceptionType INVALID_FORTUNE_EXCEPTION = new DynamicCommandExceptionType((object) -> new TranslatableText("fortune.fortuneNotFound", object));
		
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
	
	private static class CurseArgumentType implements ArgumentType<Curse> {
		public static final DynamicCommandExceptionType INVALID_CURSE_EXCEPTION = new DynamicCommandExceptionType((object) -> new TranslatableText("curse.curseNotFound", object));
		
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
	
	private static class ContractArgumentType implements ArgumentType<Contract> {
		public static final DynamicCommandExceptionType INVALID_CONTRACT_EXCEPTION = new DynamicCommandExceptionType((object) -> new TranslatableText("contract.contractNotFound", object));
		
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
	
	private static class TransformationArgumentType implements ArgumentType<Transformation> {
		public static final DynamicCommandExceptionType INVALID_TRANSFORMATION_EXCEPTION = new DynamicCommandExceptionType((object) -> new TranslatableText("transformation.transformationNotFound", object));
		
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
}
