package com._Turpster.AdventureGame.Entitys;

import com._Turpster.AdventureGame.Level.*;
import java.awt.*;

public abstract class Entity
{
    boolean hasHealth;
    int health;
    int speed;
    int characterFacing;
    int velX;
    int velY;
    Rectangle hitbox;
    Image playerModel;
    
    public Entity(final int x, final int y, final int width, final int height, final Level level, final int health, final int speed) {
        this.characterFacing = 0;
        this.hitbox = new Rectangle(x, y, width, height);
        level.addEntity(this);
        this.speed = speed;
        if (health != 0) {
            this.health = health;
            this.hasHealth = true;
        }
        else {
            this.hasHealth = false;
        }
    }
    
    public void render(final Graphics g) {
    }
    
    public void tick() {
    }
    
    public boolean isHasHealth() {
        return this.hasHealth;
    }
    
    public void setHasHealth(final boolean hasHealth) {
        this.hasHealth = hasHealth;
    }
    
    public int getHealth() {
        return this.health;
    }
    
    public void setHealth(final int health) {
        this.health = health;
    }
    
    public int getSpeed() {
        return this.speed;
    }
    
    public void setSpeed(final int speed) {
        this.speed = speed;
    }
    
    public int getCharacterFacing() {
        return this.characterFacing;
    }
    
    public void setCharacterFacing(final int characterFacing) {
        this.characterFacing = characterFacing;
    }
    
    public int getVelX() {
        return this.velX;
    }
    
    public void setVelX(final int velX) {
        this.velX = velX;
    }
    
    public int getVelY() {
        return this.velY;
    }
    
    public void setVelY(final int velY) {
        this.velY = velY;
    }
    
    public Rectangle getHitbox() {
        return this.hitbox;
    }
    
    public void setHitbox(final Rectangle hitbox) {
        this.hitbox = hitbox;
    }
    
    public Image getPlayerModel() {
        return this.playerModel;
    }
    
    public void setPlayerModel(final Image playerModel) {
        this.playerModel = playerModel;
    }
}
