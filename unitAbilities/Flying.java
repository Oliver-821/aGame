package unitAbilities;

import akka.actor.ActorRef;
import commands.BasicCommands;
import structures.Board;
import structures.GameState;
import structures.basic.Tile;


/**
 * unit could move anywhere on the board 
 */
public class Flying implements Ability{

	@Override
	public void execute(int x, int y, GameState gameState, ActorRef out) {
		highlight(x, y, gameState, out);
	}
	
	/**
	 *unit move high light. unit could move anywhere if there is no unit on the tile
	 * 
	 *@param x :tile position x
	 *@param y :tile position y
	 *@param gameState : GameState
	 *@param out : ActorRef
	 *
	 */
	public void highlight(int x, int y, GameState gameState, ActorRef out) {
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
				} else {
					BasicCommands.drawTile(out, tile[i][j], 1);
					tile[i][j].setTileStatus(1);
					try {Thread.sleep(5);} catch (InterruptedException e) {e.printStackTrace();}
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
