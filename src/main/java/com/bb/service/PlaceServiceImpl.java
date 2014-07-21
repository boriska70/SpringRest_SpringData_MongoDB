package com.bb.service;

import com.bb.dal.dao.PlaceRepository;
import com.bb.dal.entity.Address;
import com.bb.dal.entity.Place;
import com.bb.rest.dto.PlaceDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * User: belozovs
 * Date: 7/17/14
 * Description
 */

@Service
public class PlaceServiceImpl implements PlaceService {

    private final static Logger logger = LoggerFactory.getLogger(PlaceServiceImpl.class);

    @Autowired
    ReloadableResourceBundleMessageSource resourceBundle;

    @Autowired
    private PlaceRepository placeRepository;

    @Override
    public List<Place> getAllPlaces() {
        List<Place> allPlaces = (List<Place>) placeRepository.findAll();
        if (allPlaces != null && !allPlaces.isEmpty()) {
            logger.info("Found {} places in database", allPlaces.size());
            return allPlaces;
        }
        else {
            logger.info("Places collection is empty");
            return new ArrayList<Place>();
        }
    }

    @Override
    public Place getPlaceByAlias(String alias) {
        Place place = placeRepository.findByAlias(alias);
        if (place != null) {
            logger.info("Found place by alias {}: {}", alias, place);
        }
        else {
            logger.info("Place with alias {} not found", alias);
        }
        return place;
    }

    @Override
    public Place addPlace(Place place) throws PlaceServiceException {

        Place oldPlace = placeRepository.findByAlias(place.getAlias());
        if (oldPlace == null) {
            Place newPlace = placeRepository.save(place);
            logger.info("Place added to database: {}", place);
            return newPlace;
        }
        else {
            logger.error("Duplicate place when adding place {}", place);
            throw new PlaceServiceException(resourceBundle.getMessage("duplicate.place", null, LocaleContextHolder.getLocale()) + place);
        }
    }

    @Override
    public Place updatePlace(Place place) throws PlaceServiceException {
        Place oldPlace = placeRepository.findByAlias(place.getAlias());

        if (oldPlace != null) {
            Place newPlace = placeRepository.save(place);
            logger.info("Place updated in database: {}", place);
            return newPlace;
        }
        else {
            logger.error("Cannot update place with alias {} as it does not exist", place.getAlias());
            throw new PlaceServiceException(resourceBundle.getMessage("cannot.update.place", null, LocaleContextHolder.getLocale()) + place.getAlias());
        }

    }

    @Override
    public Place delete(String alias) {
        Place place = placeRepository.findByAlias(alias);
        if(place!=null){
            placeRepository.delete(place);
        }
        return place;
    }
}
