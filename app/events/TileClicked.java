package events;


import com.fasterxml.jackson.databind.JsonNode;

import akka.actor.ActorRef;
import commands.BasicCommands;
import structures.GameState;


/**
 * Indicates that the user has clicked an object on the game canvas, in this case a tile.
 * The event returns the x (horizontal) and y (vertical) indices of the tile that was
 * clicked. Tile indices start at 1.
 * 
 * { 
 *   messageType = “tileClicked”
 *   tilex = <x index of the tile>
 *   tiley = <y index of the tile>
 * }
 * 
 * @author Dr. Richard McCreadie
 *
 */
public class TileClicked implements EventProcessor{

	@Override
	public void processEvent(ActorRef out, GameState gameState, JsonNode message) {
		
		int tilex = message.get("tilex").asInt();
		int tiley = message.get("tiley").asInt();
		
		if(gameState.getGameState().equalsIgnoreCase("run")) {
			if(gameState.getIsHumanTurn()) {
				gameState.getHuman().humanPlay(tilex, tiley, gameState, out);
			} else {
				BasicCommands.addPlayer1Notification(out, "AI Turn", 2);
			}
		}else {
			BasicCommands.addPlayer1Notification(out,gameState.getGameState() , 2);
		}

		

	}	
}


	
		
		
	


