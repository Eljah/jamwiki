package de.flex.maven.plugin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import junit.framework.TestCase;

public class ParserTest extends TestCase {

	public void testGetOutputFile() throws FileNotFoundException, IOException {
		Parser p = new Parser(JFlexMojoTest.SRC_TEST_RESOURCES_FLEX,
				JFlexMojoTest.OUTPUT_DIRECTORY);
		File expected = new File(JFlexMojoTest.OUTPUT_DIRECTORY
				+ JFlexMojoTest.EXPECTED_PACKAGE_DIR
				+ JFlexMojoTest.EXPECTED_CLASS_NAME + ".java");
		assertEquals("guessed output filename", expected.getAbsolutePath(), p
				.getOutputFile().getAbsolutePath());
	}

}
