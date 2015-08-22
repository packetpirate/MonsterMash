package com.ludum;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;

import com.ludum.entities.Player;

public class HUD {
	private Game game;
	
	public HUD(Game game) {
		this.game = game;
	}
	
	public void render(Graphics2D g2d) {
		{ // Draw the health and mana bars.
			// Draw gray backdrop behind health and mana.
			g2d.setColor(Color.GRAY);
			g2d.fillRect(5, 5, 170, 76);
			g2d.setColor(Color.WHITE);
			g2d.drawRect(5, 5, 170, 76);
			
			// Draw the health bar.
			double hW = (game.player.currentHealth() / Player.MAX_HEALTH) * 150;
			g2d.setColor(Color.BLACK);
			g2d.fillRect(11, 11, 158, 19);
			g2d.setColor(Color.RED);
			g2d.fillRect(15, 15, (int)hW, 11);
			
			// Draw the mana bar.
			double mW = (game.player.currentMana() / Player.MAX_MANA) * 150;
			g2d.setColor(Color.BLACK);
			g2d.fillRect(11, 34, 158, 19);
			g2d.setColor(Color.CYAN);
			g2d.fillRect(15, 38, (int)mW, 11);
			
			// Draw the experience bar.
			double eW = ((double)game.player.getExperience() / (double)game.player.getExperienceToLevel()) * 150;
			g2d.setColor(Color.BLACK);
			g2d.fillRect(11, 57, 158, 19);
			g2d.setColor(Color.GREEN);
			g2d.fillRect(15, 61, (int)eW, 11);
			
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
					g2d.fillRect((slotBarX + (i * 32) + (i * 4) + 4), (slotBarY + 4), 32, 32);
					
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
			g2d.drawString(("Level: " + game.player.getLevel()), 5, 96);
			g2d.drawString(("Current Spell: " + game.player.getCurrentSpell().getName()), 5, 111);
		} // End drawing health and mana.
	}
}
