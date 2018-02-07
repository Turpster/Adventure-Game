package com._Turpster.AdventureGame;

import com._Turpster.AdventureGame.Runnable.*;
import java.awt.*;
import java.util.*;

public class Handler
{
    Game game;
    ArrayList<Renderable> renderables;
    ArrayList<Tickable> tickables;
    ArrayList<TurpRunnable> runnables;
    
    public Handler(final Game game) {
        this.renderables = new ArrayList<Renderable>();
        this.tickables = new ArrayList<Tickable>();
        this.runnables = new ArrayList<TurpRunnable>();
        this.game = game;
    }
    
    public void render(final Graphics g) {
        this.game.getMenu().render(g);
        this.game.getGBH().render(g);
        for (final Renderable ra : this.renderables) {
            ra.render(g);
        }
    }
    
    public void tick() {
        this.game.getMenu().tick();
        this.game.getCamera().tick();
        for (final TurpRunnable TR : this.runnables) {
            TR.tick();
        }
        for (final Tickable ta : this.tickables) {
            ta.tick();
        }
    }
    
    public void addRenderable(final Renderable ra) {
        if (!this.renderables.contains(ra)) {
            this.renderables.add(ra);
        }
    }
    
    public void removeRenderable(final Renderable ra) {
        if (this.renderables.contains(ra)) {
            this.renderables.remove(ra);
        }
    }
    
    public void addTickable(final Tickable ra) {
        if (!this.tickables.contains(ra)) {
            this.tickables.add(ra);
        }
    }
    
    public void removeTickable(final Tickable ra) {
        if (this.tickables.contains(ra)) {
            this.tickables.remove(ra);
        }
    }
    
    public void addRunnable(final TurpRunnable TR) {
        if (!this.runnables.contains(TR)) {
            this.runnables.add(TR);
        }
    }
    
    public void removeRunnable(final TurpRunnable TR) {
        if (this.runnables.contains(TR)) {
            this.runnables.remove(TR);
        }
    }
    
    public ArrayList<Renderable> getRenderables() {
        return this.renderables;
    }
}
