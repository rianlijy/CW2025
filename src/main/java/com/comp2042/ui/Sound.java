package com.comp2042.ui;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class Sound {

    private MediaPlayer musicPlayer;
    private MediaPlayer placeSFX;
    private MediaPlayer warningSFX;
    private boolean muted = false;
    private double lastVolume = 0.5;

    public Sound() {
        musicPlayer = load("/sounds/bgm.mp3", true);
        placeSFX = load("/sounds/place.mp3", false);
        warningSFX = load("/sounds/garbage_warning.mp3", false);

        musicPlayer.setVolume(0.4);
        placeSFX.setVolume(1.0);
        warningSFX.setVolume(0.6);
    }

    private MediaPlayer load(String path, boolean loop) {
        Media media = new Media(getClass().getResource(path).toExternalForm());
        MediaPlayer mp = new MediaPlayer(media);
        if (loop) mp.setCycleCount(MediaPlayer.INDEFINITE);
        return mp;
    }

    public void startMusic() {
        musicPlayer.play();
    }

    public void playPlace() {
        placeSFX.stop();
        placeSFX.play();
    }
    public void playWarning() {
        warningSFX.stop();
        warningSFX.play();
    }

    public void setVolume(double v) {
        if (!muted) {
            lastVolume = v;
            musicPlayer.setVolume(v * 0.6);
            placeSFX.setVolume(Math.min(1.0, v * 1.5));
            warningSFX.setVolume(v * 0.8);
        }
    }
    public void toggleMute() {
        if (!muted) {
            muted = true;
            musicPlayer.setVolume(0);
            placeSFX.setVolume(0);
            warningSFX.setVolume(0);
        } else {
            muted = false;
            setVolume(lastVolume);
        }
    }

    public boolean isMuted() {
        return muted;
    }

    public double getLastVolume() {
        return lastVolume;
    }
}
