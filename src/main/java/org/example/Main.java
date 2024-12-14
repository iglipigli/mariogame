package org.example;
import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Gra gra = new Gra();
        frame.getContentPane().add(gra);
        frame.setPreferredSize(new Dimension(800,600));
        frame.setVisible(true);
        frame.pack();
    }
}