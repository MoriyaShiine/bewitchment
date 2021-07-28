package moriyashiine.bewitchment.common.block.juniper;

import com.terraformersmc.terraform.wood.block.TerraformDoorBlock;
import moriyashiine.bewitchment.common.block.entity.TaglockHolderBlockEntity;
import moriyashiine.bewitchment.common.block.entity.interfaces.TaglockHolder;
import moriyashiine.bewitchment.common.block.util.interfaces.SpecialDoor;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("ConstantConditions")
public class JuniperDoorBlock extends TerraformDoorBlock implements BlockEntityProvider, SpecialDoor {
	public JuniperDoorBlock(Settings settings) {
		super(settings);
	}
	
	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new TaglockHolderBlockEntity(pos, state);
	}
	
	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		TaglockHolder.onUse(world, state.get(HALF) == DoubleBlockHalf.UPPER ? pos.down() : pos, player);
		return super.onUse(state, world, pos, player, hand, hit);
	}
	
	@Override
	public ActionResult onSpecialUse(BlockState state, World world, BlockPos pos, LivingEntity user, Hand hand) {
		TaglockHolder.onUse(world, state.get(HALF) == DoubleBlockHalf.UPPER ? pos.down() : pos, user);
		return ActionResult.PASS;
	}
	
	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
		super.onPlaced(world, pos, state, placer, itemStack);
		if (!world.isClient && placer != null) {
			BlockEntity blockEntity = world.getBlockEntity(state.get(HALF) == DoubleBlockHalf.UPPER ? pos.down() : pos);
			TaglockHolder taglockHolder = (TaglockHolder) blockEntity;
			taglockHolder.setOwner(placer.getUuid());
			taglockHolder.syncTaglockHolder(world, blockEntity);
			blockEntity.markDirty();
		}
	}
	
	@Override
	public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
		if (!world.isClient && state.getBlock() != newState.getBlock()) {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof TaglockHolder) {
				ItemScatterer.spawn(world, pos, ((TaglockHolder) blockEntity).getTaglockInventory());
			}
		}
		super.onStateReplaced(state, world, pos, newState, moved);
	}
}
