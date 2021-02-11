package moriyashiine.bewitchment.client.renderer;

import moriyashiine.bewitchment.client.model.ContributorHornsModel;
import moriyashiine.bewitchment.common.Bewitchment;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;

public final class ContributorRenderer extends FeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {
    private static boolean init = false;
    private static Set<UUID> contributors;
    private static final ContributorHornsModel model = new ContributorHornsModel();
    private static final Identifier texture = new Identifier(Bewitchment.MODID, "textures/entity/armor/horns.png");

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
        if (contributors.contains(player.getGameProfile().getId())) {
            renderHorns(matrices, vertexConsumers, light);
        }
    }

    private void renderHorns(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        matrices.push();
        getContextModel().head.rotate(matrices);
        model.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntitySolid(texture)), light, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
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
