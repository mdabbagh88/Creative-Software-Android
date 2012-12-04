package cs.java.tml;

import cs.java.xml.Tag;

public class TML {
	public static Tag load(String string) {
		TMLParser parser = new TMLParser(string);
		return parser.parse();
	}
}
