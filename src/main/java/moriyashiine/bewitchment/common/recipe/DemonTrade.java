package moriyashiine.bewitchment.common.recipe;

import com.google.common.collect.ImmutableMap;
import com.google.gson.*;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootGsons;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.function.LootFunction;
import net.minecraft.resource.JsonDataLoader;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.registry.Registry;
import net.minecraft.village.TradeOffer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class DemonTrade {
    public static final Map<Identifier, DemonTrade> TRADES = new HashMap<>();
    private final Function<LootContext, ItemStack> firstBuy;
    private final Function<LootContext, ItemStack> secondBuy;
    private final Function<LootContext, ItemStack> sell;
    private final int maxUses;

    public DemonTrade(Function<LootContext, ItemStack> firstBuy, Function<LootContext, ItemStack> secondBuy, Function<LootContext, ItemStack> sell, int maxUses) {
        this.firstBuy = firstBuy;
        this.secondBuy = secondBuy;
        this.sell = sell;
        this.maxUses = maxUses;
    }

    public TradeOffer generate(LootContext context) {
        return new TradeOffer(firstBuy.apply(context), secondBuy.apply(context), sell.apply(context), maxUses, 0, 1);
    }

    public static class Loader extends JsonDataLoader {
        private static final Logger LOGGER = LogManager.getLogger();
        private static final Gson GSON = LootGsons.getFunctionGsonBuilder().registerTypeAdapter(DemonTrade.class, new DemonTrade.Serializer()).create(); //its magic bois


        public Loader() {
            super(GSON, "bw_demon_trades");
        }

        @Override
        protected void apply(Map<Identifier, JsonElement> loader, ResourceManager manager, Profiler profiler) {
            ImmutableMap.Builder<Identifier, DemonTrade> builder = ImmutableMap.builder();
            loader.forEach((identifier, jsonElement) -> {
                try {
                    DemonTrade trade = GSON.fromJson(jsonElement, DemonTrade.class);
                    builder.put(identifier, trade);
                } catch (Exception exception) {
                    LOGGER.error("Couldn't parse demon trade table {}", identifier, exception);
                }
            });
            TRADES.putAll(builder.build());
        }
    }

    public static class Serializer implements JsonDeserializer<DemonTrade> {
        @Override
        public DemonTrade deserialize(JsonElement element, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject json = JsonHelper.asObject(element, "demon trade");
            return new DemonTrade(deserializeSupplier(context, JsonHelper.getObject(json, "first_buy")),
                    json.has("second_buy") ? deserializeSupplier(context, JsonHelper.getObject(json, "second_buy")) : (lootContext) -> ItemStack.EMPTY,
                    deserializeSupplier(context, JsonHelper.getObject(json, "sell")),
                    JsonHelper.getInt(json, "max_uses", 10));
        }

        //maybe also a serialization method?

        private static Function<LootContext, ItemStack> deserializeSupplier(JsonDeserializationContext context, JsonObject object) {
            Identifier id = new Identifier(JsonHelper.getString(object, "item"));
            ItemStack baseStack = new ItemStack(Registry.ITEM.getOrEmpty(id).orElseThrow(() -> new IllegalStateException("Item: " + id.toString() + " does not exist")));

            LootFunction[] lootFunctions = JsonHelper.deserialize(object, "functions", new LootFunction[0], context, LootFunction[].class);

            return (lootContext) -> {
                ItemStack stack = baseStack;
                for (LootFunction lootFunction : lootFunctions) {
                    stack = lootFunction.apply(baseStack, lootContext);
                }
                return stack;
            };
        }


    }
}
