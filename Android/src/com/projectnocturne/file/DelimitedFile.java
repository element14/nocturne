 /**
 * <p>
 * <u><b>Copyright Notice</b></u>
 * </p><p>
 * The copyright in this document is the property of 
 * Bath Institute of Medical Engineering.
 * </p><p>
 * Without the written consent of Bath Institute of Medical Engineering
 * given by Contract or otherwise the document must not be copied, reprinted or
 * reproduced in any material form, either wholly or in part, and the contents
 * of the document or any method or technique available there from, must not be
 * disclosed to any other person whomsoever.
 *  </p><p>
 *  <b><i>Copyright 2013-2014 Bath Institute of Medical Engineering.</i></b>
 * --------------------------------------------------------------------------
 * 
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

