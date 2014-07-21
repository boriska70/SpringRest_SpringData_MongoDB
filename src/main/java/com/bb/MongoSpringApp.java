package com.bb;

import com.bb.dal.entity.Place;
import com.mongodb.Mongo;
import com.mongodb.ServerAddress;
import org.apache.log4j.Logger;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;
import static org.springframework.data.mongodb.core.query.Update.update;

/**
 * User: belozovs
 * Date: 7/13/14
 * Description
 */
public class MongoSpringApp {

    private final static Logger logger = Logger.getLogger(MongoSpringApp.class);

    private static final String dbHost = "localhost";
    private static final int dbPort = 27017;
    private static final String dbName = "sightseeing";
    private static final String collectionName = "places";

    public static void main(String[] args) throws UnknownHostException {


        MongoOperations mongoOperations = new MongoTemplate(new SimpleMongoDbFactory(new Mongo(new ServerAddress(dbHost, dbPort)), dbName));

        Place place = new Place();
        place.setName("place1");
        place.setAlias("myplace1alias");
        place.setNotes("notes for place 1");

        mongoOperations.insert(place);
        System.out.println("Insert: "+place);

        place = mongoOperations.findById(place.getId(),Place.class);
        System.out.println("Found: "+place);

        mongoOperations.updateFirst(query(where("name").is("place1")), update("alias", "my place1 alias"), Place.class);
        place = mongoOperations.findOne(query(where("name").is("place1")), Place.class);
        System.out.println("Updated: " + place);

        mongoOperations.remove(place);

        List<Place> allPlaces =  mongoOperations.findAll(Place.class);
        System.out.println("Number of places = : " + allPlaces.size());

        Iterator<String> itCollections = mongoOperations.getCollectionNames().iterator();
        while (itCollections.hasNext()){
            System.out.println("Collection found: "+itCollections.next());
        }


        mongoOperations.dropCollection(collectionName);



    }



}
