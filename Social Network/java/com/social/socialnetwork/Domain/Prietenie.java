package com.social.socialnetwork.Domain;

import java.time.LocalDateTime;

public class Prietenie extends Entitate<Tuplu<Long, Long>>{
    LocalDateTime dateCreated;

    /**
     * constructor pentru o prietenie
     * @param user1 id-ul pentru primul user
     * @param user2 id-ul pentru al doileea user
     */
    public Prietenie(Long user1, Long user2) {
        this.id = new Tuplu<>(user1, user2);
        this.dateCreated = LocalDateTime.now();
    }

    public Prietenie(Long user1, Long user2, LocalDateTime friendsFrom) {
        this.id = new Tuplu<>(user1, user2);
        this.dateCreated = friendsFrom;
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
        return "Prietenie{" +
                this.id.getLeft() + "<->" +this.id.getRight() + "Friends From " + dateCreated +
                '}';
    }
}
