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
