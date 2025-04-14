package com.social.socialnetwork.Utils.Observer;

import com.social.socialnetwork.Utils.Events.Event;

import java.lang.reflect.Array;
import java.util.ArrayList;

public interface Observable <E extends Event>{
    ArrayList<Observer> observers = new ArrayList<>();
    default void addObserver(Observer<E> observer){
        observers.add(observer);
    }
    default void removeObserver(Observer<E> observer){
        observers.remove(observer);
    }
    default void notifyAllObservers(E eventUpdate){
        observers.forEach(x->x.update(eventUpdate));
    }
}
