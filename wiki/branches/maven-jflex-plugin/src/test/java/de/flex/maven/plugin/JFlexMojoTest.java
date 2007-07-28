/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * JFlex Maven2 plugin                                                     *
 * Copyright (c) 2007       Régis Décamps <decamps@users.sf.net>           *
 * All rights reserved.                                                    *
 *                                                                         *
 * This program is free software; you can redistribute it and/or modify    *
 * it under the terms of the GNU General Public License. See the file      *
 * COPYRIGHT for more information.                                         *
 *                                                                         *
 * This program is distributed in the hope that it will be useful,         *
 * but WITHOUT ANY WARRANTY; without even the implied warranty of          *
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the           *
 * GNU General Public License for more details.                            *
 *                                                                         *
 * You should have received a copy of the GNU General Public License along *
 * with this program; if not, write to the Free Software Foundation, Inc., *
 * 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA                 *
 *                                                                         *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package de.flex.maven.plugin;

import java.io.File;
import java.io.IOException;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

import junit.framework.TestCase;

public class JFlexMojoTest extends TestCase {
	protected static final String SRC_TEST_RESOURCES_FLEX = "src/test/resources/preprocessor.jflex";
	protected static final String EXPECTED_PACKAGE_DIR = "/org/jamwiki/parser/jflex/";
	protected static final String EXPECTED_CLASS_NAME = "JAMWikiPreProcessor";
	protected static final String OUTPUT_DIRECTORY = "target/test/generated";
	
	JFlexMojo mojo;
	Parser preprocessorLex;

	@Override
	protected void setUp() throws Exception {
		mojo = new JFlexMojo();
		mojo.setOutputDirectory(new File(OUTPUT_DIRECTORY));
		// in a standard execution, the Mojo sets this:
		preprocessorLex = new Parser(SRC_TEST_RESOURCES_FLEX,
				OUTPUT_DIRECTORY);
	}

	public void testInit() {
		assertEquals(OUTPUT_DIRECTORY, mojo.getOutputDirectory().getPath());
	}

	/**
	 * Test the full parser generation.
	 * 
	 * This is an integration rather than a unit-test, and is extremely useful.
	 */
	public void testGenerate() throws MojoExecutionException,
			MojoFailureException {
		Parser[] lexFiles = { preprocessorLex };
		mojo.setLexFiles(lexFiles);

		mojo.execute();

		// approximative checking... Can't use md5 as the generated file
		// contains the date.
		File produced = new File(OUTPUT_DIRECTORY
				+ EXPECTED_PACKAGE_DIR + EXPECTED_CLASS_NAME + ".java");
	
		assertTrue("produced file is a file: " + produced.getAbsolutePath(),
				produced.isFile());
		long size = produced.length();
		boolean correctSize = (size > 26000) && (size < 28000);
		assertTrue("size of produced file between 26k and 28k. Actual is "
				+ size, correctSize);
		produced.delete();
	}
	
	public void testGuessDirectory() {
		
	}
}
