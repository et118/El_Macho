package com.github.et118.El_Macho.Commands;

import discord4j.core.event.domain.message.MessageCreateEvent;
import reactor.core.publisher.Mono;

public class PingCommand extends Command{
    public PingCommand(CommandInfo commandInfo) {
        super(commandInfo);
    }

    @Override
    public Mono<Void> execute(MessageCreateEvent event) {
        return event.getMessage().getChannel().flatMap(channel -> channel.createMessage("Pong!")).then();
    }
}
