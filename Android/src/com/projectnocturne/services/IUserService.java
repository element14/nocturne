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

package com.projectnocturne.services;

import java.util.ArrayList;

import com.projectnocturne.datamodel.User;

/**
 * @author aspela
 */
public interface IUserService {

	public boolean createUser(User user);

	public boolean deleteUser(String email1);

	public ArrayList getAllUsers();

	public User getUser(String email1);

	public User updateUser(User updatedUser);
}
