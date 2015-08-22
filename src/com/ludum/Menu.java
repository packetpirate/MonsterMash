package com.ludum;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class Menu {
	private Rectangle2D.Double startButton;
	private Color startButtonColor;
	
	public Menu() {
		double w = 200;
		double h = 80;
		double x = ((Game.WIDTH / 2) - (w / 2));
		double y = (Game.HEIGHT / 2);
		startButton = new Rectangle2D.Double(x, y, w, h);
		
		startButtonColor = new Color(0xBF1919);
	}
	
	public void update(Game game) {
		// Determine the background color of the start button.
		if(startButton.contains(game.screen.mousePos)) {
			startButtonColor = new Color(0x6B0E0E);
		} else {
			startButtonColor = new Color(0xBF1919);
		}
	}
	
	public void render(Graphics2D g2d) {
		g2d.setColor(Color.BLACK);
		g2d.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
		
		{ // Draw the title.
			Font font = new Font("Serif", Font.PLAIN, 48);
			FontMetrics metrics = g2d.getFontMetrics(font);
			String str = "MONSTER MASH";
			int x = ((Game.WIDTH / 2) - (metrics.stringWidth(str) / 2));
			int y = (metrics.getHeight() + 10);
			
			g2d.setColor(Color.WHITE);
			g2d.setFont(font);
			g2d.drawString(str, x, y);
		} // End title drawing.
		
		{ // Draw the start button.
			Font font = new Font("Serif", Font.PLAIN, 32);
			FontMetrics metrics = g2d.getFontMetrics(font);
			String str = "Start";
			int w = metrics.stringWidth(str);
			int h = metrics.getHeight();
			
			Path2D.Double path = new Path2D.Double();
			path.moveTo(startButton.x, (startButton.y + 15));
			path.lineTo((startButton.x + 15), startButton.y);
			path.lineTo((startButton.x + startButton.width), startButton.y);
			path.lineTo((startButton.x + startButton.width), (startButton.y + startButton.height - 15));
			path.lineTo((startButton.x + startButton.width - 15), (startButton.y + startButton.height));
			path.lineTo(startButton.x, (startButton.y + startButton.height));
			path.closePath();
			
			g2d.setColor(startButtonColor);
			g2d.fill(path);
			g2d.setColor(Color.WHITE);
			g2d.draw(path);
			
			int x = (int)(startButton.x + (startButton.width / 2) - (w / 2));
			int y = (int)(startButton.y + (startButton.height / 2) + 10);
			
			g2d.setFont(font);
			g2d.setColor(Color.WHITE);
			g2d.drawString(str, x, y);
		} // End drawing start button.
	}
	
	public void dispatchClick(Point2D.Double location) {
		if(startButton.contains(location)) {
			Game.state = GameState.GAME_STARTED;
		}
	}
}
