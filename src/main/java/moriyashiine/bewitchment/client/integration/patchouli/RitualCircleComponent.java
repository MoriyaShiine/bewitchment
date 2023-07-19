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
		if (circles[1] != null) {
			graphics.drawTexture(circles[1], x, y, 0, 0, 64, 64, 64, 64);
		}
		graphics.drawTexture(circles[0], x + 16, y + 16, 16, 16, 28, 28, 64, 64);
	}

	@Override
	public void onVariablesAvailable(UnaryOperator<IVariable> lookup) {
		circles[0] = Bewitchment.id("textures/gui/patchouli/chalk/" + lookup.apply(inner).asString() + ".png");
		if (!lookup.apply(outer).asString().isEmpty()) {
			circles[1] = Bewitchment.id("textures/gui/patchouli/chalk/" + lookup.apply(outer).asString() + ".png");
		}
	}
}
