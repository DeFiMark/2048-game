package gameMain;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.util.ArrayList;
//import java.util.Scanner;
//import java.util.TreeMap;

//import gameMain.Window;

//import ai_MachineIntelligence.PathGenerator;
//import chapterDesign.ChapterDesigner;
//import characters.*;
//import extras.*;
//import graphics.PlayerInfoGFX;
//import graphics.PopUpMenu;
//import tiles.Tile;

public class Game extends Canvas implements Runnable {

	private static final long serialVersionUID = 12345L;
	//All of these do not pertain to the constructor
	public final static int WIDTH = 1000;
	public final static int HEIGHT = WIDTH;//WIDTH/5 * 4 + 34;
	
	private boolean running = false;
//	public static int nRow = 5;
//	public static int scale = WIDTH/nRow;
	
	public Menu menu;
	public GameBoard board;
	public EndScreen endScreen;
	
	public enum STATE {
		Game,
		Menu,
		EndScreen
	}
	
	public STATE gameState = STATE.Menu;
	
	public Game() {		
		this.addKeyListener(new KeyInput(this));
		menu = new Menu(this);
		endScreen = new EndScreen(this);
		board = new GameBoard(this);
		new Window(WIDTH, HEIGHT, "2048", this);
	}
	
	public void tick() {
		
		if (gameState == STATE.Game) {
			board.tick();
		}
		
	}
	public void render(Graphics g) {
		
		g.setColor(Color.black);
		g.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);

		if (gameState == STATE.Game) {
			board.render(g);
		} else if (gameState == STATE.Menu) {
			menu.render(g);
		} else if (gameState == STATE.EndScreen) {
			endScreen.render(g);
		}
	}
	
	public void setGameState(STATE state) {
		this.gameState = state;
	}
	
	public void run() {
		running = true;
		this.requestFocus();
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		long timer = System.currentTimeMillis();
//		 int frames = 0;
		while (running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while (delta >= 1) {
				tick();
				delta--;
			}
			if (running) {
				renderGame();
			}
//			 frames++;

			if (System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
//				 System.out.println("FPS: " + frames);
//				 frames = 0;
			}
		}
		stop();
	}
	public void renderGame() {
		BufferStrategy bs = this.getBufferStrategy();
		if (bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		Graphics g = bs.getDrawGraphics();
		
		render(g);
		
		g.dispose();
		bs.show();
	}

	public synchronized void start() {
		running = true;
		run();
	}

	public synchronized void stop() {
		try {
//			thread.join();
			running = false;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static int clamp(int var, int min, int max) {
		if (var >= max) {
			return var = max;
		} else if (var <= min) {
			return var = min;
		} else {
			return var;
		}
	}
	public static void main(String[] args) {
		new Game();
	}
	
	public void renderLoseGame(Graphics g) {
		g.setColor(Color.black);
		g.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
		g.setColor(Color.red);
		g.setFont(new Font("Times New Roman", Font.BOLD, 55));
		g.drawString("YOU LOSE", Game.HEIGHT/3 + Game.HEIGHT/11, Game.WIDTH/3 + Game.WIDTH/7);
	}
	public void renderStartScreen(Graphics g) {
		g.setColor(Color.DARK_GRAY);
		g.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
		
		g.setColor(Color.RED);
		g.setFont(new Font("Times New Roman", Font.ITALIC, 80));
		g.drawString("Fire Emblem", Game.WIDTH/2 - 220, Game.HEIGHT/2-40);
		g.setFont(new Font("Times New Roman", Font.ITALIC, 50));
		g.drawString("Press Any Button To Continue", Game.WIDTH/2 - 300, Game.HEIGHT/2 + 40);
	}
	
}
