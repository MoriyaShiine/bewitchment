package moriyashiine.bewitchment.common.entity.projectile;

import moriyashiine.bewitchment.common.entity.interfaces.MasterAccessor;
import moriyashiine.bewitchment.common.entity.living.HerneEntity;
import moriyashiine.bewitchment.common.registry.BWEntityTypes;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("ConstantConditions")
public class HornedSpearEntity extends PersistentProjectileEntity {
	public ItemStack spear = ItemStack.EMPTY;
	private boolean dealtDamage = false;
	
	public HornedSpearEntity(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
		super(entityType, world);
	}
	
	public HornedSpearEntity(World world, double x, double y, double z) {
		super(BWEntityTypes.HORNED_SPEAR, x, y, z, world);
	}
	
	public HornedSpearEntity(EntityType<? extends PersistentProjectileEntity> type, LivingEntity owner, World world, ItemStack stack) {
		super(type, owner, world);
		spear = stack;
	}
	
	@Override
	protected ItemStack asItemStack() {
		return spear;
	}
	
	@Override
	protected void onEntityHit(EntityHitResult result) {
		Entity owner = getOwner();
		Entity entity = result.getEntity();
		float damage = 7;
		if (entity instanceof LivingEntity) {
			damage += EnchantmentHelper.getAttackDamage(spear, ((LivingEntity) entity).getGroup());
		}
		if (owner instanceof HerneEntity) {
			damage *= 3;
		}
		dealtDamage = true;
		if (entity.damage(DamageSource.trident(this, owner == null ? this : owner), damage)) {
			if (entity.getType() == EntityType.ENDERMAN) {
				return;
			}
			if (entity instanceof LivingEntity livingEntity) {
				if (owner instanceof LivingEntity) {
					EnchantmentHelper.onUserDamaged(livingEntity, owner);
					EnchantmentHelper.onTargetDamaged((LivingEntity) owner, livingEntity);
				}
				onHit(livingEntity);
			}
		}
		setVelocity(getVelocity().multiply(-0.01, -0.1, -0.01));
	}
	
	@Nullable
	@Override
	protected EntityHitResult getEntityCollision(Vec3d currentPosition, Vec3d nextPosition) {
		if (isOwnerAlive()) {
			EntityHitResult collision = ProjectileUtil.getEntityCollision(this.world, this, currentPosition, nextPosition, getBoundingBox().stretch(getVelocity()).expand(1), this::canHit);
			if (collision != null) {
				Entity entity = collision.getEntity();
				if (entity instanceof MasterAccessor && getOwner().getUuid().equals(((MasterAccessor) entity).getMasterUUID())) {
					return null;
				}
			}
		}
		return dealtDamage ? null : super.getEntityCollision(currentPosition, nextPosition);
	}
	
	@Override
	public void onPlayerCollision(PlayerEntity player) {
		Entity owner = getOwner();
		if (owner == null || owner.getUuid() == player.getUuid()) {
			super.onPlayerCollision(player);
		}
	}
	
	@Override
	public void tick() {
		if (inGroundTime > 4) {
			dealtDamage = true;
		}
		Entity entity = getOwner();
		if (entity instanceof PlayerEntity && (dealtDamage || isNoClip() || getY() <= 0)) {
			if (!isOwnerAlive()) {
				if (!world.isClient && pickupType == PersistentProjectileEntity.PickupPermission.ALLOWED) {
					dropStack(asItemStack(), 0.1f);
				}
				remove(RemovalReason.DISCARDED);
			}
			else {
				setNoClip(true);
				Vec3d vec3d = new Vec3d(entity.getX() - getX(), entity.getEyeY() - getY(), entity.getZ() - getZ());
				setPos(getX(), getY() + vec3d.y * 0.1, getZ());
				if (world.isClient) {
					lastRenderY = getY();
				}
				setVelocity(getVelocity().multiply(0.95d).add(vec3d.normalize().multiply(0.3)));
			}
		}
		super.tick();
	}
	
	@Override
	protected void age() {
		if (pickupType != PickupPermission.ALLOWED) {
			super.age();
		}
	}
	
	@Environment(EnvType.CLIENT)
	@Override
	public boolean shouldRender(double cameraX, double cameraY, double cameraZ) {
		return true;
	}
	
	@Override
	public void readCustomDataFromNbt(NbtCompound nbt) {
		super.readCustomDataFromNbt(nbt);
		spear = ItemStack.fromNbt(nbt.getCompound("Spear"));
		dealtDamage = nbt.getBoolean("DealtDamage");
	}
	
	@Override
	public void writeCustomDataToNbt(NbtCompound nbt) {
		super.writeCustomDataToNbt(nbt);
		nbt.put("Spear", spear.writeNbt(new NbtCompound()));
		nbt.putBoolean("DealtDamage", dealtDamage);
	}
	
	private boolean isOwnerAlive() {
		Entity entity = getOwner();
		if (entity != null && entity.isAlive()) {
			return !(entity instanceof ServerPlayerEntity) || !entity.isSpectator();
		}
		else {
			return false;
		}
	}
}
