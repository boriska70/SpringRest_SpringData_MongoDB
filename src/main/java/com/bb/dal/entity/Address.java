package com.bb.dal.entity;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;

/**
 * User: belozovs
 * Date: 7/17/14
 * Description
 */
@Document
public class Address {

    @Id
    private ObjectId id;
    private String alias;
    private String address;
    private String metro;
    private String comment;

    public Address() {
        id = new ObjectId();
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMetro() {
        return metro;
    }

    public void setMetro(String metro) {
        this.metro = metro;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Address address = (Address) o;

        if (!alias.equals(address.alias)) {
            return false;
        }
        if (id != null ? !id.equals(address.id) : address.id != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + alias.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Address [id = "+id+", alias = "+ alias+", address = "+address+", metro = "+ metro+", comment = "+ comment+"]";
    }
}
