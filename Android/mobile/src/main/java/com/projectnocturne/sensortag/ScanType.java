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
