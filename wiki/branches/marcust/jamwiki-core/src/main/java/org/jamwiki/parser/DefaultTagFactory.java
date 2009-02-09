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
package org.jamwiki.parser;

import org.jamwiki.parser.jflex.HtmlLinkTag;
import org.jamwiki.parser.jflex.IncludeOnlyTag;
import org.jamwiki.parser.jflex.NoIncludeTag;
import org.jamwiki.parser.jflex.TemplateTag;
import org.jamwiki.parser.jflex.WikiHeadingTag;
import org.jamwiki.parser.jflex.WikiLinkTag;
import org.jamwiki.parser.jflex.WikiReferenceTag;
import org.jamwiki.parser.jflex.WikiReferencesTag;
import org.jamwiki.parser.jflex.WikiSignatureTag;

public class DefaultTagFactory implements TagFactory {

	public WikiReferencesTag newWikiReferencesTag() {
		return new WikiReferencesTag();
	}

	public HtmlLinkTag newHtmlLinkTag() {
		return new HtmlLinkTag();
	}

	public IncludeOnlyTag newIncludeOnlyTag() {
		return new IncludeOnlyTag();
	}

	public NoIncludeTag newNoIncludeTag() {
		return new NoIncludeTag();
	}

	public TemplateTag newTemplateTag() {
		return new TemplateTag();
	}

	public WikiHeadingTag newWikiHeadingTag() {
		return new WikiHeadingTag();
	}

	public WikiLinkTag newWikiLinkTag() {
		return new WikiLinkTag();
	}

	public WikiReferenceTag newWikiReferenceTag() {
		return new WikiReferenceTag();

	}

	public WikiSignatureTag newWikiSignatureTag() {
		return new WikiSignatureTag();
	}

}
