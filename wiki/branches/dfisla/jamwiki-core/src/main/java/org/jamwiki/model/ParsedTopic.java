/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jamwiki.model;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import org.jamwiki.WikiException;
import org.jamwiki.WikiMessage;
import org.jamwiki.utils.WikiLogger;

/**
 *
 * @author dfisla
 */
public class ParsedTopic extends Topic {

    private static final WikiLogger logger = WikiLogger.getLogger(ParsedTopic.class.getName());
    public int num;
    public int offset;
    public boolean topicImage;
    public boolean topicFile;
    public String categoryName;
    public LinkedHashMap<String, String> categories;
    public LinkedHashMap<String, String> subCategories = new LinkedHashMap<String, String>();
    public List<Category> categoryTopics = new ArrayList<Category>(); // DONE
    public List<Category> categoryImages = new ArrayList<Category>(); // DONE
    public List<WikiFileVersion> fileVersions = new ArrayList<WikiFileVersion>(); // DONE

    public ParsedTopic() {
        super();
    }

    public void initialize(Topic topic) {

        this.adminOnly = topic.adminOnly;
        this.currentVersionId = topic.currentVersionId;
        this.deleteDate = topic.deleteDate;

        this.name = topic.name;
        this.readOnly = topic.readOnly;
        this.redirectTo = topic.redirectTo;
        this.topicId = topic.topicId;
        this.topicType = topic.topicType;
        this.virtualWiki = topic.virtualWiki;

        this.topicContent = topic.topicContent;
        this.topicContentClean = topic.topicContentClean;
        this.topicContentShort = topic.topicContentShort;
    }

    /*
    <META>false|4861833|null|StartingPoints|false|null|1|1|en|0|0|false|false|null
    <PARSEDCONTENT>null
     */

    /*
    <META>false|4861798|null|Anarchism|false|null|6|1|en|0|0|false|false|null
    <CAT>Category:Anarchism=Anarchism
    <CAT>Category:Political ideologies=Political ideologies
    <CAT>Category:Social philosophy=Social philosophy
    <CAT>Category:Political culture=Political culture
    <PARSEDCONTENT>null
     */
    public ParsedTopic(String data) throws WikiException {

        //logger.debug("DEBUG-DATA:" + data);

        boolean parsedContentSection = false;

        try {
            String[] lines = data.split("\n");
            StringBuffer parsedTopicContentBuffer = new StringBuffer();

            this.categories = new LinkedHashMap<String, String>();

            for (int x = 0; x < lines.length; x++) {
                String line = lines[x];
                //logger.debug("LINE: " + line);

                if ((line != null) && (line.startsWith("<META>"))) {
                    line = line.substring(6);

                    String metaParts[] = line.split("\\|");

                    this.adminOnly = Boolean.getBoolean(metaParts[0]);
                    this.currentVersionId = Integer.parseInt(metaParts[1]);

                    if (!metaParts[2].equals("null")) {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SS");
                        java.util.Date parsedDate = dateFormat.parse(metaParts[2]);
                        this.deleteDate = new Timestamp(parsedDate.getTime());
                    } else {
                        this.deleteDate = null;
                    }

                    this.name = metaParts[3];
                    this.readOnly = Boolean.getBoolean(metaParts[4]);

                    if (!metaParts[5].equals("null")) {
                        this.redirectTo = metaParts[5];
                    } else {
                        this.redirectTo = null;
                    }
                    this.topicId = Integer.parseInt(metaParts[6]);
                    this.topicType = Integer.parseInt(metaParts[7]);
                    this.virtualWiki = metaParts[8];

                    this.num = Integer.parseInt(metaParts[9]);
                    this.offset = Integer.parseInt(metaParts[10]);
                    this.topicImage = Boolean.getBoolean(metaParts[11]);
                    this.topicFile = Boolean.getBoolean(metaParts[12]);

                    if (!metaParts[13].equals("null")) {
                        this.categoryName = metaParts[13];
                    } else {
                        this.categoryName = null;
                    }

                } else if ((line != null) && (line.startsWith("<CAT>"))) {
                    line = line.substring(5);

                    String categoryParts[] = line.split("=");
                    String key = categoryParts[0];
                    String value = categoryParts[1];
                    categories.put(key, value);
                } else if ((line != null) && (line.startsWith("<SUBCAT>"))) {
                    line = line.substring(8);

                    String categoryParts[] = line.split("=");
                    String key = categoryParts[0];
                    String value = categoryParts[1];
                    subCategories.put(key, value);
                } else if ((line != null) && (line.startsWith("<CATTOPIC>"))) {
                    line = line.substring(10);
                    categoryTopics.add(new Category(line));
                } else if ((line != null) && (line.startsWith("<CATIMAGE>"))) {
                    line = line.substring(10);
                    categoryImages.add(new Category(line));
                } else if ((line != null) && (line.startsWith("<FILEVERSION>"))) {
                    line = line.substring(13);
                    fileVersions.add(new WikiFileVersion(line));
                } else if ((line != null) && (line.startsWith("<PARSEDCONTENT>"))) {
                    parsedContentSection = true;
                    line = line.replace("<PARSEDCONTENT>", "");
                    //logger.debug("CONTENT-BUFFER: " + line);
                    parsedTopicContentBuffer.append(line);
                } else if (parsedContentSection) {
                    //logger.debug("CONTENT-BUFFER: " + line);
                    parsedTopicContentBuffer.append(line);
                } else {
                    logger.severe("UNKNOWN-PARSEDTOPIC-LINE: " + line);
                }
            }

            this.topicContent = parsedTopicContentBuffer.toString();

        } catch (Exception ex) {
            logger.fine(ex.getMessage(), ex);
            throw new WikiException(new WikiMessage("Could not parse object from data."), ex);
        }

    }

    /*
    <META>false|4861833|null|StartingPoints|false|null|1|1|en|0|0|false|false|null
    <PARSEDCONTENT>null
     */

    /*
    <META>false|4861798|null|Anarchism|false|null|6|1|en|0|0|false|false|null
    <CAT>Category:Anarchism=Anarchism
    <CAT>Category:Political ideologies=Political ideologies
    <CAT>Category:Social philosophy=Social philosophy
    <CAT>Category:Political culture=Political culture
    <PARSEDCONTENT>null
     */
    @Override
    public String toString() {

        StringBuffer sb = new StringBuffer();
        sb.append("<META>");

        sb.append(this.adminOnly);
        sb.append("|");
        sb.append(this.currentVersionId);
        sb.append("|");
        sb.append(this.deleteDate);
        sb.append("|");
        sb.append(this.name);
        sb.append("|");
        sb.append(this.readOnly);
        sb.append("|");
        sb.append(this.redirectTo);
        sb.append("|");
        sb.append(this.topicId);
        sb.append("|");
        sb.append(this.topicType);
        sb.append("|");
        sb.append(this.virtualWiki);
        sb.append("|");
        sb.append(num);
        sb.append("|");
        sb.append(offset);
        sb.append("|");
        sb.append(topicImage);
        sb.append("|");
        sb.append(topicFile);
        sb.append("|");
        sb.append(categoryName);
        sb.append("\n");

        if ((categories != null) && (categories.size() > 0)) {
            for (String key : categories.keySet()) {
                sb.append(String.format("<CAT>%s=%s", key, (String) categories.get(key)));
                sb.append("\n");
            }
        }

        if ((subCategories != null) && (subCategories.size() > 0)) {
            for (String key : subCategories.keySet()) {
                sb.append(String.format("<SUBCAT>%s=%s", key, (String) categories.get(key)));
                sb.append("\n");
            }
        }

        if ((categoryTopics != null) && (categoryTopics.size() > 0)) {
            for (Category catTopic : categoryTopics) {
                sb.append("<CATTOPIC>");
                sb.append(catTopic.toString());
                sb.append("\n");
            }
        }

        if ((categoryImages != null) && (categoryImages.size() > 0)) {
            for (Category catImage : categoryImages) {
                sb.append("<CATIMAGE>");
                sb.append(catImage.toString());
                sb.append("\n");
            }
        }

        if ((fileVersions != null) && (fileVersions.size() > 0)) {
            for (WikiFileVersion fileVersion : fileVersions) {
                sb.append("<FILEVERSION>");
                sb.append(fileVersion.toString());
                sb.append("\n");
            }
        }

        if ((this.topicContent != null) && (this.topicContent.length() > 0)) {
            sb.append("<PARSEDCONTENT>");
            sb.append("\n");
            sb.append(this.topicContent);
        }

        return sb.toString();
    }
}
