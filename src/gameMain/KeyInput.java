package gameMain;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import gameMain.Game.STATE;

public class KeyInput extends KeyAdapter {

	
	public Game game;
	public char key;
	public int keyCode;
	
	public KeyInput(Game game) {
		this.game = game;
	}
	
	public void keyPressed(KeyEvent e) {
	
		key = e.getKeyChar();
		keyCode = e.getExtendedKeyCode();
		
		if (game.gameState == STATE.Menu) {
			
			if (key == 'a' || keyCode == KeyEvent.VK_ENTER) {
				game.menu.select();
			}
			
			if (keyCode == KeyEvent.VK_UP) {
				game.menu.decSelectedOptions();
			} else if (keyCode == KeyEvent.VK_DOWN) {
				game.menu.incSelectedOptions();
			}
			
			
		} else if (game.gameState == STATE.Game){
			
			if (keyCode == KeyEvent.VK_RIGHT) {
				game.board.moveRight();
			} else if (keyCode == KeyEvent.VK_LEFT) {
				game.board.moveLeft();
			}
			
		} else if (game.gameState == STATE.EndScreen) {
			
			if (keyCode == KeyEvent.VK_ENTER) {
				game.menu.reset();
				game.setGameState(STATE.Menu);
			}
		}
		
		
		
	}
}
