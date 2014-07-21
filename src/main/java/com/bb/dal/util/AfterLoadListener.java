package com.bb.dal.util;

import com.bb.dal.entity.Place;
import com.mongodb.DBObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.stereotype.Component;

/**
 * User: belozovs
 * Date: 7/14/14
 * Description
 */
@Component
public class AfterLoadListener extends AbstractMongoEventListener<Place> {

    private static final Logger logger = LoggerFactory.getLogger(AfterLoadListener.class);

    @Override
    public void onAfterLoad(DBObject dbo) {
        super.onAfterLoad(dbo);
        logger.debug("Loaded Place "+dbo.get("name"));
    }
}
