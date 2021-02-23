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
	private final ModelPart bodyBase;
	private final ModelPart tail01;
	private final ModelPart tail02;
	private final ModelPart tail03;
	private final ModelPart tail04;
	private final ModelPart tail05;
	private final ModelPart tail06;
	private final ModelPart neck00;
	private final ModelPart neck01;
	private final ModelPart head;
	private final ModelPart lowerJaw;
	private final ModelPart lFang;
	private final ModelPart rFang;
	private final ModelPart tongue;
	
	public SnakeEntityModel() {
		textureWidth = 64;
		textureHeight = 32;
		
		bodyBase = new ModelPart(this);
		bodyBase.setPivot(0.0F, 23.0F, -1.0F);
		bodyBase.setTextureOffset(0, 14).addCuboid(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 3.0F, 0.0F, false);
		
		tail01 = new ModelPart(this);
		tail01.setPivot(0.0F, 0.0F, 2.5F);
		bodyBase.addChild(tail01);
		tail01.setTextureOffset(0, 21).addCuboid(-0.99F, -0.99F, 0.0F, 2.0F, 2.0F, 3.0F, 0.0F, false);
		
		tail02 = new ModelPart(this);
		tail02.setPivot(0.0F, 0.0F, 2.5F);
		tail01.addChild(tail02);
		tail02.setTextureOffset(0, 27).addCuboid(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 3.0F, 0.0F, false);
		
		tail03 = new ModelPart(this);
		tail03.setPivot(0.0F, 0.0F, 2.5F);
		tail02.addChild(tail03);
		tail03.setTextureOffset(13, 21).addCuboid(-0.99F, -0.99F, 0.0F, 2.0F, 2.0F, 3.0F, 0.0F, false);
		
		tail04 = new ModelPart(this);
		tail04.setPivot(0.0F, 0.0F, 2.5F);
		tail03.addChild(tail04);
		tail04.setTextureOffset(24, 20).addCuboid(-0.5F, -1.0F, 0.0F, 1.0F, 2.0F, 4.0F, 0.0F, false);
		
		tail05 = new ModelPart(this);
		tail05.setPivot(0.0F, 0.0F, 3.3F);
		tail04.addChild(tail05);
		tail05.setTextureOffset(35, 20).addCuboid(-0.49F, -0.99F, 0.0F, 1.0F, 2.0F, 4.0F, 0.0F, false);
		
		tail06 = new ModelPart(this);
		tail06.setPivot(0.0F, 0.4F, 3.8F);
		tail05.addChild(tail06);
		tail06.setTextureOffset(46, 21).addCuboid(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 4.0F, 0.0F, false);
		
		neck00 = new ModelPart(this);
		neck00.setPivot(0.0F, 0.0F, 0.0F);
		bodyBase.addChild(neck00);
		neck00.setTextureOffset(0, 7).addCuboid(-0.99F, -0.99F, -3.0F, 2.0F, 2.0F, 3.0F, 0.0F, false);
		
		neck01 = new ModelPart(this);
		neck01.setPivot(0.0F, 0.0F, -2.5F);
		neck00.addChild(neck01);
		neck01.setTextureOffset(0, 0).addCuboid(-1.0F, -1.0F, -3.0F, 2.0F, 2.0F, 3.0F, 0.0F, false);
		
		head = new ModelPart(this);
		head.setPivot(0.0F, 0.0F, -2.6F);
		neck01.addChild(head);
		head.setTextureOffset(20, 0).addCuboid(-1.5F, -1.52F, -2.1F, 3.0F, 2.0F, 2.0F, 0.0F, false);
		
		lowerJaw = new ModelPart(this);
		lowerJaw.setPivot(0.0F, 0.9F, -0.4F);
		head.addChild(lowerJaw);
		lowerJaw.setTextureOffset(19, 13).addCuboid(-1.09F, -0.5F, -3.8F, 2.0F, 1.0F, 4.0F, 0.0F, false);
		
		ModelPart snout = new ModelPart(this);
		snout.setPivot(0.0F, -0.6F, -2.0F);
		head.addChild(snout);
		setRotation(snout, 0.2618F, 0.0F, 0.0F);
		snout.setTextureOffset(20, 7).addCuboid(-1.0F, -1.0F, -2.9F, 2.0F, 2.0F, 3.0F, 0.1F, false);
		
		lFang = new ModelPart(this);
		lFang.setPivot(-0.7F, 0.1F, -2.1F);
		snout.addChild(lFang);
		setRotation(lFang, -0.2618F, 0.0F, 0.0F);
		lFang.setTextureOffset(16, 0).addCuboid(-0.3F, 0.0F, -0.5F, 0.0F, 2.0F, 1.0F, 0.0F, false);
		
		rFang = new ModelPart(this);
		rFang.setPivot(0.7F, 0.1F, -2.1F);
		snout.addChild(rFang);
		setRotation(rFang, -0.2618F, 0.0F, 0.0F);
		rFang.setTextureOffset(16, 0).addCuboid(0.1F, 0.0F, -0.5F, 0.0F, 2.0F, 1.0F, 0.0F, true);
		
		tongue = new ModelPart(this);
		tongue.setPivot(0.0F, 0.9F, -4.7F);
		head.addChild(tongue);
		tongue.setTextureOffset(31, 0).addCuboid(-0.5F, 0.0F, -2.7F, 1.0F, 0.0F, 4.0F, 0.0F, false);
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
		if (entity.isInSittingPose()) {
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
			bodyBase.yaw = MathHelper.cos(limbAngle + entity.getEntityId());
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
	
	@Override
	protected Iterable<ModelPart> getHeadParts() {
		return ImmutableList.of();
	}
	
	@Override
	protected Iterable<ModelPart> getBodyParts() {
		return ImmutableList.of(bodyBase);
	}
	
	private void setRotation(ModelPart bone, float x, float y, float z) {
		bone.pitch = x;
		bone.yaw = y;
		bone.roll = z;
	}
}
