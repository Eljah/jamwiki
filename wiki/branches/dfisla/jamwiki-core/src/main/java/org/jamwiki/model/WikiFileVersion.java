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
import java.text.SimpleDateFormat;
import org.apache.commons.lang.StringUtils;
import org.jamwiki.WikiException;
import org.jamwiki.WikiMessage;

/**
 * Provides an object representing a version of a file uploaded to the Wiki.
 */
public class WikiFileVersion {

    private Integer authorId = null;
    private String authorDisplay = null;
    private int fileId = -1;
    private long fileSize = -1;
    private int fileVersionId = -1;
    private String mimeType = WikiFile.UNKNOWN_MIME_TYPE;
    private String uploadComment = null;
    private Timestamp uploadDate = new Timestamp(System.currentTimeMillis()); //2009-11-28 00:31:39.82
    private String url = null;

    /**
     *
     */
    public WikiFileVersion() {
    }

    public WikiFileVersion(String data) throws WikiException {

        try {
            if ((data != null) && (data.indexOf("|") != -1)) {

                String[] parts = data.split("\\|");
                if ((parts != null) && (parts.length == 9)) {
                    authorId = Integer.parseInt(parts[0]);
                    authorDisplay = parts[1];
                    fileId = Integer.parseInt(parts[2]);
                    fileSize = Long.parseLong(parts[3]);
                    fileVersionId = Integer.parseInt(parts[4]);
                    mimeType = parts[5];
                    uploadComment = parts[6];

                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SS");
                    java.util.Date parsedDate = dateFormat.parse(parts[7]);
                    uploadDate = new Timestamp(parsedDate.getTime());

                    url = parts[8];
                }
            }
        } catch (Exception ex) {
            throw new WikiException(new WikiMessage("Could not parse object from data."), ex);
        }



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
    public String getAuthorDisplay() {
        return this.authorDisplay;
    }

    /**
     *
     */
    public void setAuthorDisplay(String authorDisplay) {
        this.authorDisplay = authorDisplay;
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
    public long getFileSize() {
        return this.fileSize;
    }

    /**
     *
     */
    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    /**
     *
     */
    public int getFileVersionId() {
        return this.fileVersionId;
    }

    /**
     *
     */
    public void setFileVersionId(int fileVersionId) {
        this.fileVersionId = fileVersionId;
    }

    /**
     * This method will either return the MIME type set for the file, or a default
     * MIME type indicating that the MIME type is unknown.  This method will never
     * return <code>null</code>.
     */
    public String getMimeType() {
        return (StringUtils.isBlank(this.mimeType)) ? WikiFile.UNKNOWN_MIME_TYPE : this.mimeType;
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
    public String getUploadComment() {
        return this.uploadComment;
    }

    /**
     *
     */
    public void setUploadComment(String uploadComment) {
        this.uploadComment = uploadComment;
    }

    /**
     *
     */
    public Timestamp getUploadDate() {
        return this.uploadDate;
    }

    /**
     *
     */
    public void setUploadDate(Timestamp uploadDate) {
        this.uploadDate = uploadDate;
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

    @Override
    public String toString() {

        return String.format("%d|%s|%d|%d|%d|%s|%s|%s|%s",
                authorId,
                authorDisplay,
                fileId,
                fileSize,
                fileVersionId,
                mimeType,
                uploadComment,
                uploadDate.toString(),
                url);

    }
}
