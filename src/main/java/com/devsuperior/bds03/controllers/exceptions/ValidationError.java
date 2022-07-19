package com.devsuperior.bds03.controllers.exceptions;

import java.util.ArrayList;
import java.util.List;


//Classe que irá servir de lista com os errors
public class ValidationError extends StandardError {

    List<FieldMessage> errors = new ArrayList<>();

    public List<FieldMessage> getErros() {
        return errors;
    }

    public void addError(String fieldName, String message) {
        errors.add(new FieldMessage(fieldName, message));
    }
}
