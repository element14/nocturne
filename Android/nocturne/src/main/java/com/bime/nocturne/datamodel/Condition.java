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

public final class Condition extends RealmObject {

    private String condition_desc;
    private String condition_name;
    @PrimaryKey
    @SerializedName("id")
    private String uniqueId = "";
    private String createdStr;
    private String lastupdatedStr;

    public Condition() {
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

    public String getCondition_desc() {
        return condition_desc;
    }

    public void setCondition_desc(final String pCondition_desc) {
        condition_desc = pCondition_desc;
    }

    public String getCondition_name() {
        return condition_name;
    }

    public void setCondition_name(final String pCondition_name) {
        condition_name = pCondition_name;
    }
}
