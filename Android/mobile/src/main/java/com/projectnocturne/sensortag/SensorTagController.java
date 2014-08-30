/**
 * 
 * Copyright Notice
 *  ----------------
 *
 * The copyright in this document is the property of 
 * Bath Institute of Medical Engineering.
 *
 * Without the written consent of Bath Institute of Medical Engineering
 * given by Contract or otherwise the document must not be copied, reprinted or
 * reproduced in any material form, either wholly or in part, and the contents
 * of the document or any method or technique available there from, must not be
 * disclosed to any other person whomsoever.
 * 
 *  Copyright 2013-2014 Bath Institute of Medical Engineering.
 * --------------------------------------------------------------------------
 *
 */

package com.projectnocturne.sensortag;

import java.util.List;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattService;

/**
 * @author andy
 * 
 */
public final class SensorTagController {

	private static final SensorTagController instance = new SensorTagController();

	public static SensorTagController getInstance() {
		return instance;
	}

	public List<BluetoothGattService> mServiceList = null;
	public BluetoothAdapter mBluetoothAdapter;
	public BluetoothGatt mBluetoothGatt;
	public BluetoothDevice mBtDevice;
}
