package com.ludum;

public class GameTime {
    private long elapsedMillis;
    public long getElapsedMillis() { return this.elapsedMillis; }
    public long getElapsedSecs() { return (this.elapsedMillis / 1000); }
    private long lastUpdate;
    private long offset;
    public long getOffset() { return this.offset; }
    public void increaseOffset() { this.offset = System.currentTimeMillis() - this.lastUpdate; }
    
    public GameTime() {
        this.elapsedMillis = 0;
        this.lastUpdate = System.currentTimeMillis();
    }
    
    public void update() {
        this.elapsedMillis += ((System.currentTimeMillis() - this.lastUpdate) - this.offset);
        this.lastUpdate = System.currentTimeMillis();
        this.offset = 0;
    }
    
    public void reset() { 
        this.elapsedMillis = 0;
        this.lastUpdate = System.currentTimeMillis();
        this.offset = 0;
    }
}
