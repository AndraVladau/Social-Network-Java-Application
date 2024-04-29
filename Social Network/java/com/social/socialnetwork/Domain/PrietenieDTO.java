package com.social.socialnetwork.Domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PrietenieDTO {
    private Long id1;
    private Long id2;
    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getPrenume() {
        return prenume;
    }

    public void setPrenume(String prenume) {
        this.prenume = prenume;
    }

    public LocalDateTime getFriendsFrom() {
        return friendsFrom;
    }

    public void setFriendsFrom(LocalDateTime friendsFrom) {
        this.friendsFrom = friendsFrom;
    }
    private String nume;
    private String prenume;
    private LocalDateTime friendsFrom;

    public PrietenieDTO(String nume, String prenume, LocalDateTime friendsFrom) {
        this.nume = nume;
        this.prenume = prenume;
        this.friendsFrom = friendsFrom;
    }

    @Override
    public String toString() {
        return nume + " " + " " + prenume + " " + friendsFrom.format(DateTimeFormatter.ISO_DATE);
    }

    public Long getId1() {
        return id1;
    }

    public void setId1(Long id1) {
        this.id1 = id1;
    }

    public Long getId2() {
        return id2;
    }

    public void setId2(Long id2) {
        this.id2 = id2;
    }
}
