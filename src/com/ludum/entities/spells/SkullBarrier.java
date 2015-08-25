package com.ludum.entities.spells;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;

import com.ludum.Game;
import com.ludum.entities.LightType;
import com.ludum.gfx.Textures;

public class SkullBarrier extends Spell {
	public static final String NAME = "Skull Barrier";
	public static final int MAX_SKULLS = 3;
	public static final int ORBIT = 80;
	
	public static int skulls;
	public static double theta;
	
	@Override
	public String getName() { return SkullBarrier.NAME; }
	
	public SkullBarrier() {
		super(1000, 30, 0);
		
		skulls = 0;
		theta = 0;
	}
	
	@Override
	public void renderIcon(Graphics2D g2d, Point2D.Double position) {
		if(Textures.SKULL_BARRIER_ICON.img != null) {
			g2d.drawImage(Textures.SKULL_BARRIER_ICON.img, (int)position.x, (int)position.y, null);
		}
	}
	
	@Override
	public void cast(Game game) {
		if((game.player.currentMana() >= manaCost) && (skulls < SkullBarrier.MAX_SKULLS)) {
			skulls++;
			game.player.useMana(manaCost);
			
			Point2D.Double position = new Point2D.Double((game.player.location.x + (Math.cos(0) * SkullBarrier.ORBIT)), 
														 (game.player.location.y + (Math.sin(0) * SkullBarrier.ORBIT)));
			game.currentLevel.spellEffects.add(new SpellEffect(this, position, new Point2D.Double(game.screen.mousePos.x, game.screen.mousePos.y)) {
				{ // Begin pseudo-constructor.
					light = LightType.createLight(location, LightType.SKULL_BARRIER);
					game.currentLevel.lightFactory.lights.add(light);
				} // End pseudo-constructor.
				
				@Override
				public void update(Game game) {
					// Update location. (orbits the player)
					SkullBarrier.theta += (Math.PI / 180.0f) * 3;
					double x = Math.cos(SkullBarrier.theta) * SkullBarrier.ORBIT;
					double y = Math.sin(SkullBarrier.theta) * SkullBarrier.ORBIT;
					
					location.x = game.player.location.x + x;
					location.y = game.player.location.y + y;
					light.location.x = location.x;
					light.location.y = location.y;
				}
				
				@Override
				public void render(Graphics2D g2d) {
					if(Textures.SKULL_BARRIER.img != null) {
						int x = (int)(location.x - (Textures.SKULL_BARRIER.img.getWidth() / 2));
						int y = (int)(location.y - (Textures.SKULL_BARRIER.img.getHeight() / 2));
						g2d.drawImage(Textures.SKULL_BARRIER.img, x, y, null);
					} else {
						
						g2d.setColor(Color.WHITE);
						g2d.fillOval((int)(location.x - 10), (int)(location.y - 10), 20, 20);
					}
				}
			});
			
			lastCast = Game.time.getElapsedMillis();
		}
	}
}
