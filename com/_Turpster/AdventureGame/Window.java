package com._Turpster.AdventureGame;

import javax.swing.*;
import java.awt.*;

public class Window extends Canvas
{
    private static final long serialVersionUID = -7464844025450212696L;
    
    public Window(final int width, final int height, final String title, final Game game) {
        final JFrame frame = new JFrame(title);
        frame.setSize(Toolkit.getDefaultToolkit().getScreenSize());
        game.setHEIGHT(Toolkit.getDefaultToolkit().getScreenSize().height);
        game.setWIDTH(Toolkit.getDefaultToolkit().getScreenSize().width);
        frame.setDefaultCloseOperation(3);
        frame.setResizable(false);
        frame.setUndecorated(true);
        frame.setLocationRelativeTo(null);
        frame.add(game);
        frame.setVisible(true);
        game.start();
    }
}
