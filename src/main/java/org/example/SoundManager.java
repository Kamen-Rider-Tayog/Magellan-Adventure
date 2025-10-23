package org.example;

import javax.sound.sampled.*;
import java.io.File;
import java.io.InputStream;

public class SoundManager {
    private Clip backgroundMusic;
    private float volume = 0.5f; // Default volume (0.0 to 1.0)

    public SoundManager() {
        loadBackgroundMusic();
    }

    private void loadBackgroundMusic() {
        try {
            // Try to load from resources first
            InputStream audioStream = getClass().getClassLoader()
                    .getResourceAsStream("assets/sound/backgroundMusic.wav");

            if (audioStream != null) {
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(audioStream);
                backgroundMusic = AudioSystem.getClip();
                backgroundMusic.open(audioInput);
            } else {
                // Fallback to file system
                File audioFile = new File("assets/sound/backgroundMusic.wav");
                if (audioFile.exists()) {
                    AudioInputStream audioInput = AudioSystem.getAudioInputStream(audioFile);
                    backgroundMusic = AudioSystem.getClip();
                    backgroundMusic.open(audioInput);
                } else {
                    System.err.println("Background music file not found: assets/sound/backgroundMusic.wav");
                    return;
                }
            }

            // Set the clip to loop continuously
            backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);

            // Set initial volume
            setVolume(volume);

            // AUTO-START THE MUSIC IMMEDIATELY
            backgroundMusic.start();

        } catch (Exception e) {
            System.err.println("Error loading background music: " + e.getMessage());
        }
    }

    public void setVolume(float volume) {
        this.volume = Math.max(0.0f, Math.min(1.0f, volume)); // Clamp between 0 and 1

        if (backgroundMusic != null) {
            FloatControl gainControl = (FloatControl) backgroundMusic.getControl(FloatControl.Type.MASTER_GAIN);
            float dB = (float) (Math.log(this.volume) / Math.log(10.0) * 20.0);
            gainControl.setValue(dB);
        }
    }

    public float getVolume() {
        return volume;
    }
}