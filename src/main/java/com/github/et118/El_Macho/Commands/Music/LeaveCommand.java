package com.github.et118.El_Macho.Commands.Music;

import com.github.et118.El_Macho.Commands.Command;
import com.github.et118.El_Macho.Commands.CommandInfo;
import com.github.et118.El_Macho.Misc.Colors;
import com.github.et118.El_Macho.Music.MusicManager;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.VoiceState;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.Member;
import discord4j.core.spec.EmbedCreateSpec;
import reactor.core.publisher.Mono;

public class LeaveCommand extends Command {
    public LeaveCommand(CommandInfo commandInfo) {
        super(commandInfo);
    }

    @Override
    public Mono<Void> execute(MessageCreateEvent event) {
        if(event.getMember().get().getVoiceState().block() == null) return Mono.empty();
        Mono<Void> leaveChannel = event.getGuild()
                .map(Guild::getId)
                .flatMap(id -> MusicManager.leaveChannel(id));
        Mono<Void> sendFeedback = event.getMessage().getChannel().flatMap(channel -> channel.createMessage(
                EmbedCreateSpec.builder()
                        .color(Colors.INFO)
                        .title("Left Channel")
                        .build()
        )).then();
        return leaveChannel.then(sendFeedback);
    }
}
