package com._Turpster.AdventureGame;

import java.io.*;
import com._Turpster.AdventureGame.Exceptions.*;

public class Inventory implements Serializable
{
    private static final long serialVersionUID = -3791450500481804564L;
    Item[] items;
    int slots;
    
    public Inventory(final int slots) {
        this.slots = slots;
    }
    
    public Inventory(final File config) throws InvalidGameData {
    }
    
    public void addItem(final Item item, final int slot) {
        this.items[slot] = item;
    }
    
    public void removeItem(final int slot) {
        this.items[slot] = null;
    }
    
    public Item getItem(final int slot) {
        return this.items[slot];
    }
}
