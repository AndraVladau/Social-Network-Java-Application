package com.social.socialnetwork.Domain;

import java.time.LocalDateTime;

public class CererePrietenie extends Entitate<Tuplu<Long, Long>>{
    LocalDateTime dateCreated;

    public int getStatus() {
        return status;
    }

//    public void setStatus(int status) {
//        this.status = status;
//    }

    private int status;
    public static final int PENDING = 0;
    public static final int ACCPETED = 1;
    public static final int REFUSED = 2;

    /**
     * constructor pentru o prietenie
     * @param userTrimie id-ul pentru primul user
     * @param userAccept id-ul pentru al doileea user
     */
    public CererePrietenie(Long userTrimie, Long userAccept) {
        this.id = new Tuplu<>(userTrimie, userAccept);
        this.dateCreated = null;
        this.status = PENDING;
    }

    public CererePrietenie(Long user1, Long user2, LocalDateTime friendsFrom) {
        this.id = new Tuplu<>(user1, user2);
        this.status = ACCPETED;
        this.dateCreated = friendsFrom;
    }

    /**
     * Pentru a putea seta in ce stadiu se afla cerea */
    public CererePrietenie(Long user1, Long user2, LocalDateTime friendsFrom, Integer status) {
        this.id = new Tuplu<>(user1, user2);
        this.status = status;
        this.dateCreated = friendsFrom;
    }
/**
 * Pe masura ce cererile se solutioneaza se seteaza stadiul in care se afla cererea
* */
    public void setAccepted(){
        this.status = ACCPETED;
        this.dateCreated = LocalDateTime.now();
    }

    public void setRefused(){
        this.status = REFUSED;
        this.dateCreated = null;
    }

    /**
     * data la care s-au imprietenit
     * @return
     */
    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    /**
     * suprascierea functiei de tostring
     * @return
     */
    @Override
    public String toString() {
        return "CererePrietenie{" +
                "dateCreated=" + dateCreated +
                ", status=" + status +
                ", id=" + id +
                '}';
    }
}
