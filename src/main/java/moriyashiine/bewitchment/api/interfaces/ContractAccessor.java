package moriyashiine.bewitchment.api.interfaces;

import moriyashiine.bewitchment.api.registry.Contract;
import net.minecraft.entity.LivingEntity;

import java.util.List;
import java.util.Optional;

public interface ContractAccessor {
	static Optional<ContractAccessor> of(LivingEntity entity) {
		return Optional.of(((ContractAccessor) entity));
	}
	
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
	
	boolean hasNegativeEffects();
}
