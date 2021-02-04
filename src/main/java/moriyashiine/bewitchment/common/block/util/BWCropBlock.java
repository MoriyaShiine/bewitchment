package moriyashiine.bewitchment.common.block.util;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.common.registry.BWDamageSources;
import moriyashiine.bewitchment.common.registry.BWObjects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemConvertible;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class BWCropBlock extends CropBlock {
	private static final VoxelShape[] AGE_TO_SHAPE = {Block.createCuboidShape(0, 0, 0, 16, 2, 16), Block.createCuboidShape(0, 0, 0, 16, 4, 16), Block.createCuboidShape(0, 0, 0, 16, 6, 16), Block.createCuboidShape(0, 0, 0, 16, 8, 16), Block.createCuboidShape(0, 0, 0, 16, 10, 16), Block.createCuboidShape(0, 0, 0, 16, 12, 16), Block.createCuboidShape(0, 0, 0, 16, 14, 16), Block.createCuboidShape(0, 0, 0, 16, 16, 16)};
	
	public BWCropBlock(Settings settings) {
		super(settings);
	}
	
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		int age = getAge(state);
		int modelAge = 0;
		if (this == BWObjects.ACONITE_CROP) {
			modelAge = age == 0 ? 1 : age == 1 ? 3 : age == 2 ? 5 : 7;
		}
		if (this == BWObjects.BELLADONNA_CROP) {
			modelAge = age == 0 ? 1 : age == 1 ? 3 : age == 2 ? 5 : 6;
		}
		if (this == BWObjects.GARLIC_CROP) {
			modelAge = age == 0 ? 2 : age == 1 ? 3 : age == 2 ? 5 : 6;
		}
		if (this == BWObjects.MANDRAKE_CROP) {
			modelAge = age == 0 ? 1 : age == 1 ? 2 : age == 2 ? 3 : 3;
		}
		return AGE_TO_SHAPE[modelAge];
	}
	
	@Environment(EnvType.CLIENT)
	@Override
	protected ItemConvertible getSeedsItem() {
		return this == BWObjects.ACONITE_CROP ? BWObjects.ACONITE_SEEDS : this == BWObjects.BELLADONNA_CROP ? BWObjects.BELLADONNA_SEEDS : this == BWObjects.GARLIC_CROP ? BWObjects.GARLIC : BWObjects.MANDRAKE_SEEDS;
	}
	
	@Override
	public IntProperty getAgeProperty() {
		return Properties.AGE_3;
	}
	
	@Override
	public int getMaxAge() {
		return 3;
	}
	
	@Override
	protected int getGrowthAmount(World world) {
		return MathHelper.nextInt(world.random, 1, 2);
	}
	
	@Override
	public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
		super.onEntityCollision(state, world, pos, entity);
		if (entity instanceof LivingEntity) {
			boolean damage = false;
			if (this == BWObjects.GARLIC_CROP && BewitchmentAPI.isVampire(entity, true)) {
				damage = true;
			}
			else if (this == BWObjects.ACONITE_CROP && BewitchmentAPI.isWerewolf(entity, true)) {
				damage = true;
			}
			if (damage) {
				entity.damage(BWDamageSources.MAGIC_COPY, ((LivingEntity) entity).getMaxHealth() * 1 / 4f);
			}
		}
	}
	
	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(getAgeProperty());
	}
}
