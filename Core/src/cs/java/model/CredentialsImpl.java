package cs.java.model;

public class CredentialsImpl implements Credentials {

    private final String password;
    private final String username;

    public CredentialsImpl(String username, String password) {
        this.password = password;
        this.username = username;
    }

    @Override
		public String getPassword() {
        return password;
    }

    @Override
		public String getUsername() {
        return username;
    }

}
