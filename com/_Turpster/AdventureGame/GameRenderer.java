package com._Turpster.AdventureGame;

import com._Turpster.AdventureGame.Runnable.*;
import java.awt.image.*;
import com._Turpster.AdventureGame.GameBox.*;
import java.awt.*;
import java.util.logging.*;
import java.io.*;
import javax.swing.*;

public class GameRenderer
{
    Game game;
    Rectangle StartGame;
    Rectangle Load;
    Rectangle End;
    Rectangle createCharacter;
    Rectangle command;
    int Intro;
    String message;
    char[] messagechar;
    int charnum;
    String introtext;
    
    public String getMessage() {
        return this.message;
    }
    
    public void setMessage(final String message) {
        this.message = message;
    }
    
    public String getIntrotext() {
        return this.introtext;
    }
    
    public void setIntrotext(final String introtext) {
        this.introtext = introtext;
    }
    
    public int getCharnum() {
        return this.charnum;
    }
    
    public void setCharnum(final int charnum) {
        this.charnum = charnum;
    }
    
    public int getIntro() {
        return this.Intro;
    }
    
    public void setIntro(final int intro) {
        this.Intro = intro;
    }
    
    public GameRenderer(final Game game) {
        this.Intro = 0;
        this.message = "Our story begins with a character named Spyro";
        this.messagechar = this.message.toCharArray();
        this.charnum = 0;
        this.introtext = "";
        this.game = game;
        this.StartGame = new Rectangle(game.WIDTH / 2 - 50, game.HEIGHT / 2 - 12 - 80, 100, 24);
        this.Load = new Rectangle(game.WIDTH / 2 - 50, game.HEIGHT / 2 - 12 - 50, 100, 24);
        this.End = new Rectangle(game.WIDTH / 2 - 50, game.HEIGHT / 2 - 12 - 20, 100, 24);
    }
    
    public void render(final Graphics g) {
        if (Game.mt == MenuType.StartingMenu) {
            this.StartGame = new Rectangle(this.game.WIDTH / 2 - 50, this.game.HEIGHT / 2 - 12 - 80, 100, 24);
            this.Load = new Rectangle(this.game.WIDTH / 2 - 50, this.game.HEIGHT / 2 - 12 - 50, 100, 24);
            this.End = new Rectangle(this.game.WIDTH / 2 - 50, this.game.HEIGHT / 2 - 12 - 20, 100, 24);
            this.loadMenuCharacter(this.game.WIDTH / 2 - 135, this.game.HEIGHT / 2 + 15, g, true);
            final Graphics2D g2D = (Graphics2D)g;
            g2D.setComposite(this.makeTransparent(0.8f));
            g.setColor(new Color(255, 255, 255));
            g.fillRoundRect(this.StartGame.x, this.StartGame.y, this.StartGame.width, this.StartGame.height, 5, 5);
            g.fillRoundRect(this.Load.x, this.Load.y, this.Load.width, this.Load.height, 5, 5);
            g.setFont(new Font("DotumChe Pixel Regular", 1, 15));
            if (this.game.isDebug()) {
                this.command = new Rectangle(this.game.WIDTH - 150, this.game.HEIGHT - 60, 140, 24);
                g.fillRoundRect(this.command.x, this.command.y, this.command.width, this.command.height, 5, 5);
                g2D.setComposite(this.makeTransparent(1.0f));
                g.setColor(Color.BLUE);
                g.drawString("Commands", this.command.x + 29, this.command.y + 19);
            }
            g2D.setComposite(this.makeTransparent(1.0f));
            g.setColor(new Color(235, 100, 100));
            g.fillRoundRect(this.End.x, this.End.y, this.End.width, this.End.height, 5, 5);
            g.setColor(Color.black);
            g.drawString("Start", this.StartGame.x + 23, this.StartGame.y + 20);
            g.drawString("Load", this.Load.x + 28, this.Load.y + 19);
            g.drawString("End", this.End.x + 33, this.End.y + 19);
        }
        else if (Game.mt == MenuType.InGame) {
            if (this.Intro == 1) {
                g.setColor(Color.black);
                g.fillRect(0, 0, this.game.WIDTH, this.game.HEIGHT);
                final Font font = g.getFont();
                g.setColor(Color.white);
                new TurpRunnable(this.game, 0, 0) {
                    @Override
                    public void run() {
                        try {
                            this.game.getMenu().setIntrotext(String.valueOf(this.game.getMenu().getIntrotext()) + this.game.getMenu().messagechar[this.game.getMenu().getCharnum()]);
                            this.game.getMenu().setCharnum(this.game.getMenu().getCharnum() + 1);
                        }
                        catch (ArrayIndexOutOfBoundsException e) {
                            this.setRunning(false);
                        }
                    }
                };
                new TurpRunnable(this.game, 0, 300) {
                    @Override
                    public void run() {
                        try {
                            this.game.getMenu().setIntrotext(this.game.getMenu().getIntrotext().substring(1, this.game.getMenu().getIntrotext().length() - 1));
                            if (this.game.getMenu().getIntrotext().length() == 1) {
                                this.game.getMenu().setIntrotext("");
                            }
                        }
                        catch (StringIndexOutOfBoundsException e) {
                            this.setRunning(false);
                        }
                    }
                };
                new TurpRunnable(this.game, 0, 330) {
                    @Override
                    public void run() {
                        this.game.getFade().setFadevalue(1.0f);
                        this.game.getFade().setIncrement(-0.02f);
                        GameRenderer.this.Intro = 0;
                        this.setRunning(false);
                    }
                };
                Game.drawCenteredString(g, this.introtext, new Rectangle(0, 0, this.game.WIDTH, this.game.HEIGHT - 70), new Font("DotumChe Pixel", 0, 20));
                g.setFont(font);
            }
            else if (this.Intro == 0) {
                final Color color = g.getColor();
                g.setColor(new Color(1, 5, 15));
                g.fillRect(0, 0, this.game.WIDTH, this.game.HEIGHT);
                g.setColor(color);
                this.game.getCamera().render(g);
            }
        }
    }
    
    public void loadMenuCharacter(final int x, final int y, final Graphics g, final boolean doStats) {
        final Color color = g.getColor();
        g.setColor(Color.WHITE);
        this.createCharacter = new Rectangle(x + 5, y + 215, 260, 24);
        final Graphics2D g2D = (Graphics2D)g;
        g2D.setComposite(this.makeTransparent(0.8f));
        g.setColor(Color.white);
        g.fillRoundRect(this.createCharacter.x, this.createCharacter.y, this.createCharacter.width, this.createCharacter.height, 5, 5);
        g.setFont(new Font("DotumChe Pixel Regular", 1, 15));
        g.setColor(Color.black);
        g2D.setComposite(this.makeTransparent(1.0f));
        g.drawString("Select Character", this.createCharacter.x + 46, this.createCharacter.y + 19);
        g.setFont(new Font("DotumChe Pixel Regular", 1, 190));
        if (this.game.getCharacter() == null) {
            g.setColor(Color.white);
            g.drawString("?", x + 70, y + 190);
        }
        else {
            if (CharacterType.getCharacterAvatar(this.game.getCharacter().getCt()) != null) {
                g.drawImage(CharacterType.getCharacterAvatar(this.game.getCharacter().getCt()), x + 38, y + 2, 190, 190, null);
            }
            else {
                g.setColor(Color.magenta);
                g.fillRect(x + 55, y + 15, 80, 80);
                g.fillRect(x + 135, y + 95, 80, 80);
                g.setColor(Color.WHITE);
                g.fillRect(x + 135, y + 15, 80, 80);
                g.fillRect(x + 55, y + 95, 80, 80);
            }
            g.setColor(Color.WHITE);
            g.setFont(new Font("DotumChe Pixel Regular", 2, 13));
            Game.drawCenteredString(g, CharacterType.getCharacterName(this.game.getCharacter().getCt()), new Rectangle(x + 133, y + 201, 0, 0), g.getFont());
        }
        g.setFont(new Font("DotumChe Pixel Regular", 1, 15));
        g.setColor(color);
    }
    
    public void tick() {
        if (this.Intro == 0) {
            this.game.getCamera().tick();
        }
    }
    
    private AlphaComposite makeTransparent(final float alpha) {
        final int type = 3;
        return AlphaComposite.getInstance(type, alpha);
    }
    
    public void endClicked() {
        this.game.stop();
    }
    
    public void StartGameClicked() {
        if (this.game.character == null) {
            this.game.getGBH().addGameBox("Are you sure you want to make a new character?", BoxPurpose.newGame);
        }
    }
    
    public void loadClicked() {
        final JFileChooser FC = new JFileChooser();
        FC.showOpenDialog(null);
        try {
            final File gamesavefile = FC.getSelectedFile();
            if (gamesavefile != null) {
                final String filesave = gamesavefile.getPath();
                this.game.getSh().loadGame(this.game, filesave);
            }
        }
        catch (StreamCorruptedException | ClassCastException ex2) {
            final Exception ex;
            final Exception e = ex;
            this.game.getLogger().log(Level.SEVERE, "Invalid Save.");
        }
    }
    
    public void createCharacterClicked() {
        JOptionPane.showMessageDialog(this.game, "Characters have not been implemented just yet.", "Coming Soon", 1);
    }
    
    public void commandsClicked() {
        if (this.game.isCommandCaptured()) {
            this.game.setCommandCaptured(false);
        }
        else {
            this.game.setCommandCaptured(true);
        }
    }
}
