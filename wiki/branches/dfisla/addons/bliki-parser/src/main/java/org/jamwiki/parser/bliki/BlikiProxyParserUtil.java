/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jamwiki.parser.bliki;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jamwiki.utils.LinkUtil;
import org.jamwiki.utils.WikiLink;

/**
 *
 * @author dfisla
 */
public class BlikiProxyParserUtil {

    private static final Logger logger = Logger.getLogger(BlikiProxyParserUtil.class.getName());

    private static final Pattern WIKI_LINK_PATTERN = Pattern.compile("\\[\\[[ ]*(\\:[ ]*)?[ ]*([^\\n\\r\\|]+)([ ]*\\|[ ]*([^\\n\\r]+))?[ ]*\\]\\]([a-z]*)");

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
