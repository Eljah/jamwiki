/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2001 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *	notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *	notice, this list of conditions and the following disclaimer in
 *	the documentation and/or other materials provided with the
 *	distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *	if any, must include the following acknowledgment:
 *	   "This product includes software developed by the
 *		Apache Software Foundation (http://www.apache.org/)."
 *	Alternately, this acknowledgment may appear in the software itself,
 *	if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Apache" and "Apache Software Foundation" and
 *	"Apache Lucene" must not be used to endorse or promote products
 *	derived from this software without prior written permission. For
 *	written permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache",
 *	"Apache Lucene", nor may "Apache" appear in their name, without
 *	prior written permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 * Original from: lucene source code package
 * Changes as described in http://www.geocrawler.com/archives/3/2624/2000/11/0/4746798/
 */

package org.jamwiki.search.lucene;

import java.io.Reader;
import org.apache.lucene.analysis.LetterTokenizer;

/**
 * LowerCaseKeepNumbersTokenizer performs the function of LetterTokenizer
 * and LowerCaseFilter together.  It divides text at non-letters and converts
 * them to lower case.  While it is functionally equivalent to the combination
 * of LetterTokenizer and LowerCaseFilter, there is a performance advantage
 * to doing the two tasks at once, hence this (redundant) implementation.
 * <P>
 * Note: this does a decent job for most European languages, but does a terrible
 * job for some Asian languages, where words are not separated by spaces.
 */
public final class LowerCaseKeepNumbersTokenizer extends LetterTokenizer {

	/**
	 * Construct a new LowerCaseKeepNumbersTokenizer.
	 */
	public LowerCaseKeepNumbersTokenizer(Reader in) {
		super(in);
	}

	/** Collects only characters which satisfy
	 * {@link Character#isLetter(char)}.
	 */
	protected char normalize(char c) {
		return Character.toLowerCase(c);
	}

	/**
	 * Collects only characters which satisfy
	 * {@link Character#isLetter(char)}.
	 */
	protected boolean isTokenChar(char c) {
		return Character.isLetterOrDigit(c);
	}
}
