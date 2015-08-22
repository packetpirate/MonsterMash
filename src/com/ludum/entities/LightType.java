package com.ludum.entities;

import java.awt.Color;
import java.awt.geom.Point2D;

public enum LightType {
	PLAYER(50, 10, Color.WHITE, false, 0),
	TORCH(150, 50, Color.ORANGE, true, 5),
	FIREBALL(40, 5, Color.ORANGE, true, 10);
	
	int radius;
	int minRadius;
	int maxRadius;
	int brightness;
	Color color;
	boolean flicker;
	int flickerVariance;
	
	LightType(int radius, int brightness, Color color, boolean flicker, int flickerVariance) {
		this.radius = radius;
		this.minRadius = radius;
		this.maxRadius = radius;
		this.brightness = brightness;
		this.color = color;
		this.flicker = flicker;
		this.flickerVariance = flickerVariance;
	}
	
	public static Light createLight(Point2D.Double location, LightType type) {
		return new Light(new Point2D.Double(location.x, location.y), 
						 type.radius, type.brightness, type.color, type.flicker, type.flickerVariance);
	}
}
