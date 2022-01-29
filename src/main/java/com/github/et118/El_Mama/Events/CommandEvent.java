package com.github.et118.El_Mama.Events;

import reactor.core.publisher.Mono;

public class CommandEvent extends Event{
    public CommandEvent(EventInfo info) {
        super(info);
    }

    @Override
    public Class[] getEventSubscriptions() {
        return new Class[0];
    }

    @Override
    public Mono<Void> execute(Class eventType, discord4j.core.event.domain.Event rawEvent) {
        return null;
    }
}
