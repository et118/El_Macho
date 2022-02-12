package com.github.et118.El_Macho.Commands;

import com.github.et118.El_Macho.Commands.Music.MusicManager;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.VoiceState;
import discord4j.core.object.entity.Member;
import reactor.core.publisher.Mono;

public class PlayCommand extends Command{
    public PlayCommand(CommandInfo commandInfo) {
        super(commandInfo);
    }

    @Override
    public Mono<Void> execute(MessageCreateEvent event) {
        Mono<Void> joinChannel = Mono.justOrEmpty(event.getMember())
                .flatMap(Member::getVoiceState)
                .flatMap(VoiceState::getChannel)
                .flatMap(voiceChannel -> MusicManager.joinChannel(voiceChannel));

        Mono<Void> playTrack = Mono.justOrEmpty(event.getMember())
                .flatMap(Member::getVoiceState)
                .flatMap(VoiceState::getChannel)
                .flatMap(voiceChannel -> MusicManager.playTrack(event, false));
        return joinChannel.then(playTrack);
    }
}
