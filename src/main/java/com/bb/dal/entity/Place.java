package com.bb.dal.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.swing.*;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * User: belozovs
 * Date: 7/13/14
 * Description
 */
@Document(collection = "places")
public class Place {

    @Id
    private String id;
    @NotNull
    @Indexed(unique = true)
    private String name;
    @NotNull
    @Indexed(unique = true)
    private String alias;
    private String notes;
    List<Address> addresses;


    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAlias() {
        return alias;
    }

    public String getNotes() {
        return notes;
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

    @Override
    public String toString() {
        return "Place [id = "+id+", name = "+ name+", alias = "+ alias+", notes = "+notes+", addresses = " + addresses + "]";
    }


}
