package services;

import play.Configuration;
import play.Play;

public class PlayConfigurationProvider {

	final Configuration config;

	public PlayConfigurationProvider() {
		this.config = Play.application().configuration();
	}

	public boolean getSmtpSSL() {
		return config.getBoolean("smtp.ssl");
	}

	public Integer getSmtpPort() {
		return config.getInt("smtp.port");
	}

	public String getSmtpHost() {
		return config.getString("smtp.host");
	}

	public String getSmtpUsername() {
		return config.getString("smtp.username");
	}

	public String getSmtpPassword() {
		return config.getString("smtp.password");
	}

}
