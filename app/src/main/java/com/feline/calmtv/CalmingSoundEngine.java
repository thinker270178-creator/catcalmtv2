package com.feline.calmtv;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioFormat;
import android.media.AudioTrack;

/**
 * Audio system based on feline psychoacoustic research:
 * 
 * Key frequencies:
 * - Purring: 25-50 Hz (healing frequency range - bone density, wound healing)
 * - Upper harmonics: 40-67 Hz (contentment, bonding)
 * - Species-specific music: Tempos based on cat purring (1380 bpm vs human 60-80 bpm)
 * - Frequency range: 55-200 Hz fundamental (cat vocalization range)
 * 
 * Research sources:
 * - Snowdon et al. (2015) "Cats prefer species-appropriate music" (Applied Animal Behaviour Science)
 * - von Muggenthaler (2001) "The felid purr: A healing mechanism?"
 * - Hampton et al. (2020) "Effects of music on behavior and physiological stress response"
 */
public class CalmingSoundEngine {
    private AudioTrack audioTrack;
    private Thread audioThread;
    private volatile boolean running = false;
    
    // Scientific parameters
    private static final int SAMPLE_RATE = 44100;
    private static final int BUFFER_SIZE = AudioTrack.getMinBufferSize(
        SAMPLE_RATE,
        AudioFormat.CHANNEL_OUT_STEREO,
        AudioFormat.ENCODING_PCM_16BIT
    );
    
    // Purr frequencies (Hz) - therapeutic range
    private static final float PURR_FUNDAMENTAL = 27.5f;  // Low purr
    private static final float PURR_HARMONIC_1 = 44.0f;   // Contentment frequency
    private static final float PURR_HARMONIC_2 = 55.0f;   // Upper harmonic
    
    // Musical frequencies optimized for cat hearing
    // Cats hear best at 500Hz-32kHz, with peak sensitivity around 8kHz
    // But for calming, we use lower frequencies similar to their vocalizations
    private static final float[] CAT_SCALE = {
        110.0f,  // A2 - low comfortable range
        123.47f, // B2
        146.83f, // D3
        164.81f, // E3
        196.00f, // G3
        220.00f  // A3
    };
    
    private int phase = 0;
    private float masterVolume = 0.15f; // Gentle volume
    
    public CalmingSoundEngine(Context context) {
        AudioAttributes attributes = new AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
            .build();
        
        AudioFormat format = new AudioFormat.Builder()
            .setSampleRate(SAMPLE_RATE)
            .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
            .setChannelMask(AudioFormat.CHANNEL_OUT_STEREO)
            .build();
        
        audioTrack = new AudioTrack(
            attributes,
            format,
            BUFFER_SIZE * 4,
            AudioTrack.MODE_STREAM,
            android.media.AudioManager.AUDIO_SESSION_ID_GENERATE
        );
    }
    
    public void start() {
        running = true;
        audioThread = new Thread(new Runnable() {
            @Override
            public void run() {
                generateAndPlayAudio();
            }
        });
        audioThread.start();
        audioTrack.play();
    }
    
    public void pause() {
        running = false;
        if (audioTrack != null) {
            audioTrack.pause();
        }
    }
    
    public void resume() {
        if (audioTrack != null) {
            running = true;
            audioTrack.play();
        }
    }
    
    public void release() {
        running = false;
        if (audioThread != null) {
            try {
                audioThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (audioTrack != null) {
            audioTrack.stop();
            audioTrack.release();
        }
    }
    
    /**
     * Generate continuous calming audio combining:
     * 1. Purr-frequency drone (25-67 Hz harmonics)
     * 2. Gentle melodic tones (cat vocalization range)
     * 3. Pink noise (natural, non-alarming)
     */
    private void generateAndPlayAudio() {
        short[] buffer = new short[BUFFER_SIZE];
        double time = 0;
        double melodicPhase = 0;
        int noteIndex = 0;
        double noteDuration = 4.0; // Very slow note changes (4 seconds each)
        double noteTimer = 0;
        
        while (running) {
            for (int i = 0; i < buffer.length; i += 2) { // Stereo
                double sample = 0;
                
                // 1. Purr foundation - multi-harmonic low frequency
                sample += generatePurr(time) * 0.4;
                
                // 2. Gentle melodic tones (very slow, simple)
                sample += generateMelody(time, melodicPhase, noteIndex) * 0.2;
                
                // 3. Subtle pink noise for naturalness
                sample += generatePinkNoise() * 0.05;
                
                // Apply master volume and convert to 16-bit
                short audioSample = (short) (sample * masterVolume * Short.MAX_VALUE);
                
                buffer[i] = audioSample;     // Left channel
                buffer[i + 1] = audioSample; // Right channel
                
                time += 1.0 / SAMPLE_RATE;
                noteTimer += 1.0 / SAMPLE_RATE;
                
                // Change note every 4 seconds
                if (noteTimer >= noteDuration) {
                    noteTimer = 0;
                    noteIndex = (noteIndex + 1) % CAT_SCALE.length;
                    melodicPhase = 0; // Reset phase for smooth transition
                }
            }
            
            audioTrack.write(buffer, 0, buffer.length);
        }
    }
    
    /**
     * Generate purr-like sound with healing frequencies
     * Research shows 25-50 Hz promotes healing and reduces stress
     */
    private double generatePurr(double time) {
        double purr = 0;
        
        // Fundamental purr frequency
        purr += Math.sin(2 * Math.PI * PURR_FUNDAMENTAL * time) * 0.5;
        
        // First harmonic (strong contentment association)
        purr += Math.sin(2 * Math.PI * PURR_HARMONIC_1 * time) * 0.3;
        
        // Second harmonic
        purr += Math.sin(2 * Math.PI * PURR_HARMONIC_2 * time) * 0.2;
        
        // Gentle amplitude modulation (breathing rhythm ~0.4 Hz)
        double breathMod = 0.7 + 0.3 * Math.sin(2 * Math.PI * 0.4 * time);
        purr *= breathMod;
        
        return purr;
    }
    
    /**
     * Generate simple melodic tones in cat-appropriate frequency range
     * Very slow, stepwise motion (no jarring jumps)
     */
    private double generateMelody(double time, double phase, int noteIndex) {
        float freq = CAT_SCALE[noteIndex];
        
        // Sine wave with soft attack/decay envelope
        double envelope = Math.sin(phase * Math.PI); // 0 to 1 to 0 over note duration
        double tone = Math.sin(2 * Math.PI * freq * time);
        
        return tone * envelope;
    }
    
    /**
     * Pink noise generator (1/f noise - found in nature)
     * More natural and less harsh than white noise
     */
    private double pinkNoiseB0 = 0, pinkNoiseB1 = 0, pinkNoiseB2 = 0;
    private double pinkNoiseB3 = 0, pinkNoiseB4 = 0, pinkNoiseB5 = 0, pinkNoiseB6 = 0;
    
    private double generatePinkNoise() {
        double white = Math.random() * 2 - 1;
        pinkNoiseB0 = 0.99886 * pinkNoiseB0 + white * 0.0555179;
        pinkNoiseB1 = 0.99332 * pinkNoiseB1 + white * 0.0750759;
        pinkNoiseB2 = 0.96900 * pinkNoiseB2 + white * 0.1538520;
        pinkNoiseB3 = 0.86650 * pinkNoiseB3 + white * 0.3104856;
        pinkNoiseB4 = 0.55000 * pinkNoiseB4 + white * 0.5329522;
        pinkNoiseB5 = -0.7616 * pinkNoiseB5 - white * 0.0168980;
        double pink = pinkNoiseB0 + pinkNoiseB1 + pinkNoiseB2 + pinkNoiseB3 + pinkNoiseB4 + pinkNoiseB5 + pinkNoiseB6 + white * 0.5362;
        pinkNoiseB6 = white * 0.115926;
        return pink * 0.11;
    }
}
