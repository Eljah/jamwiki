package org.jamwiki.parser.bliki;

import info.bliki.htmlcleaner.TagNode;
import info.bliki.htmlcleaner.Utils;
import info.bliki.wiki.filter.HTMLConverter;
import info.bliki.wiki.model.IWikiModel;
import info.bliki.wiki.model.ImageFormat;

import java.io.IOException;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jamwiki.parser.ParserInput;
import org.jamwiki.utils.LinkUtil;
import org.jamwiki.utils.NamespaceHandler;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.jamwiki.DataAccessException;
import org.jamwiki.WikiBase;
import org.jamwiki.model.Topic;
import org.jamwiki.model.WikiFile;
import org.jamwiki.model.WikiImage;
import org.jamwiki.utils.ImageUtil;
import org.jamwiki.utils.WikiLink;

public class JAMHTMLConverter extends HTMLConverter {

    private static final Logger logger = Logger.getLogger(LinkUtil.class.getName());
    private static final int DEFAULT_THUMBNAIL_SIZE = 180;
    private ParserInput fParserInput;

    public JAMHTMLConverter(ParserInput parserInput) {
        super();
        fParserInput = parserInput;
    }

   
    @Override
    public void imageNodeToText(TagNode imageTagNode, ImageFormat imageFormat, Appendable resultBuffer, IWikiModel model)
            throws IOException {
        String imageName = imageFormat.getFilename();
        imageName = imageName.replaceAll("_", " ");
        Map<String, String> map = imageTagNode.getAttributes();
        String caption = imageFormat.getCaption();
        String alt = null;
        if (caption != null && caption.length() > 0) {
            alt = imageFormat.getAlt();
            caption = Utils.escapeXml(caption, true, false, true);
        }
        if (alt == null) {
            alt = "";
        }
        String location = imageFormat.getLocation();
        logger.debug("IMAGE-LINK-LOCATION =>: " + location);
        String type = imageFormat.getType();
        int pxWidth = imageFormat.getWidth();
        int pxHeight = imageFormat.getHeight();

        boolean frame = type == null ? false : type.equals("frame");
        boolean thumb = type == null ? false : type.equals("thumb");
        if (thumb && pxWidth <= 0) {
            pxWidth = DEFAULT_THUMBNAIL_SIZE;
        }

        try {
            String linkHtml = this.buildImageLinkHtml(fParserInput.getContext(), fParserInput.getVirtualWiki(), model.getImageNamespace() + NamespaceHandler.NAMESPACE_SEPARATOR + imageName, frame, thumb, imageFormat.getLocation(), imageFormat.getCaption(), pxWidth, false, null, false);
            logger.debug("IMAGE-LINK-HTML =>: " + linkHtml);
            resultBuffer.append(linkHtml);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    
    /**
     * Utility method for building an anchor tag that links to an image page
     * and includes the HTML image tag to display the image.
     *
     * @param context The servlet context for the link that is being created.
     * @param virtualWiki The virtual wiki for the link that is being created.
     * @param topicName The name of the image for which a link is being
     *  created.
     * @param frame Set to <code>true</code> if the image should display with
     *  a frame border.
     * @param thumb Set to <code>true</code> if the image should display as a
     *  thumbnail.
     * @param align Indicates how the image should horizontally align on the
     *  page.  Valid values are "left", "right" and "center".
     * @param caption An optional text caption to display for the image.  If
     *  no caption is used then this value should be either empty or
     *  <code>null</code>.
     * @param maxDimension A value in pixels indicating the maximum width or
     *  height value allowed for the image.  Images will be resized so that
     *  neither the width or height exceeds this value.
     * @param suppressLink If this value is <code>true</code> then the
     *  generated HTML will include the image tag without a link to the image
     *  topic page.
     * @param style The CSS class to use with the img HTML tag.  This value
     *  can be <code>null</code> or empty if no custom style is used.
     * @param escapeHtml Set to <code>true</code> if the caption should be
     *  HTML escaped.  This value should be <code>true</code> in any case
     *  where the caption is not guaranteed to be free from potentially
     *  malicious HTML code.
     * @return The full HTML required to display an image enclosed within an
     *  HTML anchor tag that links to the image topic page.
     * @throws DataAccessException Thrown if any error occurs while retrieving image
     *  information.
     * @throws IOException Thrown if any error occurs while reading image information.
     */
    public String buildImageLinkHtml(String context, String virtualWiki, String topicName, boolean frame, boolean thumb, String align, String caption, int maxDimension, boolean suppressLink, String style, boolean escapeHtml) throws DataAccessException, IOException {
        String url = LinkUtil.buildImageFileUrl(context, virtualWiki, topicName);
        if (url == null) {
            logger.debug("IMAGE-LINK-NULL!");
            StringBuilder sb = new StringBuilder();
            WikiLink uploadLink = LinkUtil.parseWikiLink("Special:Upload");
            String uploadLinkHtml = LinkUtil.buildInternalLinkHtml(context, virtualWiki, uploadLink, "Upload " + topicName, "edit", null, true);

            String imageName = topicName.trim().replaceAll(" ", "_").replaceFirst("Image:", "File:");

            if(caption == null){
                caption = "Wikipedia " + topicName;
            }

            //String imgSrcHtml = String.format("<img src=\"http://%s.wikipedia.org/wiki/%s\" width=\"40\" height=\"30\" alt=\"\" />", virtualWiki, imageName);
            String wikipediaLinkHtml = String.format("<a href=\"http://%s.wikipedia.org/wiki/%s\" class=\"%s\" title=\"%s\" target=\"_new\">%s</a>", virtualWiki, imageName, "image", topicName, caption);

            sb.append(wikipediaLinkHtml);
            sb.append("<br/>");
            sb.append(uploadLinkHtml);
            //logger.debug("UPLOAD-LINK-HTML: " + uploadLinkHtml);
            ///<a href="/wiki/en/Special:Upload" class="edit" title="Special:Upload">Image:Anarchy-symbol.svg</a>
            return sb.toString();
        }

        WikiFile wikiFile = WikiBase.getDataHandler().lookupWikiFile(virtualWiki, topicName);
        Topic topic = WikiBase.getDataHandler().lookupTopic(virtualWiki, topicName, false, null);

        StringBuffer html = new StringBuffer();
        // Topics can be files
        if (topic.getTopicType() == Topic.TYPE_FILE) {
            // file, not an image
            if (StringUtils.isBlank(caption)) {
                caption = topicName.substring(NamespaceHandler.NAMESPACE_IMAGE.length() + 1);
            }
            html.append("<a href=\"").append(url).append("\">");
            if (escapeHtml) {
                html.append(StringEscapeUtils.escapeHtml(caption));
            } else {
                html.append(caption);
            }
            html.append("</a>");
            return html.toString();
        }

        WikiImage wikiImage = ImageUtil.initializeImage(wikiFile, maxDimension);
        if (caption == null) {
            caption = "";
        }
        if (frame || thumb || !StringUtils.isBlank(align)) {
            html.append("<div class=\"");
            if (thumb || frame) {
                html.append("imgthumb ");
            }
            if (align != null && align.equalsIgnoreCase("left")) {
                html.append("imgleft ");
            } else if (align != null && align.equalsIgnoreCase("center")) {
                html.append("imgcenter ");
            } else if ((align != null && align.equalsIgnoreCase("right")) || thumb || frame) {
                html.append("imgright ");
            } else {
                // default alignment
                html.append("image ");
            }
            html = new StringBuffer(html.toString().trim()).append("\">");
        }
        if (wikiImage.getWidth() > 0) {
            html.append("<div style=\"width:").append((wikiImage.getWidth() + 2)).append("px;\">");
        }
        if (!suppressLink) {
            html.append("<a class=\"wikiimg\" href=\"").append(LinkUtil.buildTopicUrl(context, virtualWiki, topicName, true)).append("\">");
        }
        if (StringUtils.isBlank(style)) {
            style = "wikiimg";
        }
        html.append("<img class=\"").append(style).append("\" src=\"");
        html.append(url);
        html.append('\"');
        html.append(" width=\"").append(wikiImage.getWidth()).append('\"');
        html.append(" height=\"").append(wikiImage.getHeight()).append('\"');
        html.append(" alt=\"").append(StringEscapeUtils.escapeHtml(caption)).append('\"');
        html.append(" />");
        if (!suppressLink) {
            html.append("</a>");
        }
        if (!StringUtils.isBlank(caption)) {
            html.append("<div class=\"imgcaption\">");
            if (escapeHtml) {
                html.append(StringEscapeUtils.escapeHtml(caption));
            } else {
                html.append(caption);
            }
            html.append("</div>");
        }
        if (wikiImage.getWidth() > 0) {
            html.append("</div>");
        }
        if (frame || thumb || !StringUtils.isBlank(align)) {
            html.append("</div>");
        }
        return html.toString();
    }
}
