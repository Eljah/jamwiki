/**
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE, version 2.1, dated February 1999.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the latest version of the GNU Lesser General
 * Public License as published by the Free Software Foundation;
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program (LICENSE.txt); if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package org.jamwiki.parser.image;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import net.sf.ehcache.Element;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.jamwiki.DataAccessException;
import org.jamwiki.Environment;
import org.jamwiki.ImageData;
import org.jamwiki.WikiBase;
import org.jamwiki.WikiException;
import org.jamwiki.WikiMessage;
import org.jamwiki.model.Namespace;
import org.jamwiki.model.Topic;
import org.jamwiki.model.TopicType;
import org.jamwiki.model.TopicVersion;
import org.jamwiki.model.WikiImage;
import org.jamwiki.model.WikiFile;
import org.jamwiki.model.WikiFileVersion;
import org.jamwiki.model.WikiUser;
import org.jamwiki.parser.LinkUtil;
import org.jamwiki.parser.ParserException;
import org.jamwiki.parser.ParserOutput;
import org.jamwiki.parser.ParserUtil;
import org.jamwiki.parser.WikiLink;
import org.jamwiki.utils.Utilities;
import org.jamwiki.utils.WikiCache;
import org.jamwiki.utils.WikiLogger;
import org.jamwiki.utils.WikiUtil;

/**
 * Utility methods for performing wiki-specific image tasks, such as generating
 * HTML to display an image or building links to images.
 */
public abstract class ImageUtil {

	private static final WikiLogger logger = WikiLogger.getLogger(ImageUtil.class.getName());
	/** Cache name for the cache of image dimensions. */
	private static final String CACHE_IMAGE_DIMENSIONS = "org.jamwiki.parser.image.ImageUtil.CACHE_IMAGE_DIMENSIONS";
	/** Sub-folder of the "files" directory into which to place resized images. */
	private static final String RESIZED_IMAGE_SUBFOLDER = "resized";
	/** Path to the template used to format a center-aligned image. */
	private static final String TEMPLATE_IMAGE_ALIGN_CENTER = "templates/image-align-center.template";
	/** Path to the template used to format a left-aligned image. */
	private static final String TEMPLATE_IMAGE_ALIGN_LEFT = "templates/image-align-left.template";
	/** Path to the template used to format an image that is not aligned. */
	private static final String TEMPLATE_IMAGE_ALIGN_NONE = "templates/image-align-none.template";
	/** Path to the template used to format a right-aligned image. */
	private static final String TEMPLATE_IMAGE_ALIGN_RIGHT = "templates/image-align-right.template";
	/** Path to the template used to format gallery elements without captions. */
	private static final String TEMPLATE_IMAGE_GALLERY_ELEMENT_NO_CAPTION = "templates/image-gallery-element-no-caption.template";
	/** Path to the template used to format gallery elements with captions. */
	private static final String TEMPLATE_IMAGE_GALLERY_ELEMENT_WITH_CAPTION = "templates/image-gallery-element-with-caption.template";
	/** Path to the template used to format img tags that are not vertical aligned. */
	private static final String TEMPLATE_IMAGE_IMG_STANDARD = "templates/image-img-standard.template";
	/** Path to the template used to format img tags that are vertical aligned. */
	private static final String TEMPLATE_IMAGE_IMG_VERTICAL = "templates/image-img-vertical.template";
	/** Path to the template used to format a plain image. */
	private static final String TEMPLATE_IMAGE_PLAIN = "templates/image-plain.template";
	/** Path to the template used to format center-aligned image thumbnails. */
	private static final String TEMPLATE_IMAGE_THUMBNAIL_CENTER = "templates/image-thumbnail-center.template";
	/** Path to the template used to format left-aligned image thumbnails. */
	private static final String TEMPLATE_IMAGE_THUMBNAIL_LEFT = "templates/image-thumbnail-left.template";
	/** Path to the template used to format right-aligned image thumbnails. */
	private static final String TEMPLATE_IMAGE_THUMBNAIL_RIGHT = "templates/image-thumbnail-right.template";

	/**
	 *
	 */
	private static void addToCache(WikiImage wikiImage, Dimension dimensions) {
		String key = wikiImage.getVirtualWiki() + "/" + wikiImage.getUrl();
		WikiCache.addToCache(CACHE_IMAGE_DIMENSIONS, key, dimensions);
	}

	/**
	 * Utility method for building the URL to an image file (NOT the image topic
	 * page).  If the file does not exist then this method will return
	 * <code>null</code>.
	 *
	 * @param virtualWiki The virtual wiki for the URL that is being created.
	 * @param topicName The name of the image for which a link is being created.
	 * @return The URL to an image file (not the image topic) or <code>null</code>
	 *  if the file does not exist.
	 * @throws DataAccessException Thrown if any error occurs while retrieving file info.
	 */
	public static String buildImageFileUrl(String virtualWiki, String topicName) throws DataAccessException {
		WikiFile wikiFile = WikiBase.getDataHandler().lookupWikiFile(virtualWiki, topicName);
		if (wikiFile == null) {
			return null;
		}
		return buildRelativeImageUrl(isImagesOnFS() ? wikiFile.getUrl() : ("?fileId=" + wikiFile.getFileId()));
	}

	/**
	 *
	 */
	private static String buildRelativeImageUrl(String filename) {
		if (isImagesOnFS()) {
			String url = FilenameUtils.normalize(Environment.getValue(Environment.PROP_FILE_DIR_RELATIVE_PATH) + "/" + filename);
			return FilenameUtils.separatorsToUnix(url);
		} else {
			return getImageServletUrl() + filename;
		}
	}

	/**
	 * Utility method for building an anchor tag that links to an image page
	 * and includes the HTML image tag to display the image.
	 *
	 * @param context The servlet context for the link that is being created.
	 * @param linkVirtualWiki The virtual wiki to use when looking up the
	 *  image/file, and when linking to the image/file topic page.
	 * @param topicName The name of the image for which a link is being
	 *  created.
	 * @param imageMetadata A container for the image display params, such as
	 *  border, alignment, caption, etc.
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
	public static String buildImageLinkHtml(String context, String linkVirtualWiki, String topicName, ImageMetadata imageMetadata, String style, boolean escapeHtml) throws DataAccessException, IOException {
		String url = ImageUtil.buildImageFileUrl(linkVirtualWiki, topicName);
		if (url == null) {
			return ImageUtil.buildUploadLink(context, linkVirtualWiki, topicName);
		}
		Topic topic = WikiBase.getDataHandler().lookupTopic(linkVirtualWiki, topicName, false);
		String caption = imageMetadata.getCaption();
		if (topic.getTopicType() == TopicType.FILE) {
			// file, not an image - use the file name, minus the translated/untranslated namespace
			return ImageUtil.buildLinkToFile(url, topic, caption, escapeHtml);
		}
		WikiFile wikiFile = WikiBase.getDataHandler().lookupWikiFile(topic.getVirtualWiki(), topic.getName());
		WikiImage wikiImage = null;
		try {
			wikiImage = ImageUtil.initializeWikiImage(wikiFile, imageMetadata);
		} catch (FileNotFoundException e) {
			// do not log the full exception as the logs can fill up very for this sort of error, and it is generally due to a bad configuration.  instead log a warning message so that the administrator can try to fix the problem
			logger.warn("File not found while parsing image link for topic: " + topic.getVirtualWiki() + " / " + topicName + ".  Make sure that the following file exists and is readable by the JAMWiki installation: " + e.getMessage());
			return ImageUtil.buildUploadLink(context, topic.getVirtualWiki(), topicName);
		}
		if (wikiImage == null) {
			return ImageUtil.buildLinkToFile(url, topic, caption, escapeHtml);
		}
		if (StringUtils.isBlank(style)) {
			style = "wikiimg";
		}
		if (imageMetadata.getBordered()) {
			style += " thumbborder";
		}
		Object[] args = (imageMetadata.getVerticalAlignment() != ImageVerticalAlignmentEnum.NOT_SPECIFIED) ? new Object[6] : new Object[5];
		args[0] = style;
		args[1] = buildRelativeImageUrl(wikiImage.getUrl());
		args[2] = wikiImage.getWidth();
		args[3] = wikiImage.getHeight();
		args[4] = StringEscapeUtils.escapeHtml4(imageMetadata.getAlt());
		// TODO: combine the standard & vertical templates by adding a CSS class for
		// vertical alignment
		String template = TEMPLATE_IMAGE_IMG_STANDARD;
		if (imageMetadata.getVerticalAlignment() != ImageVerticalAlignmentEnum.NOT_SPECIFIED) {
			template = TEMPLATE_IMAGE_IMG_VERTICAL;
			args[5] = imageMetadata.getVerticalAlignment().toString();
		}
		StringBuilder html = new StringBuilder();
		String imageHtml = WikiUtil.formatFromTemplate(template, args);
		if (!StringUtils.isWhitespace(imageMetadata.getLink())) {
			// wrap the image in a link
			if (imageMetadata.getLink() == null) {
				// no link set, link to the image topic page.  At this point we have validated
				// that the link is an image, so do not perform further validation and link to the
				// CURRENT virtual wiki, even if it is a shared image
				WikiLink wikiLink = LinkUtil.parseWikiLink(context, linkVirtualWiki, topicName);
				String link = wikiLink.toRelativeUrl();
				html.append("<a class=\"wikiimg\" href=\"").append(link).append("\">");
				html.append(imageHtml);
				html.append("</a>");
			} else {
				try {
					// try to parse as an external link
					html.append(LinkUtil.buildExternalLinkHtml(imageMetadata.getLink(), "wikiimg", imageHtml));
				} catch (ParserException e) {
					// not an external link, but an internal link
					WikiLink wikiLink = LinkUtil.parseWikiLink(context, topic.getVirtualWiki(), imageMetadata.getLink());
					String link = LinkUtil.buildTopicUrl(wikiLink);
					html.append("<a class=\"wikiimg\" href=\"").append(link).append("\">");
					html.append(imageHtml);
					html.append("</a>");
				}
			}
		} else {
			html.append(imageHtml);
		}
		if (!StringUtils.isBlank(caption) && imageMetadata.getBorder() != ImageBorderEnum._GALLERY) {
			// captions are only displayed for thumbnails and framed images.  galleries are handled separately.
			html.append("\n<div class=\"thumbcaption\">");
			if (escapeHtml) {
				html.append(StringEscapeUtils.escapeHtml4(caption));
			} else {
				html.append(caption);
			}
			html.append("</div>\n");
		}
		return ImageUtil.buildWrappedImageDiv(imageMetadata, wikiImage.getWidth(), wikiImage.getHeight(), html.toString());
	}

	/**
	 * Given a file URL and a maximum dimension, return a relative path for the file.
	 */
	private static String buildImagePath(String currentUrl, int originalWidth, int scaledWidth) {
		if (originalWidth <= scaledWidth) {
			// no resizing necessary, return the original URL
			return currentUrl;
		}
		String path = FilenameUtils.normalize(RESIZED_IMAGE_SUBFOLDER + "/" + currentUrl);
		String dimensionInfo = "-" + scaledWidth + "px";
		int pos = path.lastIndexOf('.');
		if (pos != -1) {
			path = path.substring(0, pos) + dimensionInfo + path.substring(pos);
		} else {
			path += dimensionInfo;
		}
		return path;
	}

	/**
	 * Determine the CSS styles to apply to the image wrapper div.
	 */
	private static String buildWrappedImageDiv(ImageMetadata imageMetadata, int width, int height, String imageHtml) throws IOException {
		// CSS and wrappers are processed differently for thumb/frame vs. non-thumb/non-frame
		if (imageMetadata.getBorder() == ImageBorderEnum._GALLERY) {
			// gallery images.
			int verticalPadding = ((imageMetadata.getGalleryHeight() - height) > 0) ? (int)Math.floor((imageMetadata.getGalleryHeight() - height) / 2) : 0;
			Object[] args = (StringUtils.isBlank(imageMetadata.getCaption())) ? new Object[4] : new Object[5];
			args[0] = width + 35;
			args[1] = verticalPadding;
			args[2] = width + 2;
			args[3] = imageHtml;
			String template = TEMPLATE_IMAGE_GALLERY_ELEMENT_NO_CAPTION;
			if (!StringUtils.isBlank(imageMetadata.getCaption())) {
				args[4] = imageMetadata.getCaption();
				template = TEMPLATE_IMAGE_GALLERY_ELEMENT_WITH_CAPTION;
			}
			return WikiUtil.formatFromTemplate(template, args);
		} else if (imageMetadata.getBorder() != ImageBorderEnum.THUMB && imageMetadata.getBorder() != ImageBorderEnum.FRAME) {
			// non-thumbnail images
			if (imageMetadata.getHorizontalAlignment() == ImageHorizontalAlignmentEnum.LEFT) {
				return WikiUtil.formatFromTemplate(TEMPLATE_IMAGE_ALIGN_LEFT, imageHtml);
			} else if (imageMetadata.getHorizontalAlignment() == ImageHorizontalAlignmentEnum.RIGHT) {
				return WikiUtil.formatFromTemplate(TEMPLATE_IMAGE_ALIGN_RIGHT, imageHtml);
			} else if (imageMetadata.getHorizontalAlignment() == ImageHorizontalAlignmentEnum.CENTER) {
				return WikiUtil.formatFromTemplate(TEMPLATE_IMAGE_ALIGN_CENTER, imageHtml);
			} else if (imageMetadata.getHorizontalAlignment() == ImageHorizontalAlignmentEnum.NONE) {
				return WikiUtil.formatFromTemplate(TEMPLATE_IMAGE_ALIGN_NONE, imageHtml);
			} else {
				return WikiUtil.formatFromTemplate(TEMPLATE_IMAGE_PLAIN, imageHtml);
			}
		} else {
			// thumbnail images
			if (imageMetadata.getHorizontalAlignment() == ImageHorizontalAlignmentEnum.CENTER) {
				return WikiUtil.formatFromTemplate(TEMPLATE_IMAGE_THUMBNAIL_CENTER, width + 2, imageHtml);
			} else if (imageMetadata.getHorizontalAlignment() == ImageHorizontalAlignmentEnum.LEFT) {
				return WikiUtil.formatFromTemplate(TEMPLATE_IMAGE_THUMBNAIL_LEFT, width + 2, imageHtml);
			} else {
				return WikiUtil.formatFromTemplate(TEMPLATE_IMAGE_THUMBNAIL_RIGHT, width + 2, imageHtml);
			}
		}
	}

	/**
	 * Generate an HTML link to the image file without any resizing.
	 */
	private static String buildLinkToFile(String url, Topic topic, String caption, boolean escapeHtml) {
		StringBuilder html = new StringBuilder();
		if (StringUtils.isBlank(caption)) {
			caption = topic.getPageName();
		}
		html.append("<a href=\"").append(url).append("\">");
		if (escapeHtml) {
			html.append(StringEscapeUtils.escapeHtml4(caption));
		} else {
			html.append(caption);
		}
		html.append("</a>");
		return html.toString();
	}

	/**
	 *
	 */
	private static String buildUploadLink(String context, String virtualWiki, String topicName) throws DataAccessException {
		WikiLink uploadLink = LinkUtil.parseWikiLink(context, virtualWiki, "Special:Upload?topic=" + Utilities.encodeAndEscapeTopicName(topicName));
		return LinkUtil.buildInternalLinkHtml(uploadLink, topicName, "edit", null, true);
	}

	/**
	 *
	 */
	private static int calculateImageIncrement(double dimension) {
		int increment = Environment.getIntValue(Environment.PROP_IMAGE_RESIZE_INCREMENT);
		return (int)(Math.ceil(dimension / (double)increment) * increment);
	}

	/**
	 * Determine the scaled dimensions, rounded to an increment for performance reasons,
	 * given a max width and height.  For example, if the original dimensions are 800x400,
	 * the max width height are 200, and the increment is 400, the result is 400x200.
	 */
	private static Dimension calculateIncrementalDimensions(WikiImage wikiImage, Dimension originalDimensions, Dimension scaledDimensions) throws IOException {
		if (isImagesOnFS()) {
			return calculateIncrementalDimensionsForImageFile(wikiImage, originalDimensions, scaledDimensions);
		} else {
			return calculateIncrementalDimensionsForImageBlob(wikiImage, originalDimensions, scaledDimensions);
		}
	}

	private static Dimension calculateIncrementalDimensionsForImageFile(WikiImage wikiImage, Dimension originalDimensions, Dimension scaledDimensions) throws IOException {
		int increment = Environment.getIntValue(Environment.PROP_IMAGE_RESIZE_INCREMENT);
		// use width for incremental resizing
		int incrementalWidth = calculateImageIncrement(scaledDimensions.getWidth());
		if (increment <= 0 || incrementalWidth >= originalDimensions.getWidth()) {
			// let the browser scale the image
			return originalDimensions;
		}
		int incrementalHeight = (int)Math.round(((double)incrementalWidth / (double)originalDimensions.getWidth()) * (double)originalDimensions.getHeight());
		// check to see if an image with the desired dimensions already exists on the filesystem
		String newUrl = buildImagePath(wikiImage.getUrl(), (int)originalDimensions.getWidth(), incrementalWidth);
		File newImageFile = new File(Environment.getValue(Environment.PROP_FILE_DIR_FULL_PATH), newUrl);
		if (newImageFile.exists()) {
			return new Dimension(incrementalWidth, incrementalHeight);
		}
		// otherwise generate a scaled instance
		File imageFile = new File(Environment.getValue(Environment.PROP_FILE_DIR_FULL_PATH), wikiImage.getUrl());
		BufferedImage bufferedImage = ImageProcessor.resizeImage(imageFile, incrementalWidth, incrementalHeight);
		newUrl = buildImagePath(wikiImage.getUrl(), (int)originalDimensions.getWidth(), bufferedImage.getWidth());
		newImageFile = new File(Environment.getValue(Environment.PROP_FILE_DIR_FULL_PATH), newUrl);
		ImageProcessor.saveImage(bufferedImage, newImageFile);
		return new Dimension(bufferedImage.getWidth(), bufferedImage.getHeight());
	}

	private static Dimension calculateIncrementalDimensionsForImageBlob(WikiImage wikiImage, Dimension originalDimensions, Dimension scaledDimensions) throws IOException {
		int increment = Environment.getIntValue(Environment.PROP_IMAGE_RESIZE_INCREMENT);
		// use width for incremental resizing
		int incrementalWidth = calculateImageIncrement(scaledDimensions.getWidth());
		if (increment <= 0 || incrementalWidth >= originalDimensions.getWidth()) {
			// let the browser scale the image
			return originalDimensions;
		}
		int incrementalHeight = (int)Math.round(((double)incrementalWidth / (double)originalDimensions.getWidth()) * (double)originalDimensions.getHeight());
		// check to see if an image with the desired dimensions already exists on the filesystem
		Dimension d1  = ImageProcessor.retrieveImageDimensions(wikiImage.getFileId(), incrementalWidth);
		if       (d1 != null)
		{
			return d1;
		}
		// otherwise generate a scaled instance
		return ImageProcessor.resizeImage(wikiImage.getFileId(), incrementalWidth, incrementalHeight);
	}

	/**
	 * Determine the scaled dimensions, given a max width and height.  For example, if
	 * the original dimensions are 800x400 and the max width height are 200, the result
	 * is 200x100.
	 */
	private static Dimension calculateScaledDimensions(Dimension originalDimensions, int maxWidth, int maxHeight) {
		if (maxWidth <= 0 && maxHeight <=0) {
			return originalDimensions;
		}
		double heightScalingFactor = ((double)maxHeight / (double)originalDimensions.getHeight());
		double widthScalingFactor = ((double)maxWidth / (double)originalDimensions.getWidth());
		// scale by whichever is proportionally smaller
		int width, height;
		if (maxWidth <= 0) {
			width = (int)Math.round(heightScalingFactor * (double)originalDimensions.getWidth());
			height = (int)Math.round(heightScalingFactor * (double)originalDimensions.getHeight());
		} else if (maxHeight <= 0) {
			width = (int)Math.round(widthScalingFactor * (double)originalDimensions.getWidth());
			height = (int)Math.round(widthScalingFactor * (double)originalDimensions.getHeight());
		} else if (heightScalingFactor < widthScalingFactor) {
			width = (int)Math.round(heightScalingFactor * (double)originalDimensions.getWidth());
			height = (int)Math.round(heightScalingFactor * (double)originalDimensions.getHeight());
		} else {
			width = (int)Math.round(widthScalingFactor * (double)originalDimensions.getWidth());
			height = (int)Math.round(widthScalingFactor * (double)originalDimensions.getHeight());
		}
		return new Dimension(width, height);
	}

	/**
	 * Given a filename, generate the URL to use to store the file on the filesystem.
	 */
	public static String generateFileUrl(String virtualWiki, String filename, Date date) throws WikiException {
		if (StringUtils.isBlank(virtualWiki)) {
			throw new WikiException(new WikiMessage("common.exception.novirtualwiki"));
		}
		String url = filename;
		if (StringUtils.isBlank(url)) {
			throw new WikiException(new WikiMessage("upload.error.filename"));
		}
		// file is appended with a timestamp of DDHHMMSS
		GregorianCalendar cal = new GregorianCalendar();
		if (date != null) {
			cal.setTime(date);
		}
		String day = Integer.toString(cal.get(Calendar.DAY_OF_MONTH));
		if (day.length() == 1) {
			day = "0" + day;
		}
		String hour = Integer.toString(cal.get(Calendar.HOUR_OF_DAY));
		if (hour.length() == 1) {
			hour = "0" + hour;
		}
		String minute = Integer.toString(cal.get(Calendar.MINUTE));
		if (minute.length() == 1) {
			minute = "0" + minute;
		}
		String second = Integer.toString(cal.get(Calendar.SECOND));
		if (second.length() == 1) {
			second = "0" + second;
		}
		String suffix = "-" + day + hour + minute + second;
		int pos = url.lastIndexOf('.');
		url = (pos == -1) ? url + suffix : url.substring(0, pos) + suffix + url.substring(pos);
		// now pre-pend the file system directory
		// subdirectory is composed of vwiki/year/month
		String year = Integer.toString(cal.get(Calendar.YEAR));
		String month = Integer.toString(cal.get(Calendar.MONTH) + 1);
		String subdirectory = "/" + virtualWiki + "/" + year + "/" + month;
		if (isImagesOnFS()) {
			File directory = new File(Environment.getValue(Environment.PROP_FILE_DIR_FULL_PATH), subdirectory);
			if (!directory.exists() && !directory.mkdirs()) {
				throw new WikiException(new WikiMessage("upload.error.directorycreate", directory.getAbsolutePath()));
			}
		}
		return subdirectory + "/" + url;
	}

	/**
	 * Given an image file name, generate the appropriate topic name for the image.
	 */
	public static String generateFileTopicName(String virtualWiki, String filename) {
		String topicName = Namespace.namespace(Namespace.FILE_ID).getLabel(virtualWiki) + Namespace.SEPARATOR;
		topicName += Utilities.decodeAndEscapeTopicName(filename, true);
		return topicName;
	}

	/**
	 * Given a virtualWiki and WikiFIle that correspond to an existing image,
	 * return the WikiImage object.  In addition, if the image metadata specifies
	 * a max width or max height greater than zero then a resized version of the
	 * image may be created.
	 *
	 * @param wikiFile Given a WikiFile object, use it to initialize a
	 *  WikiImage object.
	 * @param imageMetadata The maximum width or height for the initialized
	 *  WikiImage object.  Setting this value to 0 or less will cause the
	 *  value to be ignored.
	 * @return An initialized WikiImage object.
	 * @throws IOException Thrown if an error occurs while initializing the
	 *  WikiImage object.
	 */
	private static WikiImage initializeWikiImage(WikiFile wikiFile, ImageMetadata imageMetadata) throws DataAccessException, IOException {
		if (wikiFile == null) {
			throw new IllegalArgumentException("wikiFile may not be null");
		}
		WikiImage wikiImage = new WikiImage(wikiFile);
		// get the size of the original (unresized) image
		Dimension originalDimensions = ImageUtil.retrieveFromCache(wikiImage);
		if (originalDimensions == null) {
			if (isImagesOnFS()) {
				File file = new File(Environment.getValue(Environment.PROP_FILE_DIR_FULL_PATH), wikiImage.getUrl());
				originalDimensions = ImageProcessor.retrieveImageDimensions(file);
			} else {
				originalDimensions = ImageProcessor.retrieveImageDimensions(wikiImage.getFileId(), 0);
			}
			if (originalDimensions == null) {
				logger.info("Unable to determine dimensions for image: " + wikiImage.getUrl());
				return null;
			}
			addToCache(wikiImage, originalDimensions);
		}
		if (!imageMetadata.getAllowEnlarge() && imageMetadata.getMaxWidth() > originalDimensions.getWidth() && imageMetadata.getMaxHeight() > originalDimensions.getHeight()) {
			imageMetadata.setMaxWidth((int)originalDimensions.getWidth());
			imageMetadata.setMaxHeight((int)originalDimensions.getHeight());
		}
		// determine the width & height of scaled image (if needed)
		Dimension scaledDimensions = calculateScaledDimensions(originalDimensions, imageMetadata.getMaxWidth(), imageMetadata.getMaxHeight());
		wikiImage.setWidth((int)scaledDimensions.getWidth());
		wikiImage.setHeight((int)scaledDimensions.getHeight());
		// return an appropriate WikiImage object with URL to the scaled image, proper width, and proper height
		Dimension incrementalDimensions = calculateIncrementalDimensions(wikiImage, originalDimensions, scaledDimensions);
		if (isImagesOnFS()) {
			String url = buildImagePath(wikiImage.getUrl(), (int)originalDimensions.getWidth(), (int)incrementalDimensions.getWidth());
			wikiImage.setUrl(url);
		} else {
			int resized  = incrementalDimensions.width != originalDimensions.width ? incrementalDimensions.width : 0;
			String url = "?fileId=" + wikiImage.getFileId() + "&resized=" + resized;
			wikiImage.setUrl(url);
		}
		return wikiImage;
	}

	/**
	 * Utility method for determining if a file name corresponds to a file type that is allowed
	 * for this wiki instance.
	 *
	 * @param filename The file name.
	 * @return <code>true</code> if the file type has not been blacklisted and is allowed for upload.
	 */
	public static boolean isFileTypeAllowed(String filename) {
		String extension = FilenameUtils.getExtension(filename);
		int blacklistType = Environment.getIntValue(Environment.PROP_FILE_BLACKLIST_TYPE);
		if (blacklistType == WikiBase.UPLOAD_ALL) {
			return true;
		}
		if (blacklistType == WikiBase.UPLOAD_NONE) {
			return false;
		}
		if (StringUtils.isBlank(extension)) {
			// FIXME - should non-extensions be represented in the whitelist/blacklist?
			return true;
		}
		extension = extension.toLowerCase();
		List list = WikiUtil.retrieveUploadFileList();
		if (blacklistType == WikiBase.UPLOAD_BLACKLIST) {
			return !list.contains(extension);
		}
		if (blacklistType == WikiBase.UPLOAD_WHITELIST) {
			return list.contains(extension);
		}
		return false;
	}

	/**
	 * Given a File object, determine if the file is an image or if it is some
	 * other type of file.
	 *
	 * @param file The File object for the file that is being examined.
	 * @return Returns <code>true</code> if the file is an image object.
	 */
	public static boolean isImage(File file) {
		try {
			return (ImageProcessor.retrieveImageDimensions(file) != null);
		} catch (IOException x) {
			return false;
		}
	}

	/**
	 * Given a object name, determine if the object is an image or if it is some
	 * other type of data.
	 *
	 * @param fileId The file identifier that is being examined.
	 * @return Returns <code>true</code> if the object is an image object.
	 */
	public static boolean isImage(int fileId) {
		try {
			return (ImageProcessor.retrieveImageDimensions(fileId, 0) != null);
		} catch (IOException x) {
			return false;
		}
	}

	/**
	 * Determine if image information is available in the cache.  If so return it,
	 * otherwise return <code>null</code>.
	 */
	private static Dimension retrieveFromCache(WikiImage wikiImage) throws DataAccessException {
		String key = wikiImage.getVirtualWiki() + "/" + wikiImage.getUrl();
		Element cachedDimensions = WikiCache.retrieveFromCache(CACHE_IMAGE_DIMENSIONS, key);
		return (cachedDimensions != null) ? (Dimension)cachedDimensions.getObjectValue() : null;
	}

	/**
	 * Given a file name that might correspond to an absolute URL, strip any directories
	 * and convert spaces in the name to underscores.
	 *
	 * @param filename The file name (path) to be sanitized.
	 * @return A sanitized version of the file name.
	 */
	public static String sanitizeFilename(String filename) {
		if (StringUtils.isBlank(filename)) {
			return null;
		}
		// some browsers set the full path, so strip to just the file name
		filename = FilenameUtils.getName(filename);
		filename = StringUtils.replace(filename.trim(), " ", "_");
		return filename;
	}

	/**
	 *
	 */
	public static Topic writeImageTopic(String virtualWiki, String topicName, String contents, WikiUser user, boolean isImage, String ipAddress) throws DataAccessException, ParserException, WikiException {
		Topic topic = WikiBase.getDataHandler().lookupTopic(virtualWiki, topicName, false);
		int charactersChanged = 0;
		if (topic == null || !StringUtils.equals(virtualWiki, topic.getVirtualWiki())) {
			// if topic doesn't exist or the shared version was returned, create a new record
			topic = new Topic(virtualWiki, topicName);
			topic.setTopicContent(contents);
			charactersChanged = StringUtils.length(contents);
		}
		if (isImage) {
			topic.setTopicType(TopicType.IMAGE);
		} else {
			topic.setTopicType(TopicType.FILE);
		}
		TopicVersion topicVersion = new TopicVersion(user, ipAddress, contents, topic.getTopicContent(), charactersChanged);
		topicVersion.setEditType(TopicVersion.EDIT_UPLOAD);
		ParserOutput parserOutput = ParserUtil.parserOutput(topic.getTopicContent(), virtualWiki, topicName);
		WikiBase.getDataHandler().writeTopic(topic, topicVersion, parserOutput.getCategories(), parserOutput.getLinks());
		return topic;
	}

	/**
	 * Add/Update a WikiFile record, and add a WikiFileVersion record.
	 *
	 * @param topic The Topic record corresponding to this WikiFile.
	 * @param wikiFileVersion A skeleton WikiFileVersion record.  Most of the values of this
	 *  record will be populated from other parameters passed to this method, but fields
	 *  such as uploadComment should be populated prior to calling this method.
	 * @param user The user who is creating the file record, or <code>null</code> if the user
	 *  creating the file record is anonymous.
	 * @param ipAddress The IP address of the user creating the file record.
	 * @param filename The path on the filesystem relative to the file upload root for the
	 *  file version being created.
	 * @param url The relative URL for the file version being created.
	 * @param contentType The MIME type of the file version record being created.  For
	 *  example, "image/jpeg".
	 * @param fileSize The size of the file version record in bytes.
	 * @return The new or updated WikiFile record.
	 */
	public static WikiFile writeWikiFile(Topic topic, WikiFileVersion wikiFileVersion, WikiUser user, String ipAddress, String filename, String url, String contentType, long fileSize, ImageData imageData) throws DataAccessException, WikiException {
		wikiFileVersion.setAuthorDisplay(ipAddress);
		Integer authorId = null;
		if (user != null && user.getUserId() > 0) {
			authorId = user.getUserId();
		}
		wikiFileVersion.setAuthorId(authorId);
		WikiFile wikiFile = WikiBase.getDataHandler().lookupWikiFile(topic.getVirtualWiki(), topic.getName());
		if (wikiFile == null || !StringUtils.equals(wikiFile.getVirtualWiki(), topic.getVirtualWiki())) {
			// if file doesn't exist or the shared version was returned, create a new record
			wikiFile = new WikiFile();
			wikiFile.setVirtualWiki(topic.getVirtualWiki());
		}
		wikiFile.setFileName(filename);
		wikiFile.setUrl(url);
		wikiFileVersion.setUrl(url);
		wikiFileVersion.setMimeType(contentType);
		wikiFile.setMimeType(contentType);
		wikiFileVersion.setFileSize(fileSize);
		wikiFile.setFileSize(fileSize);
		wikiFile.setTopicId(topic.getTopicId());
		WikiBase.getDataHandler().writeFile(wikiFile, wikiFileVersion, imageData);
		return wikiFile;
	}

	/**
	 * @return <code>true</code> if images are stored on file system and <code>false</code> if in database.
	 */
	public static boolean isImagesOnFS() {
		String  fileDir  = Environment.getValue(Environment.PROP_FILE_DIR_RELATIVE_PATH);
		return !fileDir.isEmpty();
	}

	/**
	 * FIXME Works only if war file name corresponds to environment properties.
	 *
	 * @return ImageServlet URL.
	 */
	public static String getImageServletUrl() {
		return "/" + Environment.getValue(Environment.PROP_SITE_NAME).toLowerCase() + "-" + Environment.getValue(Environment.PROP_BASE_WIKI_VERSION) + "/" + Environment.getValue(Environment.PROP_VIRTUAL_WIKI_DEFAULT) + "/Special:Image";
	}
}
