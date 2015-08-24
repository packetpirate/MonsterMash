package com.ludum.entities.spells.summons;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.Iterator;

import com.ludum.Game;
import com.ludum.entities.items.Grave;
import com.ludum.entities.minions.Minion;
import com.ludum.entities.spells.Spell;
import com.ludum.gfx.Textures;

public class SummonSkeleton extends Spell {
	public static final String NAME = "Summon Skeleton";
	public static final int FEAR_RADIUS = 100;
	public static final int FEAR_CHANCE = 4; // as in 4%
	// TODO: play with fear chance percentage a bit
	
	@Override
	public String getName() { return SummonSkeleton.NAME; }
	
	public SummonSkeleton() {
		super(5000, 20, 0);
	}
	
	@Override
	public void update(Game game) {
		// TODO: Think of something unique for skeletons to do.
		// Maybe because they're so spoopy, regular peasants flee from them?
	}
	
	@Override
	public void renderIcon(Graphics2D g2d, Point2D.Double position) {
		if(Textures.SUMMON_SKELETON_ICON.img != null) {
			g2d.drawImage(Textures.SUMMON_SKELETON_ICON.img, (int)position.x, (int)position.y, null);
		}
	}
	
	@Override
	public void cast(Game game) {
		if(game.player.currentMana() >= manaCost) {
			synchronized(game.graves) {
				if(!game.graves.isEmpty()) {
					Iterator<Grave> it = game.graves.iterator();
					while(it.hasNext()) {
						Grave g = it.next();
						
						if(g.isAlive()) {
							if(canCast() && g.contains(new Point2D.Double(game.screen.mousePos.x, 
															 			  game.screen.mousePos.y))) {
								// TODO: Nerf health and damage. 2spooky4me
								Minion skeleton = new Minion("Skeleton", new Point2D.Double(g.location.x, g.location.y), 
														   200, 3, 3, 3) {
									@Override
									public void render(Graphics2D g2d) {
										if(Textures.SKELETON.img != null) {
											int x = (int)(location.x - (Textures.SKELETON.img.getWidth() / 2));
											int y = (int)(location.y - (Textures.SKELETON.img.getHeight() / 2));
											g2d.drawImage(Textures.SKELETON.img, x, y, null);
										}
									}
								};
								game.player.summon(skeleton);
								game.player.useMana(manaCost);
								lastCast = Game.time.getElapsedMillis();
								it.remove();
								continue;
							}
						}
					}
				}
			}
		}
	}
}
