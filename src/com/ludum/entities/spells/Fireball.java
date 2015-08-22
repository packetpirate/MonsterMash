package com.ludum.entities.spells;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.Iterator;

import com.ludum.Game;
import com.ludum.entities.EnemyFactory;
import com.ludum.entities.LightType;
import com.ludum.entities.enemies.Enemy;

public class Fireball extends Spell {
	public Fireball() {
		super("Fireball", 500, 10, 50);
	}
	
	@Override
	public void cast(Game game) {
		if(game.player.currentMana() >= manaCost) {
			game.player.useMana(manaCost);
			game.registerSpellEffect(new SpellEffect(this, new Point2D.Double(game.player.location.x, game.player.location.y), new Point2D.Double(game.screen.mousePos.x, game.screen.mousePos.y)) {
				{ // Begin pseudo-constructor.
					light = LightType.createLight(location, LightType.FIREBALL);
					game.lightFactory.lights.add(light);
				} // End pseudo-constructor.
				
				@Override
				public void update(Game game) {
					super.update(game);
					
					// Do fireball update stuff here.
					double dx = Math.cos(theta) * 5;
					double dy = Math.sin(theta) * 5;
					
					location.x += dx;
					location.y += dy;
					light.location.x = location.x;
					light.location.y = location.y;
				}
				
				@Override
				public void render(Graphics2D g2d) {
					g2d.setColor(Color.ORANGE);
					g2d.fillOval((int)(location.x - 5), (int)(location.y - 5), 10, 10);
				}
			});
			
			lastCast = Game.time.getElapsedMillis();
		}
	}
}
