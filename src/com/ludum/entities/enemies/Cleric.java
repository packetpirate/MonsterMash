package com.ludum.entities.enemies;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.Iterator;

import com.ludum.Game;
import com.ludum.entities.EnemyFactory;
import com.ludum.entities.minions.Minion;
import com.ludum.gfx.Screen;
import com.ludum.gfx.Textures;

public class Cleric extends Enemy {
	public static final int SPEED = 2;
	public static final int BANISH_RANGE = 200;
	public static final int BANISH_DAMAGE = 20;
	
	public Cleric(EnemyFactory origin, Point2D.Double spawnLocation) {
		super(origin, "Cleric", 150, 100, 1, spawnLocation);
	}
	
	@Override
	public void update(Game game) {
		if(isAlive()) {
			// Moves towards minions first, then  the player. Only gets within banish range of minions.
			synchronized(game.player.getMinions()) {
				if(!game.player.getMinions().isEmpty()) {
					boolean damageDone = false;
					Iterator<Minion> it = game.player.getMinions().iterator();
					while(it.hasNext()) {
						Minion m = it.next();
						
						double dist = Screen.dist(location, m.location);
						if(m.isAlive()) {
							if(m.canTakeDamage()) {
								if(!damageDone && dist <= Cleric.BANISH_RANGE) {
									m.takeDamage(Cleric.BANISH_DAMAGE);
									damageDone = true;
								} else if(dist <= 20) {
									m.takeDamage(damage);
								}
							}
							
							if(dist > Cleric.BANISH_RANGE) {
								double theta = Math.atan2((m.location.y - location.y), 
														  (m.location.x - location.x));
								double dx = Math.cos(theta) * Cleric.SPEED;
								double dy = Math.cos(theta) * Cleric.SPEED;
								
								location.x += dx;
								location.y += dy;
							}
						}
					}
				} else {
					double theta = Math.atan2((game.player.location.y - location.y), 
											  (game.player.location.x - location.x));
					double dx = Math.cos(theta) * Cleric.SPEED;
					double dy = Math.cos(theta) * Cleric.SPEED;
					
					location.x += dx;
					location.y += dy;
				}
			}
		}
	}
	
	@Override
	public void render(Graphics2D g2d, Game game) {
		if(Textures.CLERIC.img != null) {
			int x = (int)(location.x - (Textures.CLERIC.img.getWidth() / 2));
			int y = (int)(location.y - (Textures.CLERIC.img.getHeight() / 2));

			// Determine which way the cleric should face.
			if(game.player.location.x >= location.x) {
				// Face to the right. (flip the image)
				g2d.drawImage(Textures.CLERIC.img, 
							  (x + Textures.CLERIC.img.getWidth()), y,
							  -Textures.CLERIC.img.getWidth(),
							  Textures.CLERIC.img.getHeight(), null);
			} else {
				// Face to the left. (just draw normally)
				g2d.drawImage(Textures.CLERIC.img, x, y, null);
			}
		} else {
			g2d.setColor(Color.MAGENTA);
			g2d.fillOval((int)(location.x - 10), (int)(location.y - 10), 20, 20);
		}
	}
}
