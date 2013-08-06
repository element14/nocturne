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

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;

/**
 * This class holds functions related to file manipulation.
 * 
 * @author andy
 */
public class FileUtilities {

	/**
	 * copy one file to a new file.
	 * 
	 * @param in
	 *            the file to make the copy of.
	 * @param out
	 *            the destination for the copy of the file.
	 * @throws Exception
	 */
	public static void copyFile(final File in, final File out) throws IOException {
		final FileInputStream fis = new FileInputStream(in);
		final FileOutputStream fos = new FileOutputStream(out);
		try {
			final byte[] buf = new byte[1024];
			int i = 0;
			while ((i = fis.read(buf)) != -1) {
				fos.write(buf, 0, i);
			}
		} catch (final IOException e) {
			throw e;
		} finally {
			fis.close();
			fos.close();
		}
	}

	public static boolean StoreByteImage(final Context mContext, final byte[] imageData, final int quality,
			final String imgDirectory, final String filename) {

		// File sdImageMainDirectory = new File(imgDirectory);
		FileOutputStream fileOutputStream = null;
		try {

			final BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = 5;

			final Bitmap myImage = BitmapFactory.decodeByteArray(imageData, 0, imageData.length, options);

			final String newFilename = imgDirectory + File.separator + filename + ".jpg";
			fileOutputStream = new FileOutputStream(newFilename);

			final BufferedOutputStream bos = new BufferedOutputStream(fileOutputStream);

			myImage.compress(CompressFormat.JPEG, quality, bos);

			bos.flush();
			fileOutputStream.flush();

			bos.close();
			fileOutputStream.close();

		} catch (final FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return true;
	}

}
