package com.github.et118.El_Macho.Commands;

import com.github.et118.El_Macho.Commands.Music.MusicManager;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Guild;
import reactor.core.publisher.Mono;

public class LeaveCommand extends Command{
    public LeaveCommand(CommandInfo commandInfo) {
        super(commandInfo);
    }

    @Override
    public Mono<Void> execute(MessageCreateEvent event) {
        return event.getGuild()
                .map(Guild::getId)
                .flatMap(id -> MusicManager.leaveChannel(id));
    }
}
