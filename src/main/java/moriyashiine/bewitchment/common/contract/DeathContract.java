package moriyashiine.bewitchment.common.contract;

import dev.emi.trinkets.api.TrinketsApi;
import moriyashiine.bewitchment.api.interfaces.entity.ContractAccessor;
import moriyashiine.bewitchment.api.interfaces.entity.CurseAccessor;
import moriyashiine.bewitchment.api.registry.Contract;
import moriyashiine.bewitchment.api.registry.Curse;
import moriyashiine.bewitchment.common.registry.BWDamageSources;
import moriyashiine.bewitchment.common.registry.BWObjects;
import moriyashiine.bewitchment.common.registry.BWRegistries;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;

import java.util.Collections;

public class DeathContract extends Contract {
	public DeathContract(boolean doesBaphometOffer) {
		super(doesBaphometOffer);
	}
	
	public void tick(LivingEntity target, boolean includeNegative) {
		if (target.damage(BWDamageSources.DEATH, Float.MAX_VALUE)) {
			((ContractAccessor) target).removeContract(this);
		}
	}
	
	@Override
	public void finishUsing(LivingEntity user, boolean includeNegative) {
		if (includeNegative) {
			Curse curse = null;
			while (curse == null || curse.type != Curse.Type.LESSER) {
				curse = BWRegistries.CURSES.get(user.getRandom().nextInt(BWRegistries.CURSES.getEntries().size()));
			}
			((CurseAccessor) user).addCurse(new Curse.Instance(curse, user instanceof PlayerEntity && TrinketsApi.getTrinketsInventory((PlayerEntity) user).containsAny(Collections.singleton(BWObjects.NAZAR)) ? 12000 : 24000));
		}
	}
}
