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
import moriyashiine.bewitchment.api.interfaces.ContractAccessor;
import moriyashiine.bewitchment.api.registry.Contract;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;

@SuppressWarnings("ConstantConditions")
public class BWCommands {
	public static void init(CommandDispatcher<ServerCommandSource> dispatcher, boolean dedicated) {
		dispatcher.register(CommandManager.literal("contract").requires(serverCommandSource -> serverCommandSource.hasPermissionLevel(3)).then(CommandManager.argument("target", EntityArgumentType.entity()).then(CommandManager.literal("get").then(CommandManager.argument("contract", ContractArgumentType.contract()).executes(context -> {
			Entity target = EntityArgumentType.getEntity(context, "target");
			if (target instanceof LivingEntity) {
				ContractAccessor contractAccessor = ContractAccessor.of((LivingEntity) target).orElse(null);
				if (contractAccessor != null) {
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
			}
			return 0;
		}))).then(CommandManager.literal("add").then(CommandManager.argument("contract", ContractArgumentType.contract()).executes(context -> {
			Entity target = EntityArgumentType.getEntity(context, "target");
			if (target instanceof LivingEntity) {
				ContractAccessor contractAccessor = ContractAccessor.of((LivingEntity) target).orElse(null);
				if (contractAccessor != null) {
					Contract contract = ContractArgumentType.getContract(context, "contract");
					contractAccessor.addContract(new Contract.Instance(contract, 168000));
					context.getSource().sendFeedback(new LiteralText("Added contract " + BWRegistries.CONTRACTS.getId(contract).toString() + " to " + target.getEntityName() + " for 7 days"), true);
					return 1;
				}
			}
			return 0;
		}).then(CommandManager.argument("days", IntegerArgumentType.integer(0, 365)).executes(context -> {
			Entity target = EntityArgumentType.getEntity(context, "target");
			if (target instanceof LivingEntity) {
				ContractAccessor contractAccessor = ContractAccessor.of((LivingEntity) target).orElse(null);
				if (contractAccessor != null) {
					Contract contract = ContractArgumentType.getContract(context, "contract");
					int days = IntegerArgumentType.getInteger(context, "days");
					contractAccessor.addContract(new Contract.Instance(contract, days * 24000));
					context.getSource().sendFeedback(new LiteralText("Added contract " + BWRegistries.CONTRACTS.getId(contract).toString() + " to " + target.getEntityName() + " for " + days + (days == 1 ? " day" : " days")), true);
					return 1;
				}
			}
			return 0;
		})))).then(CommandManager.literal("remove").then(CommandManager.argument("contract", ContractArgumentType.contract()).executes(context -> {
			Entity target = EntityArgumentType.getEntity(context, "target");
			if (target instanceof LivingEntity) {
				ContractAccessor contractAccessor = ContractAccessor.of((LivingEntity) target).orElse(null);
				if (contractAccessor != null) {
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
			}
			return 0;
		}))).then(CommandManager.literal("clear").executes(context -> {
			Entity target = EntityArgumentType.getEntity(context, "target");
			if (target instanceof LivingEntity) {
				ContractAccessor contractAccessor = ContractAccessor.of((LivingEntity) target).orElse(null);
				if (contractAccessor != null) {
					contractAccessor.getContracts().clear();
					context.getSource().sendFeedback(new LiteralText("Removed all contracts from " + target.getEntityName()), true);
					return 1;
				}
			}
			return 0;
		}))));
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
}
