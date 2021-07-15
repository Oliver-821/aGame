package cardAbilities;

import akka.actor.ActorRef;
import commands.BasicCommands;
import structures.AIPlayer;
import structures.GameState;
import structures.HumanPlayer;

/**
 * both human and AI will draw a card
 */
public class BlazeHound implements cardAbility{

	@Override
	public void execute(int handPosition, GameState gameState, ActorRef out) {
		HumanPlayer human = gameState.getHuman();
		AIPlayer AI = gameState.getAI();
		BasicCommands.addPlayer1Notification(out, "draw a card", 2);
		human.darwCard(out);
		BasicCommands.addPlayer2Notification(out, "draw a card", 2);
		AI.darwCard(out);
	}
	

}
