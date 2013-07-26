package com.projectnocturne.datamodel;

import java.io.IOException;
import java.util.List;

import android.content.Context;

import com.db4o.Db4oEmbedded;
import com.db4o.config.CommonConfiguration;
import com.db4o.config.EmbeddedConfiguration;

public final class UsersProvider extends Db4oHelper {

	public final static String TAG = UsersProvider.class.getName();

	private static UsersProvider provider = null;

	public static UsersProvider getInstance(final Context ctx) {
		if (provider == null) {
			provider = new UsersProvider(ctx);
		}
		return provider;
	}

	public UsersProvider(final Context ctx) {
		super(ctx);
	}

	/**
	 * Configure the behaviour of the database
	 */
	@Override
	protected EmbeddedConfiguration dbConfig() throws IOException {
		final EmbeddedConfiguration configEmbedded = Db4oEmbedded.newConfiguration();
		final CommonConfiguration configCommon = configEmbedded.common();
		configCommon.activationDepth(8); // Global activation depth.

		configEmbedded.common().objectClass(Users.class).objectField("_id").indexed(true);
		configEmbedded.common().objectClass(Users.class).cascadeOnUpdate(true);
		configEmbedded.common().objectClass(Users.class).cascadeOnActivate(true);
		return configEmbedded;
	}

	public void delete(final Users usr) {
		db().delete(usr);
		db().commit();
	}

	public List<Users> findAll() {
		return db().query(Users.class);
	}

	public void store(final Users usr) {
		db().store(usr);
		db().commit();
	}
}
