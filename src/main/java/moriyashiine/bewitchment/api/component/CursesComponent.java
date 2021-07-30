package moriyashiine.bewitchment.api.component;

import dev.onyxstudios.cca.api.v3.component.ComponentV3;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import moriyashiine.bewitchment.api.registry.Curse;
import moriyashiine.bewitchment.common.registry.BWComponents;
import moriyashiine.bewitchment.common.registry.BWRegistries;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CursesComponent implements ComponentV3, ServerTickingComponent {
	private final LivingEntity entity;
	private final List<Curse.Instance> curses = new ArrayList<>();
	
	public CursesComponent(LivingEntity player) {
		this.entity = player;
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
		NbtList curseList = new NbtList();
		for (Curse.Instance instance : getCurses()) {
			NbtCompound curse = new NbtCompound();
			curse.putString("Curse", BWRegistries.CURSES.getId(instance.curse).toString());
			curse.putInt("Duration", instance.duration);
			curseList.add(curse);
		}
		return curseList;
	}
	
	@Override
	public void readFromNbt(NbtCompound tag) {
		NbtList curses = tag.getList("Curses", NbtType.COMPOUND);
		for (int i = 0; i < curses.size(); i++) {
			NbtCompound curse = curses.getCompound(i);
			addCurse(new Curse.Instance(BWRegistries.CURSES.get(new Identifier(curse.getString("Curse"))), curse.getInt("Duration")));
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
			instance.curse.tick(entity);
			instance.duration--;
			if (instance.duration <= 0) {
				curses.remove(i);
			}
		}
	}
	
	public static CursesComponent get(LivingEntity entity) {
		return BWComponents.CURSES_COMPONENT.get(entity);
	}
	
	public static Optional<CursesComponent> maybeGet(LivingEntity entity) {
		return BWComponents.CURSES_COMPONENT.maybeGet(entity);
	}
}
