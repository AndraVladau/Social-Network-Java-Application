package com.social.socialnetwork.Validators;

import com.social.socialnetwork.AppExceptions.ValidationException;

public interface Validator<T> {
    void validate(T entity) throws ValidationException;
}
