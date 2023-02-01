package gameMain;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import gameMain.Game.STATE;

public class Menu {

	public Game game;
	public int optionIndex;
	public int playerIndex;
	public boolean[] options;
	public int boxX, boxW, boxH, boxDistApart, prettyRectDistApart;
	public String[] titles = {"Easy", "Normal", "Hard", "Insane", "Autistic"};
	
	public String[] players = {"Mark", "Jasmine", "Compton"};
	public boolean[] playerOptions;
	
	public boolean onPlayerSelect = false;
	
	public Menu(Game game) {
		this.game = game;
		options = new boolean[titles.length];
		playerOptions = new boolean[players.length];
		boxX = 250;
		boxW = 500;
		boxH = 120;
		boxDistApart = 35;
		prettyRectDistApart = 3;
	}
	
	public void reset() {
		optionIndex = 0;
		playerIndex = 0;
		onPlayerSelect = false;
		options = new boolean[titles.length];
		playerOptions = new boolean[players.length];
		game.board.reset();
	}
	
	public void render(Graphics g) {
		int thickness = 4;
		int startHeight = 60;
		g.setColor(Color.black);
		g.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
		int len = onPlayerSelect ? players.length : titles.length;
		for (int i = 0; i < len; i++) {
			drawGapStringRectangle(
					g, 
					Color.RED,
					boxX, 
					startHeight + ((boxH + boxDistApart) * i), 
					boxW, 
					boxH, 
					thickness, 
					onPlayerSelect ? players[i] : titles[i]
			);
		}
		g.setColor(Color.BLUE);
		int index = onPlayerSelect ? playerIndex : optionIndex;
		for (int i = 0; i < (thickness + prettyRectDistApart + 1); i++) {
			g.drawRect(boxX + thickness + i, startHeight + ((boxH + boxDistApart) * index) + thickness + i, boxW - thickness - (2*i), boxH - thickness - (2*i));
		}
		}
	
	private void drawPrettyRect(Graphics g, Color color, int startX, int startY, int width, int height, int thickness) {
		
		g.setColor(color);
		for (int i = 0; i < thickness; i++) {
			g.drawRect(startX + i, startY + i, width, height);
			g.drawRect(startX + (prettyRectDistApart * thickness) + i, startY +(prettyRectDistApart * thickness) + i, width - ((2*prettyRectDistApart) * thickness), height - ((2*prettyRectDistApart) * thickness));
		}
	
	}
	
	public void drawGapStringRectangle(Graphics g, Color color, int startX, int startY, int width, int height, int thickness, String string) {
		drawPrettyRect(g, color, startX, startY, width, height, thickness);
		int font = width/20;
		g.setFont(new Font("Times New Roman", Font.BOLD, font));
		if (string != null) g.drawString(string, startX + (width/2) - (font*2) - (3*string.length()), startY + (height/2) + 10);
		
	}
	
	public void incSelectedOptions() {
		
		if (onPlayerSelect) {
			playerOptions[playerIndex] = false;
			playerIndex++;
			if (playerIndex >= playerOptions.length) {
				playerIndex = 0;
			}
			playerOptions[playerIndex] = true;
		} else {
			options[optionIndex] = false;
			optionIndex++;
			if (optionIndex >= options.length)
				optionIndex = 0;
			options[optionIndex] = true;
		}
		
	}

	public void decSelectedOptions() {
		
		if (onPlayerSelect) {
			playerOptions[playerIndex] = false;
			playerIndex--;
			if (playerIndex < 0) {
				playerIndex = players.length - 1;
			}
			playerOptions[playerIndex] = true;
		} else {
			options[optionIndex] = false;
			optionIndex--;
			if (optionIndex < 0)
				optionIndex = options.length - 1;
			options[optionIndex] = true;
		}
	}
	
	public void select() {
		if (!onPlayerSelect) {
			onPlayerSelect = true;
		} else {
			
			int speed = 0;
			switch (optionIndex) {
				case 0:
					speed = 2;
					break;
				case 1:
					speed = 4;
					break;
				case 2:
					speed = 6;
					break;
				case 3:
					speed = 8;
					break;
				case 4:
					speed = 10;
					break;
			}
			
			game.board.newGame(speed);
			game.setGameState(STATE.Game);
		}
	}
	
	public String getPlayer() {
		return players[playerIndex];
	}
}
