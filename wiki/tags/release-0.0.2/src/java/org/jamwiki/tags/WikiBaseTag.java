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
package org.jamwiki.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import org.jamwiki.WikiBase;

/**
 *
 */
public class WikiBaseTag extends TagSupport {

	private String var;

	/**
	 *
	 */
	public int doEndTag() throws JspException {
		try {
			this.pageContext.setAttribute(var, WikiBase.getInstance());
		} catch (Exception e) {
			throw new JspException("Getting WikiBase", e);
		}
		return SKIP_BODY;
	}

	/**
	 *
	 */
	public String getVar() {
		return var;
	}

	/**
	 *
	 */
	public void setVar(String var) {
		this.var = var;
	}
}
