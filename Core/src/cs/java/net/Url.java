package cs.java.net;

import static cs.java.lang.Lang.asString;
import static cs.java.lang.Lang.iterate;
import static cs.java.lang.Lang.list;
import static cs.java.lang.Lang.text;
import static cs.java.lang.Lang.urlEncode;
import cs.java.collections.Iteration;
import cs.java.collections.List;
import cs.java.collections.Map;
import cs.java.collections.Mapped;
import cs.java.lang.KeyValue;
import cs.java.lang.Text;

public class Url {

	private String baseUrl;
	private final List<KeyValue> arguments = list();

	public Url() {
	}

	public Url(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	public Url add(String argument, Object value) {
		arguments.add(new KeyValue(argument.toString(), urlEncode(asString(value))));
		return this;
	}

	public Url addArguments(Map<String, String> arguments) {
		for (Mapped<String, String> mapped : iterate(arguments))
			add(mapped.key(), mapped.value());
		return this;
	}

	public Url remove(String key) {
		Iteration<KeyValue> iteration = iterate(arguments);
		for (KeyValue argument : iteration)
			if (argument.key.equals(key)) iteration.remove();
		return this;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	@Override public String toString() {
		Text url = text(baseUrl);
		url.add("?");
		for (KeyValue argument : arguments)
			url.add(argument.key, "=", argument.value, "&");
		url.cutEnd(1);
		return url.toString();
	}
}
