package com.social.socialnetwork.Utils.Observer;

import com.social.socialnetwork.Utils.Events.Event;

public interface Observer <E extends Event> {
    void update(E eventUpdate);
}
