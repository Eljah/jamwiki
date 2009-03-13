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

import java.sql.Timestamp;
import org.jamwiki.utils.WikiLogger;

/**
 * Provides an object representing a Wiki recent change.
 */
public class RecentChange {

	private static final WikiLogger logger = WikiLogger.getLogger(RecentChange.class.getName());
	private Integer authorId = null;
	private String authorName = null;
	private int charactersChanged = 0;
	private String editComment = null;
	private Timestamp editDate = null;
	private int editType = -1;
	private Integer previousTopicVersionId = null;
	private int topicId = -1;
	private String topicName = null;
	private int topicVersionId = -1;
	private String virtualWiki = null;

	/**
	 *
	 */
	public RecentChange() {
	}

	/**
	 *
	 */
	public RecentChange(Topic topic, TopicVersion topicVersion, String authorName) {
		this.topicId = topic.getTopicId();
		this.topicName = topic.getName();
		this.topicVersionId = topicVersion.getTopicVersionId();
		this.previousTopicVersionId = topicVersion.getPreviousTopicVersionId();
		this.authorId = topicVersion.getAuthorId();
		this.authorName = authorName;
		this.charactersChanged = topicVersion.getCharactersChanged();
		this.editComment = topicVersion.getEditComment();
		this.editDate = topicVersion.getEditDate();
		this.editType = topicVersion.getEditType();
		this.virtualWiki = topic.getVirtualWiki();
	}

	/**
	 *
	 */
	public Integer getAuthorId() {
		return this.authorId;
	}

	/**
	 *
	 */
	public void setAuthorId(Integer authorId) {
		this.authorId = authorId;
	}

	/**
	 *
	 */
	public String getAuthorName() {
		return this.authorName;
	}

	/**
	 *
	 */
	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}

	/**
	 *
	 */
	public String getChangeTypeNotification() {
		StringBuffer changeTypeNotification = new StringBuffer();
		if (this.previousTopicVersionId == null) {
			changeTypeNotification.append('n');
		}
		if (this.editType == TopicVersion.EDIT_MINOR) {
			changeTypeNotification.append('m');
		}
		if (this.editType == TopicVersion.EDIT_DELETE) {
			changeTypeNotification.append('d');
		}
		if (this.editType == TopicVersion.EDIT_UNDELETE) {
			changeTypeNotification.append('u');
		}
		return changeTypeNotification.toString();
	}

	/**
	 *
	 */
	public int getCharactersChanged() {
		return this.charactersChanged;
	}

	/**
	 *
	 */
	public void setCharactersChanged(int charactersChanged) {
		this.charactersChanged = charactersChanged;
	}

	/**
	 *
	 */
	public boolean getDelete() {
		return this.editType == TopicVersion.EDIT_DELETE;
	}

	/**
	 *
	 */
	public String getEditComment() {
		return this.editComment;
	}

	/**
	 *
	 */
	public void setEditComment(String editComment) {
		this.editComment = editComment;
	}

	/**
	 *
	 */
	public Timestamp getEditDate() {
		return this.editDate;
	}

	/**
	 *
	 */
	public void setEditDate(Timestamp editDate) {
		this.editDate = editDate;
	}

	/**
	 *
	 */
	public int getEditType() {
		return this.editType;
	}

	/**
	 *
	 */
	public void setEditType(int editType) {
		this.editType = editType;
	}

	/**
	 *
	 */
	public boolean getMinor() {
		return this.editType == TopicVersion.EDIT_MINOR;
	}

	/**
	 *
	 */
	public boolean getMove() {
		return this.editType == TopicVersion.EDIT_MOVE;
	}

	/**
	 *
	 */
	public boolean getNormal() {
		return this.editType == TopicVersion.EDIT_NORMAL;
	}

	/**
	 *
	 */
	public Integer getPreviousTopicVersionId() {
		return this.previousTopicVersionId;
	}

	/**
	 *
	 */
	public void setPreviousTopicVersionId(Integer previousTopicVersionId) {
		this.previousTopicVersionId = previousTopicVersionId;
	}

	/**
	 *
	 */
	public int getTopicId() {
		return this.topicId;
	}

	/**
	 *
	 */
	public void setTopicId(int topicId) {
		this.topicId = topicId;
	}

	/**
	 *
	 */
	public String getTopicName() {
		return this.topicName;
	}

	/**
	 *
	 */
	public void setTopicName(String topicName) {
		this.topicName = topicName;
	}

	/**
	 *
	 */
	public int getTopicVersionId() {
		return this.topicVersionId;
	}

	/**
	 *
	 */
	public void setTopicVersionId(int topicVersionId) {
		this.topicVersionId = topicVersionId;
	}

	/**
	 *
	 */
	public boolean getUndelete() {
		return this.editType == TopicVersion.EDIT_UNDELETE;
	}

	/**
	 *
	 */
	public String getVirtualWiki() {
		return this.virtualWiki;
	}

	/**
	 *
	 */
	public void setVirtualWiki(String virtualWiki) {
		this.virtualWiki = virtualWiki;
	}
}
