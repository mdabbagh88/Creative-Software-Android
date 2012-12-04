package cs.java.tml;

import static cs.java.lang.Lang.equalOne;
import static cs.java.lang.Lang.iterate;
import static cs.java.lang.Lang.text;
import cs.java.collections.List;
import cs.java.lang.Base;
import cs.java.lang.Text;
import cs.java.xml.Tag;

public class TMLParser extends Base {

	private final int levelSpaceSize;
	private final List<Text> rows;
	private final Tag top = new TagImpl();

	public TMLParser(String string) {
		rows = text(string).split("\n");
		levelSpaceSize = findFirstLevelSize();
	}

	protected int countPrefixSpace(Text string) {
		int count = 0;
		for (char character : string)
			if (equalOne(character, ' ', '\t'))
				count++;
			else break;
		return count;
	}

	private int findFirstLevelSize() {
		for (Text text : rows) {
			int prefixSpaceCount = countPrefixSpace(text);
			if (prefixSpaceCount > 0) return prefixSpaceCount;
		}
		return 0;
	}

	private Tag getLastParent(int level) {
		Tag tag = top;
		for (@SuppressWarnings("unused")
		int num : iterate(level))
			tag = top.getChilds().last();
		return tag;
	}

	public Tag parse() {
		for (Text text : rows) {
			int level = countPrefixSpace(text) / levelSpaceSize;
			getLastParent(level).getChilds().add(new TagImpl(text.toString()));
		}
		return top;
	}
}
