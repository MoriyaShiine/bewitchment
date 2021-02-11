package moriyashiine.bewitchment.client.renderer;

import moriyashiine.bewitchment.common.registry.BWObjects;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;

public final class ContributorRenderer extends FeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {
    private static boolean init = false;
    private static Set<UUID> contributors;

    public ContributorRenderer(FeatureRendererContext<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> context) {
        super(context);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, AbstractClientPlayerEntity player, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        if (!init) {
            Thread loader = new ContributorListLoaderThread();
            loader.start();
            init = true;
        }

        if (player.isInvisible()) {
            return;
        }

        // TODO: if wearing cape?
        ItemStack itemStack = new ItemStack(BWObjects.ATHAME);
        renderItem(matrices, vertexConsumers, itemStack);
    }

    private void renderItem(MatrixStack matrices, VertexConsumerProvider vertexConsumerProvider, ItemStack itemStack) {
        matrices.push();
        getContextModel().head.rotate(matrices);
        matrices.translate(0, -0.75, 0);
        matrices.scale(0.5f, -0.5f, -0.5f);
        MinecraftClient.getInstance().getItemRenderer().renderItem(itemStack, ModelTransformation.Mode.NONE, 0xF00F0, OverlayTexture.DEFAULT_UV, matrices, vertexConsumerProvider);
        matrices.pop();
    }

    public static void load(Properties prop) {
        contributors = new HashSet<>();
        for (String key : prop.stringPropertyNames()) {
            try {
                contributors.add(UUID.fromString(prop.getProperty(key)));
            } catch (IllegalArgumentException ignored) {
            }
        }
    }

    private static class ContributorListLoaderThread extends Thread {
        public ContributorListLoaderThread() {
            setName("Bewitchment Contributor List Loader Thread");
            setDaemon(true);
        }

        @Override
        public void run() {
            try {
                URL contributorList = new URL("https://raw.githubusercontent.com/MoriyaShiine/bewitchment/master/contributors.properties");
                BufferedReader in = new BufferedReader(new InputStreamReader(contributorList.openStream()));
                Properties prop = new Properties();
                prop.load(in);
                in.close();
                load(prop);
            } catch (IOException e) {
                System.out.println("Failed to load contributor list. Contributor horns will not be rendered.");
            }
        }
    }
}
