package moriyashiine.bewitchment.client.model.entity.living;

import com.google.common.collect.ImmutableList;
import moriyashiine.bewitchment.common.entity.living.SnakeEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.AnimalModel;
import net.minecraft.util.math.MathHelper;

@Environment(EnvType.CLIENT)
public class SnakeEntityModel<T extends SnakeEntity> extends AnimalModel<T> {
	public ModelPart bodyBase;
	public ModelPart tail01;
	public ModelPart neck00;
	public ModelPart tail02;
	public ModelPart tail03;
	public ModelPart tail04;
	public ModelPart tail05;
	public ModelPart tail06;
	public ModelPart neck01;
	public ModelPart head;
	public ModelPart lowerJaw;
	public ModelPart snout;
	public ModelPart tongue;
	public ModelPart lFang;
	public ModelPart rFang;
	
	public SnakeEntityModel() {
		this.textureWidth = 64;
		this.textureHeight = 32;
		this.neck00 = new ModelPart(this, 0, 7);
		this.neck00.setPivot(0.0F, 0.0F, 0.0F);
		this.neck00.addCuboid(-1.01F, -0.99F, -3.0F, 2, 2, 3, 0.0F);
		this.tail03 = new ModelPart(this, 13, 21);
		this.tail03.setPivot(0.0F, 0.0F, 2.5F);
		this.tail03.addCuboid(-1.01F, -0.99F, 0.0F, 2, 2, 3, 0.0F);
		this.tongue = new ModelPart(this, 31, 0);
		this.tongue.setPivot(0.0F, 0.9F, -4.7F);
		this.tongue.addCuboid(-0.5F, 0.0F, -2.7F, 1, 0, 4, 0.0F);
		this.lFang = new ModelPart(this, 16, 0);
		this.lFang.setPivot(0.7F, 0.1F, -2.1F);
		this.lFang.addCuboid(0.0F, 0.0F, -0.5F, 0, 2, 1, 0.0F);
		this.setRotateAngle(lFang, -0.2617993877991494F, 0.0F, 0.0F);
		this.tail02 = new ModelPart(this, 0, 27);
		this.tail02.setPivot(0.0F, 0.0F, 2.5F);
		this.tail02.addCuboid(-1.0F, -1.0F, 0.0F, 2, 2, 3, 0.0F);
		this.tail06 = new ModelPart(this, 46, 21);
		this.tail06.setPivot(0.0F, 0.4F, 3.8F);
		this.tail06.addCuboid(-0.5F, -0.5F, 0.0F, 1, 1, 4, 0.0F);
		this.head = new ModelPart(this, 20, 0);
		this.head.setPivot(0.0F, 0.0F, -2.8F);
		this.head.addCuboid(-1.5F, -1.52F, -2.1F, 3, 2, 2, 0.0F);
		this.tail01 = new ModelPart(this, 0, 21);
		this.tail01.setPivot(0.0F, 0.0F, 2.5F);
		this.tail01.addCuboid(-1.01F, -0.99F, 0.0F, 2, 2, 3, 0.0F);
		this.lowerJaw = new ModelPart(this, 19, 13);
		this.lowerJaw.setPivot(0.0F, 0.9F, -0.4F);
		this.lowerJaw.addCuboid(-0.91F, -0.5F, -3.8F, 2, 1, 4, 0.0F);
		this.tail05 = new ModelPart(this, 35, 20);
		this.tail05.setPivot(0.0F, 0.0F, 3.3F);
		this.tail05.addCuboid(-0.51F, -0.99F, 0.0F, 1, 2, 4, 0.0F);
		this.bodyBase = new ModelPart(this, 0, 14);
		this.bodyBase.setPivot(0.0F, 23.0F, -1.0F);
		this.bodyBase.addCuboid(-1.0F, -1.0F, 0.0F, 2, 2, 3, 0.0F);
		this.tail04 = new ModelPart(this, 24, 20);
		this.tail04.setPivot(0.0F, 0.0F, 2.5F);
		this.tail04.addCuboid(-0.5F, -1.0F, 0.0F, 1, 2, 4, 0.0F);
		this.snout = new ModelPart(this, 20, 7);
		this.snout.setPivot(0.0F, -0.6F, -2.0F);
		this.snout.addCuboid(-1.0F, -1.0F, -2.9F, 2, 2, 3, 0.0F);
		this.setRotateAngle(snout, 0.2617993877991494F, 0.0F, 0.0F);
		this.neck01 = new ModelPart(this, 0, 0);
		this.neck01.setPivot(0.0F, 0.0F, -2.5F);
		this.neck01.addCuboid(-1.0F, -1.0F, -3.0F, 2, 2, 3, 0.0F);
		this.rFang = new ModelPart(this, 16, 0);
		this.rFang.mirror = true;
		this.rFang.setPivot(-0.7F, 0.1F, -2.1F);
		this.rFang.addCuboid(0.0F, 0.0F, -0.5F, 0, 2, 1, 0.0F);
		this.setRotateAngle(rFang, -0.2617993877991494F, 0.0F, 0.0F);
		this.bodyBase.addChild(this.neck00);
		this.tail02.addChild(this.tail03);
		this.head.addChild(this.tongue);
		this.snout.addChild(this.lFang);
		this.tail01.addChild(this.tail02);
		this.tail05.addChild(this.tail06);
		this.neck01.addChild(this.head);
		this.bodyBase.addChild(this.tail01);
		this.head.addChild(this.lowerJaw);
		this.tail04.addChild(this.tail05);
		this.tail03.addChild(this.tail04);
		this.head.addChild(this.snout);
		this.neck00.addChild(this.neck01);
		this.snout.addChild(this.rFang);
	}
	
	@Override
	protected Iterable<ModelPart> getHeadParts() {
		return ImmutableList.of();
	}
	
	@Override
	protected Iterable<ModelPart> getBodyParts() {
		return ImmutableList.of(bodyBase);
	}
	
	@Override
	public void setAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
		head.pitch = (float) (headPitch * (Math.PI / 180f));
		head.yaw = (float) (headYaw * (Math.PI / 180f)) * 0.5f;
		tongue.pitch = MathHelper.cos(animationProgress * 3 + entity.getEntityId()) / 3;
		tongue.visible = entity.tongueFlick > 0;
		boolean attacking = entity.attackTick > 0;
		lFang.visible = rFang.visible = attacking;
		if (attacking) {
			head.pitch = -1;
			lowerJaw.pitch = 1;
		}
		else {
			head.pitch = 0;
			lowerJaw.pitch = 0;
		}
		if (entity.isSitting()) {
			neck00.yaw = -0.35f;
			neck01.yaw = -0.7f;
			tail01.yaw = 0.7f;
			tail02.yaw = 0.6f;
			tail03.yaw = 0.6f;
			tail04.yaw = 0.5f;
			tail05.yaw = 1.2f;
			tail06.yaw = 1.075f;
		}
		else {
			bodyBase.yaw = MathHelper.cos(entity.curve + entity.getEntityId());
			neck00.yaw = -bodyBase.yaw / 3;
			neck01.yaw = neck00.yaw;
			tail01.yaw = neck01.yaw;
			tail02.yaw = tail01.yaw;
			tail03.yaw = tail02.yaw;
			tail04.yaw = tail03.yaw;
			tail05.yaw = tail04.yaw;
			tail06.yaw = tail05.yaw;
		}
	}
	
	/**
	 * This is a helper function from Tabula to set the rotation of model parts
	 */
	public void setRotateAngle(ModelPart modelRenderer, float x, float y, float z) {
		modelRenderer.pitch = x;
		modelRenderer.yaw = y;
		modelRenderer.roll = z;
	}
}
