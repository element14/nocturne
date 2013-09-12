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
