package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Timer;
import java.util.TimerTask;

public class Gra extends JPanel implements Runnable {
    private Gracz player;
    private Goomba goomba;
    private Mushroom leftMushroom, rightMushroom;
    private Rectangle platform;
    private boolean running;
    public boolean leftPressed, rightPressed, jumping;
    private boolean speedBoostActive;
    private float gravity = 0.5f;
    private float jumpSpeed = -10f;
    private float playerSpeed = 5f; // Default player speed
    private int score = 0; // Score variable

    public Gra() {
        setDoubleBuffered(true);
        setFocusable(true);
        setVisible(true);
        setPreferredSize(new Dimension(800, 600));

        player = new Gracz();
        platform = new Rectangle(200, 400, 400, 20);
        goomba = new Goomba(platform);

        // Initialize mushrooms
        leftMushroom = new Mushroom(platform.x, platform.y - 30);
        rightMushroom = new Mushroom(platform.x + platform.width - 30, platform.y - 30);

        // Key listeners for controls
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT -> leftPressed = true;
                    case KeyEvent.VK_RIGHT -> rightPressed = true;
                    case KeyEvent.VK_SPACE -> {
                        if (!jumping) {
                            jumping = true;
                            player.playerYVelocity = jumpSpeed;
                        }
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT -> leftPressed = false;
                    case KeyEvent.VK_RIGHT -> rightPressed = false;
                }
            }
        });

        setFocusable(true);
        requestFocusInWindow();

        running = true;
        Thread gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        while (running) {
            long startTime = System.nanoTime();

            updateGame();

            long frameTime = System.nanoTime() - startTime;
            long sleepTime = Math.max(0, 16 - frameTime / 1_000_000);

            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private synchronized void updateGame() {
        // zaktualizowanie informacji o sterowaniu gracza
        player.setControlInfo(leftPressed, rightPressed, jumping);

        // ruch poziomy
        if (leftPressed) player.position[0] -= playerSpeed;
        if (rightPressed) player.position[0] += playerSpeed;

        // grawitacja i ruch pionowy
        player.playerYVelocity += gravity;
        player.position[1] += player.playerYVelocity;

        // kolizje z platformą
        if (player.playerYVelocity > 0 &&
                player.position[1] + 50 >= platform.y &&
                player.position[0] + 50 > platform.x &&
                player.position[0] < platform.x + platform.width) {
            player.position[1] = platform.y - 50;
            player.playerYVelocity = 0;
            jumping = false;
        }

        // kolizja z platformą
        if (player.position[1] + 50 >= 600) {
            player.position[1] = 550;
            player.playerYVelocity = 0;
            jumping = false;
        }

        // Aktualizacja stanu goomby
        goomba.update();

        // Sprawdź kolizje z goombą
        if (new Rectangle((int) player.position[0], (int) player.position[1], 50, 50)
                .intersects(goomba.getBounds())) {
            System.exit(0); // Close the app
        }

        // Kolizja z lewym grzybem
        if (leftMushroom.isActive() &&
                new Rectangle((int) player.position[0], (int) player.position[1], 50, 50)
                        .intersects(leftMushroom.getBounds())) {
            leftMushroom.collect();
            incrementScore();
            activateSpeedBoost();
        }

        // Kolizja z prawym grzybem
        if (rightMushroom.isActive() &&
                new Rectangle((int) player.position[0], (int) player.position[1], 50, 50)
                        .intersects(rightMushroom.getBounds())) {
            rightMushroom.collect();
            incrementScore();
            activateSpeedBoost();
        }

        repaint();
    }

    private void incrementScore() {
        score++;
    }

    private void activateSpeedBoost() {
        if (speedBoostActive) return;

        speedBoostActive = true;
        playerSpeed = 10f; //podwajamy predkosc po grzybie
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                speedBoostActive = false;
                playerSpeed = 5f; // Resetujemy predkosc
            }
        }, 1000); // Boost trwa sekunde
    }

    @Override
    protected synchronized void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Rysujemy tło
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());

        // Rysujemy gracza
        g.drawImage(player.getCurrentSprite(), (int) player.position[0], (int) player.position[1], 50, 50, null);

        // Rysujemy platforme
        g.setColor(Color.GREEN);
        g.fillRect(platform.x, platform.y, platform.width, platform.height);

        // Rysujemy goombe
        goomba.draw(g);

        // Rysujemy grzybki
        leftMushroom.draw(g);
        rightMushroom.draw(g);

        // Rysujemy wynik
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Score: " + score, 10, 20);
    }
}
