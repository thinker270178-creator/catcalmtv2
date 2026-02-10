package com.feline.calmtv;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Visual patterns based on feline vision research:
 * - Cats see best at 20-25 Hz flicker rate (vs 15-20 Hz for humans)
 * - Dichromatic vision: blues (450nm) and greens (550nm) most visible
 * - Motion detection superior to humans (especially horizontal movement)
 * - Peripheral vision optimized for detecting small, erratic movements
 * 
 * Research sources:
 * - Loop et al. (1987) - Temporal modulation sensitivity
 * - Guenther & Zrenner (1993) - Cone photoreceptor distribution
 * - Blake (1979) - Motion perception in cats
 */
public class CalmingVisualView extends SurfaceView implements Runnable {
    private Thread renderThread;
    private volatile boolean running = false;
    private SurfaceHolder holder;
    
    // Scientific parameters
    private static final int TARGET_FPS = 22; // Optimal for cat vision (20-25 Hz)
    private static final long FRAME_TIME = 1000 / TARGET_FPS;
    
    // Color wavelengths optimized for feline dichromatic vision
    private Paint bluePaint;    // ~450nm peak sensitivity
    private Paint greenPaint;   // ~550nm peak sensitivity
    private Paint cyanPaint;    // Blend zone
    
    // Movement patterns
    private float phase = 0;
    private float slowPhase = 0;
    
    // Particle system for gentle movement stimulation
    private Particle[] particles;
    private static final int PARTICLE_COUNT = 8; // Sparse for calmness
    
    public CalmingVisualView(Context context) {
        super(context);
        holder = getHolder();
        initPaints();
        initParticles();
    }
    
    private void initPaints() {
        // Blue-violet optimized for cat S-cones (peak ~450nm)
        bluePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bluePaint.setColor(Color.rgb(100, 120, 255));
        bluePaint.setAlpha(180);
        
        // Yellow-green optimized for cat M-cones (peak ~550nm)
        greenPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        greenPaint.setColor(Color.rgb(120, 200, 140));
        greenPaint.setAlpha(180);
        
        // Cyan blend
        cyanPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        cyanPaint.setColor(Color.rgb(100, 180, 200));
        cyanPaint.setAlpha(160);
    }
    
    private void initParticles() {
        particles = new Particle[PARTICLE_COUNT];
        for (int i = 0; i < PARTICLE_COUNT; i++) {
            particles[i] = new Particle();
        }
    }
    
    public void resume() {
        running = true;
        renderThread = new Thread(this);
        renderThread.start();
    }
    
    public void pause() {
        running = false;
        try {
            if (renderThread != null) {
                renderThread.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void run() {
        long lastTime = System.currentTimeMillis();
        
        while (running) {
            long currentTime = System.currentTimeMillis();
            long deltaTime = currentTime - lastTime;
            
            if (deltaTime >= FRAME_TIME) {
                if (holder.getSurface().isValid()) {
                    Canvas canvas = holder.lockCanvas();
                    if (canvas != null) {
                        draw(canvas);
                        holder.unlockCanvasAndPost(canvas);
                    }
                }
                lastTime = currentTime;
            } else {
                try {
                    Thread.sleep(FRAME_TIME - deltaTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    private void draw(Canvas canvas) {
        int width = canvas.getWidth();
        int height = canvas.getHeight();
        
        // Deep blue-black background (low luminance for relaxation)
        canvas.drawColor(Color.rgb(15, 20, 35));
        
        // Slow breathing-rhythm gradient waves
        drawBreathingWaves(canvas, width, height);
        
        // Gentle particle movement (prey-like but slow)
        updateAndDrawParticles(canvas, width, height);
        
        // Update phase for next frame
        phase += 0.02f; // Slow movement
        slowPhase += 0.005f; // Very slow ambient movement
    }
    
    /**
     * Breathing-rhythm waves at ~0.3 Hz (cat resting respiratory rate: 20-30/min)
     */
    private void drawBreathingWaves(Canvas canvas, int width, int height) {
        Path wavePath = new Path();
        
        // Top wave (blue)
        wavePath.reset();
        wavePath.moveTo(0, 0);
        for (int x = 0; x <= width; x += 10) {
            float y = (float) (Math.sin((x * 0.005) + slowPhase) * 60 + 150);
            if (x == 0) {
                wavePath.moveTo(x, y);
            } else {
                wavePath.lineTo(x, y);
            }
        }
        wavePath.lineTo(width, 0);
        wavePath.close();
        canvas.drawPath(wavePath, bluePaint);
        
        // Bottom wave (green)
        wavePath.reset();
        wavePath.moveTo(0, height);
        for (int x = 0; x <= width; x += 10) {
            float y = (float) (height - (Math.sin((x * 0.005) - slowPhase) * 60 + 150));
            if (x == 0) {
                wavePath.moveTo(x, y);
            } else {
                wavePath.lineTo(x, y);
            }
        }
        wavePath.lineTo(width, height);
        wavePath.close();
        canvas.drawPath(wavePath, greenPaint);
    }
    
    /**
     * Sparse particles with slow, smooth movement
     * Mimics distant prey movement to maintain mild interest without stress
     */
    private void updateAndDrawParticles(Canvas canvas, int width, int height) {
        for (Particle p : particles) {
            p.update(width, height, phase);
            
            // Soft glow effect
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setColor(p.color);
            paint.setAlpha((int) (100 * p.alpha));
            canvas.drawCircle(p.x, p.y, p.size * 2, paint);
            
            paint.setAlpha((int) (200 * p.alpha));
            canvas.drawCircle(p.x, p.y, p.size, paint);
        }
    }
    
    /**
     * Particle class with Perlin-noise-like smooth movement
     */
    private class Particle {
        float x, y;
        float size;
        float alpha;
        int color;
        float speedX, speedY;
        float phaseOffset;
        
        Particle() {
            reset();
            // Random initial position
            x = (float) (Math.random() * 1920);
            y = (float) (Math.random() * 1080);
        }
        
        void reset() {
            size = 15 + (float) (Math.random() * 25);
            alpha = 0.6f + (float) (Math.random() * 0.4);
            
            // Random blue-cyan-green hue
            if (Math.random() > 0.5) {
                color = Color.rgb(100, 150 + (int)(Math.random() * 80), 200 + (int)(Math.random() * 55));
            } else {
                color = Color.rgb(100 + (int)(Math.random() * 50), 180 + (int)(Math.random() * 50), 150);
            }
            
            phaseOffset = (float) (Math.random() * Math.PI * 2);
        }
        
        void update(int width, int height, float globalPhase) {
            // Slow, smooth sinusoidal movement
            float targetX = (float) (width * 0.5 + Math.cos(globalPhase * 0.3 + phaseOffset) * width * 0.3);
            float targetY = (float) (height * 0.5 + Math.sin(globalPhase * 0.2 + phaseOffset * 1.3) * height * 0.3);
            
            // Smooth interpolation
            x += (targetX - x) * 0.01f;
            y += (targetY - y) * 0.01f;
            
            // Gentle pulsing
            alpha = 0.5f + (float) (Math.sin(globalPhase + phaseOffset) * 0.3);
        }
    }
}
