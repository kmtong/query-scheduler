package forms;

import play.data.validation.Constraints.Required;

public class DBConnection {

	@Required
	public String name;
	@Required
	public Long driverId;
	@Required
	public String url;

	public String username;
	public String password;
}
