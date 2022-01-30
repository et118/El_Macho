package com.github.et118.El_Mama.Commands;

import com.github.et118.El_Mama.Events.Event;
import com.github.et118.El_Mama.Misc.Colors;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.spec.EmbedCreateSpec;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class InfoCommand extends Command{
    private ArrayList<Command> commands;
    private ArrayList<Event> events;
    public InfoCommand(ArrayList<Command> commands, ArrayList<Event> events, CommandInfo commandInfo) {
        super(commandInfo);
        this.commands = commands;
        this.events = events;
    }

    @Override
    public Mono<Void> execute(MessageCreateEvent event) {
        EmbedCreateSpec.Builder builder = EmbedCreateSpec.builder();
        String message = event.getMessage().getContent();
        String[] arguments = message.split(" ");
        if(arguments.length <= 1) {
            builder = builder
                    .color(Colors.INFO)
                    .title("Select Subcategory")
                    .addField("Command / cmd","\u200b",false)
                    .addField("Event / evt","\u200b",false);
            EmbedCreateSpec embedCreateSpec = builder.build();
            return event.getMessage().getChannel().flatMap(channel -> channel.createMessage(embedCreateSpec)).then();
        } else {
            builder = builder.color(Colors.INFO);
            if(arguments[1].equalsIgnoreCase("command") || arguments[1].equalsIgnoreCase("cmd")) {
                Map<String,ArrayList<Command>> categories = new HashMap<>();
                for(Command command : commands) {
                    if(!categories.containsKey(command.getInfo().getCategory())) {
                        categories.put(command.getInfo().getCategory(),new ArrayList<Command>());
                    }
                    categories.get(command.getInfo().getCategory()).add(command);
                }
                for(String category : categories.keySet()) {
                    builder = builder.addField(category,"\u200b\n\n",false);
                    for(Command command : categories.get(category)) {
                        CommandInfo info = command.getInfo();
                        builder = builder.addField(info.getName(),String.join(" / ",info.getPrefixes()) + "\n" + info.getDescription(),true);
                    }
                }
                EmbedCreateSpec embedCreateSpec = builder.build();
                return event.getMessage().getChannel().flatMap(channel -> channel.createMessage(embedCreateSpec)).then();
            }

            if(arguments[1].equalsIgnoreCase("event") || arguments[1].equalsIgnoreCase("evt")) {
                Map<String,ArrayList<Event>> categories = new HashMap<>();
                for(Event ev : events) {
                    if(!categories.containsKey(ev.getInfo().getCategory())) {
                        categories.put(ev.getInfo().getCategory(),new ArrayList<Event>());
                    }
                    categories.get(ev.getInfo().getCategory()).add(ev);
                }
                for(String category : categories.keySet()) {
                    builder = builder.addField(category,"\u200b\n\n",false);
                    for(Event ev : categories.get(category)) {
                        String status;
                        if(ev.getInfo().isEnabled()) {
                            status = "```diff\n+ Status: Enabled```";
                        } else {
                            status = "```fix\n- Status: Disabled```";
                        }
                        builder = builder.addField(ev.getInfo().getName(), status + ev.getInfo().getDescription(),true);
                    }
                }
                EmbedCreateSpec embedCreateSpec = builder.build();
                return event.getMessage().getChannel().flatMap(channel -> channel.createMessage(embedCreateSpec)).then();
            }
        }
        return Mono.empty();
    }
}
