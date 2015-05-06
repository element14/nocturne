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

public final class UserConnect extends RealmObject {

    @PrimaryKey
    @SerializedName("id")
    private String uniqueId = "";
    private DateTime created;
    private DateTime lastupdated;
    private String user1_email;
    private String user2_email;
    private String user1_role;
    private String user2_role;
    private String status;

    public UserConnect() {
    }

    public DateTime getCreated() {
        return created;
    }

    public void setCreated(DateTime created) {
        this.created = created;
    }

    public DateTime getLastupdated() {
        return lastupdated;
    }

    public void setLastupdated(DateTime lastupdated) {
        this.lastupdated = lastupdated;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getUser1_email() {
        return user1_email;
    }

    public void setUser1_email(String user1_email) {
        this.user1_email = user1_email;
    }

    public String getUser2_email() {
        return user2_email;
    }

    public void setUser2_email(String user2_email) {
        this.user2_email = user2_email;
    }

    public String getUser1_role() {
        return user1_role;
    }

    public void setUser1_role(String user1_role) {
        this.user1_role = user1_role;
    }

    public String getUser2_role() {
        return user2_role;
    }

    public void setUser2_role(String user2_role) {
        this.user2_role = user2_role;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
