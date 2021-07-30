package moriyashiine.bewitchment.api.component;

import dev.onyxstudios.cca.api.v3.component.ComponentV3;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import moriyashiine.bewitchment.api.registry.Contract;
import moriyashiine.bewitchment.common.registry.BWComponents;
import moriyashiine.bewitchment.common.registry.BWRegistries;
import moriyashiine.bewitchment.common.registry.BWStatusEffects;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ContractsComponent implements ComponentV3, ServerTickingComponent {
	private final PlayerEntity obj;
	private final List<Contract.Instance> contracts = new ArrayList<>();
	
	public ContractsComponent(PlayerEntity obj) {
		this.obj = obj;
	}
	
	@Override
	public void readFromNbt(NbtCompound tag) {
		NbtList contracts = tag.getList("Contracts", NbtType.COMPOUND);
		for (int i = 0; i < contracts.size(); i++) {
			NbtCompound contract = contracts.getCompound(i);
			addContract(new Contract.Instance(BWRegistries.CONTRACTS.get(new Identifier(contract.getString("Contract"))), contract.getInt("Duration"), contract.getInt("Cost")));
		}
	}
	
	@Override
	public void writeToNbt(NbtCompound tag) {
		tag.put("Contracts", toNbtContract());
	}
	
	@Override
	public void serverTick() {
		int level = 0;
		for (int i = contracts.size() - 1; i >= 0; i--) {
			Contract.Instance instance = contracts.get(i);
			level += instance.cost;
			instance.contract.tick(obj);
			instance.duration--;
			if (instance.duration <= 0) {
				contracts.remove(i);
			}
		}
		if (level > 0) {
			obj.addStatusEffect(new StatusEffectInstance(BWStatusEffects.PACT, 10, level - 1, true, false));
			if (obj.getHealth() > obj.getMaxHealth()) {
				obj.setHealth(obj.getMaxHealth());
			}
		}
	}
	
	public List<Contract.Instance> getContracts() {
		return contracts;
	}
	
	public boolean hasContract(Contract contract) {
		return getContracts().stream().anyMatch(instance -> instance.contract == contract);
	}
	
	public void addContract(Contract.Instance instance) {
		if (hasContract(instance.contract)) {
			for (Contract.Instance contract : getContracts()) {
				if (contract.contract == instance.contract) {
					contract.duration = instance.duration;
					return;
				}
			}
		}
		getContracts().add(instance);
	}
	
	public void removeContract(Contract contract) {
		if (hasContract(contract)) {
			for (Contract.Instance instance : getContracts()) {
				if (instance.contract == contract) {
					instance.duration = 0;
				}
			}
		}
	}
	
	@SuppressWarnings("ConstantConditions")
	public NbtList toNbtContract() {
		NbtList contractList = new NbtList();
		for (Contract.Instance instance : getContracts()) {
			NbtCompound contract = new NbtCompound();
			contract.putString("Contract", BWRegistries.CONTRACTS.getId(instance.contract).toString());
			contract.putInt("Duration", instance.duration);
			contract.putInt("Cost", instance.cost);
			contractList.add(contract);
		}
		return contractList;
	}
	
	public static ContractsComponent get(PlayerEntity obj) {
		return BWComponents.CONTRACTS_COMPONENT.get(obj);
	}
	
	public static Optional<ContractsComponent> maybeGet(PlayerEntity obj) {
		return BWComponents.CONTRACTS_COMPONENT.maybeGet(obj);
	}
}
