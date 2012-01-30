/*
 * This class implements the MediaWiki syntax (http://meta.wikimedia.org/wiki/Help:Editing).
 * It will also escape any HTML tags that have not been specifically allowed to be
 * present.
 */
package org.jamwiki.parser.jflex;

import org.apache.commons.lang.StringEscapeUtils;
import org.jamwiki.parser.TableOfContents;
import org.jamwiki.utils.Utilities;

%%

%public
%class JAMWikiLexer
%extends AbstractJAMWikiLexer
%type String
%unicode
%ignorecase

/* character expressions */
newline            = "\n"
whitespace         = [ \n\t\f]
entity             = (&#[Xx]([0-9a-fA-F]{2,4});) | (&#([0-9]{2,4});) | (&[A-Za-z]{2,6};)
emptyline          = ([ \t])* ({newline})

/* non-container expressions */
hr                 = ({newline})? "----" ({newline})
wikiheading6       = "======" (.+) "======"
wikiheading5       = "=====" (.+) "====="
wikiheading4       = "====" (.+) "===="
wikiheading3       = "===" (.+) "==="
wikiheading2       = "==" (.+) "=="
wikiheading1       = "=" (.+) "="
wikiheading        = ({wikiheading6})|({wikiheading5})|({wikiheading4})|({wikiheading3})|({wikiheading2})|({wikiheading1})
bold               = "'''"
bolditalic         = "'''''"
italic             = "''"

/* lists */
listitem           = [\*#\:;]+ [^\*#\:;]
listend            = [^\*#\:;\n]+ (.)+
listdt             = ":"

/* nowiki */
nowiki             = (<[ \t]*nowiki[ \t]*>) ~(<[ \t]*\/[ \t]*nowiki[ \t]*>)

/* pre */
attributeValueInQuotes = "\"" ~"\""
attributeValueInSingleQuotes = "'" ~"'"
attributeValueNoQuotes = [^>\n]+
htmlattribute      = ([ \t]+) [a-zA-Z:]+ ([ \t]*=[ \t]*({attributeValueInQuotes}|{attributeValueInSingleQuotes}|{attributeValueNoQuotes}))*
htmlprestart       = (<[ \t]*pre ({htmlattribute})* [ \t]* (\/)? [ \t]*>)
htmlpreend         = (<[ \t]*\/[ \t]*pre[ \t]*>)
wikipre            = (" ") ([^\n])+ ~({newline})
wikipreend         = [^ ] | {newline}

/* allowed html */
heading            = h1|h2|h3|h4|h5|h6
inlinetag          = abbr|br|b|big|cite|code|del|em|font|i|ins|s|small|span|strike|strong|sub|sup|tt|u|var
blockleveltag      = blockquote|caption|center|col|colgroup|dd|div|dl|dt|{heading}|hr|li|ol|p|table|tbody|td|tfoot|th|thead|tr|ul
htmlkeyword        = {inlinetag}|{blockleveltag}
htmlbr             = <[ \t]* (\/)? [ \t]* br ({htmlattribute})* [ \t]* (\/)? [ \t]*>
htmlparagraphopen  = <[ \t]* p ({htmlattribute})* [ \t]* (\/)? [ \t]*>
htmlparagraphclose = (<[ \t]*\/[ \t]*) p ([ \t]*>)
inlinetagopen      = <[ \t]* ({inlinetag}) ({htmlattribute})* [ \t]* (\/)? [ \t]*>
blockleveltagopen  = <[ \t]* ({blockleveltag}) ({htmlattribute})* [ \t]* (\/)? [ \t]*>
blockleveltagclose = (<[ \t]*\/[ \t]*) {blockleveltag} ([ \t]*>)
htmltagopen        = <[ \t]* ({htmlkeyword}) ({htmlattribute})* [ \t]* (\/)? [ \t]*>
htmltagclose       = (<[ \t]*\/[ \t]*) {htmlkeyword} ([ \t]*>)
htmltagnocontent   = (<[ \t]*) {htmlkeyword} ({htmlattribute})* ([ \t]*\/[ \t]*>)
htmlheading        = (<[ \t]*h[1-6][^>]*>) ~(<[ \t]*\/[ \t]*h[1-6][ \t]*>)

/* javascript */
javascript         = (<[ \t]*script[^>]*>) ~(<[ \t]*\/[ \t]*script[ \t]*>)

/* processing commands */
notoc              = "__NOTOC__"
toc                = "__TOC__"
forcetoc           = "__FORCETOC__"

/* tables */
tableattribute     = ([ \t]*) [a-zA-Z:]+ ([ \t]*=[^>\n\|]+[ \t]*)*
tablestart         = "{|" (.)* {newline}
tableend           = "|}" ({newline})?
tablecell          = "|" [^\+\-\}] | "|" ({tableattribute})+ "|" [^\|]
tablecells         = "||" | "!!"
tablecellsstyle    = "||" ({tableattribute})+ "|" ([^|])
tableheading       = "!" | "!" ({tableattribute})+ "|" [^\|]
tablerow           = "|-" [ \t]* ({tableattribute})* {newline}
tablecaption       = "|+" | "|+" ({tableattribute})+ "|" [^\|]

/* wiki links */
protocol           = "http://" | "https://" | "mailto:" | "mailto://" | "ftp://" | "file://"
htmllinkwiki       = "[" ({protocol}) ([^\]\n]+) "]"
htmllinkraw        = ({protocol}) ([^ <'\"\n\t]+)
htmllink           = ({htmllinkwiki}) | ({htmllinkraw})
wikilinkcontent    = [^\n\]] | "]" [^\n\]] | {htmllink}
wikilink           = "[[" ({wikilinkcontent})+ "]]" [a-z]*
nestedwikilink     = "[[" ({wikilinkcontent})+ "|" ({wikilinkcontent} | {wikilink})+ "]]"

/* references */
reference          = (<[ \t]*) "ref" ([ \t]+name[ \t]*=[^>\/\n]+[ \t]*)? ([ \t]*>) ~(<[ \t]*\/[ \t]*ref[ \t]*>)
referencenocontent = (<[ \t]*) "ref" ([ \t]+name[ \t]*=[^>\/\n]+[ \t]*) ([ \t]*\/[ \t]*>)
references         = (<[ \t]*) "references" ([ \t]*[\/]?[ \t]*>)

/* paragraphs */
/* TODO: this pattern does not match text such as "< is a less than sign" */
startparagraph     = ({emptyline})? ({emptyline})? ([^< \n])|{inlinetagopen}|{wikilink}|{nestedwikilink}|{htmllink}|{bold}|{bolditalic}|{italic}|{entity}|{nowiki}
paragraphempty     = ({emptyline}) ({emptyline})+
endparagraph1      = ({newline}){1,2} ({hr}|{wikiheading}|{listitem}|{wikipre}|{tablestart})
endparagraph2      = (({newline})([ \t]*)){2}
endparagraph3      = {blockleveltagopen}|{htmlprestart}|{blockleveltagclose}
endparagraph       = {endparagraph1}|{endparagraph2}|{endparagraph3}

%state TABLE, LIST, PRE, WIKIPRE, PARAGRAPH

%%

/* ----- paragraphs ----- */

<YYINITIAL> {
    ^{startparagraph} {
        if (logger.isTraceEnabled()) logger.trace("startparagraph: " + yytext() + " (" + yystate() + ")");
        this.parseParagraphStart(yytext());
        beginState(PARAGRAPH);
        return "";
    }
    ^{paragraphempty} {
        if (logger.isTraceEnabled()) logger.trace("paragraphempty: " + yytext() + " (" + yystate() + ")");
        this.parseParagraphEmpty(yytext());
        return "";
    }
}
<PARAGRAPH> {
    {endparagraph} {
        if (logger.isTraceEnabled()) logger.trace("endparagraph: " + yytext() + " (" + yystate() + ")");
        this.parseParagraphEnd(yytext());
        endState();
        return "";
    }
}

/* ----- nowiki ----- */

<YYINITIAL, WIKIPRE, PRE, LIST, TABLE, PARAGRAPH> {
    {nowiki} {
        if (logger.isTraceEnabled()) logger.trace("nowiki: " + yytext() + " (" + yystate() + ")");
        String content = JFlexParserUtil.tagContent(yytext());
        return "<nowiki>" + StringEscapeUtils.escapeHtml(content) + "</nowiki>";
    }
}

/* ----- pre ----- */

<YYINITIAL, LIST, TABLE> {
    {htmlprestart} {
        if (logger.isTraceEnabled()) logger.trace("htmlprestart: " + yytext() + " (" + yystate() + ")");
        if (!allowHTML()) {
            return StringEscapeUtils.escapeHtml(yytext());
        }
        beginState(PRE);
        this.pushTag("pre", yytext());
        return "";
    }
}
<PRE> {
    {htmlpreend} {
        if (logger.isTraceEnabled()) logger.trace("htmlpreend: " + yytext() + " (" + yystate() + ")");
        // state only changes to pre if allowHTML() is true, so no need to check here
        endState();
        this.popTag("pre");
        return "";
    }
}
<YYINITIAL, WIKIPRE, LIST, TABLE> {
    ^{wikipre} {
        if (logger.isTraceEnabled()) logger.trace("wikipre: " + yytext() + " (" + yystate() + ")");
        // rollback all but the first (space) character for further processing
        yypushback(yytext().length() - 1);
        if (yystate() != WIKIPRE) {
            beginState(WIKIPRE);
            this.pushTag("pre", null);
        }
        return "";
    }
}
<WIKIPRE> {
    ^{wikipreend} {
        if (logger.isTraceEnabled()) logger.trace("wikipreend: " + yytext() + " (" + yystate() + ")");
        endState();
        // rollback everything to allow processing as non-pre text
        yypushback(yytext().length());
        this.popTag("pre");
        return  "\n";
    }
}

/* ----- table of contents ----- */

<YYINITIAL, LIST, TABLE, PARAGRAPH> {
    {notoc} {
        if (logger.isTraceEnabled()) logger.trace("notoc: " + yytext() + " (" + yystate() + ")");
        this.parserInput.getTableOfContents().setStatus(TableOfContents.STATUS_NO_TOC);
        return "";
    }
    {toc} {
        if (logger.isTraceEnabled()) logger.trace("toc: " + yytext() + " (" + yystate() + ")");
        this.parserInput.getTableOfContents().setStatus(TableOfContents.STATUS_TOC_INITIALIZED);
        this.parserInput.getTableOfContents().setForceTOC(true);
        return yytext();
    }
    {forcetoc} {
        if (logger.isTraceEnabled()) logger.trace("forcetoc: " + yytext() + " (" + yystate() + ")");
        this.parserInput.getTableOfContents().setForceTOC(true);
        return "";
    }
}

/* ----- tables ----- */

<YYINITIAL, TABLE, PARAGRAPH> {
    ^{tablestart} {
        if (logger.isTraceEnabled()) logger.trace("tablestart: " + yytext() + " (" + yystate() + ")");
        if (this.peekTag().getTagType().equals("p")) {
            popTag("p");
        }
        if (yystate() == PARAGRAPH) {
            endState();
        }
        beginState(TABLE);
        String tagAttributes = yytext().trim().substring(2).trim();
        this.pushTag("table", "<table " + tagAttributes + ">");
        return "";
    }
}
<TABLE> {
    ^{tablecaption} {
        if (logger.isTraceEnabled()) logger.trace("tablecaption: " + yytext() + " (" + yystate() + ")");
        processTableStack();
        if (yytext().length() > 2) {
            // for captions with CSS specified an extra character is matched
            yypushback(1);
        }
        parseTableCell(yytext(), "caption", "|+");
        return "";
    }
    ^{tableheading} {
        if (logger.isTraceEnabled()) logger.trace("tableheading: " + yytext() + " (" + yystate() + ")");
        // if a column was already open, close it
        processTableStack();
        // FIXME - hack!  make sure that a table row is open
        if (!this.peekTag().getTagType().equals("tr")) {
            this.pushTag("tr", null);
        }
        if (yytext().length() > 2) {
            // for headings with CSS specified an extra character is matched
            yypushback(1);
        }
        parseTableCell(yytext(), "th", "!");
        return "";
    }
    ^{tablecell} {
        if (logger.isTraceEnabled()) logger.trace("tablecell: " + yytext() + " (" + yystate() + ")");
        // if a column was already open, close it
        processTableStack();
        // FIXME - hack!  make sure that a table row is open
        if (!this.peekTag().getTagType().equals("tr")) {
            this.pushTag("tr", null);
        }
        // extra character matched by both regular expressions so push it back
        yypushback(1);
        parseTableCell(yytext(), "td", "|");
        return "";
    }
    {tablecells} {
        if (logger.isTraceEnabled()) logger.trace("tablecells: " + yytext() + " (" + yystate() + ")");
        if (this.peekTag().getTagType().equals("td") && yytext().equals("||")) {
            this.popTag("td");
            this.pushTag("td", null);
            return "";
        }
        if (this.peekTag().getTagType().equals("th")) {
            this.popTag("th");
            this.pushTag("th", null);
            return "";
        }
        return yytext();
    }
    {tablecellsstyle} {
        if (logger.isTraceEnabled()) logger.trace("tablecellsstyle: " + yytext() + " (" + yystate() + ")");
        if (!this.peekTag().getTagType().equals("td")) {
            return yytext();
        }
        // one extra character matched by the pattern, so roll it back
        yypushback(1);
        this.popTag("td");
        parseTableCell(yytext(), "td", "|");
        return "";
    }
    ^{tablerow} {
        if (logger.isTraceEnabled()) logger.trace("tablerow: " + yytext() + " (" + yystate() + ")");
        // if a column was already open, close it
        processTableStack();
        if (!this.peekTag().getTagType().equals("table") && !this.peekTag().getTagType().equals("caption")) {
            this.popTag("tr");
        }
        String openTagRaw = null;
        if (yytext().trim().length() > 2) {
            openTagRaw = "<tr " + yytext().substring(2).trim() + ">";
        }
        this.pushTag("tr", openTagRaw);
        return "";
    }
    ^{tableend} {
        if (logger.isTraceEnabled()) logger.trace("tableend: " + yytext() + " (" + yystate() + ")");
        // if a column was already open, close it
        processTableStack();
        // end TABLE state
        endState();
        this.popTag("tr");
        this.popTag("table");
        return "";
    }
}

/* ----- headings ----- */

<YYINITIAL> {
    ^{hr} {
        if (logger.isTraceEnabled()) logger.trace("hr: " + yytext() + " (" + yystate() + ")");
        // pushback the closing newline
        yypushback(1);
        return "<hr />";
    }
}
<YYINITIAL, PARAGRAPH, TABLE> {
    ^{newline}? {wikiheading6} {
        return this.parse(TAG_TYPE_WIKI_HEADING, yytext(), 6);
    }
    ^{newline}? {wikiheading5} {
        return this.parse(TAG_TYPE_WIKI_HEADING, yytext(), 5);
    }
    ^{newline}? {wikiheading4} {
        return this.parse(TAG_TYPE_WIKI_HEADING, yytext(), 4);
    }
    ^{newline}? {wikiheading3} {
        return this.parse(TAG_TYPE_WIKI_HEADING, yytext(), 3);
    }
    ^{newline}? {wikiheading2} {
        return this.parse(TAG_TYPE_WIKI_HEADING, yytext(), 2);
    }
    ^{newline}? {wikiheading1} {
        return this.parse(TAG_TYPE_WIKI_HEADING, yytext(), 1);
    }
    {htmlheading} {
        return this.parse(TAG_TYPE_HTML_HEADING, yytext());
    }
}

/* ----- lists ----- */

<YYINITIAL, LIST, TABLE, PARAGRAPH> {
    ^{listitem} {
        if (logger.isTraceEnabled()) logger.trace("listitem: " + yytext() + " (" + yystate() + ")");
        if (this.peekTag().getTagType().equals("p")) {
            popTag("p");
        }
        if (yystate() == PARAGRAPH) {
            endState();
        }
        if (yystate() != LIST) beginState(LIST);
        // one non-list character matched, roll it back
        yypushback(1);
        this.processListStack(yytext());
        return "";
    }
}
<LIST> {
    ^{listend} {
        if (logger.isTraceEnabled()) logger.trace("listend: " + yytext() + " (" + yystate() + ")");
        String raw = yytext();
        // roll back any matches to allow re-parsing
        yypushback(raw.length());
        endState();
        this.popAllListTags();
        return "";
    }
    {listdt} {
        if (logger.isTraceEnabled()) logger.trace("listdt: " + yytext() + " (" + yystate() + ")");
        if (this.peekTag().getTagType().equals("dt")) {
            // special case list of the form "; term : definition"
            this.popTag("dt");
            this.pushTag("dd", null);
            return "";
        }
        return yytext();
    }
}

/* ----- wiki links ----- */

<YYINITIAL, LIST, TABLE, PARAGRAPH> {
    {wikilink} {
        if (logger.isTraceEnabled()) logger.trace("wikilink: " + yytext() + " (" + yystate() + ")");
        return this.parse(TAG_TYPE_WIKI_LINK, yytext());
    }
    {nestedwikilink} {
        if (logger.isTraceEnabled()) logger.trace("nestedwikilink: " + yytext() + " (" + yystate() + ")");
        return this.parse(TAG_TYPE_WIKI_LINK, yytext(), "nested");
    }
    {htmllinkraw} {
        return this.parse(TAG_TYPE_HTML_LINK, yytext(), false);
    }
    {htmllinkwiki} {
        String raw = yytext();
        // strip the opening and closing brackets
        return this.parse(TAG_TYPE_HTML_LINK, raw.substring(1, raw.length() - 1), true);
    }
}

/* ----- bold / italic ----- */

<YYINITIAL, WIKIPRE, LIST, TABLE, PARAGRAPH> {
    {bold} {
        return this.parse(TAG_TYPE_WIKI_BOLD_ITALIC, yytext(), "b");
    }
    {bolditalic} {
        return this.parse(TAG_TYPE_WIKI_BOLD_ITALIC, yytext(), (String)null);
    }
    {italic} {
        return this.parse(TAG_TYPE_WIKI_BOLD_ITALIC, yytext(), "i");
    }
}

/* ----- references ----- */

<YYINITIAL, LIST, TABLE, PARAGRAPH> {
    {reference} {
        return this.parse(TAG_TYPE_WIKI_REFERENCE, yytext());
    }
    {referencenocontent} {
        return this.parse(TAG_TYPE_WIKI_REFERENCE, yytext());
    }
    {references} {
        logger.trace("references: " + yytext() + " (" + yystate() + ")");
        if (this.peekTag().getTagType().equals("p")) {
            // if a paragraph is already opened, close it
            this.popTag("p");
        }
        return this.parse(TAG_TYPE_WIKI_REFERENCES, yytext());
    }
}

/* ----- html ----- */

<YYINITIAL, LIST, TABLE, PARAGRAPH> {
    {htmlbr} {
        if (logger.isTraceEnabled()) logger.trace("htmlbr: " + yytext() + " (" + yystate() + ")");
        if (!allowHTML()) {
            return StringEscapeUtils.escapeHtml(yytext());
        }
        // <br> may have attributes, so check for them
        HtmlTagItem htmlTagItem = JFlexParserUtil.sanitizeHtmlTag(yytext());
        // Mediawiki standard is to include a newline after br tags
        return (htmlTagItem == null) ? "" : htmlTagItem.toHtml() + '\n';
    }
    {htmlparagraphopen} {
        if (logger.isTraceEnabled()) logger.trace("htmlparagraphopen: " + yytext() + " (" + yystate() + ")");
        if (!allowHTML()) {
            return StringEscapeUtils.escapeHtml(yytext());
        }
        if (this.peekTag().getTagType().equals("p")) {
            // if a paragraph is already opened, close it before opening a new paragraph
            this.popTag("p");
        }
        this.pushTag("p", yytext());
        if (yystate() != PARAGRAPH) {
            beginState(PARAGRAPH);
        }
        return "";
    }
    {htmlparagraphclose} {
        if (logger.isTraceEnabled()) logger.trace("htmlparagraphclose: " + yytext() + " (" + yystate() + ")");
        if (!allowHTML()) {
            return StringEscapeUtils.escapeHtml(yytext());
        }
        if (this.peekTag().getTagType().equals("p")) {
            // only perform processing if a paragraph is open.  otherwise just suppress this tag.
            this.popTag("p");
        }
        if (yystate() == PARAGRAPH) {
            endState();
        }
        return "";
    }
}
<YYINITIAL, WIKIPRE, LIST, TABLE, PARAGRAPH> {
    {htmltagnocontent} {
        if (logger.isTraceEnabled()) logger.trace("htmltagnocontent: " + yytext() + " (" + yystate() + ")");
        if (!allowHTML()) {
            return StringEscapeUtils.escapeHtml(yytext());
        }
        HtmlTagItem tagItem = JFlexParserUtil.sanitizeHtmlTag(yytext());
        return (tagItem == null) ? "" : tagItem.toHtml();
    }
    {htmltagopen} {
        if (logger.isTraceEnabled()) logger.trace("htmltagopen: " + yytext() + " (" + yystate() + ")");
        if (!allowHTML()) {
            return StringEscapeUtils.escapeHtml(yytext());
        }
        this.pushTag(null, yytext());
        return "";
    }
    {htmltagclose} {
        if (logger.isTraceEnabled()) logger.trace("htmltagclose: " + yytext() + " (" + yystate() + ")");
        if (!allowHTML()) {
            return StringEscapeUtils.escapeHtml(yytext());
        }
        this.popTag(null, yytext());
        return "";
    }
}

/* ----- javascript ----- */

<YYINITIAL, LIST, TABLE, PARAGRAPH> {
    {javascript} {
        return this.parse(TAG_TYPE_JAVASCRIPT, yytext());
    }
}

/* ----- other ----- */

<YYINITIAL> {
    ^{emptyline} {
        // suppress superfluous empty lines
        return "";
    }
}
<PARAGRAPH> {
    {newline} {
        // convert newlines within paragraphs to spaces
        return " ";
    }
}
<YYINITIAL, WIKIPRE, PRE, LIST, TABLE, PARAGRAPH> {
    {entity} {
        if (logger.isTraceEnabled()) logger.trace("entity: " + yytext() + " (" + yystate() + ")");
        String raw = yytext().toLowerCase();
        return (Utilities.isHtmlEntity(raw)) ? raw : StringEscapeUtils.escapeHtml(raw);
    }
    {whitespace} | . {
        // no need to log this
        return StringEscapeUtils.escapeHtml(yytext());
    }
}
