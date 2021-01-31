package moriyashiine.bewitchment.api.item;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.api.registry.Sigil;
import moriyashiine.bewitchment.common.block.entity.interfaces.SigilHolder;
import moriyashiine.bewitchment.common.registry.BWEntityTypes;
import moriyashiine.bewitchment.common.registry.BWObjects;
import moriyashiine.bewitchment.common.registry.BWRegistries;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.BlockState;
import net.minecraft.block.DoorBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@SuppressWarnings("ConstantConditions")
public class SigilItem extends Item {
	public final Sigil sigil;
	
	public SigilItem(Settings settings, Sigil sigil) {
		super(settings);
		this.sigil = sigil;
	}
	
	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		World world = context.getWorld();
		BlockPos pos = context.getBlockPos();
		ItemStack stack = context.getStack();
		PlayerEntity player = context.getPlayer();
		boolean client = world.isClient;
		BlockState state = world.getBlockState(pos);
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (!sigil.active) {
			ItemPlacementContext placementContext = new ItemPlacementContext(context);
			if (!state.canReplace(placementContext)) {
				pos = pos.offset(context.getSide());
			}
			if (!world.getBlockState(pos).canReplace(placementContext)) {
				return ActionResult.PASS;
			}
			BlockState sigilState = BWObjects.SIGIL.getPlacementState(placementContext);
			if (sigilState.canPlaceAt(world, pos)) {
				if (!client) {
					world.playSound(null, pos, sigilState.getSoundGroup().getPlaceSound(), SoundCategory.BLOCKS, 1, MathHelper.nextFloat(world.random, 0.8f, 1.2f));
					world.setBlockState(pos, sigilState);
					blockEntity = world.getBlockEntity(pos);
					((SigilHolder) blockEntity).setSigil(sigil);
					((SigilHolder) blockEntity).setUses(sigil.uses * (BewitchmentAPI.getFamiliar(player) == BWEntityTypes.SNAKE ? 2 : 1));
					((SigilHolder) blockEntity).setOwner(player.getUuid());
					((BlockEntityClientSerializable) blockEntity).sync();
					blockEntity.markDirty();
					if (player instanceof ServerPlayerEntity) {
						Criteria.PLACED_BLOCK.trigger((ServerPlayerEntity) player, pos, stack);
						if (!player.isCreative()) {
							stack.decrement(1);
						}
					}
				}
				return ActionResult.success(client);
			}
		}
		else if (blockEntity instanceof SigilHolder && state.getBlock() != BWObjects.SIGIL) {
			if (state.getBlock() instanceof DoorBlock && state.get(DoorBlock.HALF) == DoubleBlockHalf.UPPER) {
				blockEntity = world.getBlockEntity(pos.down());
			}
			if (((SigilHolder) blockEntity).getSigil() == null) {
				if (!client) {
					((SigilHolder) blockEntity).setSigil(sigil);
					((SigilHolder) blockEntity).setUses(sigil.uses * (BewitchmentAPI.getFamiliar(player) == BWEntityTypes.SNAKE ? 2 : 1));
					((SigilHolder) blockEntity).setOwner(player.getUuid());
					((BlockEntityClientSerializable) blockEntity).sync();
					blockEntity.markDirty();
					if (!player.isCreative()) {
						stack.decrement(1);
					}
				}
				return ActionResult.success(client);
			}
		}
		return super.useOnBlock(context);
	}
	
	@Override
	public String getTranslationKey() {
		return BWObjects.SIGIL.getTranslationKey();
	}
	
	@Environment(EnvType.CLIENT)
	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		tooltip.add(new TranslatableText("sigil." + BWRegistries.SIGILS.getId(sigil).toString().replace(":", ".")).formatted(Formatting.GRAY));
	}
}
