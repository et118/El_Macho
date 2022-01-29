package com.github.et118.El_Mama.Events;

import reactor.core.publisher.Mono;

public abstract class Event {
    private EventInfo info;
    public Event(EventInfo info) {
        this.info = info;
    }
    public EventInfo getInfo() { return this.info; }
    public abstract Class[] getEventSubscriptions();
    public abstract Mono<Void> execute(Class eventType, discord4j.core.event.domain.Event rawEvent);
}
