package moriyashiine.bewitchment.client.model.entity.living;

import moriyashiine.bewitchment.common.entity.living.BaphometEntity;
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
import net.minecraft.util.math.MathHelper;

@Environment(EnvType.CLIENT)
public class BaphometEntityModel<T extends BaphometEntity> extends BipedEntityModel<T> {
	private static final ItemStack CADUCEUS = new ItemStack(BWObjects.CADUCEUS);

	private final ModelPart realBody;
	private final ModelPart frontLoincloth;
	private final ModelPart backLoincloth;
	private final ModelPart rWing01;
	private final ModelPart lWing01;
	private final ModelPart BipedLeftArm;
	private final ModelPart BipedRightArm;
	private final ModelPart BipedLeftLeg;
	private final ModelPart BipedRightLeg;
	private final ModelPart realHead;

	private boolean realArm = false;

	public BaphometEntityModel(ModelPart root) {
		super(root);
		realBody = root.getChild("realBody");
		frontLoincloth = realBody.getChild("stomach").getChild("hips").getChild("frontLoincloth");
		backLoincloth = realBody.getChild("stomach").getChild("hips").getChild("backLoincloth");
		rWing01 = realBody.getChild("rWing01");
		lWing01 = realBody.getChild("lWing01");
		BipedLeftArm = root.getChild("BipedLeftArm");
		BipedRightArm = root.getChild("BipedRightArm");
		BipedLeftLeg = root.getChild("BipedLeftLeg");
		BipedRightLeg = root.getChild("BipedRightLeg");
		realHead = root.getChild("realHead");
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData data = BipedEntityModel.getModelData(Dilation.NONE, 0);
		ModelPartData root = data.getRoot();
		ModelPartData realBody = root.addChild("realBody", ModelPartBuilder.create().uv(20, 19).cuboid(-4.0F, 0.0F, -2.5F, 8.0F, 6.0F, 5.0F), ModelTransform.of(0.0F, -12.7F, 0.0F, 0.0F, 0.0F, 0.0F));
		realBody.addChild("boobLeft", ModelPartBuilder.create().uv(18, 52).mirrored(true).cuboid(-1.5F, 0.5F, -1.8F, 3.0F, 3.0F, 3.0F), ModelTransform.of(1.6F, 1.9F, -0.8F, -0.6981F, -0.1745F, 0.0F));
		realBody.addChild("boobRight", ModelPartBuilder.create().uv(18, 52).cuboid(-1.5F, 0.5F, -1.8F, 3.0F, 3.0F, 3.0F), ModelTransform.of(-1.6F, 1.9F, -0.8F, -0.6981F, 0.1745F, 0.0F));
		ModelPartData stomach = realBody.addChild("stomach", ModelPartBuilder.create().uv(19, 31).cuboid(-3.5F, 0.0F, -2.5F, 7.0F, 6.0F, 5.0F).uv(90, 48).cuboid(-4.5F, 3.7F, -3.0F, 9.0F, 3.0F, 2.0F).uv(108, 18).cuboid(-4.0F, 1.7F, -3.0F, 8.0F, 2.0F, 2.0F), ModelTransform.of(0.0F, 6.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		ModelPartData hips = stomach.addChild("hips", ModelPartBuilder.create().uv(16, 43).cuboid(-4.0F, 0.0F, -2.5F, 8.0F, 3.0F, 5.0F), ModelTransform.of(0.0F, 6.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		hips.addChild("frontLoincloth", ModelPartBuilder.create().uv(65, 0).cuboid(-5.0F, 0.4698F, -0.329F, 10.0F, 10.0F, 2.0F), ModelTransform.of(0.0F, 0.0F, -2.0F, 0.0F, 0.0F, 0.0F));
		hips.addChild("backLoincloth", ModelPartBuilder.create().uv(91, 0).cuboid(-4.5F, 0.0F, -1.0F, 9.0F, 10.0F, 2.0F), ModelTransform.of(0.0F, 1.1F, 1.8F, 0.0F, 0.0F, 0.0F));
		ModelPartData rWing01 = realBody.addChild("rWing01", ModelPartBuilder.create().uv(57, 40).mirrored(true).cuboid(-1.0F, -1.5F, 0.0F, 2.0F, 3.0F, 6.0F), ModelTransform.of(-1.8F, 2.8F, 1.4F, 0.3491F, -0.6458F, 0.0F));
		ModelPartData rWing02 = rWing01.addChild("rWing02", ModelPartBuilder.create().uv(43, 41).mirrored(true).cuboid(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 9.0F), ModelTransform.of(-0.1F, 0.2F, 5.6F, 0.5236F, -0.1396F, 0.0F));
		ModelPartData rWing03 = rWing02.addChild("rWing03", ModelPartBuilder.create().uv(42, 53).mirrored(true).cuboid(-1.0F, 0.0F, -1.0F, 2.0F, 9.0F, 2.0F), ModelTransform.of(-0.1F, 0.4F, 8.0F, -0.1745F, 0.0F, 0.0F));
		ModelPartData rWing04 = rWing03.addChild("rWing04", ModelPartBuilder.create().uv(51, 52).mirrored(true).cuboid(-0.5F, 0.0F, -0.5F, 1.0F, 11.0F, 1.0F), ModelTransform.of(0.0F, 8.6F, 0.2F, -0.4712F, 0.0F, 0.0F));
		rWing04.addChild("rWing05", ModelPartBuilder.create().uv(61, 5).mirrored(true).cuboid(0.0F, 0.0F, -13.0F, 0.0F, 20.0F, 13.0F), ModelTransform.of(0.0F, 2.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		rWing03.addChild("rFeathers02", ModelPartBuilder.create().uv(90, 13).mirrored(true).cuboid(-0.5F, -0.6F, -13.7F, 1.0F, 16.0F, 16.0F), ModelTransform.of(0.1F, 2.3F, 0.0F, -0.4363F, 0.0F, 0.0F));
		rWing02.addChild("rFeathers01", ModelPartBuilder.create().uv(65, 31).mirrored(true).cuboid(-0.5F, -0.6F, -11.1F, 1.0F, 10.0F, 22.0F), ModelTransform.of(-0.2F, 0.0F, 0.0F, -0.1745F, 0.0F, 0.0F));
		ModelPartData lWing01 = realBody.addChild("lWing01", ModelPartBuilder.create().uv(57, 40).mirrored(true).cuboid(-1.0F, -1.5F, 0.0F, 2.0F, 3.0F, 6.0F), ModelTransform.of(1.8F, 2.8F, 1.4F, 0.3491F, 0.6458F, 0.0F));
		ModelPartData lWing02 = lWing01.addChild("lWing02", ModelPartBuilder.create().uv(43, 41).mirrored(true).cuboid(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 9.0F), ModelTransform.of(0.1F, 0.2F, 5.6F, 0.5236F, 0.1396F, 0.0F));
		ModelPartData lWing03 = lWing02.addChild("lWing03", ModelPartBuilder.create().uv(42, 53).mirrored(true).cuboid(-1.0F, 0.0F, -1.0F, 2.0F, 9.0F, 2.0F), ModelTransform.of(0.1F, 0.4F, 8.0F, -0.1745F, 0.0F, 0.0F));
		ModelPartData lWing04 = lWing03.addChild("lWing04", ModelPartBuilder.create().uv(51, 52).mirrored(true).cuboid(-0.5F, 0.0F, -0.5F, 1.0F, 11.0F, 1.0F), ModelTransform.of(0.0F, 8.6F, 0.2F, -0.4712F, 0.0F, 0.0F));
		lWing04.addChild("lWing05", ModelPartBuilder.create().uv(61, 5).mirrored(true).cuboid(0.0F, 0.0F, -13.0F, 0.0F, 20.0F, 13.0F), ModelTransform.of(0.0F, 2.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		lWing03.addChild("lFeathers02", ModelPartBuilder.create().uv(90, 13).mirrored(true).cuboid(-0.5F, -0.6F, -13.7F, 1.0F, 16.0F, 16.0F), ModelTransform.of(-0.1F, 2.3F, 0.0F, -0.4363F, 0.0F, 0.0F));
		lWing02.addChild("lFeathers01", ModelPartBuilder.create().uv(65, 31).mirrored(true).cuboid(-0.5F, -0.6F, -11.1F, 1.0F, 10.0F, 22.0F), ModelTransform.of(0.2F, 0.0F, 0.0F, -0.1745F, 0.0F, 0.0F));
		ModelPartData BipedRightLeg = root.addChild("BipedRightLeg", ModelPartBuilder.create().uv(0, 16).mirrored(true).cuboid(-2.5F, -1.6F, -2.9F, 5.0F, 11.0F, 5.0F), ModelTransform.of(-2.4F, 2.1F, 0.3F, 0.0F, 0.0F, 0.0F));
		ModelPartData rLeg02 = BipedRightLeg.addChild("rLeg02", ModelPartBuilder.create().uv(0, 32).mirrored(true).cuboid(-1.5F, 0.0F, -2.0F, 3.0F, 8.0F, 4.0F), ModelTransform.of(-0.2F, 7.6F, -1.3F, 0.7679F, 0.1396F, 0.0436F));
		ModelPartData rLeg03 = rLeg02.addChild("rLeg03", ModelPartBuilder.create().uv(0, 44).mirrored(true).cuboid(-1.0F, 0.0F, -1.5F, 2.0F, 10.0F, 3.0F), ModelTransform.of(0.0F, 6.0F, 0.2F, -0.4887F, 0.0F, -0.1047F));
		ModelPartData rHoofClaw01a = rLeg03.addChild("rHoofClaw01a", ModelPartBuilder.create().uv(9, 58).mirrored(true).cuboid(-0.5F, -0.5F, -2.0F, 1.0F, 1.0F, 2.0F), ModelTransform.of(-0.5F, 9.5F, -1.3F, 0.0F, 0.1396F, 0.0349F));
		rHoofClaw01a.addChild("rHoofClaw01b", ModelPartBuilder.create().uv(0, 57).mirrored(true).cuboid(-0.51F, -1.1F, -1.2F, 1.0F, 1.0F, 3.0F), ModelTransform.of(0.0F, 0.0F, -1.0F, 0.3491F, 0.0F, 0.0F));
		ModelPartData rHoofClaw02a = rLeg03.addChild("rHoofClaw02a", ModelPartBuilder.create().uv(9, 58).mirrored(true).cuboid(-0.5F, -0.5F, -2.0F, 1.0F, 1.0F, 2.0F), ModelTransform.of(0.5F, 9.5F, -1.3F, 0.0F, -0.1396F, -0.0349F));
		rHoofClaw02a.addChild("rHoofClaw02b", ModelPartBuilder.create().uv(0, 57).mirrored(true).cuboid(-0.51F, -1.1F, -1.2F, 1.0F, 1.0F, 3.0F), ModelTransform.of(0.0F, 0.0F, -1.0F, 0.3491F, 0.0F, 0.0F));
		root.addChild("BipedRightArm", ModelPartBuilder.create().uv(46, 19).mirrored(true).cuboid(-2.0F, -2.0F, -2.0F, 3.0F, 16.0F, 4.0F), ModelTransform.of(-5.0F, -10.8F, 0.0F, 0.0F, 0.0F, 0.1F));
		ModelPartData BipedLeftLeg = root.addChild("BipedLeftLeg", ModelPartBuilder.create().uv(0, 16).cuboid(-2.5F, -1.6F, -2.9F, 5.0F, 11.0F, 5.0F), ModelTransform.of(2.6F, 2.1F, 0.3F, 0.0F, 0.0F, 0.0F));
		ModelPartData lLeg02 = BipedLeftLeg.addChild("lLeg02", ModelPartBuilder.create().uv(0, 32).cuboid(-1.5F, 0.0F, -2.0F, 3.0F, 8.0F, 4.0F), ModelTransform.of(0.2F, 7.6F, -1.3F, 0.7679F, -0.1396F, -0.0436F));
		ModelPartData lLeg03 = lLeg02.addChild("lLeg03", ModelPartBuilder.create().uv(0, 44).cuboid(-1.0F, 0.0F, -1.5F, 2.0F, 10.0F, 3.0F), ModelTransform.of(0.0F, 6.0F, 0.2F, -0.4887F, 0.0F, 0.1047F));
		ModelPartData lHoofClaw01a = lLeg03.addChild("lHoofClaw01a", ModelPartBuilder.create().uv(9, 58).cuboid(-0.5F, -0.5F, -2.0F, 1.0F, 1.0F, 2.0F), ModelTransform.of(0.5F, 9.5F, -1.3F, 0.0F, -0.1396F, -0.0349F));
		lHoofClaw01a.addChild("lHoofClaw01b", ModelPartBuilder.create().uv(0, 57).cuboid(-0.49F, -1.1F, -1.2F, 1.0F, 1.0F, 3.0F), ModelTransform.of(0.0F, 0.0F, -1.0F, 0.3491F, 0.0F, 0.0F));
		ModelPartData lHoofClaw02a = lLeg03.addChild("lHoofClaw02a", ModelPartBuilder.create().uv(9, 58).cuboid(-0.5F, -0.5F, -2.0F, 1.0F, 1.0F, 2.0F), ModelTransform.of(-0.5F, 9.5F, -1.3F, 0.0F, 0.1396F, 0.0349F));
		lHoofClaw02a.addChild("lHoofClaw02b", ModelPartBuilder.create().uv(0, 57).cuboid(-0.49F, -1.1F, -1.2F, 1.0F, 1.0F, 3.0F), ModelTransform.of(0.0F, 0.0F, -1.0F, 0.3491F, 0.0F, 0.0F));
		ModelPartData realHead = root.addChild("realHead", ModelPartBuilder.create().uv(1, 2).cuboid(-3.5F, -7.0F, -3.5F, 7.0F, 7.0F, 7.0F), ModelTransform.of(0.0F, -12.7F, 1.0F, 0.0F, 0.0F, 0.0F));
		ModelPartData rHorn01 = realHead.addChild("rHorn01", ModelPartBuilder.create().uv(23, 0).mirrored(true).cuboid(-1.0F, -2.7F, -1.0F, 2.0F, 4.0F, 2.0F), ModelTransform.of(-2.9F, -7.2F, -0.2F, -0.1745F, -0.1396F, -0.4014F));
		ModelPartData rHorn02 = rHorn01.addChild("rHorn02", ModelPartBuilder.create().mirrored(true).cuboid(-0.4F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F, new Dilation(0.3F, 0.3F, 0.3F)), ModelTransform.of(0.0F, -2.2F, 0.0F, -0.1745F, 0.0F, -0.2618F));
		ModelPartData rHorn03 = rHorn02.addChild("rHorn03", ModelPartBuilder.create().uv(0, 4).mirrored(true).cuboid(-0.3F, -3.0F, -0.6F, 1.0F, 3.0F, 1.0F, new Dilation(0.15F, 0.15F, 0.15F)), ModelTransform.of(0.0F, -2.7F, 0.0F, -0.1047F, 0.0F, -0.1745F));
		ModelPartData rHorn04 = rHorn03.addChild("rHorn04", ModelPartBuilder.create().mirrored(true).cuboid(-0.5F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F), ModelTransform.of(0.0F, -2.8F, 0.0F, 0.0524F, 0.0F, -0.1396F));
		rHorn04.addChild("rHorn05", ModelPartBuilder.create().mirrored(true).cuboid(-0.5F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F), ModelTransform.of(0.0F, -2.7F, 0.0F, 0.0524F, 0.0F, 0.1396F));
		ModelPartData lHorn01 = realHead.addChild("lHorn01", ModelPartBuilder.create().uv(23, 0).cuboid(-1.0F, -2.7F, -1.0F, 2.0F, 4.0F, 2.0F), ModelTransform.of(2.9F, -7.2F, -0.2F, -0.1745F, 0.1396F, 0.4014F));
		ModelPartData lHorn02 = lHorn01.addChild("lHorn02", ModelPartBuilder.create().cuboid(-0.6F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F, new Dilation(0.3F, 0.3F, 0.3F)), ModelTransform.of(0.0F, -2.2F, 0.0F, -0.1745F, 0.0F, 0.2618F));
		ModelPartData lHorn03 = lHorn02.addChild("lHorn03", ModelPartBuilder.create().uv(0, 4).cuboid(-0.7F, -3.0F, -0.6F, 1.0F, 3.0F, 1.0F, new Dilation(0.15F, 0.15F, 0.15F)), ModelTransform.of(0.0F, -2.7F, 0.0F, -0.1047F, 0.0F, 0.1745F));
		ModelPartData lHorn04 = lHorn03.addChild("lHorn04", ModelPartBuilder.create().cuboid(-0.5F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F), ModelTransform.of(0.0F, -2.8F, 0.0F, 0.0524F, 0.0F, 0.1396F));
		lHorn04.addChild("lHorn05", ModelPartBuilder.create().cuboid(-0.5F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F), ModelTransform.of(0.0F, -2.7F, 0.0F, 0.0524F, 0.0F, -0.1396F));
		realHead.addChild("snout", ModelPartBuilder.create().uv(29, 2).cuboid(-2.0F, -1.9F, -5.1F, 4.0F, 3.0F, 5.0F), ModelTransform.of(0.0F, -4.6F, -2.5F, 0.5236F, 0.0F, 0.0F));
		realHead.addChild("jawUpper00", ModelPartBuilder.create().uv(43, 11).cuboid(-2.5F, -1.0F, -5.0F, 5.0F, 2.0F, 5.0F), ModelTransform.of(0.0F, -2.5F, -2.2F, 0.0698F, 0.0F, 0.0F));
		ModelPartData jawLower = realHead.addChild("jawLower", ModelPartBuilder.create().uv(48, 5).cuboid(-2.0F, -0.5F, -4.0F, 4.0F, 1.0F, 4.0F), ModelTransform.of(0.0F, -1.0F, -3.0F, -0.0349F, 0.0F, 0.0F));
		jawLower.addChild("beard", ModelPartBuilder.create().uv(18, 59).cuboid(-1.5F, 0.0F, -1.0F, 3.0F, 3.0F, 2.0F), ModelTransform.of(0.0F, 0.3F, -2.4F, -0.0349F, 0.0F, 0.0F));
		realHead.addChild("lEar", ModelPartBuilder.create().uv(48, 0).cuboid(0.0F, -0.5F, -1.0F, 4.0F, 1.0F, 2.0F), ModelTransform.of(2.6F, -6.0F, 0.8F, -0.3491F, 0.0F, 0.3142F));
		realHead.addChild("rEar", ModelPartBuilder.create().uv(48, 0).mirrored(true).cuboid(-4.0F, -0.5F, -1.0F, 4.0F, 1.0F, 2.0F), ModelTransform.of(-2.6F, -6.0F, 0.8F, -0.3491F, 0.0F, -0.3142F));
		ModelPartData torch00 = realHead.addChild("torch00", ModelPartBuilder.create().uv(117, 0).cuboid(-1.0F, -2.5F, -1.0F, 2.0F, 3.0F, 2.0F), ModelTransform.of(0.0F, -6.6F, -1.2F, 0.0F, -0.7854F, 0.0F));
		ModelPartData torch01a = torch00.addChild("torch01a", ModelPartBuilder.create().uv(117, 6).cuboid(-0.8F, -3.8F, -0.8F, 1.0F, 4.0F, 1.0F), ModelTransform.of(0.0F, -2.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		torch01a.addChild("torch01b", ModelPartBuilder.create().uv(117, 6).cuboid(-0.2F, -3.8F, -0.8F, 1.0F, 4.0F, 1.0F), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		torch01a.addChild("torch01c", ModelPartBuilder.create().uv(117, 6).cuboid(-0.8F, -3.8F, -0.2F, 1.0F, 4.0F, 1.0F), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		torch01a.addChild("torch01d", ModelPartBuilder.create().uv(117, 6).cuboid(-0.2F, -3.8F, -0.2F, 1.0F, 4.0F, 1.0F), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		ModelPartData torch02a = torch01a.addChild("torch02a", ModelPartBuilder.create().uv(115, 12).cuboid(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F), ModelTransform.of(0.0F, -3.6F, 0.0F, 0.0F, 0.0F, 0.0F));
		torch02a.addChild("torch03a", ModelPartBuilder.create().uv(122, 6).mirrored(true).cuboid(-0.5F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F), ModelTransform.of(-0.6F, -1.5F, -0.6F, 0.2443F, -0.1396F, -0.2793F));
		torch02a.addChild("torch03b", ModelPartBuilder.create().uv(122, 6).cuboid(-0.5F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F), ModelTransform.of(0.6F, -1.5F, -0.6F, 0.2443F, 0.2094F, 0.2793F));
		torch02a.addChild("torch03d", ModelPartBuilder.create().uv(122, 10).mirrored(true).cuboid(-0.5F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F), ModelTransform.of(-0.6F, -1.5F, 0.6F, -0.2443F, 0.2094F, -0.2793F));
		torch02a.addChild("torch03c", ModelPartBuilder.create().uv(122, 10).cuboid(-0.5F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F), ModelTransform.of(0.6F, -1.5F, 0.6F, -0.2443F, -0.1396F, 0.2793F));
		torch02a.addChild("flame00", ModelPartBuilder.create().uv(89, 10).cuboid(0.0F, -6.2F, -2.0F, 0.0F, 6.0F, 4.0F), ModelTransform.of(0.0F, -1.6F, 0.0F, 0.0F, 0.0F, 0.0F));
		torch02a.addChild("flame01", ModelPartBuilder.create().uv(89, 17).cuboid(0.0F, -6.2F, -2.0F, 0.0F, 6.0F, 4.0F), ModelTransform.of(0.0F, -1.6F, 0.0F, 0.0F, -1.5708F, 0.0F));
		realHead.addChild("lCheekFur", ModelPartBuilder.create().uv(31, 54).cuboid(0.0F, -2.5F, 0.0F, 5.0F, 5.0F, 0.0F), ModelTransform.of(2.4F, -2.4F, -0.3F, -0.6981F, -0.3491F, 0.576F));
		realHead.addChild("rCheekFur", ModelPartBuilder.create().uv(31, 54).mirrored(true).cuboid(-5.0F, -2.5F, 0.0F, 5.0F, 5.0F, 0.0F), ModelTransform.of(-2.4F, -2.4F, -0.3F, -0.6981F, 0.3491F, -0.576F));
		root.addChild("BipedLeftArm", ModelPartBuilder.create().uv(46, 19).cuboid(-1.0F, -2.0F, -2.0F, 3.0F, 16.0F, 4.0F), ModelTransform.of(5.0F, -10.8F, 0.0F, 0.0F, 0.0F, -0.1F));
		return TexturedModelData.of(data, 128, 64);
	}

	@Override
	public void setAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float realHeadYaw, float realHeadPitch) {
		entity.setStackInHand(Hand.MAIN_HAND, CADUCEUS);
		realArm = false;
		super.setAngles(entity, limbAngle, limbDistance, animationProgress, realHeadYaw, realHeadPitch);
		realArm = true;
		copyRotation(realHead, super.head);
		copyRotation(realBody, super.body);
		copyRotation(BipedLeftArm, super.leftArm);
		BipedLeftArm.roll -= 0.1f;
		copyRotation(BipedRightArm, super.rightArm);
		BipedRightArm.roll += 0.1f;
		copyRotation(BipedLeftLeg, super.leftLeg);
		BipedLeftLeg.pitch /= 2;
		BipedLeftLeg.pitch -= 0.2793f;
		BipedLeftLeg.roll -= 0.096f;
		copyRotation(BipedRightLeg, super.rightLeg);
		BipedRightLeg.pitch /= 2;
		BipedRightLeg.pitch -= 0.2793f;
		BipedRightLeg.roll += 0.096f;
		lWing01.yaw = MathHelper.cos(animationProgress / 16) / 3 + 1 / 3f;
		rWing01.yaw = -lWing01.yaw;
		frontLoincloth.pitch = Math.min(BipedLeftLeg.pitch, BipedRightLeg.pitch) + 1 / 3f - 0.3491f;
		backLoincloth.pitch = Math.max(BipedLeftLeg.pitch, BipedRightLeg.pitch) + 1 / 3.75f;
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
		realHead.render(matrices, vertices, light, overlay, red, green, blue, alpha);
		realBody.render(matrices, vertices, light, overlay, red, green, blue, alpha);
		BipedLeftArm.render(matrices, vertices, light, overlay, red, green, blue, alpha);
		BipedRightArm.render(matrices, vertices, light, overlay, red, green, blue, alpha);
		BipedLeftLeg.render(matrices, vertices, light, overlay, red, green, blue, alpha);
		BipedRightLeg.render(matrices, vertices, light, overlay, red, green, blue, alpha);
	}

	@Override
	protected ModelPart getArm(Arm arm) {
		return realArm ? (arm == Arm.LEFT ? BipedLeftArm : BipedRightArm) : super.getArm(arm);
	}

	private void copyRotation(ModelPart to, ModelPart from) {
		to.pitch = from.pitch;
		to.yaw = from.yaw;
		to.roll = from.roll;
	}
}
