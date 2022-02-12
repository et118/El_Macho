package com.github.et118.El_Macho.Commands.Music;

import com.github.et118.El_Macho.Commands.Command;
import com.github.et118.El_Macho.Commands.CommandInfo;
import com.github.et118.El_Macho.Commands.Music.MusicManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import discord4j.core.event.domain.message.MessageCreateEvent;
import reactor.core.publisher.Mono;

import java.util.ArrayList;

public class QueueCommand extends Command {
    public QueueCommand(CommandInfo commandInfo) {
        super(commandInfo);
    }

    @Override
    public Mono<Void> execute(MessageCreateEvent event) {
        if(event.getMember().get().getVoiceState().block() == null) return Mono.empty();
        return Mono.fromRunnable(() -> {
            ArrayList<AudioTrack> queue = MusicManager.getTrackScheduler(event.getGuildId().get()).getQueue();
            AudioTrack playingTrack = MusicManager.getTrackScheduler(event.getGuildId().get()).getPlayingTrack();
            if(playingTrack != null) {
                System.out.println("Playing track: " + playingTrack.getInfo().title);
            }
            for(AudioTrack track : queue) {
                System.out.println("Queue: " + track.getInfo().title);
            }
        });
    }
}
