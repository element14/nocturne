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

/**
 * @author andy
 */
public class DbMetadata extends RealmObject {

    //public static final int RegistrationStatus_ACCEPTED = 63353;
    //public static final int RegistrationStatus_DENIED = 63354;
    private static final String LOG_TAG = DbMetadata.class.getSimpleName() + "::";
    private int registrationStatus = 0;
    private long timestamp = 0;
    private String version = "";
    @PrimaryKey
    @SerializedName("id")
    private String uniqueId = "";
    private String createdStr;
    private String lastupdatedStr;

    public DbMetadata() {
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

//    public DateTime getCreated() {return DateTime.parse(createdStr);}
//    public void setCreated(DateTime created) {this.createdStr = created.toString();}
//    public DateTime getLastupdated() {return DateTime.parse(lastupdatedStr);}
//    public void setLastupdated(DateTime lastupdated) {this.lastupdatedStr = lastupdated.toString();}

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public void setRegistrationStatus(final int aRegStatus) {
        registrationStatus = aRegStatus;
    }
 public int getRegistrationStatus() {        return registrationStatus;    }


    public enum UserConnectionStatus {
        REQUEST_ACCEPTED, REQUEST_DENIED, REQUEST_SENT;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(final long pTimestamp) {
        timestamp = pTimestamp;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(final String pVersion) {
        version = pVersion;
    }
}
