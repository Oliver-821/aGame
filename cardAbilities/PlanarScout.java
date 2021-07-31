package cardAbilities;

import akka.actor.ActorRef;
import commands.AdvancedCommands;
import commands.BasicCommands;
import structures.Board;
import structures.GameState;
import structures.basic.Player;
import structures.basic.Tile;

/**
 * card high light for planar scout. It could be summoned anywhere on the board
 * Planar Scout and Ironcliff Guardian both use this 
 */
public class PlanarScout implements cardAbility {

	@Override
	public void execute(int handPosition, GameState gameState, ActorRef out) {
		// TODO Auto-generated method stub
		summonHighlight(handPosition,gameState,out);

	}
	
	/**
	 * summon highlight for spell card 
	 * 
	 * @param handPosition : on hand card position 
	 * @param gameState : GameState
	 * @param out : ActorRef
	 */
	public void summonHighlight(int handPosition, GameState gameState, ActorRef out) {
		Board board = gameState.getBoard();
		Tile[][] tile = board.getTile();
		AdvancedCommands.clearHighlight(gameState, out);
		Player player = gameState.getCurrPlayer();
		AdvancedCommands.addPlayerNotification(out, "summon Highlight", 2, gameState.getIsHumanTurn());
		if(gameState.getIsHumanTurn() && player.getMana() <5) {
			BasicCommands.addPlayer1Notification(out, "no enough mana", 2);
		}else {
			for(int i = 0; i < 9 ; i++) {
				for(int j = 0; j < 5; j++) {
					if(tile[i][j].getUnit() == null) {
						BasicCommands.drawTile(out, tile[i][j], 1);
						tile[i][j].setTileStatus(1);
						try {Thread.sleep(5);} catch (InterruptedException e) {e.printStackTrace();}
					}
				}
			}
			gameState.setGameAction("clickCard");
			gameState.setHandPosition(handPosition);
		}

	}
}



