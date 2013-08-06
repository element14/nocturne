/*<p>
 * Copyright 2012 Andy Aspell-Clark
 *</p><p>
 * This file is part of eBookLauncher.
 * </p><p>
 * eBookLauncher is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 * </p><p>
 * eBookLauncher is distributed in the hope that it will be useful, but
 *  WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 *  or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 *  for more details.
 * </p><p>
 * You should have received a copy of the GNU General Public License along
 * with eBookLauncher. If not, see http://www.gnu.org/licenses/.
 *</p>
 */
package com.projectnocturne.file;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVStrategy;

/**
 * <p>
 * This is a base class that handles all interaction with a delimited text file.
 * It can be sub classed to provide handling for more specific files.
 * </p>
 * <p>
 * It contains member functions to open and close a file, to read in the first
 * and subsequent lines of the file as field lists as well as simple strings.
 * </p>
 * 
 * @author andy
 * 
 */
public class DelimitedFile extends AsciiFileReader {
	/**
	 * the character used to delimit the fields in the file
	 */
	protected String m_sDelimiter = ",";
	protected String m_sFilepath = "./";

	public DelimitedFile() {
	}

	public DelimitedFile(final String psDelimiter) {
		m_sDelimiter = psDelimiter;
	}

	public ArrayList<String> convertLineToFields(final String line) {
		// Log.w("DelimitedFile", "convertLineToFields converting line [" + line
		// +
		// "] to fields");
		final ArrayList<String> alFields = new ArrayList<String>();

		final CSVStrategy csvStrat = new CSVStrategy(m_sDelimiter.charAt(0), '\"', '#');
		csvStrat.setIgnoreLeadingWhitespaces(true);
		final CSVParser csvParser = new CSVParser(new StringReader(line), csvStrat);
		String[][] st;
		try {
			st = csvParser.getAllValues();
			for (final String[] element : st) {
				for (final String element2 : element) {
					alFields.add(element2);
				}
			}
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return alFields;
	}

	// <editor-fold desc="Properties">
	public String getDelimiter() {
		return m_sDelimiter;
	}

	// </editor-fold>

	/**
	 * reads the first line of the file
	 * 
	 * @return a list of the fields in the first line of the file
	 */
	public ArrayList<String> readFirstLineFromFile() {
		ArrayList<String> alFields = null;

		final String line = super.firstLineFromFile();
		if (line != null) {
			alFields = new ArrayList<String>();
			alFields = convertLineToFields(line);
		}

		return alFields;
	}

	/**
	 * 
	 * @return a list of the fields in the next line of the file
	 */
	public ArrayList<String> readNextLineFromFile() {
		ArrayList<String> alFields = new ArrayList<String>();

		final String line = super.nextLineFromFile();
		alFields = convertLineToFields(line);

		return alFields;
	}

	public void setDelimiter(final String aDelim) {
		m_sDelimiter = aDelim;
	}

} // class()

