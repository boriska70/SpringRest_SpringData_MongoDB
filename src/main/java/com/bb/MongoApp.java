package com.bb;

import com.mongodb.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.UnknownHostException;

/**
 * User: belozovs
 * Date: 7/13/14
 * Description
 */
public class MongoApp {

    private static final Logger logger = LoggerFactory.getLogger(com.bb.MongoApp.class);

    private static final String hostName = "localhost";
    private static final int port = 27017;
    private static final String dbName = "test";
    private static final String placeCollection = "place";
    private static final String titleColumnUniqueIndex = "title";


    public static void mainStandAloneApp(String[] args) {

        logger.info("Initializing database");
        MongoClient mongoClient = init();
        if (mongoClient == null) {
            logger.info("Failed to initialize database, cannot continue");
            System.exit(-1);
        }
        DB db = mongoClient.getDB(dbName);
        DBCollection places = db.getCollection(placeCollection);
        int initCounter = places.find().count();
        logger.info("Database {} on {}:{} has been initialized successfully, places in DB: {}", new Object[]{dbName, hostName, port, initCounter});

        BasicDBObject doc = new BasicDBObject("title", "MongoDB").
                append("description", "database").
                append("likes", 100).
                append("url", "http://www.tutorialspoint.com/mongodb/").
                append("by", "tutorials point");

        WriteResult insertResult = insert(places, doc);
        if(insertResult == null){
            logger.error("Failed to insert!!!! Still have {} documents in {}", places.count(), placeCollection);
        } else {
            logger.error("Inserted! There are {} documents in {}", places.count(), placeCollection);
        }

        DBCursor allPlaces = places.find();
        System.out.println("Places in cursor: " + allPlaces.count());
        while (allPlaces.hasNext()) {
            System.out.println(allPlaces.next());
        }
        System.out.println("### Done ###");

    }

    private static final MongoClient init() {
        try {
            MongoClient mongoClient = new MongoClient(new ServerAddress(hostName, port));
            logger.info("MongoDB Client connected to {}:{}", hostName, port);
            DB db = mongoClient.getDB(dbName);
            logger.info("MongoDB database {} is ready for usage");
            DBCollection places = db.createCollection(placeCollection, null);
            logger.info("Collection {} is ready for usage", placeCollection);
            places.ensureIndex(new BasicDBObject(titleColumnUniqueIndex, 1), "_unique_title_index", true);
            logger.info("Unique index for {} is applied to collection {}", titleColumnUniqueIndex, placeCollection);
            return mongoClient;
        } catch (UnknownHostException uhe) {
            logger.error(uhe.getLocalizedMessage(), uhe);
            return null;
        } catch (MongoException me) {
            logger.error(me.getLocalizedMessage(), me);
            return null;
        }
    }

    private static final WriteResult insert(DBCollection dbCollection, BasicDBObject dbObject) {
        try {
            WriteResult result = dbCollection.insert(dbObject);
            logger.info("Result of inserting to {} is: {}", dbCollection, result);
            return result;
        } catch (MongoException me) {
            logger.error("Failed to insert to {} to {}", dbObject, dbCollection);
            logger.error("Insertion failure reason: " + me.getLocalizedMessage(), me);
            return null;
        }
    }

    private static final void logAllDocuments(DBCollection dbCollection){
        DBCursor dbCursor = dbCollection.find();
        logger.info("###Printing {} documents from collection {}", dbCursor.size(), dbCollection.getName());
        while (dbCursor.hasNext()){
            logger.info(">> {} ", dbCursor.next().toString());
        }
        logger.info("### Done! All documents from collection {} where printed", dbCollection.getName());
    }

}
