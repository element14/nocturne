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
