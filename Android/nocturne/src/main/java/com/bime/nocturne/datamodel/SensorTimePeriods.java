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
 * </p><p>
 * <b><i>Copyright 2013-2014 Bath Institute of Medical Engineering.</i></b>
 * --------------------------------------------------------------------------
 */
package com.bime.nocturne.datamodel;


import com.google.gson.annotations.SerializedName;

import org.joda.time.DateTime;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class SensorTimePeriods extends RealmObject {

    @PrimaryKey
    @SerializedName("id")
    private String uniqueId = "";
    private String createdStr;
    private String lastupdatedStr;
    private String sensor_alert_time;
    private long sensor_id;
    private String sensor_value_expected;
    private String sensor_warn_time;
    private String start_time;
    private String stop_time;

    public SensorTimePeriods() {
    }

    public String getCreatedStr() {
        return createdStr;
    }
    public void setCreatedStr(String createdStr) {
        this.createdStr = createdStr;
    }

    public String getLastupdatedStr() {
        return lastupdatedStr;
    }
    public void setLastupdatedStr(String lastupdatedStr) {
        this.lastupdatedStr = lastupdatedStr;
    }

//    public DateTime getCreated() {
//        return DateTime.parse(createdStr);
//    }
//    public void setCreated(DateTime created) {
//        this.createdStr = created.toString();
//    }
//
//    public DateTime getLastupdated() {
//        return DateTime.parse(lastupdatedStr);
//    }
//    public void setLastupdated(DateTime lastupdated) {
//        this.lastupdatedStr = lastupdated.toString();
//    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getSensor_alert_time() {
        return sensor_alert_time;
    }

    public void setSensor_alert_time(final String pSensor_alert_time) {
        sensor_alert_time = pSensor_alert_time;
    }

    public long getSensor_id() {
        return sensor_id;
    }

    public void setSensor_id(final long pSensor_id) {
        sensor_id = pSensor_id;
    }

    public String getSensor_value_expected() {
        return sensor_value_expected;
    }

    public void setSensor_value_expected(final String pSensor_value_expected) {
        sensor_value_expected = pSensor_value_expected;
    }

    public String getSensor_warn_time() {
        return sensor_warn_time;
    }

    public void setSensor_warn_time(final String pSensor_warn_time) {
        sensor_warn_time = pSensor_warn_time;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(final String pStart_time) {
        start_time = pStart_time;
    }

    public String getStop_time() {
        return stop_time;
    }

    public void setStop_time(final String pStop_time) {
        stop_time = pStop_time;
    }
}
