package cs.android.http;

import org.apache.http.HttpStatus;

public class HttpStatusException extends RuntimeException {

	private final int statusCode;

	public HttpStatusException(int statusCode) {
		super("HttpStatus " + statusCode);
		this.statusCode = statusCode;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public boolean isUnauthorized() {
		return statusCode == HttpStatus.SC_UNAUTHORIZED;
	}
}
