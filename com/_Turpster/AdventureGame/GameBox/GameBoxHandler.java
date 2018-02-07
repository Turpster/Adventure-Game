package com._Turpster.AdventureGame.GameBox;

import com._Turpster.AdventureGame.*;
import java.util.*;
import java.awt.event.*;
import java.awt.*;

public class GameBoxHandler
{
    Game game;
    ArrayList<GameBox> GameBoxes;
    
    public ArrayList<GameBox> getGameBoxes() {
        return this.GameBoxes;
    }
    
    public void setGameBoxes(final ArrayList<GameBox> gameBoxes) {
        this.GameBoxes = gameBoxes;
    }
    
    public Game getGame() {
        return this.game;
    }
    
    public void setGame(final Game game) {
        this.game = game;
    }
    
    public GameBoxHandler(final Game game) {
        this.GameBoxes = new ArrayList<GameBox>();
        this.game = game;
    }
    
    public void render(final Graphics g) {
        for (final GameBox GB : this.GameBoxes) {
            GB.render(g);
        }
    }
    
    public void addGameBox(final String Text) {
        this.GameBoxes.add(new GameBoxOption(this, Text, null));
    }
    
    public void addGameBox(final String Text, final BoxPurpose BP) {
        this.GameBoxes.add(new GameBoxOption(this, Text, BP));
    }
    
    public void handleClick(final MouseEvent e) {
        if (!this.GameBoxes.isEmpty()) {
            final GameBox focusedWindow = this.GameBoxes.get(this.GameBoxes.size() - 1);
            if (focusedWindow instanceof GameBoxOption) {
                final GameBoxOption GBO = (GameBoxOption)focusedWindow;
                if (GBO.getNoR().intersects(new Rectangle(e.getX(), e.getY(), 1, 1))) {
                    GBO.no();
                }
                else if (GBO.getYesR().intersects(new Rectangle(e.getX(), e.getY(), 1, 1))) {
                    GBO.yes();
                }
            }
        }
    }
}
