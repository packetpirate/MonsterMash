package com.ludum.entities.items;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;

import com.ludum.Game;
import com.ludum.gfx.Screen;
import com.ludum.gfx.Textures;

public class Grave {
	private static final long DURATION = 10000;
	
	public Point2D.Double location;
	private long creation;
	public boolean isAlive() { return !(Game.time.getElapsedMillis() >= (creation + DURATION)); }
	
	public Grave(Point2D.Double location) {
		this.location = location;
		this.creation = Game.time.getElapsedMillis();
	}
	
	public void update(Game game) {
		
	}
	
	public void render(Graphics2D g2d) {
		if(Textures.GRAVE.img != null) {
			int w = Textures.GRAVE.img.getWidth();
			int h = Textures.GRAVE.img.getHeight();
			g2d.drawImage(Textures.GRAVE.img, (int)(location.x - (w / 2)), (int)(location.y - (h / 2)), null);
		} else {
			g2d.setColor(Color.GRAY);
			g2d.fillRect((int)(location.x - 10), (int)(location.y - 10), 20, 20);
		}
	}
	
	public boolean contains(Point2D.Double click) {
		return (Screen.dist(click, location) <= Textures.GRAVE.img.getWidth());
	}
}
