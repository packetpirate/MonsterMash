package com.ludum;

import javax.swing.JFrame;

public class MainComponent implements Runnable {
	private Game game;
	private boolean running;
	
	public MainComponent() {
		running = false;
	}
	
	public void start() {
		if(running) return;
		running = true;
		new Thread(this).start();
	}
	
	@Override
	public void run() {
		while(running) {
			game.update();
			game.render();
			
			try {
				Thread.sleep(20);
			} catch(InterruptedException ie) {
				System.out.println("Uh-oh! Spaghettios...");
			}
		}
		
		dispose();
	}
	
	public void stop() {
		if(!running) return;
		running = false;
	}
	
	public void dispose() {
		
	}
	
	public static void main(String[] args) {
		MainComponent main = new MainComponent();
		main.game = new Game();
		
		JFrame frame = new JFrame(Game.TITLE);
		frame.add(main.game.screen);
		
		frame.setResizable(false);
		frame.setAlwaysOnTop(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.pack();
		frame.setVisible(true);
		
		main.start();
	}
}
