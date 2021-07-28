package moriyashiine.bewitchment.client.model.entity.living;

import moriyashiine.bewitchment.common.entity.living.HerneEntity;
import moriyashiine.bewitchment.common.registry.BWObjects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;

@Environment(EnvType.CLIENT)
public class HerneEntityModel<T extends HerneEntity> extends BipedEntityModel<T> {
	public static final ItemStack HORNED_SPEAR = new ItemStack(BWObjects.HORNED_SPEAR);
	
	private final ModelPart chest;
	private final ModelPart moss01;
	private final ModelPart moss02;
	private final ModelPart mossL;
	private final ModelPart mossR;
	private final ModelPart bipedLeftArm;
	private final ModelPart bipedRightArm;
	private final ModelPart bipedLeftLeg;
	private final ModelPart bipedRightLeg;
	private final ModelPart neck00;
	
	private boolean realArm = false;
	
	public HerneEntityModel(ModelPart root) {
		super(root);
		chest = root.getChild("chest");
		moss01 = chest.getChild("stomach").getChild("moss01");
		moss02 = chest.getChild("stomach").getChild("moss02");
		mossL = chest.getChild("stomach").getChild("mossL");
		mossR = chest.getChild("stomach").getChild("mossR");
		bipedLeftArm = root.getChild("bipedLeftArm");
		bipedRightArm = root.getChild("bipedRightArm");
		bipedLeftLeg = root.getChild("bipedLeftLeg");
		bipedRightLeg = root.getChild("bipedRightLeg");
		neck00 = root.getChild("neck00");
	}
	
	public static TexturedModelData getTexturedModelData() {
		ModelData data = BipedEntityModel.getModelData(Dilation.NONE, 0);
		ModelPartData root = data.getRoot();
		ModelPartData bipedLeftLeg = root.addChild("bipedLeftLeg",
				ModelPartBuilder.create()
						.uv(0, 16).cuboid(-2.5F, -0.7F, -3.0F, 5.0F, 12.0F, 6.0F, new Dilation(-0.2F, -0.2F, -0.2F)),
				ModelTransform.of(2.5F, -0.2F, 0.6F, 0.0F, 0.0F, 0.0F));
		ModelPartData lLeg02 = bipedLeftLeg.addChild("lLeg02",
				ModelPartBuilder.create()
						.uv(0, 34).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 8.0F, 4.0F, new Dilation(-0.3F, -0.3F, -0.3F)),
				ModelTransform.of(0.0F, 9.5F, -0.7F, 0.733F, 0.0F, 0.0873F));
		ModelPartData lLeg03 = lLeg02.addChild("lLeg03",
				ModelPartBuilder.create()
						.uv(0, 47).cuboid(-1.5F, 0.0F, -1.5F, 3.0F, 9.0F, 3.0F, new Dilation(-0.15F, -0.15F, -0.15F)),
				ModelTransform.of(0.0F, 7.1F, 0.1F, -0.4538F, 0.0F, 0.0F));
		ModelPartData lHoofClaw01a = lLeg03.addChild("lHoofClaw01a",
				ModelPartBuilder.create()
						.uv(9, 60).cuboid(-0.5F, -0.5F, -2.0F, 1.0F, 1.0F, 2.0F),
				ModelTransform.of(0.7F, 8.4F, -1.4F, 0.0524F, -0.1047F, 0.0F));
		lHoofClaw01a.addChild("lHoofClaw01b",
				ModelPartBuilder.create()
						.uv(0, 59).cuboid(-0.49F, -1.1F, -1.2F, 1.0F, 1.0F, 3.0F),
				ModelTransform.of(0.0F, 0.0F, -0.8F, 0.3491F, 0.0F, 0.0F));
		ModelPartData lHoofClaw02a = lLeg03.addChild("lHoofClaw02a",
				ModelPartBuilder.create()
						.uv(9, 60).cuboid(-0.5F, -0.5F, -2.0F, 1.0F, 1.0F, 2.0F),
				ModelTransform.of(-0.6F, 8.4F, -1.4F, 0.0524F, 0.0524F, 0.0F));
		lHoofClaw02a.addChild("lHoofClaw02b",
				ModelPartBuilder.create()
						.uv(0, 59).cuboid(-0.49F, -1.1F, -1.2F, 1.0F, 1.0F, 3.0F),
				ModelTransform.of(0.0F, 0.0F, -0.8F, 0.3491F, 0.0F, 0.0F));
		ModelPartData bipedRightArm = root.addChild("bipedRightArm",
				ModelPartBuilder.create()
						.uv(58, 18).cuboid(-2.0F, -1.0F, -2.5F, 4.0F, 20.0F, 4.0F),
				ModelTransform.of(-5.5F, -15.7F, 1.0F, 0.0F, 0.0F, 0.0F));
		bipedRightArm.addChild("rMantle",
				ModelPartBuilder.create()
						.uv(69, 45).cuboid(-2.5F, -2.0F, -3.0F, 5.0F, 8.0F, 6.0F),
				ModelTransform.of(0.1F, -0.3F, -0.5F, 0.0F, 0.0F, 0.1309F));
		ModelPartData chest = root.addChild("chest",
				ModelPartBuilder.create()
						.uv(23, 15).cuboid(-4.9F, -8.0F, -3.5F, 10.0F, 8.0F, 7.0F),
				ModelTransform.of(0.0F, -9.4F, 0.0F, 0.0F, 0.0F, 0.0F));
		chest.addChild("lPec",
				ModelPartBuilder.create()
						.uv(19, 50).cuboid(-3.0F, -3.0F, -3.0F, 6.0F, 6.0F, 3.0F),
				ModelTransform.of(2.6F, -4.8F, -1.2F, 0.0F, -0.1047F, 0.0873F));
		chest.addChild("rPec",
				ModelPartBuilder.create()
						.uv(19, 50).cuboid(-3.0F, -3.0F, -3.0F, 6.0F, 6.0F, 3.0F),
				ModelTransform.of(-2.6F, -4.8F, -1.2F, 0.0F, 0.1047F, -0.0873F));
		ModelPartData stomach = chest.addChild("stomach",
				ModelPartBuilder.create()
						.uv(19, 31).cuboid(-4.5F, -6.5F, -3.0F, 9.0F, 10.0F, 6.0F),
				ModelTransform.of(0.0F, 6.2F, 0.5F, 0.0F, 0.0F, 0.0F));
		stomach.addChild("moss01",
				ModelPartBuilder.create()
						.uv(92, 45).cuboid(-5.0F, 0.0F, -0.5F, 10.0F, 9.0F, 1.0F),
				ModelTransform.of(0.0F, 1.4F, -2.5F, -0.3665F, 0.0F, 0.0F));
		stomach.addChild("moss02",
				ModelPartBuilder.create()
						.uv(39, 53).cuboid(-5.0F, 0.0F, -0.5F, 10.0F, 9.0F, 1.0F),
				ModelTransform.of(0.0F, 1.4F, 2.8F, 0.0F, 0.0F, 0.0F));
		stomach.addChild("mossL",
				ModelPartBuilder.create()
						.uv(111, 48).cuboid(-0.5F, -0.7F, -3.5F, 1.0F, 9.0F, 7.0F),
				ModelTransform.of(-4.7F, 2.1F, -0.1F, -0.1745F, 0.0F, 0.1745F));
		stomach.addChild("mossR",
				ModelPartBuilder.create()
						.uv(111, 48).cuboid(-0.5F, -0.7F, -3.5F, 1.0F, 9.0F, 7.0F),
				ModelTransform.of(4.7F, 2.1F, -0.1F, -0.1745F, 0.0F, -0.1745F));
		chest.addChild("mMantle",
				ModelPartBuilder.create()
						.uv(75, 26).cuboid(-6.0F, 0.0F, -4.5F, 12.0F, 10.0F, 9.0F),
				ModelTransform.of(0.0F, -8.8F, -0.5F, 0.0F, 0.0F, 0.0F));
		ModelPartData bipedLeftArm = root.addChild("bipedLeftArm",
				ModelPartBuilder.create()
						.uv(58, 18).cuboid(-2.0F, -1.0F, -2.5F, 4.0F, 20.0F, 4.0F),
				ModelTransform.of(5.5F, -15.7F, 1.0F, 0.0F, 0.0F, 0.0F));
		bipedLeftArm.addChild("lMantle",
				ModelPartBuilder.create()
						.uv(69, 45).cuboid(-2.5F, -2.0F, -3.0F, 5.0F, 8.0F, 6.0F),
				ModelTransform.of(-0.1F, -0.3F, -0.5F, 0.0F, 0.0F, -0.1309F));
		ModelPartData bipedRightLeg = root.addChild("bipedRightLeg",
				ModelPartBuilder.create()
						.uv(0, 16).cuboid(-2.5F, -0.7F, -3.0F, 5.0F, 12.0F, 6.0F, new Dilation(-0.2F, -0.2F, -0.2F)),
				ModelTransform.of(-2.5F, -0.2F, 0.6F, 0.0F, 0.0F, 0.0F));
		ModelPartData rLeg02 = bipedRightLeg.addChild("rLeg02",
				ModelPartBuilder.create()
						.uv(0, 34).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 8.0F, 4.0F, new Dilation(-0.3F, -0.3F, -0.3F)),
				ModelTransform.of(0.0F, 9.5F, -0.7F, 0.733F, 0.0F, -0.0873F));
		ModelPartData rLeg03 = rLeg02.addChild("rLeg03",
				ModelPartBuilder.create()
						.uv(0, 47).cuboid(-1.5F, 0.0F, -1.5F, 3.0F, 9.0F, 3.0F, new Dilation(-0.15F, -0.15F, -0.15F)),
				ModelTransform.of(0.0F, 7.1F, 0.1F, -0.4538F, 0.0F, 0.0F));
		ModelPartData rHoofClaw01a = rLeg03.addChild("rHoofClaw01a",
				ModelPartBuilder.create()
						.uv(9, 60).cuboid(-0.5F, -0.5F, -2.0F, 1.0F, 1.0F, 2.0F),
				ModelTransform.of(-0.7F, 8.4F, -1.4F, 0.0524F, 0.1047F, 0.0F));
		rHoofClaw01a.addChild("rHoofClaw01b",
				ModelPartBuilder.create()
						.uv(0, 59).cuboid(-0.51F, -1.1F, -1.2F, 1.0F, 1.0F, 3.0F),
				ModelTransform.of(0.0F, 0.0F, -0.8F, 0.3491F, 0.0F, 0.0F));
		ModelPartData rHoofClaw02a = rLeg03.addChild("rHoofClaw02a",
				ModelPartBuilder.create()
						.uv(9, 60).cuboid(-0.5F, -0.5F, -2.0F, 1.0F, 1.0F, 2.0F),
				ModelTransform.of(0.6F, 8.4F, -1.4F, 0.0524F, -0.0524F, 0.0F));
		rHoofClaw02a.addChild("rHoofClaw02b",
				ModelPartBuilder.create()
						.uv(0, 59).cuboid(-0.51F, -1.1F, -1.2F, 1.0F, 1.0F, 3.0F),
				ModelTransform.of(0.0F, 0.0F, -0.8F, 0.3491F, 0.0F, 0.0F));
		ModelPartData neck00 = root.addChild("neck00",
				ModelPartBuilder.create()
						.uv(51, 0).cuboid(-3.5F, -1.7F, -3.0F, 7.0F, 3.0F, 6.0F),
				ModelTransform.of(0.0F, -17.5F, -0.1F, 0.0F, 0.0F, 0.0F));
		ModelPartData neck01 = neck00.addChild("neck01",
				ModelPartBuilder.create()
						.uv(28, 0).cuboid(-3.11F, -3.0F, -2.5F, 6.0F, 3.0F, 5.0F),
				ModelTransform.of(0.0F, -1.2206F, -0.584F, 0.2182F, 0.0F, 0.0F));
		ModelPartData realHead = neck01.addChild("realHead",
				ModelPartBuilder.create()
						.cuboid(-3.0F, -4.8F, -3.6F, 6.0F, 5.0F, 6.0F),
				ModelTransform.of(0.0F, -2.8F, 0.2F, -0.1396F, 0.0F, 0.0F));
		realHead.addChild("snout",
				ModelPartBuilder.create()
						.uv(78, 0).cuboid(-2.0F, -1.35F, -3.9F, 4.0F, 2.0F, 5.0F),
				ModelTransform.of(0.0F, -3.2F, -4.1F, 0.2793F, 0.0F, 0.0F));
		realHead.addChild("lowerJaw",
				ModelPartBuilder.create()
						.uv(97, 10).cuboid(-2.0F, -0.4F, -3.6F, 4.0F, 1.0F, 4.0F),
				ModelTransform.of(0.0F, -0.5F, -3.9F, 0.0F, 0.0F, 0.0F));
		realHead.addChild("lEar",
				ModelPartBuilder.create()
						.uv(20, 0).cuboid(0.0F, -1.0F, -0.5F, 3.0F, 2.0F, 1.0F),
				ModelTransform.of(2.3F, -3.4F, 0.9F, 0.1745F, -0.3491F, -0.4538F));
		realHead.addChild("rEar",
				ModelPartBuilder.create()
						.uv(20, 0).cuboid(-3.0F, -1.0F, -0.5F, 3.0F, 2.0F, 1.0F),
				ModelTransform.of(-2.3F, -3.4F, 0.9F, 0.1745F, 0.3491F, 0.4538F));
		ModelPartData lAntler01 = realHead.addChild("lAntler01",
				ModelPartBuilder.create()
						.uv(107, 0).cuboid(-1.0F, -2.3F, -1.0F, 2.0F, 3.0F, 2.0F),
				ModelTransform.of(1.5F, -4.6F, -0.6F, -0.3491F, 0.0F, 0.2793F));
		ModelPartData lAntler02a = lAntler01.addChild("lAntler02a",
				ModelPartBuilder.create()
						.uv(107, 0).cuboid(-0.2F, -0.5F, 0.0F, 1.0F, 1.0F, 6.0F),
				ModelTransform.of(-0.2F, -1.7F, -0.3F, 0.7854F, 0.5236F, 0.1396F));
		ModelPartData lAntler02b = lAntler02a.addChild("lAntler02b",
				ModelPartBuilder.create()
						.uv(107, 0).cuboid(-0.7F, -0.5F, 0.0F, 1.0F, 1.0F, 6.0F),
				ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		ModelPartData lAntler03 = lAntler02b.addChild("lAntler03",
				ModelPartBuilder.create()
						.uv(107, 0).cuboid(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 5.0F),
				ModelTransform.of(0.2F, 0.0F, 5.4F, 0.6109F, -0.2618F, 0.0F));
		ModelPartData lAntler04 = lAntler03.addChild("lAntler04",
				ModelPartBuilder.create()
						.uv(107, 0).cuboid(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 5.0F),
				ModelTransform.of(0.0F, 0.0F, 4.8F, -0.5236F, 0.0F, 0.0F));
		lAntler04.addChild("lAntler04b",
				ModelPartBuilder.create()
						.uv(107, 0).cuboid(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 3.0F),
				ModelTransform.of(0.0F, 0.0F, 4.8F, 0.3491F, -0.2618F, 0.0F));
		ModelPartData lAntler07a = lAntler03.addChild("lAntler07a",
				ModelPartBuilder.create()
						.uv(107, 0).cuboid(-0.5F, 0.0F, -0.5F, 1.0F, 3.0F, 1.0F),
				ModelTransform.of(0.0F, 0.1F, 1.0F, 0.8727F, 0.0F, -0.5934F));
		lAntler07a.addChild("lAntler07b",
				ModelPartBuilder.create()
						.uv(107, 0).cuboid(-0.5F, 0.0F, -0.5F, 1.0F, 3.0F, 1.0F),
				ModelTransform.of(0.0F, 2.9F, 0.0F, 0.2793F, 0.0F, 0.0F));
		ModelPartData lAntler08a = lAntler03.addChild("lAntler08a",
				ModelPartBuilder.create()
						.uv(107, 0).cuboid(-0.5F, 0.0F, -0.5F, 1.0F, 3.0F, 1.0F),
				ModelTransform.of(0.0F, 0.1F, 1.2F, 0.6981F, 0.0F, 0.3491F));
		lAntler08a.addChild("lAntler08b",
				ModelPartBuilder.create()
						.uv(107, 0).cuboid(-0.5F, 0.0F, -0.5F, 1.0F, 5.0F, 1.0F),
				ModelTransform.of(0.0F, 2.9F, 0.0F, 0.2793F, 0.0F, 0.3491F));
		ModelPartData lAntler09a = lAntler03.addChild("lAntler09a",
				ModelPartBuilder.create()
						.uv(107, 0).cuboid(-0.5F, 0.0F, -0.5F, 1.0F, 5.0F, 1.0F),
				ModelTransform.of(0.0F, -0.4F, -1.0F, 0.6109F, -0.1222F, 0.4363F));
		lAntler09a.addChild("lAntler09b",
				ModelPartBuilder.create()
						.uv(107, 0).cuboid(-0.5F, 0.0F, -0.5F, 1.0F, 3.0F, 1.0F),
				ModelTransform.of(0.0F, 4.8F, 0.0F, 0.2793F, 0.0F, 0.3491F));
		ModelPartData lAntler05a = lAntler02a.addChild("lAntler05a",
				ModelPartBuilder.create()
						.uv(107, 0).cuboid(-0.5F, -3.5F, -0.4F, 1.0F, 3.0F, 1.0F),
				ModelTransform.of(0.0F, 0.4F, 4.8F, -0.5236F, 0.0F, 0.3665F));
		ModelPartData lAntler05b = lAntler05a.addChild("lAntler05b",
				ModelPartBuilder.create()
						.uv(107, 0).cuboid(-0.5F, -3.5F, -0.9F, 1.0F, 3.0F, 1.0F),
				ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		ModelPartData lAntler05c = lAntler05b.addChild("lAntler05c",
				ModelPartBuilder.create()
						.uv(107, 0).cuboid(-0.5F, -3.5F, -0.5F, 1.0F, 4.0F, 1.0F),
				ModelTransform.of(-0.1F, -2.9F, -0.2F, 0.0F, 0.0F, -0.1745F));
		lAntler05c.addChild("lAntler05d",
				ModelPartBuilder.create()
						.uv(107, 0).cuboid(-0.5F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F),
				ModelTransform.of(0.1F, -3.1F, 0.0F, 0.0F, 0.0F, -0.3491F));
		ModelPartData lAntler10a = lAntler02a.addChild("lAntler10a",
				ModelPartBuilder.create()
						.uv(107, 0).cuboid(-0.5F, -3.5F, -0.5F, 1.0F, 4.0F, 1.0F),
				ModelTransform.of(0.4F, -0.4F, 0.7F, 0.0F, 0.0F, 0.3665F));
		lAntler10a.addChild("lAntler10b",
				ModelPartBuilder.create()
						.uv(107, 0).cuboid(-0.5F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F),
				ModelTransform.of(0.1F, -3.1F, 0.0F, 0.0F, 0.0F, -0.3142F));
		ModelPartData lAntler11a = lAntler02a.addChild("lAntler11a",
				ModelPartBuilder.create()
						.uv(107, 0).cuboid(-0.5F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F),
				ModelTransform.of(0.4F, 0.0F, -0.2F, 0.4363F, 0.0F, 0.4363F));
		lAntler11a.addChild("lAntler11b",
				ModelPartBuilder.create()
						.uv(107, 0).cuboid(-0.5F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F),
				ModelTransform.of(0.1F, -2.8F, 0.0F, 0.0F, 0.0F, -0.3142F));
		ModelPartData rAntler01 = realHead.addChild("rAntler01",
				ModelPartBuilder.create()
						.uv(107, 0).cuboid(-1.0F, -2.2F, -1.0F, 2.0F, 3.0F, 2.0F),
				ModelTransform.of(-1.5F, -4.7F, -0.6F, -0.3491F, 0.0F, -0.2793F));
		ModelPartData rAntler02a = rAntler01.addChild("rAntler02a",
				ModelPartBuilder.create()
						.uv(107, 0).cuboid(-0.2F, -0.5F, 0.0F, 1.0F, 1.0F, 6.0F),
				ModelTransform.of(0.2F, -1.7F, -0.3F, 0.7854F, -0.5236F, -0.1396F));
		ModelPartData rAntler02b = rAntler02a.addChild("rAntler02b",
				ModelPartBuilder.create()
						.uv(107, 0).cuboid(-0.7F, -0.5F, 0.0F, 1.0F, 1.0F, 6.0F),
				ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		ModelPartData rAntler03 = rAntler02b.addChild("rAntler03",
				ModelPartBuilder.create()
						.uv(107, 0).cuboid(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 5.0F),
				ModelTransform.of(-0.2F, 0.0F, 5.4F, 0.6109F, 0.2618F, 0.0F));
		ModelPartData rAntler04 = rAntler03.addChild("rAntler04",
				ModelPartBuilder.create()
						.uv(107, 0).cuboid(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 5.0F),
				ModelTransform.of(0.0F, 0.0F, 4.8F, -0.5236F, 0.0F, 0.0F));
		rAntler04.addChild("rAntler04b",
				ModelPartBuilder.create()
						.uv(107, 0).cuboid(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 3.0F),
				ModelTransform.of(0.0F, 0.0F, 4.8F, 0.3491F, 0.2618F, 0.0F));
		ModelPartData rAntler07a = rAntler03.addChild("rAntler07a",
				ModelPartBuilder.create()
						.uv(107, 0).cuboid(-0.5F, 0.0F, -0.5F, 1.0F, 3.0F, 1.0F),
				ModelTransform.of(0.0F, 0.1F, 1.0F, 0.8727F, 0.0F, 0.5934F));
		rAntler07a.addChild("rAntler07b",
				ModelPartBuilder.create()
						.uv(107, 0).cuboid(-0.5F, 0.0F, -0.5F, 1.0F, 3.0F, 1.0F),
				ModelTransform.of(0.0F, 2.9F, 0.0F, 0.2793F, 0.0F, 0.0F));
		ModelPartData rAntler08a = rAntler03.addChild("rAntler08a",
				ModelPartBuilder.create()
						.uv(107, 0).cuboid(-0.5F, 0.0F, -0.5F, 1.0F, 3.0F, 1.0F),
				ModelTransform.of(0.0F, 0.1F, 1.2F, 0.6981F, 0.0F, -0.3491F));
		rAntler08a.addChild("rAntler08b",
				ModelPartBuilder.create()
						.uv(107, 0).cuboid(-0.5F, 0.0F, -0.5F, 1.0F, 5.0F, 1.0F),
				ModelTransform.of(0.0F, 2.9F, 0.0F, 0.2793F, 0.0F, -0.3491F));
		ModelPartData rAntler09a = rAntler03.addChild("rAntler09a",
				ModelPartBuilder.create()
						.uv(107, 0).cuboid(-0.5F, 0.0F, -0.5F, 1.0F, 5.0F, 1.0F),
				ModelTransform.of(0.0F, -0.4F, -1.0F, 0.6109F, 0.1222F, -0.4363F));
		rAntler09a.addChild("rAntler09b",
				ModelPartBuilder.create()
						.uv(107, 0).cuboid(-0.5F, 0.0F, -0.5F, 1.0F, 3.0F, 1.0F),
				ModelTransform.of(0.0F, 4.8F, 0.0F, 0.2793F, 0.0F, -0.3491F));
		ModelPartData rAntler05a = rAntler02a.addChild("rAntler05a",
				ModelPartBuilder.create()
						.uv(107, 0).cuboid(-0.5F, -3.5F, -0.4F, 1.0F, 3.0F, 1.0F),
				ModelTransform.of(0.0F, 0.4F, 4.8F, -0.5236F, 0.0F, -0.3665F));
		ModelPartData rAntler05b = rAntler05a.addChild("rAntler05b",
				ModelPartBuilder.create()
						.uv(107, 0).cuboid(-0.5F, -3.5F, -0.9F, 1.0F, 3.0F, 1.0F),
				ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		ModelPartData rAntler05c = rAntler05b.addChild("rAntler05c",
				ModelPartBuilder.create()
						.uv(107, 0).cuboid(-0.5F, -3.5F, -0.5F, 1.0F, 4.0F, 1.0F),
				ModelTransform.of(0.1F, -2.9F, -0.2F, 0.0F, 0.0F, 0.1745F));
		rAntler05c.addChild("rAntler05d",
				ModelPartBuilder.create()
						.uv(107, 0).cuboid(-0.5F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F),
				ModelTransform.of(-0.1F, -3.1F, 0.0F, 0.0F, 0.0F, 0.3491F));
		ModelPartData rAntler10a = rAntler02a.addChild("rAntler10a",
				ModelPartBuilder.create()
						.uv(107, 0).cuboid(-0.5F, -3.5F, -0.5F, 1.0F, 4.0F, 1.0F),
				ModelTransform.of(-0.4F, -0.4F, 0.7F, 0.0F, 0.0F, -0.3665F));
		rAntler10a.addChild("rAntler10b",
				ModelPartBuilder.create()
						.uv(107, 0).cuboid(-0.5F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F),
				ModelTransform.of(-0.1F, -3.1F, 0.0F, 0.0F, 0.0F, 0.3142F));
		ModelPartData rAntler11a = rAntler02a.addChild("rAntler11a",
				ModelPartBuilder.create()
						.uv(107, 0).cuboid(-0.5F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F),
				ModelTransform.of(-0.4F, 0.0F, -0.2F, 0.4363F, 0.0F, -0.4363F));
		rAntler11a.addChild("rAntler11b",
				ModelPartBuilder.create()
						.uv(107, 0).cuboid(-0.5F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F),
				ModelTransform.of(0.1F, -2.8F, 0.0F, 0.0F, 0.0F, 0.3142F));
		realHead.addChild("upperJaw00",
				ModelPartBuilder.create()
						.uv(76, 8).cuboid(-2.5F, -0.7F, -3.7F, 5.0F, 2.0F, 5.0F),
				ModelTransform.of(0.0F, -2.0F, -4.1F, 0.0F, 0.0F, 0.0F));
		neck00.addChild("neckFur01",
				ModelPartBuilder.create()
						.uv(79, 17).cuboid(-2.0F, -1.0F, -1.8F, 4.0F, 5.0F, 2.0F, new Dilation(0.1F, 0.1F, 0.1F)),
				ModelTransform.of(0.0F, -1.75F, -1.9F, -0.6109F, 0.0F, 0.0F));
		neck00.addChild("neckFur02",
				ModelPartBuilder.create()
						.uv(110, 16).cuboid(-3.0F, -1.0F, -1.9F, 6.0F, 8.0F, 2.0F, new Dilation(0.15F, 0.15F, 0.15F)),
				ModelTransform.of(0.0F, 0.5F, -2.9F, -0.1309F, 0.0F, 0.0F));
		return TexturedModelData.of(data, 128, 64);
	}
	
	@Override
	public void setAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float realHeadYaw, float realHeadPitch) {
		entity.setStackInHand(Hand.MAIN_HAND, HORNED_SPEAR);
		realArm = false;
		super.setAngles(entity, limbAngle, limbDistance, animationProgress, realHeadYaw, realHeadPitch);
		realArm = true;
		copyRotation(neck00, super.head);
		copyRotation(chest, super.body);
		copyRotation(bipedLeftArm, super.leftArm);
		bipedLeftArm.roll -= 0.1309f;
		copyRotation(bipedRightArm, super.rightArm);
		bipedRightArm.roll += 0.1309f;
		copyRotation(bipedLeftLeg, super.leftLeg);
		bipedLeftLeg.pitch /= 2;
		bipedLeftLeg.pitch -= 0.2793f;
		bipedLeftLeg.roll -= 0.1047f;
		copyRotation(bipedRightLeg, super.rightLeg);
		bipedRightLeg.pitch /= 2;
		bipedRightLeg.pitch -= 0.2793f;
		bipedRightLeg.roll += 0.1047f;
		moss01.pitch = Math.min(bipedLeftLeg.pitch, bipedRightLeg.pitch);
		mossL.pitch = moss01.pitch;
		mossR.pitch = moss01.pitch;
		moss02.pitch = Math.max(bipedLeftLeg.pitch, bipedRightLeg.pitch) + 1 / 3f;
	}
	
	@Override
	public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
		neck00.render(matrices, vertices, light, overlay, red, green, blue, alpha);
		chest.render(matrices, vertices, light, overlay, red, green, blue, alpha);
		bipedLeftArm.render(matrices, vertices, light, overlay, red, green, blue, alpha);
		bipedRightArm.render(matrices, vertices, light, overlay, red, green, blue, alpha);
		bipedLeftLeg.render(matrices, vertices, light, overlay, red, green, blue, alpha);
		bipedRightLeg.render(matrices, vertices, light, overlay, red, green, blue, alpha);
	}
	
	@Override
	protected ModelPart getArm(Arm arm) {
		return realArm ? (arm == Arm.LEFT ? bipedLeftArm : bipedRightArm) : super.getArm(arm);
	}
	
	private void copyRotation(ModelPart to, ModelPart from) {
		to.pitch = from.pitch;
		to.yaw = from.yaw;
		to.roll = from.roll;
	}
}
