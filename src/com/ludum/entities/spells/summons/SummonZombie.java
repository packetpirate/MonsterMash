package com.ludum.entities.spells.summons;

import java.awt.geom.Point2D;
import java.util.Iterator;

import com.ludum.Game;
import com.ludum.entities.items.Grave;
import com.ludum.entities.minions.Minion;
import com.ludum.entities.spells.Spell;

public class SummonZombie extends Spell {
	public SummonZombie() {
		super("Summon Zombie", 3000, 10, 0);
	}
	
	@Override
	public void update(Game game) {
		
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
								Minion zombie = new Minion(new Point2D.Double(g.location.x, g.location.y), 60, 1, 1, 1);
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
