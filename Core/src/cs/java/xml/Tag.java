package cs.java.xml;

import cs.java.collections.List;
import cs.java.collections.Map;

public interface Tag extends Iterable<Tag> {
	String get(String attribute);

	Map<String, String> getAttributes();

	List<Tag> getChilds();

	String getName();

	boolean has(String attribute);
}
