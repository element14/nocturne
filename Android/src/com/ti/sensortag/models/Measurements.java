package com.ti.sensortag.models;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Arrays;
import java.util.List;

import android.util.Log;

public enum Measurements {
	INSTANCE;

	public final static String TAG = "Measurements";

	public final static String PROPERTY_ACCELEROMETER = "ACCELEROMETER", PROPERTY_AMBIENT_TEMPERATURE = "AMBIENT",
			PROPERTY_IR_TEMPERATURE = "IR_TEMPERATURE", PROPERTY_HUMIDITY = "HUMIDITY",
			PROPERTY_MAGNETOMETER = "MAGNETOMETER", PROPERTY_GYROSCOPE = "GYROSCOPE",
			PROPERTY_SIMPLE_KEYS = "SIMPLE_KEYS", PROPERTY_BAROMETER = "BAROMETER";

	private final PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);

	// Model values
	private SimpleKeysStatus status;
	private double ambientTemperature, irTemperature, humidity, barometer;
	private Point3D accelerometer, magnetometer, gyroscope;

	// TODO: add support for addPropertyChangeListener(propertyName, listener);
	public void addPropertyChangeListener(final PropertyChangeListener listener) {
		// Don't add the same object twice. I can't imagine a use-case where you
		// would want to do that.
		final List<PropertyChangeListener> listeners = Arrays.asList(changeSupport.getPropertyChangeListeners());
		if (!listeners.contains(this)) {
			changeSupport.addPropertyChangeListener(listener);
		}
	}

	public Point3D getAccelerometer() {
		return accelerometer;
	}

	public double getAmbientTemperature() {
		return ambientTemperature;
	}

	public double getIrTemperature() {
		return irTemperature;
	}

	public SimpleKeysStatus getStatus() {
		return status;
	}

	public void removePropertyChangeListener(final PropertyChangeListener listener) {
		changeSupport.removePropertyChangeListener(listener);
	}

	public void setAccelerometer(final double x, final double y, final double z) {
		final Point3D newValue = new Point3D(x, y, z);
		final Point3D oldValue = accelerometer;
		accelerometer = newValue;
		changeSupport.firePropertyChange(PROPERTY_ACCELEROMETER, oldValue, newValue);
	}

	public void setAmbientTemperature(final double newValue) {
		final double oldValue = ambientTemperature;
		ambientTemperature = newValue;
		changeSupport.firePropertyChange(PROPERTY_AMBIENT_TEMPERATURE, oldValue, newValue);
	}

	public void setBarometer(final Double newValue) {
		final Double oldValue = barometer;
		barometer = newValue;
		changeSupport.firePropertyChange(PROPERTY_BAROMETER, oldValue, newValue);

		Log.i(TAG, "setBarometer(" + newValue + ");");
	}

	public void setGyroscope(final float x, final float y, final float z) {
		final Point3D newValue = new Point3D(x, y, z);
		final Point3D oldValue = gyroscope;
		gyroscope = newValue;
		changeSupport.firePropertyChange(PROPERTY_GYROSCOPE, oldValue, newValue);
	}

	public void setHumidity(final double newValue) {
		final double oldValue = humidity;
		humidity = newValue;
		changeSupport.firePropertyChange(PROPERTY_HUMIDITY, oldValue, newValue);
	}

	public void setMagnetometer(final double x, final double y, final double z) {
		final Point3D newValue = new Point3D(x, y, z);
		final Point3D oldValue = magnetometer;
		magnetometer = newValue;
		changeSupport.firePropertyChange(PROPERTY_MAGNETOMETER, oldValue, newValue);
	}

	public void setSimpleKeysStatus(final SimpleKeysStatus newValue) {
		final SimpleKeysStatus oldValue = status;
		status = newValue;
		changeSupport.firePropertyChange(PROPERTY_SIMPLE_KEYS, oldValue, newValue);
	}

	public void setTargetTemperature(final double newValue) {
		final double oldValue = irTemperature;
		irTemperature = newValue;
		changeSupport.firePropertyChange(PROPERTY_IR_TEMPERATURE, oldValue, newValue);
	}
}
