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

import org.apache.log4j.Logger;

/**
 *
 */
public class WikiFile {

	// FIXME - consider making this an ACL (more flexible)
	private boolean adminOnly = false;
	private boolean deleted = false;
	private int fileId = -1;
	private String fileName = null;
	private String mimeType = null;
	private boolean readOnly = false;
	private String url = null;
	private int topicId = -1;
	private String virtualWiki = null;
	private static Logger logger = Logger.getLogger(WikiFile.class);

	/**
	 *
	 */
	public WikiFile() {
	}

	/**
	 *
	 */
	public boolean getAdminOnly() {
		return this.adminOnly;
	}

	/**
	 *
	 */
	public void setAdminOnly(boolean adminOnly) {
		this.adminOnly = adminOnly;
	}

	/**
	 *
	 */
	public boolean getDeleted() {
		return this.deleted;
	}

	/**
	 *
	 */
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	/**
	 *
	 */
	public int getFileId() {
		return this.fileId;
	}

	/**
	 *
	 */
	public void setFileId(int fileId) {
		this.fileId = fileId;
	}

	/**
	 *
	 */
	public String getFileName() {
		return this.fileName;
	}

	/**
	 *
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 *
	 */
	public String getMimeType() {
		return this.mimeType;
	}

	/**
	 *
	 */
	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	/**
	 *
	 */
	public boolean getReadOnly() {
		return this.readOnly;
	}

	/**
	 *
	 */
	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
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
	public String getUrl() {
		return this.url;
	}

	/**
	 *
	 */
	public void setUrl(String url) {
		this.url = url;
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