package com.ti.sensortag.ble;

import static android.bluetooth.BluetoothGatt.*;

public class BluetoothGattUtils {
    public static String decodeReturnCode(int status){
	switch(status){
	case GATT_FAILURE: return "GATT_FAILURE";
	case GATT_INSUFFICIENT_AUTHENTICATION: return "GATT_INSUFFICIENT_AUTHENTICATION";
	case GATT_INSUFFICIENT_ENCRYPTION: return "GATT_INSUFFICIENT_ENCRYPTION";
	case GATT_INVALID_ATTRIBUTE_LENGTH: return "GATT_INVALID_ATTRIBUTE_LENGTH";
	case GATT_INVALID_OFFSET: return "GATT_INVALID_OFFSET";
	case GATT_READ_NOT_PERMITTED: return "GATT_READ_NOT_PERMITTED";
	case GATT_REQUEST_NOT_SUPPORTED: return "GATT_REQUEST_NOT_SUPPORTED";
	case GATT_SUCCESS: return "GATT_SUCCESS";
	case GATT_WRITE_NOT_PERMITTED: return "GATT_WRITE_NOT_PERMITTED";
	default: return "Unknown return code: " + status;
	}
    }
}
