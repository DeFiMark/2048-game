package gameMain;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class Block {

	public Game game;
	public static int width = ( Game.WIDTH - (2*GameBoard.leftRightPadding)) / GameBoard.COLS;
	public static int height = ( Game.HEIGHT - (GameBoard.topMenuSize+GameBoard.botMenuSize) ) / GameBoard.ROWS;
	
	public int posX, posY;
	public int x, y;
	private int type;
		
//	private int delay = 1;
//	private int count = 0;
	
	
	public Block(Game game, int x, int y, int type) {
		this.game = game;
		this.x = x;
		this.y = y;
		this.posX = GameBoard.leftRightPadding + (width * x);
		this.posY = GameBoard.topMenuSize + (height * y);
		this.type = type;
	}
	
	public void render(Graphics g) {
		
		g.setColor(getColor());
		g.fillRect(posX, posY, width - 2, height - 2);
		
		if (type > 0) {
			g.setColor(Color.black);
			g.setFont(new Font("Times New Roman", Font.BOLD, 30));
			g.drawString(String.valueOf(type), posX + (int)Math.floor(width*getMultiplier()), posY + (int)Math.floor(height*0.56));
		}	
	}
	
	public Color getColor() {
		switch (type) {
			case 0:
				return Color.white;
			case 2:
				return new Color(100, 250, 100);
			case 4:
				return new Color(150, 200, 150);
			case 8:
				return new Color(250, 150, 100);
			case 16:
				return new Color(100, 100, 250);
			case 32:
				return new Color(200, 200, 150);
			case 64:
				return new Color(120, 140, 0);
			case 128:
				return new Color(200, 0, 200);
			case 256:
				return new Color(130, 50, 250);
			case 512:
				return new Color(20, 80, 110);
			case 1024:
				return new Color(50, 0, 150);
			case 2048:
				return new Color(250, 50, 70);
			case 4096:
				return new Color(0, 250, 250);
			case 8192:
				return new Color(250, 250, 0);
			case 16384:
				return new Color(30, 50, 40);
			case 32768:
				return new Color(250, 250, 175);
			default:
				return Color.cyan;
		}
	}
	
	public double getMultiplier() {
		switch (type) {
			case 0:
				return 0.41;
			case 2:
				return 0.41;
			case 4:
				return 0.41;
			case 8:
				return 0.41;
			case 16:
				return 0.38;
			case 32:
				return 0.38;
			case 64:
				return 0.38;
			case 128:
				return 0.35;
			case 256:
				return 0.35;
			case 512:
				return 0.35;
			case 1024:
				return 0.30;
			case 2048:
				return 0.30;
			case 4096:
				return 0.30;
			case 8192:
				return 0.30;
			case 16384:
				return 0.25;
			case 32768:
				return 0.25;
			default:
				return 0.4;
		}
	}
	
	public void setType(int newType) {
		type = newType;
	}
	
	public int getType() {
		return type;
	}
	
	public void tick() {
//		count++;
//		if (count >= delay) {
//			count = 0;
//			posY++;
//		}
		posY += game.board.blockSpeed;
		if (GameBoard.topMenuSize + (height * y+1) <= posY && y < GameBoard.COLS - 1) {
			y++;
		}
	}
	
	public void move(int dir) {
		posX = GameBoard.leftRightPadding + ( Block.width * (x+dir));
		x += dir;
	}
	
		
	public int bottomY() {
		return posY + height - 2;
	}
	
}
