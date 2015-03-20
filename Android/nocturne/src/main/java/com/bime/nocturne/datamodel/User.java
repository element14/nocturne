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

import android.database.Cursor;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

//if ignoreUnknown is false, Jackson would throw an exception if we don't parse all fields
@JsonIgnoreProperties(ignoreUnknown = true)
public final class User {

    @JsonProperty("addr_line1")
    protected String addr_line1 = "";

    @JsonProperty("addr_line2")
    protected String addr_line2 = "";

    @JsonProperty("addr_line3")
    protected String addr_line3 = "";

    @JsonProperty("email1")
    protected String email1 = "";

    @JsonProperty("name_first")
    protected String name_first = "";

    @JsonProperty("name_last")
    protected String name_last = "";

    @JsonProperty("phone_home")
    protected String phone_home = "";

    @JsonProperty("phone_mbl")
    protected String phone_mbl = "";

    @JsonProperty("postcode")
    protected String postcode = "";

    @JsonProperty("status")
    protected String status = "";

    @JsonProperty("username")
    protected String username = "";

    public User() {
        super();
    }

    /**
     * @param results
     */
    public User(final Cursor results) {
        username = results.getString(results.getColumnIndex(UserDb.FIELD_NAME_USERNAME));
        status = results.getString(results.getColumnIndex(UserDb.FIELD_NAME_STATUS));
        name_first = results.getString(results.getColumnIndex(UserDb.FIELD_NAME_name_first));
        name_last = results.getString(results.getColumnIndex(UserDb.FIELD_NAME_name_last));
        email1 = results.getString(results.getColumnIndex(UserDb.FIELD_NAME_email1));
        phone_mbl = results.getString(results.getColumnIndex(UserDb.FIELD_NAME_phone_mbl));
        phone_home = results.getString(results.getColumnIndex(UserDb.FIELD_NAME_phone_mbl));
        addr_line1 = results.getString(results.getColumnIndex(UserDb.FIELD_NAME_addr_line1));
        addr_line2 = results.getString(results.getColumnIndex(UserDb.FIELD_NAME_addr_line2));
        addr_line3 = results.getString(results.getColumnIndex(UserDb.FIELD_NAME_addr_line3));
        postcode = results.getString(results.getColumnIndex(UserDb.FIELD_NAME_postcode));
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

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(final String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return null;
    }

}
