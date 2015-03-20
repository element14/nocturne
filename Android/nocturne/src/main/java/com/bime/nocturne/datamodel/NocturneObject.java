package com.bime.nocturne.datamodel;

import org.joda.time.DateTime;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by aspela on 20/03/15.
 */
public class NocturneObject extends RealmObject {

    @PrimaryKey
    protected int uniqueId;
    protected DateTime created;
    protected DateTime lastupdated;
}
