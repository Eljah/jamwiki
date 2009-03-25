package org.jamwiki.utils;

import java.util.HashMap;
import java.util.Map;
import info.bliki.html.wikipedia.*;

public class ToWikipediaEx extends AbstractHTMLToWiki implements IHTMLToWiki {
	static private final Map<String, HTMLTag> TAG_MAP = new HashMap<String, HTMLTag>();
	static {
		TAG_MAP.put("a", new ATag());
		TAG_MAP.put("b", new OpenCloseTag("'''", "'''"));
		TAG_MAP.put("strong", new OpenCloseTag("'''", "'''"));
		TAG_MAP.put("i", new OpenCloseTag("''", "''"));
		TAG_MAP.put("em", new OpenCloseTag("''", "''"));
		TAG_MAP.put("table", new TableTag());
		TAG_MAP.put("caption", new CaptionTag());
		TAG_MAP.put("tr", new TrTag());
		TAG_MAP.put("td", new TdTag());
		TAG_MAP.put("th", new ThTag());
		TAG_MAP.put("img", new ImgTag());
		TAG_MAP.put("p", new OpenCloseHTMLTag("\n<div", "\n</div>"));
		TAG_MAP.put("code", new OpenCloseTag("<code>", "</code>"));
		TAG_MAP.put("blockquote", new OpenCloseTag("<blockquote>", "</blockquote>"));
		TAG_MAP.put("u", new OpenCloseTag("<u>", "</u>"));
		TAG_MAP.put("del", new OpenCloseTag("<s>", "</s>"));
		TAG_MAP.put("s", new OpenCloseTag("<s>", "</s>"));
		TAG_MAP.put("sub", new OpenCloseTag("<sub>", "</sub>"));
		TAG_MAP.put("sup", new OpenCloseTag("<sup>", "</sup>"));
		TAG_MAP.put("big", new OpenCloseTag("<big>", "</big>"));
		TAG_MAP.put("small", new OpenCloseTag("<small>", "</small>"));
		TAG_MAP.put("tt", new OpenCloseTag("<tt>", "</tt>"));
		TAG_MAP.put("div", new OpenCloseHTMLTag("\n<div", "\n</div>"));
		TAG_MAP.put("span", new OpenCloseHTMLTag("<span", "</span>"));
		TAG_MAP.put("font", new OpenCloseHTMLTag("<font", "</font>"));
		TAG_MAP.put("pre", new OpenCloseTag("\n<pre>", "\n</pre>\n"));
		TAG_MAP.put("h1", new OpenCloseTag("\n= ", " =\n", true));
		TAG_MAP.put("h2", new OpenCloseTag("\n== ", " ==\n", true));
		TAG_MAP.put("h3", new OpenCloseTag("\n=== ", " ===\n", true));
		TAG_MAP.put("h4", new OpenCloseTag("\n==== ", " ====\n", true));
		TAG_MAP.put("h5", new OpenCloseTag("\n===== ", " =====\n", true));
		TAG_MAP.put("h6", new OpenCloseTag("\n====== ", " ======\n", true));
		TAG_MAP.put("ul", new ListTag("*"));
		TAG_MAP.put("ol", new ListTag("#"));
		TAG_MAP.put("script", new NoOutputTag());
	}

	public ToWikipediaEx(boolean noDiv, boolean noFont) {
		super(TAG_MAP, noDiv, noFont);
	}

	public ToWikipediaEx() {
		this(false, false);
	}

}
