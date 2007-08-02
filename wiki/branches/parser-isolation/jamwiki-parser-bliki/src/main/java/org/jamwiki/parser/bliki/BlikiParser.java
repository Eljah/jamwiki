package org.jamwiki.parser.bliki;

import org.jamwiki.parser.ParserDocument;
import org.jamwiki.parser.ParserInput;

public class BlikiParser { //FIXME extends JFlexParser {
	ParserInput parserInput;
	
	public BlikiParser(ParserInput parserInput) {
		//FIXME super(parserInput);
	}

	/**
	 * Parse text for online display.
	 */
	public String parseHTML(String raw) throws Exception {
		ParserDocument doc = new ParserDocument();
		
		String titlePrefix = "/jamwiki/" + parserInput.getVirtualWiki() + '/';
		JAMWikiModel wikiModel = new JAMWikiModel(parserInput, doc, titlePrefix + "${image}", titlePrefix + "${title}");
		String htmlStr = wikiModel.render(raw);
		htmlStr = htmlStr == null ? "" : htmlStr;
		return htmlStr;
	}
}
