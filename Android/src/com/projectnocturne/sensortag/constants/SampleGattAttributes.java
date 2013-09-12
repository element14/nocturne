/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.projectnocturne.sensortag.constants;

import java.util.HashMap;

/**
 * This class includes a small subset of standard GATT attributes for
 * demonstration purposes.
 */
public class SampleGattAttributes {
	private static final String LOG_TAG = SampleGattAttributes.class.getSimpleName();

	private static HashMap<String, String> attributes = new HashMap();
	public static String HEART_RATE_MEASUREMENT = "00002a37-0000-1000-8000-00805f9b34fb";
	public static String CLIENT_CHARACTERISTIC_CONFIG = "00002902-0000-1000-8000-00805f9b34fb";

	static {
		// Sample Services.
		attributes.put("0000180d-0000-1000-8000-00805f9b34fb", "Heart Rate Service");
		attributes.put("0000180a-0000-1000-8000-00805f9b34fb", "Device Information Service");
		// Sample Characteristics.
		attributes.put(HEART_RATE_MEASUREMENT, "Heart Rate Measurement");
		attributes.put("00002a29-0000-1000-8000-00805f9b34fb", "Manufacturer Name String");

		attributes.put(BleServices.ALERT_NOTIFICATION, "Alert Notification");
		attributes.put(BleServices.BATTERY_SERVICE, "Battery Service");
		attributes.put(BleServices.BLOOD_PRESSURE, "BLOOD_PRESSURE");
		attributes.put(BleServices.CURRENT_TIME, "BLOOD_PRESSURE");
		attributes.put(BleServices.CYCLING_SPEED_AND_CADENCE, "CYCLING_SPEED_AND_CADENCE");
		attributes.put(BleServices.DEVICE_INFORMATION, "DEVICE_INFORMATION");
		attributes.put(BleServices.GENERIC_ACCESS, "GENERIC_ACCESS");
		attributes.put(BleServices.GENERIC_ATTRIBUTE, "GENERIC_ATTRIBUTE");
		attributes.put(BleServices.GLUCOSE, "GLUCOSE");
		attributes.put(BleServices.HEALTH_THERMOMETER, "HEALTH_THERMOMETER");
		attributes.put(BleServices.HEART_RATE, "HEART_RATE");
		attributes.put(BleServices.HUMAN_INTERFACE_DEVICE, "HUMAN_INTERFACE_DEVICE");
		attributes.put(BleServices.IMMEDIATE_ALERT, "IMMEDIATE_ALERT");
		attributes.put(BleServices.LINK_LOSS, "LINK_LOSS");
		attributes.put(BleServices.NEXT_DST_CHANGE, "NEXT_DST_CHANGE");
		attributes.put(BleServices.PHONE_ALERT_STATUS, "PHONE_ALERT_STATUS");
		attributes.put(BleServices.REFERENCE_TIME_UPDATE, "REFERENCE_TIME_UPDATE");
		attributes.put(BleServices.RUNNING_SPEED_AND_CADENCE, "RUNNING_SPEED_AND_CADENCE");
		attributes.put(BleServices.SCAN_PARAMETERS, "SCAN_PARAMETERS");
		attributes.put(BleServices.TX_POWER, "TX_POWER");

		attributes.put(BleCharacteristics.ALERT_CATEGORY_ID, "Alert Cat ID");
		attributes.put(BleCharacteristics.ALERT_CATEGORY_ID_BIT_MASK, "Alert Cat ID bitmask");
		attributes.put(BleCharacteristics.ALERT_LEVEL, "ALERT_LEVEL");
		attributes.put(BleCharacteristics.ALERT_NOTIFICATION_CONTROL_POINT, "ALERT_NOTIFICATION_CONTROL_POINT");
		attributes.put(BleCharacteristics.ALERT_STATUS, "ALERT_STATUS");
		attributes.put(BleCharacteristics.APPEARANCE, "APPEARANCE");

		attributes.put(TiBleConstants.IRTEMPERATURE_SERV_UUID, "IR Temperature Service");
		attributes.put(TiBleConstants.IRTEMPERATURE_DATA_UUID, "IR Temperature Data");
		attributes.put(TiBleConstants.IRTEMPERATURE_CONF_UUID, "IR Temperature Conf");

		attributes.put(TiBleConstants.ACCELEROMETER_DATA_UUID, "Accelerometer Data");
		attributes.put(TiBleConstants.ACCELEROMETER_SERV_UUID, "Accelerometer Service");
		attributes.put(TiBleConstants.ACCELEROMETER_CONF_UUID, "Accelerometer Conf");
		attributes.put(TiBleConstants.ACCELEROMETER_PERI_UUID, "Accelerometer Peri");

		attributes.put(TiBleConstants.HUMIDITY_SERV_UUID, "Humidity Data Service");
		attributes.put(TiBleConstants.HUMIDITY_DATA_UUID, "Humidity Data");
		attributes.put(TiBleConstants.HUMIDITY_CONF_UUID, "Humidity Conf");

		attributes.put(TiBleConstants.MAGNETOMETER_SERV_UUID, "Magnetometer Service");
		attributes.put(TiBleConstants.MAGNETOMETER_DATA_UUID, "Magnetometer Data");
		attributes.put(TiBleConstants.MAGNETOMETER_CONF_UUID, "Magnetometer conf");
		attributes.put(TiBleConstants.MAGNETOMETER_PERI_UUID, "Magnetometer Peri");

		attributes.put(TiBleConstants.BAROMETER_SERV_UUID, "Barometer Data Service");
		attributes.put(TiBleConstants.BAROMETER_DATA_UUID, "Barometer Data");
		attributes.put(TiBleConstants.BAROMETER_CONF_UUID, "Barometer Conf");
		attributes.put(TiBleConstants.BAROMETER_CALIBRATION_UUID, "Barometer Calibration");

		attributes.put(TiBleConstants.GYROSCOPE_SERV_UUID, "Gyroscope Data Service");
		attributes.put(TiBleConstants.GYROSCOPE_DATA_UUID, "Gyroscope Data");
		attributes.put(TiBleConstants.GYROSCOPE_CONF_UUID, "Gyroscope Conf");

		attributes.put(TiBleConstants.SIMPLE_KEYS_SERV_UUID, "SIMPLE_KEYS_SERV");
		attributes.put(TiBleConstants.SIMPLE_KEYS_DATA_UUID, "SIMPLE_KEYS_DATA");
		attributes.put(TiBleConstants.SIMPLE_KEYS_KEYPRESSED_UUID, "SIMPLE_KEYS_KEYPRESSED");

		attributes.put(TiBleConstants.TEST_SERVICE_UUID, "TEST_SERVICE_UUID");
		attributes.put(TiBleConstants.TEST_DATA_UUID, "TEST_DATA_UUID");
		attributes.put(TiBleConstants.TEST_CONFIG_UUID, "TEST_CONFIG_UUID");
	}

	public static String lookup(final String uuid, final String defaultName) {
		// Log.i(LOG_TAG, "lookup [" + uuid + " : " + defaultName + "]");
		final String name = attributes.get(uuid.toUpperCase());
		return name == null ? defaultName : name;
	}
}
