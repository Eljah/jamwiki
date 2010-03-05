/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jamwiki.parser.bliki;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang.StringUtils;
import org.jamwiki.parser.ParserException;
import org.jamwiki.parser.ParserInput;
import org.apache.log4j.Logger;
import org.jamwiki.parser.ParserOutput;
import org.jamwiki.parser.jflex.JFlexParserUtil;
import org.jamwiki.utils.LinkUtil;
import org.jamwiki.utils.WikiLink;

/**
 *
 * @author dfisla
 */
public class BlikiProxyParserUtil {

    private static final Logger logger = Logger.getLogger(JFlexParserUtil.class.getName());

    private static final Pattern WIKI_LINK_PATTERN = Pattern.compile("\\[\\[[ ]*(\\:[ ]*)?[ ]*([^\\n\\r\\|]+)([ ]*\\|[ ]*([^\\n\\r]+))?[ ]*\\]\\]([a-z]*)");


    /**
     * Provide a way to run the pre-processor against a fragment of text, such
     * as an image caption.  This method should be used sparingly since it is
     * not very efficient.
     * @param parserInput 
     * @param raw 
     * @return HTML string representation of input
     * @throws ParserException
     */
    /*
    protected static String parseFragment(ParserInput parserInput, String raw) throws ParserException {
        if (StringUtils.isBlank(raw)) {
            return raw;
        }

        ParserOutput parserOutput = new ParserOutput();
        String output = null;

        String context = parserInput.getContext();
        if (context == null) {
            context = "";
        }
        JAMWikiModel wikiModel = new JAMWikiModel(parserInput, parserOutput, context);

        output = wikiModel.render(new JAMHTMLConverter(parserInput), raw);
        logger.debug("RAW: " + raw + " OUTPUT: " + output);
        output = output == null ? "" : output;

        return output;
    }
    */
    /**
     * Parse Mediawiki Link
     * @param raw
     * @return WikiLink object
     */
    protected static WikiLink parseWikiLink(String raw) {
        if (StringUtils.isBlank(raw)) {
            return new WikiLink();
        }
        Matcher m = WIKI_LINK_PATTERN.matcher(raw.trim());
        if (!m.matches()) {
            return new WikiLink();
        }
        String url = m.group(2);
        WikiLink wikiLink = LinkUtil.parseWikiLink(url);
        wikiLink.setColon((m.group(1) != null));
        wikiLink.setText(m.group(4));
        String suffix = m.group(5);
        if (!StringUtils.isBlank(suffix)) {
            if (StringUtils.isBlank(wikiLink.getText())) {
                wikiLink.setText(wikiLink.getDestination() + suffix);
            } else {
                wikiLink.setText(wikiLink.getText() + suffix);
            }
        }
        return wikiLink;
    }
}
