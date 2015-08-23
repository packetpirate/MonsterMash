package com.ludum.entities.spells;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

import com.ludum.Game;
import com.ludum.entities.LightType;
import com.ludum.gfx.Textures;

public class EldritchBolt extends Spell {
	public EldritchBolt() {
		super("Eldritch Bolt", 200, 5, 10);
	}
	
	@Override
	public void renderIcon(Graphics2D g2d, Point2D.Double position) {
		if(Textures.ELDRITCH_BOLT_ICON.img != null) {
			g2d.drawImage(Textures.ELDRITCH_BOLT_ICON.img, (int)position.x, (int)position.y, null);
		}
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
					if(Textures.ELDRITCH_BOLT.img != null) {
						AffineTransform saved = g2d.getTransform();
						
						int x = (int)(location.x - (Textures.ELDRITCH_BOLT.img.getWidth() / 2));
						int y = (int)(location.y - (Textures.ELDRITCH_BOLT.img.getHeight() / 2));
						AffineTransform rotate = AffineTransform.getRotateInstance((theta + (Math.PI / 2)), location.x, location.y);
						g2d.setTransform(rotate);
						g2d.drawImage(Textures.ELDRITCH_BOLT.img, x, y, null);
						
						g2d.setTransform(saved);
					} else {
						g2d.setColor(new Color(0x479D47));
						g2d.fillOval((int)(location.x - 2), (int)(location.y - 2), 4, 4);
					}
				}
			});
			
			lastCast = Game.time.getElapsedMillis();
		}
	}
}
