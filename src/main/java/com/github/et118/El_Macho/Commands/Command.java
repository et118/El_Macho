package com.github.et118.El_Macho.Commands;

import discord4j.core.event.domain.message.MessageCreateEvent;
import reactor.core.publisher.Mono;

public abstract class Command {
    private CommandInfo commandInfo;
    public Command(CommandInfo commandInfo) {
        this.commandInfo = commandInfo;
    }
    public CommandInfo getInfo() {return this.commandInfo;}
    public abstract Mono<Void> execute(MessageCreateEvent event);
}
