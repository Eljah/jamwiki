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
package org.jamwiki.model;

import java.io.Serializable;
import java.sql.Timestamp;
import org.apache.log4j.Logger;

/**
 * Provides an object representing a version of a Wiki topic.
 */
public class TopicVersion implements Serializable {

    public static final int EDIT_NORMAL = 1;
    public static final int EDIT_MINOR = 2;
    public static final int EDIT_REVERT = 3;
    public static final int EDIT_MOVE = 4;
    public static final int EDIT_DELETE = 5;
    public static final int EDIT_PERMISSION = 6;
    public static final int EDIT_UNDELETE = 7;
    public static final int EDIT_IMPORT = 8;

    private Integer authorId = null;
    private String authorDisplay = null;
    private int charactersChanged = 0;
    private String editComment = null;
    private Timestamp editDate = new Timestamp(System.currentTimeMillis());
    private int editType = EDIT_NORMAL;
    private Integer previousTopicVersionId = null;
    private int topicId = -1;
    private int topicVersionId = -1;
    private String versionContent = null;
    private String versionContentClean = null;
    private String versionContentShort = null;
    private int bzType = 0;
    private static final Logger logger = Logger.getLogger(TopicVersion.class.getName());

    /**
     *
     */
    public TopicVersion() {
    }

    /**
     *
     * @param user 
     * @param editComment
     * @param authorDisplay
     * @param versionContent
     * @param charactersChanged
     */
    public TopicVersion(WikiUser user, String authorDisplay, String editComment, String versionContent, int charactersChanged) {
        if (user != null && user.getUserId() > 0) {
            this.authorId = user.getUserId();
        }
        this.authorDisplay = authorDisplay;
        this.editComment = editComment;
        this.versionContent = versionContent;
        this.charactersChanged = charactersChanged;
    }

    /**
     *
     * @return
     */
    public Integer getAuthorId() {
        return this.authorId;
    }

    /**
     *
     * @param authorId
     */
    public void setAuthorId(Integer authorId) {
        this.authorId = authorId;
    }

    /**
     *
     * @return
     */
    public String getAuthorDisplay() {
        return this.authorDisplay;
    }

    /**
     *
     * @param authorDisplay
     */
    public void setAuthorDisplay(String authorDisplay) {
        this.authorDisplay = authorDisplay;
    }

    /**
     *
     * @return
     */
    public int getCharactersChanged() {
        return this.charactersChanged;
    }

    /**
     *
     * @param charactersChanged
     */
    public void setCharactersChanged(int charactersChanged) {
        this.charactersChanged = charactersChanged;
    }

    /**
     *
     * @return
     */
    public String getEditComment() {
        return this.editComment;
    }

    /**
     *
     * @param editComment
     */
    public void setEditComment(String editComment) {
        this.editComment = editComment;
    }

    /**
     *
     * @return
     */
    public Timestamp getEditDate() {
        return this.editDate;
    }

    /**
     *
     * @param editDate
     */
    public void setEditDate(Timestamp editDate) {
        this.editDate = editDate;
    }

    /**
     *
     * @return
     */
    public int getEditType() {
        return this.editType;
    }

    /**
     *
     * @param editType
     */
    public void setEditType(int editType) {
        this.editType = editType;
    }

    /**
     *
     * @return
     */
    public Integer getPreviousTopicVersionId() {
        return this.previousTopicVersionId;
    }

    /**
     *
     * @param previousTopicVersionId
     */
    public void setPreviousTopicVersionId(Integer previousTopicVersionId) {
        this.previousTopicVersionId = previousTopicVersionId;
    }

    /**
     *
     * @return
     */
    public int getTopicId() {
        return this.topicId;
    }

    /**
     *
     * @param topicId
     */
    public void setTopicId(int topicId) {
        this.topicId = topicId;
    }

    /**
     *
     * @return
     */
    public int getTopicVersionId() {
        return this.topicVersionId;
    }

    /**
     *
     * @param topicVersionId
     */
    public void setTopicVersionId(int topicVersionId) {
        this.topicVersionId = topicVersionId;
    }

    /**
     *
     * @return
     */
    public String getVersionContent() {
        return this.versionContent;
    }

    /**
     *
     * @param versionContent
     */
    public void setVersionContent(String versionContent) {
        this.versionContent = versionContent;
    }

    /**
     *
     * @return
     */
    public String getVersionContentClean() {
        return versionContentClean;
    }

    /**
     *
     * @param versionContentClean
     */
    public void setVersionContentClean(String versionContentClean) {
        this.versionContentClean = versionContentClean;
    }

    /**
     *
     * @return
     */
    public String getVersionContentShort() {
        return versionContentShort;
    }

    /**
     *
     * @param versionContentShort
     */
    public void setVersionContentShort(String versionContentShort) {
        this.versionContentShort = versionContentShort;
    }

    /**
     *
     * @return
     */
    public int getBzType() {
        return bzType;
    }

    /**
     *
     * @param bzType
     */
    public void setBzType(int bzType) {
        this.bzType = bzType;
    }
}
