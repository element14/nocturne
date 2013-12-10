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

public class AsciiFile {

	protected BufferedReader br;

	protected int m_lPosition;
	/**
	 * the name of the file to read
	 */
	protected String m_sFilename = "";
	protected String m_sFilepath = File.separatorChar + "sdcard" + java.io.File.separatorChar;

	public final int getCurrentPosition() {
		return m_lPosition;
	}

	public String getFilename() {
		return m_sFilename;
	}

	public final void setCurrentPosition(final int value) {
		m_lPosition = value;
	}

	public void setFilename(final String aFilename) {
		int indexOf = aFilename.lastIndexOf(java.io.File.separatorChar);
		if (indexOf == -1) {
			indexOf = aFilename.lastIndexOf(java.io.File.separatorChar);
		}
		m_sFilename = aFilename.substring(indexOf + 1);
		m_sFilepath = aFilename.substring(0, indexOf + 1);
	}

}
