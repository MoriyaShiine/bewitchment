package moriyashiine.bewitchment.common.block.entity.interfaces;

import moriyashiine.bewitchment.api.registry.Sigil;
import moriyashiine.bewitchment.common.block.entity.DragonsBloodChestBlockEntity;
import moriyashiine.bewitchment.common.block.entity.SigilBlockEntity;
import moriyashiine.bewitchment.common.registry.BWObjects;
import moriyashiine.bewitchment.common.registry.BWRegistries;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;
import java.util.UUID;

@SuppressWarnings("ConstantConditions")
public interface SigilHolder {
	List<UUID> getEntities();

	UUID getOwner();

	void setOwner(UUID owner);

	Sigil getSigil();

	void setSigil(Sigil sigil);

	int getUses();

	void setUses(int uses);

	boolean getModeOnWhitelist();

	void setModeOnWhitelist(boolean modeOnWhitelist);

	default void fromNbtSigil(NbtCompound nbt) {
		NbtList entitiesList = nbt.getList("Entities", NbtType.STRING);
		for (int i = 0; i < entitiesList.size(); i++) {
			getEntities().add(UUID.fromString(entitiesList.getString(i)));
		}
		if (nbt.contains("Owner")) {
			setOwner(nbt.getUuid("Owner"));
		}
		setSigil(BWRegistries.SIGILS.get(new Identifier(nbt.getString("Sigil"))));
		setUses(nbt.getInt("Uses"));
		setModeOnWhitelist(nbt.getBoolean("ModeOnWhitelist"));
	}

	default void toNbtSigil(NbtCompound nbt) {
		NbtList entitiesList = new NbtList();
		for (int i = 0; i < getEntities().size(); i++) {
			entitiesList.add(NbtString.of(getEntities().get(i).toString()));
		}
		nbt.put("Entities", entitiesList);
		if (getOwner() != null) {
			nbt.putUuid("Owner", getOwner());
		}
		if (getSigil() != null) {
			nbt.putString("Sigil", BWRegistries.SIGILS.getId(getSigil()).toString());
		}
		nbt.putInt("Uses", getUses());
		nbt.putBoolean("ModeOnWhitelist", getModeOnWhitelist());
	}

	default void use(World world, BlockPos pos, LivingEntity user, Hand hand) {
		if (getSigil() != null && getSigil().active && test(user)) {
			ActionResult result = getSigil().use(world, pos, user, hand);
			if (result == ActionResult.SUCCESS) {
				BlockEntity blockEntity = world.getBlockEntity(pos);
				setUses(getUses() - 1);
				syncSigilHolder(blockEntity);
				blockEntity.markDirty();
			}
		}
	}

	default void tick(World world, BlockPos pos, BlockEntity blockEntity) {
		if (world != null && !world.isClient) {
			if (getSigil() != null) {
				if (!getSigil().active) {
					int used = getSigil().tick(world, pos);
					if (used > 0) {
						setUses(getUses() - used);
						syncSigilHolder(blockEntity);
						blockEntity.markDirty();
					}
				}
				if (getUses() <= 0) {
					getEntities().clear();
					setOwner(null);
					setSigil(null);
					setUses(0);
					setModeOnWhitelist(false);
					syncSigilHolder(blockEntity);
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

	default void syncSigilHolder(BlockEntity blockEntity) {
		if (blockEntity instanceof SigilBlockEntity sigilBlockEntity) {
			sigilBlockEntity.sync();
		} else if (blockEntity instanceof DragonsBloodChestBlockEntity dragonsBloodChestBlockEntity) {
			dragonsBloodChestBlockEntity.sync();
		}
	}

	static void onUse(World world, BlockPos pos, LivingEntity user, Hand hand) {
		if (world.getBlockEntity(pos) instanceof SigilHolder sigilHolder && sigilHolder.test(user)) {
			sigilHolder.use(world, pos, user, hand);
		}
	}
}
