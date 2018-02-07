package com._Turpster.AdventureGame;

import java.awt.*;

public class Fade implements Tickable
{
    Game game;
    private final Rectangle fadebox;
    float increment;
    float fadevalue;
    
    public Fade(final Game game) {
        this.increment = 0.0f;
        this.fadevalue = 0.0f;
        this.game = game;
        this.fadebox = new Rectangle(0, 0, game.WIDTH, game.HEIGHT);
    }
    
    @Override
    public void tick() {
        this.fade();
    }
    
    public void render(final Graphics g) {
        final Graphics2D g2D = (Graphics2D)g;
        g2D.setComposite(Game.makeTransparent(this.fadevalue));
        final Color color = g.getColor();
        g.setColor(Color.BLACK);
        g.fillRect(this.fadebox.x, this.fadebox.y, this.fadebox.width, this.fadebox.height);
        g2D.setComposite(Game.makeTransparent(1.0f));
        g.setColor(color);
    }
    
    public void fade() {
        if (this.fadevalue + this.increment <= 1.0f) {
            if (this.fadevalue + this.increment >= 0.0f) {
                this.fadevalue += this.increment;
            }
            else {
                this.fadevalue = 0.0f;
            }
        }
        else {
            this.fadevalue = 1.0f;
        }
    }
    
    public float getIncrement() {
        return this.increment;
    }
    
    public void setIncrement(final float increment) {
        this.increment = increment;
    }
    
    public float getFadevalue() {
        return this.fadevalue;
    }
    
    public void setFadevalue(final float fadevalue) {
        this.fadevalue = fadevalue;
    }
}
