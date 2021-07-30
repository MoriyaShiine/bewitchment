package moriyashiine.bewitchment.api.component;

import dev.onyxstudios.cca.api.v3.component.ComponentV3;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import moriyashiine.bewitchment.api.registry.Fortune;
import moriyashiine.bewitchment.common.registry.BWComponents;
import moriyashiine.bewitchment.common.registry.BWRegistries;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;

import java.util.Optional;

public class FortuneComponent implements ComponentV3, ServerTickingComponent {
	private final PlayerEntity obj;
	private Fortune.Instance fortune = null;
	
	public FortuneComponent(PlayerEntity obj) {
		this.obj = obj;
	}
	
	@Override
	public void readFromNbt(NbtCompound tag) {
		if (tag.contains("Fortune")) {
			setFortune(new Fortune.Instance(BWRegistries.FORTUNES.get(new Identifier(tag.getString("Fortune"))), tag.getInt("FortuneDuration")));
		}
	}
	
	@SuppressWarnings({"ConstantConditions", "NullableProblems"})
	@Override
	public void writeToNbt(NbtCompound tag) {
		if (getFortune() != null) {
			tag.putString("Fortune", BWRegistries.FORTUNES.getId(getFortune().fortune).toString());
			tag.putInt("FortuneDuration", getFortune().duration);
		}
	}
	
	@Override
	public void serverTick() {
		if (getFortune() != null) {
			if (getFortune().fortune.tick((ServerWorld) obj.world, obj)) {
				getFortune().duration = 0;
			}
			else {
				getFortune().duration--;
			}
			if (getFortune().duration <= 0) {
				if (getFortune().fortune.finish((ServerWorld) obj.world, obj)) {
					setFortune(null);
				}
				else {
					getFortune().duration = obj.getRandom().nextInt(120000);
				}
			}
		}
	}
	
	public Fortune.Instance getFortune() {
		return fortune;
	}
	
	public void setFortune(Fortune.Instance fortune) {
		this.fortune = fortune;
	}
	
	public static FortuneComponent get(PlayerEntity obj) {
		return BWComponents.FORTUNE_COMPONENT.get(obj);
	}
	
	public static Optional<FortuneComponent> maybeGet(PlayerEntity obj) {
		return BWComponents.FORTUNE_COMPONENT.maybeGet(obj);
	}
}
