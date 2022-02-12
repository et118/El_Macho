package com.github.et118.El_Macho.Commands;

import com.github.et118.El_Macho.Commands.Music.MusicManager;
import discord4j.core.event.domain.message.MessageCreateEvent;
import reactor.core.publisher.Mono;

public class LoopCommand extends Command{
    public LoopCommand(CommandInfo commandInfo) {
        super(commandInfo);
    }

    @Override
    public Mono<Void> execute(MessageCreateEvent event) {
        return Mono.fromRunnable(()->{
            if(event.getMessage().getContent().split(" ").length < 2) {
                MusicManager.getTrackScheduler(event.getGuildId().get()).setLoopingTrack(true);
                System.out.println("Looped track");
            } else if(event.getMessage().getContent().split(" ")[1].equalsIgnoreCase("playlist")) {
                MusicManager.getTrackScheduler(event.getGuildId().get()).setLoopingPlaylist(true);
                System.out.println("Looped playlist");
            }
        });
    }
}
