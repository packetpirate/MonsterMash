package com.ludum.entities.enemies;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;

import com.ludum.Game;
import com.ludum.entities.EnemyFactory;
import com.ludum.gfx.Textures;

// Most basic enemy unit.
public class Peasant extends Enemy {
	public static final int SPEED = 2;
	
	public Peasant(EnemyFactory origin, Point2D.Double spawnLocation) {
		super(origin, "Peasant", 50, 10, 2, spawnLocation);
	}
	
	@Override
	public void update(Game game) {
		super.update(game);
		
		if(isAlive()) {
			// Basic enemy. Just travels toward the player.
			double theta = Math.atan2((game.player.location.y - location.y), (game.player.location.x - location.x));
			if(hasStatus("fear")) theta -= Math.PI;
			
			double dx = Math.cos(theta) * Peasant.SPEED;
			double dy = Math.sin(theta) * Peasant.SPEED;
			
			location.x += dx;
			location.y += dy;
		}
	}
	
	@Override
	public void render(Graphics2D g2d, Game game) {
		if(Textures.PEASANT.img != null) {
			int x = (int)(location.x - (Textures.PEASANT.img.getWidth() / 2));
			int y = (int)(location.y - (Textures.PEASANT.img.getHeight() / 2));

			// Determine which way the archer should face.
			if(game.player.location.x >= location.x) {
				// Face to the right. (flip the image)
				g2d.drawImage(Textures.PEASANT.img, 
							  (x + Textures.PEASANT.img.getWidth()), y,
							  -Textures.PEASANT.img.getWidth(),
							  Textures.PEASANT.img.getHeight(), null);
			} else {
				// Face to the left. (just draw normally)
				g2d.drawImage(Textures.PEASANT.img, x, y, null);
			}
		} else {
			g2d.setColor(Color.GRAY);
			g2d.fillOval((int)(location.x - 10), (int)(location.y - 10), 20, 20);
		}
	}
}