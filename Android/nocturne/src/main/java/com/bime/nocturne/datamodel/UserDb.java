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

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

//if ignoreUnknown is false, Jackson would throw an exception if we don't parse all fields
public class UserDb extends RealmObject {

    @PrimaryKey
    @SerializedName("id")
    private String uniqueId = "";
    private String createdStr;
    private String lastupdatedStr;
    private String addrLine1 = "";
    private String addrLine2 = "";
    private String addrLine3 = "";
    private String email1 = "";
    private String nameFirst = "";
    private String nameLast = "";
    private String phoneHome = "";
    private String phoneMbl = "";
    private String postcode = "";
    private String status = "";
    private RealmList<UserCondition> conditions;
    private RealmList<UserConnect> connections;

    public UserDb() {
        super();
    }

    public UserDb(@NonNull final String username) {
        super();
        this.email1 = username;
        this.uniqueId = username;
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

    /**
     * @return the addrLine1
     */
    public String getAddrLine1() {
        return addrLine1;
    }

    /**
     * @param addrLine1 the addrLine1 to set
     */
    public void setAddrLine1(final String addrLine1) {
        this.addrLine1 = addrLine1;
    }

    /**
     * @return the addrLine2
     */
    public String getAddrLine2() {
        return addrLine2;
    }

    /**
     * @param addrLine2 the addrLine2 to set
     */
    public void setAddrLine2(final String addrLine2) {
        this.addrLine2 = addrLine2;
    }

    /**
     * @return the addrLine3
     */
    public String getAddrLine3() {
        return addrLine3;
    }

    /**
     * @param addrLine3 the addrLine3 to set
     */
    public void setAddrLine3(final String addrLine3) {
        this.addrLine3 = addrLine3;
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
     * @return the nameFirst
     */
    public String getNameFirst() {
        return nameFirst;
    }

    /**
     * @param nameFirst the nameFirst to set
     */
    public void setNameFirst(final String nameFirst) {
        this.nameFirst = nameFirst;
    }

    /**
     * @return the nameLast
     */
    public String getNameLast() {
        return nameLast;
    }

    /**
     * @param nameLast the nameLast to set
     */
    public void setNameLast(final String nameLast) {
        this.nameLast = nameLast;
    }

    /**
     * @return the phoneHome
     */
    public String getPhoneHome() {
        return phoneHome;
    }

    /**
     * @param phoneHome the phoneHome to set
     */
    public void setPhoneHome(final String phoneHome) {
        this.phoneHome = phoneHome;
    }

    /**
     * @return the phoneMbl
     */
    public String getPhoneMbl() {
        return phoneMbl;
    }

    /**
     * @param phoneMbl the phoneMbl to set
     */
    public void setPhoneMbl(final String phoneMbl) {
        this.phoneMbl = phoneMbl;
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

    public RealmList<UserCondition> getConditions() {
        return conditions;
    }

    public void setConditions(final RealmList<UserCondition> pConditions) {
        conditions = pConditions;
    }

    public RealmList<UserConnect> getConnections() {
        return connections;
    }

    public void setConnections(final RealmList<UserConnect> pConnections) {
        connections = pConnections;
    }
}
