package com.ludum.entities;

import java.awt.Color;
import java.awt.geom.Point2D;

public enum LightType {
	PLAYER(80, 5, Color.WHITE, false, 0, -1),
	TORCH(150, 5, Color.ORANGE, true, 5, -1),
	ELDRITCH_BOLT(10, 0, new Color(0x66E066), false, 0, -1),
	FIREBALL(40, 1, Color.ORANGE, true, 10, -1),
	EXPLOSION(60, 0, new Color(0xFFD633), true, 40, 1000),
	LIGHTNING_BOLT(15, 0, Color.WHITE, true, 5, -1),
	LIGHTNING_STRIKE(20, 0, new Color(0xE6FFFF), true, 5, 1000);
	
	int radius;
	int minRadius;
	int maxRadius;
	int brightness;
	Color color;
	boolean flicker;
	int flickerVariance;
	long duration;
	
	LightType(int radius, int brightness, Color color, boolean flicker, int flickerVariance, long duration) {
		this.radius = radius;
		this.minRadius = radius;
		this.maxRadius = radius;
		this.brightness = brightness;
		this.color = color;
		this.flicker = flicker;
		this.flickerVariance = flickerVariance;
		this.duration = duration;
	}
	
	public static Light createLight(Point2D.Double location, LightType type) {
		return new Light(new Point2D.Double(location.x, location.y), 
						 type.radius, type.brightness, type.color, 
						 type.flicker, type.flickerVariance, type.duration);
	}
}
