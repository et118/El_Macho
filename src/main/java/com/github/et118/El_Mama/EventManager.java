package com.github.et118.El_Mama;

import com.github.et118.El_Mama.Events.CommandEvent;
import com.github.et118.El_Mama.Events.Event;
import com.github.et118.El_Mama.Events.EventInfo;
import discord4j.core.GatewayDiscordClient;
import reactor.core.publisher.Mono;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EventManager {
    private final GatewayDiscordClient discordClient;
    private ArrayList<Event> events;
    private Map<Class,ArrayList<Event>> subscribedEvents;

    public EventManager(GatewayDiscordClient discordClient) {
        this.discordClient = discordClient;
        this.events = new ArrayList<>();
        this.subscribedEvents = new HashMap<>();
    }

    public void addEvents() {
        events.add(new CommandEvent(new EventInfo("Command", "Controls commands", "Core", true)));

        subscribeToEvents();
    }

    private Mono<Void> onEvent(Class c, discord4j.core.event.domain.Event rawEvent) {
        Mono<Void> executeEvents = Mono.empty();
        for(Event event : subscribedEvents.get(c)) {
            if(event.getInfo().isEnabled()) {
                executeEvents = executeEvents.then(event.execute(c,rawEvent));
            }
        }
        return executeEvents;
    }

    private void subscribeToEvents() {
        for(Event event : events) {
            for(Class c : event.getEventSubscriptions()) {
                if(!subscribedEvents.containsKey(c)) {
                    subscribedEvents.put(c,new ArrayList<>());
                    discordClient.getEventDispatcher().on(c).flatMap(e -> this.onEvent(c, (discord4j.core.event.domain.Event) e)).subscribe();
                }
                subscribedEvents.get(c).add(event);
            }
        }
    }
}
