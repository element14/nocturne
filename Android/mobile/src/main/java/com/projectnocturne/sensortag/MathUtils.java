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
 package com.projectnocturne.sensortag;

import static android.bluetooth.BluetoothGattCharacteristic.FORMAT_SINT8;
import static android.bluetooth.BluetoothGattCharacteristic.FORMAT_UINT8;
import android.bluetooth.BluetoothGattCharacteristic;

public final class MathUtils {

	/**
	 * Gyroscope, Magnetometer, Barometer, IR temperature all store 16 bit two's
	 * complement values in the awkward format LSB MSB, which cannot be directly
	 * parsed as getIntValue(FORMAT_SINT16, offset) because the bytes are stored
	 * in the "wrong" direction.
	 * 
	 * This function extracts these 16 bit two's complement values.
	 * */
	public static Integer shortSignedAtOffset(final BluetoothGattCharacteristic c, final int offset) {
		final Integer lowerByte = c.getIntValue(FORMAT_UINT8, offset);
		final Integer upperByte = c.getIntValue(FORMAT_SINT8, offset + 1); // Note:
		// interpret
		// MSB as
		// signed.

		return (upperByte << 8) + lowerByte;
	}

	public static Integer shortUnsignedAtOffset(final BluetoothGattCharacteristic c, final int offset) {
		final Integer lowerByte = c.getIntValue(FORMAT_UINT8, offset);
		final Integer upperByte = c.getIntValue(FORMAT_UINT8, offset + 1); // Note:
		// interpret
		// MSB as
		// unsigned.

		return (upperByte << 8) + lowerByte;
	}

}
