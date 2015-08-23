package com.ludum;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class GameOver {
	private Rectangle2D.Double retryButton;
	private Color retryButtonColor;
	private Rectangle2D.Double mainMenuButton;
	private Color mainMenuButtonColor;
	
	public GameOver() {
		{ // Create retry button.
			double w = 200;
			double h = 80;
			double x = ((Game.WIDTH / 2) - (w / 2));
			double y = ((Game.HEIGHT / 2) - h);
			
			retryButton = new Rectangle2D.Double(x, y, w, h);
			retryButtonColor = new Color(0xBF1919);
		} // End retry button.
		
		{ // Create main menu button.
			double w = 200;
			double h = 80;
			double x = ((Game.WIDTH / 2) - (w / 2));
			double y = ((Game.HEIGHT / 2) + h);
			
			mainMenuButton = new Rectangle2D.Double(x, y, w, h);
			mainMenuButtonColor = new Color(0xBF1919);
		} // End main menu button.
	}
	
	public void update(Game game) {
		// Determine the background color of the retry button.
		if(retryButton.contains(game.screen.mousePos)) {
			retryButtonColor = new Color(0x6B0E0E);
		} else {
			retryButtonColor = new Color(0xBF1919);
		}
		
		// Determine the background color of the main menu button.
		if(mainMenuButton.contains(game.screen.mousePos)) {
			mainMenuButtonColor = new Color(0x6B0E0E);
		} else {
			mainMenuButtonColor = new Color(0xBF1919);
		}
	}
	
	public void render(Graphics2D g2d) {
		g2d.setColor(Color.BLACK);
		g2d.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
		
		{ // Draw game over text.
			Font font = new Font("Serif", Font.PLAIN, 48);
			FontMetrics metrics = g2d.getFontMetrics(font);
			String str = "GAME OVER";
			int x = ((Game.WIDTH / 2) - (metrics.stringWidth(str) / 2));
			int y = (metrics.getHeight() + 10);
			
			g2d.setColor(Color.WHITE);
			g2d.setFont(font);
			g2d.drawString(str, x, y);
		} // End game over text draw.
		
		{ // Draw retry button.
			Font font = new Font("Serif", Font.PLAIN, 32);
			FontMetrics metrics = g2d.getFontMetrics(font);
			String str = "Retry";
			int w = metrics.stringWidth(str);
			
			Path2D.Double path = new Path2D.Double();
			path.moveTo(retryButton.x, (retryButton.y + 15));
			path.lineTo((retryButton.x + 15), retryButton.y);
			path.lineTo((retryButton.x + retryButton.width), retryButton.y);
			path.lineTo((retryButton.x + retryButton.width), (retryButton.y + retryButton.height - 15));
			path.lineTo((retryButton.x + retryButton.width - 15), (retryButton.y + retryButton.height));
			path.lineTo(retryButton.x, (retryButton.y + retryButton.height));
			path.closePath();
			
			g2d.setColor(retryButtonColor);
			g2d.fill(path);
			g2d.setColor(Color.WHITE);
			g2d.draw(path);
			
			int x = (int)(retryButton.x + (retryButton.width / 2) - (w / 2));
			int y = (int)(retryButton.y + (retryButton.height / 2) + 10);
			
			g2d.setFont(font);
			g2d.setColor(Color.WHITE);
			g2d.drawString(str, x, y);
		} // End retry button draw.
		
		{ // Draw main menu button.
			Font font = new Font("Serif", Font.PLAIN, 32);
			FontMetrics metrics = g2d.getFontMetrics(font);
			String str = "Main Menu";
			int w = metrics.stringWidth(str);
			
			Path2D.Double path = new Path2D.Double();
			path.moveTo(mainMenuButton.x, (mainMenuButton.y + 15));
			path.lineTo((mainMenuButton.x + 15), mainMenuButton.y);
			path.lineTo((mainMenuButton.x + mainMenuButton.width), mainMenuButton.y);
			path.lineTo((mainMenuButton.x + mainMenuButton.width), (mainMenuButton.y + mainMenuButton.height - 15));
			path.lineTo((mainMenuButton.x + mainMenuButton.width - 15), (mainMenuButton.y + mainMenuButton.height));
			path.lineTo(mainMenuButton.x, (mainMenuButton.y + mainMenuButton.height));
			path.closePath();
			
			g2d.setColor(mainMenuButtonColor);
			g2d.fill(path);
			g2d.setColor(Color.WHITE);
			g2d.draw(path);
			
			int x = (int)(mainMenuButton.x + (mainMenuButton.width / 2) - (w / 2));
			int y = (int)(mainMenuButton.y + (mainMenuButton.height / 2) + 10);
			
			g2d.setFont(font);
			g2d.setColor(Color.WHITE);
			g2d.drawString(str, x, y);
		} // End main menu button draw.
	}
	
	public void dispatchClick(Point2D.Double location) {
		if(retryButton.contains(location)) {
			Game.state = GameState.GAME_STARTED;
		} else if(mainMenuButton.contains(location)) {
			Game.state = GameState.MENU;
		}
	}
}
