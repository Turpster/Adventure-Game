package com._Turpster.AdventureGame;

import java.util.logging.*;
import java.io.*;
import com._Turpster.AdventureGame.Runnable.*;

public class Commands extends CommandExecutor
{
    private Game game;
    
    public Commands(final Game game) {
        this.game = game;
    }
    
    @Override
    public boolean onCommand(final String commandLabel, final String[] args) {
        if (commandLabel.equalsIgnoreCase("setcharacter")) {
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("null")) {
                    this.game.setCharacter(null);
                }
                else if (CharacterType.getCharacter(args[0]) != null) {
                    this.game.setCharacter(new Character(CharacterType.getCharacter(args[0]), 0, 100, 1, 1, 1, 0, 100, new Inventory(30)));
                }
            }
            return true;
        }
        if (commandLabel.equalsIgnoreCase("load")) {
            if (args.length == 1) {
                try {
                    this.game.getSh().loadGame(this.game, args[0]);
                }
                catch (StreamCorruptedException | ClassCastException ex2) {
                    this.game.getLogger().log(Level.SEVERE, "Invalid Save.");
                }
            }
            else {
                if (args.length != 0) {
                    return false;
                }
                this.game.getSh().loadGame(this.game);
            }
            return true;
        }
        if (commandLabel.equalsIgnoreCase("save")) {
            if (args.length == 1) {
                this.game.getSh().saveGame(args[0]);
            }
            else {
                if (args.length != 0) {
                    return false;
                }
                this.game.getSh().saveGame();
            }
            return true;
        }
        if (commandLabel.equalsIgnoreCase("box")) {
            if (args.length == 0) {
                this.game.getGBH().addGameBox("This is a test box");
            }
            else {
                String message = "";
                for (int i = 0; i < args.length; ++i) {
                    message = String.valueOf(message) + args[i];
                    if (i != args.length - 1) {
                        message = String.valueOf(message) + " ";
                    }
                }
                this.game.getGBH().addGameBox(message);
            }
            return false;
        }
        if (!commandLabel.equalsIgnoreCase("debug")) {
            return false;
        }
        if (args.length != 1) {
            return false;
        }
        if (args[0].equalsIgnoreCase("runnable")) {
            new TurpRunnable(this.game, 50, 100) {
                @Override
                public void run() {
                    System.out.println("This is a test");
                }
            };
            return true;
        }
        return false;
    }
}
