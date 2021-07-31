package unitAbilities;

import akka.actor.ActorRef;
import commands.BasicCommands;
import structures.Board;
import structures.GameState;
import structures.basic.Tile;

/**
 * range attack of pyromancer
 */
public class Pyromancer implements Ability{

	@Override
	public void execute(int x, int y, GameState gameState, ActorRef out) {
		Board board = gameState.getBoard();
		Tile[][] tile = board.getTile();
		for(int i = 0; i < 9 ; i++) {
			for(int j = 0; j < 5; j++) {
				if(tile[i][j].getUnit() != null) {
					if(tile[i][j].getUnit().getId() < 30) {
						BasicCommands.drawTile(out, tile[i][j], 2);
						tile[i][j].setTileStatus(2);
						try {Thread.sleep(5);} catch (InterruptedException e) {e.printStackTrace();}
					}
				}
			}
		}
		gameState.setGameAction("clickUnit");
		gameState.setTempTile(tile[x][y]);
		gameState.setUnit(tile[x][y].getUnit());
		gameState.setPosX(x);
		gameState.setPosY(y);
		
	}
	
}
