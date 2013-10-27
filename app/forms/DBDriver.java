package forms;

import java.io.File;

import play.data.validation.Constraints.Required;

public class DBDriver {

	@Required
	public String name;
	@Required
	public String driverClass;
	public File jarFile;
}
