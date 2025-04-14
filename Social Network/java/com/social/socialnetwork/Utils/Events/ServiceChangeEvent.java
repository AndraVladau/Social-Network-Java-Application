package com.social.socialnetwork.Utils.Events;

import com.social.socialnetwork.Domain.Utilizator;

public class ServiceChangeEvent implements Event {
    private ChangeEventType type;
    private Long user1, user2;

    public ServiceChangeEvent(ChangeEventType type, Long user1, Long user2){
        this.type = type; // ce tip de event e
        this.user1 = user1;
        this.user2 = user2;
    }

    public ServiceChangeEvent(ChangeEventType type){
        this.type = type;
    }

    public ServiceChangeEvent(){
        this.type = null;
    }

    public ChangeEventType getType() {
        return type;
    }

    public Long getUser1() {
        return user1;
    }

    public void setUser1(Long user1) {
        this.user1 = user1;
    }

    public Long getUser2() {
        return user2;
    }

    public void setUser2(Long user2) {
        this.user2 = user2;
    }
}
