/********************************************************************egg***m******a**************n************
 * File: Address.java
 * Course materials (19W) CST 8277
 * @author Vaibhav Jain and shadi al khalil
 * (Modified) @date 2019 28
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
 *
 */
package com.algonquincollege.cst8277.models;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * Simple Address class - it uses a generated Id.
 */
@Entity
@NamedQuery(name="Address.findAll", query="SELECT a FROM Address a order by a.id")

@Table
public class Address extends ModelBase implements Serializable {
    /** explicit set serialVersionUID */
    private static final long serialVersionUID = 1L;

    // TODO - additional persistent field
    
    /**
     * below annotation is for one to one relation which mapped by address entity with employee entity 
     */
    @OneToOne(mappedBy = "address")
    private Employee employee;
//    fields of address that will be set corresponding to Employee
    private String city;
    private String country;
    private String postal;
    private String state;
    public Address(Employee employee, String city, String country, String postal, String state, String street) {
        super();
        this.employee = employee;
        this.city = city;
        this.country = country;
        this.postal = postal;
        this.state = state;
        this.street = street;
    }
// getter and setter for employee class instance
    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    private String street;

 // getter and setter for city
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
 // CREATE TABLE ADDRESS (ID INTEGER IDENTITY NOT NULL, CITY VARCHAR, COUNTRY VARCHAR, POSTAL VARCHAR, STATE VARCHAR, STREET VARCHAR, VERSION INTEGER, PRIMARY KEY (ID))
 // getter and setter for country
    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
 // getter and setter for postal code
     public String getPostal() {
        return postal;
    }
     
    public void setPostal(String postal) {
        this.postal = postal;
    }
 // getter and setter for state
     public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
 // getter and setter for street
     public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    // JPA requires each @Entity class have a default constructor
    public Address() {
        super();
    }

    // Strictly speaking, JPA does not require hashcode() and equals(),
    // but it is a good idea to have one that tests using the PK (@Id) field

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Address)) {
            return false;
        }
        Address other = (Address)obj;
        if (id != other.id) {
            return false;
        }
        return true;
    }

}