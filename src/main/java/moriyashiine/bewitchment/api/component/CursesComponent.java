/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.api.component;

import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import moriyashiine.bewitchment.api.registry.Curse;
import moriyashiine.bewitchment.common.registry.BWRegistries;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class CursesComponent implements ServerTickingComponent {
	private final LivingEntity obj;
	private final List<Curse.Instance> curses = new ArrayList<>();

	public CursesComponent(LivingEntity obj) {
		this.obj = obj;
	}

	@Override
	public void readFromNbt(NbtCompound tag) {
		NbtList cursesList = tag.getList("Curses", NbtElement.COMPOUND_TYPE);
		for (int i = 0; i < cursesList.size(); i++) {
			NbtCompound curseCompound = cursesList.getCompound(i);
			addCurse(new Curse.Instance(BWRegistries.CURSE.get(new Identifier(curseCompound.getString("Curse"))), curseCompound.getInt("Duration")));
		}
	}

	@Override
	public void writeToNbt(NbtCompound tag) {
		tag.put("Curses", toNbtCurse());
	}

	@Override
	public void serverTick() {
		for (int i = curses.size() - 1; i >= 0; i--) {
			Curse.Instance instance = curses.get(i);
			instance.curse.tick(obj);
			instance.duration--;
			if (instance.duration <= 0) {
				curses.remove(i);
			}
		}
	}

	public List<Curse.Instance> getCurses() {
		return curses;
	}

	public boolean hasCurse(Curse curse) {
		return getCurses().stream().anyMatch(instance -> instance.curse == curse);
	}

	public void addCurse(Curse.Instance instance) {
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

	public void removeCurse(Curse curse) {
		if (hasCurse(curse)) {
			for (Curse.Instance instance : getCurses()) {
				if (instance.curse == curse) {
					instance.duration = 0;
				}
			}
		}
	}

	@SuppressWarnings("ConstantConditions")
	public NbtList toNbtCurse() {
		NbtList cursesList = new NbtList();
		for (Curse.Instance instance : getCurses()) {
			NbtCompound curseCompound = new NbtCompound();
			curseCompound.putString("Curse", BWRegistries.CURSE.getId(instance.curse).toString());
			curseCompound.putInt("Duration", instance.duration);
			cursesList.add(curseCompound);
		}
		return cursesList;
	}
}
