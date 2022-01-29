package com.github.et118.El_Mama.Events;

import com.github.et118.El_Mama.Commands.Command;
import com.github.et118.El_Mama.Commands.CommandInfo;
import com.github.et118.El_Mama.Commands.PingCommand;
import discord4j.core.event.domain.message.MessageCreateEvent;
import reactor.core.publisher.Mono;
import java.util.ArrayList;

public class CommandEvent extends Event{
    private String commandPrefix;
    private ArrayList<Command> commands;
    public CommandEvent(String commandPrefix, EventInfo info) {
        super(info);
        this.commandPrefix = commandPrefix;
        this.commands = new ArrayList<>();

        this.commands.add(new PingCommand(new CommandInfo(new String[]{"p","ping"})));
    }

    @Override
    public Class[] getEventSubscriptions() {
        return new Class[]{MessageCreateEvent.class};
    }

    private boolean isCommandMessage(String message) {
        if(!message.startsWith(this.commandPrefix)) return false;
        for (Command command : this.commands) {
            for (String commandPrefix : command.getCommandInfo().getPrefixes()) {
                if(message.split(" ")[0].toLowerCase().equals(this.commandPrefix + commandPrefix.toLowerCase())){
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public Mono<Void> execute(Class eventType, discord4j.core.event.domain.Event rawEvent) {
        if(eventType.equals(MessageCreateEvent.class)) {
            MessageCreateEvent event = (MessageCreateEvent) rawEvent;
            String content = event.getMessage().getContent();
            if(isCommandMessage(content) && !event.getMessage().getAuthor().get().isBot()) {
                String messagePrefix = event.getMessage().getContent().toLowerCase().split(" ")[0];
                for(Command command : commands) {
                    for(String commandKeyword : command.getCommandInfo().getPrefixes()) {
                        if(messagePrefix.equals(this.commandPrefix + commandKeyword)) {
                            return command.execute(event);
                        }
                    }

                }
            }
        }
        return Mono.empty();
    }
}
