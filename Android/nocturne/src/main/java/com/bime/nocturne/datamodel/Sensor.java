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
package com.projectnocturne.datamodel;

import com.google.gson.annotations.SerializedName;

import org.joda.time.DateTime;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public final class Sensor extends RealmObject {

    private String sensor_desc;
    private String sensor_name;
    @PrimaryKey
    @SerializedName("id")
    private String uniqueId = "";
    private String createdStr;
    private String lastupdatedStr;

    public Sensor() {
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

    public String getSensor_desc() {
        return sensor_desc;
    }

    public void setSensor_desc(final String pSensor_desc) {
        sensor_desc = pSensor_desc;
    }

    public String getSensor_name() {
        return sensor_name;
    }

    public void setSensor_name(final String pSensor_name) {
        sensor_name = pSensor_name;
    }
}
