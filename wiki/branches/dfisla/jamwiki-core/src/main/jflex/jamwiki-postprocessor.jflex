/*
 * This class adds paragraph tags as appropriate.
 */
package org.jamwiki.parser.jflex;

import org.apache.log4j.Logger;

%%

%public
%class JAMWikiPostProcessor
%extends JFlexLexer
%type String
%unicode
%ignorecase

/* code copied verbatim into the generated .java file */
%{
    private static final Logger logger = Logger.getLogger(JAMWikiPostProcessor.class.getName());
%}

/* character expressions */
newline            = "\n"
whitespace         = {newline} | [ \t\f]

/* nowiki */
nowiki             = (<[ ]*nowiki[ ]*>) ~(<[ ]*\/[ ]*nowiki[ ]*>)

/* pre */
htmlprestart       = (<[ ]*pre[ ]*>)
htmlpreend         = (<[ ]*\/[ ]*pre[ ]*>)

/* javascript */
javascript         = (<[ ]*script[^>]*>) ~(<[ ]*\/[ ]*script[ ]*>)

/* processing commands */
toc                = "__TOC__"

/* references */
references         = (<[ ]*) "references" ([ ]*[\/]?[ ]*>)

%state PRE

%%

/* ----- nowiki ----- */

<YYINITIAL, PRE>{nowiki} {
    logger.debug("nowiki: " + yytext() + " (" + yystate() + ")");
    return JFlexParserUtil.tagContent(yytext());
}

/* ----- pre ----- */

<YYINITIAL>{htmlprestart} {
    logger.debug("htmlprestart: " + yytext() + " (" + yystate() + ")");
    beginState(PRE);
    return yytext();
}

<PRE>{htmlpreend} {
    logger.debug("htmlpreend: " + yytext() + " (" + yystate() + ")");
    endState();
    return yytext();
}

/* ----- processing commands ----- */

<YYINITIAL>{toc} {
    logger.debug("toc: " + yytext() + " (" + yystate() + ")");
    return this.parserInput.getTableOfContents().attemptTOCInsertion();
}

/* ----- references ----- */

<YYINITIAL>{references} {
    logger.debug("references: " + yytext() + " (" + yystate() + ")");
    WikiReferencesTag parserTag = new WikiReferencesTag();
    return parserTag.parse(this.parserInput, this.mode, yytext());
}

/* ----- javascript ----- */

<YYINITIAL>{javascript} {
    logger.debug("javascript: " + yytext() + " (" + yystate() + ")");
    return yytext();
}

/* ----- other ----- */

<YYINITIAL, PRE>{whitespace} {
    // no need to log this
    return yytext();
}

<YYINITIAL, PRE>. {
    // no need to log this
    return yytext();
}
