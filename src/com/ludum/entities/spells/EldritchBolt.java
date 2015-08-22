package com.ludum.entities.spells;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;

import com.ludum.Game;
import com.ludum.entities.LightType;

public class EldritchBolt extends Spell {
	public EldritchBolt() {
		super("Eldritch Bolt", 50, 1, 5);
	}
	
	@Override
	public void cast(Game game) {
		if(game.player.currentMana() >= manaCost) {
			game.player.useMana(manaCost);
			game.registerSpellEffect(new SpellEffect(this, new Point2D.Double(game.player.location.x, game.player.location.y), new Point2D.Double(game.screen.mousePos.x, game.screen.mousePos.y)) {
				{ // Begin pseudo-constructor.
					light = LightType.createLight(location, LightType.ELDRITCH_BOLT);
					game.lightFactory.lights.add(light);
				} // End pseudo-constructor.
				
				@Override
				public void update(Game game) {
					super.update(game);
					
					// Do fireball update stuff here.
					double dx = Math.cos(theta) * 10;
					double dy = Math.sin(theta) * 10;
					
					location.x += dx;
					location.y += dy;
					light.location.x = location.x;
					light.location.y = location.y;
				}
				
				@Override
				public void render(Graphics2D g2d) {
					g2d.setColor(new Color(0x479D47));
					g2d.fillOval((int)(location.x - 2), (int)(location.y - 2), 4, 4);
				}
			});
			
			lastCast = Game.time.getElapsedMillis();
		}
	}
}
