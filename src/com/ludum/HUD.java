package com.ludum;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

import com.ludum.entities.Player;
import com.ludum.gfx.Textures;

public class HUD {
	private Game game;
	
	public HUD(Game game) {
		this.game = game;
	}
	
	public void render(Graphics2D g2d) {
		{ // Draw the health and mana bars.
			// Draw gray backdrop behind health and mana.
			if(Textures.HUD1.img != null) {
				g2d.drawImage(Textures.HUD1.img, 5, 5, null);
			} else {
				g2d.setColor(Color.GRAY);
				g2d.fillRect(5, 5, 170, 76);
				g2d.setColor(Color.WHITE);
				g2d.drawRect(5, 5, 170, 76);
			}
			
			
			// Draw the health bar.
			double hW = (game.player.currentHealth() / Player.MAX_HEALTH) * 180;
			RoundRectangle2D.Double health = new RoundRectangle2D.Double(15, 15, hW, 11, 8, 8);
			g2d.setColor(new Color(0xC13333));
			g2d.fill(health);
			
			// Draw the mana bar.
			double mW = (game.player.currentMana() / Player.MAX_MANA) * 180;
			RoundRectangle2D.Double mana = new RoundRectangle2D.Double(15, 40, mW, 11, 8, 8);
			g2d.setColor(new Color(0x5C98BF));
			g2d.fill(mana);
			
			// Draw the experience bar.
			double eW = ((double)game.player.getExperience() / (double)game.player.getExperienceToLevel()) * 180;
			RoundRectangle2D.Double exp = new RoundRectangle2D.Double(15, 65, eW, 11, 8, 8);
			g2d.setColor(new Color(0xAAFFAA));
			g2d.fill(exp);
			
			{ // Draw the spell slot bar.
				int slotSize = 32;
				int slotBarWidth = (slotSize * game.player.getSpells().size()) + ((game.player.getSpells().size() + 1) * 4);
				int slotBarHeight = slotSize + 8;
				int slotBarX = ((Game.WIDTH / 2) - (slotBarWidth / 2));
				int slotBarY = (Game.HEIGHT - slotBarHeight - 5);
				
				g2d.setColor(Color.GRAY);
				g2d.fillRect(slotBarX, slotBarY, slotBarWidth, slotBarHeight);
				g2d.setColor(Color.WHITE);
				g2d.drawRect(slotBarX, slotBarY, slotBarWidth, slotBarHeight);
				
				for(int i = 0; i < game.player.getSpells().size(); i++) {
					Rectangle2D.Double rect = new Rectangle2D.Double((slotBarX + (i * 32) + (i * 4) + 4), (slotBarY + 4), 32, 32);
					g2d.setColor(Color.BLACK);
					g2d.fill(rect);
					
					game.player.getSpells().get(i).renderIcon(g2d, new Point2D.Double(rect.x, rect.y));
					
					Stroke oldStroke = g2d.getStroke();
					if(i == game.player.getSelectedSpell()) {
						g2d.setColor(Color.WHITE);
						g2d.setStroke(new BasicStroke(3.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER,
                                1.0f, new float[]{16.0f, 16.0f}, 8.0f));
					} else g2d.setColor(Color.DARK_GRAY);
					g2d.draw(rect);
					g2d.setStroke(oldStroke);
				}
			} // End spell slot drawing.

			g2d.setColor(Color.WHITE);
			g2d.drawString(("Level: " + game.player.getLevel()), 5, 120);
			g2d.drawString(("Current Spell: " + game.player.getCurrentSpell().getName()), 5, 135);
		} // End drawing health and mana.
	}
}
