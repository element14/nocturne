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

import static android.bluetooth.BluetoothGatt.GATT_FAILURE;
import static android.bluetooth.BluetoothGatt.GATT_INSUFFICIENT_AUTHENTICATION;
import static android.bluetooth.BluetoothGatt.GATT_INSUFFICIENT_ENCRYPTION;
import static android.bluetooth.BluetoothGatt.GATT_INVALID_ATTRIBUTE_LENGTH;
import static android.bluetooth.BluetoothGatt.GATT_INVALID_OFFSET;
import static android.bluetooth.BluetoothGatt.GATT_READ_NOT_PERMITTED;
import static android.bluetooth.BluetoothGatt.GATT_REQUEST_NOT_SUPPORTED;
import static android.bluetooth.BluetoothGatt.GATT_SUCCESS;
import static android.bluetooth.BluetoothGatt.GATT_WRITE_NOT_PERMITTED;

public class BluetoothGattUtils {
	public static String decodeReturnCode(final int status) {
		switch (status) {
		case GATT_FAILURE:
			return "GATT_FAILURE";
		case GATT_INSUFFICIENT_AUTHENTICATION:
			return "GATT_INSUFFICIENT_AUTHENTICATION";
		case GATT_INSUFFICIENT_ENCRYPTION:
			return "GATT_INSUFFICIENT_ENCRYPTION";
		case GATT_INVALID_ATTRIBUTE_LENGTH:
			return "GATT_INVALID_ATTRIBUTE_LENGTH";
		case GATT_INVALID_OFFSET:
			return "GATT_INVALID_OFFSET";
		case GATT_READ_NOT_PERMITTED:
			return "GATT_READ_NOT_PERMITTED";
		case GATT_REQUEST_NOT_SUPPORTED:
			return "GATT_REQUEST_NOT_SUPPORTED";
		case GATT_SUCCESS:
			return "GATT_SUCCESS";
		case GATT_WRITE_NOT_PERMITTED:
			return "GATT_WRITE_NOT_PERMITTED";
		default:
			return "Unknown return code: " + status;
		}
	}
}
