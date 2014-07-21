package com.bb.rest.dto;




import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * User: belozovs
 * Date: 7/17/14
 * Description
 *
 *
 * {
 "name": "place3",
 "notes": "place3 notes",
 "addressDTOs": [
 {
 "address": "Street 3, 2",
 "metro": "Center-3",
 "comment": "My first address"
 }
 ]
 }
 *
 */
public class PlaceDTO implements SightSeeingDtoInterface{

    private final static Logger logger = LoggerFactory.getLogger(PlaceDTO.class);

    private String alias;
    private String name;
    private String notes;
    private List<AddressDTO> addressDTOs;

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public List<AddressDTO> getAddressDTOs() {
        return addressDTOs;
    }

    public void setAddressDTOs(List<AddressDTO> addressDTOs) {
        this.addressDTOs = addressDTOs;
    }

    @Override
    public String toString() {

        StringBuffer buffer = new StringBuffer();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("alias", alias);
            jsonObject.put("name", name);
            jsonObject.put("notes", notes);
            jsonObject.put("addresses", addressDTOs);
        } catch (JSONException e) {
            logger.error("Error occurred while creating PlaceDTO json");
        }

        return jsonObject.toString();
    }
}
