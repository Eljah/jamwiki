package org.jamwiki.parser;

public interface TableOfContents {

	/** Status indicating that this TOC object has not yet been initialized.  For the JFlex parser this will mean no __TOC__ tag has been added to the document being parsed. */
	public static final int STATUS_TOC_UNINITIALIZED = 0;
	/** Status indicating that this TOC object has been initialized.  For the JFlex parser this will mean a __TOC__ tag has been added to the document being parsed. */
	public static final int STATUS_TOC_INITIALIZED = 1;
	/** Status indicating that the document being parsed does not allow a table of contents. */
	public static final int STATUS_NO_TOC = 2;

	/**
	 * Add a new table of contents entry to the table of contents object.
	 * The entry should contain the name to use in the HTML anchor tag,
	 * the text to display in the table of contents, and the indentation
	 * level for the entry within the table of contents.
	 *
	 * @param name The name of the entry, to be used in the anchor tag name.
	 * @param text The text to display for the table of contents entry.
	 * @param level The level of the entry.  If an entry is a sub-heading of
	 *  another entry the value should be 2.  If there is a sub-heading of that
	 *  entry then its value would be 3, and so forth.
	 */
	public abstract void addEntry(String name, String text, int level);

	/**
	 * This method checks to see if a TOC is allowed to be inserted, and if so
	 * returns an HTML representation of the TOC.
	 *
	 * @return An HTML representation of the current table of contents object,
	 *  or an empty string if the table of contents can not be inserted due
	 *  to an inadequate number of entries or some other reason.
	 */
	public abstract String attemptTOCInsertion();

	/**
	 * Return the current table of contents status, such as "no table of contents
	 * allowed" or "uninitialized".
	 *
	 * @return The current status of this table of contents object.
	 */
	public abstract int getStatus();

	/**
	 * Force a TOC to appear, even if there are fewer than four headings.
	 *
	 * @param forceTOC Set to <code>true</code> if a TOC is being forced
	 *  to appear, false otherwise.
	 */
	public abstract void setForceTOC(boolean forceTOC);

	/**
	 * Set the current table of contents status, such as "no table of contents
	 * allowed" or "uninitialized".
	 *
	 * @param status The current status of this table of contents object.
	 */
	public abstract void setStatus(int status);

	/**
	 * Return the number of entries in this TOC object.
	 *
	 * @return The number of entries in this table of contents object.
	 */
	public abstract int size();

	/**
	 * Return an HTML representation of this table of contents object.
	 *
	 * @return An HTML representation of this table of contents object.
	 */
	public abstract String toHTML();

}