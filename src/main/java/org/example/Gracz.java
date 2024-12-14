package org.example;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import javax.imageio.ImageIO;

public class Gracz {
    public float[] position;
    public float playerYVelocity = 0;
    public int[] infoControls;
    public BufferedImage[] IdleSprites;
    public BufferedImage[] runSprites;
    private BufferedImage currentImage;
    private boolean facingRight = true; // Tracks the player's direction

    int animationID = 0;
    Timer animationTimer;

    Gracz() {
        IdleSprites = new BufferedImage[2]; // Left and right idle
        runSprites = new BufferedImage[3]; // Only right-facing running sprites

        IdleSprites[0] = loadImage("mario_left_idle.png");
        IdleSprites[1] = loadImage("mario_right_idle.png");

        runSprites[0] = loadImage("mario_right_01.png");
        runSprites[1] = loadImage("mario_right_02.png");
        runSprites[2] = loadImage("mario_right_03.png");

        position = new float[]{550.0f, 100.0f};
        infoControls = new int[3];

        startAnimationCycle();
    }

    private BufferedImage loadImage(String imagePath) {
        try {
            return ImageIO.read(new File(imagePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void startAnimationCycle() {
        animationTimer = new Timer();
        animationTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                updateAnimation();
            }
        }, 0, 200); // Updates every 200ms
    }

    private void updateAnimation() {
        BufferedImage[] activeSprites = getActiveSprites();
        if (activeSprites != null && activeSprites.length > 0) {
            animationID = (animationID + 1) % activeSprites.length;
            currentImage = activeSprites[animationID];

            // Flip the sprite if moving left
            if (!facingRight) {
                currentImage = flipImageHorizontally(currentImage);
            }
        }
    }

    private BufferedImage[] getActiveSprites() {
        if (infoControls[0] == 1) { // Moving left
            facingRight = false;
            return runSprites; // Use run animation, flipped later
        } else if (infoControls[1] == 1) { // Moving right
            facingRight = true;
            return runSprites;
        } else { // Idle
            return new BufferedImage[]{IdleSprites[facingRight ? 1 : 0]};
        }
    }

    public BufferedImage getCurrentSprite() {
        if (currentImage == null) {
            currentImage = IdleSprites[1]; // Default to right idle
        }
        return currentImage;
    }

    private BufferedImage flipImageHorizontally(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage flippedImage = new BufferedImage(width, height, image.getType());
        Graphics2D g = flippedImage.createGraphics();
        g.drawImage(image, 0, 0, width, height, width, 0, 0, height, null);
        g.dispose();
        return flippedImage;
    }

    public void setControlInfo(boolean a, boolean d, boolean space) {
        infoControls[0] = a ? 1 : 0;
        infoControls[1] = d ? 1 : 0;
        infoControls[2] = space ? 1 : 0;
    }
}
