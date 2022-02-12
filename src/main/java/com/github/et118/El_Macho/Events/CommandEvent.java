package com.github.et118.El_Mama.Events;

import com.github.et118.El_Mama.Commands.Command;
import com.github.et118.El_Mama.Commands.CommandInfo;
import com.github.et118.El_Mama.Commands.InfoCommand;
import com.github.et118.El_Mama.Commands.PingCommand;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import reactor.core.publisher.Mono;
import java.util.ArrayList;

public class CommandEvent extends Event{
    private String commandPrefix;
    private ArrayList<Command> commands;
    private ArrayList<Event> events;
    public CommandEvent(String commandPrefix, ArrayList<Event> events, EventInfo info) {
        super(info);
        this.commandPrefix = commandPrefix;
        this.commands = new ArrayList<>();
        this.events = (ArrayList<Event>) events.clone();
        this.events.add(0,this);

        this.commands.add(new PingCommand(new CommandInfo(new String[]{"p","ping"},"Ping","Tests if the bot can respond","Core",true)));
        this.commands.add(new InfoCommand(commands, events, new CommandInfo(new String[]{"i","info"},"Info", "Shows information about commands and events","Core",true)));
    }

    @Override
    public Class[] getEventSubscriptions() {
        return new Class[]{MessageCreateEvent.class};
    }

    private boolean isCommandMessageFromPlayer(Message message) {
        if(message.getAuthor().get().isBot()) return false;
        if(!message.getContent().startsWith(this.commandPrefix)) return false;
        for (Command command : this.commands) {
            for (String commandPrefix : command.getInfo().getPrefixes()) {
                if(message.getContent().split(" ")[0].toLowerCase().equals(this.commandPrefix + commandPrefix.toLowerCase())){
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
            if(isCommandMessageFromPlayer(event.getMessage())) {
                String messagePrefix = event.getMessage().getContent().toLowerCase().split(" ")[0];
                for(Command command : commands) {
                    for(String commandKeyword : command.getInfo().getPrefixes()) {
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
