package com.projectnocturne.datamodel;

import java.io.File;
import java.io.IOException;

import android.content.Context;
import android.util.Log;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.config.EmbeddedConfiguration;

public abstract class Db4oHelper {

	private static ObjectContainer oc = null;
	private final Context context;

	/**
	 * @param ctx
	 */

	public Db4oHelper(final Context ctx) {
		context = ctx;
	}

	/**
	 * Closes the database
	 */

	public void close() {
		if (oc != null) {
			oc.commit();
			oc.close();
		}
	}

	/**
	 * Create, open and close the database
	 */
	public ObjectContainer db() {
		try {
			if (oc == null || oc.ext().isClosed()) {
				oc = Db4oEmbedded.openFile(dbConfig(), db4oDBFullPath(context));
			}
			return oc;

		} catch (final Exception ie) {
			Log.e(Db4oHelper.class.getName(), ie.toString());
			return null;
		}
	}

	/**
	 * Returns the path for the database location
	 */

	private String db4oDBFullPath(final Context ctx) {
		final File extFileDir = ctx.getExternalFilesDir(null);
		return extFileDir + File.separator + "trainingplans.db4o";
	}

	/**
	 * Configure the behavior of the database
	 */
	protected abstract EmbeddedConfiguration dbConfig() throws IOException;

}