package com.ludum;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

import com.ludum.entities.Player;
import com.ludum.entities.spells.BloodSiphon;
import com.ludum.entities.spells.EldritchBolt;
import com.ludum.entities.spells.Fireball;
import com.ludum.entities.spells.LightningBolt;
import com.ludum.entities.spells.SkullBarrier;
import com.ludum.entities.spells.Spell;
import com.ludum.entities.spells.summons.SummonSkeleton;
import com.ludum.entities.spells.summons.SummonWraith;
import com.ludum.entities.spells.summons.SummonZombie;
import com.ludum.gfx.Textures;

public class HUD {
	private Game game;
	private Rectangle2D.Double nextLevelButton;
	
	public HUD(Game game) {
		this.game = game;
		this.nextLevelButton = new Rectangle2D.Double((Game.WIDTH - 105), 5, 100, 30);
	}
	
	public void render(Graphics2D g2d) {
		Font saved = g2d.getFont();
		
		{ // Draw the health and mana bars.
			Font font = new Font("Serif", Font.PLAIN, 8);
			FontMetrics metrics = g2d.getFontMetrics(font);
			
			String healthText = (int)game.player.currentHealth() + " / " + (int)Player.MAX_HEALTH;
			String manaText = (int)game.player.currentMana() + " / " + (int)Player.MAX_MANA;
			String expText = "Lvl " + game.player.getLevel();
			
			// Draw the health bar.
			double hW = (game.player.currentHealth() / Player.MAX_HEALTH) * 180;
			RoundRectangle2D.Double health = new RoundRectangle2D.Double(15, 15, hW, 12, 8, 8);
			RoundRectangle2D.Double healthBack = new RoundRectangle2D.Double(15, 15, 180, 12, 8, 8);
			g2d.setColor(new Color(0x808080));
			g2d.fill(healthBack);
			g2d.setColor(new Color(0xC13333));
			g2d.fill(health);
			
			double htW = metrics.stringWidth(healthText);
			double htH = metrics.getHeight();
			g2d.setColor(Color.WHITE);
			g2d.drawString(healthText, (int)(healthBack.x + (healthBack.width / 2) - (htW / 2)), 
									   (int)(healthBack.y + healthBack.height - ((healthBack.height - htH) / 2)));
			
			// Draw the mana bar.
			double mW = (game.player.currentMana() / Player.MAX_MANA) * 180;
			RoundRectangle2D.Double mana = new RoundRectangle2D.Double(15, 40, mW, 12, 8, 8);
			RoundRectangle2D.Double manaBack = new RoundRectangle2D.Double(15, 40, 180, 12, 8, 8);
			g2d.setColor(new Color(0x808080));
			g2d.fill(manaBack);
//			g2d.setColor(new Color(0x5C98BF));
			g2d.setColor(new Color(0x3333C1));
			g2d.fill(mana);
			
			double mtW = metrics.stringWidth(manaText);
			double mtH = metrics.getHeight();
			g2d.setColor(Color.WHITE);
			g2d.drawString(manaText, (int)(manaBack.x + (manaBack.width / 2) - (mtW / 2)), 
									   (int)(manaBack.y + manaBack.height - ((manaBack.height - mtH) / 2)));
			
			// Draw the experience bar.
			double eW = ((double)game.player.getExperience() / (double)game.player.getExperienceToLevel()) * 180;
			RoundRectangle2D.Double exp = new RoundRectangle2D.Double(15, 65, eW, 12, 8, 8);
			RoundRectangle2D.Double expBack = new RoundRectangle2D.Double(15, 65, 180, 12, 8, 8);
			g2d.setColor(new Color(0x808080));
			g2d.fill(expBack);
//			g2d.setColor(new Color(0xAAFFAA));
			g2d.setColor(new Color(0x33C133));
			g2d.fill(exp);
			
			double etW = metrics.stringWidth(expText);
			double etH = metrics.getHeight();
			g2d.setColor(Color.WHITE);
			g2d.drawString(expText, (int)(expBack.x + (expBack.width / 2) - (etW / 2)), 
									   (int)(expBack.y + expBack.height - ((expBack.height - etH) / 2)));
		} // End drawing health and mana.
		
		{ // Draw summon icons.
			if(Textures.SUMMON_ICON.img != null) {
				for(int i = 0; i < game.player.getSummonPoints(); i++) {
					g2d.drawImage(Textures.SUMMON_ICON.img, 
								 (int)((i * Textures.SUMMON_ICON.img.getWidth()) + (i * 4) + 15), 90,
								 null);
				}
			}
		} // End drawing summon icons.
			
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
			if(game.player.getSpells().get(BloodSiphon.NAME).isActive()) {
				drawSpellSlot(g2d, BloodSiphon.NAME, slotBarX, slotBarY, i);
				i++;
			}
			if(game.player.getSpells().get(SkullBarrier.NAME).isActive()) {
				drawSpellSlot(g2d, SkullBarrier.NAME, slotBarX, slotBarY, i);
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
		
		if(Game.state == GameState.LEVEL_CLEAR) {
			Color color = (nextLevelButton.contains(game.screen.mousePos))?
							new Color(0x6B0E0E):
							new Color(0xBF1919);
			g2d.setColor(color);
			g2d.fill(nextLevelButton);
			g2d.setColor(Color.WHITE);
			g2d.draw(nextLevelButton);
			
			Font font = new Font("Serif", Font.PLAIN, 18);
			FontMetrics metrics = g2d.getFontMetrics(font);
			int w = metrics.stringWidth("Next Level");
			int h = metrics.getHeight();
			
			g2d.setFont(font);
			g2d.setColor(Color.WHITE);
			g2d.drawString("Next Level", 
						   (int)(Game.WIDTH - ((nextLevelButton.width - w) / 2) - w - 5), 
						   (int)(((nextLevelButton.height - metrics.getDescent() - ((nextLevelButton.height - h) / 2)) + 5)));
		}
		
		g2d.setFont(saved);
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
	
	public void dispatchClick(Point2D.Double location) {
		if(nextLevelButton.contains(location)) {
			Game.state = GameState.LEVEL_TRANSITION;
		}
	}
}
