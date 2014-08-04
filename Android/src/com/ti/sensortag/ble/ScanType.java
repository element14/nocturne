package com.ti.sensortag.ble;

/**
 * Android devices implement scanning differently. 
 * This enum encodes what scanning type is used.
 * 
 * Some devices, like the Samsung phones, will return an onLeScan callback for each advertisement
 * observed during the scanning period. As one would expect it to.
 * This is dubbed CONTINUOUS scanning.
 * 
 * But other devices, like the Nexus phones, will only call one onLeScan callback 
 * for each device observed while scanning.
 * This is dubbed ONE_OFF scanning.
 * 
 * The CONTINUOUS mode will easily allow one to in real-time know if 
 * a device is currently advertising. While the ONE-OFF mode
 * is potentially energy efficient.
 * 
 * This was true at 2013-08-06.
 */
public enum ScanType {
    CONTINUOUS,
    ONE_OFF
}
