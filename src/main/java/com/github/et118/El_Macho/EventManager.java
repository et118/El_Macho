package com.github.et118.El_Macho;

import com.fasterxml.jackson.databind.exc.ValueInstantiationException;
import com.github.et118.El_Macho.Events.*;
import discord4j.core.GatewayDiscordClient;
import discord4j.discordjson.json.ImmutableEmbedAuthorData;
import reactor.core.publisher.Hooks;
import reactor.core.publisher.Mono;

import java.time.Duration;
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
        Hooks.onOperatorDebug();
    }

    public void addEvents() {

        events.add(new MusicEvent(new EventInfo("Music","Adds and removes the music player","Music",true)));
        events.add(new DisconnectEvent(new EventInfo("Disconnect","Disconnects the bot when sitting in an empty call","Music",true)));
        //Don't add events under the CommandEvent. It uses the other events in the Info Command.
        events.add(new CommandEvent("!", events, new EventInfo("Command", "Controls commands", "Core", true)));
        subscribeToEvents();
    }

    private Mono<Void> onEvent(Class c, discord4j.core.event.domain.Event rawEvent) {
        Mono<Void> executeEvents = Mono.empty();
        for(Event event : subscribedEvents.get(c)) {
            if(event.getInfo().isEnabled()) {
                executeEvents = executeEvents.then(event.execute(c,rawEvent));
            }
        }
        return executeEvents
                .doOnError(System.err::println)
                .onErrorResume(e -> Mono.fromRunnable(e::printStackTrace))
                .timeout(Duration.ofMillis(20000));
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
