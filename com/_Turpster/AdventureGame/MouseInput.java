package com._Turpster.AdventureGame;

import java.awt.event.*;

public class MouseInput extends MouseAdapter
{
    Game game;
    
    public MouseInput(final Game game) {
        this.game = game;
    }
    
    @Override
    public void mousePressed(final MouseEvent e) {
        super.mousePressed(e);
    }
    
    @Override
    public void mouseReleased(final MouseEvent e) {
        if (this.game.getGBH().getGameBoxes().isEmpty()) {
            super.mouseReleased(e);
            if (Game.mt == MenuType.StartingMenu) {
                if (this.game.getMenu().StartGame.inside(e.getX(), e.getY())) {
                    this.game.getMenu().StartGameClicked();
                }
                if (this.game.getMenu().Load.inside(e.getX(), e.getY())) {
                    this.game.getMenu().loadClicked();
                }
                if (this.game.getMenu().End.inside(e.getX(), e.getY())) {
                    this.game.getMenu().endClicked();
                }
                if (this.game.getMenu().createCharacter.inside(e.getX(), e.getY())) {
                    this.game.getMenu().createCharacterClicked();
                }
                if (this.game.isDebug() && this.game.getMenu().command.inside(e.getX(), e.getY())) {
                    this.game.getMenu().commandsClicked();
                }
            }
        }
        else {
            this.game.getGBH().handleClick(e);
        }
    }
    
    @Override
    public void mouseClicked(final MouseEvent e) {
    }
    
    @Override
    public void mouseExited(final MouseEvent e) {
        super.mouseExited(e);
    }
    
    @Override
    public void mouseEntered(final MouseEvent e) {
        super.mouseEntered(e);
    }
    
    @Override
    public void mouseDragged(final MouseEvent e) {
        super.mouseDragged(e);
    }
    
    @Override
    public void mouseMoved(final MouseEvent e) {
        super.mouseMoved(e);
    }
}
