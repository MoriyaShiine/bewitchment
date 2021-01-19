package moriyashiine.bewitchment.common.block.elder;

import com.terraformersmc.terraform.wood.block.TerraformDoorBlock;
import moriyashiine.bewitchment.api.interfaces.misc.Lockable;
import moriyashiine.bewitchment.common.block.entity.LockableBlockEntity;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("ConstantConditions")
public class ElderDoorBlock extends TerraformDoorBlock implements BlockEntityProvider {
	public ElderDoorBlock(Settings settings) {
		super(settings);
	}
	
	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockView world) {
		return new LockableBlockEntity();
	}
	
	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		ActionResult result = Lockable.onUse(world, state.get(HALF) == DoubleBlockHalf.UPPER ? pos.down() : pos, player, hand);
		if (result != ActionResult.PASS) {
			return result;
		}
		return super.onUse(state, world, pos, player, hand, hit);
	}
	
	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
		super.onPlaced(world, pos, state, placer, itemStack);
		if (!world.isClient && placer != null) {
			BlockEntity blockEntity = world.getBlockEntity(state.get(HALF) == DoubleBlockHalf.UPPER ? pos.down() : pos);
			Lockable lockable = (Lockable) blockEntity;
			lockable.setOwner(placer.getUuid());
			lockable.syncLockable(world, blockEntity);
			blockEntity.markDirty();
		}
	}
}
