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

/**
 * @author andy
 */
public class DbMetadata extends NocturneObject {
    public static final int RegistrationStatus_ACCEPTED = 63353;
    public static final int RegistrationStatus_DENIED = 63354;
    private static final String LOG_TAG = DbMetadata.class.getSimpleName() + "::";
    public RegistrationStatus registrationStatus = RegistrationStatus.NOT_STARTED;
    public long timestamp = 0;
    public String version = "";

    public DbMetadata() {
    }

    public void setRegistrationStatus(final int newValue) {
        switch (newValue) {
            case 0:
                registrationStatus = RegistrationStatus.NOT_STARTED;
                break;
            case 1:
                registrationStatus = RegistrationStatus.REQUEST_ACCEPTED;
                break;
            case 2:
                registrationStatus = RegistrationStatus.REQUEST_DENIED;
                break;
            case 3:
                registrationStatus = RegistrationStatus.REQUEST_SENT;
                break;
            default:
                registrationStatus = RegistrationStatus.NOT_STARTED;
        }
    }

    public void setRegistrationStatus(final RegistrationStatus aRegStatus) {
        registrationStatus = aRegStatus;
    }

    @Override
    public String toString() {
        return null;
    }

    public enum RegistrationStatus {
        NOT_STARTED, REQUEST_ACCEPTED, REQUEST_DENIED, REQUEST_SENT;
    }

    public enum UserConnectionStatus {
        REQUEST_ACCEPTED, REQUEST_DENIED, REQUEST_SENT;
    }

}
