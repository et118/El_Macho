package com.github.et118.El_Macho.Events;

import com.github.et118.El_Macho.Music.MusicManager;
import discord4j.core.event.domain.VoiceStateUpdateEvent;
import discord4j.core.object.VoiceState;
import discord4j.voice.VoiceConnection;
import reactor.core.publisher.Mono;

public class DisconnectEvent extends Event{
    public DisconnectEvent(EventInfo info) {
        super(info);
    }

    @Override
    public Class[] getEventSubscriptions() {
        return new Class[]{discord4j.core.event.domain.VoiceStateUpdateEvent.class};
    }

    @Override
    public Mono<Void> execute(Class eventType, discord4j.core.event.domain.Event rawEvent) {
        VoiceStateUpdateEvent event = (VoiceStateUpdateEvent) rawEvent;
        VoiceConnection botConnection = MusicManager.getGuild(event.getCurrent().getGuildId()).getVoiceConnection().block();
        if(botConnection != null && (event.isLeaveEvent() || event.isMoveEvent()) && !event.getOld().get().getUser().block().isBot()) {
            if(event.getOld().get().getChannelId().get().equals(botConnection.getChannelId().block())) {
                Long voiceStatesLeft = event.getOld().get().getChannel().block().getVoiceStates().filter(state -> !state.getUser().block().isBot()).count().block();
                if(event.isMoveEvent())
                    voiceStatesLeft -= 1;
                if(voiceStatesLeft == 0) {
                    return MusicManager.leaveChannel(event.getCurrent().getGuildId());
                }
            }
        }
        return Mono.empty();
    }
}
