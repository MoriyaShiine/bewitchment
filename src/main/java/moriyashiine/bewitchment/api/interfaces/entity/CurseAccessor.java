package moriyashiine.bewitchment.api.interfaces.entity;

import moriyashiine.bewitchment.api.registry.Curse;
import moriyashiine.bewitchment.common.registry.BWRegistries;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;

import java.util.List;

@SuppressWarnings("ConstantConditions")
public interface CurseAccessor {
	List<Curse.Instance> getCurses();
	
	default boolean hasCurse(Curse curse) {
		return getCurses().stream().anyMatch(instance -> instance.curse == curse);
	}
	
	default void addCurse(Curse.Instance instance) {
		if (hasCurse(instance.curse)) {
			for (Curse.Instance curse : getCurses()) {
				if (curse.curse == instance.curse) {
					curse.duration = instance.duration;
					return;
				}
			}
		}
		getCurses().add(instance);
	}
	
	default void removeCurse(Curse curse) {
		if (hasCurse(curse)) {
			for (Curse.Instance instance : getCurses()) {
				if (instance.curse == curse) {
					instance.duration = 0;
				}
			}
		}
	}
	
	default NbtList toNbtCurse() {
		NbtList curseList = new NbtList();
		for (Curse.Instance instance : getCurses()) {
			NbtCompound curse = new NbtCompound();
			curse.putString("Curse", BWRegistries.CURSES.getId(instance.curse).toString());
			curse.putInt("Duration", instance.duration);
			curseList.add(curse);
		}
		return curseList;
	}
}
