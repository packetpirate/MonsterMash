package com.ludum;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

import com.ludum.entities.Player;
import com.ludum.entities.spells.EldritchBolt;
import com.ludum.entities.spells.Fireball;
import com.ludum.entities.spells.LightningBolt;
import com.ludum.entities.spells.Spell;
import com.ludum.entities.spells.summons.SummonSkeleton;
import com.ludum.entities.spells.summons.SummonWraith;
import com.ludum.entities.spells.summons.SummonZombie;
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
				int slots = game.player.getSpellCount();
				int slotSize = 32;
				int slotBarWidth = (slotSize * slots) + ((slots + 1) * 4);
				int slotBarHeight = slotSize + 8;
				int slotBarX = ((Game.WIDTH / 2) - (slotBarWidth / 2));
				int slotBarY = (Game.HEIGHT - slotBarHeight - 5);
				
				g2d.setColor(Color.GRAY);
				g2d.fillRect(slotBarX, slotBarY, slotBarWidth, slotBarHeight);
				g2d.setColor(Color.WHITE);
				g2d.drawRect(slotBarX, slotBarY, slotBarWidth, slotBarHeight);
				
				int i = 0;
				
				if(game.player.getSpells().get(EldritchBolt.NAME).isActive()) {
					drawSpellSlot(g2d, EldritchBolt.NAME, slotBarX, slotBarY, i);
					i++;
				}
				if(game.player.getSpells().get(Fireball.NAME).isActive()) {
					drawSpellSlot(g2d, Fireball.NAME, slotBarX, slotBarY, i);
					i++;	
				}
				if(game.player.getSpells().get(LightningBolt.NAME).isActive()) {
					drawSpellSlot(g2d, LightningBolt.NAME, slotBarX, slotBarY, i);
					i++;
				}
				if(game.player.getSpells().get(SummonZombie.NAME).isActive()) {
					drawSpellSlot(g2d, SummonZombie.NAME, slotBarX, slotBarY, i);
					i++;
				}
				if(game.player.getSpells().get(SummonSkeleton.NAME).isActive()) {
					drawSpellSlot(g2d, SummonSkeleton.NAME, slotBarX, slotBarY, i);
					i++;
				}
				if(game.player.getSpells().get(SummonWraith.NAME).isActive()) {
					drawSpellSlot(g2d, SummonWraith.NAME, slotBarX, slotBarY, i);
					i++;
				}
			} // End spell slot drawing.
			
			synchronized(game.messages) {
				for(Message msg : game.messages) {
					msg.render(g2d);
				}
			}

			g2d.setColor(Color.WHITE);
			g2d.drawString(("Level: " + game.player.getLevel()), 5, 120);
			g2d.drawString(("Current Spell: " + game.player.getCurrentSpell().getName()), 5, 135);
			g2d.drawString(("Summon Points: " + game.player.getSummonPoints()), 5, 150);
		} // End drawing health and mana.
	}
	
	private void drawSpellSlot(Graphics2D g2d, String name, int slotBarX, int slotBarY, int i) {
		Rectangle2D.Double rect = new Rectangle2D.Double((slotBarX + (i * 32) + (i * 4) + 4), 
														 (slotBarY + 4), 32, 32);
		g2d.setColor(Color.BLACK);
		g2d.fill(rect);
		
		Spell sp = game.player.getSpells().get(name);
		
		double alpha = ((Game.time.getElapsedMillis() - sp.getLastCast()) / (double)sp.getCooldown());
		if(alpha > 1.0f) alpha = 1.0f;
		else if(alpha < 0.0f) alpha = 0.0f;
		AlphaComposite saved = (AlphaComposite) g2d.getComposite();
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float)alpha));
		game.player.getSpells().get(name).renderIcon(g2d, new Point2D.Double(rect.x, rect.y));
		g2d.setComposite(saved);
		
		Stroke oldStroke = g2d.getStroke();
		if(i == game.player.getSelectedSpell()) {
			if(game.player.getCurrentSpell().canCast()) {
				g2d.setColor(new Color(0x990000));
			} else {
				g2d.setColor(Color.WHITE);
			}
			g2d.setStroke(new BasicStroke(3.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER,
                    1.0f, new float[]{16.0f, 16.0f}, 8.0f));
		} else g2d.setColor(Color.DARK_GRAY);
		g2d.draw(rect);
		g2d.setStroke(oldStroke);
	}
}
