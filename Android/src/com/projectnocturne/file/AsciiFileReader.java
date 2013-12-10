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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class AsciiFileReader extends AsciiFile {

	protected BufferedReader buffrdRdr;
	protected FileInputStream fIn;
	protected InputStreamReader isr;

	/**
	 * close the ascii file so that it can no longer be read.
	 */
	public void closeFile() {
		try {
			buffrdRdr.close();
			isr.close();
		} catch (final Exception ex) {
			// Logger.getLogger(AsciiFileReader.class.getName()).log(Level.SEVERE,
			// null, ex);
		}
	}

	/**
	 * reads the first line of the file.
	 * 
	 * @return a string containing the first line of the file
	 */
	public String firstLineFromFile() {
		String sFileLine = null;
		try {
			closeFile();
			openFile();
			sFileLine = buffrdRdr.readLine();
			if (sFileLine != null) {
				m_lPosition += sFileLine.length();
				sFileLine.trim();
			} else {
				sFileLine = "";
			}
		} catch (final IOException ex) {
			// Logger.getLogger(AsciiFileReader.class.getName()).log(Level.SEVERE,
			// null,
			// ex);
		}
		return sFileLine;
	}

	public void mark() {
		try {
			buffrdRdr.mark(100);
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * reads the first line of the file.
	 * 
	 * @return a string containing a line of the file
	 */
	public String nextLineFromFile() {
		String sFileLine = null;
		try {
			sFileLine = buffrdRdr.readLine();
			if (sFileLine != null) {
				m_lPosition += sFileLine.length();
				sFileLine.trim();
			} else {
				sFileLine = "";
			}
		} catch (final IOException ex) {
			// Logger.getLogger(AsciiFileReader.class.getName()).log(Level.SEVERE,
			// null,
			// ex);
		}
		return sFileLine.trim();
	}

	/**
	 * opens the file ready for reading.
	 */
	public void openFile() {
		try {
			final File gpxfile = new File(m_sFilepath + m_sFilename);
			isr = new FileReader(gpxfile);
			buffrdRdr = new BufferedReader(isr, 2048);
		} catch (final FileNotFoundException ex) {
			// Logger.getLogger(AsciiFileReader.class.getName()).log(Level.SEVERE,
			// null,
			// ex);
		}
	}

	/**
	 * reads in the next line from the file.
	 */
	public String read() {
		return nextLineFromFile().trim();
	}

	public void reset() {
		try {
			buffrdRdr.reset();
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public String toString() {
		return "File: " + m_sFilepath + m_sFilename;
	}
} // class()
