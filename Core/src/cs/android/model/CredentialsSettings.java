package cs.android.model;

import static cs.java.lang.Lang.is;
import cs.java.model.Credentials;
import cs.java.model.CredentialsImpl;

public class CredentialsSettings extends Settings implements Credentials {

	public static final String SETTING_USERNAME = "username";
	public static final String SETTING_PASSWORD = "password";

	public void clearCredentials() {
		save(new CredentialsImpl("", ""));
	}

	@Override
	public String getPassword() {
		return loadString(SETTING_PASSWORD);
	}

	@Override
	public String getUsername() {
		return loadString(SETTING_USERNAME);
	}

	public boolean hasLogin() {
		return is(getUsername(), getPassword());
	}

	public void save(Credentials credentials) {
		save(SETTING_USERNAME, credentials.getUsername(), SETTING_PASSWORD, credentials.getPassword());
	}

}
