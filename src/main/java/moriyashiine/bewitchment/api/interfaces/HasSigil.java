package moriyashiine.bewitchment.api.interfaces;

import moriyashiine.bewitchment.api.registry.Sigil;
import moriyashiine.bewitchment.common.registry.BWObjects;
import moriyashiine.bewitchment.common.registry.BWRegistries;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;
import java.util.UUID;

@SuppressWarnings("ConstantConditions")
public interface HasSigil {
	Sigil getSigil();
	
	void setSigil(Sigil sigil);
	
	int getUses();
	
	void setUses(int uses);
	
	List<UUID> getEntities();
	
	boolean getModeOnWhitelist();
	
	void setModeOnWhitelist(boolean modeOnWhitelist);
	
	default void fromTagSigil(CompoundTag tag) {
		ListTag entities = tag.getList("Entities", NbtType.STRING);
		for (int i = 0; i < entities.size(); i++) {
			getEntities().add(UUID.fromString(entities.getString(i)));
		}
		setSigil(BWRegistries.SIGILS.get(new Identifier(tag.getString("Sigil"))));
		setUses(tag.getInt("Uses"));
		setModeOnWhitelist(tag.getBoolean("ModeOnWhitelist"));
	}
	
	default void toTagSigil(CompoundTag tag) {
		ListTag entities = new ListTag();
		for (int i = 0; i < getEntities().size(); i++) {
			entities.add(StringTag.of(getEntities().get(i).toString()));
		}
		tag.put("Entities", entities);
		if (getSigil() != null) {
			tag.putString("Sigil", BWRegistries.SIGILS.getId(getSigil()).toString());
		}
		tag.putInt("Uses", getUses());
		tag.putBoolean("ModeOnWhitelist", getModeOnWhitelist());
	}
	
	default ActionResult use(World world, BlockPos pos, LivingEntity user, Hand hand) {
		if (getSigil() != null && getSigil().active && test(user)) {
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
	
	default boolean test(Entity entity) {
		if (!getEntities().isEmpty()) {
			if (getModeOnWhitelist()) {
				return getEntities().contains(entity.getUuid());
			}
			return !getEntities().contains(entity.getUuid());
		}
		return true;
	}
	
	static ActionResult onUse(World world, BlockPos pos, LivingEntity user, Hand hand) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof HasSigil) {
			return ((HasSigil) blockEntity).use(world, pos, user, hand);
		}
		return ActionResult.PASS;
	}
}
