package com.github.et118.El_Mama.Commands;

import discord4j.core.event.domain.message.MessageCreateEvent;
import reactor.core.publisher.Mono;

public abstract class Command {
    private CommandInfo commandInfo;
    public Command(CommandInfo commandInfo) {
        this.commandInfo = commandInfo;
    }
    public CommandInfo getCommandInfo() {return this.commandInfo;}
    public abstract Mono<Void> execute(MessageCreateEvent event);
}
