/**
 *
 */
package org.jamwiki.parser.alt;

import java.io.*;
import org.jamwiki.Environment;
import org.jamwiki.WikiBase;
import org.jamwiki.utils.WikiLogger;
import org.jamwiki.parser.AbstractLexer;
import org.jamwiki.parser.ParserInput;
import org.jamwiki.utils.LinkUtil;
import org.jamwiki.utils.Utilities;

%%

%public
%type String
%unicode
%extends AbstractLexer
%class VQWikiLinkLex

%init{
	yybegin( NORMAL );
%init}

%eofval{
	return null;
%eofval}

%{
	protected static WikiLogger logger = WikiLogger.getLogger( VQWikiLinkLex.class.getName() );
	
	/**
	 *
	 */
	protected boolean exists(String topic) {
		try {
			return WikiBase.exists(this.parserInput.getVirtualWiki(), topic);
		} catch (Exception err) {
			logger.severe("Error looking up topic " + topic, err);
		}
		return false;
	}
	
	/**
	 *
	 */
	public void setParserInput(ParserInput parserInput) throws Exception {
		this.parserInput = parserInput;
	}
	
	/**
	 *
	 */
	protected String getTopicLink(String link, String description) {
		link = link.trim();
		description = description.trim();
		if (exists(link)) {
			try {
				return "<a class=\"topic\" href=\"" + LinkUtil.buildInternalLinkUrl(this.parserInput.getContext(), this.parserInput.getVirtualWiki(), link)
				+ "\">" + description + "</a>";
			} catch (Exception e) {
				return link;
			}
		} else if (description.equals(link)) {
			return "<a class=\"edit\" href=\"Special:Edit?topic=" + Utilities.encodeForURL(link)
				+ "\">" + description + "</a>";
		} else {
			return description + " (<a class=\"edit\" href=\"Special:Edit?topic=" + Utilities.encodeForURL(link)
				+ "\">" + link + "</a>)";
		}
	}
%}

whitespace = ([\t\ \r\n])
notbacktick_tabcrlf = ([^`\t\r\n])
notsquares_tabcrlf = ([^\]\t\r\n])
notbacktick_notsquares_tabcrlf = ([^`\]\t\r\n])
wikiname = (([:uppercase:]+[:lowercase:]+)([:uppercase:]+[:lowercase:]+)+)
topic = ({wikiname})
topicbacktick = (`{notbacktick_tabcrlf}+`)
topicbackticktail = (`{notbacktick_tabcrlf}+`([:letter:])+)
topicsquarebracket = (\[\[{notsquares_tabcrlf}+\]\])
topicsquarebrackettail = (\[\[{notsquares_tabcrlf}+\]\]([:letter:])+)
prettytopicsquarebracket = (\[\[{notsquares_tabcrlf}+\|{notsquares_tabcrlf}+\]\])
prettytopicsquarebrackettail = (\[\[{notsquares_tabcrlf}+\|{notsquares_tabcrlf}+\]\]([:letter:])+)
protocols = (http|ftp|mailto|news|https|telnet|file)
extlinkchar = ([^\t\ \r\n\<\>])
prettyextlinkchar = ([^\t\r\n\<\>\|])
hyperlink = ({protocols}:{extlinkchar}+)
framedhyperlink = (\[({protocols}:{prettyextlinkchar}+)\])
prettyhyperlink = (\[({protocols}:{prettyextlinkchar}+)\|{notsquares_tabcrlf}+\])
image = ({hyperlink}(\.gif|\.jpg|\.png|\.jpeg|\.GIF|\.JPG|\.PNG|\.JPEG|\.bmp|\.BMP))
extlink = (([:letter:]|[:digit:])+:{extlinkchar}+)
framedextlink = (\[([:letter:]|[:digit:])+:{notbacktick_notsquares_tabcrlf}+\])
noformat = (__)
externalstart = (\[<[A-Za-z]+>\])
externalend = (\[<\/[A-Za-z]+>\])

%state NORMAL, OFF, PRE, EXTERNAL

%%
<NORMAL>\\{noformat}	{
  logger.fine( "escaped double backslash" );
  return "__";
}

<NORMAL>{noformat}	{
  logger.fine( "off" );
  yybegin( OFF );
}

<OFF>{noformat}	{
  logger.fine( "on" );
  yybegin( NORMAL );
}

<NORMAL, PRE>{externalstart} {
  logger.fine( "external" );
  yybegin( EXTERNAL );
}

<EXTERNAL>{externalend} {
  logger.fine( "external end");
  yybegin( NORMAL );
}

<NORMAL>(<pre>) {
  logger.fine( "@@@@{newline} entering PRE" );
  yybegin( PRE );
  return yytext();
}

<PRE>(<\/pre>) {
  logger.fine( "{newline}x2 leaving pre" );
  yybegin( NORMAL );
  return yytext();
}

<NORMAL>{image}	{
  logger.fine( "{image}" );
  String link = yytext();
  return "<img src=\"" + link.trim() + "\"/>";
}

<NORMAL>{hyperlink}	{
  logger.fine( "{hyperlink}" );
  String link = yytext();
  String punctuation = Utilities.extractTrailingPunctuation(link);

  if(punctuation!=null){
    link = link.substring(0, link.length()-punctuation.length());
  }

  return "<a class=\"externallink\" href=\"" + link.trim() + "\">" +
    link + "</a>" + punctuation;
}

<NORMAL>{framedhyperlink}	{
  logger.fine( "{framedhyperlink}" );
  String link = yytext();

  link = link.substring(1, link.length()-1).trim();

  return "<a class=\"externallink\" href=\"" + link.trim() + "\">" +
    link + "</a>";
}

<NORMAL>{prettyhyperlink}
{
  logger.fine( "{prettyhyperlink}" + yytext() );
  String input = yytext();
  int position = input.indexOf('|');
  
  String link = null;
  String desc = null;
  link = input.substring(1, position).trim();
  desc = input.substring(position + 1, input.length() - 1).trim();
  if (desc.length() == 0)
  {
     desc = link;
  }

  return "<a class=\"externallink\" href=\"" + link.trim() + "\" title= \"" + link + "\" rel=\"nofollow\">" +
    desc + "</a>";
}

<NORMAL>{prettytopicsquarebracket}
{
  logger.fine( "{prettytopicsquarebracket} '" + yytext() + "'" );
  String input = yytext();
  int position = input.indexOf('|');
  
  String link = null;
  String desc = null;
  link = input.substring(2, position).trim();
  desc = input.substring(position + 1, input.length() - 2).trim();
  if (desc.length() == 0)
  {
     desc = link;
  }

  return getTopicLink(link, desc);
}


<NORMAL>{prettytopicsquarebrackettail}
{
  logger.fine( "{prettytopicsquarebrackettail} '" + yytext() + "'" );
  String input = yytext();
  int position = input.indexOf('|');
  
  String link = null;
  String desc = null;
  link = input.substring(2, position).trim();
  desc = input.substring(position + 1, input.indexOf("]]")).trim();
  if (desc.length() == 0)
  {
     desc = link;
  }
  desc = desc + input.substring(input.indexOf("]]") + 2);

  return getTopicLink(link, desc);
}

<NORMAL>{topic} {
  logger.fine( "{topic} '" + yytext() + "'" );
  String link = yytext();
  return getTopicLink(link, link);
}

<NORMAL>{topicbacktick} {
  logger.fine( "{topicbacktick} '" + yytext() + "'" );
  String link = yytext();
  link = link.substring(1);
  link = link.substring( 0, link.length() - 1).trim();
  return getTopicLink(link, link);
}

<NORMAL>{topicbackticktail} {
  logger.fine( "{topicbackticktail} '" + yytext() + "'" );
  String link = yytext();
  link = link.substring(1);
  String desc = link.substring(link.indexOf('`') + 1).trim();
  link = link.substring( 0, link.indexOf('`')).trim();
  desc = link + desc;
  return getTopicLink(link, desc);
}

<NORMAL>{topicsquarebracket} {
  logger.fine( "{topicsquarebracket} '" + yytext() + "'");
  String link = yytext();
  link = link.substring( 2, link.length() - 2).trim();
  return getTopicLink(link, link);
}

<NORMAL>{topicsquarebrackettail} {
  logger.fine( "{topicsquarebrackettail} '" + yytext() + "'");
  String link = yytext();
  link = link.substring(2);
  String desc = link.substring(link.indexOf("]]") + 2);
  link = link.substring( 0, link.indexOf("]]")).trim();
  desc = link + desc;
  return getTopicLink(link, desc);
}

<NORMAL>{extlink} {
  logger.fine("{extlink}");
  String text = yytext();
  try{
    return LinkExtender.generateLink(
      text.substring( 0, text.indexOf( ':' ) ),
      text.substring( text.indexOf( ':' ) + 1 ),
      text
    );
  }catch( Exception err ){
    logger.severe( "error generating link from extender", err );
    return text;
  }
}

<NORMAL>{framedextlink} {
  logger.fine("{framedextlink}");
  String text = yytext();
  // trim off the square brackets
  text = text.substring(1, text.length()-1);
  try{
    return LinkExtender.generateLink(
      text.substring( 0, text.indexOf( ':' ) ),
      text.substring( text.indexOf( ':' ) + 1 ),
      text
    );
  }catch( Exception err ){
    logger.severe( "error generating link from extender", err );
    return text;
  }
}

<NORMAL, OFF, PRE, EXTERNAL>{whitespace} {
  return yytext();
}

<NORMAL, OFF, PRE, EXTERNAL>.  {
// logger.fine( ". (" + yytext() + ")" );
 return yytext();
}
