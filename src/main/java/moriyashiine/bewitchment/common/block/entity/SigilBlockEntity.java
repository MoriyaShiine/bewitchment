/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.bewitchment.common.block.entity;

import moriyashiine.bewitchment.api.registry.Sigil;
import moriyashiine.bewitchment.common.block.entity.interfaces.SigilHolder;
import moriyashiine.bewitchment.common.registry.BWBlockEntityTypes;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SigilBlockEntity extends BlockEntity implements SigilHolder {
	private final List<UUID> entities = new ArrayList<>();
	private UUID owner = null;
	private Sigil sigil = null;
	private int uses = 0;
	private boolean modeOnWhitelist = false;

	public SigilBlockEntity(BlockPos pos, BlockState state) {
		super(BWBlockEntityTypes.SIGIL, pos, state);
	}

	@Override
	public List<UUID> getEntities() {
		return entities;
	}

	@Override
	public UUID getOwner() {
		return owner;
	}

	@Override
	public void setOwner(UUID owner) {
		this.owner = owner;
	}

	@Override
	public Sigil getSigil() {
		return sigil;
	}

	@Override
	public void setSigil(Sigil sigil) {
		this.sigil = sigil;
	}

	@Override
	public int getUses() {
		return uses;
	}

	@Override
	public void setUses(int uses) {
		this.uses = uses;
	}

	@Override
	public boolean getModeOnWhitelist() {
		return modeOnWhitelist;
	}

	@Override
	public void setModeOnWhitelist(boolean modeOnWhitelist) {
		this.modeOnWhitelist = modeOnWhitelist;
	}

	@Override
	public NbtCompound toInitialChunkDataNbt() {
		NbtCompound nbt = super.toInitialChunkDataNbt();
		writeNbt(nbt);
		return nbt;
	}

	@Nullable
	@Override
	public Packet<ClientPlayPacketListener> toUpdatePacket() {
		return BlockEntityUpdateS2CPacket.create(this);
	}

	@Override
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);
		fromNbtSigil(nbt);
	}

	@Override
	protected void writeNbt(NbtCompound nbt) {
		super.writeNbt(nbt);
		toNbtSigil(nbt);
	}

	public void sync() {
		if (world != null && !world.isClient) {
			world.updateListeners(pos, getCachedState(), getCachedState(), Block.NOTIFY_LISTENERS);
		}
	}

	public static void tick(World world, BlockPos pos, BlockState state, SigilBlockEntity blockEntity) {
		blockEntity.tick(world, pos, blockEntity);
	}
}
