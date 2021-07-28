package moriyashiine.bewitchment.api.interfaces.entity;

import moriyashiine.bewitchment.api.registry.Contract;
import moriyashiine.bewitchment.common.registry.BWRegistries;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;

import java.util.List;

@SuppressWarnings("ConstantConditions")
public interface ContractAccessor {
	List<Contract.Instance> getContracts();
	
	default boolean hasContract(Contract contract) {
		return getContracts().stream().anyMatch(instance -> instance.contract == contract);
	}
	
	default void addContract(Contract.Instance instance) {
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
	
	default void removeContract(Contract contract) {
		if (hasContract(contract)) {
			for (Contract.Instance instance : getContracts()) {
				if (instance.contract == contract) {
					instance.duration = 0;
				}
			}
		}
	}
	
	default NbtList toNbtContract() {
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
}
