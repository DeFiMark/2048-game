package gameMain;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Random;

import gameMain.Game.STATE;

public class GameBoard {

	public Game game;

	public int boxWidth, boxHeight;
	public static int COLS = 6;
	public static int ROWS = 6;
	public static int topMenuSize = Game.HEIGHT * 30 / 100;
	public static int botMenuSize = 50;
	public static int leftRightPadding = Game.WIDTH/10;//Game.WIDTH / 20;
	public static int blockStartHeight = topMenuSize * 25 / 40;
	
	Random rng;
	
	/** 2D Array of Blocks */
	public Block[][] blockMap;
	
	/** A List of all the Blocks in this map */
	public ArrayList<Block> blocks;
	
	/** Current Block The User Is In Control Of */
	Block currentBlock;
	
	/** Next Block To Fall */
	public int nextBlock;
	
	/** Max Number Index On The Board */
	public int maxNumberIndex;
	
	/** Max Number On The Board */
	public int maxNumberOnBoard;
	
	/** Current Score Of The Game */
	public int score;
	
	/** Speed That Blocks Fall */
	public int blockSpeed;
	
	public GameBoard(Game game) {
		this.game = game;
	}
	
	public void newGame(int blockSpeed) {
			
		this.blockSpeed = blockSpeed;
		rng = new Random();
		
		blocks = new ArrayList<>();
		blockMap = new Block[ROWS][COLS];
		
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLS; j++) {
				Block b = new Block(
						game, 
						i,//leftRightPadding + (Block.width * i), 
						j,//topMenuSize + (Block.height * j), 
//						(j == COLS - 1 && i == 0) ? 2 : 
						0
				);
				blockMap[i][j] = b;
				blocks.add(b);
			}
		}
		maxNumberIndex = 3;
		generateNextBlock();
		currentBlock = new Block(game, 0, -1, nextBlock);
		generateNextBlock();
	}
	
	public void render(Graphics g) {
		
		g.setColor(Color.white);
		g.setFont(new Font("Times New Roman", Font.BOLD, 22));
		g.drawString("Score: " + score, leftRightPadding, topMenuSize / 5);
		g.drawString(highScoreString(), leftRightPadding, topMenuSize / 5 + 30);
		g.setFont(new Font("Times New Roman", Font.BOLD, 50));
		g.drawString("Next Up: " + String.valueOf(nextBlock), Game.WIDTH/2 - 90, topMenuSize / 3 + 45);
		
		for (int i = 0; i < blocks.size(); i++) {
			Block b = blocks.get(i);
			b.render(g);
		}
		currentBlock.render(g);
	}
	
	
	
	public void tick() {
		currentBlock.tick();
		int collission = collissionDetected();
		if (collission > 0) {
			int currentX = currentBlock.x;//getCurrentX();

			// set current block data
			blockMap[currentX][collission - 1].setType(currentBlock.getType());
			currentBlock.x = currentX;
			currentBlock.y = collission - 1;
						
			// search around the current block for matches
			ArrayList<Block> matches = checkForMatches(currentX, collission - 1, blockMap[currentX][collission - 1].getType());
			if (matches.size() > 0) {
				
				for (int i = 0; i < matches.size(); i++) {
					
					Block b = matches.get(i);
					System.out.println("MATCH: " + b.x + "," + b.y );
					blockMap[b.x][b.y].setType(0);;
				}
				
				// new block size
				int newBlockSize = blockMap[currentX][collission - 1].getType() * 2;
				
				// increment score
				incrementScore(newBlockSize);
						
				// compare to old highest size
				incrementMaxNumber(newBlockSize);
				
				// double block's size
				blockMap[currentX][collission - 1].setType(newBlockSize);
				
				// check to see if any whitespaces are present
				checkWhiteSpace();
				
				// check the entire board for possible matches due to white space change
				checkEntireBoardForMatches();				
			}
			
			// set the current block to be a new block
			currentBlock = new Block(game, currentX, -3, nextBlock);
			
			// generate the next block that will be dropped
			generateNextBlock();
			
		} else if (collission == 0) {
			endGame();
		}
	}
	
	public void checkWhiteSpace() {
		
		boolean[] hasWhiteSpace = new boolean[ROWS];
		boolean hasAnyWhiteSpace = false;
		for (int i = 0; i < ROWS; i++) {
			hasWhiteSpace[i] = false;
		}
		
		for (int i = 0; i < ROWS; i++) {
			boolean hasUnit = false;
			for (int j = 0; j < COLS; j++) {
				if (blockMap[i][j].getType() > 0) {
					hasUnit = true;
				} else {
					if (hasUnit) {
						hasWhiteSpace[i] = true;
						hasAnyWhiteSpace = true;
					}
				}
			}
		}
		
		if (hasAnyWhiteSpace) {
			for (int i = 0; i < ROWS; i++) {
				if (!hasWhiteSpace[i]) {
					continue;
				}
				for (int j = COLS - 1; j >= 1; j--) {
					if (blockMap[i][j].getType() == 0) {
						blockMap[i][j].setType(blockMap[i][j-1].getType());
						blockMap[i][j-1].setType(0);
					}
				}
			}
		}		
	}
	
	public void checkEntireBoardForMatches() {
		
		System.out.println("Checking the entire board for matches");
		
		ArrayList<Block> matches;
		boolean foundMatch = false;
		
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLS; j++) {
				matches = checkForMatches(i, j, blockMap[i][j].getType());
				if (matches.size() > 0) {
					foundMatch = true;
					for (int z = 0; z < matches.size(); z++) {
						
						Block b = matches.get(z);
						System.out.println("MATCH: " + b.x + "," + b.y );
						blockMap[b.x][b.y].setType(0);;
					}
					
					// new block size
					int newBlockSize = blockMap[i][j].getType() * 2;
					
					// increment score
					incrementScore(newBlockSize);
							
					// compare to old highest size
					incrementMaxNumber(newBlockSize);
					
					// double block's size
					blockMap[i][j].setType(newBlockSize);
					
					// check to see if any whitespaces are present
					checkWhiteSpace();
					
					// break out of loop to re-enter
					break;
				}
			}
		}
		
		if (foundMatch) {
			checkEntireBoardForMatches();
		} else {
			return;
		}
	}
	
	public ArrayList<Block> checkForMatches(int x, int y, int type) {
		
		ArrayList<Block> matches = new ArrayList<>();
		
		if (type < 2) {
			return matches;
		}
		
		// check in left right and beneath
		if (x > 0) {
			if (blockMap[x-1][y].getType() == type) {
				matches.add(blockMap[x-1][y]);
//				System.out.println("MATCH FOUND AT " + (x-1) + "," + y);
			}
		}
		
		if (y < COLS-1) {
			if (blockMap[x][y+1].getType() == type) {
				matches.add(blockMap[x][y+1]);
//				System.out.println("MATCH FOUND AT " + x + "," + (y+1));
			}
		}
		
		if (x < ROWS-1) {
			if (blockMap[x+1][y].getType() == type) {
				matches.add(blockMap[x+1][y]);
//				System.out.println("MATCH FOUND AT " + (x+1) + "," + y);
			}
		}
		
		return matches;
	}
	
	private void incrementMaxNumber(int newBlockSize) {
		if (maxNumberOnBoard < newBlockSize) {
			maxNumberOnBoard = newBlockSize;
			if (newBlockSize != 64 && newBlockSize != 256 && newBlockSize != 1024 && newBlockSize != 8192) {
				maxNumberIndex++;
			}
		}
	}
	
	public void moveRight() {
		int currentX = currentBlock.x;//getCurrentX();
		if (currentX == ROWS - 1) {
			return;
		}
		int bottomY = currentBlock.bottomY();
		
		for (int i = 0; i < COLS; i++) {
			if (bottomY >= blockMap[currentX+1][i].posY && blockMap[currentX+1][i].getType() > 0) {
				return;
			}
		}
		
		currentBlock.move(1);
	}
	
	public void moveLeft() {
		int currentX = currentBlock.x;//getCurrentX();
		if (currentX == 0) {
			return;
		}
		
		int bottomY = currentBlock.bottomY();
		
		for (int i = 0; i < COLS; i++) {
			if (bottomY >= blockMap[currentX-1][i].posY && blockMap[currentX-1][i].getType() > 0) {
				return;
			}
		}
		
		currentBlock.move(-1);
	}
	
	
	public int collissionDetected() {
		
		int bottomY = currentBlock.bottomY();
		int currentX = currentBlock.x; //getCurrentX();
		
		for (int i = 0; i < COLS; i++) {
			
			if (bottomY >= topMenuSize + (Block.height * COLS) && blockMap[currentX][COLS - 1].getType() == 0) {
				return COLS;
			}
			
			if (bottomY >= blockMap[currentX][i].posY && blockMap[currentX][i].getType() > 0) {
				return i;	
			}
		}
		
		return -1;
	}
	
	public void incrementScore(int size) {
		score += (int)( size * scoreMultiplier(blockSpeed) );
	}
	
	public static double scoreMultiplier(int speed) {
		switch (speed) {
			case 2:
				return 1;
			case 4:
				return 1.5;
			case 6:
				return 2;
			case 8:
				return 3;
			case 10:
				return 4;
			default:
				return 1;
		}
	}
	
	public void endGame() {
		
		blockMap = new Block[ROWS][COLS];
		blocks.removeAll(blocks);
		game.endScreen.setHighscore(score);
		game.setGameState(STATE.EndScreen);
	}
	
	public void generateNextBlock() {
		nextBlock = (int) Math.max(2, Math.pow(2, rng.nextInt(maxNumberIndex)));
		if (nextBlock > maxNumberOnBoard) {
			maxNumberOnBoard = nextBlock;
		}
	}
	
	public void setBlockSpeed(int newSpeed) {
		blockSpeed = newSpeed;
	}
	
	public void reset() {
		score = 0;
		maxNumberOnBoard = 0;
		
	}
	
	public String highScoreString() {
		if (score > game.endScreen.previousHighScore) {
			return "High Score: " + score + "  (" + game.menu.getPlayer() + ")";
		} else {
			return "High Score: " + game.endScreen.previousHighScore + "  (" + game.endScreen.previousHighScorePlayer + ")";
		}
	}
	
}
