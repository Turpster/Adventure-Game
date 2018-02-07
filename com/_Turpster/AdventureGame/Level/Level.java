package com._Turpster.AdventureGame.Level;

import java.awt.geom.*;
import java.awt.*;
import com._Turpster.AdventureGame.Entitys.*;
import java.util.*;

public abstract class Level
{
    ArrayList<Shape> shapes;
    ArrayList<Entity> entitys;
    
    public Level() {
        this.shapes = new ArrayList<Shape>();
        this.entitys = new ArrayList<Entity>();
    }
    
    public void addShape(final Shape shape) {
        if (!this.shapes.contains(shape)) {
            this.shapes.add(shape);
        }
    }
    
    public void removeShape(final Shape shape) {
        if (this.shapes.contains(shape)) {
            this.shapes.remove(shape);
        }
    }
    
    public ArrayList<Shape> getShapes() {
        return this.shapes;
    }
    
    public void addEntity(final Entity entity) {
        if (!this.entitys.contains(entity)) {
            this.entitys.add(entity);
        }
    }
    
    public void removeEntity(final Entity entity) {
        if (this.entitys.contains(entity)) {
            this.entitys.remove(entity);
        }
    }
    
    public ArrayList<Entity> getEntitys() {
        return this.entitys;
    }
    
    public void tick() {
    }
    
    public void render(final Graphics g, final int xt, final int yt) {
        for (final Shape shape : this.shapes) {
            if (shape instanceof Rectangle) {
                final Rectangle rec = (Rectangle)shape;
                g.fillRect((int)rec.getX() + xt, (int)rec.getY() + yt, (int)rec.getWidth(), (int)rec.getHeight());
            }
            else if (shape instanceof Ellipse2D) {
                final Ellipse2D circle = (Ellipse2D)shape;
                g.fillOval((int)circle.getX() + xt, (int)circle.getY() + yt, (int)circle.getWidth(), (int)circle.getHeight());
            }
            else {
                if (!(shape instanceof Polygon)) {
                    continue;
                }
                final Polygon polygon = (Polygon)shape;
                g.fillPolygon(polygon);
            }
        }
        for (final Entity entity : this.entitys) {
            final boolean b = entity instanceof Player;
            entity.render(g);
        }
    }
}
