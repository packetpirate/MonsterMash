package com.ludum.entities;

import com.ludum.Game;

public class Status {
	public String name;
	private long duration;
	private long created;
	
	public boolean isActive() {
		return (Game.time.getElapsedMillis() < (created + duration));
	}
	
	public Status(String name, long duration) {
		this.name = name;
		this.duration = duration;
		this.created = Game.time.getElapsedMillis();
	}
}
