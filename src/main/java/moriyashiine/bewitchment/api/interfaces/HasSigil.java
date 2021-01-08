package moriyashiine.bewitchment.api.interfaces;

import moriyashiine.bewitchment.api.registry.Sigil;
import moriyashiine.bewitchment.common.registry.BWObjects;
import moriyashiine.bewitchment.common.registry.BWRegistries;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@SuppressWarnings("ConstantConditions")
public interface HasSigil {
	Sigil getSigil();
	
	void setSigil(Sigil sigil);
	
	int getUses();
	
	void setUses(int uses);
	
	default void fromTagSigil(CompoundTag tag) {
		setSigil(BWRegistries.SIGILS.get(new Identifier(tag.getString("Sigil"))));
		setUses(tag.getInt("Uses"));
	}
	
	default void toTagSigil(CompoundTag tag) {
		if (getSigil() != null) {
			tag.putString("Sigil", BWRegistries.SIGILS.getId(getSigil()).toString());
		}
		tag.putInt("Uses", getUses());
	}
	
	default ActionResult use(World world, BlockPos pos, LivingEntity user, Hand hand) {
		if (getSigil() != null && getSigil().active) {
			ActionResult result = getSigil().use(world, pos, user, hand);
			if (result.isAccepted() || result == ActionResult.FAIL) {
				setUses(getUses() - 1);
				world.getBlockEntity(pos).markDirty();
			}
			return result;
		}
		return ActionResult.PASS;
	}
	
	default void tick(World world, BlockPos pos, BlockEntity blockEntity) {
		if (world != null) {
			if (getSigil() != null) {
				if (!getSigil().active) {
					int used = getSigil().tick(world, pos);
					if (used > 0) {
						setUses(getUses() - used);
						blockEntity.markDirty();
					}
				}
				if (getUses() <= 0) {
					setSigil(null);
					setUses(0);
					blockEntity.markDirty();
					if (blockEntity.getCachedState().getBlock() == BWObjects.SIGIL) {
						world.setBlockState(pos, Blocks.AIR.getDefaultState());
					}
				}
			}
		}
	}
	
	static ActionResult onUse(World world, BlockPos pos, LivingEntity user, Hand hand) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof HasSigil) {
			return ((HasSigil) blockEntity).use(world, pos, user, hand);
		}
		return ActionResult.PASS;
	}
}
