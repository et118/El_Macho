package com.github.et118.El_Macho.Music;

import com.sedmelluq.discord.lavaplayer.format.StandardAudioDataFormats;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.playback.MutableAudioFrame;
import discord4j.voice.AudioProvider;

import java.nio.ByteBuffer;

public class LavaPlayerProvider extends AudioProvider {

    private AudioPlayer audioPlayer;
    private MutableAudioFrame frame = new MutableAudioFrame();

    public LavaPlayerProvider(AudioPlayer audioPlayer) {
        super(ByteBuffer.allocate(StandardAudioDataFormats.DISCORD_OPUS.maximumChunkSize()));
        frame.setBuffer(getBuffer());
        this.audioPlayer = audioPlayer;
    }

    @Override
    public boolean provide() {
        boolean didProvide = audioPlayer.provide(frame);
        if(didProvide) {
            getBuffer().flip();
        }
        return didProvide;
    }
}
