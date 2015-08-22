package com.ludum.gfx;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

import com.ludum.Game;
import com.ludum.entities.EnemyFactory;
import com.ludum.entities.Player;
import com.ludum.entities.enemies.Enemy;
import com.ludum.entities.spells.SpellEffect;

public class Screen extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private Game game;
	public Point mousePos;
	
	public boolean mouseDown;
	public boolean keys[];

	public Screen(Game game) {
		Dimension d = new Dimension(Game.WIDTH, Game.HEIGHT);
		
		setMinimumSize(d);
		setMaximumSize(d);
		setPreferredSize(d);
		
		mouseDown = false;
		mousePos = new Point();
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent m) {
				mouseDown = true;
			}
			
			@Override
			public void mouseReleased(MouseEvent m) {
				mouseDown = false;
			}
		});
		
		addMouseMotionListener(new MouseAdapter() {
			@Override
			public void mouseMoved(MouseEvent m) {
				mousePos = m.getPoint();
			}
			
			@Override
			public void mouseDragged(MouseEvent m) {
				mousePos = m.getPoint();
			}
		});
		
		keys = new boolean[5];
		for(int i = 0; i < 5; i++) {
			keys[i] = false;
		}
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent k) {
				int key = k.getKeyCode();
				
				if(key == KeyEvent.VK_W) keys[0] = true;
				if(key == KeyEvent.VK_A) keys[1] = true;
				if(key == KeyEvent.VK_S) keys[2] = true;
				if(key == KeyEvent.VK_D) keys[3] = true;
				
				if(key == KeyEvent.VK_P) {
					if(!keys[4]) game.togglePause();
					keys[4] = true;
				}
			}
			
			@Override
			public void keyReleased(KeyEvent k) {
				int key = k.getKeyCode();
				
				if(key == KeyEvent.VK_W) keys[0] = false;
				if(key == KeyEvent.VK_A) keys[1] = false;
				if(key == KeyEvent.VK_S) keys[2] = false;
				if(key == KeyEvent.VK_D) keys[3] = false;
				
				if(key == KeyEvent.VK_P) keys[4] = false;
			}
		});
		
		this.game = game;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D)g;
		
		g2d.setColor(new Color(0x336600));
		g2d.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
		
		for(SpellEffect effect : game.spellEffects) {
			if(effect.alive) effect.render(g2d);
		}
		
		for(EnemyFactory factory : game.factories) {
			if(factory.isAlive()) factory.render(g2d);
		}
		
		for(Enemy enemy : game.enemies) {
			if(enemy.isAlive()) enemy.render(g2d);
		}
		
		g2d.setColor(Color.RED);
		g2d.fillOval((int)(game.player.location.x - 10), (int)(game.player.location.y - 10), 20, 20);
		
		{ // Draw the health and mana bars.
			// Draw the health bar.
			double hW = (game.player.currentHealth() / Player.MAX_HEALTH) * 94;
			g2d.setColor(Color.BLACK);
			g2d.fillRect(5, 5, 102, 24);
			g2d.setColor(Color.RED);
			g2d.fillRect(9, 9, (int)hW, 16);
			
			// Draw the mana bar.
			double mW = (game.player.currentMana() / Player.MAX_MANA) * 94;
			g2d.setColor(Color.BLACK);
			g2d.fillRect(5, 31, 102, 24);
			g2d.setColor(Color.BLUE);
			g2d.fillRect(9, 35, (int)mW, 16);
		} // End drawing health and mana.
		
		g2d.setColor(Color.BLACK);
		g2d.drawString(("Current Spell: " + game.player.getCurrentSpell().getName()), 5, 70);
		g2d.drawString(("Spells On Screen: " + Integer.toString(game.spellEffects.size())), 5, 85);
		
		if(game.isPaused()) {
			Font font = new Font("Serif", Font.PLAIN, 32);
			FontMetrics metrics = g2d.getFontMetrics(font);
			String str = "PAUSED";
			int w = metrics.stringWidth(str);
			int h = metrics.getHeight();
			
			g2d.setColor(Color.BLACK);
			g2d.setFont(font);
			g2d.drawString(str, ((Game.WIDTH / 2) - (w / 2)), ((Game.HEIGHT / 2) - (h / 2)));
		}
	}
}
