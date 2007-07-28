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
import java.io.IOException;

import org.apache.commons.io.FilenameUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;

import JFlex.Main;
import JFlex.Options;

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
	private File[] lexFiles;

	private Log log = getLog();

	/**
	 * Name of the directory into which JFlex should generate the parser.
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
		for (int i = 0; i < lexFiles.length; i++) {
			// set default output directory.
			// lexFiles[i].setTargetDirectory(outputDirectory);

			File lexFile = lexFiles[i];
			ClassInfo classInfo;
			try {
				classInfo = LexSimpleAnalyzer.guessPackageAndClass(lexFile);
			} catch (FileNotFoundException e1) {
				throw new MojoFailureException(e1.getMessage());
			} catch (IOException e3) {
				classInfo = new ClassInfo();
				classInfo.className = LexSimpleAnalyzer.DEFAULT_NAME;
				classInfo.packageName = null;
			}

			checkParameters(lexFile);

			/* set destination directory */
			File generatedFile = new File(outputDirectory
					+ classInfo.getOutputFilename());

			/* Generate only if needs to */
			if (lexFile.lastModified() < generatedFile.lastModified()) {
				log.info(generatedFile.getName() + " is up to date.");
				continue;
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

	public File getOutputDirectory() {
		return outputDirectory;
	}

	public void setOutputDirectory(File outputDirectory) {
		this.outputDirectory = outputDirectory;
	}

	public void setLexFiles(File[] lexFiles) {
		this.lexFiles=lexFiles;
		
	}

}
