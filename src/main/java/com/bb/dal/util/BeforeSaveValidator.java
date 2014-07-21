package com.bb.dal.util;

import com.mongodb.DBObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * User: belozovs
 * Date: 7/14/14
 * Description
 */
@Component
public class BeforeSaveValidator extends AbstractMongoEventListener {

    private final static Logger logger = LoggerFactory.getLogger(BeforeSaveValidator.class);

    @Autowired
    private Validator validator;

    @Override
    public void onBeforeSave(Object source, DBObject dbo) {

        Set<ConstraintViolation<Object>> violations = validator.validate(source);

        if(violations.size()>0){
            logger.error("Violation found");
            Iterator<ConstraintViolation<Object>> iterator = violations.iterator();
            while (iterator.hasNext()){
                ConstraintViolation<Object> nextViolation = iterator.next();
                logger.error(nextViolation.getMessage());
            }
            throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violations));
        }




    }
}
