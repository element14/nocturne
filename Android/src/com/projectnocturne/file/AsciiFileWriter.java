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
