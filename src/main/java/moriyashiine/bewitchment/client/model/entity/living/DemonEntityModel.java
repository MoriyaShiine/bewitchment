package moriyashiine.bewitchment.client.model.entity.living;

import moriyashiine.bewitchment.common.entity.living.DemonEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Arm;
import net.minecraft.util.math.MathHelper;

@Environment(EnvType.CLIENT)
public class DemonEntityModel<T extends DemonEntity> extends BipedEntityModel<T> {
    private final ModelPart realBody;
    private final ModelPart tail01;
    private final ModelPart lWing01;
    private final ModelPart rWing01;
    private final ModelPart BipedLeftArm;
    private final ModelPart BipedRightArm;
    private final ModelPart BipedLeftLeg;
    private final ModelPart BipedRightLeg;
    private final ModelPart realHead;

    private boolean realArm = false;

    public DemonEntityModel(ModelPart root) {
        super(root);
        realBody = root.getChild("realBody");
        tail01 = realBody.getChild("tail01");
        lWing01 = realBody.getChild("lWing01");
        rWing01 = realBody.getChild("rWing01");
        BipedLeftArm = root.getChild("BipedLeftArm");
        BipedRightArm = root.getChild("BipedRightArm");
        BipedLeftLeg = root.getChild("BipedLeftLeg");
        BipedRightLeg = root.getChild("BipedRightLeg");
        realHead = root.getChild("realHead");
    }

    public static TexturedModelData getTexturedModelDataMale() {
        ModelData data = BipedEntityModel.getModelData(Dilation.NONE, 0);
        ModelPartData root = data.getRoot();
        ModelPartData realBody = root.addChild("realBody",
                ModelPartBuilder.create()
                        .uv(19, 17).cuboid(-4.0F, 0.0F, -2.0F, 8.0F, 13.0F, 4.0F),
                ModelTransform.of(0.0F, -4.4F, 0.0F, 0.0F, 0.0F, 0.0F));
        ModelPartData tail01 = realBody.addChild("tail01",
                ModelPartBuilder.create()
                        .uv(13, 37).cuboid(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 4.0F),
                ModelTransform.of(0.0F, 10.1F, 1.3F, -0.8727F, 0.0F, 0.0F));
        ModelPartData tail02 = tail01.addChild("tail02",
                ModelPartBuilder.create()
                        .uv(13, 37).cuboid(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 4.0F),
                ModelTransform.of(0.0F, 0.0F, 3.8F, -0.1396F, 0.0F, 0.0F));
        ModelPartData tail03 = tail02.addChild("tail03",
                ModelPartBuilder.create()
                        .uv(15, 45).cuboid(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 5.0F),
                ModelTransform.of(0.0F, 0.0F, 2.9F, 0.0698F, 0.0F, 0.0F));
        ModelPartData tail04 = tail03.addChild("tail04",
                ModelPartBuilder.create()
                        .uv(15, 45).cuboid(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 5.0F),
                ModelTransform.of(0.0F, 0.0F, 4.9F, 0.1396F, 0.0F, 0.0F));
        ModelPartData tail05 = tail04.addChild("tail05",
                ModelPartBuilder.create()
                        .uv(15, 45).cuboid(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 5.0F),
                ModelTransform.of(0.0F, 0.0F, 4.9F, 0.2269F, 0.0F, 0.0F));
        ModelPartData tailTip01 = tail05.addChild("tailTip01",
                ModelPartBuilder.create()
                        .uv(16, 53).cuboid(-1.0F, -0.5F, 0.0F, 2.0F, 1.0F, 2.0F),
                ModelTransform.of(0.0F, 0.0F, 4.5F, 0.2618F, 0.0F, 0.0F));
        tailTip01.addChild("tailTip02",
                ModelPartBuilder.create()
                        .uv(15, 58).cuboid(-0.5F, -0.5F, -0.5F, 2.0F, 1.0F, 2.0F),
                ModelTransform.of(0.0F, 0.1F, 0.8F, 0.0F, -0.7854F, 0.0F));
        ModelPartData lWing01 = realBody.addChild("lWing01",
                ModelPartBuilder.create()
                        .uv(26, 35).cuboid(-1.0F, -1.5F, 0.0F, 2.0F, 3.0F, 5.0F),
                ModelTransform.of(2.5F, 2.1F, 1.4F, 0.2731F, 0.5236F, 0.0F));
        ModelPartData lWing02 = lWing01.addChild("lWing02",
                ModelPartBuilder.create()
                        .uv(27, 44).cuboid(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 8.0F),
                ModelTransform.of(0.1F, 0.2F, 4.3F, 0.5236F, 0.0F, 0.0F));
        ModelPartData lWing03 = lWing02.addChild("lWing03",
                ModelPartBuilder.create()
                        .uv(29, 54).cuboid(-1.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F),
                ModelTransform.of(0.1F, -0.5F, 7.1F, 0.2094F, 0.0F, 0.0F));
        lWing03.addChild("lWing04",
                ModelPartBuilder.create()
                        .uv(24, 55).cuboid(-0.5F, 0.0F, -0.5F, 1.0F, 8.0F, 1.0F),
                ModelTransform.of(0.0F, 7.7F, 0.0F, -0.4189F, 0.0F, 0.0F));
        lWing02.addChild("lWingMembrane",
                ModelPartBuilder.create()
                        .uv(41, 26).cuboid(0.0F, 0.4F, -2.2F, 0.0F, 14.0F, 11.0F),
                ModelTransform.of(0.0F, 0.0F, 0.0F, -0.0911F, 0.0F, 0.0F));
        ModelPartData rWing01 = realBody.addChild("rWing01",
                ModelPartBuilder.create()
                        .uv(26, 35).cuboid(-1.0F, -1.5F, 0.0F, 2.0F, 3.0F, 5.0F),
                ModelTransform.of(-2.5F, 2.1F, 1.4F, 0.2731F, -0.5236F, 0.0F));
        ModelPartData rWing02 = rWing01.addChild("rWing02",
                ModelPartBuilder.create()
                        .uv(27, 44).cuboid(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 8.0F),
                ModelTransform.of(-0.1F, 0.2F, 4.3F, 0.5236F, 0.0F, 0.0F));
        ModelPartData rWing03 = rWing02.addChild("rWing03",
                ModelPartBuilder.create()
                        .uv(29, 54).cuboid(-1.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F),
                ModelTransform.of(-0.1F, -0.5F, 7.1F, 0.2094F, 0.0F, 0.0F));
        rWing03.addChild("rWing04",
                ModelPartBuilder.create()
                        .uv(24, 55).cuboid(-0.5F, 0.0F, -0.5F, 1.0F, 8.0F, 1.0F),
                ModelTransform.of(0.0F, 7.7F, 0.0F, -0.4189F, 0.0F, 0.0F));
        rWing02.addChild("rWingMembrane",
                ModelPartBuilder.create()
                        .uv(41, 26).cuboid(0.0F, 0.4F, -2.2F, 0.0F, 14.0F, 11.0F),
                ModelTransform.of(0.0F, 0.0F, 0.0F, -0.0911F, 0.0F, 0.0F));
        ModelPartData realHead = root.addChild("realHead",
                ModelPartBuilder.create()
                        .cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F),
                ModelTransform.of(0.0F, -4.4F, 0.0F, 0.0F, 0.0F, 0.0F));
        ModelPartData rHorn01 = realHead.addChild("rHorn01",
                ModelPartBuilder.create()
                        .uv(32, 0).cuboid(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F),
                ModelTransform.of(-2.9F, -7.4F, -1.3F, 0.1047F, 0.0F, -0.4189F));
        ModelPartData rHorn02 = rHorn01.addChild("rHorn02",
                ModelPartBuilder.create()
                        .uv(32, 0).cuboid(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F, new Dilation(-0.1F, -0.1F, -0.1F)),
                ModelTransform.of(0.0F, -1.7F, 0.0F, -0.1047F, 0.0F, 0.192F));
        ModelPartData rHorn03 = rHorn02.addChild("rHorn03",
                ModelPartBuilder.create()
                        .uv(35, 5).cuboid(-0.4F, -2.0F, -0.4F, 1.0F, 2.0F, 1.0F, new Dilation(0.15F, 0.15F, 0.15F)),
                ModelTransform.of(0.0F, -1.6F, 0.0F, -0.1396F, 0.0F, 0.0698F));
        rHorn03.addChild("rHorn04",
                ModelPartBuilder.create()
                        .uv(35, 10).cuboid(-0.5F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F),
                ModelTransform.of(0.0F, -1.7F, 0.0F, -0.1396F, 0.0F, 0.1047F));
        ModelPartData lHorn01 = realHead.addChild("lHorn01",
                ModelPartBuilder.create()
                        .uv(32, 0).cuboid(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F),
                ModelTransform.of(2.9F, -7.4F, -1.3F, 0.1047F, 0.0F, 0.4189F));
        ModelPartData lHorn02 = lHorn01.addChild("lHorn02",
                ModelPartBuilder.create()
                        .uv(32, 0).cuboid(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F, new Dilation(-0.1F, -0.1F, -0.1F)),
                ModelTransform.of(0.0F, -1.7F, 0.0F, -0.1047F, 0.0F, -0.192F));
        ModelPartData lHorn03 = lHorn02.addChild("lHorn03",
                ModelPartBuilder.create()
                        .uv(35, 5).cuboid(-0.6F, -2.0F, -0.4F, 1.0F, 2.0F, 1.0F, new Dilation(0.15F, 0.15F, 0.15F)),
                ModelTransform.of(0.0F, -1.6F, 0.0F, -0.1396F, 0.0F, -0.0698F));
        lHorn03.addChild("lHorn04",
                ModelPartBuilder.create()
                        .uv(35, 10).cuboid(-0.5F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F),
                ModelTransform.of(0.0F, -1.7F, 0.0F, -0.1396F, 0.0F, -0.1047F));
        ModelPartData BipedRightLeg = root.addChild("BipedRightLeg",
                ModelPartBuilder.create()
                        .uv(0, 17).cuboid(-2.0F, -1.0F, -2.5F, 4.0F, 8.0F, 5.0F),
                ModelTransform.of(-2.1F, 6.0F, 0.1F, 0.0F, 0.0F, 0.0F));
        ModelPartData rLeg02 = BipedRightLeg.addChild("rLeg02",
                ModelPartBuilder.create()
                        .uv(0, 30).cuboid(-1.5F, 0.0F, -2.0F, 3.0F, 6.0F, 4.0F),
                ModelTransform.of(0.0F, 5.7F, -0.4F, 0.6981F, 0.0F, -0.1047F));
        ModelPartData rLeg03 = rLeg02.addChild("rLeg03",
                ModelPartBuilder.create()
                        .uv(0, 41).cuboid(-1.0F, 0.0F, -1.5F, 2.0F, 8.0F, 3.0F),
                ModelTransform.of(0.0F, 5.2F, 0.2F, -0.4189F, 0.0F, 0.0F));
        ModelPartData rHoofClaw01a = rLeg03.addChild("rHoofClaw01a",
                ModelPartBuilder.create()
                        .uv(0, 57).cuboid(-0.5F, -0.5F, -2.0F, 1.0F, 1.0F, 2.0F),
                ModelTransform.of(-0.5F, 7.4F, -1.3F, 0.1745F, 0.1396F, 0.0349F));
        rHoofClaw01a.addChild("rHoofClaw01b",
                ModelPartBuilder.create()
                        .uv(7, 56).cuboid(-0.49F, -1.1F, -1.2F, 1.0F, 1.0F, 3.0F),
                ModelTransform.of(0.0F, 0.0F, -1.0F, 0.3491F, 0.0F, 0.0F));
        ModelPartData rHoofClaw02a = rLeg03.addChild("rHoofClaw02a",
                ModelPartBuilder.create()
                        .uv(0, 57).cuboid(-0.5F, -0.5F, -2.0F, 1.0F, 1.0F, 2.0F),
                ModelTransform.of(0.5F, 7.4F, -1.3F, 0.1745F, -0.0873F, -0.0349F));
        rHoofClaw02a.addChild("rHoofClaw02b",
                ModelPartBuilder.create()
                        .uv(7, 56).cuboid(-0.49F, -1.1F, -1.2F, 1.0F, 1.0F, 3.0F),
                ModelTransform.of(0.0F, 0.0F, -1.0F, 0.3491F, 0.0F, 0.0F));
        root.addChild("BipedLeftArm",
                ModelPartBuilder.create()
                        .uv(44, 16).cuboid(-1.0F, -2.0F, -2.0F, 4.0F, 14.0F, 4.0F),
                ModelTransform.of(5.0F, -2.4F, 0.0F, 0.0F, 0.0F, 0.0F));
        root.addChild("BipedRightArm",
                ModelPartBuilder.create()
                        .uv(44, 16).cuboid(-3.0F, -2.0F, -2.0F, 4.0F, 14.0F, 4.0F),
                ModelTransform.of(-5.0F, -2.4F, 0.0F, 0.0F, 0.0F, 0.0F));
        ModelPartData BipedLeftLeg = root.addChild("BipedLeftLeg",
                ModelPartBuilder.create()
                        .uv(0, 17).cuboid(-2.0F, -1.0F, -2.5F, 4.0F, 8.0F, 5.0F),
                ModelTransform.of(2.1F, 6.0F, 0.1F, 0.0F, 0.0F, 0.0F));
        ModelPartData lLeg02 = BipedLeftLeg.addChild("lLeg02",
                ModelPartBuilder.create()
                        .uv(0, 30).cuboid(-1.5F, 0.0F, -2.0F, 3.0F, 6.0F, 4.0F),
                ModelTransform.of(0.0F, 5.7F, -0.4F, 0.6981F, 0.0F, 0.1047F));
        ModelPartData lLeg03 = lLeg02.addChild("lLeg03",
                ModelPartBuilder.create()
                        .uv(0, 41).cuboid(-1.0F, 0.0F, -1.5F, 2.0F, 8.0F, 3.0F),
                ModelTransform.of(0.0F, 5.2F, 0.2F, -0.4189F, 0.0F, 0.0F));
        ModelPartData lHoofClaw01a = lLeg03.addChild("lHoofClaw01a",
                ModelPartBuilder.create()
                        .uv(0, 57).cuboid(-0.5F, -0.5F, -2.0F, 1.0F, 1.0F, 2.0F),
                ModelTransform.of(0.5F, 7.4F, -1.3F, 0.1745F, -0.1396F, -0.0349F));
        lHoofClaw01a.addChild("lHoofClaw01b",
                ModelPartBuilder.create()
                        .uv(7, 56).cuboid(-0.49F, -1.1F, -1.2F, 1.0F, 1.0F, 3.0F),
                ModelTransform.of(0.0F, 0.0F, -1.0F, 0.3491F, 0.0F, 0.0F));
        ModelPartData lHoofClaw02a = lLeg03.addChild("lHoofClaw02a",
                ModelPartBuilder.create()
                        .uv(0, 57).cuboid(-0.5F, -0.5F, -2.0F, 1.0F, 1.0F, 2.0F),
                ModelTransform.of(-0.5F, 7.4F, -1.3F, 0.1745F, 0.0873F, 0.0349F));
        lHoofClaw02a.addChild("lHoofClaw02b",
                ModelPartBuilder.create()
                        .uv(7, 56).cuboid(-0.49F, -1.1F, -1.2F, 1.0F, 1.0F, 3.0F),
                ModelTransform.of(0.0F, 0.0F, -1.0F, 0.3491F, 0.0F, 0.0F));
        return TexturedModelData.of(data, 64, 64);
    }

    public static TexturedModelData getTexturedModelDataFemale() {
        ModelData data = BipedEntityModel.getModelData(Dilation.NONE, 0);
        ModelPartData root = data.getRoot();
        ModelPartData realBody = root.addChild("realBody",
                ModelPartBuilder.create()
                        .uv(19, 17).cuboid(-4.0F, 0.0F, -2.0F, 8.0F, 13.0F, 4.0F),
                ModelTransform.of(0.0F, -4.4F, 0.0F, 0.0F, 0.0F, 0.0F));
        ModelPartData tail01 = realBody.addChild("tail01",
                ModelPartBuilder.create()
                        .uv(13, 37).cuboid(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 4.0F),
                ModelTransform.of(0.0F, 10.1F, 1.3F, -0.8727F, 0.0F, 0.0F));
        ModelPartData tail02 = tail01.addChild("tail02",
                ModelPartBuilder.create()
                        .uv(13, 37).cuboid(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 4.0F),
                ModelTransform.of(0.0F, 0.0F, 3.8F, -0.1396F, 0.0F, 0.0F));
        ModelPartData tail03 = tail02.addChild("tail03",
                ModelPartBuilder.create()
                        .uv(15, 45).cuboid(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 5.0F),
                ModelTransform.of(0.0F, 0.0F, 2.9F, 0.0698F, 0.0F, 0.0F));
        ModelPartData tail04 = tail03.addChild("tail04",
                ModelPartBuilder.create()
                        .uv(15, 45).cuboid(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 5.0F),
                ModelTransform.of(0.0F, 0.0F, 4.9F, 0.1396F, 0.0F, 0.0F));
        ModelPartData tail05 = tail04.addChild("tail05",
                ModelPartBuilder.create()
                        .uv(15, 45).cuboid(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 5.0F),
                ModelTransform.of(0.0F, 0.0F, 4.9F, 0.2269F, 0.0F, 0.0F));
        ModelPartData tailTip01 = tail05.addChild("tailTip01",
                ModelPartBuilder.create()
                        .uv(16, 53).cuboid(-1.0F, -0.5F, 0.0F, 2.0F, 1.0F, 2.0F),
                ModelTransform.of(0.0F, 0.0F, 4.5F, 0.2618F, 0.0F, 0.0F));
        tailTip01.addChild("tailTip02",
                ModelPartBuilder.create()
                        .uv(15, 58).cuboid(-0.5F, -0.5F, -0.5F, 2.0F, 1.0F, 2.0F),
                ModelTransform.of(0.0F, 0.1F, 0.8F, 0.0F, -0.7854F, 0.0F));
        ModelPartData lWing01 = realBody.addChild("lWing01",
                ModelPartBuilder.create()
                        .uv(26, 35).cuboid(-1.0F, -1.5F, 0.0F, 2.0F, 3.0F, 5.0F),
                ModelTransform.of(2.5F, 2.1F, 1.4F, 0.2731F, 0.5236F, 0.0F));
        ModelPartData lWing02 = lWing01.addChild("lWing02",
                ModelPartBuilder.create()
                        .uv(27, 44).cuboid(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 8.0F),
                ModelTransform.of(0.1F, 0.2F, 4.3F, 0.5236F, 0.0F, 0.0F));
        ModelPartData lWing03 = lWing02.addChild("lWing03",
                ModelPartBuilder.create()
                        .uv(29, 54).cuboid(-1.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F),
                ModelTransform.of(0.1F, -0.5F, 7.1F, 0.2094F, 0.0F, 0.0F));
        lWing03.addChild("lWing04",
                ModelPartBuilder.create()
                        .uv(24, 55).cuboid(-0.5F, 0.0F, -0.5F, 1.0F, 8.0F, 1.0F),
                ModelTransform.of(0.0F, 7.7F, 0.0F, -0.4189F, 0.0F, 0.0F));
        lWing02.addChild("lWingMembrane",
                ModelPartBuilder.create()
                        .uv(41, 26).cuboid(0.0F, 0.4F, -2.2F, 0.0F, 14.0F, 11.0F),
                ModelTransform.of(0.0F, 0.0F, 0.0F, -0.0911F, 0.0F, 0.0F));
        ModelPartData rWing01 = realBody.addChild("rWing01",
                ModelPartBuilder.create()
                        .uv(26, 35).cuboid(-1.0F, -1.5F, 0.0F, 2.0F, 3.0F, 5.0F),
                ModelTransform.of(-2.5F, 2.1F, 1.4F, 0.2731F, -0.5236F, 0.0F));
        ModelPartData rWing02 = rWing01.addChild("rWing02",
                ModelPartBuilder.create()
                        .uv(27, 44).cuboid(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 8.0F),
                ModelTransform.of(-0.1F, 0.2F, 4.3F, 0.5236F, 0.0F, 0.0F));
        ModelPartData rWing03 = rWing02.addChild("rWing03",
                ModelPartBuilder.create()
                        .uv(29, 54).cuboid(-1.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F),
                ModelTransform.of(-0.1F, -0.5F, 7.1F, 0.2094F, 0.0F, 0.0F));
        rWing03.addChild("rWing04",
                ModelPartBuilder.create()
                        .uv(24, 55).cuboid(-0.5F, 0.0F, -0.5F, 1.0F, 8.0F, 1.0F),
                ModelTransform.of(0.0F, 7.7F, 0.0F, -0.4189F, 0.0F, 0.0F));
        rWing02.addChild("rWingMembrane",
                ModelPartBuilder.create()
                        .uv(41, 26).cuboid(0.0F, 0.4F, -2.2F, 0.0F, 14.0F, 11.0F),
                ModelTransform.of(0.0F, 0.0F, 0.0F, -0.0911F, 0.0F, 0.0F));
        ModelPartData realHead = root.addChild("realHead",
                ModelPartBuilder.create()
                        .cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F),
                ModelTransform.of(0.0F, -4.4F, 0.0F, 0.0F, 0.0F, 0.0F));
        ModelPartData rHorn01 = realHead.addChild("rHorn01",
                ModelPartBuilder.create()
                        .uv(32, 0).cuboid(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F),
                ModelTransform.of(-2.9F, -7.4F, -1.3F, 0.1047F, 0.0F, -0.4189F));
        ModelPartData rHorn02 = rHorn01.addChild("rHorn02",
                ModelPartBuilder.create()
                        .uv(32, 0).cuboid(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F, new Dilation(-0.1F, -0.1F, -0.1F)),
                ModelTransform.of(0.0F, -1.7F, 0.0F, -0.1047F, 0.0F, 0.192F));
        ModelPartData rHorn03 = rHorn02.addChild("rHorn03",
                ModelPartBuilder.create()
                        .uv(35, 5).cuboid(-0.4F, -2.0F, -0.4F, 1.0F, 2.0F, 1.0F, new Dilation(0.15F, 0.15F, 0.15F)),
                ModelTransform.of(0.0F, -1.6F, 0.0F, -0.1396F, 0.0F, 0.0698F));
        rHorn03.addChild("rHorn04",
                ModelPartBuilder.create()
                        .uv(35, 10).cuboid(-0.5F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F),
                ModelTransform.of(0.0F, -1.7F, 0.0F, -0.1396F, 0.0F, 0.1047F));
        ModelPartData lHorn01 = realHead.addChild("lHorn01",
                ModelPartBuilder.create()
                        .uv(32, 0).cuboid(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F),
                ModelTransform.of(2.9F, -7.4F, -1.3F, 0.1047F, 0.0F, 0.4189F));
        ModelPartData lHorn02 = lHorn01.addChild("lHorn02",
                ModelPartBuilder.create()
                        .uv(32, 0).cuboid(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F, new Dilation(-0.1F, -0.1F, -0.1F)),
                ModelTransform.of(0.0F, -1.7F, 0.0F, -0.1047F, 0.0F, -0.192F));
        ModelPartData lHorn03 = lHorn02.addChild("lHorn03",
                ModelPartBuilder.create()
                        .uv(35, 5).cuboid(-0.6F, -2.0F, -0.4F, 1.0F, 2.0F, 1.0F, new Dilation(0.15F, 0.15F, 0.15F)),
                ModelTransform.of(0.0F, -1.6F, 0.0F, -0.1396F, 0.0F, -0.0698F));
        lHorn03.addChild("lHorn04",
                ModelPartBuilder.create()
                        .uv(35, 10).cuboid(-0.5F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F),
                ModelTransform.of(0.0F, -1.7F, 0.0F, -0.1396F, 0.0F, -0.1047F));
        ModelPartData BipedRightLeg = root.addChild("BipedRightLeg",
                ModelPartBuilder.create()
                        .uv(0, 17).cuboid(-2.0F, -1.0F, -2.5F, 4.0F, 8.0F, 5.0F),
                ModelTransform.of(-2.1F, 6.0F, 0.1F, 0.0F, 0.0F, 0.0F));
        ModelPartData rLeg02 = BipedRightLeg.addChild("rLeg02",
                ModelPartBuilder.create()
                        .uv(0, 30).cuboid(-1.5F, 0.0F, -2.0F, 3.0F, 6.0F, 4.0F),
                ModelTransform.of(0.0F, 5.7F, -0.4F, 0.6981F, 0.0F, -0.1047F));
        ModelPartData rLeg03 = rLeg02.addChild("rLeg03",
                ModelPartBuilder.create()
                        .uv(0, 41).cuboid(-1.0F, 0.0F, -1.5F, 2.0F, 8.0F, 3.0F),
                ModelTransform.of(0.0F, 5.2F, 0.2F, -0.4189F, 0.0F, 0.0F));
        ModelPartData rHoofClaw01a = rLeg03.addChild("rHoofClaw01a",
                ModelPartBuilder.create()
                        .uv(0, 57).cuboid(-0.5F, -0.5F, -2.0F, 1.0F, 1.0F, 2.0F),
                ModelTransform.of(-0.5F, 7.4F, -1.3F, 0.1745F, 0.1396F, 0.0349F));
        rHoofClaw01a.addChild("rHoofClaw01b",
                ModelPartBuilder.create()
                        .uv(7, 56).cuboid(-0.49F, -1.1F, -1.2F, 1.0F, 1.0F, 3.0F),
                ModelTransform.of(0.0F, 0.0F, -1.0F, 0.3491F, 0.0F, 0.0F));
        ModelPartData rHoofClaw02a = rLeg03.addChild("rHoofClaw02a",
                ModelPartBuilder.create()
                        .uv(0, 57).cuboid(-0.5F, -0.5F, -2.0F, 1.0F, 1.0F, 2.0F),
                ModelTransform.of(0.5F, 7.4F, -1.3F, 0.1745F, -0.0873F, -0.0349F));
        rHoofClaw02a.addChild("rHoofClaw02b",
                ModelPartBuilder.create()
                        .uv(7, 56).cuboid(-0.49F, -1.1F, -1.2F, 1.0F, 1.0F, 3.0F),
                ModelTransform.of(0.0F, 0.0F, -1.0F, 0.3491F, 0.0F, 0.0F));
        root.addChild("BipedLeftArm",
                ModelPartBuilder.create()
                        .uv(44, 16).cuboid(-1.0F, -2.0F, -2.0F, 4.0F, 14.0F, 4.0F),
                ModelTransform.of(5.0F, -2.4F, 0.0F, 0.0F, 0.0F, 0.0F));
        root.addChild("BipedRightArm",
                ModelPartBuilder.create()
                        .uv(44, 16).cuboid(-3.0F, -2.0F, -2.0F, 4.0F, 14.0F, 4.0F),
                ModelTransform.of(-5.0F, -2.4F, 0.0F, 0.0F, 0.0F, 0.0F));
        ModelPartData BipedLeftLeg = root.addChild("BipedLeftLeg",
                ModelPartBuilder.create()
                        .uv(0, 17).cuboid(-2.0F, -1.0F, -2.5F, 4.0F, 8.0F, 5.0F),
                ModelTransform.of(2.1F, 6.0F, 0.1F, 0.0F, 0.0F, 0.0F));
        ModelPartData lLeg02 = BipedLeftLeg.addChild("lLeg02",
                ModelPartBuilder.create()
                        .uv(0, 30).cuboid(-1.5F, 0.0F, -2.0F, 3.0F, 6.0F, 4.0F),
                ModelTransform.of(0.0F, 5.7F, -0.4F, 0.6981F, 0.0F, 0.1047F));
        ModelPartData lLeg03 = lLeg02.addChild("lLeg03",
                ModelPartBuilder.create()
                        .uv(0, 41).cuboid(-1.0F, 0.0F, -1.5F, 2.0F, 8.0F, 3.0F),
                ModelTransform.of(0.0F, 5.2F, 0.2F, -0.4189F, 0.0F, 0.0F));
        ModelPartData lHoofClaw01a = lLeg03.addChild("lHoofClaw01a",
                ModelPartBuilder.create()
                        .uv(0, 57).cuboid(-0.5F, -0.5F, -2.0F, 1.0F, 1.0F, 2.0F),
                ModelTransform.of(0.5F, 7.4F, -1.3F, 0.1745F, -0.1396F, -0.0349F));
        lHoofClaw01a.addChild("lHoofClaw01b",
                ModelPartBuilder.create()
                        .uv(7, 56).cuboid(-0.49F, -1.1F, -1.2F, 1.0F, 1.0F, 3.0F),
                ModelTransform.of(0.0F, 0.0F, -1.0F, 0.3491F, 0.0F, 0.0F));
        ModelPartData lHoofClaw02a = lLeg03.addChild("lHoofClaw02a",
                ModelPartBuilder.create()
                        .uv(0, 57).cuboid(-0.5F, -0.5F, -2.0F, 1.0F, 1.0F, 2.0F),
                ModelTransform.of(-0.5F, 7.4F, -1.3F, 0.1745F, 0.0873F, 0.0349F));
        lHoofClaw02a.addChild("lHoofClaw02b",
                ModelPartBuilder.create()
                        .uv(7, 56).cuboid(-0.49F, -1.1F, -1.2F, 1.0F, 1.0F, 3.0F),
                ModelTransform.of(0.0F, 0.0F, -1.0F, 0.3491F, 0.0F, 0.0F));
        return TexturedModelData.of(data, 64, 64);
    }

    @Override
    public void setAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float realHeadYaw, float realHeadPitch) {
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
        BipedLeftLeg.pitch -= 0.2618f;
        BipedLeftLeg.roll -= 0.1047f;
        copyRotation(BipedRightLeg, super.rightLeg);
        BipedRightLeg.pitch /= 2;
        BipedRightLeg.pitch -= 0.2618f;
        BipedRightLeg.roll += 0.1047f;
        lWing01.yaw = MathHelper.cos(animationProgress / 16) / 3 + 1 / 3f;
        rWing01.yaw = -lWing01.yaw;
        tail01.roll = MathHelper.sin(animationProgress / 8) / 8;
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
