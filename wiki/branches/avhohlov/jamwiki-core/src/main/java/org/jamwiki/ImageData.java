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
package org.jamwiki;

/**
 * Class for image and other data storing and loading.
 */
public class ImageData {
	public int      fileVersionId;

	public String	mimeType;

	public int	width;

	public int	height;

	public byte	data[];

	public ImageData(int fileVersionId, String mimeType, int width, int height, byte data[]) {
		this.fileVersionId = fileVersionId;
		this.mimeType      = mimeType;
		this.width         = width;
		this.height        = height;
		this.data          = data;
	}

	public ImageData(String mimeType, int width, int height, byte data[]) {
		this(-1, mimeType, width, height, data);
	}
}
