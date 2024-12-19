package org.example;

import java.awt.*;

public class Goomba {
    private float x, y;
    private float speed = 2f;
    private int width = 50, height = 50;
    private int direction = 1; // 1 prawo, -1 lewo
    private Rectangle platform;

    public Goomba(Rectangle platform) {
        this.platform = platform;
        this.x = platform.x; // Zaczyna na lewej krawędzi
        this.y = platform.y - height; // w pozycji y ciut wiekszą od platformy
    }

    public void update() {
        // Ruch goomby w kierunku ustawionym przez direction
        x += speed * direction;

        // Odwracamy kierunek gdy dojdzie do krawędzi platformy
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
