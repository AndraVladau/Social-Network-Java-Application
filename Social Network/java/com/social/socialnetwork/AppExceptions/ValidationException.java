package com.social.socialnetwork.AppExceptions;

/**
 * clasa de erori din validari
 */
public class ValidationException extends AppException{
    public ValidationException(){
        super();
    }
    public ValidationException(String message){
        super(message);
    }
    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ValidationException(Throwable cause) {
        super(cause);
    }

    public ValidationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
