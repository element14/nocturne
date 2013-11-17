/**
 * 
 * Copyright Notice
 *  ----------------
 *
 * The copyright in this document is the property of 
 * Bath Institute of Medical Engineering.
 *
 * Without the written consent of Bath Institute of Medical Engineering
 * given by Contract or otherwise the document must not be copied, reprinted or
 * reproduced in any material form, either wholly or in part, and the contents
 * of the document or any method or technique available there from, must not be
 * disclosed to any other person whomsoever.
 * 
 *  Copyright 2013-2014 Bath Institute of Medical Engineering.
 * --------------------------------------------------------------------------
 * 
 */
package com.projectnocturne.datamodel;

import java.util.Observable;

/**
 * this class is the base class for all my data models. this is used so that an
 * oberserver can be informed of a change to the model rather than of a change
 * to an individual data item.
 * 
 * @author andy
 */
public abstract class AbstractDataModel extends Observable {

} // class()

