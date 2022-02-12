package com.github.et118.El_Macho.Events;

import com.github.et118.El_Macho.Commands.Music.MusicManager;
import discord4j.core.event.domain.guild.GuildCreateEvent;
import discord4j.core.event.domain.guild.GuildDeleteEvent;
import reactor.core.publisher.Mono;

public class MusicEvent extends Event{
    public MusicEvent(EventInfo info) {
        super(info);
    }

    @Override
    public Class[] getEventSubscriptions() {
        return new Class[]{GuildCreateEvent.class, GuildDeleteEvent.class};
    }

    @Override
    public Mono<Void> execute(Class eventType, discord4j.core.event.domain.Event rawEvent) {
        if(eventType.equals(GuildCreateEvent.class)) {
            return MusicManager.addMusicPlayer(((GuildCreateEvent)rawEvent).getGuild());
        }
        if(eventType.equals(GuildDeleteEvent.class)) { //TODO Check if leaving and joining same guild multiple times messes up something
            return MusicManager.removeMusicPlayer(((GuildDeleteEvent)rawEvent).getGuildId());
        }
        return Mono.empty();
    }
}
