package com._Turpster.AdventureGame.Level;

import com._Turpster.AdventureGame.Entitys.*;

public class Intro extends Level
{
    public Intro() {
        this.addEntity(new Player(100, 100, 100, 100, this, 0, 5));
    }
}
