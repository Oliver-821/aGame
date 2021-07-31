package events;

import com.fasterxml.jackson.databind.JsonNode;

import akka.actor.ActorRef;
import commands.AdvancedCommands;
import commands.BasicCommands;
import structures.AIPlayer;
import structures.AIThread;
import structures.GameState;
import structures.HumanPlayer;


/**
 * Indicates that the user has clicked an object on the game canvas, in this case
 * the end-turn button.
 * 
 * { 
 *   messageType = “endTurnClicked”
 * }
 * 
 *
 */
public class EndTurnClicked implements EventProcessor{
	private HumanPlayer human;
	private AIPlayer AI;
	@Override
	public void processEvent(ActorRef out, GameState gameState, JsonNode message) {
		
		human = gameState.getHuman();
		AI = gameState.getAI();
		human.clearhighlightCards(gameState, out);
		if(gameState.getGameState().equals("run")) {
			if(gameState.getIsHumanTurn()) {
				// human click the end turn. human need to draw one card and drain mana, AI plays 	
				human.darwCard(out);
				if(!human.getIsRemainCards()) {
					gameState.setGameAction("AI Win");
					BasicCommands.addPlayer1Notification(out, "Sorry, AI Win", 2);
				}else {
					human.drainMana(out);
					AdvancedCommands.clearHighlight(gameState, out);
					BasicCommands.addPlayer2Notification(out, "AI Turn", 2);
					try {Thread.sleep(2000);} catch (InterruptedException e) {e.printStackTrace();}
					AI.gainMana(gameState,out);
					// set turn as AI 
					gameState.setIsHumanTurn(false);
					AIThread AIthread = new AIThread(AI,gameState,out);
					Thread ai = new Thread(AIthread);
					ai.start();
					try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}
					AIthread.exit = true;
					gameState.getBoard().resetUnit();
					gameState.getBoard().countUnitTurn();
					gameState.setUnit(null);
				}
			} else 
				BasicCommands.addPlayer1Notification(out, "AI Turn", 2);
		}else 
			BasicCommands.addPlayer1Notification(out,gameState.getGameState() , 2);		
	}

}
