package com.ludum.gfx;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import com.ludum.Game;
import com.ludum.GameOver;
import com.ludum.GameState;
import com.ludum.HUD;
import com.ludum.Menu;
import com.ludum.entities.EnemyFactory;
import com.ludum.entities.enemies.Enemy;
import com.ludum.entities.spells.LightningBolt;
import com.ludum.entities.spells.SpellEffect;

public class Screen extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private Game game;
	public Menu menu;
	public GameOver gameOver;
	public HUD hud;
	public Point mousePos;
	
	public boolean mouseDown;
	public boolean keys[];

	public Screen(Game game) {
		Dimension d = new Dimension(Game.WIDTH, Game.HEIGHT);
		
		setMinimumSize(d);
		setMaximumSize(d);
		setPreferredSize(d);

		this.game = game;
		menu = new Menu();
		gameOver = new GameOver();
		hud = new HUD(game);
		
		mouseDown = false;
		mousePos = new Point();
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D)g;
		
		if(Game.state == GameState.MENU) {
			menu.render(g2d);
		} else if(Game.state == GameState.GAME_STARTED) {
			g2d.setColor(new Color(0x336600));
			g2d.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
			
			AlphaComposite savedComp = (AlphaComposite)g2d.getComposite();
			
			synchronized(game.spellEffects) {
				for(SpellEffect effect : game.spellEffects) {
					if(effect.alive) effect.render(g2d);
				}
			}
			// TODO: Figure out a better way to handle this.
			// Specific: Draw lightning bolt strikes.
			if(LightningBolt.strikes.size() > 1) {
				g2d.setColor(new Color(0xE6FFFF));
				g2d.setStroke(new BasicStroke(3.0f));
				for(int i = 0; i < (LightningBolt.strikes.size() - 1); i++) {
					Point2D.Double strike1 = LightningBolt.strikes.get(i);
					Point2D.Double strike2 = LightningBolt.strikes.get(i + 1);
					g2d.drawLine((int)strike1.x, (int)strike1.y, (int)strike2.x, (int)strike2.y);
				}
			}
			// End drawing lightning bolt strikes.
			
			synchronized(game.factories) {
				for(EnemyFactory factory : game.factories) {
					if(factory.isAlive()) factory.render(g2d);
				}
			}
			
			synchronized(game.enemies) {
				for(Enemy enemy : game.enemies) {
					if(enemy.isAlive()) enemy.render(g2d);
				}
			}
			
			g2d.setColor(Color.RED);
			g2d.fillOval((int)(game.player.location.x - 10), (int)(game.player.location.y - 10), 20, 20);
			
			// Draw lights in the light factory.
			BufferedImage overlay = new BufferedImage(Game.WIDTH, Game.HEIGHT, BufferedImage.TYPE_INT_ARGB);
			Graphics2D og2d = overlay.createGraphics();
			og2d.setColor(new Color(0.0f, 0.0f, 0.0f, 0.85f));
			og2d.clearRect(0, 0, Game.WIDTH, Game.HEIGHT);
			game.lightFactory.render(overlay);
			
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.85f));
			g2d.drawImage(overlay, 0, 0, null);
			g2d.setComposite(savedComp);
			
			hud.render(g2d);
			
			if(game.isPaused()) {
				Font font = new Font("Serif", Font.PLAIN, 32);
				FontMetrics metrics = g2d.getFontMetrics(font);
				String str = "PAUSED";
				int w = metrics.stringWidth(str);
				int h = metrics.getHeight();
				
				g2d.setColor(Color.WHITE);
				g2d.setFont(font);
				g2d.drawString(str, ((Game.WIDTH / 2) - (w / 2)), ((Game.HEIGHT / 2) - (h / 2)));
			}
		} else if(Game.state == GameState.GAME_OVER) {
			gameOver.render(g2d);
		}
	}
	
	public static boolean inBounds(int x, int y) {
		return ((x >= 0) && (x < Game.WIDTH) && (y >= 0) && (y < Game.HEIGHT));
	}
}
