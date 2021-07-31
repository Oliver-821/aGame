package events;

import com.fasterxml.jackson.databind.JsonNode;

import akka.actor.ActorRef;
import commands.AdvancedCommands;
import commands.BasicCommands;
import structures.GameState;

/**
 * Indicates that the user has clicked an object on the game canvas, in this case
 * somewhere that is not on a card tile or the end-turn button.
 * 
 * { 
 *   messageType = “otherClicked”
 * }
 * 
 *
 */
public class OtherClicked implements EventProcessor{

	@Override
	public void processEvent(ActorRef out, GameState gameState, JsonNode message) {
		if(gameState.getGameState().equalsIgnoreCase("run")) {
			if(gameState.getIsHumanTurn()) {
				AdvancedCommands.clearHighlight(gameState, out);
				gameState.setGameAction("pending");
				gameState.setTempTile(null);
				gameState.setUnit(null);
				gameState.setHandPosition(-1);
			}else 
				BasicCommands.addPlayer1Notification(out, "AI Turn", 2);
		}else {
			BasicCommands.addPlayer1Notification(out,gameState.getGameState() , 2);
		}
		
	
		
	}

}


