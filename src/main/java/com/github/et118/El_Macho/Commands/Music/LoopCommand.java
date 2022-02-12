package com.github.et118.El_Macho.Commands.Music;

import com.github.et118.El_Macho.Commands.Command;
import com.github.et118.El_Macho.Commands.CommandInfo;
import com.github.et118.El_Macho.Commands.Music.MusicManager;
import discord4j.core.event.domain.message.MessageCreateEvent;
import reactor.core.publisher.Mono;

public class LoopCommand extends Command {
    public LoopCommand(CommandInfo commandInfo) {
        super(commandInfo);
    }

    @Override
    public Mono<Void> execute(MessageCreateEvent event) {
        if(event.getMember().get().getVoiceState().block() == null) return Mono.empty();
        return Mono.fromRunnable(()->{
            if(event.getMessage().getContent().split(" ").length < 2) {
                MusicManager.getTrackScheduler(event.getGuildId().get()).setLoopingTrack(true);
            } else if(event.getMessage().getContent().split(" ")[1].equalsIgnoreCase("playlist")) {
                MusicManager.getTrackScheduler(event.getGuildId().get()).setLoopingPlaylist(true);
            }
        });
    }
}
