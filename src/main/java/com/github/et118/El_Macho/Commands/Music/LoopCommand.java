package com.github.et118.El_Macho.Commands.Music;

import com.github.et118.El_Macho.Commands.Command;
import com.github.et118.El_Macho.Commands.CommandInfo;
import com.github.et118.El_Macho.Misc.Colors;
import com.github.et118.El_Macho.Music.MusicManager;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.spec.EmbedCreateSpec;
import reactor.core.publisher.Mono;

public class LoopCommand extends Command {
    public LoopCommand(CommandInfo commandInfo) {
        super(commandInfo);
    }

    @Override
    public Mono<Void> execute(MessageCreateEvent event) {
        if(event.getMember().get().getVoiceState().block() == null) return Mono.empty();
        Mono<Void> loopTrack = Mono.fromRunnable(() -> MusicManager.getTrackScheduler(event.getGuildId().get()).setLoopingTrack(true));
        Mono<Void> loopPlaylist = Mono.fromRunnable(() -> MusicManager.getTrackScheduler(event.getGuildId().get()).setLoopingPlaylist(true));

        if(event.getMessage().getContent().split(" ").length < 2) {
            Mono<Void> sendFeedback = event.getMessage().getChannel().flatMap(channel -> channel.createMessage(
                    EmbedCreateSpec.builder()
                            .color(Colors.INFO)
                            .title("Looped Track")
                            .build()
            )).then();
            return loopTrack.then(sendFeedback);
        } else if(event.getMessage().getContent().split(" ")[1].equalsIgnoreCase("playlist")) {
            Mono<Void> sendFeedback = event.getMessage().getChannel().flatMap(channel -> channel.createMessage(
                    EmbedCreateSpec.builder()
                            .color(Colors.INFO)
                            .title("Looped Playlist")
                            .build()
            )).then();
            return loopPlaylist.then(sendFeedback);
        }
        return event.getMessage().getChannel().flatMap(channel -> channel.createMessage(
                EmbedCreateSpec.builder()
                        .color(Colors.ERROR)
                        .title("Unknown Parameter: " + event.getMessage().getContent().split(" ")[1])
                        .build()
        )).then();
    }
}
