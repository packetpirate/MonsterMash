package com.ludum.entities.spells.summons;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.Iterator;

import com.ludum.Game;
import com.ludum.entities.items.Grave;
import com.ludum.entities.minions.Minion;
import com.ludum.entities.spells.Spell;
import com.ludum.gfx.Textures;

public class SummonZombie extends Spell {
	public static final String NAME = "Summon Zombie";
	
	@Override
	public String getName() { return SummonZombie.NAME; }
	
	public SummonZombie() {
		super(2000, 10, 0);
	}
	
	@Override
	public void update(Game game) {
		
	}
	
	@Override
	public void renderIcon(Graphics2D g2d, Point2D.Double position) {
		if(Textures.SUMMON_ZOMBIE_ICON.img != null) {
			g2d.drawImage(Textures.SUMMON_ZOMBIE_ICON.img, (int)position.x, (int)position.y, null);
		}
	}
	
	@Override
	public void cast(Game game) {
		if(game.player.currentMana() >= manaCost) {
			synchronized(game.currentLevel.graves) {
				if(!game.currentLevel.graves.isEmpty()) {
					Iterator<Grave> it = game.currentLevel.graves.iterator();
					while(it.hasNext()) {
						Grave g = it.next();
						
						if(g.isAlive()) {
							if(canCast() && g.contains(new Point2D.Double(game.screen.mousePos.x, 
															 			  game.screen.mousePos.y))) {
								Minion zombie = new Minion("Zombie", new Point2D.Double(g.location.x, g.location.y), 60, 1, 1, 1) {
									@Override
									public void render(Graphics2D g2d) {
										if(Textures.ZOMBIE.img != null) {
											int x = (int)(location.x - (Textures.ZOMBIE.img.getWidth() / 2));
											int y = (int)(location.y - (Textures.ZOMBIE.img.getHeight() / 2));
											g2d.drawImage(Textures.ZOMBIE.img, x, y, null);
										}
									}
								};
								game.player.summon(zombie);
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
