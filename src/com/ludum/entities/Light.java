package com.ludum.entities;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.Random;

import com.ludum.Game;

public class Light {
	public Point2D.Double location;
	public int radius;
	public int maxRadius;
	public int minRadius;
	public int brightness;
	public Color color;
	public long creation;
	public long duration; // Duration of -1 = permanent
	public boolean flicker;
	private boolean alive;
	public void killLight() { alive = false; }
	public boolean isAlive() {
		return (alive && ((duration == -1) || (Game.time.getElapsedMillis() < (creation + duration))));
	}
	
	public Light(Point2D.Double location, int radius, int brightness, Color color, long duration) {
		this.location = location;
		this.radius = radius;
		this.maxRadius = radius;
		this.minRadius = radius;
		this.brightness = brightness;
		this.color = color;
		this.creation = Game.time.getElapsedMillis();
		this.duration = duration;
		this.flicker = false;
		this.alive = true;
		
		if(brightness > radius) {
			this.brightness = radius;
		}
	}
	
	public Light(Point2D.Double location, int radius, int brightness, Color color, boolean flicker, int flickerVariance, long duration) {
		this(location, radius, brightness, color, duration);
		
		this.flicker = flicker;
		this.maxRadius = radius + flickerVariance;
		this.minRadius = radius - flickerVariance;
	}
	
	public void render(BufferedImage overlay) {
		Random rand = new Random();
		
		int x = (int)location.x;
		int y = (int)location.y;
		int radius = (flicker) ?
						rand.nextInt(maxRadius - minRadius) + minRadius :
						this.radius;
		
		int minX = (int)Math.max((x - radius), 0);
		int maxX = (int)Math.min((x + radius), (Game.WIDTH - 1));
		int minY = (int)Math.max((y - radius), 0);
		int maxY = (int)Math.min((y + radius), (Game.HEIGHT - 1));
		
		float rS = radius * radius;
		
		for(int i = minX; i <= maxX; i++) {
			for(int j = minY; j <= maxY; j++) {
				int dx = i - x;
				int dy = j - y;
				if(((dx * dx) + (dy * dy)) <= rS) {
					double dist = Math.sqrt((dx * dx) + (dy * dy));
					double ratio = 0.0;

					if (dist >= brightness) {
						ratio = (dist - brightness) / (radius - brightness);
					} else {
						ratio = 0.0;
					}
					
					ratio = Math.sqrt(ratio);
					
					int rgb = overlay.getRGB(i, j);
					int a = (rgb >> 24) & 0xff;
					int r = (rgb >> 16) & 0xff;
					int g = (rgb >> 8) & 0xff;
					int b = rgb & 0xff;
					
					int nA = (int)(ratio * (double)(0xff));
					int nR = (int)((r * ratio) + (color.getRed() * (1 - ratio)));
					int nG = (int)((g * ratio) + (color.getGreen() * (1 - ratio)));
					int nB = (int)((b * ratio) + (color.getBlue() * (1 - ratio)));
					
					rgb = (nA << 24) | (nR << 16) | (nG << 8) | nB;
					
					overlay.setRGB(i, j, rgb);
				}
			}
		}
	}
}
