/*
 * This class adds paragraph tags as appropriate.
 */
package org.jamwiki.parser;

import org.jamwiki.utils.WikiLogger;

%%

%public
%class JAMWikiPostProcessor
%extends AbstractLexer
%type String
%unicode
%ignorecase

/* code included in the constructor */
%init{
    yybegin(NORMAL);
    states.add(new Integer(yystate()));
%init}

/* code called after parsing is completed */
%eofval{
    StringBuffer output = new StringBuffer();
    if (yystate() == P) {
        endState();
        output.append("</p>");
    }
    return (output.length() == 0) ? null : output.toString();
%eofval}

/* code copied verbatim into the generated .java file */
%{
    protected static WikiLogger logger = WikiLogger.getLogger(JAMWikiPostProcessor.class.getName());
    
    /**
     *
     */
    public void setParserInput(ParserInput parserInput) throws Exception {
        this.parserInput = parserInput;
        // validate parser settings
        boolean validated = true;
        if (this.parserInput == null) validated = false;
        if (this.parserInput.getTableOfContents() == null) validated = false;
        if (!validated) {
            throw new Exception("Parser info not properly initialized");
        }
    }
%}

/* character expressions */
newline            = ((\r\n) | (\n))
whitespace         = {newline} | [ \t\f]
inputcharacter     = ([^ \n\r\t])

/* nowiki */
nowikistart        = (<[ ]*nowiki[ ]*>)
nowikiend          = (<[ ]*\/[ ]*nowiki[ ]*>)

/* pre */
htmlprestart       = (<[ ]*pre[ ]*>)
htmlpreend         = (<[ ]*\/[ ]*pre[ ]*>)

/* javascript */
javascript         = (<[ ]*script[^>]*>) ~(<[ ]*\/[ ]*script[ ]*>)

/* processing commands */
toc                = "__TOC__"


/* paragraph */
noparagraph        = (((<[ ]*) td ([^/>]*>)) ([^\n])+ ([\n])? ((<[ ]*\/[ ]*) td ([ ]*>))) | "<" [ ]* "hr" ~">"
emptyline          = ({newline} {newline} {newline})
nonparagraphtag    = table|div|h1|h2|h3|h4|h5|ul|dl|ol|span|p
nonparagraphstart  = ((<[ ]*) {nonparagraphtag} ([^/>]*>)) | ((<[ ]*\/[ ]*) td ([ ]*>))
nonparagraphend    = ((<[ ]*\/[ ]*) {nonparagraphtag} ([ ]*>)) | ((<[ ]*) td ([^/>]*>))
anchorname         = (<[ ]*a[ ]*name[ ]*=[^/]+\/[ ]*[a]?[ ]*>)
break              = (<[ ]*) br ([ ]*[\/]?[ ]*>)
paragraphend       = ({newline} {newline})
paragraphstart     = ({inputcharacter})

%state NOWIKI, PRE, NORMAL, P, NONPARAGRAPH

%%

/* ----- nowiki ----- */

<PRE, NORMAL, P, NONPARAGRAPH>{nowikistart} {
    logger.finer("nowikistart: " + yytext() + " (" + yystate() + ")");
    beginState(NOWIKI);
    return "";
}

<NOWIKI>{nowikiend} {
    logger.finer("nowikiend: " + yytext() + " (" + yystate() + ")");
    endState();
    return "";
}

/* ----- pre ----- */

<NORMAL, P, NONPARAGRAPH>{htmlprestart} {
    logger.finer("htmlprestart: " + yytext() + " (" + yystate() + ")");
    beginState(PRE);
    return "<pre>";
}

<PRE>{htmlpreend} {
    logger.finer("htmlpreend: " + yytext() + " (" + yystate() + ")");
    endState();
    return "</pre>";
}

/* ----- processing commands ----- */

<NORMAL, P, NONPARAGRAPH>{toc} {
    logger.finer("toc: " + yytext() + " (" + yystate() + ")");
    return this.parserInput.getTableOfContents().attemptTOCInsertion();
}

/* ----- javascript ----- */

<NORMAL, P, NONPARAGRAPH>{javascript} {
    logger.finer("javascript: " + yytext() + " (" + yystate() + ")");
    return yytext();
}

/* ----- layout ----- */

<NORMAL, NONPARAGRAPH>{noparagraph} {
    // <hr> and <td> tags _with no newlines_ should be ignored for the sake of paragraph parsing
    logger.finer("noparagraph: " + yytext() + " (" + yystate() + ")");
    return yytext();
}

<NORMAL, P>{emptyline} {
    logger.finer("emptyline: " + yytext() + " (" + yystate() + ")");
    StringBuffer output = new StringBuffer();
    if (yystate() == P) {
        output.append("</p>");
        endState();
    }
    return output.toString() + "\n<p><br /></p>";
}

<NORMAL, P, NONPARAGRAPH>{anchorname} {
    // for layout purposes and <a name="foo"></a> link should be returned without
    // changes, but should not affect paragraph layout in any way.
    logger.finer("anchorname: " + yytext() + " (" + yystate() + ")");
    return yytext();
}

<NORMAL, P, NONPARAGRAPH>{break} {
    // for layout purposes <br> tags should not affect paragraph layout in any way.
    logger.finer("break: " + yytext() + " (" + yystate() + ")");
    return yytext();
}

<NORMAL, P, NONPARAGRAPH>{nonparagraphstart} {
    logger.finer("nonparagraphstart: " + yytext() + " (" + yystate() + ")");
    StringBuffer output = new StringBuffer();
    if (yystate() == P) {
        output.append("</p>");
        endState();
    }
    beginState(NONPARAGRAPH);
    return output.toString() + yytext();
}

<NORMAL, P, NONPARAGRAPH>{nonparagraphend} {
    logger.finer("nonparagraphend: " + yytext() + " (" + yystate() + ")");
    if (yystate() == NONPARAGRAPH) {
        endState();
    } else {
        logger.warning("Attempt to end nonparagraph state while state is not nonparagraph for text: " + yytext());
    }
    return yytext();
}

<NORMAL>{paragraphstart} {
    logger.finer("paragraphstart: " + yytext() + " (" + yystate() + ")");
    beginState(P);
    return "<p>" + yytext();
}

<P>{paragraphend} {
    logger.finer("end of paragraph: " + yytext() + " (" + yystate() + ")");
    endState();
    return "</p>" + yytext();
}

/* ----- other ----- */

<PRE, NOWIKI, NORMAL, NONPARAGRAPH, P>{whitespace} {
    // no need to log this
    return yytext();
}

<PRE, NOWIKI, NORMAL, NONPARAGRAPH, P>. {
    // no need to log this
    return yytext();
}
