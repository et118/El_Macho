package com.github.et118.El_Macho.Music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

import java.util.ArrayList;
import java.util.Collections;

public class TrackScheduler extends AudioEventAdapter {

    private AudioPlayer audioPlayer;
    private ArrayList<AudioTrack> queue;

    private boolean playing;
    private boolean nextTrackPlaytop;
    private boolean loopingTrack;
    private boolean loopingPlaylist;

    public TrackScheduler(AudioPlayer audioPlayer) {
        this.audioPlayer = audioPlayer;
        this.playing = false;
        this.nextTrackPlaytop = false;
        this.loopingTrack = false;
        this.loopingPlaylist = false;
        this.queue = new ArrayList<>();
    }

    public void play(AudioTrack track, boolean addToQueue) {
        boolean started = audioPlayer.startTrack(track,addToQueue);
        this.playing = true;
        if(!started) {
            if(nextTrackPlaytop) {
                System.out.println("Track added first in queue: " + track.getInfo().title);
                queue.add(0,track);
                nextTrackPlaytop = false;
            } else {
                System.out.println("Track added last in queue: " + track.getInfo().title);
                queue.add(track);
            }
        }
    }

    public void play(AudioPlaylist playlist, boolean addToQueue) {
        boolean playtop = false;
        if(nextTrackPlaytop) playtop = true;
        if(!playtop) {
            for(AudioTrack track : playlist.getTracks()) {
                this.play(track,addToQueue);
            }
        } else {
            for(int i = 0; i < playlist.getTracks().size(); i++) {
                nextTrackPlaytop = true;
                this.play(playlist.getTracks().get(playlist.getTracks().size()-1-i),addToQueue);
            }
        }
        nextTrackPlaytop = false;
    }

    public void setPaused(boolean paused) {
        audioPlayer.setPaused(paused);
    }

    public void skip() {
        if(queue.isEmpty()) {
            audioPlayer.stopTrack();
            this.playing = false;
        } else {
            this.play(queue.remove(0),false);
        }
    }

    public void clear() {
        queue.clear();
    }

    public void shuffle() {
        if(!queue.isEmpty()) {
            Collections.shuffle(queue);
        }
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        System.out.println("Track Ended: " + endReason);
        if(endReason.mayStartNext) {
            if(loopingTrack) {
                audioPlayer.startTrack(track.makeClone(),false);
            } else if(loopingPlaylist){
                queue.add(track.makeClone());
                skip();
            } else {
                skip();
            }
        }
    }

    @Override
    public void onTrackException(AudioPlayer player, AudioTrack track, FriendlyException exception) {
        System.err.println(exception.getMessage());
        exception.printStackTrace();
    }

    public ArrayList<AudioTrack> getQueue() {return queue;}

    public AudioTrack getPlayingTrack() {return audioPlayer.getPlayingTrack();}

    public void setNextTrackPlaytop(boolean nextTrackPlaytop) {
        this.nextTrackPlaytop = nextTrackPlaytop;
    }

    public void setLoopingTrack(boolean loopingTrack) {
        this.loopingTrack = loopingTrack;
        this.loopingPlaylist = false;
    }

    public void setLoopingPlaylist(boolean loopingPlaylist) {
        this.loopingPlaylist = loopingPlaylist;
        this.loopingTrack = false;
    }

    public boolean isLoopingTrack() {
        return loopingTrack;
    }

    public boolean isLoopingPlaylist() {
        return loopingPlaylist;
    }

    public boolean isPlaying() {
        return playing;
    }
}
