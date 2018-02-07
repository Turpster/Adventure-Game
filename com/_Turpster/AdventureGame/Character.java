package com._Turpster.AdventureGame;

import java.io.*;
import com._Turpster.AdventureGame.Exceptions.*;

public class Character implements Serializable
{
    private static final long serialVersionUID = 8877768762956006183L;
    private int level;
    private int healthpoints;
    private int agilitypoints;
    private int speedpoints;
    private int strengthpoints;
    private int score;
    private int magicpoints;
    private Inventory inventory;
    private CharacterType ct;
    private int uniqueCharacterNum;
    
    public Character(final CharacterType ct, final int level, final int healthpoints, final int agilitypoints, final int speedpoints, final int strengthpoints, final int score, final int magicpoints, final Inventory startingInv, final int uniqueCharacterNum) {
        this.uniqueCharacterNum = uniqueCharacterNum;
        this.ct = ct;
        this.level = level;
        this.healthpoints = healthpoints;
        this.agilitypoints = agilitypoints;
        this.speedpoints = speedpoints;
        this.strengthpoints = strengthpoints;
        this.score = score;
        this.magicpoints = magicpoints;
        this.inventory = startingInv;
    }
    
    public Character(final CharacterType ct, final int level, final int healthpoints, final int agilitypoints, final int speedpoints, final int strengthpoints, final int score, final int magicpoints, final Inventory startingInv) {
        this.uniqueCharacterNum = 1;
        this.ct = ct;
        this.level = level;
        this.healthpoints = healthpoints;
        this.agilitypoints = agilitypoints;
        this.speedpoints = speedpoints;
        this.strengthpoints = strengthpoints;
        this.score = score;
        this.magicpoints = magicpoints;
        this.inventory = startingInv;
    }
    
    public Character(final File config) throws InvalidGameData {
    }
    
    public int getHealthpoints() {
        return this.healthpoints;
    }
    
    public void setHealthpoints(final int healthpoints) {
        this.healthpoints = healthpoints;
    }
    
    public int getAgilitypoints() {
        return this.agilitypoints;
    }
    
    public void setAgilitypoints(final int agilitypoints) {
        this.agilitypoints = agilitypoints;
    }
    
    public int getSpeedpoints() {
        return this.speedpoints;
    }
    
    public void setSpeedpoints(final int speedpoints) {
        this.speedpoints = speedpoints;
    }
    
    public int getStrengthpoints() {
        return this.strengthpoints;
    }
    
    public void setStrengthpoints(final int strengthpoints) {
        this.strengthpoints = strengthpoints;
    }
    
    public int getScore() {
        return this.score;
    }
    
    public void setScore(final int score) {
        this.score = score;
    }
    
    public int getMagicpoints() {
        return this.magicpoints;
    }
    
    public void setMagicpoints(final int magicpoints) {
        this.magicpoints = magicpoints;
    }
    
    public Inventory getInventory() {
        return this.inventory;
    }
    
    public void setInventory(final Inventory inventory) {
        this.inventory = inventory;
    }
    
    public CharacterType getCt() {
        return this.ct;
    }
    
    public void setCt(final CharacterType ct) {
        this.ct = ct;
    }
    
    public int getLevel() {
        return this.level;
    }
    
    public void setLevel(final int level) {
        this.level = level;
    }
    
    public int getUniqueCharacterNum() {
        return this.uniqueCharacterNum;
    }
    
    public void setUniqueCharacterNum(final int uniqueCharacterNum) {
        this.uniqueCharacterNum = uniqueCharacterNum;
    }
}
