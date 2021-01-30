package moriyashiine.bewitchment.api.interfaces.entity;

import net.minecraft.util.Identifier;

public interface TransformationAccessor {
	Identifier getTransformation();
	
	void setTransformation(Identifier transformation);
	
	boolean getAlternateForm();
	
	void setAlternateForm(boolean alternateForm);
}
