package com._Turpster.AdventureGame;

import java.awt.event.*;

public class KeyboardInput extends KeyAdapter
{
    Game game;
    
    public KeyboardInput(final Game game) {
        this.game = game;
    }
    
    @Override
    public void keyPressed(final KeyEvent e) {
        super.keyPressed(e);
        if (e.getKeyCode() == 27) {
            if (this.game.isCommandCaptured()) {
                this.game.setCommandCaptured(false);
            }
            else {
                this.game.setCommandCaptured(true);
            }
        }
        else {
            this.game.isCommandCaptured();
        }
    }
    
    @Override
    public void keyReleased(final KeyEvent e) {
        super.keyReleased(e);
    }
    
    @Override
    public void keyTyped(final KeyEvent e) {
        if (this.game.isCommandCaptured()) {
            if (e.getKeyChar() == KeyEvent.getExtendedKeyCodeForChar(8)) {
                if (this.game.getCommands().length() > 2) {
                    this.game.setCommands(this.game.getCommands().substring(0, this.game.getCommands().length() - 1));
                }
            }
            else if (e.getKeyChar() == KeyEvent.getExtendedKeyCodeForChar(10)) {
                this.game.commandExecute();
            }
            else if (e.getKeyChar() != KeyEvent.getExtendedKeyCodeForChar(27)) {
                this.game.setCommands(String.valueOf(this.game.getCommands()) + e.getKeyChar());
            }
        }
    }
}
