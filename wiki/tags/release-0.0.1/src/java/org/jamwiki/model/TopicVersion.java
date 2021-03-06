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
 * along with this program (gpl.txt); if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package org.jamwiki.model;

import java.sql.Timestamp;
import org.apache.log4j.Logger;

/**
 *
 */
public class TopicVersion {

	public static final int EDIT_NORMAL = 1;
	public static final int EDIT_MINOR = 2;
	public static final int EDIT_REVERT = 3;
	public static final int EDIT_MOVE = 4;
	// FIXME - change back to -1 once author handling works
	private int authorId = 1;
	private String authorIpAddress = null;
	private String editComment = null;
	private Timestamp editDate = new Timestamp(System.currentTimeMillis());
	private int editType = EDIT_NORMAL;
	private int topicId = -1;
	private int topicVersionId = -1;
	private String versionContent = null;
	private static Logger logger = Logger.getLogger(TopicVersion.class);

	/**
	 *
	 */
	public TopicVersion() {
	}

	/**
	 *
	 */
	public int getAuthorId() {
		return this.authorId;
	}

	/**
	 *
	 */
	public void setAuthorId(int authorId) {
		this.authorId = authorId;
	}

	/**
	 *
	 */
	public String getAuthorIpAddress() {
		return this.authorIpAddress;
	}

	/**
	 *
	 */
	public void setAuthorIpAddress(String authorIpAddress) {
		this.authorIpAddress = authorIpAddress;
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
	public String getVersionContent() {
		return this.versionContent;
	}

	/**
	 *
	 */
	public void setVersionContent(String versionContent) {
		this.versionContent = versionContent;
	}
}
