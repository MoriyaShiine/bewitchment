package moriyashiine.bewitchment.api.interfaces.entity;

import moriyashiine.bewitchment.api.registry.Transformation;

public interface TransformationAccessor {
	Transformation getTransformation();
	
	void setTransformation(Transformation transformation);
	
	boolean getAlternateForm();
	
	void setAlternateForm(boolean alternateForm);
}
