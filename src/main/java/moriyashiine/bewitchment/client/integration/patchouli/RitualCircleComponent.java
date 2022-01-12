/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.bewitchment.client.integration.patchouli;

import com.mojang.blaze3d.systems.RenderSystem;
import moriyashiine.bewitchment.common.Bewitchment;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
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
	public void render(MatrixStack ms, @NotNull IComponentRenderContext context, float pticks, int mouseX, int mouseY) {
		ms.push();
		ms.translate(x, y, -1);
		ms.scale(4, 4, 1);
		if (circles[1] != null) {
			RenderSystem.setShaderTexture(0, circles[1]);
			DrawableHelper.drawTexture(ms, 0, 0, 0, 0, 16, 16, 16, 16);
		}
		ms.translate(4, 4, 1);
		RenderSystem.setShaderTexture(0, circles[0]);
		DrawableHelper.drawTexture(ms, 0, 0, 4, 4, 7, 7, 16, 16);
		ms.pop();
	}

	@Override
	public void onVariablesAvailable(UnaryOperator<IVariable> lookup) {
		circles[0] = new Identifier(Bewitchment.MODID, "textures/gui/patchouli/chalk/" + lookup.apply(inner).asString() + ".png");
		if (!lookup.apply(outer).asString().isEmpty()) {
			circles[1] = new Identifier(Bewitchment.MODID, "textures/gui/patchouli/chalk/" + lookup.apply(outer).asString() + ".png");
		}
	}
}
