/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.bewitchment.client.model.entity.living;

import moriyashiine.bewitchment.common.entity.living.LilithEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Arm;
import net.minecraft.util.math.MathHelper;

@Environment(EnvType.CLIENT)
public class LilithEntityModel<T extends LilithEntity> extends BipedEntityModel<T> {
	private final ModelPart realBody;
	private final ModelPart frontLoincloth;
	private final ModelPart backLoincloth;
	private final ModelPart lWing01;
	private final ModelPart rWing01;
	private final ModelPart bipedLeftArm;
	private final ModelPart bipedRightArm;
	private final ModelPart bipedLeftLeg;
	private final ModelPart bipedRightLeg;
	private final ModelPart realHead;

	private boolean realArm = false;

	public LilithEntityModel(ModelPart root) {
		super(root);
		realBody = root.getChild("realBody");
		frontLoincloth = realBody.getChild("stomach").getChild("hips").getChild("frontLoincloth");
		backLoincloth = realBody.getChild("stomach").getChild("hips").getChild("backLoincloth");
		rWing01 = realBody.getChild("rWing01");
		lWing01 = realBody.getChild("lWing01");
		bipedLeftArm = root.getChild("bipedLeftArm");
		bipedRightArm = root.getChild("bipedRightArm");
		bipedLeftLeg = root.getChild("bipedLeftLeg");
		bipedRightLeg = root.getChild("bipedRightLeg");
		realHead = root.getChild("realHead");
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData data = BipedEntityModel.getModelData(Dilation.NONE, 0);
		ModelPartData root = data.getRoot();
		ModelPartData realBody = root.addChild("realBody", ModelPartBuilder.create().uv(20, 16).cuboid(-4.0F, 0.0F, -2.5F, 8.0F, 6.0F, 5.0F), ModelTransform.of(0.0F, -12.0F, 1.2F, 0.0F, 0.0F, 0.0F));
		ModelPartData stomach = realBody.addChild("stomach", ModelPartBuilder.create().uv(19, 28).cuboid(-3.5F, 0.0F, -2.5F, 7.0F, 6.0F, 5.0F), ModelTransform.of(0.0F, 6.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		ModelPartData hips = stomach.addChild("hips", ModelPartBuilder.create().uv(19, 39).cuboid(-4.0F, 0.0F, -2.5F, 8.0F, 3.0F, 5.0F), ModelTransform.of(0.0F, 5.9F, 0.0F, 0.0F, 0.0F, 0.0F));
		hips.addChild("frontLoincloth", ModelPartBuilder.create().uv(73, 45).cuboid(-5.6F, 0.0F, 0.0F, 10.0F, 14.0F, 5.0F), ModelTransform.of(0.0F, 0.0F, -2.7F, -0.0698F, 0.0F, 0.0F));
		hips.addChild("backLoincloth", ModelPartBuilder.create().uv(103, 45).cuboid(-5.5F, 0.0F, -1.0F, 9.0F, 12.0F, 2.0F), ModelTransform.of(0.0F, 0.1F, 1.8F, 0.0F, 0.0F, 0.0F));
		ModelPartData boobs = realBody.addChild("boobs", ModelPartBuilder.create().uv(19, 48).cuboid(-3.5F, 0.0F, -2.0F, 7.0F, 4.0F, 4.0F), ModelTransform.of(0.0F, 1.9F, -0.8F, -0.6981F, 0.0F, 0.0F));
		boobs.addChild("boobWrap", ModelPartBuilder.create().uv(20, 59).cuboid(-3.5F, 0.27F, 0.15F, 7.0F, 0.0F, 4.0F), ModelTransform.of(0.0F, 3.7F, -1.9F, -0.8727F, 0.0F, 0.0F));
		ModelPartData lWing01 = realBody.addChild("lWing01", ModelPartBuilder.create().uv(55, 40).mirrored(true).cuboid(-1.0F, -1.5F, 0.0F, 2.0F, 3.0F, 6.0F), ModelTransform.of(1.8F, 2.8F, 1.4F, 0.3491F, 0.6458F, 0.0F));
		ModelPartData lWing02 = lWing01.addChild("lWing02", ModelPartBuilder.create().uv(41, 41).mirrored(true).cuboid(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 9.0F), ModelTransform.of(0.1F, 0.2F, 5.6F, 0.5236F, 0.1396F, 0.0F));
		ModelPartData lWing03 = lWing02.addChild("lWing03", ModelPartBuilder.create().uv(42, 53).mirrored(true).cuboid(-1.0F, 0.0F, -1.0F, 2.0F, 9.0F, 2.0F), ModelTransform.of(0.1F, 0.4F, 8.0F, -0.1745F, 0.0F, 0.0F));
		ModelPartData lWing04 = lWing03.addChild("lWing04", ModelPartBuilder.create().uv(51, 52).mirrored(true).cuboid(-0.5F, 0.0F, -0.5F, 1.0F, 11.0F, 1.0F), ModelTransform.of(0.0F, 8.6F, 0.2F, -0.4712F, 0.0F, 0.0F));
		lWing04.addChild("lFeathers03", ModelPartBuilder.create().uv(60, 0).mirrored(true).cuboid(0.0F, 0.0F, -13.0F, 0.0F, 20.0F, 13.0F), ModelTransform.of(0.0F, 2.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		lWing03.addChild("lFeathers02", ModelPartBuilder.create().uv(90, 0).mirrored(true).cuboid(-0.5F, -0.6F, -13.7F, 1.0F, 16.0F, 16.0F), ModelTransform.of(-0.1F, 2.3F, 0.0F, -0.4363F, 0.0F, 0.0F));
		lWing02.addChild("lFeathers01", ModelPartBuilder.create().uv(65, 13).mirrored(true).cuboid(-0.5F, -0.6F, -11.1F, 1.0F, 10.0F, 22.0F), ModelTransform.of(0.2F, 0.0F, 0.0F, -0.1745F, 0.0F, 0.0F));
		ModelPartData rWing01 = realBody.addChild("rWing01", ModelPartBuilder.create().uv(55, 40).mirrored(true).cuboid(-1.0F, -1.5F, 0.0F, 2.0F, 3.0F, 6.0F), ModelTransform.of(-1.8F, 2.8F, 1.4F, 0.3491F, -0.6458F, 0.0F));
		ModelPartData rWing02 = rWing01.addChild("rWing02", ModelPartBuilder.create().uv(41, 41).mirrored(true).cuboid(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 9.0F), ModelTransform.of(-0.1F, 0.2F, 5.6F, 0.5236F, -0.1396F, 0.0F));
		ModelPartData rWing03 = rWing02.addChild("rWing03", ModelPartBuilder.create().uv(42, 53).mirrored(true).cuboid(-1.0F, 0.0F, -1.0F, 2.0F, 9.0F, 2.0F), ModelTransform.of(-0.1F, 0.4F, 8.0F, -0.1745F, 0.0F, 0.0F));
		ModelPartData rWing04 = rWing03.addChild("rWing04", ModelPartBuilder.create().uv(51, 52).mirrored(true).cuboid(-0.5F, 0.0F, -0.5F, 1.0F, 11.0F, 1.0F), ModelTransform.of(0.0F, 8.6F, 0.2F, -0.4712F, 0.0F, 0.0F));
		rWing04.addChild("rFeathers03", ModelPartBuilder.create().uv(60, 0).mirrored(true).cuboid(0.0F, 0.0F, -13.0F, 0.0F, 20.0F, 13.0F), ModelTransform.of(0.0F, 2.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		rWing03.addChild("rFeathers02", ModelPartBuilder.create().uv(90, 0).mirrored(true).cuboid(-0.5F, -0.6F, -13.7F, 1.0F, 16.0F, 16.0F), ModelTransform.of(0.1F, 2.3F, 0.0F, -0.4363F, 0.0F, 0.0F));
		rWing02.addChild("rFeathers01", ModelPartBuilder.create().uv(65, 13).mirrored(true).cuboid(-0.5F, -0.6F, -11.1F, 1.0F, 10.0F, 22.0F), ModelTransform.of(-0.2F, 0.0F, 0.0F, -0.1745F, 0.0F, 0.0F));
		ModelPartData bipedLeftLeg = root.addChild("bipedLeftLeg", ModelPartBuilder.create().uv(0, 16).cuboid(-2.5F, -1.0F, -2.5F, 5.0F, 12.0F, 5.0F), ModelTransform.of(2.3F, 1.9F, 1.2F, 0.0F, 0.0F, 0.0F));
		ModelPartData lLeg02 = bipedLeftLeg.addChild("lLeg02", ModelPartBuilder.create().uv(0, 34).cuboid(-2.0F, -1.2F, -2.0F, 4.0F, 12.0F, 4.0F), ModelTransform.of(0.0F, 11.5F, -0.2F, 0.0873F, 0.0F, 0.0349F));
		lLeg02.addChild("lTalon01", ModelPartBuilder.create().uv(0, 52).cuboid(-0.5F, -0.5F, -4.0F, 1.0F, 2.0F, 4.0F), ModelTransform.of(1.1F, 9.7F, -1.4F, 0.0349F, -0.1745F, 0.0F));
		lLeg02.addChild("lTalon02", ModelPartBuilder.create().uv(0, 52).cuboid(-0.5F, -0.5F, -4.0F, 1.0F, 2.0F, 4.0F), ModelTransform.of(0.0F, 9.7F, -1.7F, 0.0349F, 0.0F, 0.0F));
		lLeg02.addChild("lTalon03", ModelPartBuilder.create().uv(0, 52).cuboid(-0.5F, -0.5F, -4.0F, 1.0F, 2.0F, 4.0F), ModelTransform.of(-1.1F, 9.7F, -1.4F, 0.0349F, 0.1745F, 0.0F));
		lLeg02.addChild("lTalon04", ModelPartBuilder.create().uv(0, 58).cuboid(-0.5F, -0.5F, 0.0F, 1.0F, 2.0F, 4.0F), ModelTransform.of(0.0F, 9.7F, 1.6F, -0.0349F, 0.0F, 0.0F));
		ModelPartData bipedRightArm = root.addChild("bipedRightArm", ModelPartBuilder.create().uv(46, 16).mirrored(true).cuboid(-2.0F, -2.0F, -2.0F, 3.0F, 16.0F, 4.0F), ModelTransform.of(-4.5F, -10.1F, 1.2F, 0.0F, 0.0F, 0.0F));
		bipedRightArm.addChild("rClaw01", ModelPartBuilder.create().uv(11, 53).mirrored(true).cuboid(-2.0F, 0.0F, -0.5F, 2.0F, 5.0F, 1.0F), ModelTransform.of(-0.6F, 11.2F, -1.6F, 0.0F, 0.0F, -0.1745F));
		bipedRightArm.addChild("rClaw02", ModelPartBuilder.create().uv(11, 53).mirrored(true).cuboid(-2.0F, 0.0F, -0.5F, 2.0F, 5.0F, 1.0F), ModelTransform.of(-0.6F, 11.8F, -0.5F, 0.0F, 0.0F, -0.1222F));
		bipedRightArm.addChild("rClaw03", ModelPartBuilder.create().uv(11, 53).mirrored(true).cuboid(-2.0F, 0.0F, -0.5F, 2.0F, 5.0F, 1.0F), ModelTransform.of(-0.6F, 11.8F, 0.6F, 0.0F, 0.0F, -0.1222F));
		bipedRightArm.addChild("rClaw04", ModelPartBuilder.create().uv(11, 53).mirrored(true).cuboid(-2.0F, 0.0F, -0.5F, 2.0F, 5.0F, 1.0F), ModelTransform.of(-0.6F, 11.2F, 1.6F, 0.0F, 0.0F, -0.1745F));
		ModelPartData bipedLeftArm = root.addChild("bipedLeftArm", ModelPartBuilder.create().uv(46, 16).cuboid(-1.0F, -2.0F, -2.0F, 3.0F, 16.0F, 4.0F), ModelTransform.of(4.5F, -10.1F, 1.2F, 0.0F, 0.0F, 0.0F));
		bipedLeftArm.addChild("lClaw01", ModelPartBuilder.create().uv(11, 53).cuboid(0.0F, 0.0F, -0.5F, 2.0F, 5.0F, 1.0F), ModelTransform.of(0.6F, 11.2F, -1.6F, 0.0F, 0.0F, 0.1745F));
		bipedLeftArm.addChild("lClaw02", ModelPartBuilder.create().uv(11, 53).cuboid(0.0F, 0.0F, -0.5F, 2.0F, 5.0F, 1.0F), ModelTransform.of(0.6F, 11.8F, -0.5F, 0.0F, 0.0F, 0.1222F));
		bipedLeftArm.addChild("lClaw03", ModelPartBuilder.create().uv(11, 53).cuboid(0.0F, 0.0F, -0.5F, 2.0F, 5.0F, 1.0F), ModelTransform.of(0.6F, 11.8F, 0.6F, 0.0F, 0.0F, 0.1222F));
		bipedLeftArm.addChild("lClaw04", ModelPartBuilder.create().uv(11, 53).cuboid(0.0F, 0.0F, -0.5F, 2.0F, 5.0F, 1.0F), ModelTransform.of(0.6F, 11.2F, 1.6F, 0.0F, 0.0F, 0.1745F));
		ModelPartData realHead = root.addChild("realHead", ModelPartBuilder.create().cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F), ModelTransform.of(0.0F, -12.0F, 1.2F, 0.0F, 0.0F, 0.0F));
		ModelPartData lHorn01 = realHead.addChild("lHorn01", ModelPartBuilder.create().uv(109, 0).mirrored(true).cuboid(-1.5F, -4.0F, -1.5F, 3.0F, 4.0F, 3.0F), ModelTransform.of(2.3F, -6.9F, -1.3F, -0.2618F, 0.0F, 0.5236F));
		ModelPartData lHorn02 = lHorn01.addChild("lHorn02", ModelPartBuilder.create().uv(109, 7).mirrored(true).cuboid(-1.0F, -3.0F, -1.0F, 2.0F, 3.0F, 2.0F), ModelTransform.of(0.0F, -3.5F, 0.0F, -0.4538F, 0.0F, -0.1745F));
		ModelPartData lHorn03 = lHorn02.addChild("lHorn03", ModelPartBuilder.create().uv(118, 7).mirrored(true).cuboid(-0.99F, -3.0F, -0.99F, 2.0F, 3.0F, 2.0F), ModelTransform.of(0.0F, -2.5F, 0.0F, -0.4363F, 0.0F, 0.0F));
		ModelPartData lHorn04a = lHorn03.addChild("lHorn04a", ModelPartBuilder.create().mirrored(true).cuboid(-0.7F, -3.0F, -0.7F, 1.0F, 3.0F, 1.0F), ModelTransform.of(0.2F, -2.7F, -0.3F, -0.6981F, 0.0F, -0.4014F));
		lHorn04a.addChild("lHorn04b", ModelPartBuilder.create().mirrored(true).cuboid(-0.3F, -3.0F, -0.7F, 1.0F, 3.0F, 1.0F), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		lHorn04a.addChild("lHorn04c", ModelPartBuilder.create().mirrored(true).cuboid(-0.7F, -3.0F, -0.3F, 1.0F, 3.0F, 1.0F), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		ModelPartData lHorn04d = lHorn04a.addChild("lHorn04d", ModelPartBuilder.create().mirrored(true).cuboid(-0.3F, -3.0F, -0.3F, 1.0F, 3.0F, 1.0F), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		ModelPartData lHorn05 = lHorn04d.addChild("lHorn05", ModelPartBuilder.create().uv(0, 4).mirrored(true).cuboid(-0.5F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F), ModelTransform.of(0.0F, -2.8F, 0.0F, -0.4363F, 0.0F, 0.2269F));
		lHorn05.addChild("lHorn06", ModelPartBuilder.create().mirrored(true).cuboid(-0.5F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F), ModelTransform.of(0.0F, -2.7F, 0.0F, 0.4887F, 0.0F, 0.2269F));
		ModelPartData rHorn01 = realHead.addChild("rHorn01", ModelPartBuilder.create().uv(109, 0).mirrored(true).cuboid(-1.5F, -4.0F, -1.5F, 3.0F, 4.0F, 3.0F), ModelTransform.of(-2.3F, -6.9F, -1.3F, -0.2618F, 0.0F, -0.5236F));
		ModelPartData rHorn02 = rHorn01.addChild("rHorn02", ModelPartBuilder.create().uv(109, 7).mirrored(true).cuboid(-1.0F, -3.0F, -1.0F, 2.0F, 3.0F, 2.0F), ModelTransform.of(0.0F, -3.5F, 0.0F, -0.4538F, 0.0F, 0.1745F));
		ModelPartData rHorn03 = rHorn02.addChild("rHorn03", ModelPartBuilder.create().uv(118, 7).mirrored(true).cuboid(-1.01F, -3.0F, -0.99F, 2.0F, 3.0F, 2.0F), ModelTransform.of(0.0F, -2.5F, 0.0F, -0.4363F, 0.0F, 0.0F));
		ModelPartData rHorn04a = rHorn03.addChild("rHorn04a", ModelPartBuilder.create().mirrored(true).cuboid(-0.7F, -3.0F, -0.7F, 1.0F, 3.0F, 1.0F), ModelTransform.of(-0.2F, -2.7F, 0.3F, -0.6981F, 0.0F, 0.4014F));
		rHorn04a.addChild("rHorn04b", ModelPartBuilder.create().mirrored(true).cuboid(-0.3F, -3.0F, -0.7F, 1.0F, 3.0F, 1.0F), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		rHorn04a.addChild("rHorn04c", ModelPartBuilder.create().mirrored(true).cuboid(-0.7F, -3.0F, -0.3F, 1.0F, 3.0F, 1.0F), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		ModelPartData rHorn04d = rHorn04a.addChild("rHorn04d", ModelPartBuilder.create().mirrored(true).cuboid(-0.3F, -3.0F, -0.3F, 1.0F, 3.0F, 1.0F), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		ModelPartData rHorn05 = rHorn04d.addChild("rHorn05", ModelPartBuilder.create().uv(0, 4).mirrored(true).cuboid(-0.5F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F), ModelTransform.of(0.0F, -2.8F, 0.0F, -0.4363F, 0.0F, -0.2269F));
		rHorn05.addChild("rHorn06", ModelPartBuilder.create().mirrored(true).cuboid(-0.5F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F), ModelTransform.of(0.0F, -2.7F, 0.0F, 0.4887F, 0.0F, -0.2269F));
		realHead.addChild("lHair", ModelPartBuilder.create().uv(45, 0).cuboid(-2.0F, 0.0F, -1.5F, 2.0F, 10.0F, 3.0F), ModelTransform.of(-2.4F, -6.3F, 1.1F, 0.3491F, 0.0F, 0.3142F));
		realHead.addChild("rHair", ModelPartBuilder.create().uv(45, 0).cuboid(0.0F, 0.0F, -1.5F, 2.0F, 10.0F, 3.0F), ModelTransform.of(2.4F, -6.3F, 1.1F, 0.3491F, 0.0F, -0.3142F));
		ModelPartData crown00 = realHead.addChild("crown00", ModelPartBuilder.create().uv(27, 0).cuboid(-3.0F, -2.5F, 0.0F, 6.0F, 3.0F, 1.0F), ModelTransform.of(0.0F, -8.0F, -3.5F, 0.0F, 0.0F, 0.0F));
		ModelPartData crownL01 = crown00.addChild("crownL01", ModelPartBuilder.create().uv(27, 0).mirrored(true).cuboid(-0.2F, -1.5F, -0.49F, 2.0F, 3.0F, 1.0F), ModelTransform.of(2.3F, -1.0F, 0.5F, 0.0F, 0.0F, -0.6981F));
		ModelPartData crownL02 = crownL01.addChild("crownL02", ModelPartBuilder.create().uv(27, 0).mirrored(true).cuboid(-0.7F, -1.0F, -0.5F, 3.0F, 2.0F, 1.0F), ModelTransform.of(1.5F, -0.1F, 0.1F, 0.0F, 0.0F, -0.4189F));
		ModelPartData crownL03 = crownL02.addChild("crownL03", ModelPartBuilder.create().uv(27, 0).mirrored(true).cuboid(-0.7F, -1.0F, -0.49F, 3.0F, 2.0F, 1.0F), ModelTransform.of(2.2F, -0.2F, 0.0F, 0.0F, 0.0F, -0.6109F));
		ModelPartData crownL04 = crownL03.addChild("crownL04", ModelPartBuilder.create().uv(27, 0).mirrored(true).cuboid(-0.1F, -0.5F, -0.5F, 3.0F, 1.0F, 1.0F), ModelTransform.of(2.0F, -0.3F, 0.0F, 0.0F, 0.0F, -0.4363F));
		crownL04.addChild("crownL05", ModelPartBuilder.create().uv(27, 0).mirrored(true).cuboid(-0.7F, -0.5F, -0.5F, 3.0F, 1.0F, 1.0F), ModelTransform.of(0.1F, 0.6F, 0.0F, 0.0F, 0.0F, -0.2094F));
		ModelPartData crownR01 = crown00.addChild("crownR01", ModelPartBuilder.create().uv(27, 0).mirrored(true).cuboid(-1.8F, -1.5F, -0.49F, 2.0F, 3.0F, 1.0F), ModelTransform.of(-2.3F, -1.0F, 0.5F, 0.0F, 0.0F, 0.6981F));
		ModelPartData crownR02 = crownR01.addChild("crownR02", ModelPartBuilder.create().uv(27, 0).mirrored(true).cuboid(-2.3F, -1.0F, -0.5F, 3.0F, 2.0F, 1.0F), ModelTransform.of(-1.5F, -0.1F, 0.1F, 0.0F, 0.0F, 0.4189F));
		ModelPartData crownR03 = crownR02.addChild("crownR03", ModelPartBuilder.create().uv(27, 0).mirrored(true).cuboid(-2.3F, -1.0F, -0.49F, 3.0F, 2.0F, 1.0F), ModelTransform.of(-2.2F, -0.2F, 0.0F, 0.0F, 0.0F, 0.6109F));
		ModelPartData crownR04 = crownR03.addChild("crownR04", ModelPartBuilder.create().uv(27, 0).mirrored(true).cuboid(-2.9F, -0.5F, -0.5F, 3.0F, 1.0F, 1.0F), ModelTransform.of(-2.0F, -0.3F, 0.0F, 0.0F, 0.0F, 0.4363F));
		crownR04.addChild("crownR05", ModelPartBuilder.create().uv(27, 0).mirrored(true).cuboid(-2.3F, -0.5F, -0.5F, 3.0F, 1.0F, 1.0F), ModelTransform.of(-0.1F, 0.6F, 0.0F, 0.0F, 0.0F, 0.2094F));
		ModelPartData HAIR = realHead.addChild("HAIR", ModelPartBuilder.create().uv(56, 0).cuboid(-4.0F, 6.0F, 1.5F, 8.0F, 11.0F, 1.0F, new Dilation(0.1F, 0.1F, 0.1F)), ModelTransform.of(0.0F, -8.0F, 1.8F, 0.0F, 0.0F, 0.0F));
		HAIR.addChild("base_r1", ModelPartBuilder.create().uv(85, 0).cuboid(-5.0F, -2.0F, -1.0F, 6.0F, 10.0F, 6.0F, new Dilation(0.1F, 0.1F, 0.1F)), ModelTransform.of(0.0F, 3.0F, -3.0F, 0.4363F, 0.7854F, 0.3054F));
		ModelPartData bipedRightLeg = root.addChild("bipedRightLeg", ModelPartBuilder.create().uv(0, 16).mirrored(true).cuboid(-2.5F, -1.0F, -2.5F, 5.0F, 12.0F, 5.0F), ModelTransform.of(-2.3F, 1.9F, 1.2F, 0.0F, 0.0F, 0.0F));
		ModelPartData rLeg02 = bipedRightLeg.addChild("rLeg02", ModelPartBuilder.create().uv(0, 34).mirrored(true).cuboid(-2.0F, -1.2F, -2.0F, 4.0F, 12.0F, 4.0F), ModelTransform.of(0.0F, 11.5F, -0.2F, 0.0873F, 0.0F, -0.0349F));
		rLeg02.addChild("rTalon01", ModelPartBuilder.create().uv(0, 52).mirrored(true).cuboid(-0.5F, -0.5F, -4.0F, 1.0F, 2.0F, 4.0F), ModelTransform.of(-1.1F, 9.7F, -1.4F, 0.0349F, 0.1745F, 0.0F));
		rLeg02.addChild("rTalon02", ModelPartBuilder.create().uv(0, 52).mirrored(true).cuboid(-0.5F, -0.5F, -4.0F, 1.0F, 2.0F, 4.0F), ModelTransform.of(0.0F, 9.7F, -1.7F, 0.0349F, 0.0F, 0.0F));
		rLeg02.addChild("rTalon03", ModelPartBuilder.create().uv(0, 52).mirrored(true).cuboid(-0.5F, -0.5F, -4.0F, 1.0F, 2.0F, 4.0F), ModelTransform.of(1.1F, 9.7F, -1.4F, 0.0349F, -0.1745F, 0.0F));
		rLeg02.addChild("rTalon04", ModelPartBuilder.create().uv(0, 58).mirrored(true).cuboid(-0.5F, -0.5F, 0.0F, 1.0F, 2.0F, 4.0F), ModelTransform.of(0.0F, 9.7F, 1.6F, -0.0349F, 0.0F, 0.0F));
		return TexturedModelData.of(data, 128, 64);
	}

	@Override
	public void setAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float realHeadYaw, float realHeadPitch) {
		realArm = false;
		super.setAngles(entity, limbAngle, limbDistance, animationProgress, realHeadYaw, realHeadPitch);
		realArm = true;
		copyRotation(realHead, super.head);
		copyRotation(realBody, super.body);
		copyRotation(bipedLeftArm, super.leftArm);
		bipedLeftArm.roll -= 0.1f;
		copyRotation(bipedRightArm, super.rightArm);
		bipedRightArm.roll += 0.1f;
		copyRotation(bipedLeftLeg, super.leftLeg);
		bipedLeftLeg.pitch /= 2;
		bipedLeftLeg.pitch -= 0.0698f;
		bipedLeftLeg.roll -= 0.0349f;
		copyRotation(bipedRightLeg, super.rightLeg);
		bipedRightLeg.pitch /= 2;
		bipedRightLeg.pitch -= 0.0698f;
		bipedRightLeg.roll += 0.0349f;
		lWing01.yaw = MathHelper.cos(animationProgress / 16) / 3 + 1 / 3f;
		rWing01.yaw = -lWing01.yaw;
		frontLoincloth.pitch = Math.min(bipedLeftLeg.pitch, bipedRightLeg.pitch);
		backLoincloth.pitch = Math.max(bipedLeftLeg.pitch, bipedRightLeg.pitch);
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
		realHead.render(matrices, vertices, light, overlay, red, green, blue, alpha);
		realBody.render(matrices, vertices, light, overlay, red, green, blue, alpha);
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
