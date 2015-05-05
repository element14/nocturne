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

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import org.joda.time.DateTime;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

//if ignoreUnknown is false, Jackson would throw an exception if we don't parse all fields
public final class User extends RealmObject {

    @PrimaryKey
    @SerializedName("id")
    private String uniqueId = "";
    private String createdStr;
    private String lastupdatedStr;

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

    public DateTime getCreated() {
        return DateTime.parse(createdStr);
    }

    public void setCreated(DateTime created) {
        this.createdStr = created.toString();
    }

    public DateTime getLastupdated() {
        return DateTime.parse(lastupdatedStr);
    }

    public void setLastupdated(DateTime lastupdated) {
        this.lastupdatedStr = lastupdated.toString();
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }




    private String addr_line1 = "";
    private String addr_line2 = "";
    private String addr_line3 = "";
    private String email1 = "";
    private String name_first = "";
    private String name_last = "";
    private String phone_home = "";
    private String phone_mbl = "";
    private String postcode = "";
    private String status = "";
    
    private RealmList<UserCondition> conditions;
    private RealmList<UserConnect> connections;

    public User() {
        super();
    }

    public User(@NonNull final String username) {
        super();
        this.email1 = username;
        this.uniqueId = username;
    }

    /**
     * @return the addr_line1
     */
    public String getAddr_line1() {
        return addr_line1;
    }

    /**
     * @param addr_line1 the addr_line1 to set
     */
    public void setAddr_line1(final String addr_line1) {
        this.addr_line1 = addr_line1;
    }

    /**
     * @return the addr_line2
     */
    public String getAddr_line2() {
        return addr_line2;
    }

    /**
     * @param addr_line2 the addr_line2 to set
     */
    public void setAddr_line2(final String addr_line2) {
        this.addr_line2 = addr_line2;
    }

    /**
     * @return the addr_line3
     */
    public String getAddr_line3() {
        return addr_line3;
    }

    /**
     * @param addr_line3 the addr_line3 to set
     */
    public void setAddr_line3(final String addr_line3) {
        this.addr_line3 = addr_line3;
    }

    /**
     * @return the email1
     */
    public String getEmail1() {
        return email1;
    }

    /**
     * @param email1 the email1 to set
     */
    public void setEmail1(final String email1) {
        this.email1 = email1;
    }

    /**
     * @return the name_first
     */
    public String getName_first() {
        return name_first;
    }

    /**
     * @param name_first the name_first to set
     */
    public void setName_first(final String name_first) {
        this.name_first = name_first;
    }

    /**
     * @return the name_last
     */
    public String getName_last() {
        return name_last;
    }

    /**
     * @param name_last the name_last to set
     */
    public void setName_last(final String name_last) {
        this.name_last = name_last;
    }

    /**
     * @return the phone_home
     */
    public String getPhone_home() {
        return phone_home;
    }

    /**
     * @param phone_home the phone_home to set
     */
    public void setPhone_home(final String phone_home) {
        this.phone_home = phone_home;
    }

    /**
     * @return the phone_mbl
     */
    public String getPhone_mbl() {
        return phone_mbl;
    }

    /**
     * @param phone_mbl the phone_mbl to set
     */
    public void setPhone_mbl(final String phone_mbl) {
        this.phone_mbl = phone_mbl;
    }

    /**
     * @return the postcode
     */
    public String getPostcode() {
        return postcode;
    }

    /**
     * @param postcode the postcode to set
     */
    public void setPostcode(final String postcode) {
        this.postcode = postcode;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(final String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email1 + '\'' +
                ", name_first='" + name_first + '\'' +
                ", name_last='" + name_last + '\'' +
                '}';
    }
}
