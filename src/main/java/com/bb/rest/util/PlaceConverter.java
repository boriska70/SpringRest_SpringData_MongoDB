package com.bb.rest.util;

import com.bb.dal.entity.Address;
import com.bb.dal.entity.Place;
import com.bb.rest.dto.AddressDTO;
import com.bb.rest.dto.PlaceDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * User: belozovs
 * Date: 7/17/14
 * Description
 */
@Component
public class PlaceConverter {

    public PlaceDTO createDto(Place place, boolean showAddresses) {
        PlaceDTO placeDTO = new PlaceDTO();
        placeDTO.setName(place.getName());
        placeDTO.setAlias(place.getAlias());
        placeDTO.setNotes(place.getNotes());
        if (showAddresses) {
            List<AddressDTO> addressDTOList = new ArrayList<AddressDTO>();
            List<Address> addressList = place.getAddresses();
            if (addressList != null && !addressList.isEmpty()) {
                for (Address address : addressList) {
                    AddressDTO addressDTO = new AddressDTO();
                    addressDTO.setAddressAlias(address.getAlias());
                    addressDTO.setMetro(address.getMetro());
                    addressDTO.setAddress(address.getAddress());
                    addressDTO.setComment(address.getComment());
                    addressDTOList.add(addressDTO);
                }
            }
            placeDTO.setAddressDTOs(addressDTOList);
        }
        return placeDTO;
    }

    public Place createDocument(PlaceDTO placeDTO) {
        Place place = new Place();
        place.setName(placeDTO.getName());
        place.setAlias(placeDTO.getAlias());
        place.setNotes(placeDTO.getNotes());
        List<Address> addressList = new ArrayList<Address>();
        List<AddressDTO> addressDTOList = placeDTO.getAddressDTOs();
        if (addressDTOList != null && !addressDTOList.isEmpty()) {
            for (AddressDTO addressDTO : addressDTOList) {
                Address address = new Address();
                address.setAlias(addressDTO.getAddressAlias());
                address.setAddress(addressDTO.getAddress());
                address.setMetro(addressDTO.getMetro());
                address.setComment(addressDTO.getComment());
                addressList.add(address);
            }
        }
        place.setAddresses(addressList);
        return place;
    }

}
