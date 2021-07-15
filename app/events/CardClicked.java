package events;


import com.fasterxml.jackson.databind.JsonNode;

import akka.actor.ActorRef;
import commands.BasicCommands;
import structures.GameState;

/**
 * Indicates that the user has clicked an object on the game canvas, in this case a card.
 * The event returns the position in the player's hand the card resides within.
 * 
 * { 
 *   messageType = “cardClicked”
 *   position = <hand index position [1-6]>
 * }
 * 
 *
 */
public class CardClicked implements EventProcessor{

	@Override
	public void processEvent(ActorRef out, GameState gameState, JsonNode message) {
		
		int handPosition = message.get("position").asInt();
		if(gameState.getGameState().equalsIgnoreCase("run")) {
			if(gameState.getIsHumanTurn()) {
				gameState.getHuman().summonHighlight(handPosition, gameState, out);
			}else 
				BasicCommands.addPlayer1Notification(out, "AI Turn", 2);
		}else {
			BasicCommands.addPlayer1Notification(out, gameState.getGameState() , 2);
		}
		
		
	}

}
