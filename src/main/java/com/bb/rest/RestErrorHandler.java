package com.bb.rest;

import com.bb.dal.util.Consts;
import com.bb.rest.dto.ErrorInfo;
import com.bb.service.PlaceServiceException;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * User: belozovs
 * Date: 7/18/14
 * Description
 */
@ControllerAdvice
public class RestErrorHandler {

    private final static Logger logger = LoggerFactory.getLogger(RestErrorHandler.class);

    @Autowired
    private ReloadableResourceBundleMessageSource resourceBundle;

    public ReloadableResourceBundleMessageSource getResourceBundle() {
        return resourceBundle;
    }

    public void setResourceBundle(ReloadableResourceBundleMessageSource resourceBundle) {
        this.resourceBundle = resourceBundle;
    }

    @ExceptionHandler(PlaceServiceException.class)
    @ResponseBody
    public ResponseEntity<ErrorInfo> handlePlaceServiceException(HttpServletRequest request, PlaceServiceException ex) {
        logger.info("PlaceServiceException occurred: URL=" + request.getRequestURL());
        logger.info("handlePlaceServiceException says: {}", ex.getMessage());
        Locale locale = LocaleContextHolder.getLocale();
        ErrorInfo errorInfo = new ErrorInfo(resourceBundle.getMessage("error.message.type", null, locale),ex.getLocalizedMessage(),HttpStatus.INTERNAL_SERVER_ERROR.toString());
        return new ResponseEntity<ErrorInfo>(errorInfo, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseEntity<String> handleArgumentNotValidException(HttpServletRequest request, MethodArgumentNotValidException ex) {
        logger.info("MethodArgumentNotValidException occurred:: URL=" + request.getRequestURL());
        List<FieldError> errors = ex.getBindingResult().getFieldErrors();
        List<ErrorInfo> errorInfos = new ArrayList<ErrorInfo>();
        if (errors != null && !errors.isEmpty()) {
            for (FieldError error : errors) {
                StringBuffer errorSB = new StringBuffer().append("Object: ").append(error.getObjectName()).append(", field: ").append(error.getField()).append(", rejected value: ").append(error.getRejectedValue()).append(", reason: ").append(error.getCode());
                ErrorInfo errorInfo = new ErrorInfo(errorSB.toString(), errorSB.toString(),HttpStatus.BAD_REQUEST.toString());
                errorInfos.add(errorInfo);
            }
        }
        logger.info("handleMethodArgumentNotValidException says: {}", ex.getMessage());

        return new ResponseEntity<String>(errorInfos.toString(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ResponseBody
    public ResponseEntity<ErrorInfo> requestHandlingNoHandlerFound(HttpServletRequest req, NoHandlerFoundException ex) {
        Locale locale = LocaleContextHolder.getLocale();
        String errorMessage = resourceBundle.getMessage("generic.error", null, locale);

        String errorURL = req.getRequestURL().toString();

        ErrorInfo errorInfo = new ErrorInfo(errorMessage,ex.getLocalizedMessage(),HttpStatus.INTERNAL_SERVER_ERROR.toString());

        return new ResponseEntity<ErrorInfo>(errorInfo, HttpStatus.BAD_REQUEST);
    }


}
