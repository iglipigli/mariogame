package org.example;

import java.awt.*;

public class Mushroom {
    private float x, y;
    private int width = 30, height = 30;
    private boolean active;
    private Rectangle platform;

    public Mushroom(float x, float y) {
        this.x = x;
        this.y = y;
        this.active = true;
    }

    public void draw(Graphics g) {
        if (active) {
            g.setColor(Color.ORANGE);
            g.fillRect((int) x, (int) y, width, height);
        }
    }

    public Rectangle getBounds() {
        return new Rectangle((int) x, (int) y, width, height);
    }

    public boolean isActive() {
        return active;
    }

    public void collect() {
        active = false;
        new Thread(this::respawn).start();
    }

    private void respawn() {
        try {
            Thread.sleep(3000);
            active = true;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
