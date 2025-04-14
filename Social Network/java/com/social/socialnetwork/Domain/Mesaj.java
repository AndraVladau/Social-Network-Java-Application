package com.social.socialnetwork.Domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Mesaj extends Entitate<Long> {
    private Long fromUser;
    private List<Long> toUsers;
    private String mesajScris;
    private LocalDateTime data;
    private Mesaj replyTo;

    public Mesaj(Long from, List<Long> Longs, String mesajScris, LocalDateTime data) {
        this.fromUser = from;
        toUsers = Longs;
        this.mesajScris = mesajScris;
        this.data = data;
        replyTo=null;
    }

    public Mesaj(Long from, List<Long> Longs, String mesajScris) {
        this.fromUser = from;
        toUsers = Longs;
        this.mesajScris = mesajScris;
        this.data = LocalDateTime.now();
        replyTo=null;
    }

    public Mesaj(Long fromUser, Mesaj replyTo, String mesajScris) {
        this.fromUser = fromUser;
        this.replyTo = replyTo;
        this.toUsers = Collections.singletonList(replyTo.getFromUser());
        this.mesajScris = mesajScris;
        this.data = LocalDateTime.now();
    }

    public Mesaj(Long fromUser, Long toUser, Mesaj replyTo, String mesajScris, LocalDateTime data) {
        this.fromUser = fromUser;
        this.replyTo = replyTo;
        this.toUsers = Collections.singletonList(toUser);
        this.mesajScris = mesajScris;
        this.data = data;
    }
    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    public String getMesajScris() {
        return mesajScris;
    }

    public void setMesajScris(String mesajScris) {
        this.mesajScris = mesajScris;
    }

    public Mesaj getReplyTo() {
        return replyTo;
    }

    public void setReplyTo(Mesaj replyTo) {
        this.replyTo = replyTo;
    }

    public List<Long> getToUsers() {
        return toUsers;
    }

    public void setToUsers(List<Long> toUsers) {
        this.toUsers = toUsers;
    }

    public Long getFromUser() {
        return fromUser;
    }

    public void setFromUser(Long fromUser) {
        this.fromUser = fromUser;
    }

    @Override
    public String toString() {
        return "[" + id + "]" +
                "fromUser=" + fromUser +
                ", toUsers=" + toUsers +
                ", mesajScris='" + mesajScris + '\'' +
                ", data=" + data +
                ", replyTo=" + replyTo;
    }

    //fa altfel functiile
}

