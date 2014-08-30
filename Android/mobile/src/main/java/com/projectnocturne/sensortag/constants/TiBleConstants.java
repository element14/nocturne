/************************************************************************************
 *
 *  Copyright (C) 2013 HTC
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 ************************************************************************************/
package com.projectnocturne.sensortag.constants;

/**
 * UUIDs and values for working with TI CC2541DK-SENSOR CC2541 SensorTag
 * Development Kit.
 * 
 */
public class TiBleConstants {
	private static final String LOG_TAG = TiBleConstants.class.getSimpleName();

	public static final String BLUETOOTH_BASE_UUID = "00000000-0000-1000-8000-00805F9B34FB";
	public static final String TI_BASE_UUID = "F0000000-0451-4000-B000-000000000000";

	public static final String IRTEMPERATURE_SERV_UUID = "F000AA00-0451-4000-B000-000000000000";
	public static final String IRTEMPERATURE_DATA_UUID = "F000AA01-0451-4000-B000-000000000000";
	public static final String IRTEMPERATURE_CONF_UUID = "F000AA02-0451-4000-B000-000000000000";

	public static final String ACCELEROMETER_SERV_UUID = "F000AA10-0451-4000-B000-000000000000";
	public static final String ACCELEROMETER_DATA_UUID = "F000AA11-0451-4000-B000-000000000000";
	public static final String ACCELEROMETER_CONF_UUID = "F000AA12-0451-4000-B000-000000000000";
	public static final String ACCELEROMETER_PERI_UUID = "F000AA13-0451-4000-B000-000000000000";

	public static final String HUMIDITY_SERV_UUID = "F000AA20-0451-4000-B000-000000000000";
	public static final String HUMIDITY_DATA_UUID = "F000AA21-0451-4000-B000-000000000000";
	public static final String HUMIDITY_CONF_UUID = "F000AA22-0451-4000-B000-000000000000";

	public static final String MAGNETOMETER_SERV_UUID = "F000AA30-0451-4000-B000-000000000000";
	public static final String MAGNETOMETER_DATA_UUID = "F000AA31-0451-4000-B000-000000000000";
	public static final String MAGNETOMETER_CONF_UUID = "F000AA32-0451-4000-B000-000000000000";
	public static final String MAGNETOMETER_PERI_UUID = "F000AA33-0451-4000-B000-000000000000";

	public static final String BAROMETER_SERV_UUID = "F000AA40-0451-4000-B000-000000000000";
	public static final String BAROMETER_DATA_UUID = "F000AA41-0451-4000-B000-000000000000";
	public static final String BAROMETER_CONF_UUID = "F000AA42-0451-4000-B000-000000000000";
	public static final String BAROMETER_CALIBRATION_UUID = "F000AA43-0451-4000-B000-000000000000";

	public static final String GYROSCOPE_SERV_UUID = "F000AA50-0451-4000-B000-000000000000";
	public static final String GYROSCOPE_DATA_UUID = "F000AA51-0451-4000-B000-000000000000";
	public static final String GYROSCOPE_CONF_UUID = "F000AA52-0451-4000-B000-000000000000";

	public static final String SIMPLE_KEYS_SERV_UUID = "0000FFE0-0000-1000-8000-00805F9B34FB";
	public static final String SIMPLE_KEYS_DATA_UUID = "0000FFE1-0000-1000-8000-00805F9B34FB";
	public static final String SIMPLE_KEYS_KEYPRESSED_UUID = "0000FFE1-0000-1000-8000-00805F9B34FB";

	public static final String TEST_SERVICE_UUID = "F000AA60-0451-4000-B000-000000000000";
	public static final String TEST_DATA_UUID = "F000AA61-0451-4000-B000-000000000000";
	public static final String TEST_CONFIG_UUID = "F000AA62-0451-4000-B000-000000000000";

}
