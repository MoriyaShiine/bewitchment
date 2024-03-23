/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.mixin.ritual;

import moriyashiine.bewitchment.common.block.entity.GlyphBlockEntity;
import moriyashiine.bewitchment.common.misc.BWUtil;
import moriyashiine.bewitchment.common.registry.BWRitualFunctions;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
	public LivingEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@ModifyVariable(method = "applyArmorToDamage", at = @At("HEAD"), argsOnly = true)
	private float modifyDamage(float amount, DamageSource source) {
		if (!getWorld().isClient && !source.isIn(DamageTypeTags.BYPASSES_INVULNERABILITY) && getWorld().getWorldBorder().contains(getBlockPos())) {
			for (BlockPos foundPos : BWUtil.getBlockPoses(getBlockPos(), 16)) {
				if (getWorld().getWorldBorder().contains(foundPos) && getWorld().getBlockEntity(foundPos) instanceof GlyphBlockEntity glyph && glyph.ritualFunction == BWRitualFunctions.PREVENT_DAMAGE) {
					return 0;
				}
			}
		}
		return amount;
	}
}
