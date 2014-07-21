package com.bb.rest.dto;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User: belozovs
 * Date: 7/20/14
 * Description
 */
public class ErrorInfo {

    private final static Logger logger = LoggerFactory.getLogger(ErrorInfo.class);

    private String messageType;
    private String message;
    private String status;

    public ErrorInfo(String messageType, String message, String status) {
        this.messageType = messageType;
        this.message = message;
        this.status = status;
    }

    public String getMessageType() {
        return messageType;
    }

    public String getMessage() {
        return message;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {

        StringBuffer buffer = new StringBuffer();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("messageType", messageType);
            jsonObject.put("message", message);
            jsonObject.put("status", status);
        } catch (JSONException e) {
            logger.error("Error occurred while creating ErrorInfo json");
        }

        return jsonObject.toString();
    }
}
