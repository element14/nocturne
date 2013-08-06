/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.projectnocturne.datamodel;

import java.io.File;

import android.os.Environment;

/**
 * 
 * @author andy
 */
public class DbConstants {

	public String getDbName() {
		return "InLibrisLibertasDb";
	}

	public String getDbPath() {
		final File extDir = Environment.getExternalStorageDirectory();
		return extDir.getPath() + File.separator + "droidinactu" + File.separator + "InLibrisLibertas" + File.separator;
	}

	public int getDbVersion() {
		return 1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see uk.co.droidinactu.common.db.AbstractDbConstants#runInDebugMode()
	 */
	public Boolean runInDebugMode() {
		return true;
	}

}
