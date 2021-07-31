package cardAbilities;

import akka.actor.ActorRef;
import structures.GameState;

public interface cardAbility {
	/**
	 * 
	 * @param handPosition : on hand card position 
	 * @param gameState : GameState
	 * @param out : ActorRef
	 */
	public void execute(int handPosition, GameState gameState,ActorRef out);
}
