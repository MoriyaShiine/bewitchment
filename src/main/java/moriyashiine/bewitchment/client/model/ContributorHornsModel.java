// Made with Blockbench 3.7.5
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports

package moriyashiine.bewitchment.client.model;

import net.minecraft.client.model.Model;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;

public class ContributorHornsModel extends Model {
    private final ModelPart armorHead;
    private final ModelPart lUpperHorn01;
    private final ModelPart lUpperHorn02;
    private final ModelPart lUpperHorn03;
    private final ModelPart rUpperHorn01;
    private final ModelPart rUpperHorn02;
    private final ModelPart rUpperHorn03;
    private final ModelPart lHorn01;
    private final ModelPart lHorn02;
    private final ModelPart lHorn03;
    private final ModelPart lHorn04;
    private final ModelPart lHorn05;
    private final ModelPart rHorn01;
    private final ModelPart rHorn02;
    private final ModelPart rHorn03;
    private final ModelPart rHorn04;
    private final ModelPart rHorn05;

    public ContributorHornsModel() {
        super(RenderLayer::getEntityTranslucent);
        textureWidth = 16;
        textureHeight = 16;
        armorHead = new ModelPart(this);
        armorHead.setPivot(0.0F, 0.0F, 0.0F);

        lUpperHorn01 = new ModelPart(this);
        lUpperHorn01.setPivot(2.0F, -8.7F, -0.5F);
        armorHead.addChild(lUpperHorn01);
        setRotationAngle(lUpperHorn01, 0.8727F, 0.1745F, 0.0F);
        lUpperHorn01.setTextureOffset(6, 5).addCuboid(-1.0F, -1.0F, -1.8F, 2.0F, 2.0F, 3.0F, 0.0F, true);

        lUpperHorn02 = new ModelPart(this);
        lUpperHorn02.setPivot(0.0F, 0.0F, 0.9F);
        lUpperHorn01.addChild(lUpperHorn02);
        setRotationAngle(lUpperHorn02, -0.3142F, 0.2618F, 0.0F);
        lUpperHorn02.setTextureOffset(0, 12).addCuboid(-0.5F, -0.6F, -0.5F, 1.0F, 1.0F, 3.0F, 0.2F, true);

        lUpperHorn03 = new ModelPart(this);
        lUpperHorn03.setPivot(0.0F, 0.0F, 2.4F);
        lUpperHorn02.addChild(lUpperHorn03);
        setRotationAngle(lUpperHorn03, -0.1745F, 0.1745F, 0.0F);
        lUpperHorn03.setTextureOffset(8, 12).addCuboid(-0.5F, -0.5F, -0.4F, 1.0F, 1.0F, 3.0F, 0.0F, true);

        rUpperHorn01 = new ModelPart(this);
        rUpperHorn01.setPivot(-2.0F, -8.7F, -0.5F);
        armorHead.addChild(rUpperHorn01);
        setRotationAngle(rUpperHorn01, 0.8727F, -0.1745F, 0.0F);
        rUpperHorn01.setTextureOffset(6, 5).addCuboid(-1.0F, -1.0F, -1.8F, 2.0F, 2.0F, 3.0F, 0.0F, true);

        rUpperHorn02 = new ModelPart(this);
        rUpperHorn02.setPivot(0.0F, 0.0F, 0.9F);
        rUpperHorn01.addChild(rUpperHorn02);
        setRotationAngle(rUpperHorn02, -0.3142F, -0.2618F, 0.0F);
        rUpperHorn02.setTextureOffset(0, 12).addCuboid(-0.5F, -0.6F, -0.5F, 1.0F, 1.0F, 3.0F, 0.2F, true);

        rUpperHorn03 = new ModelPart(this);
        rUpperHorn03.setPivot(0.0F, 0.0F, 2.4F);
        rUpperHorn02.addChild(rUpperHorn03);
        setRotationAngle(rUpperHorn03, -0.1745F, -0.1745F, 0.0F);
        rUpperHorn03.setTextureOffset(8, 12).addCuboid(-0.5F, -0.5F, -0.4F, 1.0F, 1.0F, 3.0F, 0.0F, true);

        lHorn01 = new ModelPart(this);
        lHorn01.setPivot(2.9F, -7.3F, 1.6F);
        armorHead.addChild(lHorn01);
        setRotationAngle(lHorn01, -0.6109F, 0.0F, 1.309F);
        lHorn01.setTextureOffset(0, 0).addCuboid(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F, 0.0F, false);

        lHorn02 = new ModelPart(this);
        lHorn02.setPivot(-0.1F, -1.5F, -0.1F);
        lHorn01.addChild(lHorn02);
        setRotationAngle(lHorn02, -0.2618F, 0.0F, 0.4014F);
        lHorn02.setTextureOffset(0, 4).addCuboid(-0.6F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F, 0.2F, false);

        lHorn03 = new ModelPart(this);
        lHorn03.setPivot(0.0F, -1.6F, 0.0F);
        lHorn02.addChild(lHorn03);
        setRotationAngle(lHorn03, -0.1745F, 0.0F, 0.4363F);
        lHorn03.setTextureOffset(0, 4).addCuboid(-0.8F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F, 0.1F, false);

        lHorn04 = new ModelPart(this);
        lHorn04.setPivot(0.0F, -1.7F, 0.0F);
        lHorn03.addChild(lHorn04);
        setRotationAngle(lHorn04, 0.0524F, 0.0F, 0.1396F);
        lHorn04.setTextureOffset(12, 0).addCuboid(-0.5F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        lHorn05 = new ModelPart(this);
        lHorn05.setPivot(0.0F, -2.7F, 0.0F);
        lHorn04.addChild(lHorn05);
        setRotationAngle(lHorn05, 0.0524F, 0.0F, -0.3142F);
        lHorn05.setTextureOffset(12, 0).addCuboid(-0.5F, -2.1F, -0.5F, 1.0F, 2.0F, 1.0F, 0.0F, false);

        rHorn01 = new ModelPart(this);
        rHorn01.setPivot(-2.9F, -7.3F, 1.6F);
        armorHead.addChild(rHorn01);
        setRotationAngle(rHorn01, -0.6109F, 0.0F, -1.309F);
        rHorn01.setTextureOffset(0, 0).addCuboid(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F, 0.0F, true);

        rHorn02 = new ModelPart(this);
        rHorn02.setPivot(0.1F, -1.5F, -0.1F);
        rHorn01.addChild(rHorn02);
        setRotationAngle(rHorn02, -0.2618F, 0.0F, -0.4014F);
        rHorn02.setTextureOffset(0, 4).addCuboid(-0.4F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F, 0.2F, true);

        rHorn03 = new ModelPart(this);
        rHorn03.setPivot(0.0F, -1.6F, 0.0F);
        rHorn02.addChild(rHorn03);
        setRotationAngle(rHorn03, -0.1745F, 0.0F, -0.4363F);
        rHorn03.setTextureOffset(0, 4).addCuboid(-0.2F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F, 0.1F, true);

        rHorn04 = new ModelPart(this);
        rHorn04.setPivot(0.0F, -1.7F, 0.0F);
        rHorn03.addChild(rHorn04);
        setRotationAngle(rHorn04, 0.0524F, 0.0F, -0.1396F);
        rHorn04.setTextureOffset(12, 0).addCuboid(-0.5F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, true);

        rHorn05 = new ModelPart(this);
        rHorn05.setPivot(0.0F, -2.7F, 0.0F);
        rHorn04.addChild(rHorn05);
        setRotationAngle(rHorn05, 0.0524F, 0.0F, 0.3142F);
        rHorn05.setTextureOffset(12, 0).addCuboid(-0.5F, -2.1F, -0.5F, 1.0F, 2.0F, 1.0F, 0.0F, true);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
        armorHead.render(matrices, vertices, light, overlay, red, green, blue, alpha);
    }

    public void setRotationAngle(ModelPart bone, float x, float y, float z) {
        bone.pitch = x;
        bone.yaw = y;
        bone.roll = z;
    }

}