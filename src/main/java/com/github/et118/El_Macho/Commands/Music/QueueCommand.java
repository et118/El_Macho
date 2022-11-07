package com.github.et118.El_Macho.Commands.Music;

import com.github.et118.El_Macho.Commands.Command;
import com.github.et118.El_Macho.Commands.CommandInfo;
import com.github.et118.El_Macho.Misc.Colors;
import com.github.et118.El_Macho.Music.MusicManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.spec.EmbedCreateSpec;
import reactor.core.publisher.Mono;

import java.util.ArrayList;

public class QueueCommand extends Command {
    private int maximumResults;
    public QueueCommand(CommandInfo commandInfo, int maximumResults) {
        super(commandInfo);
        this.maximumResults = maximumResults;
    }

    @Override
    public Mono<Void> execute(MessageCreateEvent event) { //TODO add info about looping and paused in queue command
        if(event.getMember().get().getVoiceState().block() == null) return Mono.empty();
        Mono<Void> sendQueueMessage = event.getMessage().getChannel().flatMap(channel -> {
            ArrayList<AudioTrack> queue = MusicManager.getTrackScheduler(event.getGuildId().get()).getQueue();
            AudioTrack playingTrack = MusicManager.getTrackScheduler(event.getGuildId().get()).getPlayingTrack();
            EmbedCreateSpec.Builder builder = EmbedCreateSpec.builder();
            if(playingTrack != null) {
                builder = builder
                        .color(Colors.INFO)
                        .author("Now Playing:",null,null)
                        .title(playingTrack.getInfo().title)
                        .url(playingTrack.getInfo().uri)
                        .thumbnail("https://img.youtube.com/vi/"+playingTrack.getIdentifier()+"/0.jpg");
            } else {
                builder = EmbedCreateSpec.builder()
                        .color(Colors.ERROR)
                        .title("Nothing Is Playing");
                return channel.createMessage(builder.build());
            }

            String status = "";
            if(MusicManager.getTrackScheduler(event.getGuildId().get()).isLoopingTrack()) {
                status += "Looping Track\n";
            }
            if(MusicManager.getTrackScheduler(event.getGuildId().get()).isLoopingPlaylist()) {
                status += "Looping Playlist\n";
            }
            if(MusicManager.getAudioPlayer(event.getGuildId().get()).isPaused()) {
                status += "Paused\n";
            }
            if(!status.isEmpty()) {
                builder.addField("\u200b","`" + status + "`",false);
            }
            if(queue.size() != 0) {
                long duration = playingTrack.getDuration()- playingTrack.getPosition();
                String message = "";
                for(int i = 0; (i < queue.size()) && (i < maximumResults); i++) {
                    AudioTrack track = queue.get(i);
                    int hours = (int)duration/1000/60/60;
                    int minutes = (int)duration/1000/60%60;
                    int seconds = (int)duration/1000%60;
                    String untilPlayingString = String.format("%02d",hours) + ":" + String.format("%02d",minutes) + ":" + String.format("%02d",seconds);

                    hours = (int)track.getDuration()/1000/60/60;
                    minutes = (int)track.getDuration()/1000/60%60;
                    seconds = (int)track.getDuration()/1000%60;
                    String durationString = String.format("%02d",hours) + ":" + String.format("%02d",minutes) + ":" + String.format("%02d",seconds);

                    message += " " + (i+1) + ". [" + queue.get(i).getInfo().title + "](" + queue.get(i).getInfo().uri + ") ";
                    message += "\n**Playing in: **`" + untilPlayingString + "`";
                    message += "    **Duration: `"+durationString+"`**\n\n";

                    duration += track.getDuration();
                }
                builder.addField("Queue: " + queue.size(),message,false);
            }
            return channel.createMessage(builder.build());
        }).doOnError(throwable -> System.err.println(throwable.getMessage())).then();
        return sendQueueMessage;
    }
}
