package com.ludum.gfx;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Point2D;

import javax.swing.JPanel;

import com.ludum.Game;
import com.ludum.GameOver;
import com.ludum.GameState;
import com.ludum.HUD;
import com.ludum.Menu;

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
		} else if((Game.state == GameState.GAME_STARTED) || (Game.state == GameState.LEVEL_CLEAR)) {
			AlphaComposite savedComp = (AlphaComposite)g2d.getComposite();
			game.currentLevel.render(g2d, game);
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
	
	public static double dist(Point2D.Double origin, Point2D.Double target) {
		double a = (target.x - origin.x);
		double b = (target.y - origin.y);
		return Math.sqrt((a * a) + (b * b));
	}
}
