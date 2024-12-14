package org.example;

import java.awt.*;

public class Goomba {
    private float x, y; // Goomba's position
    private float speed = 2f; // Speed of movement
    private int width = 50, height = 50; // Size of the Goomba
    private int direction = 1; // 1 for right, -1 for left
    private Rectangle platform; // Reference to the platform

    public Goomba(Rectangle platform) {
        this.platform = platform;
        this.x = platform.x; // Start at the left edge of the platform
        this.y = platform.y - height; // Position just above the platform
    }

    public void update() {
        // Move the Goomba in the current direction
        x += speed * direction;

        // Reverse direction when reaching the platform's edges
        if (x <= platform.x || x + width >= platform.x + platform.width) {
            direction *= -1;
        }
    }

    public void draw(Graphics g) {
        // Draw the Goomba as a red square
        g.setColor(Color.RED);
        g.fillRect((int) x, (int) y, width, height);
    }

    public Rectangle getBounds() {
        return new Rectangle((int) x, (int) y, width, height);
    }
}
