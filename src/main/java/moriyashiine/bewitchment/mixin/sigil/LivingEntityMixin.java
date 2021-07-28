package moriyashiine.bewitchment.mixin.sigil;

import moriyashiine.bewitchment.common.block.entity.interfaces.SigilHolder;
import moriyashiine.bewitchment.common.misc.BWUtil;
import moriyashiine.bewitchment.common.registry.BWSigils;
import moriyashiine.bewitchment.common.world.BWWorldState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("ConstantConditions")
@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
	public LivingEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}
	
	@ModifyVariable(method = "applyArmorToDamage", at = @At("HEAD"))
	private float modifyDamage(float amount, DamageSource source) {
		if (!world.isClient && source == DamageSource.FALL) {
			BWWorldState worldState = BWWorldState.get(world);
			BlockPos sigilPos = BWUtil.getClosestBlockPos(getBlockPos(), 16, currentPos -> worldState.potentialSigils.contains(currentPos.asLong()) && world.getBlockEntity(currentPos) instanceof SigilHolder && ((SigilHolder) world.getBlockEntity(currentPos)).getSigil() == BWSigils.HEAVY);
			if (sigilPos != null) {
				BlockEntity blockEntity = world.getBlockEntity(sigilPos);
				SigilHolder sigil = (SigilHolder) blockEntity;
				if (sigil.test(this)) {
					sigil.setUses(sigil.getUses() - 1);
					blockEntity.markDirty();
					amount *= 3;
				}
			}
		}
		return amount;
	}
	
	@Inject(method = "canHaveStatusEffect", at = @At("RETURN"), cancellable = true)
	private void canHaveStatusEffect(StatusEffectInstance effect, CallbackInfoReturnable<Boolean> callbackInfo) {
		if (callbackInfo.getReturnValue() && !world.isClient && !effect.isAmbient() && effect.getEffectType().getType() != StatusEffectType.HARMFUL) {
			BWWorldState worldState = BWWorldState.get(world);
			BlockPos sigilPos = BWUtil.getClosestBlockPos(getBlockPos(), 16, currentPos -> worldState.potentialSigils.contains(currentPos.asLong()) && world.getBlockEntity(currentPos) instanceof SigilHolder && ((SigilHolder) world.getBlockEntity(currentPos)).getSigil() == BWSigils.RUIN);
			if (sigilPos != null) {
				BlockEntity blockEntity = world.getBlockEntity(sigilPos);
				SigilHolder sigil = (SigilHolder) blockEntity;
				if (sigil.test(this)) {
					sigil.setUses(sigil.getUses() - 1);
					blockEntity.markDirty();
					callbackInfo.setReturnValue(false);
				}
			}
		}
	}
}
