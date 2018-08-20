package com._Turpster.AdventureGame;

import java.io.*;

public class SaveHandler
{
    private Game game;
    
    public SaveHandler(final Game game) {
        this.game = game;
    }
    
    public void saveGame() {
        try {
            final FileOutputStream saveFile = new FileOutputStream("gamesave.dat");
            final ObjectOutputStream save = new ObjectOutputStream(saveFile);
            save.writeObject(this.game.getCharacter());
            save.close();
        }
        catch (Exception exc) {
            exc.printStackTrace();
        }
    }
    
    public void loadGame(final Game game) {
        try {
            final FileInputStream saveFile = new FileInputStream("gamesave.dat");
            final ObjectInputStream save = new ObjectInputStream(saveFile);
            game.setCharacter((Character)save.readObject());
            save.close();
        }
        catch (ClassNotFoundException | IOException ex2) {
            ex2.printStackTrace();
        }
    }
    
    public void saveGame(final String datafile) {
        try {
            final FileOutputStream saveFile = new FileOutputStream(datafile);
            final ObjectOutputStream save = new ObjectOutputStream(saveFile);
            save.writeObject(this.game.getCharacter());
            save.close();
        }
        catch (Exception exc) {
            exc.printStackTrace();
        }
    }
    
    public void loadGame(final Game game, final String datafile) throws StreamCorruptedException, ClassCastException, NullPointerException {
        try {
            final FileInputStream saveFile = new FileInputStream(datafile);
            final ObjectInputStream save = new ObjectInputStream(saveFile);
            game.setCharacter((Character)save.readObject());
            save.close();
        }
        catch (ClassNotFoundException | IOException ex2) {
            ex2.printStackTrace();
        }
    }
    
    public void saveGameState() {
    }
    
    public void loadGameState() {
    }
}
