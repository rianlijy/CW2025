package com.comp2042.ui;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 * Manages sound effects and background music for the game.
 * Handles loading, playing, pausing, and volume control for all audio.
 */
public class Sound {

    private MediaPlayer musicPlayer;
    private MediaPlayer placeSFX;
    private MediaPlayer warningSFX;
    private boolean muted = false;
    private double lastVolume = 0.5;

    /**
     * Constructs a new Sound manager and initializes all audio.
     * Loads three audio files:
     * - Background music (bgm.mp3) loops continuously at 40% volume
     * - Place sound effect (place.mp3) at 100% volume
     * - Warning sound effect (garbage_warning.mp3) at 60% volume
     * - This is to make sure sound is equal
     * All audio files are loaded from resources directory. The background music loops continuously.
     */

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

    /**
     * Sets the volume for all audio elements with different multipliers for each type.
     * Only works if not muted.
     */
    public void setVolume(double v) {
        if (!muted) {
            lastVolume = v;
            musicPlayer.setVolume(v * 0.6);
            placeSFX.setVolume(Math.min(1.0, v * 1.5));
            warningSFX.setVolume(v * 0.8);
        }
    }

    /**
     * Toggles mute state, storing the last volume to restore when unmuted.
     */
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
