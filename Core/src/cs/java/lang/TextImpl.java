package cs.java.lang;

import cs.java.collections.GenericIterator;
import cs.java.collections.List;

import java.io.IOException;
import java.util.Iterator;

import static cs.java.lang.Lang.list;

public class TextImpl implements Text {

    private final StringBuilder value = new StringBuilder();

    public TextImpl(CharSequence... strings) {
        add(strings);
    }

    @Override
		public Text add(CharSequence... strings) {
        for (CharSequence string : strings)
            value.append(string);
        return this;
    }

    @Override
		public Text add(CharSequence string) {
        value.append(string);
        return this;
    }

    @Override
		public Object add(Object... objects) {
        for (Object object : objects)
            value.append(String.valueOf(object));
        return this;
    }

    @Override
		public Text addLine() {
        return add("\n");
    }

    @Override
		public Text addSpace() {
        return add(" ");
    }

    @Override
		public Appendable append(char c) throws IOException {
        return value.append(c);
    }

    @Override
		public Appendable append(CharSequence csq) throws IOException {
        return value.append(csq);
    }

    @Override
		public Appendable append(CharSequence csq, int start, int end) throws IOException {
        return value.append(csq, start, end);
    }

    @Override
		public Text caseDown() {
        set(toString().toLowerCase());
        return this;
    }

    @Override
		public Text caseUp(int index) {
        set(value.substring(0, index) + value.substring(index, index + 1).toUpperCase()
                + value.substring(index + 1, length()));
        return this;
    }

    @Override
		public char charAt(int index) {
        return value.charAt(index);
    }

    private void clear() {
        value.delete(0, length());
    }

    @Override
		public Text cut(int start, int end) {
        value.delete(start, end);
        return this;
    }

    @Override
		public Text cutEnd(int length) {
        return cut(length() - length, length());
    }

    @Override
		public Text cutStart(int length) {
        return cut(0, length);
    }

    @Override
		public boolean isEmpty() {
        return value.length() == 0;
    }

    @Override
		public Iterator<Character> iterator() {
        return new GenericIterator<Character>(length()) {
            @Override
            protected Character getValue() {
                return charAt(index());
            }
        };
    }

    @Override
		public int length() {
        return value.length();
    }

    @Override
		public Text remove(String... strings) {
        String text = toString();
        for (String string : strings)
            text = text.replaceAll(string, "");
        set(text);
        return this;
    }

    @Override
		public Text replace(String regex, String replace) {
        set(toString().replaceAll(regex, replace));
        return this;
    }

    @Override
		public Text replaceEnd(String string) {
        cutEnd(string.length());
        add(string);
        return this;
    }

    public void set(CharSequence text) {
        clear();
        value.append(text);
    }

    @Override
		public List<Text> split(String regex) {
        List<Text> split = list();
        for (String string : toString().split(regex))
            split.add(new TextImpl(string));
        return split;
    }

    @Override
		public CharSequence subSequence(int start, int end) {
        return value.subSequence(start, end);
    }

    @Override
		public String toString() {
        return value.toString();
    }

    @Override
		public Text trim() {
        set(toString().trim());
        return this;
    }
}
