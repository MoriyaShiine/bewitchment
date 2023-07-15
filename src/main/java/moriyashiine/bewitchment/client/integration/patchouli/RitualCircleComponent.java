/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.client.integration.patchouli;

import moriyashiine.bewitchment.common.Bewitchment;
import net.minecraft.client.gui.DrawContext;
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
	public void render(DrawContext graphics, IComponentRenderContext context, float pticks, int mouseX, int mouseY) {
		graphics.getMatrices().push();
		graphics.getMatrices().translate(x, y, -1);
		graphics.getMatrices().scale(4, 4, 1);
		if (circles[1] != null) {
			graphics.drawTexture(circles[1], 0, 0, 0, 0, 16, 16, 16, 16);
		}
		graphics.getMatrices().translate(4, 4, 1);
		graphics.drawTexture(circles[0], 0, 0, 4, 4, 7, 7, 16, 16);
		graphics.getMatrices().pop();
	}

	@Override
	public void onVariablesAvailable(UnaryOperator<IVariable> lookup) {
		circles[0] = Bewitchment.id("textures/gui/patchouli/chalk/" + lookup.apply(inner).asString() + ".png");
		if (!lookup.apply(outer).asString().isEmpty()) {
			circles[1] = Bewitchment.id("textures/gui/patchouli/chalk/" + lookup.apply(outer).asString() + ".png");
		}
	}
}
