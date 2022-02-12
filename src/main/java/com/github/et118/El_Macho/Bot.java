package com.github.et118.El_Macho;

import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;

public class Bot {
    private final String botToken;

    public Bot(String botToken) {
        this.botToken = botToken;
    }

    public void start() {
        GatewayDiscordClient discordClient = DiscordClientBuilder.create(this.botToken).build().login().block();
        EventManager eventManager = new EventManager(discordClient);
        eventManager.addEvents();
        discordClient.onDisconnect().block();
    }
}
