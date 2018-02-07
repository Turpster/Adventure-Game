package com._Turpster.AdventureGame.Entitys;

import com._Turpster.AdventureGame.Level.*;
import java.awt.*;

public class Player extends Entity
{
    boolean moving;
    
    public Player(final int x, final int y, final int width, final int height, final Level level, final int health, final int speed) {
        super(x, y, width, height, level, health, speed);
        this.moving = false;
    }
    
    @Override
    public void render(final Graphics g) {
        this.tick();
        if (this.playerModel == null) {
            final Color color = g.getColor();
            g.setColor(Color.white);
            g.fillRect(this.hitbox.x, this.hitbox.y, this.hitbox.width, this.hitbox.height);
            g.setColor(Color.MAGENTA);
            g.fillRect(this.hitbox.x, this.hitbox.y, this.hitbox.width / 2, this.hitbox.height / 2);
            g.fillRect(this.hitbox.x + this.hitbox.width / 2, this.hitbox.y + this.hitbox.height / 2, this.hitbox.width / 2, this.hitbox.height / 2);
            g.setColor(color);
        }
    }
    
    @Override
    public void tick() {
        if (this.velX != 0 || this.velY != 0) {
            this.moving = true;
        }
        else {
            this.moving = false;
        }
    }
}
