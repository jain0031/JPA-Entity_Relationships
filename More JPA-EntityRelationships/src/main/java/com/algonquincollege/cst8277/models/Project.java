/********************************************************************egg***m******a**************n************
 * File: Project.java
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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.JoinColumn;

/**
* File: Project.java
* Date Created: March 24, 2019
* @author Vaibhav Jain, Shadi Al Khalil 
*/

/**
 * The Project class demonstrates:
 * <ul>
 * The class become as entity for JPA by using @Entity annotation to mape it to
 * the project table in the db
 * <li>Generated Id
 * <li>Version locking
 * <li>ManyToMany mapping
 * </ul>
 */
@Entity
public class Project extends ModelBase implements Serializable {
    /** explicit set serialVersionUID */
    private static final long serialVersionUID = 1L;

    // since the relationship with Employee Entity is N:M we use @ManyToMany
    // annotation and it mapes the lise
    // to the projects list property in Employee entity

    @ManyToMany(mappedBy = "projects")
    private List<Employee> employees = new ArrayList<>();

    @Override
    public String toString() {
        return "Project [employees=" + employees + ", description=" + description + ", name=" + name + "]";
    }

    // properties of this class
    private String description;
    private String name;

// getter employees where one or many employees can resides in a list
    public List<Employee> getEmployees() {
        return employees;
    }

// setter for employees
    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }

// getter for description
    public String getDescription() {
        return description;
    }

// setter for description
    public void setDescription(String description) {
        this.description = description;
    }

// getter of name of the project
    public String getName() {
        return name;
    }

// setter of name
    public void setName(String name) {
        this.name = name;
    }

    // JPA requires each @Entity class have a default constructor
    public Project() {
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
        if (!(obj instanceof Project)) {
            return false;
        }
        Project other = (Project) obj;
        if (id != other.id) {
            return false;
        }
        return true;
    }

}