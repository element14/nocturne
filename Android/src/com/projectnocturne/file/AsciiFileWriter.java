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

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AsciiFileWriter extends AsciiFile {

	protected OutputStreamWriter osw;
	protected BufferedWriter buffrdWtr;

	public final void closeFile() {
		try {
			buffrdWtr.close();
			osw.close();
		} catch (final IOException ex) {
			Logger.getLogger(AsciiFileWriter.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	/**
	 * opens the file ready for writing
	 */
	public final void openFile() {
		try {
			osw = new FileWriter(m_sFilepath + m_sFilename);
			// FileOutputStream of =
			// android.content.Context.openFileOutput(this.m_sFilename,
			// android.content.Context.MODE_WORLD_READABLE);
			buffrdWtr = new BufferedWriter(osw);
		} catch (final FileNotFoundException ex) {
			Logger.getLogger(AsciiFileReader.class.getName()).log(Level.SEVERE, null, ex);
		} catch (final IOException ioex) {
			Logger.getLogger(AsciiFileReader.class.getName()).log(Level.SEVERE, null, ioex);
		}
	}

	/**
	 * writes a text line out to the file
	 * 
	 * @param psLine
	 */
	public final void Write(final String psLine) {
		try {
			buffrdWtr.write(psLine);
		} catch (final IOException ex) {
			Logger.getLogger(AsciiFileWriter.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	/**
	 * writes a text line out to the file and appends a newline to the end of it
	 * 
	 * @param psLine
	 */
	public final void WriteLine(final String psLine) {
		try {
			buffrdWtr.write(psLine);
			buffrdWtr.write("\n");
		} catch (final IOException ex) {
			Logger.getLogger(AsciiFileWriter.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}
