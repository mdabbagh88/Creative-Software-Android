package cs.android.lang;

import cs.android.http.CSHttpClientImpl;
import cs.android.rpc.ServerRequestBase;

public class GetRequest extends ServerRequestBase {

	private String content;
	private final CSHttpClientImpl http = new CSHttpClientImpl();

	public GetRequest(String serverUrl) {
		http.setUrl(serverUrl);
	}

	public void add(String key, String value) {
		http.add(key, value);
	}

	public String getResponse() {
		return content;
	}

	// @Override protected void process() {
	// http.executeGet();
	// content = http.getResponseString();
	// if (set(content))
	// success();
	// else failed("Request failed");
	// }
}
