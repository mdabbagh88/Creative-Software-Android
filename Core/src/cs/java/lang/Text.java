package cs.java.lang;

import cs.java.collections.List;

public interface Text extends CharSequence, Appendable, Iterable<Character> {

	Text add(CharSequence... string);

	Text add(CharSequence string);

	Object add(Object... msg);

	Text addLine();

	Text addSpace();

	Text caseDown();

	Text caseUp(int index);

	Text cut(int start, int end);

	Text cutEnd(int length);

	Text cutStart(int length);

	boolean isEmpty();

	Text remove(String... strings);

	Text replace(String regex, String replace);

	Text replaceEnd(String string);

	List<Text> split(String string);

	Text trim();

}
