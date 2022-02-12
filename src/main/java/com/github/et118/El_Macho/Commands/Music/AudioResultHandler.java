package com.github.et118.El_Macho.Commands.Music;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

public class AudioResultHandler implements AudioLoadResultHandler {

    private AudioPlayer audioPlayer;
    private TrackScheduler trackScheduler;
    private LoadStatus loadStatus;
    public enum LoadStatus {
        TrackLoaded,
        PlaylistLoaded,
        NoMatches,
        LoadFailed,
        Nothing
    }

    public AudioResultHandler (AudioPlayer audioPlayer, TrackScheduler trackScheduler) {
        this.audioPlayer = audioPlayer;
        this.trackScheduler = trackScheduler;
        this.loadStatus = LoadStatus.Nothing;
    }

    @Override
    public void trackLoaded(AudioTrack track) {
        trackScheduler.play(track,true);
        loadStatus = LoadStatus.TrackLoaded;
    }

    @Override
    public void playlistLoaded(AudioPlaylist playlist) {
        if(playlist.isSearchResult()) {
            trackScheduler.play(playlist.getTracks().get(0),true);
            loadStatus = LoadStatus.TrackLoaded;
        } else {
            trackScheduler.play(playlist,true);
            loadStatus = LoadStatus.PlaylistLoaded;
        }
    }

    @Override
    public void noMatches() {
        System.out.println("No matches found");
        loadStatus = LoadStatus.NoMatches;
    }

    @Override
    public void loadFailed(FriendlyException exception) {
        System.out.println("Load Failed: " + exception.getMessage());
        loadStatus = LoadStatus.LoadFailed;
    }

    public LoadStatus getLoadStatus() {return loadStatus;}
}
