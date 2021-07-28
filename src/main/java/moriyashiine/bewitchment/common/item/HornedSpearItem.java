package moriyashiine.bewitchment.common.item;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import com.jamieswhiteshirt.reachentityattributes.ReachEntityAttributes;
import moriyashiine.bewitchment.client.network.packet.SyncHornedSpearEntity;
import moriyashiine.bewitchment.common.entity.projectile.HornedSpearEntity;
import moriyashiine.bewitchment.common.registry.BWEntityTypes;
import moriyashiine.bewitchment.common.registry.BWSoundEvents;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.sound.SoundCategory;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

import java.util.UUID;

public class HornedSpearItem extends SwordItem {
	private static final EntityAttributeModifier REACH_MODIFIER = new EntityAttributeModifier(UUID.fromString("1d25c6a0-3f2c-4d4e-8f5f-941f51aab1b7"), "Weapon modifier", 2, EntityAttributeModifier.Operation.ADDITION);
	
	public HornedSpearItem(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
		super(toolMaterial, attackDamage, attackSpeed, settings);
	}
	
	@Override
	public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot slot) {
		Multimap<EntityAttribute, EntityAttributeModifier> map = LinkedHashMultimap.create(super.getAttributeModifiers(slot));
		if (slot == EquipmentSlot.MAINHAND) {
			map.put(ReachEntityAttributes.ATTACK_RANGE, REACH_MODIFIER);
		}
		return map;
	}
	
	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		ItemStack stack = user.getStackInHand(hand);
		if (stack.getDamage() >= stack.getMaxDamage() - 1) {
			return TypedActionResult.fail(stack);
		}
		return ItemUsage.consumeHeldItem(world, user, hand);
	}
	
	@Override
	public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
		int timer = getMaxUseTime(stack) - remainingUseTicks;
		if (timer >= 10) {
			if (!world.isClient) {
				spawnEntity(world, user, stack);
			}
			if (user instanceof PlayerEntity) {
				((PlayerEntity) user).incrementStat(Stats.USED.getOrCreateStat(this));
			}
		}
	}
	
	@Override
	public UseAction getUseAction(ItemStack stack) {
		return UseAction.SPEAR;
	}
	
	@Override
	public int getMaxUseTime(ItemStack stack) {
		return 72000;
	}
	
	public static void spawnEntity(World world, LivingEntity owner, ItemStack stack) {
		stack.damage(1, owner, stackUser -> stackUser.sendToolBreakStatus(stackUser.getActiveHand()));
		HornedSpearEntity spear = new HornedSpearEntity(BWEntityTypes.HORNED_SPEAR, owner, world, stack.copy());
		spear.setProperties(owner, owner.getPitch(), owner.getYaw(), 0, 3, 1);
		if (owner instanceof PlayerEntity) {
			if (((PlayerEntity) owner).isCreative()) {
				spear.pickupType = PersistentProjectileEntity.PickupPermission.CREATIVE_ONLY;
			}
		}
		else {
			spear.pickupType = PersistentProjectileEntity.PickupPermission.DISALLOWED;
		}
		world.spawnEntity(spear);
		PlayerLookup.tracking(spear).forEach(serverPlayerEntity -> SyncHornedSpearEntity.send(serverPlayerEntity, spear));
		world.playSoundFromEntity(null, spear, BWSoundEvents.ITEM_HORNED_SPEAR_USE, SoundCategory.PLAYERS, 1, 1);
		if (owner instanceof PlayerEntity && !((PlayerEntity) owner).isCreative()) {
			stack.decrement(1);
		}
	}
}
