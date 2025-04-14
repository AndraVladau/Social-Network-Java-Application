package com.social.socialnetwork.Repository;

import com.social.socialnetwork.AppExceptions.ValidationException;
import com.social.socialnetwork.Domain.Entitate;
import com.social.socialnetwork.Validators.FactoryValidator;
import com.social.socialnetwork.Validators.Validator;
import com.social.socialnetwork.Validators.ValidatorStrategies;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryRepository<ID, E extends Entitate<ID>> implements Repository<ID, E>{
    Validator validator;

    Map<ID, E> entities;

    public InMemoryRepository(ValidatorStrategies validatorStrategies) {
        entities = new HashMap<>();
        validator = FactoryValidator.getFactoryInstance().createValidator(validatorStrategies);
    }

    /**
     * functia de returnare a unui obiect dupa un id
     * @param id -the id of the entity to be returned
     *           id must not be null
     * @return Optional care contine obiectul(sau optional gol)
     */
    @Override
    public Optional<E> findOne(ID id) {
        if(id==null) {
            throw new IllegalArgumentException("ID must be not null");
        }
        return Optional.ofNullable(entities.get(id));
    }

    /**
     * functia care returneaza toate valorile
     * @return iterabil prin valorile
     */
    @Override
    public Iterable<E> findAll() {
        return entities.values();
    }

    /**
     * functia de salvare a unei intitati
     * @param entity
     *         entity must be not null
     * @throws ValidationException
     *            if the entity is not valid
     * @return Optional empty daca entitateae a fost salvata
     *          Optional cu entitatea daca exista deja o entitate cu acelasi id
     */
    @Override
    public Optional<E> save(E entity) {
        if (entity==null)
            throw new IllegalArgumentException("Entity must be not null");
        validator.validate(entity);
        return Optional.ofNullable(entities.putIfAbsent(entity.getId(), entity));
    }

    /**
     * functia de stergere a unei entitati
     * @param id
     *      id must be not null
     * @return Optional cu entitatea daca a fost sters cu succes
     *         Optional empty daca nu exista entitatea in repo
     */
    @Override
    public Optional<E> delete(ID id) {
        if(id == null){
            throw new IllegalArgumentException("ID must be not null");
        }
        return Optional.ofNullable(entities.remove(id));
    }

    /**
     * functia de update
     * @param entity
     *          entity must not be null
     * @throws ValidationException
     *            if the entity is not valid
     * @return Optional empty daca entitatea a fost modificata cu succes
     *         Optional cu entitatea daca nu a fost modificata
     */
    @Override
    public Optional<E> update(E entity) {

        if(entity == null)
            throw new IllegalArgumentException("Entity must be not null!");
        validator.validate(entity);
        entities.put(entity.getId(),entity);
        if(entities.get(entity.getId()) != null) {
            entities.put(entity.getId(),entity);
            return Optional.empty();
        }
        return Optional.ofNullable(entity);

    }

    /**
     * dimensiunea repositoryului
     * @return intreg reprezentand dimensiuna
     */
    @Override
    public int size() {
        return entities.size();
    }
}
