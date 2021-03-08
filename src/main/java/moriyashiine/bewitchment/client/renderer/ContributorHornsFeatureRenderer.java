package moriyashiine.bewitchment.client.renderer;

import moriyashiine.bewitchment.client.model.ContributorHornsModel;
import moriyashiine.bewitchment.common.Bewitchment;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.PlayerModelPart;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.apache.commons.io.IOUtils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.UUID;

public final class ContributorHornsFeatureRenderer extends FeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {
	private static final Identifier TEXTURE = new Identifier(Bewitchment.MODID, "textures/entity/armor/contributor_horns.png");
	private static final ContributorHornsModel MODEL = new ContributorHornsModel();
	private static final Set<UUID> CONTRIBUTORS = new HashSet<>();
	private static final String CONTRIBUTORS_URL = "https://raw.githubusercontent.com/MoriyaShiine/bewitchment/master/contributors.properties";
	
	private static boolean init = false;
	
	public ContributorHornsFeatureRenderer(FeatureRendererContext<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> context) {
		super(context);
	}
	
	@Override
	public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, AbstractClientPlayerEntity player, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
		if (!init) {
			Thread loader = new ContributorListLoaderThread();
			loader.start();
			init = true;
		}
		else if (!player.isInvisible() && player.isPartVisible(PlayerModelPart.HAT) && CONTRIBUTORS.contains(player.getUuid())) {
			matrices.push();
			getContextModel().head.rotate(matrices);
			MODEL.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntitySolid(TEXTURE)), light, OverlayTexture.DEFAULT_UV, 1, 1, 1, 1);
			matrices.pop();
		}
	}
	
	private static class ContributorListLoaderThread extends Thread {
		public ContributorListLoaderThread() {
			setName("Bewitchment Contributor List Loader Thread");
			setDaemon(true);
		}
		
		@Override
		public void run() {
			try (BufferedInputStream stream = IOUtils.buffer(new URL(CONTRIBUTORS_URL).openStream())) {
				Properties properties = new Properties();
				properties.load(stream);
				synchronized (CONTRIBUTORS) {
					CONTRIBUTORS.clear();
					for (String key : properties.stringPropertyNames()) {
						String value = properties.getProperty(key);
						UUID uuid;
						try {
							uuid = UUID.fromString(value);
							
						} catch (IllegalArgumentException ignored) {
							continue;
						}
						CONTRIBUTORS.add(uuid);
					}
				}
			} catch (IOException e) {
				System.out.println("Failed to load contributor list. Contributor horns will not be rendered.");
			}
		}
	}
}
