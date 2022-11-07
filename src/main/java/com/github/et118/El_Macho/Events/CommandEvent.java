package com.github.et118.El_Macho.Events;

import com.github.et118.El_Macho.Commands.*;
import com.github.et118.El_Macho.Commands.Core.InfoCommand;
import com.github.et118.El_Macho.Commands.Core.PingCommand;
import com.github.et118.El_Macho.Commands.Music.*;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import reactor.core.publisher.Mono;

import java.time.Duration;
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

        this.commands.add(new PingCommand(new CommandInfo(new String[]{"ping"},"Ping","Tests if the bot can respond","Core",true)));
        this.commands.add(new InfoCommand(commands, events, new CommandInfo(new String[]{"i","info","help"},"Info", "Shows information about commands and events","Core",true)));
        this.commands.add(new PlayCommand(new CommandInfo(new String[]{"play","p"},"Play","Play track","Music",true)));
        this.commands.add(new PlaytopCommand(new CommandInfo(new String[]{"playtop","pt"},"Playtop","Play track first in queue","Music",true)));
        this.commands.add(new JoinCommand(new CommandInfo(new String[]{"join"},"Join","Join the voice channel","Music",true)));
        this.commands.add(new LeaveCommand(new CommandInfo(new String[]{"leave"},"Leave","Leave the voice channel","Music",true)));
        this.commands.add(new LoopCommand(new CommandInfo(new String[]{"loop"},"Loop","Loop the current song or the playlist","Music",true)));
        this.commands.add(new UnloopCommand(new CommandInfo(new String[]{"unloop"},"Unloop","Unloop the current song or playlist","Music",true)));
        this.commands.add(new ClearCommand(new CommandInfo(new String[]{"clear"},"Clear","Clear the queue","Music",true)));
        this.commands.add(new PauseCommand(new CommandInfo(new String[]{"pause","stop"},"Pause","Pause the track","Music",true)));
        this.commands.add(new ResumeCommand(new CommandInfo(new String[]{"resume","start"},"Resume","Resume the track","Music",true)));
        this.commands.add(new ShuffleCommand(new CommandInfo(new String[]{"shuffle"},"Shuffle","Shuffle the queue","Music",true)));
        this.commands.add(new SkipCommand(new CommandInfo(new String[]{"skip","fs"},"Skip","Skip the current track","Music",true)));
        this.commands.add(new QueueCommand(new CommandInfo(new String[]{"queue","q"},"Queue","Display the queue","Music",true),5));
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
