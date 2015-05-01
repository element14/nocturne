package com.bime.nocturne.datamodel;

import com.google.gson.annotations.SerializedName;

import org.joda.time.DateTime;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by aspela on 20/03/15.
 */
public class NocturneObject extends RealmObject {

    @PrimaryKey
    @SerializedName("id")
    protected String uniqueId = "";
    protected DateTime created;
    protected DateTime lastupdated;

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
}
