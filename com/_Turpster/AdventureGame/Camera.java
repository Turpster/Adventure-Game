package com._Turpster.AdventureGame;

import com._Turpster.AdventureGame.Entitys.*;
import java.awt.*;

public class Camera implements Renderable, Tickable
{
    int xt;
    int yt;
    Game game;
    
    public int getXt() {
        return this.xt;
    }
    
    public void setXt(final int xt) {
        this.xt = xt;
    }
    
    public int getYt() {
        return this.yt;
    }
    
    public void setYt(final int yt) {
        this.yt = yt;
    }
    
    public Camera(final Game game) {
        this.xt = 0;
        this.yt = 0;
        this.game = game;
    }
    
    @Override
    public void tick() {
        for (final Entity e : this.game.getLevel().getEntitys()) {
            if (e instanceof Player) {
                this.focus(e);
            }
        }
    }
    
    @Override
    public void render(final Graphics g) {
        if (this.game.getLevel() != null) {
            this.game.getLevel().render(g, this.xt, this.yt);
        }
    }
    
    public void focus(final Entity e) {
        Point focuspoint = new Point((int)(e.getHitbox().getX() - e.getHitbox().getWidth() / 2.0), (int)(e.getHitbox().getY() - e.getHitbox().getHeight() / 2.0));
        focuspoint = new Point(0, 0);
        this.xt = (int)(this.game.WIDTH / 2 + focuspoint.getX());
        this.yt = (int)(this.game.HEIGHT / 2 + focuspoint.getY());
    }
}
