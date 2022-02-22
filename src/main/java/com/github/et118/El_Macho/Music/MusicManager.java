package com.github.et118.El_Macho.Commands.Music;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.track.playback.NonAllocatingAudioFrameBuffer;
import discord4j.common.util.Snowflake;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.channel.VoiceChannel;
import discord4j.voice.AudioProvider;
import reactor.core.publisher.Mono;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class MusicManager {

    private static Map<Snowflake, Guild> guilds = new HashMap<>();
    private static Map<Snowflake, AudioPlayerManager> audioPlayerManagers = new HashMap<>();
    private static Map<Snowflake, AudioPlayer> audioPlayers = new HashMap<>();
    private static Map<Snowflake, AudioProvider> audioProviders = new HashMap<>();
    private static Map<Snowflake, AudioLoadResultHandler> audioLoadResultHandlers = new HashMap<>();
    private static Map<Snowflake, TrackScheduler> trackSchedulers = new HashMap<>();

    public static Mono<Void> addMusicPlayer(Guild guild) {
        return Mono.fromRunnable(() -> {
            Snowflake guildID = guild.getId();
            if(!audioPlayerManagers.containsKey(guildID)) {
                AudioPlayerManager manager = new DefaultAudioPlayerManager();
                manager.getConfiguration().setFrameBufferFactory(NonAllocatingAudioFrameBuffer::new);
                AudioSourceManagers.registerRemoteSources(manager);
                AudioPlayer player = manager.createPlayer();
                TrackScheduler scheduler = new TrackScheduler(player);
                AudioLoadResultHandler resultHandler = new AudioResultHandler(player,scheduler);
                AudioProvider provider = new LavaPlayerProvider(player);
                player.addListener(scheduler);

                guilds.put(guildID,guild);
                audioPlayerManagers.put(guildID,manager);
                audioPlayers.put(guildID,player);
                audioProviders.put(guildID,provider);
                audioLoadResultHandlers.put(guildID,resultHandler);
                trackSchedulers.put(guildID,scheduler);
                System.out.println("Music Player Added To: " + guild.getName());
            }
        });
    }

    public static Mono<Void> removeMusicPlayer(Snowflake guildID) {
        return Mono.fromRunnable(() -> {
            audioPlayerManagers.remove(guildID);
            audioPlayers.remove(guildID);
            audioProviders.remove(guildID);
            audioLoadResultHandlers.remove(guildID);
            trackSchedulers.remove(guildID);
            System.out.println("Music Player Removed From: " + guilds.get(guildID).getName());
            guilds.remove(guildID);
        });
    }

    public static Mono<Void> playTrack(MessageCreateEvent event, boolean playtop) { //TODO Twitch streams, Soundcloud and Spotify integration
        //Can't be returned in a Mono because it somehow causes a rxjava timeout
        if(event.getMessage().getAuthor().get().isBot()) return Mono.empty();
        Snowflake id = event.getGuildId().get();
        AudioPlayerManager manager = audioPlayerManagers.get(id);
        AudioLoadResultHandler handler = audioLoadResultHandlers.get(id);
        String[] message = event.getMessage().getContent().split(" ",2);
        if(message.length <=1) return Mono.empty();
        trackSchedulers.get(id).setNextTrackPlaytop(playtop);
        if (message[1].contains("youtube.com/playlist") || message[1].contains("youtube.com/watch") || message[1].contains("youtu.be/")) {
            try { manager.loadItem(message[1],handler).get(); } catch (InterruptedException | ExecutionException e) { e.printStackTrace(); }
        } else {
            try { manager.loadItem("ytsearch:"+message[1],handler).get(); } catch (InterruptedException | ExecutionException e) { e.printStackTrace(); }
        }

        return Mono.empty();
    }

    public static Mono<Void> joinChannel(VoiceChannel channel) { //New method disconnected randomly
        channel.join(spec -> spec.setProvider(audioProviders.get(channel.getGuildId()))).block();
        return Mono.empty();
    }

    public static Mono<Void> leaveChannel(Snowflake guildID) {
        return guilds.get(guildID).getVoiceConnection()
                .flatMap(voiceConnection -> voiceConnection.disconnect())
                .then(Mono.fromRunnable(()->{trackSchedulers.get(guildID).clear();
                                             trackSchedulers.get(guildID).skip();}))
                .then();
    }

    public static Guild getGuild(Snowflake guildID) {
        return guilds.get(guildID);
    }

    public static AudioPlayerManager getAudioPlayerManager(Snowflake guildID) {
        return audioPlayerManagers.get(guildID);
    }

    public static AudioPlayer getAudioPlayer(Snowflake guildID) {
        return audioPlayers.get(guildID);
    }

    public static AudioProvider getAudioProvider(Snowflake guildID) {
        return audioProviders.get(guildID);
    }

    public static AudioLoadResultHandler getAudioLoadResultHandler(Snowflake guildID) {
        return audioLoadResultHandlers.get(guildID);
    }

    public static TrackScheduler getTrackScheduler(Snowflake guildID) {
        return trackSchedulers.get(guildID);
    }
}
