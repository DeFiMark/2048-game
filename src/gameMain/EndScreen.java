package gameMain;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class EndScreen {

	private Game game;
	
	public int highscore;
	public int highestScore;
	
	
	public int previousHighScore;
	public String previousHighScorePlayer;
	
	public Scanner reader;
	
	public PrintWriter writer;
	
	public File file;
	
	public EndScreen(Game game) {
		
		this.game = game;
		
		try {
			file = new File("highscores");
			this.reader = new Scanner(file);
		
			if (reader.hasNextLine()) {
				String[] scoreLine = reader.nextLine().split(":");
				this.previousHighScorePlayer = scoreLine[0];
				this.previousHighScore = Integer.parseInt(scoreLine[1]);
			}
			reader.close();
		} catch (FileNotFoundException e) {
			System.out.println("could not find highscores");
		}
	}
	
	public void setHighscore(int highscore) {
		this.highscore = highscore;
		
		try {
			if (this.previousHighScore < highscore) {
				
				String player = game.menu.getPlayer();
				writer = new PrintWriter(file);
				writer.println(player + ":" + highscore);
				writer.close();
				this.highestScore = highscore;
			} else {
				this.highestScore = this.previousHighScore;
			}
		} catch (FileNotFoundException e) {
			System.out.println("could not find highscores");
		}
		
	}
	
	public String currentHighScorePlayer() {
		return (highscore == highestScore) ? game.menu.getPlayer() : previousHighScorePlayer;
	}
	
	public void render(Graphics g) {
		
		g.setColor(Color.red);
		g.setFont(new Font("Times New Roman", Font.BOLD, 60));
		g.drawString("GAME OVER", Game.WIDTH / 2 - 200, Game.HEIGHT / 2 - 100);
		g.drawString("Score: " + highscore, Game.WIDTH / 2 - 140, Game.HEIGHT / 2);
		g.drawString("HighScore: " + highestScore, Game.WIDTH / 2 - 200, Game.HEIGHT / 2 + 100);
		g.drawString(
				"Highest Score Player: " + currentHighScorePlayer(), 
				Game.WIDTH / 2 - 350, 
				Game.HEIGHT / 2 + 200
		);
		g.drawString("Press Enter To Play Again", Game.WIDTH / 2 - 350, Game.HEIGHT / 2 + 300);
	}
	
}
