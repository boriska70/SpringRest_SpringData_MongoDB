package com.bb.rest.dto;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User: belozovs
 * Date: 7/21/14
 * Description
 */
public class AddressDTO implements SightSeeingDtoInterface{

    private final static Logger logger = LoggerFactory.getLogger(AddressDTO.class);

    private String addressAlias;
    private String address;
    private String metro;
    private String comment;

    public String getAddressAlias() {
        return addressAlias;
    }

    public void setAddressAlias(String addressAlias) {
        this.addressAlias = addressAlias;
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

    StringBuffer buffer = new StringBuffer();

    @Override
    public String toString() {

    JSONObject jsonObject = new JSONObject();
    try {
        jsonObject.put("addressAlias", addressAlias);
        jsonObject.put("address", address);
        jsonObject.put("metro", metro);
        jsonObject.put("comment", comment);
    } catch (JSONException e) {
        logger.error("Error occurred while creating AddressDTO json");
    }

    return jsonObject.toString();
    }
}
