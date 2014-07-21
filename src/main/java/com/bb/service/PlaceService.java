package com.bb.service;

import com.bb.dal.entity.Address;
import com.bb.dal.entity.Place;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: belozovs
 * Date: 7/17/14
 * Time: 2:24 PM
 * To change this template use File | Settings | File Templates.
 */
public interface PlaceService {

    List<Place> getAllPlaces();
    Place getPlaceByAlias(String alias);

    Place addPlace(Place place) throws PlaceServiceException;
    Place updatePlace(Place place) throws PlaceServiceException;

    Place delete(String alias);

}
