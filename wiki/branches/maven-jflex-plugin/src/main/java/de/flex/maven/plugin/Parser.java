/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * JFlex Maven2 plugin                                                     *
 * Copyright (c) 2007       Régis Décamps <decamps@users.sf.net>           *
 * All rights reserved.   
 * Based on the Jflex ant task                                             *
 * Copyright (C) 2001       Rafal Mantiuk <Rafal.Mantiuk@bellstream.pl>    *
 * Copyright (C) 2003       changes by Gerwin Klein <lsf@jflex.de>         *
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
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;

import org.apache.maven.plugin.logging.Log;

/**
 * Parameter of the maven-jflex-plugin mojo.
 * 
 */
public class Parser {
	private static final long serialVersionUID = -1022869916489077448L;
	public static final String DEFAULT_NAME = "Yylex";
	/**
	 * The lexer definition the parser will be generated from.
	 */
	public File lexFile;

	/**
	 * The java parser code to generate. Defaults to
	 * <code>${outputDirectory}/package/name/classname.java</code>
	 */
	public File outputFile;

	/**
	 * Output directory. Set by mojo to
	 * <code>target/generated-sources/jflex</code>
	 */
	private File outputDirectory;

	public Parser(String lexFilename, String targetDirectoryName) {
		this.lexFile = new File(lexFilename);
		this.outputDirectory = new File(targetDirectoryName);
	}

	public void setOutputDirectory(File outputDirectory) {
		this.outputDirectory = outputDirectory;
	}

	public File getLexFile() {
		return lexFile;
	}

	/**
	 * Return the output file name for this lexer definition. In that case the
	 * outputdirectory from the mojo configuration is ignored.
	 * 
	 * If this parameter is not defined in the configuration, it will guess a
	 * probable location from the lexer definition file:
	 * <code>${outputDirectory}/package/name/classname.java</code>
	 * 
	 * 
	 * @return The name of the java code to generate.
	 * 
	 * Credit goes to the authors of the maven-jlex-plugin
	 * 
	 * 
	 * @throws IOException
	 * @author Rafal Mantiuk (Rafal.Mantiuk@bellstream.pl)
	 * @author Gerwin Klein (lsf@jflex.de)
	 * @throws FileNotFoundException
	 *             if the lex file does not exist
	 * 
	 */
	public File getOutputFile() throws FileNotFoundException, IOException {
		if (outputFile != null)
			return outputFile;

		LineNumberReader reader = new LineNumberReader(new FileReader(lexFile));

		String packageName = null;
		String className = null;

		// TODO Reuse code from parser if it exists.
		while (className == null || packageName == null) {
			String line = reader.readLine();
			if (line == null)
				break;

			if (packageName == null) {
				int index = line.indexOf("package");
				if (index >= 0) {
					index += 7;

					int end = line.indexOf(';', index);
					if (end >= index) {
						packageName = line.substring(index, end);
						packageName = packageName.trim();
					}
				}
			}

			if (className == null) {
				int index = line.indexOf("%class");
				if (index >= 0) {
					index += 6;

					className = line.substring(index);
					className = className.trim();
				}
			}
		}

		// package name may be null, but class name not
		if (className == null) {
			// log.warn("Could not guess class name from " +
			// lexFile.getName()
			// + ". Class name set to " + DEFAULT_NAME);
			className = DEFAULT_NAME;
		}
		return new File(outputDirectory + "/"
				+ packageName.replace('.', File.separatorChar) + "/"
				+ className + ".java");

	}
}