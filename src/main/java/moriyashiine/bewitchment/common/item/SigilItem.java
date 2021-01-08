package moriyashiine.bewitchment.common.item;

import moriyashiine.bewitchment.api.interfaces.HasSigil;
import moriyashiine.bewitchment.api.registry.Sigil;
import moriyashiine.bewitchment.common.block.entity.SigilBlockEntity;
import moriyashiine.bewitchment.common.registry.BWObjects;
import moriyashiine.bewitchment.common.registry.BWRegistries;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.BlockState;
import net.minecraft.block.DoorBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@SuppressWarnings("ConstantConditions")
public class SigilItem extends Item {
	public SigilItem(Settings settings) {
		super(settings);
	}
	
	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		World world = context.getWorld();
		BlockPos pos = context.getBlockPos();
		ItemStack stack = context.getStack();
		PlayerEntity player = context.getPlayer();
		Sigil sigil = BWRegistries.SIGILS.get(new Identifier(stack.getOrCreateTag().getString("Sigil")));
		boolean client = world.isClient;
		BlockState state = world.getBlockState(pos);
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (!sigil.active) {
			ItemPlacementContext placementContext = new ItemPlacementContext(context);
			if (!state.canReplace(placementContext)) {
				pos = pos.offset(context.getSide());
			}
			BlockState sigilBlock = BWObjects.SIGIL.getPlacementState(placementContext);
			if (sigilBlock.canPlaceAt(world, pos)) {
				if (!client) {
					world.playSound(null, pos, sigilBlock.getSoundGroup().getPlaceSound(), SoundCategory.BLOCKS, 1, MathHelper.nextFloat(world.random, 0.8f, 1.2f));
					world.setBlockState(pos, sigilBlock);
					blockEntity = world.getBlockEntity(pos);
					((HasSigil) blockEntity).setSigil(sigil);
					((HasSigil) blockEntity).setUses(sigil.uses);
					((SigilBlockEntity) blockEntity).syncSigil();
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
		else if (blockEntity instanceof HasSigil && state.getBlock() != BWObjects.SIGIL) {
			if (state.getBlock() instanceof DoorBlock && state.get(DoorBlock.HALF) == DoubleBlockHalf.UPPER) {
				blockEntity = world.getBlockEntity(pos.down());
			}
			if (((HasSigil) blockEntity).getSigil() == null) {
				((HasSigil) blockEntity).setSigil(sigil);
				((HasSigil) blockEntity).setUses(sigil.uses);
				blockEntity.markDirty();
				if (!player.isCreative()) {
					stack.decrement(1);
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
	
	@Override
	public void appendStacks(ItemGroup group, DefaultedList<ItemStack> stacks) {
		if (isIn(group)) {
			BWRegistries.SIGILS.forEach(sigil -> {
				ItemStack stack = new ItemStack(this);
				stack.getOrCreateTag().putString("Sigil", BWRegistries.SIGILS.getId(sigil).toString());
				stack.getOrCreateTag().putInt("Type", RANDOM.nextInt(10));
				stacks.add(stack);
			});
		}
	}
	
	@Environment(EnvType.CLIENT)
	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		if (stack.hasTag() && stack.getOrCreateTag().contains("Sigil")) {
			tooltip.add(new TranslatableText("sigil." + stack.getOrCreateTag().getString("Sigil").replace(":", ".")).setStyle(Style.EMPTY.withColor(Formatting.GRAY)));
		}
	}
}
