package cs.android.http;

import java.io.File;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.cookie.Cookie;

import android.graphics.Bitmap;
import cs.java.collections.List;
import cs.java.collections.Map;
import cs.java.json.JSONArray;
import cs.java.json.JSONObject;
import cs.java.json.JSONType;
import cs.java.model.Credentials;

public interface CSHttpClient extends HttpStatus {

	CSHttpClient add(String key, Object argument);

	CSHttpClient addArguments(Map<String, String> arguments);

	CSHttpClient addCookie(Cookie cookie);

	CSHttpClient debug(boolean show);

	CSHttpClient executeDelete();

	CSHttpClient executeGet();

	CSHttpClient executePatch(JSONObject values);

	CSHttpClient executePost(File file, String filePartName, String mime);

	CSHttpClient executePost(JSONType values);

	CSHttpClient executePost(Map<String, String> values);

	Cookie getCookie(int index);

	Cookie getCookie(String name);

	List<Cookie> getCookies();

	HttpResponse getResponse();

	JSONArray getResponseArray();

	Bitmap getResponseBitmap(int height);

	JSONObject getResponseObject();

	int getResponseStatus();

	String getResponseString();

	String getUrl();

	boolean isResponseOk();

	Bitmap responseBitmap();

	CSHttpClient setCredentials(Credentials credentials);

	CSHttpClient setCredentials(CredentialsProvider provider);

	CSHttpClient setCredentials(String username, String password);

	CSHttpClient setUrl(String url);

	void throwExceptionOn(int... statusCodes);

}
