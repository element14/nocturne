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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class User {

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
    private List<UserCondition> conditions;
    private List<UserConnect> connections;

    public static User fromDbObj(final UserDb pUserObj) {
        User u = new User();
        u.uniqueId = pUserObj.getUniqueId();
        u.createdStr = pUserObj.getCreatedStr();
        u.lastupdatedStr = pUserObj.getLastupdatedStr();
        u.addrLine1 = pUserObj.getAddrLine1();
        u.addrLine2 = pUserObj.getAddrLine2();
        u.addrLine3 = pUserObj.getAddrLine3();
        u.email1 = pUserObj.getEmail1();
        u.nameFirst = pUserObj.getNameFirst();
        u.nameLast = pUserObj.getNameLast();
        u.phoneHome = pUserObj.getPhoneHome();
        u.phoneMbl = pUserObj.getPhoneMbl();
        u.postcode = pUserObj.getPostcode();
        u.status = pUserObj.getStatus();
        return u;
    }

    public static JSONObject getJsonObj(final UserDb pUserObj) {
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("username", pUserObj.getEmail1());
            jsonObject.put("addr_line1", pUserObj.getAddrLine1());
            jsonObject.put("addr_line2", pUserObj.getAddrLine2());
            jsonObject.put("addr_line3", pUserObj.getAddrLine3());
            jsonObject.put("email", pUserObj.getEmail1());
            jsonObject.put("name_last", pUserObj.getNameFirst());
            jsonObject.put("name_first", pUserObj.getNameLast());
            jsonObject.put("phone_home", pUserObj.getPhoneHome());
            jsonObject.put("phone_mbl", pUserObj.getPhoneMbl());
            jsonObject.put("postcode", pUserObj.getPostcode());
            jsonObject.put("status", pUserObj.getStatus());
        } catch (JSONException e) {
            // handle exception
        }
        return jsonObject;
    }
}
