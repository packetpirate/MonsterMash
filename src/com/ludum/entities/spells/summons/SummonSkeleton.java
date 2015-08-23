package com.ludum.entities.spells.summons;

import java.awt.geom.Point2D;
import java.util.Iterator;

import com.ludum.Game;
import com.ludum.entities.items.Grave;
import com.ludum.entities.minions.Minion;
import com.ludum.entities.spells.Spell;

public class SummonSkeleton extends Spell {
	public static final int FEAR_RADIUS = 100;
	public static final int FEAR_CHANCE = 4; // as in 4%
	// TODO: play with fear chance percentage a bit
	
	public SummonSkeleton() {
		super("Summon Skeleton", 5000, 20, 0);
	}
	
	@Override
	public void update(Game game) {
		// TODO: Think of something unique for skeletons to do.
		// Maybe because they're so spoopy, regular peasants flee from them?
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
														   200, 3, 3, 3);
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
