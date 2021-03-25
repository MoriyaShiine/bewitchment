package moriyashiine.bewitchment.client.integration.patchouli;

import moriyashiine.bewitchment.common.Bewitchment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import vazkii.patchouli.api.IComponentRenderContext;
import vazkii.patchouli.api.ICustomComponent;
import vazkii.patchouli.api.IVariable;

import java.util.function.UnaryOperator;

public class RitualCircleComponent implements ICustomComponent {
	transient int x, y;
	final transient Identifier[] circles = new Identifier[2];
	IVariable inner;
	IVariable outer;
	
	@Override
	public void build(int componentX, int componentY, int pageNum) {
		this.x = componentX;
		this.y = componentY;
	}
	
	@Override
	public void render(MatrixStack ms, IComponentRenderContext context, float pticks, int mouseX, int mouseY) {
		TextureManager textureManager = MinecraftClient.getInstance().getTextureManager();
		ms.push();
		ms.translate(x, y, -1);
		ms.scale(4, 4, 1);
		if (circles[1] != null) {
			textureManager.bindTexture(circles[1]);
			DrawableHelper.drawTexture(ms, 0, 0, 0, 0, 16, 16, 16, 16);
		}
		ms.translate(4, 4, 1);
		textureManager.bindTexture(circles[0]);
		DrawableHelper.drawTexture(ms, 0, 0, 4, 4, 7, 7, 16, 16);
		ms.pop();
	}
	
	@Override
	public void onVariablesAvailable(UnaryOperator<IVariable> lookup) {
		circles[0] = new Identifier(Bewitchment.MODID, "textures/gui/chalk/" + lookup.apply(inner).asString() + ".png");
		if (!lookup.apply(outer).asString().isEmpty()) {
			circles[1] = new Identifier(Bewitchment.MODID, "textures/gui/chalk/" + lookup.apply(outer).asString() + ".png");
		}
	}
}
