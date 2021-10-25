package com.company;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        JFrame f = new JFrame("2D Transform");
        f.setSize(1050, 800);
        f.setLocation(100, 50);
        f.setResizable(false);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new Canva();

        f.add(panel);
        f.setVisible(true);
    }
}
