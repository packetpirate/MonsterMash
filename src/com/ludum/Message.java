package com.ludum;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;

public class Message {
	private String text;
	protected Point2D.Double center;
	private long created;
	private long duration; // duration of -1 is permanent
	public boolean isActive() { 
		return ((duration == -1) || (Game.time.getElapsedMillis() < (created + duration)));
	}
	
	public Message(String text, Point2D.Double center, long duration) {
		this.text = text;
		this.center = center;
		this.created = Game.time.getElapsedMillis();
		this.duration = duration;
	}
	
	public void update(Game game) {
		// Used in special cases, such as if the message needs to lock on to a moving object.
	}
	
	public void render(Graphics2D g2d) {
		Font saved = g2d.getFont();
		Font font = new Font("Serif", Font.ITALIC, 12);
		FontMetrics metrics = g2d.getFontMetrics();
		
		int w = metrics.stringWidth(text);
		int x = (int)(center.x - (w / 2));
		int y = (int) center.y;
		
		g2d.setFont(font);
		g2d.setColor(Color.WHITE);
		g2d.drawString(text, x, y);
		g2d.setFont(saved);
	}
}
