package com.ludum.entities;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class LightFactory {
	public List<Light> lights;
	
	public LightFactory() {
		lights = Collections.synchronizedList(new ArrayList<>());
	}
	
	public void reset() {
		lights.clear();
	}
	
	public Light createLight(Point2D.Double location, LightType type) {
		Light light = LightType.createLight(location, type);
		lights.add(light);
		return light;
	}
	
	public void destroyLights() {
		synchronized(lights) {
			if(!lights.isEmpty()) {
				Iterator<Light> it = lights.iterator();
				while(it.hasNext()) {
					Light light = it.next();
					
					if(!light.isAlive()) {
						it.remove();
						break;
					}
				}
			}
		}
	}
	
	public void render(BufferedImage overlay) {
		synchronized(lights) {
			if(!lights.isEmpty()) {
				Iterator<Light> it = lights.iterator();
				while(it.hasNext()) {
					Light light = it.next();
					
					if(light.isAlive()) light.render(overlay);
					else it.remove();
					
					continue;
				}
			}
		}
	}
}
