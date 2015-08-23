package com.ludum.gfx;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public enum Textures {
	GRASS(Textures.loadImage("/images/grass1.png")),
	GRAVE(Textures.loadImage("/images/grave.png")),
	HUD1(Textures.loadImage("/images/hud1.png")),
	
	NECROMANCER(Textures.loadImage("/images/necromancer.png")),
	
	ELDRITCH_BOLT(Textures.loadImage("/images/eldritch_bolt.png")),
	ELDRITCH_BOLT_ICON(Textures.loadImage("/images/eldritch_bolt_icon.png")),
	FIREBALL(Textures.loadImage("/images/fireball.png")),
	FIREBALL_ICON(Textures.loadImage("/images/fireball_icon.png")),
	LIGHTNING_BOLT_ICON(Textures.loadImage("/images/lightning_bolt_icon.png")),
	SUMMON_ZOMBIE_ICON(Textures.loadImage("/images/summon_zombie_icon.png")),
	SUMMON_SKELETON_ICON(Textures.loadImage("/images/summon_skeleton_icon.png")),
	
	ZOMBIE(Textures.loadImage("/images/zombie.png")),
	SKELETON(Textures.loadImage("/images/skeleton.png"));
	
	public BufferedImage img;
	
	Textures(BufferedImage img) {
		this.img = img;
	}
	
	public static BufferedImage loadImage(String path) {
		try {
			BufferedImage image = ImageIO.read(Textures.class.getResourceAsStream(path));
			return image;
		} catch(IOException io) {
			System.out.printf("Encountered a problem loading file \"%s\". Aborting.\n", path);
			System.exit(1);
		}
		
		return null;
	}
}
