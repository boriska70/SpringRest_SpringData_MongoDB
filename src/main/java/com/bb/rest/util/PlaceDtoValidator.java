package com.bb.rest.util;

import com.bb.rest.dto.PlaceDTO;
import com.bb.rest.dto.SightSeeingDtoInterface;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * User: belozovs
 * Date: 7/18/14
 * Description
 */
public class PlaceDtoValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return SightSeeingDtoInterface.class.isAssignableFrom(aClass);
        //return PlaceDTO.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "name.empty");
        PlaceDTO placeDTO = (PlaceDTO) o;
        if (placeDTO.getName().length() < 2) {
            errors.rejectValue("name", "name.too.short");
        }
    }
}
