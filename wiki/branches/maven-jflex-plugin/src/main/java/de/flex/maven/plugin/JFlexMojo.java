/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * JFlex Maven2 plugin                                                     *
 * Copyright (c) 2007       Régis Décamps <decamps@users.sf.net>           *
 *                                                                         *
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

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;

import JFlex.DFA;
import JFlex.Emitter;
import JFlex.ErrorMessages;
import JFlex.LexParse;
import JFlex.LexScan;
import JFlex.Main;
import JFlex.NFA;
import JFlex.Options;
import JFlex.Out;
import JFlex.Timer;

/**
 * @goal generate
 * @phase generate-sources
 * @author Régis Décamps (decamps@users.sf.net)
 * 
 */
public class JFlexMojo extends AbstractMojo {
	/**
	 * List of parser definitions to run the JFlex parser generator on.
	 * 
	 * A parser is mainly a lexFile. TODO documentation <code>
	 * 
	 * </code>
	 * 
	 * @parameter
	 * @required
	 */
	private Parser[] parsers;

	private Log log = getLog();

	/**
	 * Name of the directory into which JFlex should generate the parser.
	 * Default is
	 * <code>${project.build.directory}/generated-sources/jflex</code>.
	 * 
	 * @parameter expression="${project.build.directory}/generated-sources/jflex"
	 */
	private File outputDirectory;

	/**
	 * Whether source code generation should be verbose.
	 * 
	 * @parameter
	 */
	private boolean verbose;

	/**
	 * Whether to produce graphviz .dot files for the generated automata. This
	 * feature is EXPERIMENTAL.
	 */
	private boolean dot = false;

	/**
	 * Generate java parser from lexer definition.
	 * 
	 * This methods is checks parameters, sets options and calls
	 * JFlex.Main.generate()
	 */
	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		for (int i = 0; i < parsers.length; i++) {
			// set default output directory.
			parsers[i].setOutputDirectory(outputDirectory);

			File lexFile = parsers[i].getLexFile();

			checkParameters(lexFile);

			/* set destination directory */
			File generatedFile;
			try {
				generatedFile = parsers[i].getOutputFile();
			} catch (FileNotFoundException e1) {
				throw new MojoFailureException(e1.getMessage());
			} catch (IOException e2) {
				log.warn("Cannot guess name of the Java code to generate");
				// e1.printStackTrace();
				generatedFile = new File(outputDirectory + Parser.DEFAULT_NAME
						+ ".java");
			}

			/* Generate only if needs to */
			if (lexFile.lastModified() < generatedFile.lastModified()) {
				log.info(generatedFile.getName() + " is up to date.");
				break;
			}

			/*
			 * set options. Very strange that JFlex expects this in a static
			 * way.
			 */
			Options.setDir(FilenameUtils.getFullPath(generatedFile
					.getAbsoluteFile().getPath()));
			Options.dump = verbose;
			Options.verbose = verbose;
			Options.dot = dot;
			try {
				Main.generate(lexFile);
			} catch (Exception e) {
				throw new MojoExecutionException(e.getMessage());
			}

		}
	}

	public File findDestDirectory(String packageName) {
		File destDirectory;
		if (packageName == null) {
			destDirectory = outputDirectory;
		} else {
			destDirectory = new File(outputDirectory.getAbsolutePath()
					+ File.separatorChar
					+ packageName.replace('.', File.separatorChar));
		}
		return destDirectory;
	}

	/**
	 * Check parameter lexFile.
	 * 
	 * Must not be null and file must exist.
	 * 
	 * @param lexFile
	 *            input file to check.
	 * @throws MojoExecutionException
	 *             in case of error
	 */
	private void checkParameters(File lexFile) throws MojoExecutionException {
		if (lexFile == null) {
			throw new MojoExecutionException(
					"<lexFile> is empty. Please define input file with <lexFile>input.lex</lexFile>");
		}
		if (!lexFile.exists()) {
			throw new MojoExecutionException("Input file does not exist: "
					+ lexFile.getAbsolutePath());
		}
	}

	public Parser[] getLexFiles() {
		return parsers;
	}

	public File getOutputDirectory() {
		return outputDirectory;
	}

	public void setLexFiles(Parser[] lexFiles) {
		this.parsers = lexFiles;
	}

	public void setOutputDirectory(File outputDirectory) {
		this.outputDirectory = outputDirectory;
	}
}
