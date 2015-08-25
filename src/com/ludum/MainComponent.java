package com.ludum;

import javax.swing.JApplet;
import javax.swing.JFrame;

public class MainComponent extends JApplet implements Runnable {
	private static final long serialVersionUID = 1L;
	
	private Game game;
	
	public MainComponent() {
		
	}
	
	public void start() {
		if(game.running) return;
		game.running = true;
		new Thread(this).start();
	}
	
	@Override
	public void run() {
		while(game.running) {
			game.update();
			game.render();
			
			try {
				Thread.sleep(20);
			} catch(InterruptedException ie) {
				System.out.println("Uh-oh! Spaghettios...");
			}
		}
		
		dispose();
		System.exit(0);
	}
	
	public void stop() {
		if(!game.running) return;
		game.running = false;
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
