package moriyashiine.bewitchment.mixin.ritual;

import moriyashiine.bewitchment.common.block.entity.GlyphBlockEntity;
import moriyashiine.bewitchment.common.misc.BWUtil;
import moriyashiine.bewitchment.common.registry.BWRitualFunctions;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@SuppressWarnings("ConstantConditions")
@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
	public LivingEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}
	
	@ModifyVariable(method = "applyArmorToDamage", at = @At("HEAD"))
	private float modifyDamage(float amount, DamageSource source) {
		if (!world.isClient && !source.isOutOfWorld() && world.getWorldBorder().contains(getBlockPos()) && !BWUtil.getBlockPoses(getBlockPos(), 16, currentPos -> world.getBlockEntity(currentPos) instanceof GlyphBlockEntity && ((GlyphBlockEntity) world.getBlockEntity(currentPos)).ritualFunction == BWRitualFunctions.PREVENT_DAMAGE).isEmpty()) {
			return 0;
		}
		return amount;
	}
}
