package com.ti.sensortag.ble;

import java.util.List;

/**
 * As a last-second hack i'm storing the barometer coefficients in a global.
 */
public enum BarometerCalibrationCoefficients {
    INSTANCE;
    volatile public List<Integer> barometerCalibrationCoefficients;
}
