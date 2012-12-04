package cs.android.xml;

import static cs.java.lang.Lang.exception;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.InputSource;

import cs.android.xml.w3c.impl.W3CDocument;
import cs.java.xml.XML;
import cs.java.xml.impl.DocumentImpl;

public class XmlImpl implements XML {

	private DocumentBuilder builder;

	@Override
	public cs.java.xml.Document load(String text) {
		try {
			DocumentBuilder builder = getBuilder();
			W3CDocument doc = new W3CDocument(builder.parse(new InputSource(new StringReader(text))));
			return new DocumentImpl(doc);
		} catch (java.lang.Exception ex) {
			throw exception(ex);
		}
	}

	private DocumentBuilder getBuilder() throws ParserConfigurationException {
		if (builder == null) builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		return builder;
	}
}
