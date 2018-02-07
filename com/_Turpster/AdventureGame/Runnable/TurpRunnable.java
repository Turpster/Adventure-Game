package com._Turpster.AdventureGame.Runnable;

import com._Turpster.AdventureGame.*;

public abstract class TurpRunnable
{
    int Tick;
    int Ticks;
    int Delay;
    int DelayTick;
    volatile boolean running;
    protected Game game;
    
    public int getDelayTick() {
        return this.DelayTick;
    }
    
    public void setDelayTick(final int delayTick) {
        this.DelayTick = delayTick;
    }
    
    public int getDelay() {
        return this.Delay;
    }
    
    public void setDelay(final int delay) {
        this.Delay = delay;
    }
    
    public int getTick() {
        return this.Tick;
    }
    
    public void setTick(final int tick) {
        this.Tick = tick;
    }
    
    public int getTicks() {
        return this.Ticks;
    }
    
    public void setTicks(final int ticks) {
        this.Ticks = ticks;
    }
    
    public boolean isRunning() {
        return this.running;
    }
    
    public void setRunning(final boolean running) {
        this.running = running;
    }
    
    public TurpRunnable(final Game game, final int Ticks, final int Delay) {
        this.Tick = 0;
        this.running = true;
        this.Delay = Delay;
        this.Ticks = Ticks;
        this.game = game;
        game.getHandler().addRunnable(this);
    }
    
    public TurpRunnable(final Game game, final int Delay) {
        this.Tick = 0;
        this.running = true;
        this.Delay = Delay;
        this.game = game;
        game.getHandler().addRunnable(this);
    }
    
    public synchronized void tick() {
        if (this.running) {
            if (this.DelayTick < this.Delay) {
                ++this.DelayTick;
            }
            else if (this.Ticks == 0 && this.running) {
                this.running = false;
                this.run();
            }
            else if (this.running) {
                ++this.Tick;
                if (this.Tick > this.Ticks) {
                    this.Tick = 0;
                    this.run();
                }
            }
        }
    }
    
    public abstract void run();
}
