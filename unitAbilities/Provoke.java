package unitAbilities;

import akka.actor.ActorRef;
import commands.AdvancedCommands;
import commands.BasicCommands;
import structures.GameState;
import structures.basic.Tile;


/**
 * a unit with provoke, then adjacent units cannot move and can only attack units in range with provoke.
 */


public class Provoke {
	
	/**
	 *check if board has provoke or not and check the position
	 * 
	 *@param x :tile position x
	 *@param y :tile position y
	 *@param gameState : GameState
	 *@param out : ActorRef
	 *
	 */
	public static Boolean hasProvoke(int x, int y, GameState gameState, ActorRef out) {
		Tile[][] tile = gameState.getBoard().getTile();
		Boolean isHuman = gameState.getIsHumanTurn();
		Boolean provoke = false;
		for (int i = x - 1; i <= x + 1; i++) {
			for (int j = y - 1; j <= y + 1; j++) {
				if (i >= 0 && j >= 0 && i < 9 && j < 5) {
					if (tile[i][j].getUnit() != null){
						if(!isHuman) {
							if (tile[i][j].getUnit().getId() == 7 || tile[i][j].getUnit().getId() == 15
									|| tile[i][j].getUnit().getId() == 5 || tile[i][j].getUnit().getId() == 13) {
								provoke = true;
							} 
						}else {
							if (tile[i][j].getUnit().getId() == 35 ||tile[i][j].getUnit().getId() == 43 ) {
								provoke = true;
							}
							
						}

					}
				}
			}
		}
		return provoke;
	}
	
	/**
	 *draw provoke. if there is provoke , could only attack provoke 
	 * 
	 *@param x :tile position x
	 *@param y :tile position y
	 *@param gameState : GameState
	 *@param out : ActorRef
	 *
	 */
	public static void drawProvoke(int x, int y, GameState gameState, ActorRef out) {
		Tile[][] tile = gameState.getBoard().getTile();
		Boolean isHuman = gameState.getIsHumanTurn();
		AdvancedCommands.addPlayerNotification(out, "only attack provoke", 2, isHuman);
		for (int i = x - 1; i <= x + 1; i++) {
			for (int j = y - 1; j <= y + 1; j++) {
				if (i >= 0 && j >= 0 && i < 9 && j < 5) {
					if (tile[i][j].getUnit() != null){
						if(!isHuman) {
							if (tile[i][j].getUnit().getId() == 7 || tile[i][j].getUnit().getId() == 15
									|| tile[i][j].getUnit().getId() == 5 || tile[i][j].getUnit().getId() == 13) {
								BasicCommands.drawTile(out, tile[i][j], 2);
								tile[i][j].setTileStatus(2);
								try {Thread.sleep(1);} catch (InterruptedException e) {e.printStackTrace();}			
							} 
						}else {
							if (tile[i][j].getUnit().getId() == 35 ||tile[i][j].getUnit().getId() == 43 ) {
								BasicCommands.drawTile(out, tile[i][j], 2);
								tile[i][j].setTileStatus(2);
								try {Thread.sleep(1);} catch (InterruptedException e) {e.printStackTrace();}
							}
							
						}

					}
				}
			}
		}
	}

}
