package moriyashiine.bewitchment.api.interfaces.entity;

import moriyashiine.bewitchment.api.registry.Curse;
import moriyashiine.bewitchment.common.registry.BWRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;

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
	
	default ListTag toTagCurse() {
		ListTag curses = new ListTag();
		for (Curse.Instance instance : getCurses()) {
			CompoundTag curseTag = new CompoundTag();
			curseTag.putString("Curse", BWRegistries.CURSES.getId(instance.curse).toString());
			curseTag.putInt("Duration", instance.duration);
			curses.add(curseTag);
		}
		return curses;
	}
}
