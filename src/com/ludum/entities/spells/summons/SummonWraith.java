package com.ludum.entities.spells.summons;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.Iterator;

import com.ludum.Game;
import com.ludum.entities.items.Grave;
import com.ludum.entities.minions.Minion;
import com.ludum.entities.spells.Spell;
import com.ludum.gfx.Textures;

public class SummonWraith extends Spell {
	public static final String NAME = "Summon Wraith";
	
	@Override
	public String getName() { return SummonWraith.NAME; }
	
	public SummonWraith() {
		super(10000, 50, 0);
	}
	
	@Override
	public void update(Game game) {
		// TODO: Think of something for wraiths to do.
	}
	
	@Override
	public void renderIcon(Graphics2D g2d, Point2D.Double position) {
		if(Textures.SUMMON_WRAITH_ICON.img != null) {
			g2d.drawImage(Textures.SUMMON_WRAITH_ICON.img, (int)position.x, (int)position.y, null);
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
								Minion wraith = new Minion("Wraith", new Point2D.Double(g.location.x, g.location.y), 
														   150, 3, 5, 8) {
									@Override
									public void render(Graphics2D g2d) {
										if(Textures.WRAITH.img != null) {
											int x = (int)(location.x - (Textures.WRAITH.img.getWidth() / 2));
											int y = (int)(location.y - (Textures.WRAITH.img.getHeight() / 2));
											g2d.drawImage(Textures.WRAITH.img, x, y, null);
										}
									}
								};
								game.player.summon(wraith);
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
