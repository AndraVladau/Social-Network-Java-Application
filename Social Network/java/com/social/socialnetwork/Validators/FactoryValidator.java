package com.social.socialnetwork.Validators;

public class FactoryValidator {
    private static FactoryValidator factoryValidator;
    private FactoryValidator(){

    }
    public static FactoryValidator getFactoryInstance(){
        if(factoryValidator == null){
            factoryValidator = new FactoryValidator();
        }
        return factoryValidator;
    }

    public Validator createValidator(ValidatorStrategies strategies){
        switch (strategies){
            case UTILIZATOR -> {
                return new UtilizatorValidator();
            }
            case PRIETENIE -> {
                return new PrietenieValidator();
            }
            case CEREREPRIETENIE -> {
                return new CererePrietenieValidator();
            }
            default ->
            {
                return null;
            }
        }
    }
}
