package unitAbilities;

import akka.actor.ActorRef;
import structures.GameState;

public interface Ability {
	
	/**
	 *@param x :tile position x
	 *@param y :tile position y
	 *@param gameState : GameState
	 *@param out : ActorRef
	 *
	 */
	public void execute(int x, int y, GameState gameState,ActorRef out);
	
}
