package com.social.socialnetwork.Validators;

import com.social.socialnetwork.AppExceptions.ValidationException;
import com.social.socialnetwork.Domain.Prietenie;

public class PrietenieValidator implements Validator<Prietenie>{
    /**
     * functie validare la prietenie
     * @param entity o prietenie
     * @throws ValidationException
     */
    @Override
    public void validate(Prietenie entity) throws ValidationException {
        String mesajEroare = "";
        if(entity.getId().getLeft() <= 0){
            mesajEroare += "ID utilizator 1 invalid!\n";
        }
        if(entity.getId().getRight() <= 0){
            mesajEroare += "ID utilizator 2 invalid!\n";
        }
        if(entity.getId().getRight().equals(entity.getId().getLeft())){
            mesajEroare += "Nu poti fi prieten cu tine insuti!\n";
        }
        if(!mesajEroare.isEmpty()){
            throw new ValidationException(mesajEroare);
        }
    }
}