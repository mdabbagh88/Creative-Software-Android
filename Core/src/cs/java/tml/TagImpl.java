package cs.java.tml;

import static cs.java.lang.Lang.list;
import static cs.java.lang.Lang.map;

import java.util.Iterator;

import cs.java.collections.List;
import cs.java.collections.Map;
import cs.java.lang.Base;
import cs.java.xml.Tag;

public class TagImpl extends Base implements Tag {

	private final List<Tag> tags = list();
	private final Map<String, String> attributes = map();
	private String name = "";

	public TagImpl() {
	}

	public TagImpl(String tagText) {
		String[] items = tagText.trim().split("\\s+");
		name = items[0];
		for (String text : list(items).range(1)) {
			String[] attribute = text.split(":");
			attributes.put(attribute[0], attribute[1]);
		}
	}

	@Override
	public String get(String attribute) {
		return getAttributes().value(attribute);
	}

	@Override
	public Map<String, String> getAttributes() {
		return attributes;
	}

	@Override
	public List<Tag> getChilds() {
		return tags;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public boolean has(String attribute) {
		return getAttributes().hasKey(attribute);
	}

	@Override
	public Iterator<Tag> iterator() {
		return tags.iterator();
	}
}
