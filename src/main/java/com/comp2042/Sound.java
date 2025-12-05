package com.comp2042;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class Sound {

    private MediaPlayer musicPlayer;
    private MediaPlayer placeSFX;
    private MediaPlayer warningSFX;

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
    public void stopMusic() {
        musicPlayer.stop();
    }
    public void pauseMusic() {
        musicPlayer.pause();
    }
    public void resumeMusic() {
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
}
