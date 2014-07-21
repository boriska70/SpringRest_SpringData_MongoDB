package com.bb.rest;

import com.bb.dal.entity.Address;
import com.bb.dal.entity.Place;
import com.bb.rest.dto.AddressDTO;
import com.bb.rest.dto.PlaceDTO;
import com.bb.rest.util.PlaceConverter;
import com.bb.rest.util.PlaceDtoValidator;
import com.bb.service.PlaceService;
import com.bb.service.PlaceServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * User: belozovs
 * Date: 7/17/14
 * Description
 * http://xpadro.blogspot.co.il/2014/01/migrating-spring-mvc-restful-web.html
 * https://github.com/xpadro/spring-rest/blob/master/spring-rest-api-v4/
 * http://www.petrikainulainen.net/programming/spring-framework/spring-from-the-trenches-adding-validation-to-a-rest-api/
 */

@RestController
@RequestMapping(value = "places")
public class SightseeingService {

    private static final Logger logger = LoggerFactory.getLogger(SightseeingService.class);

    @Autowired
    PlaceService placeService;
    @Autowired
    PlaceConverter placeConverter;

    @Autowired
    ReloadableResourceBundleMessageSource resourceBundle;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.setValidator(new PlaceDtoValidator());
    }

    @RequestMapping(method = RequestMethod.GET)
    public
    @ResponseBody
    List<PlaceDTO> getAll() {

        List<Place> places = placeService.getAllPlaces();
        List<PlaceDTO> placeDTOs = new ArrayList<PlaceDTO>();

        if (places != null && !places.isEmpty()) {
            for (Place place : places) {
                PlaceDTO placeDTO = placeConverter.createDto(place, true);
                placeDTOs.add(placeDTO);
            }
        }
        return placeDTOs;
    }

    @RequestMapping(value = "/{alias}", method = RequestMethod.GET)
    public ResponseEntity<String> getByAlias(@PathVariable("alias") String alias, @RequestParam(value = "details", required = false) boolean showAddresses) {

        Place place = placeService.getPlaceByAlias(alias);

        if (place != null) {
            logger.info("Returning place dbObject by alias {} (will be converted): {}", alias, place);
            return new ResponseEntity<String>(placeConverter.createDto(place, showAddresses).toString(), HttpStatus.OK);
        }
        else {
            logger.info("Place with alias {} not found, nothing to return", alias);
            return new ResponseEntity<String>("{}", HttpStatus.OK);
        }
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED) //override default 200 or throw exception with RestErrorHandler
    public ResponseEntity addPlace(@Valid @RequestBody PlaceDTO placeDto, HttpServletRequest request, HttpServletResponse response) {

        logger.info("Going to create place {} ", placeDto.getName());
        PlaceDTO myPlaceDto = generateAlias(placeDto, request.getLocale());

        Place place = placeConverter.createDocument(myPlaceDto);

        try {
            Place resultPlace = placeService.addPlace(place);
            logger.info("New place with name {} and alias {} was created successfully", resultPlace.getName(), resultPlace.getAlias());
            return new ResponseEntity(placeConverter.createDto(place, true), HttpStatus.OK);

        } catch (PlaceServiceException e) {
            logger.error("Failed to create place with name {}", placeDto.getName(), e);
            throw e;
        }
    }

    @RequestMapping(value = "/address/{placeAlias}", method = RequestMethod.PUT)
    public ResponseEntity addOrUpdateAddressToPlace(@RequestBody AddressDTO addressDTO, @PathVariable String placeAlias, HttpServletRequest request, HttpServletResponse response) {

        logger.info("Going to update place {} ", placeAlias);
        Place place = placeService.getPlaceByAlias(placeAlias);
        if (place != null) {
            logger.info("Place found for updating: {}", place);
            if (addressDTO.getAddressAlias() == null || addressDTO.getAddressAlias().isEmpty()) {
                addressDTO.setAddressAlias(generateAddressAlias(addressDTO.getAddress()));

            }
            List<Address> addressList = place.getAddresses();
            boolean found = false;
            if (addressList != null && !addressList.isEmpty()) {
                for (Address address : addressList) {
                    if (address.getAlias().equals(addressDTO.getAddressAlias())) {
                        //the same address - updating
                        address.setAddress(addressDTO.getAddress());
                        address.setComment(addressDTO.getComment());
                        address.setMetro(addressDTO.getMetro());
                        found = true;
                        break;
                    }
                }
            }
            if (!found) {
                Address address = new Address();
                address.setAddress(addressDTO.getAddress());
                address.setAlias(addressDTO.getAddressAlias());
                address.setComment(addressDTO.getComment());
                address.setMetro(addressDTO.getMetro());
                addressList.add(address);
            }
            try {
                Place updatedPlace = placeService.updatePlace(place);
                logger.info("Address is updated/added for place: {}", updatedPlace);
                if (!found) {
                    return new ResponseEntity(placeConverter.createDto(place, true), HttpStatus.CREATED);
                }
                else {
                    return new ResponseEntity(placeConverter.createDto(place, true), HttpStatus.OK);
                }

            } catch (PlaceServiceException e) {
                logger.error("Failed to update place {}", place);
                throw e;
            }
        }
        else {
            // no place found
            logger.error("Place {} not found, cannot add/update the address", placeAlias);
            throw new PlaceServiceException(resourceBundle.getMessage("cannot.update.place", null, LocaleContextHolder.getLocale()) + place.getAlias());
        }
    }

    @RequestMapping(value = "/{alias}/address/{addressAlias}", method = RequestMethod.DELETE)
    @ResponseBody
    String delete(@PathVariable("alias") String alias, @PathVariable("addressAlias") String addressAlias) {

        Place place = placeService.getPlaceByAlias(alias);
        if (place != null) {
            List<Address> addressList = place.getAddresses();
            boolean found = false;
            if (addressList != null && !addressList.isEmpty()) {
                for (Address address : addressList) {
                    if (address.getAlias().equals(addressAlias)) {
                        addressList.remove(address);
                        logger.info("Address {} in place {} was found and removed", addressAlias, alias);
                        found = true;
                        break;
                    }
                }
            }
            if (!found) {
                logger.info("Address {} in place {} was not found ", addressAlias, alias);
            }
            else {
                place = placeService.updatePlace(place);
            }
            return placeConverter.createDto(place, true).toString();
        }
        else {
            logger.info("Place with alias {} was not found, not trying to delete address", alias);
            return "{}";
        }

    }

    @RequestMapping(value = "/{alias}", method = RequestMethod.DELETE)
    public
    @ResponseBody
    String delete(@PathVariable("alias") String alias) {

        Place place = placeService.delete(alias);
        if (place != null) {
            logger.info("Place {} has been deleted", place);
            return placeConverter.createDto(place, true).toString();
        }
        else {
            logger.info("Place with alias {} was not found, not trying to delete", alias);
            return "{}";
        }
    }

    private PlaceDTO generateAlias(PlaceDTO placeDTO, Locale locale) {
        logger.info("Generating alias for place with name {} in locale {}", placeDTO.getName(), locale.getDisplayName());
        String myAlias = placeDTO.getName();
        myAlias = myAlias.replaceAll("[^a-zA-Z0-9]", "").toLowerCase(locale);
        logger.info("Alias {} was generated for place named {}", myAlias, placeDTO.getName());
        placeDTO.setAlias(myAlias);

        List<AddressDTO> addressDTOs = placeDTO.getAddressDTOs();
        if (addressDTOs != null && !addressDTOs.isEmpty()) {
            for (AddressDTO addressDTO : addressDTOs) {
                if (addressDTO.getAddressAlias() == null || addressDTO.getAddressAlias().isEmpty()) {
                    addressDTO.setAddressAlias(generateAddressAlias(addressDTO.getAddress()));
                }
            }
        }
        return placeDTO;
    }

    private String generateAddressAlias(String addressName) {
        String addrAlias = addressName.replaceAll("[^a-zA-Z0-9]", "").toLowerCase();
        if (addrAlias.length() > 8) {
            addrAlias = addrAlias.substring(0, 7);
            logger.info("Address alias {} was generated for address {}", addrAlias, addressName);
        }
        return addrAlias;
    }
}
