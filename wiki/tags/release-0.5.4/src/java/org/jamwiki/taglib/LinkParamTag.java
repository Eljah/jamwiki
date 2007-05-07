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
package org.jamwiki.taglib;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import org.jamwiki.utils.WikiLogger;
import org.springframework.util.StringUtils;
import org.springframework.web.util.ExpressionEvaluationUtils;

/**
 * JSP tag used within {@link org.jamwiki.taglib.LinkTag} tags to add query
 * parameters to the generated URL, performing any required encoding of the
 * query parameters.
 */
public class LinkParamTag extends BodyTagSupport {

	private static WikiLogger logger = WikiLogger.getLogger(LinkParamTag.class.getName());
	private String value = null;
	private String key = null;

	/**
	 *
	 */
	public int doEndTag() throws JspException {
		String tagValue = null;
		// Resin 3.0, 3.1 throws ClassCastException with evaluateString for values like "1", so use tmp variable
		Object tmp = null;
		LinkTag parent = (LinkTag)this.getParent();
		if (parent == null) {
			throw new JspException("linkParam tag not nested within a link tag");
		}
		try {
			if (StringUtils.hasText(this.value)) {
				tmp = ExpressionEvaluationUtils.evaluate("value", this.value, pageContext);
				if (tmp != null) {
					tagValue = tmp.toString();
				}
			} else {
				tagValue = this.getBodyContent().getString();
			}
			parent.addQueryParam(this.key, tagValue);
		} catch (JspException e) {
			logger.severe("Failure in link param tag for " + this.value, e);
			throw e;
		}
		return EVAL_PAGE;
	}

	/**
	 *
	 */
	public String getKey() {
		return this.key;
	}

	/**
	 *
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 *
	 */
	public String getValue() {
		return this.value;
	}

	/**
	 *
	 */
	public void setValue(String value) {
		this.value = value;
	}
}
