package com._Turpster.AdventureGame.GameBox;

import com._Turpster.AdventureGame.*;
import java.awt.*;

public abstract class GameBox implements Renderable
{
    GameBoxHandler gbh;
    Rectangle Box;
    Rectangle closebox;
    Image closeicon;
    int WIDTH;
    int HEIGHT;
    Game game;
    protected boolean closeable;
    
    public void setCloseable(final boolean condition) {
        this.closeable = condition;
    }
    
    public boolean isCloseable() {
        return this.closeable;
    }
    
    public GameBox(final GameBoxHandler gbh) {
        this.WIDTH = 500;
        this.HEIGHT = 100;
        this.closeable = true;
        this.gbh = gbh;
        this.game = gbh.getGame();
        this.Box = new Rectangle(this.game.getWidth() / 2 - this.WIDTH / 2, this.game.getHeight() / 2 - this.HEIGHT / 2, this.WIDTH, this.HEIGHT);
        this.closebox = new Rectangle(this.Box.x + (this.Box.width - 35), this.Box.y, 30, 20);
    }
    
    public abstract void subrender(final Graphics p0);
    
    @Override
    public void render(final Graphics g) {
        final Graphics2D g2D = (Graphics2D)g;
        g.setColor(Color.white);
        g2D.setComposite(Game.makeTransparent(0.93f));
        g.fillRoundRect(this.Box.x, this.Box.y, this.Box.width, this.Box.height, 5, 5);
        g.setColor(Color.red);
        if (this.closeable) {
            g2D.setComposite(Game.makeTransparent(1.0f));
        }
        else {
            g2D.setComposite(Game.makeTransparent(0.5f));
        }
        g.fillRect(this.closebox.x, this.closebox.y, this.closebox.width, this.closebox.height);
        g2D.setComposite(Game.makeTransparent(0.93f));
        this.subrender(g);
        g2D.setComposite(Game.makeTransparent(1.0f));
    }
}
