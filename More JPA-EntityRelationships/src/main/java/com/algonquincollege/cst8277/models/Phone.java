/********************************************************************egg***m******a**************n************
 * File: Phone.java
 * Course materials (19W) CST 8277
 * @author Mike Norman
 * (Modified) @date 2019 03
 *
 * Copyright (c) 1998, 2009 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 and Eclipse Distribution License v. 1.0
 * which accompanies this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Original @authors dclarke, mbraeuer
 */
package com.algonquincollege.cst8277.models;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 * The Phone class demonstrates:
 * <ul>
 * <li>Generated Id
 * <li>Version locking
 * <li>ManyToOne mapping
 * </ul>
 */
/**
* File: Phone.java
* Date Created: March 24, 2019
* @author Vaibhav Jain, Shadi Al Khalil 
*/

/**
 * 
 * making this class as an entity of phone for jpa to use it to map its properties to the database
 *
 */
@Entity
public class Phone extends ModelBase implements Serializable {
    /** explicit set serialVersionUID */
    private static final long serialVersionUID = 1L;

    // The foriegn key in database phone table is a join column in jpa with relationship many phones to one emolpyee
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="owning_emp_id")
    private Employee employee;
    
    

    

    // phone properties and the id and version are derived from the base class
    private String areaCode;
    private String phoneNumber;

    // getter of the private property areaCode
     public String getAreaCode() {
        return areaCode;
    }
// setter of areaCode
    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }
// getter of phoneNumber
     public String getPhoneNumber() {
        return phoneNumber;
    }
// setter of PhoneNumber
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        return result;
    }


    // getter for emoloyee
    public Employee getEmployee() {
        return employee;
    }
// setter for employee
    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Phone)) {
            return false;
        }
        Phone other = (Phone)obj;
        if (id != other.id) {
            return false;
        }
        return true;
    }
}