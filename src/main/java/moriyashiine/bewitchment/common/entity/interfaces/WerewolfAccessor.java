package moriyashiine.bewitchment.common.entity.interfaces;

public interface WerewolfAccessor {
	boolean getForcedTransformation();
	
	void setForcedTransformation(boolean forcedTransformation);
	
	int getWerewolfVariant();
	
	void setWerewolfVariant(int variant);
}
